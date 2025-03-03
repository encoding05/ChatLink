package com.easychat.service;

import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.UserInfoBeautyQuery;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:靓号Service
 *@ Date:2024/11/21
 */
public interface UserInfoBeautyService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(UserInfoBeautyQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(UserInfoBeauty bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<UserInfoBeauty> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfoBeauty> listBean);

	/**
	 *@ Description:根据Id查询
	 */
	UserInfoBeauty getUserInfoBeautyById(Integer id);

	/**
	 *@ Description:根据Id更新
	 */
	Integer updateUserInfoBeautyById(UserInfoBeauty bean,Integer id);

	/**
	 *@ Description:根据Id删除
	 */
	Integer deleteUserInfoBeautyById(Integer id);

	/**
	 *@ Description:根据Email查询
	 */
	UserInfoBeauty getUserInfoBeautyByEmail(String email);

	/**
	 *@ Description:根据Email更新
	 */
	Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean,String email);

	/**
	 *@ Description:根据Email删除
	 */
	Integer deleteUserInfoBeautyByEmail(String email);

	/**
	 *@ Description:根据UserId查询
	 */
	UserInfoBeauty getUserInfoBeautyByUserId(String userId);

	/**
	 *@ Description:根据UserId更新
	 */
	Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean,String userId);

	/**
	 *@ Description:根据UserId删除
	 */
	Integer deleteUserInfoBeautyByUserId(String userId);

	void saveAccount(UserInfoBeauty beauty);
}