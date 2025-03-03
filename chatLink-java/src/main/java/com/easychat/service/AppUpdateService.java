package com.easychat.service;

import com.easychat.entity.po.AppUpdate;
import com.easychat.entity.query.AppUpdateQuery;

import java.io.IOException;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:app发布Service
 *@ Date:2024/12/14
 */
public interface AppUpdateService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<AppUpdate> findListByParam(AppUpdateQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(AppUpdateQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(AppUpdate bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<AppUpdate> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<AppUpdate> listBean);

	/**
	 *@ Description:根据Id查询
	 */
	AppUpdate getAppUpdateById(Integer id);

	/**
	 *@ Description:根据Id更新
	 */
	Integer updateAppUpdateById(AppUpdate bean,Integer id);

	/**
	 *@ Description:根据Id删除
	 */
	Integer deleteAppUpdateById(Integer id);


	void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException;

	void postUpdate(Integer id, Integer status, String grayscaleUid);

	AppUpdate getLatestUpdate(String appVersion, String uid);
}