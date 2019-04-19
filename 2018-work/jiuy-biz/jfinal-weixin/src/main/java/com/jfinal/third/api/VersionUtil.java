package com.jfinal.third.api;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jeff.zhan
 * @version 2016年11月17日 上午10:02:56
 * 
 */

public class VersionUtil {
	
	/**
	 * 
	 * @param version_1 例子：2.1.1
	 * @param version_2
	 * @return 0相等 、1大于、小于是-1、异常为-2
	 */
	public static int compareVersion(String version_1, String version_2) {
		try {
			String[] version_1_array = version_1.split("\\.");
			String[] version_2_array = version_2.split("\\.");
			for (int i = 0; i < version_2_array.length; i++) {
				if (Integer.parseInt(version_1_array[i]) > Integer.parseInt(version_2_array[i])) {
					return 1;
				} else if (Integer.parseInt(version_1_array[i]) < Integer.parseInt(version_2_array[i])) {
					return -1;
				}
			}
			return 0;
		} catch (Exception e) {
			return 2; 
		}
	}
	
	/**
	 * 
	 * @param version_1 例子：2.1.1
	 * @param version_2
	 * @return 0相等 、1大于、小于是-1
	 */
	public static int compareVersion2(String version_1, String version_2) {
		if (StringUtils.isEmpty(version_1) || StringUtils.isEmpty(version_2))
			return -1;

		String[] version_1_array = version_1.split("\\.");
		String[] version_2_array = version_2.split("\\.");
		for (int i = 0; i < version_2_array.length; i++) {
			if (Integer.parseInt(version_1_array[i]) > Integer.parseInt(version_2_array[i])) {
				return 1;
			} else if (Integer.parseInt(version_1_array[i]) < Integer.parseInt(version_2_array[i])) {
				return -1;
			}
		}
		return 0;

	}
	
	/**
	 * 大于>
	 * @param version_1
	 * @param version_2
	 * @return
	 */
	public static boolean gt(String version_1, String version_2){
		int ret = VersionUtil.compareVersion2(version_1 , version_2);
		return ret == 0 ;
	}
	/**
	 * 大于等于>=
	 * @param version_1
	 * @param version_2
	 * 0相等 、1大于、小于是-1
	 * @return
	 */
	public static boolean ge(String version_1, String version_2){
		int ret = VersionUtil.compareVersion2(version_1 , version_2);
		return ret >= 0;
	}
	/**
	 * 等于=
	 * @param version_1
	 * @param version_2
	 * @return
	 */
	public static boolean eq(String version_1, String version_2){
		int ret = VersionUtil.compareVersion2(version_1 , version_2);
		return ret == 0;
	}
	/**
	 * 小于<
	 * @param version_1
	 * @param version_2
	 * @return
	 */
	public static boolean lt(String version_1, String version_2){
		int ret = VersionUtil.compareVersion2(version_1 , version_2);
		return ret < 0;
	}
	
	/**
	 * 小于<=
	 * @param version_1
	 * @param version_2
	 * @return
	 */
	public static boolean le(String version_1, String version_2){
		int ret = VersionUtil.compareVersion2(version_1 , version_2);
		System.out.println(ret);
		return ret <= 0;
	}
	public static void main(String[] args) {
		
		boolean ret = VersionUtil.le("2.1.3" , "2.1.2");
		System.out.println(ret);
	}
}
