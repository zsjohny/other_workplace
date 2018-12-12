package com.finace.miscroservice.commons.utils.tools;

import java.util.ResourceBundle;

public class ProperUtil {

	/**
     * 读取配置文件中的属性(配置文件必须放在classes目录下)
     * @param configName 配置文件的文件名(不带后缀)
     * @param propKey 属性的键
     * @return String
     */
    public static String getProperty(String configName, String propKey) {
		return ResourceBundle.getBundle(configName).getString(propKey);
	}
    
    public static void main(String[] args) {
    	String url=getProperty("config","url");
    	System.out.println(url);
	}
}
