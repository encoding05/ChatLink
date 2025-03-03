package com.easychat.service.impl;

import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.*;
import com.easychat.entity.query.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.*;
import com.easychat.redis.RedisComponent;
import com.easychat.service.UserContactApplyService;
import com.easychat.service.UserContactService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import com.easychat.websocket.ChannelContextUtils;
import com.easychat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.transaction.annotation.Transactional;

/**
 *@ Description:用户联系人表Service
 *@ Date:2024/12/02
 */
@Service("userContactService") 
public class UserContactServiceImpl implements UserContactService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private UserContactMapper<UserContact,UserContactQuery> userContactMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	@Resource
	private  ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

	@Resource
	private MessageHandler messageHandler;
    @Resource
    private ChannelContextUtils channelContextUtils;

	public List<UserContact> findListByParam(UserContactQuery query) {

		return this.userContactMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(UserContactQuery query) {

		return this.userContactMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<UserContact> findListByPage(UserContactQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserContact> list = this.findListByParam(query);
		PaginationResultVO<UserContact> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(UserContact bean) {

		return this.userContactMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<UserContact> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<UserContact> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据UserIdAndContactId查询
	 */
	public UserContact getUserContactByUserIdAndContactId(String userId, String contactId) {

		return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 *@ Description:根据UserIdAndContactId更新
	 */
	public Integer updateUserContactByUserIdAndContactId(UserContact bean,String userId, String contactId) {

		return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 *@ Description:根据UserIdAndContactId删除
	 */
	public Integer deleteUserContactByUserIdAndContactId(String userId, String contactId) {

		return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);
	}
	@Override
	public void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType, String applyInfo) {
		// 群聊人数
		if (UserContactTypeEnum.GROUP.getType().equals(contactType)) {
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			Integer count = userContactMapper.selectCount(userContactQuery);
			SysSettingDto sysSettingDto = redisComponent.getSysSetting();
			if (count >= sysSettingDto.getMaxGroupCount())
				throw new BusinessException("群聊人数已达上限");
		}

		Date curDate = new Date();
		//同意，双方添加好友
		List<UserContact> contactList = new ArrayList<>();
		// 申请人添加对方
		UserContact userContact = new UserContact();
		userContact.setUserId(applyUserId);
		userContact.setContactId(contactId);
		userContact.setContactType(contactType);
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		contactList.add(userContact);

		//如果是申请好友，接受人添加申请人，群组不需要
		if (UserContactTypeEnum.USER.getType().equals(contactType)) {
			userContact = new UserContact();
			userContact.setUserId(receiveUserId);
			userContact.setContactId(applyUserId);
			userContact.setContactType(contactType);
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			contactList.add(userContact);
		}
		// 批量插入
		userContactMapper.insertOrUpdateBatch(contactList);

		if (UserContactTypeEnum.USER.getType().equals(contactType)) {
			redisComponent.addUserContact(receiveUserId, applyUserId);
		}

		redisComponent.addUserContact(applyUserId, contactId);

		// 创建会话
		String sessionId = null;
		if (UserContactTypeEnum.GROUP.getType().equals(contactType)) {
			sessionId = StringTools.getChatSessionId4Group(contactId);
		} else {
			sessionId = StringTools.getChatSessionId4User(new String[]{applyUserId, contactId});
		}

		List<ChatSessionUser> chatSessionUserList = new ArrayList<>();
		if (UserContactTypeEnum.USER.getType().equals(contactType)) {
			// 创建会话
			ChatSession chatSession = new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastMessage(applyInfo);
			chatSession.setLastReceiveTime(curDate.getTime());
			chatSessionMapper.insertOrUpdate(chatSession);

			// 申请人session
			ChatSessionUser applySessionUser = new ChatSessionUser();
			applySessionUser.setUserId(applyUserId);
			applySessionUser.setContactId(contactId);
			applySessionUser.setSessionId(sessionId);
			UserInfo contactUser = userInfoMapper.selectByUserId(contactId);
			applySessionUser.setContactName(contactUser.getNickName());
			chatSessionUserList.add(applySessionUser);

			// 接收人session
			ChatSessionUser receiveSessionUser = new ChatSessionUser();
			receiveSessionUser.setUserId(contactId);
			receiveSessionUser.setContactId(applyUserId);
			receiveSessionUser.setSessionId(sessionId);
			UserInfo applyUser = userInfoMapper.selectByUserId(applyUserId);
			receiveSessionUser.setContactName(applyUser.getNickName());
			chatSessionUserList.add(receiveSessionUser);
	        chatSessionUserMapper.insertOrUpdateBatch(chatSessionUserList);

			// 记录消息表
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
			chatMessage.setMessageContent(applyInfo);
			chatMessage.setSendUserId(applyUserId);
			chatMessage.setSendUserNickName(applyUser.getNickName());
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContactTypeEnum.USER.getType());
			chatMessageMapper.insert(chatMessage);

			MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
			// 发送给接收还有申请的人

			messageHandler.sendMessage(messageSendDto);

			// 发送给申请人，发送人就是接收人，联系人就是申请人
			messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND_SELF.getType());
			messageSendDto.setContactId(applyUserId);
			messageSendDto.setExtendData(contactUser);
			messageHandler.sendMessage(messageSendDto);
		} else {
			// 加入群组
			ChatSessionUser chatSessionUser = new ChatSessionUser();
			chatSessionUser.setUserId(applyUserId);
			chatSessionUser.setContactId(contactId);
			GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
			chatSessionUser.setContactName(groupInfo.getGroupName());
			chatSessionUser.setSessionId(sessionId);
			chatSessionUserMapper.insertOrUpdate(chatSessionUser);

			UserInfo applyUser = userInfoMapper.selectByUserId(applyUserId);
			String sendMessage = String.format(MessageTypeEnum.ADD_GROUP.getInitMessage(), applyUser.getNickName());
			// 增加session 信息
			ChatSession chatSession = new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastReceiveTime(curDate.getTime());
			chatSession.setLastMessage(sendMessage);
			chatSessionMapper.insertOrUpdate(chatSession);

			// 增加聊天信息
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_GROUP.getType());
			chatMessage.setMessageContent(sendMessage);
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
			chatMessage.setStatus(MessageStatusEnum.SEND.getStatus());
			chatMessageMapper.insert(chatMessage);


			// 将群组添加到联系人
			redisComponent.addUserContact(applyUserId, groupInfo.getGroupId());

			// 将联系人通道添加到群组通道
			channelContextUtils.addUser2Group(applyUserId, groupInfo.getGroupId());

			// 发送消息
			MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
			messageSendDto.setContactId(contactId);

			// 获取群组成员数量
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			Integer memberCount = userContactMapper.selectCount(userContactQuery);
			messageSendDto.setMemberCount(memberCount);
			messageSendDto.setContactName(groupInfo.getGroupName());
			messageHandler.sendMessage(messageSendDto);
		}
	}

	@Override
	public UserContactSearchResultDto searchContact(String userId, String contactId) {
		UserContactTypeEnum typeEnum = UserContactTypeEnum.getByPrefix(contactId);
		if (typeEnum == null) {
			return null;
		}

		UserContactSearchResultDto resultDto = new UserContactSearchResultDto();
		switch (typeEnum) {
			case USER:
				UserInfo userInfo = this.userInfoMapper.selectByUserId(contactId);
				if (userInfo == null) {
					return null;
				}
				resultDto = CopyTools.copy(userInfo, UserContactSearchResultDto.class);
				break;
			case GROUP:
				GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
				if (groupInfo == null) {
					return null;
				}
				resultDto.setNickName(groupInfo.getGroupName());
		}
		resultDto.setContactType(typeEnum.toString());
		resultDto.setContactId(contactId);

		if (userId.equals(contactId)) {
			resultDto.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			return resultDto;
		}
		//查询是否是好友
		UserContact userContact = this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
		resultDto.setStatus(userContact == null ? null : userContact.getStatus());
		return resultDto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String applyInfo) {
		UserContactTypeEnum typeEnum = UserContactTypeEnum.getByPrefix(contactId);
		if (typeEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		// 申请人
		String applyUserId = tokenUserInfoDto.getUserId();
		//默认申请信息
		applyInfo = StringTools.isEmpty(applyInfo) ? String.format(Constants.APPLY_INFO_TEMPLATE, tokenUserInfoDto.getNickName()) : applyInfo;

		Long curTime = System.currentTimeMillis();

		Integer joinType = null;
		String recieveUserId = contactId;

		// 查询对方好友是否已经添加，如果拉黑无法添加
		UserContact userContact = userContactMapper.selectByUserIdAndContactId(applyUserId, contactId);
		if (userContact != null && UserContactStatusEnum.BLACKLIST_BE.getStatus().equals(userContact.getStatus()))
			throw new BusinessException("对方已将您拉黑，无法添加");

		if (typeEnum == UserContactTypeEnum.GROUP) {
			GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
			if (groupInfo == null || GroupStatusEnum.DISSOLUTION.getStatus().equals(groupInfo.getStatus()))
				throw new BusinessException("群组不存在或已解散");
			recieveUserId = groupInfo.getGroupOwnerId();
			joinType = groupInfo.getJoinType();
		} else {
			UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
			if (userInfo == null)
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			joinType = userInfo.getJoinType();
		}
		if (JoinTypeEnum.JOIN.getType().equals(joinType)) {
			addContact(applyUserId, recieveUserId, contactId, typeEnum.getType(), applyInfo);

			return joinType;
		}

		UserContactApply dbApply = this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, recieveUserId, contactId);
		if (dbApply == null) {
			UserContactApply userContactApply = new UserContactApply();
			userContactApply.setApplyUserId(applyUserId);
			userContactApply.setContactType(typeEnum.getType());
			userContactApply.setReceiveUserId(recieveUserId);
			userContactApply.setLastApplyTime(curTime);
			userContactApply.setContactId(contactId);
			userContactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			userContactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.insert(userContactApply);
		} else {
			// 更新申请信息
			UserContactApply userContactApply = new UserContactApply();
			userContactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			userContactApply.setLastApplyTime(curTime);
			userContactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.updateByApplyId(userContactApply, dbApply.getApplyId());
		}

		if (dbApply == null || !UserContactApplyStatusEnum.INIT.getStatus().equals(dbApply.getStatus())){
			MessageSendDto messageSendDto = new MessageSendDto();
			messageSendDto.setMessageType(MessageTypeEnum.CONTACT_APPLY.getType());
			messageSendDto.setMessageContent(applyInfo);
			messageSendDto.setContactId(recieveUserId);
			messageHandler.sendMessage(messageSendDto);
		}
		return joinType;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeUserContact(String userId, String contactId, UserContactStatusEnum statusEnum) {
		// 移除好友
		UserContact userContact = new UserContact();
		userContact.setStatus(statusEnum.getStatus());
		userContactMapper.updateByUserIdAndContactId(userContact, userId, contactId);

		// 将好友中也移除自己
		UserContact friendContact = new UserContact();
		if (UserContactStatusEnum.DEL == statusEnum){
			friendContact.setStatus(UserContactStatusEnum.DEL_BE.getStatus());
		} else if (UserContactStatusEnum.BLACKLIST == statusEnum){
			friendContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
		}
		userContactMapper.updateByUserIdAndContactId(friendContact, contactId, userId);

		redisComponent.removeUserContact(userId, contactId);
		redisComponent.removeUserContact(contactId, userId);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addContact4Robot(String userId) {
		Date curDate = new Date();
		SysSettingDto sysSettingDto = redisComponent.getSysSetting();
		String contactId = sysSettingDto.getRobotUid();
		String contactName = sysSettingDto.getRobotNickName();
		String sendMessage = sysSettingDto.getRobotWelcome();
		sendMessage = StringTools.cleanHtmlTag(sendMessage);

		// 添加机器人好友
		UserContact userContact = new UserContact();
		userContact.setUserId(userId);
		userContact.setContactId(contactId);
		userContact.setContactType(UserContactTypeEnum.USER.getType());
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		userContactMapper.insert(userContact);
		// 增加会话信息

		String sessionId = StringTools.getChatSessionId4User(new String[]{userId, contactId});
		ChatSession chatSession = new ChatSession();
		chatSession.setLastMessage(sendMessage);
		chatSession.setSessionId(sessionId);
		chatSession.setLastReceiveTime(curDate.getTime()); // ms
		chatSessionMapper.insert(chatSession);

		// 增加会话用户信息
		ChatSessionUser chatSessionUser = new ChatSessionUser();
		chatSessionUser.setUserId(userId);
		chatSessionUser.setContactId(contactId);
		chatSessionUser.setContactName(contactName);
		chatSessionUser.setSessionId(sessionId);
		chatSessionUserMapper.insert(chatSessionUser);

		// 增加聊天信息
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setMessageType(MessageTypeEnum.CHAT.getType());
		chatMessage.setMessageContent(sendMessage);
		chatMessage.setSendUserId(contactId);
		chatMessage.setSendUserNickName(contactName);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactId(userId);
		chatMessage.setContactType(UserContactTypeEnum.USER.getType());
		chatMessage.setStatus(MessageStatusEnum.SEND.getStatus());
		chatMessageMapper.insert(chatMessage);
	}
}