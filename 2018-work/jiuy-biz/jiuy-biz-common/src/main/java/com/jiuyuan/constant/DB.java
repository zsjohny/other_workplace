package com.jiuyuan.constant;

import com.jiuyuan.util.constant.ConstantBinder;

public class DB {
	static {
        ConstantBinder.bind(DB.class, ConstantBinder.DEFAULT_CHARSET, "/db.properties");
    }
	
	public static String JDBC_DRIVERCLASSNAME;
	public static String JDBC_URL;
	public static String JDBC_USERNAME;
	public static String JDBC_PASSWORD;
}
