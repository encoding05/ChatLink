package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.UserContactStatusEnum;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.service.UserInfoService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import com.easychat.websocket.ChannelContextUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.List;
/**
 *@ Description:用户信息表Controller
 *@ Date:2024/11/21
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController {

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private ChannelContextUtils channelContextUtils;

	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(UserInfoQuery query) {
		return getSuccessResponseVO(userInfoService.findListByPage(query));
	}

	/**
	 *@ Description:新增
	 */
	@RequestMapping("add")
	public ResponseVO add(UserInfo bean) {
		this.userInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<UserInfo> listBean) {
		this.userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserInfo> listBean) {
		this.userInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:根据UserId查询
	 */
	@RequestMapping("getUserInfoByUserId")
	public ResponseVO getUserInfoByUserId(String userId) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByUserId(userId));
	}

	/**
	 *@ Description:根据UserId更新
	 */
	@RequestMapping("updateUserInfoByUserId")
	public ResponseVO updateUserInfoByUserId(UserInfo bean,String userId) {
		this.userInfoService.updateUserInfoByUserId(bean, userId);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:根据UserId删除
	 */
	@RequestMapping("deleteUserInfoByUserId")
	public ResponseVO deleteUserInfoByUserId(String userId) {
		this.userInfoService.deleteUserInfoByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:根据Email查询
	 */
	@RequestMapping("getUserInfoByEmail")
	public ResponseVO getUserInfoByEmail(String email) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByEmail(email));
	}

	/**
	 *@ Description:根据Email更新
	 */
	@RequestMapping("updateUserInfoByEmail")
	public ResponseVO updateUserInfoByEmail(UserInfo bean,String email) {
		this.userInfoService.updateUserInfoByEmail(bean, email);
		return getSuccessResponseVO(null);
	}

	/**
	 *@ Description:根据Email删除
	 */
	@RequestMapping("deleteUserInfoByEmail")
	public ResponseVO deleteUserInfoByEmail(String email) {
		this.userInfoService.deleteUserInfoByEmail(email);
		return getSuccessResponseVO(null);
	}


	@RequestMapping("/getUserInfo")
	@GlobalInterceptor
	public ResponseVO getUserInfo(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
		UserInfo userInfo = userInfoService.getUserInfoByUserId(tokenUserInfoDto.getUserId());
		UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
		return getSuccessResponseVO(userInfoVO);
	}

	@RequestMapping("/saveUserInfo")
	@GlobalInterceptor
	public ResponseVO saveUserInfo(HttpServletRequest request, UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarFileCover) throws IOException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
		userInfo.setUserId(tokenUserInfoDto.getUserId());
		userInfo.setPassword(null);
		userInfo.setStatus(null);
		userInfo.setCreateTime(null);
		userInfo.setLastLoginTime(null);

		userInfoService.updateUserInfo(userInfo, avatarFile, avatarFileCover);
		return getUserInfo(request);
	}

	@RequestMapping("/updatePassword")
	@GlobalInterceptor
	public ResponseVO updatePassword(HttpServletRequest request, @NotEmpty String password) {  // @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String password //带正则校验
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
		UserInfo userInfo = new UserInfo();
		userInfo.setPassword(StringTools.encodeMd5(password));
		userInfoService.updateUserInfoByUserId(userInfo, tokenUserInfoDto.getUserId());

		channelContextUtils.closeContext(tokenUserInfoDto.getUserId());

		return getSuccessResponseVO(null);
	}

	@RequestMapping("/logout")
	@GlobalInterceptor
	public ResponseVO logout(HttpServletRequest request) {  // @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String password //带正则校验
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
		channelContextUtils.closeContext(tokenUserInfoDto.getUserId());

		return getSuccessResponseVO(null);
	}
}