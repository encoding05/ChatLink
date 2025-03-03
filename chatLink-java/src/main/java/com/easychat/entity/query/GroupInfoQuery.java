package com.easychat.entity.query;

import java.util.Date;

/**
 *@ Description:
 *@ Date:2024/12/02
 */
public class GroupInfoQuery extends BaseQuery {

	/**
	 *@ Description:群组ID查询对象
	 */
	private String groupId;

	private String groupIdFuzzy;

	private Boolean queryGroupOwnerName;

	private Boolean queryMemberCount;

	public Boolean getQueryGroupOwnerName() {
		return queryGroupOwnerName;
	}

	public void setQueryGroupOwnerName(Boolean queryGroupOwnerName) {
		this.queryGroupOwnerName = queryGroupOwnerName;
	}

	public Boolean getQueryMemberCount() {
		return queryMemberCount;
	}

	public void setQueryMemberCount(Boolean queryMemberCount) {
		this.queryMemberCount = queryMemberCount;
	}

	/**
	 *@ Description:群组名称查询对象
	 */
	private String groupName;

	private String groupNameFuzzy;

	/**
	 *@ Description:群主ID查询对象
	 */
	private String groupOwnerId;

	private String groupOwnerIdFuzzy;

	/**
	 *@ Description:创建时间查询对象
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 *@ Description:群公告查询对象
	 */
	private String groupNotice;

	private String groupNoticeFuzzy;

	/**
	 *@ Description:加群方式 0:允许任何人加入 1:需要群主同意查询对象
	 */
	private Integer joinType;

	/**
	 *@ Description:群组状态 1:正常 0:解散查询对象
	 */
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

	public void setGroupIdFuzzy(String groupIdFuzzy) {
		this.groupIdFuzzy = groupIdFuzzy;
	}

	public String getGroupIdFuzzy() {
		return this.groupIdFuzzy;
	}

	public void setGroupNameFuzzy(String groupNameFuzzy) {
		this.groupNameFuzzy = groupNameFuzzy;
	}

	public String getGroupNameFuzzy() {
		return this.groupNameFuzzy;
	}

	public void setGroupOwnerIdFuzzy(String groupOwnerIdFuzzy) {
		this.groupOwnerIdFuzzy = groupOwnerIdFuzzy;
	}

	public String getGroupOwnerIdFuzzy() {
		return this.groupOwnerIdFuzzy;
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

	public void setGroupNoticeFuzzy(String groupNoticeFuzzy) {
		this.groupNoticeFuzzy = groupNoticeFuzzy;
	}

	public String getGroupNoticeFuzzy() {
		return this.groupNoticeFuzzy;
	}

}