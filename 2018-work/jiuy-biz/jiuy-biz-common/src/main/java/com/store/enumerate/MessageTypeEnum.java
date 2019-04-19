package com.store.enumerate;


import com.alipay.api.internal.util.StringUtils;
import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 消息类型枚举
 * 
 * 自定义类型请以100开头，如：100,101,102
 * @author zhaoxinglin
 */
public enum MessageTypeEnum {
    unknown(-1,"unknown","未知或未支持消息"),
    text(0,"text","文本消息"),
    image(1,"image","图片消息"),
    link(2,"link","链接消息"),
	voice(3,"voice","语音消息"),
	video(4,"video","视频消息"),
	shortvideo(5,"shortvideo","小视频消息"),
	location(6,"location","地理位置消息"),
	music(7,"music","音乐消息"),
	news(8,"news","图文消息"),
	event(9,"event","事件");
	
	
	
	
	
	
    private MessageTypeEnum(int intCode,String code,String name) {
    	this.intCode = intCode;
    	this.code = code;
    	this.name = name;
    }

    private int intCode;
    private String code;
    private String name ;
    

    public int getIntCode() {
        return intCode;
    }

    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }

	public static MessageTypeEnum getEnum(int intCode) {
		if (intCode == 0){
			return text;
		}else if (intCode == 1){
			return image;
		}else if (intCode == 2){
			return link;
		}else if (intCode == 3){
			return voice;
		}else if (intCode == 4){
			return video;
		}else if (intCode == 5){
			return shortvideo;
		}else if (intCode == 6){
			return location;
		}else if (intCode == 7){
			return music;
		}else if (intCode == 8){
			return news;
		}else if (intCode == 9){
			return event;
		}else{
			return unknown;
		}
	}
	public static MessageTypeEnum getEnum(String code) {
		if(StringUtils.isEmpty(code)){
			return unknown;
		}else if (code.equals("text")){
			return text;
		}else if (code.equals("image")){
			return image;
		}else if (code.equals("link")){
			return link;
		}else if (code.equals("voice")){
			return voice;
		}else if (code.equals("video")){
			return video;
		}else if (code.equals("shortvideo")){
			return shortvideo;
		}else if (code.equals("location")){
			return location;
		}else if (code.equals("music")){
			return music;
		}else if (code.equals("news")){
			return news;
		}else if (code.equals("event")){
			return event;
		}else{
			return unknown;
		}
	}
	

}
