package com.easychat.service.impl;

import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserContactApplyMapper;
import com.easychat.mappers.UserContactMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.service.UserContactApplyService;
import com.easychat.service.UserContactService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.transaction.annotation.Transactional;

/**
 *@ Description:用户联系人申请表Service
 *@ Date:2024/12/02
 */
@Service("userContactApplyService") 
public class UserContactApplyServiceImpl implements UserContactApplyService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private UserContactApplyMapper<UserContactApply,UserContactApplyQuery> userContactApplyMapper;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private UserContactService userContactService;

	public List<UserContactApply> findListByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserContactApply> list = this.findListByParam(query);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(UserContactApply bean) {

		return this.userContactApplyMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<UserContactApply> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactApplyMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<UserContactApply> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据ApplyId查询
	 */
	public UserContactApply getUserContactApplyByApplyId(Integer applyId) {

		return this.userContactApplyMapper.selectByApplyId(applyId);
	}

	/**
	 *@ Description:根据ApplyId更新
	 */
	public Integer updateUserContactApplyByApplyId(UserContactApply bean,Integer applyId) {

		return this.userContactApplyMapper.updateByApplyId(bean, applyId);
	}

	/**
	 *@ Description:根据ApplyId删除
	 */
	public Integer deleteUserContactApplyByApplyId(Integer applyId) {

		return this.userContactApplyMapper.deleteByApplyId(applyId);
	}

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	public UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	public Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean,String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
	}

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	public Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealWithApply(String userId, Integer applyId, Integer status) {
		UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getByStatus(status);

		if (statusEnum == null || UserContactApplyStatusEnum.INIT == statusEnum)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		UserContactApply applyInfo = this.userContactApplyMapper.selectByApplyId(applyId);
		if (applyInfo == null || !userId.equals(applyInfo.getReceiveUserId()))
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		UserContactApply updateInfo = new UserContactApply();
		updateInfo.setStatus(statusEnum.getStatus());
		updateInfo.setLastApplyTime(System.currentTimeMillis());

		UserContactApplyQuery applyQuery = new UserContactApplyQuery();
		applyQuery.setApplyId(applyId);
		applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());

		Integer count = userContactApplyMapper.updateByParam(updateInfo, applyQuery);
		if (count == 0)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		if (UserContactApplyStatusEnum.PASS.getStatus().equals(status)) {
			userContactService.addContact(applyInfo.getApplyUserId(), applyInfo.getReceiveUserId(), applyInfo.getContactId(), applyInfo.getContactType(), applyInfo.getApplyInfo());
			return;
		}

		if (UserContactApplyStatusEnum.BALCKLIST == statusEnum) {
			Date curDate = new Date();
			UserContact userContact = new UserContact();
			userContact.setUserId(applyInfo.getApplyUserId());
			userContact.setContactId(applyInfo.getContactId());
			userContact.setContactType(applyInfo.getContactType());
			userContact.setCreateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
			userContact.setLastUpdateTime(curDate);
			userContactMapper.insertOrUpdate(userContact);
		}
	}
}