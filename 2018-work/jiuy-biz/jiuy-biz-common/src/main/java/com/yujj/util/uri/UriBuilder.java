package com.yujj.util.uri;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yujj.util.uri.UriParams;
import com.yujj.util.uri.UriUtil;

public class UriBuilder {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private String uri;

    private UriParams params;

    public UriBuilder(String uri) {
        this(uri, DEFAULT_CHARSET, false);
    }

    public UriBuilder(String uri, String charset) {
        this(uri, charset, false);
    }

    public UriBuilder(String uri, String charset, boolean ignoreEmptyParam) {
        int questionIndex = uri.indexOf('?');
        if (questionIndex == -1) {
            this.uri = uri;
            this.params = new UriParams(charset, ignoreEmptyParam);
        } else {
            this.uri = uri.substring(0, questionIndex);
            if (questionIndex != uri.length() - 1) {
                this.params = UriUtil.parseQueryString(uri.substring(questionIndex + 1), charset, ignoreEmptyParam);
            } else {
                this.params = new UriParams(charset, ignoreEmptyParam);
            }
        }
    }

    public UriBuilder setUri(String uri) {
        int questionIndex = uri.indexOf('?');
        if (questionIndex == -1) {
            this.uri = uri;
        } else {
            this.uri = uri.substring(0, questionIndex);
            addAll(UriUtil.parseUriParams(uri, false));
        }
        return this;
    }

    public boolean contains(String key) {
        return this.params.contains(key);
    }

    public UriBuilder add(String key, Object value, boolean extractValueCollection) {
        this.params.add(key, value, extractValueCollection);
        return this;
    }

    public UriBuilder add(String key, Object value) {
        this.params.add(key, value);
        return this;
    }

    public UriBuilder addAll(Map<String, ?> map, boolean extractValueCollection) {
        this.params.addAll(map, extractValueCollection);
        return this;
    }

    public UriBuilder addAll(Map<String, ?> map) {
        this.params.addAll(map);
        return this;
    }

    public UriBuilder addAll(UriParams params) {
        this.params.addAll(params);
        return this;
    }

    public UriBuilder remove(String key) {
        this.params.remove(key);
        return this;
    }

    public UriBuilder removeAll(Collection<?> keys) {
        this.params.removeAll(keys);
        return this;
    }

    public UriBuilder clear() {
        this.params.clear();
        return this;
    }

    public UriBuilder removeEmptyParams() {
        this.params.removeEmptyParams();
        return this;
    }

    public UriBuilder set(String key, Object value, boolean extractValueCollection) {
        this.params.set(key, value, extractValueCollection);
        return this;
    }

    public UriBuilder set(String key, Object value) {
        this.params.set(key, value);
        return this;
    }

    public UriBuilder setAll(Map<String, ?> map, boolean extractValueCollection) {
        this.params.setAll(map, extractValueCollection);
        return this;
    }

    public UriBuilder setAll(Map<String, ?> map) {
        this.params.setAll(map);
        return this;
    }

    public UriBuilder setAll(UriParams params) {
        this.params.setAll(params);
        return this;
    }

    public UriBuilder replace(String key, Object value, boolean extractValueCollection) {
        this.params.replace(key, value, extractValueCollection);
        return this;
    }

    public UriBuilder replace(String key, Object value) {
        this.params.replace(key, value);
        return this;
    }

    public UriBuilder replaceAll(Map<String, ?> map, boolean extractValueCollection) {
        this.params.replaceAll(map, extractValueCollection);
        return this;
    }

    public UriBuilder replaceAll(Map<String, ?> map) {
        this.params.replaceAll(map);
        return this;
    }

    public UriBuilder replaceAll(UriParams params) {
        this.params.replaceAll(params);
        return this;
    }

    public String toUri() {
        StringBuilder builder = new StringBuilder(uri);
        if (!this.params.isEmpty()) {
            builder.append("?").append(this.params.join());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.toUri();
    }

    public boolean isParamsBlank() {
        return this.params == null || this.params.isEmpty();
    }

    public boolean isBlank() {
        return StringUtils.isBlank(this.uri) && (this.params == null || this.params.isEmpty());
    }

    public UriParams getParams() {
        return this.params;
    }

    public static void main(String[] args) {
    }
}
