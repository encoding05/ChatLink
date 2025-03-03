package com.easychat.entity.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import com.easychat.entity.enums.DateTimePatternEnum;
import com.easychat.utils.DateUtils;

/**
 *@ Description:
 *@ Date:2024/12/02
 */
public class GroupInfo implements Serializable {

	/**
	 *@ Description:群组ID
	 */
	private String groupId;

	/**
	 *@ Description:群组名称
	 */
	private String groupName;

	/**
	 *@ Description:群主ID
	 */
	private String groupOwnerId;

	public String getGroupOwnerNickName() {
		return groupOwnerNickName;
	}

	public void setGroupOwnerNickName(String groupOwnerNickName) {
		this.groupOwnerNickName = groupOwnerNickName;
	}

	private Integer memberCount;

	private String groupOwnerNickName;

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	/**
	 *@ Description:创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 *@ Description:群公告
	 */
	private String groupNotice;

	/**
	 *@ Description:加群方式 0:允许任何人加入 1:需要群主同意
	 */
	private Integer joinType;

	/**
	 *@ Description:群组状态 1:正常 0:解散
	 */
	@JsonIgnore
	private Integer status;

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupOwnerId(String groupOwnerId) {
		this.groupOwnerId = groupOwnerId;
	}

	public String getGroupOwnerId() {
		return this.groupOwnerId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setGroupNotice(String groupNotice) {
		this.groupNotice = groupNotice;
	}

	public String getGroupNotice() {
		return this.groupNotice;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public Integer getJoinType() {
		return this.joinType;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "群组ID:" + (groupId == null ? "空" :groupId) + ",群组名称:" + (groupName == null ? "空" :groupName) + ",群主ID:" + (groupOwnerId == null ? "空" :groupOwnerId) + ",创建时间:" + (createTime == null ? "空" :DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",群公告:" + (groupNotice == null ? "空" :groupNotice) + ",加群方式 0:允许任何人加入 1:需要群主同意:" + (joinType == null ? "空" :joinType) + ",群组状态 1:正常 0:解散:" + (status == null ? "空" :status);
	}
}