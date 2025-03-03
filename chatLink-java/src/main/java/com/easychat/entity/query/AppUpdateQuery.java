package com.easychat.entity.query;

import java.util.Date;

/**
 *@ Description:app发布
 *@ Date:2024/12/14
 */
public class AppUpdateQuery extends BaseQuery {

	/**
	 *@ Description:自增ID查询对象
	 */
	private Integer id;

	/**
	 *@ Description:版本号查询对象
	 */
	private String version;

	private String versionFuzzy;

	/**
	 *@ Description:更新描述查询对象
	 */
	private String updateDesc;

	private String updateDescFuzzy;

	/**
	 *@ Description:创建时间查询对象
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 *@ Description:状态 0:未发布 1:灰度发布 2:全网发布查询对象
	 */
	private Integer status;

	/**
	 *@ Description:灰度UID查询对象
	 */
	private String grayscaleUid;

	private String grayscaleUidFuzzy;

	/**
	 *@ Description:文件类型 0:本地文件 1:外链查询对象
	 */
	private Integer fileType;

	/**
	 *@ Description:外链地址查询对象
	 */
	private String outerLink;

	private String outerLinkFuzzy;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}

	public String getUpdateDesc() {
		return this.updateDesc;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setGrayscaleUid(String grayscaleUid) {
		this.grayscaleUid = grayscaleUid;
	}

	public String getGrayscaleUid() {
		return this.grayscaleUid;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setOuterLink(String outerLink) {
		this.outerLink = outerLink;
	}

	public String getOuterLink() {
		return this.outerLink;
	}

	public void setVersionFuzzy(String versionFuzzy) {
		this.versionFuzzy = versionFuzzy;
	}

	public String getVersionFuzzy() {
		return this.versionFuzzy;
	}

	public void setUpdateDescFuzzy(String updateDescFuzzy) {
		this.updateDescFuzzy = updateDescFuzzy;
	}

	public String getUpdateDescFuzzy() {
		return this.updateDescFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setGrayscaleUidFuzzy(String grayscaleUidFuzzy) {
		this.grayscaleUidFuzzy = grayscaleUidFuzzy;
	}

	public String getGrayscaleUidFuzzy() {
		return this.grayscaleUidFuzzy;
	}

	public void setOuterLinkFuzzy(String outerLinkFuzzy) {
		this.outerLinkFuzzy = outerLinkFuzzy;
	}

	public String getOuterLinkFuzzy() {
		return this.outerLinkFuzzy;
	}

}