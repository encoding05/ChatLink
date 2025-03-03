package com.easychat.service.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.*;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.ChatSessionUserMapper;
import com.easychat.mappers.UserContactMapper;
import com.easychat.mappers.UserInfoBeautyMapper;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.service.UserContactService;
import com.easychat.service.UserInfoService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import com.easychat.websocket.MessageHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:用户信息表Service
 *@ Date:2024/11/21
 */
@Service("userInfoService") 
public class UserInfoServiceImpl implements UserInfoService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery>userInfoMapper;

	@Resource
	private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private UserContactService userContactService;

	@Resource
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

	@Resource
	private MessageHandler messageHandler;


	public List<UserInfo> findListByParam(UserInfoQuery query) {

		return this.userInfoMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(UserInfoQuery query) {

		return this.userInfoMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(UserInfo bean) {

		return this.userInfoMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<UserInfo> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据UserId查询
	 */
	public UserInfo getUserInfoByUserId(String userId) {

		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 *@ Description:根据UserId更新
	 */
	public Integer updateUserInfoByUserId(UserInfo bean,String userId) {

		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 *@ Description:根据UserId删除
	 */
	public Integer deleteUserInfoByUserId(String userId) {

		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 *@ Description:根据Email查询
	 */
	public UserInfo getUserInfoByEmail(String email) {

		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 *@ Description:根据Email更新
	 */
	public Integer updateUserInfoByEmail(UserInfo bean,String email) {

		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 *@ Description:根据Email删除
	 */
	public Integer deleteUserInfoByEmail(String email) {

		return this.userInfoMapper.deleteByEmail(email);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String password, String nickName) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (userInfo != null){
			throw new BusinessException("该邮箱已被注册");
		}

		String userId = StringTools.getUserId();
		UserInfoBeauty beautyAccount = this.userInfoBeautyMapper.selectByEmail(email);
		boolean useBeautyAccount = beautyAccount != null && BeautyAccountStatusEnum.NO_USE.getStatus().equals(beautyAccount.getStatus());
		if (useBeautyAccount) {
			userId = UserContactTypeEnum.USER.getPrefix() + beautyAccount.getUserId();
		}

		Date curDate = new Date();
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setNickName(nickName);
		userInfo.setEmail(email);
		userInfo.setJoinType(JoinTypeEnum.APPLY.getType());
		userInfo.setPassword(StringTools.encodeMd5(password));
		userInfo.setCreateTime(curDate);
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setLastLoginTime(curDate);
		this.userInfoMapper.insert(userInfo);

		if (useBeautyAccount) {
			UserInfoBeauty updateBeauty = new UserInfoBeauty();
			updateBeauty.setStatus(BeautyAccountStatusEnum.USED.getStatus());
			this.userInfoBeautyMapper.updateById(updateBeauty, beautyAccount.getId());
		}

		// 创建机器人好友
		userContactService.addContact4Robot(userId);
	}

	@Override
	public UserInfoVO login(String email, String password) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);

		if (userInfo == null || !userInfo.getPassword().equals(StringTools.encodeMd5(password)))
			throw new BusinessException("用户名或密码错误");
		if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus()))
			throw new BusinessException("用户已被禁用");

		// 查询联系人
		UserContactQuery contactQuery = new UserContactQuery();
		contactQuery.setUserId(userInfo.getUserId());
		contactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		List<UserContact> contactList = userContactMapper.selectList(contactQuery);
		List<String> contactIdList = contactList.stream().map(UserContact::getContactId).collect(Collectors.toList());
		redisComponent.clearUserContact(userInfo.getUserId());
		if (!contactIdList.isEmpty())
			redisComponent.addUserContactBatch(userInfo.getUserId(), contactIdList);


        TokenUserInfoDto tokenUserInfoDto =  getTokenUserInfoDto(userInfo);

		Long lastHeartBeat = redisComponent.getUserHeartBeat(userInfo.getUserId());

		if (lastHeartBeat != null)
			throw new BusinessException("用户已登录");

		// 保存信息到Redis
		String token = StringTools.encodeMd5(tokenUserInfoDto.getUserId() + StringTools.getRandomString(Constants.LENTH_20));
		tokenUserInfoDto.setToken(token);
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);
		UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
		userInfoVO.setToken(tokenUserInfoDto.getToken());
		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());

		return userInfoVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserInfo(UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarFileCover) throws IOException {
		if (avatarFile != null) {
			String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
			File targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
			if (!targetFileFolder.exists()) {
				targetFileFolder.mkdirs();
			}
			String filePath = targetFileFolder.getPath() + "/" + userInfo.getUserId();
			avatarFile.transferTo(new File(filePath + Constants.IMAGE_SUFFIX));
			avatarFileCover.transferTo(new File(filePath + Constants.COVER_IMAGE_SUFFIX));
		}
		UserInfo dbInfo = userInfoMapper.selectByUserId(userInfo.getUserId());
		userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());
		String contactNameUpdate = null;
		if (!dbInfo.getNickName().equals(userInfo.getNickName()))
			contactNameUpdate = userInfo.getNickName();
		if (contactNameUpdate == null)
			return;

		TokenUserInfoDto tokenUserInfoDtoByUserId = redisComponent.getTokenUserInfoDtoByUserId(userInfo.getUserId());
		tokenUserInfoDtoByUserId.setNickName(contactNameUpdate);
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDtoByUserId);

		ChatSessionUser updateInfo = new ChatSessionUser();
		updateInfo.setContactName(userInfo.getNickName());

		ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
		chatSessionUserQuery.setContactId(userInfo.getUserId());
		chatSessionUserMapper.updateByParam(updateInfo, chatSessionUserQuery);

		UserContactQuery userContactQuery = new UserContactQuery();
		userContactQuery.setContactId(userInfo.getUserId());
		userContactQuery.setContactType(UserContactTypeEnum.USER.getType());
		userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		List<UserContact> userContactList = userContactMapper.selectList(userContactQuery);
		for (UserContact userContact : userContactList) {
			MessageSendDto messageSendDto = new MessageSendDto();
			messageSendDto.setContactType(UserContactTypeEnum.USER.getType());
			messageSendDto.setContactId(userContact.getUserId());
			messageSendDto.setExtendData(contactNameUpdate);
			messageSendDto.setSendUserId(userInfo.getUserId());
			messageSendDto.setSendUserNickName(userInfo.getNickName());
			messageSendDto.setMessageType(MessageTypeEnum.GROUP_NAME_UPDATE.getType());
			messageHandler.sendMessage(messageSendDto);
		}
	}

	@Override
	public void updateUserStatus(Integer status, String userId) {
		UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
		if (userStatusEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		UserInfo userInfo = new UserInfo();
		userInfo.setStatus(userStatusEnum.getStatus());
		userInfoMapper.updateByUserId(userInfo, userId);
	}
	@Override
	public void forceOffLine(String userId) {
		MessageSendDto messageSendDto = new MessageSendDto();
		messageSendDto.setContactType(UserContactTypeEnum.USER.getType());
		messageSendDto.setMessageType(MessageTypeEnum.FORCE_OFF_LINE.getType());
		messageSendDto.setContactId(userId);
		messageHandler.sendMessage(messageSendDto);
	}


	private TokenUserInfoDto getTokenUserInfoDto(UserInfo userInfo) {
		TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
		tokenUserInfoDto.setUserId(userInfo.getUserId());
		tokenUserInfoDto.setNickName(userInfo.getNickName());

		String[] adminEmails = appConfig.getAdminEmails().split(",");
        tokenUserInfoDto.setAdmin(adminEmails.length != 0 && ArrayUtils.contains(adminEmails, userInfo.getEmail()));

		return tokenUserInfoDto;
	}
}