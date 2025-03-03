package com.easychat.service;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserInfoQuery;

import java.io.IOException;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:用户信息表Service
 *@ Date:2024/11/21
 */
public interface UserInfoService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(UserInfo bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 *@ Description:根据UserId查询
	 */
	UserInfo getUserInfoByUserId(String userId);

	/**
	 *@ Description:根据UserId更新
	 */
	Integer updateUserInfoByUserId(UserInfo bean,String userId);

	/**
	 *@ Description:根据UserId删除
	 */
	Integer deleteUserInfoByUserId(String userId);

	/**
	 *@ Description:根据Email查询
	 */
	UserInfo getUserInfoByEmail(String email);

	/**
	 *@ Description:根据Email更新
	 */
	Integer updateUserInfoByEmail(UserInfo bean,String email);

	/**
	 *@ Description:根据Email删除
	 */
	Integer deleteUserInfoByEmail(String email);


	void register(String email, String password, String nickName);

	UserInfoVO login(String email, String password);

	void updateUserInfo(UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarFileCover) throws IOException;

	void updateUserStatus(Integer status, String userId);

	void forceOffLine(String userId);
}