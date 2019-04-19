/**
 * 
 */
package com.yujj.test.util;

import org.joda.time.DateTime;

/**
* @author DongZhong
* @version 创建时间: 2017年2月15日 下午3:16:57
*/
public class TestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateTime time = new DateTime(2017, 2, 15, 12, 0, 0);
		System.out.println("time:" + time.getMillis());
		System.out.println("time:" + time.toString("yyyy-MM-dd hh:mm:ss"));
	}

}
