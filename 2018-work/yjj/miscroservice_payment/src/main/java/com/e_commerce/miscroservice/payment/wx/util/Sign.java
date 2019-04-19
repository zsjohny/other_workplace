package com.e_commerce.miscroservice.payment.wx.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

import java.util.*;

/**
 * 描述
 * @author hyq
 * @date 2018/9/21 14:09
 * @return
 */
public class Sign {
	/**
	 * 微信支付签名算法sign
	 */
	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		// 所有参与传参的参数按照accsii排序（升序）
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		System.out.println("222:"+sb);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}

	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
