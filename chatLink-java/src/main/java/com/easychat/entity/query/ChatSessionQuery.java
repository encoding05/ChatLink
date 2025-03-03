package com.easychat.entity.query;


/**
 *@ Description:会话信息
 *@ Date:2024/12/26
 */
public class ChatSessionQuery extends BaseQuery {

	/**
	 *@ Description:会话ID查询对象
	 */
	private String sessionId;

	private String sessionIdFuzzy;

	/**
	 *@ Description:最后接收消息查询对象
	 */
	private String lastMessage;

	private String lastMessageFuzzy;

	/**
	 *@ Description:最后接收消息时间ms查询对象
	 */
	private Long lastReceiveTime;

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void setLastReceiveTime(Long lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public Long getLastReceiveTime() {
		return this.lastReceiveTime;
	}

	public void setSessionIdFuzzy(String sessionIdFuzzy) {
		this.sessionIdFuzzy = sessionIdFuzzy;
	}

	public String getSessionIdFuzzy() {
		return this.sessionIdFuzzy;
	}

	public void setLastMessageFuzzy(String lastMessageFuzzy) {
		this.lastMessageFuzzy = lastMessageFuzzy;
	}

	public String getLastMessageFuzzy() {
		return this.lastMessageFuzzy;
	}

}