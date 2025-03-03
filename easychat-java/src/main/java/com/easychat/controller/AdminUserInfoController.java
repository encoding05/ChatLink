package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.service.UserInfoService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RestController("adminUserInfoController")
@RequestMapping("/admin")
public class AdminUserInfoController extends ABaseController {

	@Resource
	private UserInfoService userInfoService;


	@RequestMapping("/loadUser")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadUser(UserInfoQuery userInfoQuery){
		userInfoQuery.setOrderBy("create_time desc");
		PaginationResultVO resultVO = userInfoService.findListByPage(userInfoQuery);
		return getSuccessResponseVO(resultVO);
	}

	@RequestMapping("/updateUserStatus")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO updateUserStatus(@NotNull Integer status, @NotEmpty String userId){
		userInfoService.updateUserStatus(status, userId);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/forceOffLine")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO forceOffLine(@NotEmpty String userId){
		userInfoService.forceOffLine(userId);
		return getSuccessResponseVO(null);
	}

}