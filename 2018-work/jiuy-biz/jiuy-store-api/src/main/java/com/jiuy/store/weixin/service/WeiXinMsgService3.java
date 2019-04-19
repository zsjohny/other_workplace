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
 * 发送消息-被动回复用户消息
 * @author Administrator
 *
 */
@Service
public class WeiXinMsgService3 {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgService3.class);

 /*
    目录
1 回复文本消息
2 回复图片消息
3 回复语音消息
4 回复视频消息
5 回复音乐消息
6 回复图文消息
    
   回复文本消息
    <xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间 （整型）
<MsgType><![CDATA[text]]></MsgType>text
<Content><![CDATA[你好]]></Content>回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
</xml>

    回复图片消息
    <xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间 （整型）
<MsgType><![CDATA[image]]></MsgType>image
<Image>
	<MediaId><![CDATA[media_id]]></MediaId>通过素材管理中的接口上传多媒体文件，得到的id。
</Image>
</xml>


回复语音消息
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间戳 （整型）
<MsgType><![CDATA[voice]]></MsgType>//语音，voice
<Voice>
	<MediaId><![CDATA[media_id]]></MediaId>//通过素材管理中的接口上传多媒体文件，得到的id
</Voice>
</xml>


回复视频消息
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间 （整型）
<MsgType><![CDATA[video]]></MsgType>video
<Video>
<MediaId><![CDATA[media_id]]></MediaId>通过素材管理中的接口上传多媒体文件，得到的id
<Title><![CDATA[title]]></Title>视频消息的标题
<Description><![CDATA[description]]></Description>视频消息的描述
</Video> 
</xml>

回复音乐消息
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间 （整型）
<MsgType><![CDATA[music]]></MsgType>music
<Music>
<Title><![CDATA[TITLE]]></Title>音乐标题
<Description><![CDATA[DESCRIPTION]]></Description>音乐描述
<MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>音乐链接
<HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>高质量音乐链接，WIFI环境优先使用该链接播放音乐
<ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
</Music>
</xml>


回复图文消息
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>接收方帐号（收到的OpenID）
<FromUserName><![CDATA[fromUser]]></FromUserName>开发者微信号
<CreateTime>12345678</CreateTime>消息创建时间 （整型）
<MsgType><![CDATA[news]]></MsgType>news
<ArticleCount>2</ArticleCount>图文消息个数，限制为8条以内
<Articles>多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
<item>
<Title><![CDATA[title1]]></Title> 图文消息标题
<Description><![CDATA[description1]]></Description>图文消息描述
<PicUrl><![CDATA[picurl]]></PicUrl>图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
<Url><![CDATA[url]]></Url>点击图文消息跳转链接
</item>
<item>
<Title><![CDATA[title]]></Title>
<Description><![CDATA[description]]></Description>
<PicUrl><![CDATA[picurl]]></PicUrl>
<Url><![CDATA[url]]></Url>
</item>
</Articles>
</xml>



    
    */
}
