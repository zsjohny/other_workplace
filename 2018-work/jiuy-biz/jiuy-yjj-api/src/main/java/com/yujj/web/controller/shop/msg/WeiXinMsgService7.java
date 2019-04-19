/**
 * 
 */
package com.yujj.web.controller.shop.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.service.common.MemcachedService;

/**
 * 发送消息-模板消息接口
 * 
 * 模板消息仅用于公众号向用户发送重要的服务通知，只能用于符合其要求的服务场景中，如信用卡刷卡通知，商品购买成功通知等。
 * 不支持广告等营销类消息以及其它所有可能对用户造成骚扰的消息。
 * 
 * 注意阅读模板消息运营规范
 * 
 * @author Administrator
 *
 */
@Service
public class WeiXinMsgService7 {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgService7.class);

   /*
目录
1 设置所属行业
2 获取设置的行业信息
3 获得模板ID
4 获取模板列表
5 删除模板
6 发送模板消息
7 事件推送


发送模板消息
接口调用请求说明
http请求方式: POST
https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
POST数据说明
POST数据示例如下：
      {
           "touser":"OPENID",
           "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
           "url":"http://weixin.qq.com/download",  
           "miniprogram":{
             "appid":"xiaochengxuappid12345",
             "pagepath":"index?foo=bar"
           },          
           "data":{
                   "first": {
                       "value":"恭喜你购买成功！",
                       "color":"#173177"
                   },
                   "keynote1":{
                       "value":"巧克力",
                       "color":"#173177"
                   },
                   "keynote2": {
                       "value":"39.8元",
                       "color":"#173177"
                   },
                   "keynote3": {
                       "value":"2014年9月22日",
                       "color":"#173177"
                   },
                   "remark":{
                       "value":"欢迎再次购买！",
                       "color":"#173177"
                   }
           }
       }
参数说明

参数	是否必填	说明
touser	是	接收者openid
template_id	是	模板ID
url	否	模板跳转链接
miniprogram	否
跳小程序所需数据，不需跳小程序可不用传该数据
appid	是
所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系）
pagepath	是
所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
data	是	模板数据
   注：url和miniprogram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。


返回码说明
在调用模板消息接口后，会返回JSON数据包。正常时的返回JSON数据包示例：
    {
           "errcode":0,
           "errmsg":"ok",
           "msgid":200228332
       }
事件推送
在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。


1、送达成功时，推送的XML如下：
           <xml>
           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
           <CreateTime>1395658920</CreateTime>
           <MsgType><![CDATA[event]]></MsgType>
           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
           <MsgID>200163836</MsgID>
           <Status><![CDATA[success]]></Status>
           </xml>
参数说明
参数	说明
ToUserName	公众号微信号
FromUserName	接收模板消息的用户的openid
CreateTime	创建时间
MsgType	消息类型是事件
Event	事件为模板消息发送结束
MsgID	消息id
Status	发送状态为成功
2、送达由于用户拒收（用户设置拒绝接收公众号消息）而失败时，推送的XML如下：
           <xml>
           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
           <CreateTime>1395658984</CreateTime>
           <MsgType><![CDATA[event]]></MsgType>
           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
           <MsgID>200163840</MsgID>
           <Status><![CDATA[failed:user block]]></Status>
           </xml>
参数说明
参数	说明
ToUserName	公众号微信号
FromUserName	接收模板消息的用户的openid
CreateTime	创建时间
MsgType	消息类型是事件
Event	事件为模板消息发送结束
MsgID	消息id
Status	发送状态为用户拒绝接收
3、送达由于其他原因失败时，推送的XML如下：
           <xml>
           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
           <CreateTime>1395658984</CreateTime>
           <MsgType><![CDATA[event]]></MsgType>
           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
           <MsgID>200163840</MsgID>
           <Status><![CDATA[failed: system failed]]></Status>
           </xml>
参数说明
参数	说明
ToUserName	公众号微信号
FromUserName	接收模板消息的用户的openid
CreateTime	创建时间
MsgType	消息类型是事件
Event	事件为模板消息发送结束
MsgID	消息id
Status	发送状态为发送失败（非用户拒绝）

    */
}
