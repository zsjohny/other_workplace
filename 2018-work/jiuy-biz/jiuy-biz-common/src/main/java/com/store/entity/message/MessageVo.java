package com.store.entity.message;

import com.store.enumerate.MessageTypeEnum;

/**
 * 消息
 * @author Administrator
 *
 */
public class MessageVo {
	private MessageTypeEnum messageType;
	private String content;
	private int sendTotalMemberCount = 0 ;
	private int sendSeccessMemberCount = 0 ;
	private int sendFailMemberCount =  0 ;
	
	public MessageTypeEnum getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageTypeEnum messageType) {
		this.messageType = messageType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSendTotalMemberCount() {
		return sendTotalMemberCount;
	}
	public void setSendTotalMemberCount(int sendTotalMemberCount) {
		this.sendTotalMemberCount = sendTotalMemberCount;
	}
	public int getSendSeccessMemberCount() {
		return sendSeccessMemberCount;
	}
	public void setSendSeccessMemberCount(int sendSeccessMemberCount) {
		this.sendSeccessMemberCount = sendSeccessMemberCount;
	}
	public int getSendFailMemberCount() {
		return sendFailMemberCount;
	}
	public void setSendFailMemberCount(int sendFailMemberCount) {
		this.sendFailMemberCount = sendFailMemberCount;
	}
	
	
	
}
