package com.easychat.service;

import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.query.ChatSessionUserQuery;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:会话用户Service
 *@ Date:2024/12/26
 */
public interface ChatSessionUserService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<ChatSessionUser> findListByParam(ChatSessionUserQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionUserQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(ChatSessionUser bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<ChatSessionUser> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ChatSessionUser> listBean);

	/**
	 *@ Description:根据UserIdAndContactId查询
	 */
	ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId);

	/**
	 *@ Description:根据UserIdAndContactId更新
	 */
	Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean,String userId, String contactId);

	/**
	 *@ Description:根据UserIdAndContactId删除
	 */
	Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId);

}