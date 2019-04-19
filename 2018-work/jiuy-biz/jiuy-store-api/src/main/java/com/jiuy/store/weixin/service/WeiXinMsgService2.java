/**
 * 
 */
package com.jiuy.store.weixin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.service.common.MemcachedService;

/**
 * 接收消息-接收普通消息
 * @author Administrator
 *
 */
@Service
public class WeiXinMsgService2 {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgService2.class);

    
	
 /*http://blog.csdn.net/zhuyu19911016520/article/details/48310541
    
    //粉丝留言
     2：粉丝留言
   		(1)文本消息MsgType=text
    	(2)图片消息MsgType=image
    	(3)语音MsgType=voice
    	(4)视频MsgType=shortvideo
    3：微信菜单点击事件Event=CLICK
    4：微信菜单点击事件Event=VIEW
    5：粉丝关注 eventType=subscribe
    6：粉丝取消关注 eventType=unsubscribe
    7：微信认证成功：	
  
    
    文本消息
 <xml>
 	<ToUserName><![CDATA[toUser]]></ToUserName>//开发者微信号
 	<FromUserName><![CDATA[fromUser]]></FromUserName>//发送方帐号（一个OpenID）
 	<CreateTime>1348831860</CreateTime>//消息创建时间 （整型）
 	<MsgType><![CDATA[text]]></MsgType>//text  文本消息内容
 	<Content><![CDATA[this is a test]]></Content>//消息id，64位整型
 	<MsgId>1234567890123456</MsgId>
 </xml>
    
    
    图片消息
 <xml>
 	<ToUserName><![CDATA[toUser]]></ToUserName>//开发者微信号
 	<FromUserName><![CDATA[fromUser]]></FromUserName>//发送方帐号（一个OpenID）
 	<CreateTime>1348831860</CreateTime>//消息创建时间 （整型）
 	<MsgType><![CDATA[image]]></MsgType>//image
 	<PicUrl><![CDATA[this is a url]]></PicUrl>//图片链接（由系统生成）
 	<MediaId><![CDATA[media_id]]></MediaId>//图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
 	<MsgId>1234567890123456</MsgId>//消息id，64位整型
 </xml>
    
    语音消息
 <xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>//开发者微信号
	<FromUserName><![CDATA[fromUser]]></FromUserName>//发送方帐号（一个OpenID）
	<CreateTime>1357290913</CreateTime>//消息创建时间 （整型）
	<MsgType><![CDATA[voice]]></MsgType>//语音为voice
	<MediaId><![CDATA[media_id]]></MediaId>//语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	<Format><![CDATA[Format]]></Format>//语音格式，如amr，speex等
	<MsgId>1234567890123456</MsgId>//消息id，64位整型
</xml>

视频消息
<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>//开发者微信号
	<FromUserName><![CDATA[fromUser]]></FromUserName>、、发送方帐号（一个OpenID）
	<CreateTime>1357290913</CreateTime>//消息创建时间 （整型）
	<MsgType><![CDATA[video]]></MsgType>、、视频为video
	<MediaId><![CDATA[media_id]]></MediaId>//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	<MsgId>1234567890123456</MsgId>//消息id，64位整型
</xml>
    
    小视频消息
<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>开发者微信号
	<FromUserName><![CDATA[fromUser]]></FromUserName>发送方帐号（一个OpenID）
	<CreateTime>1357290913</CreateTime>消息创建时间 （整型）
	<MsgType><![CDATA[shortvideo]]></MsgType>小视频为shortvideo
	<MediaId><![CDATA[media_id]]></MediaId>视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	<MsgId>1234567890123456</MsgId>消息id，64位整型
</xml>
    
    地理位置消息
<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>//开发者微信号
	<FromUserName><![CDATA[fromUser]]></FromUserName>//发送方帐号（一个OpenID）
	<CreateTime>1351776360</CreateTime>//消息创建时间 （整型）
	<MsgType><![CDATA[location]]></MsgType>//location
	<Location_X>23.134521</Location_X>//地理位置维度
	<Location_Y>113.358803</Location_Y>//地理位置经度
	<Scale>20</Scale>//地图缩放大小
	<Label><![CDATA[位置信息]]></Label>//地理位置信息
	<MsgId>1234567890123456</MsgId>
</xml>
    
    
    链接消息
<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>//接收方微信号
	<FromUserName><![CDATA[fromUser]]></FromUserName>、、发送方微信号，若为普通用户，则是一个OpenID
	<CreateTime>1351776360</CreateTime>//消息创建时间
	<MsgType><![CDATA[link]]></MsgType>、、消息类型，link
	<Title><![CDATA[公众平台官网链接]]></Title>、、消息标题
	<Description><![CDATA[公众平台官网链接]]></Description>、、消息描述
	<Url><![CDATA[url]]></Url>、、消息链接
	<MsgId>1234567890123456</MsgId>、、消息id，64位整型
</xml>
    
 
    
    */
}
