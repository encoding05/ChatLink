package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.po.AppUpdate;
import com.easychat.entity.query.AppUpdateQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.redis.RedisComponent;
import com.easychat.service.AppUpdateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

/**
 *@ Description:用户信息表Controller
 *@ Date:2024/11/21
 */
@RestController("adminAppUpdateController")
@RequestMapping("/admin")
public class AdminAppUpdateController extends ABaseController {

	@Resource
	private AppUpdateService appUpdateService;



	@RequestMapping("/loadUpdateList")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadUpdateList(AppUpdateQuery appUpdateQuery){
		appUpdateQuery.setOrderBy("id desc");
		PaginationResultVO resultVO = appUpdateService.findListByPage(appUpdateQuery);
		return getSuccessResponseVO(resultVO);
	}

	@RequestMapping("/saveUpdate")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO saveUpdate(Integer id, @NotEmpty String version, @NotEmpty String updateDesc, @NotEmpty Integer fileType,
									 String outLink,
									 MultipartFile file) throws IOException {
		AppUpdate appUpdate = new AppUpdate();
		appUpdate.setId(id);
		appUpdate.setVersion(version);
		appUpdate.setUpdateDesc(updateDesc);
		appUpdate.setFileType(fileType);
		appUpdate.setOuterLink(outLink);
		appUpdateService.saveUpdate(appUpdate, file);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/delUpdate")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO delUpdate(@NotNull Integer id) {
		appUpdateService.deleteAppUpdateById(id);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/postUpdate")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO postUpdate(@NotNull Integer id, @NotNull Integer status, String grayscaleUid) {
		appUpdateService.postUpdate(id, status, grayscaleUid);
		return getSuccessResponseVO(null);
	}

}