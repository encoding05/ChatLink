package com.easychat.entity.query;


/**
 *@ Description:靓号
 *@ Date:2024/11/21
 */
public class UserInfoBeautyQuery extends BaseQuery {

	/**
	 *@ Description:自增id查询对象
	 */
	private Integer id;

	/**
	 *@ Description:邮箱查询对象
	 */
	private String email;

	private String emailFuzzy;

	/**
	 *@ Description:用户id查询对象
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 *@ Description:0：未使用 1：查询对象
	 */
	private Integer status;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return this.emailFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

}