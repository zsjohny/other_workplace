/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com.jiuyuan.entity.ucpaas.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "voiceCode")
public class VoiceCode {
	private String appId; //必选   应用Id
	
	private String captchaCode; //必选   验证码内容[为数字0~9长度为4或6]
	
	private String to;   //必选   语音播放次数
	
	private String playTimes; //必选   接收号码
	
	private String displayNum; //必选   显示的主叫号码
	
	private String userData;  //可选   用户透传数据，语音验证码状态通知中获取该数据
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCaptchaCode() {
		return captchaCode;
	}
	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
	public String getPlayTimes() {
		return playTimes;
	}
	public void setPlayTimes(String playTimes) {
		this.playTimes = playTimes;
	}
	public String getDisplayNum() {
		return displayNum;
	}
	public void setDisplayNum(String displayNum) {
		this.displayNum = displayNum;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	
	
}
