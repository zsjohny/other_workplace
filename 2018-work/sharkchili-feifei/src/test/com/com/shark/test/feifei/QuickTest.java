package com.com.shark.test.feifei;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/11/26 0026
 */
public class QuickTest {
	public static void main(String[] args) {
		System.out.println("app class");
		ClassLoader appLoader = QuickTest.class.getClassLoader();
		while (appLoader != null) {
			System.out.println(appLoader.toString());
			appLoader = appLoader.getParent();
		}
		System.out.println("jar class");
		ClassLoader jarLoader = QuickTest.class.getClassLoader();
		while (jarLoader != null) {
			System.out.println(jarLoader.toString());
			jarLoader = jarLoader.getParent();
		}
	}
}
