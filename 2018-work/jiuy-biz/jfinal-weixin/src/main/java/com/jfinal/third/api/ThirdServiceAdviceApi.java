
package com.jfinal.third.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.log.Log;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;

/**
 * 微信 第三方代码管理相关api接口
 * @author zhaoxinglin
 * 相关文档地址：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1489140610_Uavc4&token=&lang=zh_CN
 */
public class ThirdServiceAdviceApi {
	static Log logger = Log.getLog(ThirdServiceAdviceApi.class);

	protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);

	public static void main(String[] args) {
		String names = ",dad,dsd,sd";

		if(names.length()>0){
			names = names.substring(1,names.length() );
		}
		System.out.println(names);
	}

	/**
	 * 发送服务模板通知
	 *
	 * curl https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?
	 * access_token=6d5M7HZCoBY2DS4ZTPBE9RQJEgO3rOfu_LCLX1Fthc5k4-bJaSQyf3XoWSvy43YnDG9lTDbN9jwn9lX1lGaBdwYHggkiXNlb0XSrVNk6gV3JvA2lVeCQtLj4tBp5G3I_AYJhAKDTRC
	 * -d "{\"touser\":\"oU17_0Jn639kEmWMctfRX3e0svOA\",\"template_id\":\"N2a_cBcgokHWjUxLb0ubZ85c2oBdMevt5_r4QazcAXc\",\"form_id\":\"1504080721025\",\"data\":\"\"}"
	 *
	 * @param appId
	 * @param openId
	 * @param template_id
	 * @return
	 */
	public String sendTemplateAdvice(String page,String appId, String openId, String template_id ,
			String form_id,String keyword1,String keyword2,String keyword3,String keyword4) {
		String authorizer_token = thirdApi.get_authorizer_token(appId);
		if(StringUtils.isEmpty(authorizer_token)){
			logger.info("获取小程序authorizer_token为空，请排查问题！！！！");
			return null;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+authorizer_token;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("touser", openId);//touser	是	接收者（用户）的 openid
		paramMap.put("template_id", template_id);//template_id	是	所需下发的模板消息的id
		paramMap.put("page", page);//page	否	点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
		paramMap.put("form_id", form_id);//form_id	是	表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
		paramMap.put("data", buildData( keyword1, keyword2, keyword3, keyword4));//data	是	模板内容，不填则下发空模板
        String param = JSONObject.toJSONString(paramMap);

		logger.info("--------------------------------------------------");
		logger.info("--------------------------------------------------");
		logger.info("--------------------------------------------------");
		logger.info("发送服务模板通知，url："+url);
		logger.info("发送服务模板通知，param："+param);
		logger.info("--------------------------------------------------");
		logger.info("--------------------------------------------------");
		logger.info("--------------------------------------------------");
		logger.info("--------------------------------------------------");
		Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
		return retMap.toString();
	}

	/**
	 * 组装发送通知数据
	 * @param keyword1
	 * @param keyword2
	 * @param keyword3
	 * @param keyword4
	 * @return
	 */
	private Map<String,Map<String,String>> buildData(String keyword1,String keyword2,String keyword3,String keyword4) {
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
		String color = "#173177";
		if(StringUtils.isNotEmpty(keyword1)){
			Map<String,String> keywordMap = new HashMap<String,String>();
			keywordMap.put("value", keyword1);
			keywordMap.put("color", color);
			map.put("keyword1", keywordMap);
		}
		if(StringUtils.isNotEmpty(keyword2)){
			Map<String,String> keywordMap = new HashMap<String,String>();
			keywordMap.put("value", keyword2);
			keywordMap.put("color", color);
			map.put("keyword2", keywordMap);
		}
		if(StringUtils.isNotEmpty(keyword3)){
			Map<String,String> keywordMap = new HashMap<String,String>();
			keywordMap.put("value", keyword3);
			keywordMap.put("color", color);
			map.put("keyword3", keywordMap);
		}
		if(StringUtils.isNotEmpty(keyword4)){
			Map<String,String> keywordMap = new HashMap<String,String>();
			keywordMap.put("value", keyword4);
			keywordMap.put("color", color);
			map.put("keyword4", keywordMap);
		}
		logger.info("======================组装发送通知数据:"+JSON.toJSONString(map));
		return map;
	}



	/**
	 * 获取模板通知列表
	 *
	 * {errcode=0, errmsg=ok, list=[{
	 * "template_id":"rPhfui7rFKPm37jX5vzhV_-aLUfVd4pSxHPk-_sVSb4",
	 * "title":"待付款提醒",
	 * "content":"下单时间{{keyword1.DATA}}\n商品详情{{keyword2.DATA}}\n支付提醒{{keyword3.DATA}}\n",
	 * "example":"下单时间：2016年8月8日\n商品详情：桔子水晶酒店（北京世贸天阶店），商务大床房（无早）\n支付提醒：请在13:57之前完成支付\n"}]}
	 * curl https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token=
	 * EGYSQxZLyovwfpbs1xIbZGrufwoSMKgaxyzLPbGMyxIND6RBR3ScoUWY4Qxj21febn6ETrBRi7sMMJnpMu66T61N_Bt6ler2NDSXHxmWaK1izdNCzxS-xvQObhn6WzKHFAQaADDQLB
	 * -d "{\"offset\":0,\"count\":20}"
	 * @param appId
	 * @return
	 */
	public String getTemplateAdvice(String appId) {
		logger.info("获取小程序authorizer_token为appId:"+appId);
		String authorizer_token = thirdApi.get_authorizer_token(appId);
		if(StringUtils.isEmpty(authorizer_token)){
			logger.info("获取小程序authorizer_token为空，请排查问题！！！！");
			return null;
		}

		String url = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token="+authorizer_token;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("offset", "0");
		paramMap.put("count", "20");//必须小于等于20
		String param = JSONObject.toJSONString(paramMap);
		logger.info(" 获取模板通知列表url:"+url+",param:"+JSON.toJSONString(param));
		String ret = WapPayHttpUtil.sendPostHttp(url, param);
		logger.info(" 获取模板通知列表ret:"+ret);
		return ret;
	}

	/**
	 * 添加服务通知模板
	 * @param appId
	 * @param template_id
	 * @param template_keywordIds
	 *
	 * 3.组合模板并添加至帐号下的个人模板库
curl https://api.weixin.qq.com/cgi-bin/wxopen/template/add?access_token=kJFW5Xf1hedxTCkCqzjo3k0WbD9gH6GBDwNyyP0WjM5nHWEVh_9X9nF9kmv50z4J4v0VVPAeDrGF
5a1VazxgAH_M96b-LHb-RMfDkjIMC1CS9g6Y4ZsczfMULvkx_QwSWREaAGDIDQ -d
"{\"id\":\"AT0002\",\"keyword_id_list\":[3,4,5]}"
返回值：{"errcode":0,"errmsg":"ok","template_id":"N2a_cBcgokHWjUxLb0ubZ0cCyYnENySTxb8i6AuJTNc"}

	 * @return
	 */
	public String addTemplateAdvice(String appId, String template_id, String template_keywordIds) {
		String authorizer_token = thirdApi.get_authorizer_token(appId);
		if(StringUtils.isEmpty(authorizer_token)){
			logger.info("获取小程序authorizer_token为空，请排查问题！！！！");
			return null;
		}

		String url = "https://api.weixin.qq.com/cgi-bin/wxopen/template/add?access_token="+authorizer_token;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", template_id);
		String[] keyword_id_list = template_keywordIds.split(",");
		paramMap.put("keyword_id_list", keyword_id_list);
		String param = JSONObject.toJSONString(paramMap);
		Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
		return retMap.toString();
	}

}
