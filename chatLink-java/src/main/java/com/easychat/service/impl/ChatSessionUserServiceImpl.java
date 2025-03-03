package com.easychat.service.impl;

import com.easychat.entity.query.SimplePage;
import com.easychat.entity.enums.PageSize;
import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.mappers.ChatSessionUserMapper;
import com.easychat.service.ChatSessionUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:会话用户Service
 *@ Date:2024/12/26
 */
@Service("chatSessionUserService") 
public class ChatSessionUserServiceImpl implements ChatSessionUserService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery>chatSessionUserMapper;

	public List<ChatSessionUser> findListByParam(ChatSessionUserQuery query) {

		return this.chatSessionUserMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(ChatSessionUserQuery query) {

		return this.chatSessionUserMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSessionUser> list = this.findListByParam(query);
		PaginationResultVO<ChatSessionUser> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(ChatSessionUser bean) {

		return this.chatSessionUserMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<ChatSessionUser> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.chatSessionUserMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<ChatSessionUser> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.chatSessionUserMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据UserIdAndContactId查询
	 */
	public ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId) {

		return this.chatSessionUserMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 *@ Description:根据UserIdAndContactId更新
	 */
	public Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean,String userId, String contactId) {

		return this.chatSessionUserMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 *@ Description:根据UserIdAndContactId删除
	 */
	public Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId) {

		return this.chatSessionUserMapper.deleteByUserIdAndContactId(userId, contactId);
	}

}