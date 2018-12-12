package org.dream.utils.http;

public class HttpToolsTest {
	public static void main(String[] args) {
		System.out.println(HttpTools.urlGet("http://www.baidu.com", null, "utf-8", true));
		System.out.println(HttpTools.urlPost("http://www.baidu.com", null, "utf-8", true));
	}
}
