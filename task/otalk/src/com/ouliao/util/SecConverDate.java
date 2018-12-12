/**
 * 
 */
package com.ouliao.util;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SecConverDate.java, 2016年3月17日 下午5:31:33
 */

public class SecConverDate {

	public static String SecConverCNDate(Long nums) {

		Integer sec = Integer.parseInt(nums.toString());

		int h = 0;
		int m = 0;
		int s = 0;

		int num = 0;
		if (sec > 3600) {

			h = sec / 3600;

			if (sec - 3600 < 60) {
				s = sec - 3600;
			}

			num = (sec - 3600) % 60;
			if (num != 0) {

				m = (sec - 3600) / 60 - (h - 1) * 60;
				s = num;

			} else {

				m = (sec - 3600) / 60 - (h - 1) * 60;

			}

		} else {

			if (sec < 60) {
				s = sec;
			}

			num = sec % 60;
			if (num != 0) {

				m = sec / 60;
				s = num;

			} else {

				m = sec / 60;

			}

		}
		String str = "";

		switch (h) {
		case 0:
			switch (m) {
			case 0:
				str = s + " 秒";
				break;

			default:
				switch (s) {
				case 0:
					str = m + " 分";
					break;

				default:
					str = m + " 分" + s + " 秒";
					break;
				}

			}
			break;

		default:

			switch (m) {
			case 0:

				switch (s) {
				case 0:
					str = h + " 小时";
					break;

				default:
					str = h + " 小时" + s + " 秒";
					break;
				}

				break;

			default:

				switch (s) {
				case 0:
					str = h + " 小时" + m + " 分";
					break;

				default:
					str = h + " 小时" + m + " 分" + s + " 秒";
					break;
				}

				break;
			}
			break;

		}
		return str;
	}


}
