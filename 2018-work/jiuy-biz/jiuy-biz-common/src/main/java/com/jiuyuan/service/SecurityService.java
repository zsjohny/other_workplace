//package com.jiuyuan.business.service.common;
package com.jiuyuan.service;

import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	public static final Pattern URL_UNSAFE_CHARS = Pattern.compile("['\"<>\\r\\n]");

	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	@Value("#{safeDomains4Redirect}")
	private Set<String> safeDomains4Redirect;

	public String getSafeRedirectUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return "/";
		}

		url = URL_UNSAFE_CHARS.matcher(url).replaceAll("");

		if (url.startsWith("//") || url.startsWith("\\\\")) {
			return "/";
		}

		if (url.startsWith("/")) {
			return url;
		}

		try {
			String domain = "." + new URL(url).getHost().toLowerCase();
			if (StringUtils.contains(domain, "\\")) {
				return "/";
			}

			for (String safeDomain : safeDomains4Redirect) {
				if (StringUtils.isNotBlank(safeDomain) && domain.endsWith(safeDomain)) {
					return url;
				}
			}
		} catch (Exception e) {
			logger.error("unable to get safe redirect url: {}", url, e);
			return "/";
		}

		return "/";
	}
}
