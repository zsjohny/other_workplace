package org.dream.utils.http;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.IPAddress;

/**
 * @author Boyce 上午11:33:04
 */
public class IpTools {
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//ip配置
		
		if (ip.equals("139.129.221.2")||ip.equals("125.119.82.142")||ip.equals("127.0.0.1")||ip.equals("115.197.242.94")
	            || ip.endsWith("0:0:0:0:0:0:1")) {  
	        // 根据网卡取本机配置的IP  
	        InetAddress inet = null;  
	        try {  
	            inet = InetAddress.getLocalHost();  
	        } catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        }  
	        ip = inet.getHostAddress();  
	    }  
		return ip;
	}

	public static boolean isInnerIP(String ipAddress) {
		if (StringUtils.isBlank(ipAddress))
			return false;
		if (!IPAddress.isValid(ipAddress))
			return false;
		long ipNum = getIpNum(ipAddress);
		/**
		 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
		 * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
		 **/
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		return isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
				|| ipAddress.startsWith("127.");
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	public static void main(String[] args) {
		String ip="0:0:0:0:0:0:1";
		if (ip.equals("127.0.0.1")  
	            || ip.endsWith("0:0:0:0:0:0:1")) {  
	        // 根据网卡取本机配置的IP  
	        InetAddress inet = null;  
	        try {  
	            inet = InetAddress.getLocalHost();  
	        } catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        }  
	        ip = inet.getHostAddress();  
	    }
		System.out.println(isInnerIP("0:0:0:0:0:0:1"));
	}
}
