package com.easychat.service.impl;

import com.easychat.entity.enums.BeautyAccountStatusEnum;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.enums.PageSize;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.UserInfoBeautyQuery;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserInfoBeautyMapper;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.service.UserInfoBeautyService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:靓号Service
 *@ Date:2024/11/21
 */
@Service("userInfoBeautyService") 
public class UserInfoBeautyServiceImpl implements UserInfoBeautyService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private UserInfoBeautyMapper<UserInfoBeauty,UserInfoBeautyQuery>userInfoBeautyMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	public List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query) {

		return this.userInfoBeautyMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(UserInfoBeautyQuery query) {

		return this.userInfoBeautyMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserInfoBeauty> list = this.findListByParam(query);
		PaginationResultVO<UserInfoBeauty> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(UserInfoBeauty bean) {

		return this.userInfoBeautyMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<UserInfoBeauty> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoBeautyMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<UserInfoBeauty> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoBeautyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据Id查询
	 */
	public UserInfoBeauty getUserInfoBeautyById(Integer id) {

		return this.userInfoBeautyMapper.selectById(id);
	}

	/**
	 *@ Description:根据Id更新
	 */
	public Integer updateUserInfoBeautyById(UserInfoBeauty bean,Integer id) {

		return this.userInfoBeautyMapper.updateById(bean, id);
	}

	/**
	 *@ Description:根据Id删除
	 */
	public Integer deleteUserInfoBeautyById(Integer id) {

		return this.userInfoBeautyMapper.deleteById(id);
	}

	/**
	 *@ Description:根据Email查询
	 */
	public UserInfoBeauty getUserInfoBeautyByEmail(String email) {

		return this.userInfoBeautyMapper.selectByEmail(email);
	}

	/**
	 *@ Description:根据Email更新
	 */
	public Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean,String email) {

		return this.userInfoBeautyMapper.updateByEmail(bean, email);
	}

	/**
	 *@ Description:根据Email删除
	 */
	public Integer deleteUserInfoBeautyByEmail(String email) {

		return this.userInfoBeautyMapper.deleteByEmail(email);
	}

	/**
	 *@ Description:根据UserId查询
	 */
	public UserInfoBeauty getUserInfoBeautyByUserId(String userId) {

		return this.userInfoBeautyMapper.selectByUserId(userId);
	}

	/**
	 *@ Description:根据UserId更新
	 */
	public Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean,String userId) {

		return this.userInfoBeautyMapper.updateByUserId(bean, userId);
	}

	/**
	 *@ Description:根据UserId删除
	 */
	public Integer deleteUserInfoBeautyByUserId(String userId) {

		return this.userInfoBeautyMapper.deleteByUserId(userId);
	}

	@Override
	public void saveAccount(UserInfoBeauty beauty) {
		if (beauty.getId() != null) {
			UserInfoBeauty dbInfo = userInfoBeautyMapper.selectById(beauty.getId());
			if (BeautyAccountStatusEnum.USED.getStatus().equals(dbInfo.getStatus()))
				throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		UserInfoBeauty dbInfo = userInfoBeautyMapper.selectByEmail(beauty.getEmail());
		// 新增时判断邮箱是否存在
		if (beauty.getId() == null && dbInfo != null)
			throw new BusinessException("靓号邮箱已经存在");

		// 修改时判断邮箱是否存在
		if (beauty.getId() != null && dbInfo != null && dbInfo.getId() != null && !dbInfo.getId().equals(beauty.getId()))
			throw new BusinessException("靓号邮箱已经存在");

		// 判断靓号是否存在
		dbInfo = userInfoBeautyMapper.selectByUserId(beauty.getUserId());
		if (beauty.getId() == null && dbInfo != null)
			throw new BusinessException("靓号已经存在");
		if (beauty.getId() != null && dbInfo != null && dbInfo.getId() != null && !dbInfo.getId().equals(beauty.getId()))
			throw new BusinessException("靓号已经存在");

		// 判断邮箱是否已注册
		UserInfo userInfo = userInfoMapper.selectByEmail(beauty.getEmail());
		if (userInfo != null)
			throw new BusinessException("靓号邮箱已注册");

		if (beauty.getId() != null)
			userInfoBeautyMapper.updateById(beauty, beauty.getId());
		else {
			beauty.setStatus(BeautyAccountStatusEnum.NO_USE.getStatus());
			userInfoBeautyMapper.insert(beauty);
		}

	}

}