package com.easychat.entity.query;


/**
 *@ Description:会话用户
 *@ Date:2024/12/26
 */
public class ChatSessionUserQuery extends BaseQuery {

	/**
	 *@ Description:用户ID查询对象
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 *@ Description:联系人ID查询对象
	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
	 *@ Description:会话ID查询对象
	 */
	private String sessionId;

	private String sessionIdFuzzy;

	/**
	 *@ Description:联系人昵称查询对象
	 */
	private String contactName;

	private String contactNameFuzzy;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return this.contactId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) {
		this.contactIdFuzzy = contactIdFuzzy;
	}

	public String getContactIdFuzzy() {
		return this.contactIdFuzzy;
	}

	public void setSessionIdFuzzy(String sessionIdFuzzy) {
		this.sessionIdFuzzy = sessionIdFuzzy;
	}

	public String getSessionIdFuzzy() {
		return this.sessionIdFuzzy;
	}

	public void setContactNameFuzzy(String contactNameFuzzy) {
		this.contactNameFuzzy = contactNameFuzzy;
	}

	public String getContactNameFuzzy() {
		return this.contactNameFuzzy;
	}

}