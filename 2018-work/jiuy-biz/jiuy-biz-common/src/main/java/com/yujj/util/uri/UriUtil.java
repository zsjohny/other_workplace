package com.yujj.util.uri;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;


public class UriUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static UriParams parseQueryString(String params, boolean ignoreBlankParam) {
        return parseQueryString(params, DEFAULT_CHARSET, ignoreBlankParam);
    }

    public static UriParams parseQueryString(String queryString, String charset, boolean ignoreBlankParam) {
        UriParams params = new UriParams(charset, ignoreBlankParam);
        if (StringUtils.isBlank(queryString)) {
            return params;
        }

        if (queryString.startsWith("?")) {
            queryString = queryString.substring(1);
        }

        String[] parts = queryString.split("&");
        for (String part : parts) {
            if (part.length() > 0) {
                int index = part.indexOf('=');
                if (index == -1) {
                    params.add(part, "");
                } else {
                    String name = decodeUriComponent(part.substring(0, index), charset);
                    if (index < part.length() - 1) {
                        String value = decodeUriComponent(part.substring(index + 1), charset);
                        params.add(name, value);
                    } else {
                        params.add(name, "");
                    }
                }
            }
        }
        return params;
    }

    public static UriParams parseUriParams(String uri, boolean ignoreBlankParam) {
        return parseUri(uri, DEFAULT_CHARSET, ignoreBlankParam);
    }

    public static UriParams parseUri(String uri, String charset, boolean ignoreBlankParam) {
        try {
            return parseQueryString(new URI(uri).getQuery(), charset, ignoreBlankParam);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to parse params from uri: " + uri);
        }
    }

    public static String decodeUriComponent(String component, String charset) {
        try {
            return URLDecoder.decode(component, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("", e);
        }
    }

    public static String concatUri(String uri, UriParams params) {
        return concatUri(uri, params, DEFAULT_CHARSET);
    }

    public static String concatUri(String uri, UriParams params, String charset) {
        return new UriBuilder(uri, charset).addAll(params).toUri();
    }
}
