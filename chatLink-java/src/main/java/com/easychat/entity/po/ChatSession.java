package com.easychat.entity.po;

import java.io.Serializable;


/**
 *@ Description:会话信息
 *@ Date:2024/12/26
 */
public class ChatSession implements Serializable {

	/**
	 *@ Description:会话ID
	 */
	private String sessionId;

	/**
	 *@ Description:最后接收消息
	 */
	private String lastMessage;

	/**
	 *@ Description:最后接收消息时间ms
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

	@Override
	public String toString() {
		return "会话ID:" + (sessionId == null ? "空" :sessionId) + ",最后接收消息:" + (lastMessage == null ? "空" :lastMessage) + ",最后接收消息时间ms:" + (lastReceiveTime == null ? "空" :lastReceiveTime);
	}
}