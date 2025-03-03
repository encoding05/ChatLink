package com.easychat.service.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.*;
import com.easychat.entity.query.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.*;
import com.easychat.redis.RedisComponent;
import com.easychat.service.GroupInfoService;
import com.easychat.service.UserContactService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import com.easychat.websocket.ChannelContextUtils;
import com.easychat.websocket.MessageHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:Service
 *@ Date:2024/12/02
 */
@Service("groupInfoService") 
public class GroupInfoServiceImpl implements GroupInfoService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private GroupInfoMapper<GroupInfo,GroupInfoQuery>groupInfoMapper;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private AppConfig appConfig;

	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	@Resource
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

	@Resource
	private MessageHandler messageHandler;

	@Resource
	private ChannelContextUtils channelContextUtils;

	@Resource
	private UserContactService userContactService;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	@Lazy
	private GroupInfoService groupInfoService;


	public List<GroupInfo> findListByParam(GroupInfoQuery query) {

		return this.groupInfoMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(GroupInfoQuery query) {

		return this.groupInfoMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<GroupInfo> list = this.findListByParam(query);
		PaginationResultVO<GroupInfo> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(GroupInfo bean) {

		return this.groupInfoMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<GroupInfo> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.groupInfoMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<GroupInfo> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.groupInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据GroupId查询
	 */
	public GroupInfo getGroupInfoByGroupId(String groupId) {

		return this.groupInfoMapper.selectByGroupId(groupId);
	}

	/**
	 *@ Description:根据GroupId更新
	 */
	public Integer updateGroupInfoByGroupId(GroupInfo bean,String groupId) {

		return this.groupInfoMapper.updateByGroupId(bean, groupId);
	}

	/**
	 *@ Description:根据GroupId删除
	 */
	public Integer deleteGroupInfoByGroupId(String groupId) {

		return this.groupInfoMapper.deleteByGroupId(groupId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
		Date curDate = new Date();
		// 新增
		if (StringTools.isEmpty(groupInfo.getGroupId())) {
			GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
			groupInfoQuery.setGroupOwnerId(groupInfo.getGroupOwnerId());
			Integer count = this.groupInfoMapper.selectCount(groupInfoQuery);
			SysSettingDto sysSettingDto = this.redisComponent.getSysSetting();
			if (count >= sysSettingDto.getMaxGroupCount())
				throw new BusinessException("最多支持创建" + sysSettingDto.getMaxGroupCount() + "个群组");

			if (null == avatarFile)
				throw new BusinessException(ResponseCodeEnum.CODE_600);

			groupInfo.setCreateTime(curDate);
			groupInfo.setGroupId(StringTools.getGroupId());
			this.groupInfoMapper.insert(groupInfo);

			// 将群组添加为联系人
			UserContact userContact = new UserContact();
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			userContact.setContactType(UserContactTypeEnum.GROUP.getType());
			userContact.setContactId(groupInfo.getGroupId());
			userContact.setUserId(groupInfo.getGroupOwnerId());
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			this.userContactMapper.insert(userContact);

			// 创建会话
			String sessionId = StringTools.getChatSessionId4Group(groupInfo.getGroupId());
			ChatSession chatSession = new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatSession.setLastReceiveTime(curDate.getTime());
			chatSessionMapper.insertOrUpdate(chatSession);

			ChatSessionUser chatSessionUser = new ChatSessionUser();
			chatSessionUser.setUserId(groupInfo.getGroupOwnerId());
			chatSessionUser.setContactId(groupInfo.getGroupId());
			chatSessionUser.setContactName(groupInfo.getGroupName());
			chatSessionUser.setSessionId(sessionId);
			chatSessionUserMapper.insert(chatSessionUser);

			// 创建消息
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.GROUP_CREATE.getType());
			chatMessage.setMessageContent(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(groupInfo.getGroupId());
			chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
			chatMessage.setStatus(MessageStatusEnum.SEND.getStatus());
			chatMessageMapper.insert(chatMessage);

			// 将群组添加到联系人
			redisComponent.addUserContact(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());
			// 将联系人通道添加到群组通道
			channelContextUtils.addUser2Group(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());

			// 发送消息
			chatSessionUser.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatSessionUser.setLastReceiveTime(curDate.getTime());
			chatSessionUser.setMemberCount(1);

			MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
			messageSendDto.setExtendData(chatSessionUser);
			messageSendDto.setContactName(groupInfo.getGroupName());
			messageSendDto.setLastMessage(chatSessionUser.getLastMessage());

			messageHandler.sendMessage(messageSendDto);

		} else {
			GroupInfo dbInfo = this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
			if (!dbInfo.getGroupOwnerId().equals(groupInfo.getGroupOwnerId()))
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			this.groupInfoMapper.updateByGroupId(groupInfo, groupInfo.getGroupId());

			// 更新相关表的冗余信息
			String contactNameUpdate = null;
			if (!dbInfo.getGroupName().equals(groupInfo.getGroupName()))
				contactNameUpdate = groupInfo.getGroupName();

			if (contactNameUpdate == null)
				return;

			ChatSessionUser updateInfo = new ChatSessionUser();
			updateInfo.setContactName(groupInfo.getGroupName());

			ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
			chatSessionUserQuery.setContactId(groupInfo.getGroupId());
			chatSessionUserMapper.updateByParam(updateInfo, chatSessionUserQuery);

			MessageSendDto messageSendDto = new MessageSendDto();
			messageSendDto.setContactType(UserContactTypeEnum.GROUP.getType());
			messageSendDto.setContactId(groupInfo.getGroupId());
			messageSendDto.setExtendData(contactNameUpdate);
			messageSendDto.setMessageType(MessageTypeEnum.GROUP_NAME_UPDATE.getType());
			messageHandler.sendMessage(messageSendDto);

		}
		if (null == avatarFile)
			return;

		String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
		File targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
		if (!targetFileFolder.exists())
			targetFileFolder.mkdirs();
		String filePath = targetFileFolder.getPath() + "/" + groupInfo.getGroupId();

		avatarFile.transferTo(new File(filePath + Constants.IMAGE_SUFFIX));
		avatarCover.transferTo(new File(filePath + Constants.COVER_IMAGE_SUFFIX));
	}

	@Override
	public void dissolutionGroup(String groupOwnerId, String groupId) {
		GroupInfo groupInfo = groupInfoMapper.selectByGroupId(groupId);
		if (groupInfo == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		// 删除群组
		GroupInfo updateInfo = new GroupInfo();
		updateInfo.setStatus(GroupStatusEnum.DISSOLUTION.getStatus());
		groupInfoMapper.updateByGroupId(updateInfo, groupId);

		// 更新联系人信息
		UserContactQuery userContactQuery = new UserContactQuery();
		userContactQuery.setContactId(groupId);
		userContactQuery.setContactType(UserContactTypeEnum.GROUP.getType());

		UserContact updateUserContact = new UserContact();
		updateUserContact.setStatus(UserContactStatusEnum.DEL.getStatus());
		userContactMapper.updateByParam(updateUserContact, userContactQuery);

		List<UserContact> userContactList = userContactMapper.selectList(userContactQuery);
		for (UserContact userContact : userContactList) {
			redisComponent.removeUserContact(userContact.getUserId(), groupId);
		}

		String sessionId = StringTools.getChatSessionId4Group(groupId);
		Date curDate = new Date();
		String messageContent = MessageTypeEnum.DISSOLUTION_GROUP.getInitMessage();

		ChatSession chatSession = new ChatSession();
		chatSession.setLastMessage(messageContent);
		chatSession.setLastReceiveTime(curDate.getTime());
		chatSessionMapper.updateBySessionId(chatSession, sessionId);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
		chatMessage.setStatus(MessageStatusEnum.SEND.getStatus());
		chatMessage.setMessageType(MessageTypeEnum.DISSOLUTION_GROUP.getType());
		chatMessage.setContactId(groupId);
		chatMessage.setMessageContent(messageContent);
		chatMessageMapper.insert(chatMessage);
		MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
		messageHandler.sendMessage(messageSendDto);
	}

	@Override
	public void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfoDto, String groupId, String contactIds, Integer opType) {
		GroupInfo groupInfo = groupInfoMapper.selectByGroupId(groupId);
		if (groupInfo == null || !groupInfo.getGroupOwnerId().equals(tokenUserInfoDto.getUserId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		String[] contactIdList = contactIds.split(",");
		for (String contactId : contactIdList) {
			if (opType.equals(0)){
				groupInfoService.leaveGroup(contactId, groupId, MessageTypeEnum.REMOVE_GROUP);
			} else {
				userContactService.addContact(contactId, null, groupId, UserContactTypeEnum.GROUP.getType(), null);
			}
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum) {
		GroupInfo groupInfo = groupInfoMapper.selectByGroupId(groupId);
		if (groupInfo == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		if (userId.equals(groupInfo.getGroupOwnerId()))
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		Integer count = userContactMapper.deleteByUserIdAndContactId(userId, groupId);
		if (count == 0)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		UserInfo userInfo = userInfoMapper.selectByUserId(userId);

		String sessionId = StringTools.getChatSessionId4Group(groupId);
		Date curDate = new Date();
		String messageContent = String.format(messageTypeEnum.getInitMessage(), userInfo.getNickName());

		ChatSession chatSession = new ChatSession();
		chatSession.setLastMessage(messageContent);
		chatSession.setLastReceiveTime(curDate.getTime());
		chatSessionMapper.updateBySessionId(chatSession, sessionId);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
		chatMessage.setStatus(MessageStatusEnum.SEND.getStatus());
		chatMessage.setMessageType(messageTypeEnum.getType());
		chatMessage.setContactId(groupId);
		chatMessage.setMessageContent(messageContent);
		chatMessageMapper.insert(chatMessage);

		UserContactQuery userContactQuery = new UserContactQuery();
		userContactQuery.setContactId(groupId);
		userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		Integer memberCount = userContactMapper.selectCount(userContactQuery);
		MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
		messageSendDto.setExtendData(userId);
		messageSendDto.setMemberCount(memberCount);
		messageHandler.sendMessage(messageSendDto);
	}

}