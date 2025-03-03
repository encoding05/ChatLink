package com.easychat.entity.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *@ Description:靓号
 *@ Date:2024/11/21
 */
public class UserInfoBeauty implements Serializable {

	/**
	 *@ Description:自增id
	 */
	private Integer id;

	/**
	 *@ Description:邮箱
	 */
	private String email;

	/**
	 *@ Description:用户id
	 */
	private String userId;

	/**
	 *@ Description:0：未使用 1：
	 */
	@JsonIgnore
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

	@Override
	public String toString() {
		return "自增id:" + (id == null ? "空" :id) + ",邮箱:" + (email == null ? "空" :email) + ",用户id:" + (userId == null ? "空" :userId) + ",0：未使用 1：:" + (status == null ? "空" :status);
	}
}