package com.jiuyuan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoleilu.hutool.date.DateUtil;

public class DateUtilJiuYuan {

    private static final Logger log = LoggerFactory.getLogger(DateUtilJiuYuan.class);

   

	/**
	 * 获取48小时前减1分钟的时间点的时间戳
	 * 用于微信客服消息限制
	 * @return
	 */
	public static long getBefore48Hour() {
		long nowTime = DateUtil.current(false);//new Date().getTime()
		long difference48Hour = 48 * 60 * 60 * 1000 ;
		long before48Hour = nowTime - difference48Hour;
		return before48Hour;
	}
}
