package com.easychat.service;

import com.easychat.entity.po.ChatSession;
import com.easychat.entity.query.ChatSessionQuery;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:会话信息Service
 *@ Date:2024/12/26
 */
public interface ChatSessionService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<ChatSession> findListByParam(ChatSessionQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(ChatSession bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<ChatSession> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ChatSession> listBean);

	/**
	 *@ Description:根据SessionId查询
	 */
	ChatSession getCharSessionBySessionId(String sessionId);

	/**
	 *@ Description:根据SessionId更新
	 */
	Integer updateCharSessionBySessionId(ChatSession bean, String sessionId);

	/**
	 *@ Description:根据SessionId删除
	 */
	Integer deleteCharSessionBySessionId(String sessionId);

}