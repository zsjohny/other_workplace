package com.jiuy;

import com.jiuy.util.MainEnter;



/**
 * 创建
 * @author Aison
 * @date   2018/5/24 14:08
 * @version V1.0
 * @Copyright 玖远网络
 */
public class DoCreate {

	public static void main(String[] args) {

		MainEnter enter = new MainEnter();
		String rs = enter.doCreate("E:\\jiuyuan\\project\\2018-jiuy\\jiuy-dev-tools\\src\\main\\java\\com\\jiuy\\mybastisConfig.xml").toString();
		System.out.println(rs);
	}
}
