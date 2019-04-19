package com.yujj.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.Platform;
import com.jiuyuan.util.http.HttpUtil;
import com.jiuyuan.util.spring.ControllerUtil;

@Controller
@RequestMapping("/download")
public class DownloadController {

	// private static final String WEIXIN_FALLBACK = "/static/download.html";

	// private static final String WEIXIN_DOWNLOAD_URL =
	// "http://a.app.qq.com/o/simple.jsp?pkgname=com.yujiejie.jiuyuan";
	private static final String WEIXIN_DOWNLOAD_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.yujiejie.mendian";

	@RequestMapping
	public String download(Platform platform, HttpServletRequest request) {
		String url = platform.isIOS() ? Client.IPHONE_LATEST_URL : Client.ANDROID_LATEST_URL;
		if (HttpUtil.isWeixin(request)) {
			// url = new UriBuilder(WEIXIN_FALLBACK).set("url", url).toUri();
			url = WEIXIN_DOWNLOAD_URL;
		}

		return ControllerUtil.redirect(url);
	}

	@RequestMapping("/{os}")
	public String downloadPlatform(@PathVariable("os") String os, HttpServletRequest request) {
		Platform platform = Platform.parse(os, Platform.DESKTOP);
		String url = platform.isIOS() ? Client.IPHONE_LATEST_URL : Client.ANDROID_LATEST_URL;
		if (HttpUtil.isWeixin(request)) {
			// url = new UriBuilder(WEIXIN_FALLBACK).set("url", url).toUri();
			url = WEIXIN_DOWNLOAD_URL;
		}

		return ControllerUtil.redirect(url);
	}
}
