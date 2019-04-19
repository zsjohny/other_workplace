package com.yujj.util.uri;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;

//import com.jiuy.util.http.HttpUtil;
import com.jiuyuan.util.EncodeUtil;
//import com.yujj.util.http.HttpUtil;
import com.jiuyuan.util.http.HttpUtil;

public class UriParams implements Serializable {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final long serialVersionUID = -6297812954658517782L;

    private Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();

    private String charset;

    private boolean ignoreEmptyParam;

    public UriParams() {
        this(DEFAULT_CHARSET, false);
    }

    public UriParams(String charset, boolean ignoreEmptyParam) {
        this.charset = charset;
        this.ignoreEmptyParam = ignoreEmptyParam;
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public List<String> get(String key) {
        return Collections.unmodifiableList(map.get(key));
    }

    public String getSingle(String key, boolean safe) {
        List<String> values = map.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        } else if (values.size() == 1 || safe) {
            return values.get(0);
        } else {
            throw new RuntimeException("Multiple values exist for key: " + key + ", values: " + values);
        }
    }

    public String getSingle(String key) {
        return getSingle(key, false);
    }

    public String getSingle(String key, String defaultValue, boolean safe) {
        return StringUtils.defaultString(getSingle(key, safe), defaultValue);
    }

    public String getSingle(String key, String defaultValue) {
        return getSingle(key, defaultValue, false);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public UriParams add(String key, Object value, boolean extractValueCollection) {
        if (value != null && extractValueCollection) {
            if (value instanceof Collection) {
                for (Object obj : (Collection<?>) value) {
                    add(key, obj, false);
                }
                return this;
            } else if (value.getClass().isArray()) {
                int size = Array.getLength(value);
                for (int i = 0; i < size; i++) {
                    Object obj = Array.get(value, i);
                    add(key, obj, false);
                }
                return this;
            }
        }

        // always add an entry to keep order of keys
        List<String> values = map.get(key);
        if (values == null) {
            values = new ArrayList<String>();
            map.put(key, values);
        }

        String val = value != null ? value.toString() : "";
        if (StringUtils.isNotEmpty(val) || !ignoreEmptyParam) {
            values.add(val);
        }

        return this;
    }

    public UriParams add(String key, Object value) {
        return add(key, value, true);
    }

    public UriParams addAll(Map<?, ?> map, boolean extractValueCollection) {
        if (map != null) {
            for (Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                if (key != null) {
                    add(key.toString(), entry.getValue(), extractValueCollection);
                }
            }
        }
        return this;
    }

    public UriParams addAll(Map<?, ?> map) {
        return addAll(map, true);
    }

    public UriParams addAll(UriParams params) {
        if (params != null) {
            for (Entry<String, List<String>> entry : params.map.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public UriParams remove(String key) {
        // do not delete the entry, or the order of keys may differ
        List<String> values = map.get(key);
        if (values != null) {
            values.clear();
        }
        return this;
    }

    public UriParams removeAll(Collection<?> keys) {
        if (keys != null) {
            for (Object key : keys) {
                if (key != null) {
                    remove(key.toString());
                }
            }
        }
        return this;
    }

    public UriParams clear() {
        // do not delete the entries, or the order of keys may differ
        for (List<String> values : map.values()) {
            values.clear();
        }
        return this;
    }

    public void removeEmptyParams() {
        for (Entry<String, List<String>> entry : map.entrySet()) {
            Iterator<String> valueIterator = entry.getValue().iterator();
            while (valueIterator.hasNext()) {
                String value = valueIterator.next();
                if (StringUtils.isBlank(value)) {
                    valueIterator.remove();
                }
            }
        }
    }

    public UriParams set(String key, Object value, boolean extractValueCollection) {
        remove(key);
        add(key, value, extractValueCollection);
        return this;
    }

    public UriParams set(String key, Object value) {
        remove(key);
        add(key, value, true);
        return this;
    }

    public UriParams setAll(Map<?, ?> map, boolean extractValueCollection) {
        if (map != null) {
            removeAll(map.keySet());
            addAll(map, extractValueCollection);
        }
        return this;
    }

    public UriParams setAll(Map<?, ?> map) {
        return setAll(map, true);
    }

    public UriParams setAll(UriParams params) {
        if (params != null) {
            removeAll(params.keySet());
            addAll(params);
        }
        return this;
    }

    public UriParams replace(String key, Object value, boolean extractValueCollection) {
        if (contains(key)) {
            remove(key);
            add(key, value, extractValueCollection);
        }
        return this;
    }

    public UriParams replace(String key, Object value) {
        if (contains(key)) {
            remove(key);
            add(key, value);
        }
        return this;
    }

    public UriParams replaceAll(Map<?, ?> map, boolean extractValueCollection) {
        if (map != null) {
            for (Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                if (key != null) {
                    replace(key.toString(), entry.getValue(), extractValueCollection);
                }
            }
        }
        return this;
    }

    public UriParams replaceAll(Map<?, ?> map) {
        return replaceAll(map, true);
    }

    public UriParams replaceAll(UriParams params) {
        if (params != null) {
            for (Entry<String, List<String>> entry : params.map.entrySet()) {
                replace(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public List<NameValuePair> asNameValuePairs() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Entry<String, List<String>> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                NameValuePair nvp = new BasicNameValuePair(entry.getKey(), value);
                nvps.add(nvp);
            }
        }

        return nvps;
    }

    public MultipartEntity asMultipartEntity(String charset) {
        MultipartEntity entity = new MultipartEntity();

        for (Entry<String, List<String>> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                entity.addPart(entry.getKey(), HttpUtil.createStringBody(value, charset));
            }
        }

        return entity;
    }

    public Map<String, List<String>> asMap() {
        Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            result.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
        }
        return result;
    }

    public Map<String, String[]> asArrayMap() {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            List<String> value = entry.getValue();
            result.put(entry.getKey(), value.toArray(new String[value.size()]));
        }
        return result;
    }

    public Map<String, String> asSingleValueMap() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            List<String> values = entry.getValue();
            if (!values.isEmpty()) {
                // the last value takes precedence
                result.put(entry.getKey(), values.get(values.size() - 1));
            }
        }
        return result;
    }

    public Map<String, String> asSingleValueMap(String joinStr) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            List<String> values = entry.getValue();
            if (!values.isEmpty()) {
                result.put(entry.getKey(), StringUtils.join(values, joinStr));
            } else if (!ignoreEmptyParam) {
                result.put(entry.getKey(), "");
            }
        }
        return result;
    }

    public String join() {
    	return join(true);
    }
    
    public String join(boolean bEncode) {
    	
        StringBuilder builder = new StringBuilder();

        for (Entry<String, List<String>> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                if (bEncode) {
	                builder.append(EncodeUtil.encodeURL(entry.getKey(), charset)).append("=");
	                builder.append(EncodeUtil.encodeURL(value, charset));
                }
                else {
	                builder.append(entry.getKey()).append("=");
	                builder.append(value);
                }
            }
        }

        return builder.toString();
    }

    public String joinAsBase64(String charset) {
        return EncodeUtil.encodeBase64URLSafeString(join(), charset);
    }

    @Override
    public String toString() {
        return join();
    }
    public String toUnEncodeString() {
        return join(false);
    }

    public boolean isEmpty() {
        if (this.map == null || this.map.isEmpty()) {
            return true;
        }
        for (List<String> values : this.map.values()) {
            if (values.size() > 0) {
                return false;
            }
        }
        return true;
    }
}
