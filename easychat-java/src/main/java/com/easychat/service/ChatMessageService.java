package com.easychat.service;

import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.query.ChatMessageQuery;

import java.io.File;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:聊天消息表Service
 *@ Date:2024/12/26
 */
public interface ChatMessageService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<ChatMessage> findListByParam(ChatMessageQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(ChatMessageQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(ChatMessage bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<ChatMessage> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ChatMessage> listBean);

	/**
	 *@ Description:根据MessageId查询
	 */
	ChatMessage getChatMessageByMessageId(Long messageId);

	/**
	 *@ Description:根据MessageId更新
	 */
	Integer updateChatMessageByMessageId(ChatMessage bean,Long messageId);

	/**
	 *@ Description:根据MessageId删除
	 */
	Integer deleteChatMessageByMessageId(Long messageId);

	MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto);

	void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile coverFile);

	File downloadFile(TokenUserInfoDto userInfoDto, Long fileId, Boolean showCover);
}