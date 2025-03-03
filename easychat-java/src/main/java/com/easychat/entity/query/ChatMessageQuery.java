package com.easychat.entity.query;


import java.util.List;

/**
 *@ Description:聊天消息表
 *@ Date:2024/12/26
 */
public class ChatMessageQuery extends BaseQuery {

	/**
	 *@ Description:消息自增ID查询对象
	 */
	private Long messageId;

	/**
	 *@ Description:会话ID查询对象
	 */
	private String sessionId;

	private String sessionIdFuzzy;

	/**
	 *@ Description:消息类型查询对象
	 */
	private Integer messageType;

	/**
	 *@ Description:消息内容查询对象
	 */
	private String messageContent;

	private String messageContentFuzzy;

	/**
	 *@ Description:发送人ID查询对象
	 */
	private String sendUserId;

	private String sendUserIdFuzzy;

	/**
	 *@ Description:发送人昵称查询对象
	 */
	private String sendUserNickName;

	private String sendUserNickNameFuzzy;

	/**
	 *@ Description:发送时间查询对象
	 */
	private Long sendTime;

	/**
	 *@ Description:联系人ID查询对象
	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
	 *@ Description:联系人类型 0:好友 1:群组查询对象
	 */
	private Integer contactType;

	/**
	 *@ Description:文件大小查询对象
	 */
	private Long fileSize;

	/**
	 *@ Description:文件名查询对象
	 */
	private String fileName;

	private String fileNameFuzzy;

	/**
	 *@ Description:文件类型查询对象
	 */
	private Integer fileType;

	/**
	 *@ Description:消息状态 0:正在发送 1:已发送查询对象
	 */
	private Integer status;

	private List<String>  contactIdList;

	private Long lastReceiveTime;

	public Long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(Long lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public List<String> getContactIdList() {
		return contactIdList;
	}

	public void setContactIdList(List<String> contactIdList) {
		this.contactIdList = contactIdList;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getMessageId() {
		return this.messageId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getMessageType() {
		return this.messageType;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageContent() {
		return this.messageContent;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserId() {
		return this.sendUserId;
	}

	public void setSendUserNickName(String sendUserNickName) {
		this.sendUserNickName = sendUserNickName;
	}

	public String getSendUserNickName() {
		return this.sendUserNickName;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public Long getSendTime() {
		return this.sendTime;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return this.contactId;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactType() {
		return this.contactType;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setSessionIdFuzzy(String sessionIdFuzzy) {
		this.sessionIdFuzzy = sessionIdFuzzy;
	}

	public String getSessionIdFuzzy() {
		return this.sessionIdFuzzy;
	}

	public void setMessageContentFuzzy(String messageContentFuzzy) {
		this.messageContentFuzzy = messageContentFuzzy;
	}

	public String getMessageContentFuzzy() {
		return this.messageContentFuzzy;
	}

	public void setSendUserIdFuzzy(String sendUserIdFuzzy) {
		this.sendUserIdFuzzy = sendUserIdFuzzy;
	}

	public String getSendUserIdFuzzy() {
		return this.sendUserIdFuzzy;
	}

	public void setSendUserNickNameFuzzy(String sendUserNickNameFuzzy) {
		this.sendUserNickNameFuzzy = sendUserNickNameFuzzy;
	}

	public String getSendUserNickNameFuzzy() {
		return this.sendUserNickNameFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) {
		this.contactIdFuzzy = contactIdFuzzy;
	}

	public String getContactIdFuzzy() {
		return this.contactIdFuzzy;
	}

	public void setFileNameFuzzy(String fileNameFuzzy) {
		this.fileNameFuzzy = fileNameFuzzy;
	}

	public String getFileNameFuzzy() {
		return this.fileNameFuzzy;
	}

}