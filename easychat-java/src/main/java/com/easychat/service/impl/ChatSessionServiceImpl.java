package com.easychat.service.impl;

import com.easychat.entity.query.SimplePage;
import com.easychat.entity.enums.PageSize;
import com.easychat.entity.po.ChatSession;
import com.easychat.entity.query.ChatSessionQuery;
import com.easychat.mappers.ChatSessionMapper;
import com.easychat.service.ChatSessionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:会话信息Service
 *@ Date:2024/12/26
 */
@Service("charSessionService") 
public class ChatSessionServiceImpl implements ChatSessionService {

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	public List<ChatSession> findListByParam(ChatSessionQuery query) {

		return this.chatSessionMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(ChatSessionQuery query) {

		return this.chatSessionMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSession> list = this.findListByParam(query);
		PaginationResultVO<ChatSession> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(ChatSession bean) {

		return this.chatSessionMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<ChatSession> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.chatSessionMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<ChatSession> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.chatSessionMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据SessionId查询
	 */
	public ChatSession getCharSessionBySessionId(String sessionId) {

		return this.chatSessionMapper.selectBySessionId(sessionId);
	}

	/**
	 *@ Description:根据SessionId更新
	 */
	public Integer updateCharSessionBySessionId(ChatSession bean, String sessionId) {

		return this.chatSessionMapper.updateBySessionId(bean, sessionId);
	}

	/**
	 *@ Description:根据SessionId删除
	 */
	public Integer deleteCharSessionBySessionId(String sessionId) {

		return this.chatSessionMapper.deleteBySessionId(sessionId);
	}

}