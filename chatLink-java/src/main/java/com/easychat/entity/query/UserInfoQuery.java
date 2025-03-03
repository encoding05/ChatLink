package com.easychat.entity.query;

import java.util.Date;

/**
 *@ Description:用户信息表
 *@ Date:2024/11/21
 */
public class UserInfoQuery extends BaseQuery {

	/**
	 *@ Description:创建时间查询对象
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 *@ Description:邮箱查询对象
	 */
	private String email;

	private String emailFuzzy;

	/**
	 *@ Description:昵称查询对象
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 *@ Description:申请好友 0：直接加入 1：同意后加好友查询对象
	 */
	private Integer joinType;

	/**
	 *@ Description:性别 0：女 1：男查询对象
	 */
	private Integer gender;

	/**
	 *@ Description:密码查询对象
	 */
	private String password;

	private String passwordFuzzy;

	/**
	 *@ Description:个性签名查询对象
	 */
	private String personalSignature;

	private String personalSignatureFuzzy;

	/**
	 *@ Description:状态查询对象
	 */
	private Integer status;

	/**
	 *@ Description:创建时间查询对象
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 *@ Description:最后登录时间查询对象
	 */
	private Date lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	/**
	 *@ Description:地区查询对象
	 */
	private String areaName;

	private String areaNameFuzzy;

	/**
	 *@ Description:地区编号查询对象
	 */
	private String areaCode;

	private String areaCodeFuzzy;

	/**
	 *@ Description:最后离开时间查询对象
	 */
	private Long lastOffTime;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public Integer getJoinType() {
		return this.joinType;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPersonalSignature(String personalSignature) {
		this.personalSignature = personalSignature;
	}

	public String getPersonalSignature() {
		return this.personalSignature;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setLastOffTime(Long lastOffTime) {
		this.lastOffTime = lastOffTime;
	}

	public Long getLastOffTime() {
		return this.lastOffTime;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return this.emailFuzzy;
	}

	public void setNickNameFuzzy(String nickNameFuzzy) {
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy() {
		return this.nickNameFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy() {
		return this.passwordFuzzy;
	}

	public void setPersonalSignatureFuzzy(String personalSignatureFuzzy) {
		this.personalSignatureFuzzy = personalSignatureFuzzy;
	}

	public String getPersonalSignatureFuzzy() {
		return this.personalSignatureFuzzy;
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

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart() {
		return this.lastLoginTimeStart;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd() {
		return this.lastLoginTimeEnd;
	}

	public void setAreaNameFuzzy(String areaNameFuzzy) {
		this.areaNameFuzzy = areaNameFuzzy;
	}

	public String getAreaNameFuzzy() {
		return this.areaNameFuzzy;
	}

	public void setAreaCodeFuzzy(String areaCodeFuzzy) {
		this.areaCodeFuzzy = areaCodeFuzzy;
	}

	public String getAreaCodeFuzzy() {
		return this.areaCodeFuzzy;
	}

}