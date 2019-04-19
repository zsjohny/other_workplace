package com.jiuyuan.constant;

/**
 * 千米物流信息
 * 
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public enum QMExpress {
	
	EMS("100436571", "EMS", "ems"),
	
	HTKY("100436572", "百世快递", "huitongkuaidi"),
	
	STO("100436573", "申通快递", "shentong"),
	
	SF("100436574", "顺丰速运", "shunfeng"),
	
	ZJS("100436580", "宅急送", "zhaijisong"),
	
	YUNDA("100436576", "韵达快递", "yunda"),
	
	YTO("100436577", "圆通速递", "yuantong"),
	
	POSTB("100436578", "邮政快递包裹", "youzhengguonei"),
	
	ZTO("100436579", "中通快递", "zhongtong"),
	
	TTKDEX("100436575", "天天快递", "tiantian");
	
	private String id;
	
	private String qmName;
	
	private String localName;
	
	QMExpress(String id, String qmName, String localName) {
		this.id = id;
		this.qmName = qmName;
		this.localName = localName;
	}

	public String getId() {
		return id;
	}

	public String getQmName() {
		return qmName;
	}

	public String getLocalName() {
		return localName;
	}

	public static QMExpress getByLocalName(String localName) {
		for (QMExpress qmExpress : QMExpress.values()) {
			if (qmExpress.getLocalName().contains(localName)) {
				return qmExpress;
			}
		}
		return null;
	}
	
}
