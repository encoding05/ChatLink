package com.easychat.service.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.enums.AppUpdateFileTypeEnum;
import com.easychat.entity.enums.AppUpdateStatusEnum;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.enums.PageSize;
import com.easychat.entity.po.AppUpdate;
import com.easychat.entity.query.AppUpdateQuery;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.AppUpdateMapper;
import com.easychat.service.AppUpdateService;
import com.easychat.utils.StringTools;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:app发布Service
 *@ Date:2024/12/14
 */
@Service("appUpdateService") 
public class AppUpdateServiceImpl implements AppUpdateService{

	/**
	 *@ Description:根据条件查询列表
	 */
	@Resource
	private AppUpdateMapper<AppUpdate,AppUpdateQuery>appUpdateMapper;

	@Resource
	private AppConfig appConfig;

	public List<AppUpdate> findListByParam(AppUpdateQuery query) {

		return this.appUpdateMapper.selectList(query);
	}

	/**
	 *@ Description:根据条件查询数量
	 */
	public Integer findCountByParam(AppUpdateQuery query) {

		return this.appUpdateMapper.selectCount(query);
	}

	/**
	 *@ Description:分页查询
	 */
	public PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery query) {

		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<AppUpdate> list = this.findListByParam(query);
		PaginationResultVO<AppUpdate> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 *@ Description:新增
	 */
	public Integer add(AppUpdate bean) {

		return this.appUpdateMapper.insert(bean);
	}

	/**
	 *@ Description:批量新增
	 */
	public Integer addBatch(List<AppUpdate> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appUpdateMapper.insertBatch(listBean);
	}

	/**
	 *@ Description:批量新增或修改
	 */
	public Integer addOrUpdateBatch(List<AppUpdate> listBean) {

		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appUpdateMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *@ Description:根据Id查询
	 */
	public AppUpdate getAppUpdateById(Integer id) {

		return this.appUpdateMapper.selectById(id);
	}

	/**
	 *@ Description:根据Id更新
	 */
	public Integer updateAppUpdateById(AppUpdate bean,Integer id) {

		return this.appUpdateMapper.updateById(bean, id);
	}

	/**
	 *@ Description:根据Id删除
	 */
	public Integer deleteAppUpdateById(Integer id) {
		AppUpdate dbInfo = this.getAppUpdateById(id);
		if (!AppUpdateStatusEnum.INIT.getStatus().equals(dbInfo.getStatus()))
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		return this.appUpdateMapper.deleteById(id);
	}

	@Override
	public void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException {
		AppUpdateFileTypeEnum fileTypeEnum = AppUpdateFileTypeEnum.getByType(appUpdate.getFileType());

		if (fileTypeEnum == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		if (appUpdate.getId() != null) {
			AppUpdate dbInfo = this.getAppUpdateById(appUpdate.getId());
			if (!AppUpdateStatusEnum.INIT.getStatus().equals(dbInfo.getStatus()))
				throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		AppUpdateQuery updateQuery = new AppUpdateQuery();
		updateQuery.setOrderBy("id desc");
		updateQuery.setSimplePage(new SimplePage(0, 1));
		List<AppUpdate> updateList = appUpdateMapper.selectList(updateQuery);
		if (!updateList.isEmpty()) {
			AppUpdate latest = updateList.get(0);
			long dbVersion = Long.parseLong(latest.getVersion().replace(".", ""));
			long currentVersion = Long.parseLong(appUpdate.getVersion().replace(".", ""));
			if (appUpdate.getId() == null && currentVersion <= dbVersion)
				throw new BusinessException("版本号必须大于当前最新版本号");
			if (appUpdate.getId() != null && currentVersion >= dbVersion && !appUpdate.getId().equals(latest.getId()))
				throw new BusinessException("版本号必须大于当前最新版本号");
		}

		if (appUpdate.getId() == null){
			appUpdate.setCreateTime(new Date());
			appUpdate.setStatus(AppUpdateStatusEnum.INIT.getStatus());
			appUpdateMapper.insert(appUpdate);
		} else {
			appUpdateMapper.updateById(appUpdate, appUpdate.getId());
		}

		if (file != null){
			File folder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER);
			if (!folder.exists()) folder.mkdirs();
			file.transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + Constants.APP_UPDATE_FILE_SUFFIX));
		}
	}

	@Override
	public void postUpdate(Integer id, Integer status, String grayscaleUid) {
		AppUpdateStatusEnum statusEnum = AppUpdateStatusEnum.getEnumByStatus(status);
		if (statusEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		if (AppUpdateStatusEnum.GRAYSCALE == statusEnum && StringTools.isEmpty(grayscaleUid))
			throw new BusinessException(ResponseCodeEnum.CODE_600);

		if (AppUpdateStatusEnum.GRAYSCALE != statusEnum)
			grayscaleUid = "";

		AppUpdate update = new AppUpdate();
		update.setStatus(status);
		update.setGrayscaleUid(grayscaleUid);
		appUpdateMapper.updateById(update, id);
	}

	@Override
	public AppUpdate getLatestUpdate(String appVersion, String uid) {

		return appUpdateMapper.selectLatestUpdate(appVersion, uid);
	}

}