package com.reliable.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GainRealIpUtil {

	/**
	 * 获取真实Ip地址
	 * 
	 * @param
	 * @return 真实Ip地址
	 */

	public static String gainRealIp(HttpServletRequest request) {

		try {
			// 获取真实ip,排除代理ip
			String ipAddress = null;
			ipAddress = request.getHeader("Referer");

			// ipAddress = this.getRequest().getRemoteAddr();
			ipAddress = request.getHeader("X-Forwarded-For");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				// 更换niginx代理
				// ipAddress = request.getRemoteAddr();
				ipAddress = request.getHeader("X-Real-IP");
				if (ipAddress.equals("" + "") || ipAddress.endsWith("0:0:0:0:0:0:0:1")) {
					// 根据网卡取本机配置的IP

					// linux下也可以获取本地的ip地址
					Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
					InetAddress ip = null;
					while (allNetInterfaces.hasMoreElements()) {
						NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
						Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
						while (addresses.hasMoreElements()) {
							ip = (InetAddress) addresses.nextElement();
							if (ip != null && ip instanceof Inet4Address) {
								// 获取真实的Ip地址
								ipAddress = ip.getHostAddress();

							}
						}
					}

				}

			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割"***.***.***.***".length()=15
			if (ipAddress != null && ipAddress.length() > 15) {

				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}

			return ipAddress;
		} catch (Exception e) {

			// 记录错误返回空字符串
			return "";
		}

	}

}
