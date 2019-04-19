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
 * 发送消息-发送消息-群发接口和原创校验
 * 
 * 在公众平台网站上，为订阅号提供了每天一条的群发权限，为服务号提供每月（自然月）4条的群发权限，
 * 而对于某些具备开发能力的公众号运营者，可以通过高级群发接口，实现更灵活的群发能力
 * 
 * 请注意：
 * 1、对于认证订阅号，群发接口每天可成功调用1次，此次群发可选择发送给全部用户或某个标签；
	2、对于认证服务号虽然开发者使用高级群发接口的每日调用限制为100次，但是用户每月只能接收4条，无论在公众平台网站上，还是使用接口群发，用户每月只能接收4条群发消息，多于4条的群发将对该用户发送失败；
 * 3、开发者可以使用预览接口校对消息样式和排版，通过预览接口可发送编辑好的消息给指定用户校验效果；
 * 4、群发过程中，微信后台会自动进行图文消息原创校验，请提前设置好相关参数(send_ignore等)；、
 * 5、开发者可以主动设置 clientmsgid 来避免重复推送。
 * 6、群发接口每分钟限制请求60次，超过限制的请求会被拒绝。
 * 7、图文消息正文中插入自己帐号和其他公众号已群发文章链接的能力。
 * @author Administrator
 *
 */
@Service
public class WeiXinMsgService6 {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinMsgService6.class);

   /*

目录
1 上传图文消息内的图片获取URL【订阅号与服务号认证后均可用】
2 上传图文消息素材【订阅号与服务号认证后均可用】
3 根据标签进行群发【订阅号与服务号认证后均可用】
4 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
5 删除群发【订阅号与服务号认证后均可用】
6 预览接口【订阅号与服务号认证后均可用】
7 查询群发消息发送状态【订阅号与服务号认证后均可用】
8 事件推送群发结果
9 使用 clientmsgid 参数，避免重复推送





    */
}
