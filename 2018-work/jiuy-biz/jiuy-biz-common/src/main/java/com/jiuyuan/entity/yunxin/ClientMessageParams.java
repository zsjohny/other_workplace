package com.jiuyuan.entity.yunxin;

public class ClientMessageParams {

	// 用户ID
	private String userId;

	// 发送内容类型 0文本，1图片，2语音，3视频，4地理位置，6表示文件，100自定类型
	private Integer type;

	// 消息类型0点对点类型，1群消息
	private Integer messageType;// 消息类型，点对点消息盒群消息

	// type为0时有效，表示文本内容
	private String msg;

	// type为1时有效，图片名称
	private String name;

	// type1,2,3,6时有效
	private String md5;

	// type1,2,3,6时有效
	private String url;

	// type为1，3时有效，表示宽度
	private Integer w;

	// type为1，3时有效，表示高度
	private Integer h;

	// type为1，2，3，6时有效，表示内容的大小
	private Integer size;

	// type为2，3时有效，表示时长
	private Integer dur;

	// type为1，2，3，6时有效，表示扩展后缀
	private String ext;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getDur() {
		return dur;
	}

	public void setDur(Integer dur) {
		this.dur = dur;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

}
