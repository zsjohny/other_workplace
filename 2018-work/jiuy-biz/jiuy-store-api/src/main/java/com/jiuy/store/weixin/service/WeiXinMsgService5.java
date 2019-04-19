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
 * 发送消息-客服消息
 * 
 * 当用户和公众号产生特定动作的交互时,微信将会把消息数据推送给开发者，开发者可以在一段时间内（目前修改为48小时）调用客服接口，
 * 通过POST一个JSON数据包来发送消息给普通用户，此接口主要用于客服等有人工消息处理环节的功能，方便开发者为用户提供更加优质的服务。
 * 
 * 目前允许的动作列表如下（公众平台会根据运营情况更新该列表，不同动作触发后，允许的客服接口下发消息条数不同，下发条数达到上限后，会遇到错误返回码，具体请见返回码说明页）：
 * 	1、用户发送信息
	2、点击自定义菜单（仅有点击推事件、扫码推事件、扫码推事件且弹出“消息接收中”提示框这3种菜单类型是会触发客服接口的）
	3、关注公众号
	4、扫描二维码
	5、支付成功
	6、用户维权
 * 为了帮助公众号使用不同的客服身份服务不同的用户群体，客服接口进行了升级，开发者可以管理客服账号，并设置客服账号的头像和昵称。该能力针对所有拥有客服接口权限的公众号开放。
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Administrator
 *
 */
@Service
public class WeiXinMsgService5 {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgService5.class);

   /*
    目录
1 客服帐号管理
1.1 添加客服帐号
1.2 修改客服帐号
1.3 删除客服帐号
1.4 设置客服帐号的头像
1.5 获取所有客服账号
1.6 接口的统一参数说明
2 客服接口-发消息


客服接口-发消息
http请求方式: POST
https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN

各消息类型所需的JSON数据包如下：
发送文本消息
{
    "touser":"OPENID",
    "msgtype":"text",
    "text":
    {
         "content":"Hello World"
    }
}
发送图片消息
{
    "touser":"OPENID",
    "msgtype":"image",
    "image":
    {
      "media_id":"MEDIA_ID"
    }
}
发送语音消息
{
    "touser":"OPENID",
    "msgtype":"voice",
    "voice":
    {
      "media_id":"MEDIA_ID"
    }
}
发送视频消息
{
    "touser":"OPENID",
    "msgtype":"video",
    "video":
    {
      "media_id":"MEDIA_ID",
      "thumb_media_id":"MEDIA_ID",
      "title":"TITLE",
      "description":"DESCRIPTION"
    }
}
发送音乐消息
{
    "touser":"OPENID",
    "msgtype":"music",
    "music":
    {
      "title":"MUSIC_TITLE",
      "description":"MUSIC_DESCRIPTION",
      "musicurl":"MUSIC_URL",
      "hqmusicurl":"HQ_MUSIC_URL",
      "thumb_media_id":"THUMB_MEDIA_ID" 
    }
}
发送图文消息（点击跳转到外链） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
{
    "touser":"OPENID",
    "msgtype":"news",
    "news":{
        "articles": [
         {
             "title":"Happy Day",
             "description":"Is Really A Happy Day",
             "url":"URL",
             "picurl":"PIC_URL"
         },
         {
             "title":"Happy Day",
             "description":"Is Really A Happy Day",
             "url":"URL",
             "picurl":"PIC_URL"
         }
         ]
    }
}
发送图文消息（点击跳转到图文消息页面） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
{
    "touser":"OPENID",
    "msgtype":"mpnews",
    "mpnews":
    {
         "media_id":"MEDIA_ID"
    }
}
发送卡券
{
  "touser":"OPENID", 
  "msgtype":"wxcard",
  "wxcard":{              
           "card_id":"123dsdajkasd231jhksad"        
            },
}

特别注意客服消息接口投放卡券仅支持非自定义Code码和导入code模式的卡券的卡券，详情请见：是否自定义code码。
请注意，如果需要以某个客服帐号来发消息（在微信6.0.2及以上版本中显示自定义头像），则需在JSON数据包的后半部分加入customservice参数，例如发送文本消息则改为：
{
    "touser":"OPENID",
    "msgtype":"text",
    "text":
    {
         "content":"Hello World"
    },
    "customservice":
    {
         "kf_account": "test1@kftest"
    }
}
参数	是否必须	说明
access_token	是	调用接口凭证
touser	是	普通用户openid
msgtype	是	消息类型，文本为text，图片为image，语音为voice，视频消息为video，音乐消息为music，图文消息（点击跳转到外链）为news，图文消息（点击跳转到图文消息页面）为mpnews，卡券为wxcard
content	是	文本消息内容
media_id	是	发送的图片/语音/视频/图文消息（点击跳转到图文消息页）的媒体ID
thumb_media_id	是	缩略图的媒体ID
title	否	图文消息/视频消息/音乐消息的标题
description	否	图文消息/视频消息/音乐消息的描述
musicurl	是	音乐链接
hqmusicurl	是	高品质音乐链接，wifi环境优先使用该链接播放音乐
url	否	图文消息被点击后跳转的链接
picurl	否	图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
接口返回说明
返回数据示例（正确时的JSON返回结果）：








    */
}
