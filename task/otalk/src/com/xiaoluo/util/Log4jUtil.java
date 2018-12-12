/**
 * 
 */
package com.xiaoluo.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * 
 * 
 * @author wb-pengjian.d
 * @version $Id: Log4jUtil.java, 2015-10-27
 */

public class Log4jUtil {

	public static Logger init(Class<?> className) {
		// 获得logger
		Logger logger = Logger.getLogger(Log4jUtil.class);

		// 读取log4j的日志配置的位置
		// String saveLog = "WEB-INF\\properties\\log4j.properties";
		// 设置log4j的保存日志参数
		// System.setProperty("saveLog",
		// "WEB-INF\\properties\\log\\log.properties");
		// 读取配置
		Properties properties = new Properties();
		try {
			properties.load(Log4jUtil.class.getClassLoader().getResourceAsStream("defaultEmailParam.properties"));

			// 设置发送日志的名称
			System.setProperty("username",
					DesUtil.decrypt(properties.getProperty("dbName"), properties.getProperty("dbKey")));
			// 设置发送日志的密码
			System.setProperty("password",
					DesUtil.decrypt(properties.getProperty("dbPassword"), properties.getProperty("dbKey")));
			// 设置发送日志的主题
			System.setProperty("subject",
					DesUtil.decrypt(properties.getProperty("sendSubject"), properties.getProperty("dbKey")));

			// 设置发送日志的host
			System.setProperty("host",
					DesUtil.decrypt(properties.getProperty("dbHost"), properties.getProperty("dbKey")));

			// 设置发送的邮箱地址ַ
			System.setProperty("emailTo",
					DesUtil.decrypt(properties.getProperty("dbHost"), properties.getProperty("dbKey")));

			// 初始化配置文件的位置
			properties.load(className.getClassLoader().getResourceAsStream("log4j.properties"));
			// 设置到日志初始化
			PropertyConfigurator.configure(properties);

		} catch (Exception e) {
			logger.warn("log4j初始化出错" + e.getMessage());
		}

		// 返回日志
		return logger;
	}

	public static void main(String[] args) {
		System.out.println(DesUtil.decrypt("6PCBYkkhRAGrz31VGkM4CbMIGtjpdliS", "19921120"));
	}
}
