package com.gugu;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class Utils {

	private static String url = "http://gw.api.taobao.com/router/rest";
	private static String appkey = "23355973";
	private static String secret = "46d2e9ad08ad0bd20533d8a4ccfa2e4e";

	public static Boolean SendMsg(String phone, String code) {

		// 短信模板的内容
		String json = "{\"code\":\"" + code + "\",\"product\":\"" + "LeaveMe" + "\"}";
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("变更验证");
		req.setSmsParamString(json);
		req.setRecNum(phone);
		req.setSmsTemplateCode("SMS_8356249");

		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

			if (rsp.getBody().contains("\"success\":true")) {
				return true;
			} else {
				return false;
			}

		} catch (ApiException e) {
			return false;
		}

	}

	public static void main(String[] args) {

		System.out.println(SendMsg("15924179757", "879227577"));

	}
}
