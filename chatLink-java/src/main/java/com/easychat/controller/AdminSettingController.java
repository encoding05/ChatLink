package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisComponent;
import com.easychat.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;

/**
 *@ Description:用户信息表Controller
 *@ Date:2024/11/21
 */
@RestController("adminSettingController")
@RequestMapping("/admin")
public class AdminSettingController extends ABaseController {

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private AppConfig appConfig;


	@RequestMapping("/getSysSetting")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO getSysSetting(){
		SysSettingDto sysSettingDto = redisComponent.getSysSetting();
		return getSuccessResponseVO(sysSettingDto);
	}

	@RequestMapping("/saveSysSetting")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO saveSysSetting(SysSettingDto sysSettingDto, MultipartFile robotFile, MultipartFile robotCover) throws IOException {
		if (robotFile != null) {
			String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
			File targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
			if (!targetFileFolder.exists()) {
				targetFileFolder.mkdirs();
			}
			String filePath = targetFileFolder + "/" + Constants.ROBOT_UID;
			robotFile.transferTo(new File(filePath + Constants.IMAGE_SUFFIX));
			robotCover.transferTo(new File(filePath + Constants.COVER_IMAGE_SUFFIX));
		}
		redisComponent.saveSysSetting(sysSettingDto);
		return getSuccessResponseVO(null);
	}


}