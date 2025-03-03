package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.UserInfoBeautyQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.UserInfoBeautyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *@ Description:用户信息表Controller
 *@ Date:2024/11/21
 */
@RestController("adminUserInfoBeautyController")
@RequestMapping("/admin")
public class AdminUserInfoBeautyController extends ABaseController {

	@Resource
	private UserInfoBeautyService userInfoBeautyService;


	@RequestMapping("/loadBeautyAccountList")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadBeautyAccountList(UserInfoBeautyQuery userInfoBeautyQuery){
		userInfoBeautyQuery.setOrderBy("id desc");
		PaginationResultVO resultVO = userInfoBeautyService.findListByPage(userInfoBeautyQuery);
		return getSuccessResponseVO(resultVO);
	}

	@RequestMapping("/saveBeautyAccount")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO saveBeautyAccount(UserInfoBeauty beauty){
		userInfoBeautyService.saveAccount(beauty);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/delBeautyAccount")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO delBeautyAccount(@NotNull Integer id){
		userInfoBeautyService.deleteUserInfoBeautyById(id);
		return getSuccessResponseVO(null);
	}

}