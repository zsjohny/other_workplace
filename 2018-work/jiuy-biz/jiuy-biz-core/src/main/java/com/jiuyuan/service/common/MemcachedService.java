package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.util.adapter.ValueSupplier;
import com.jiuyuan.util.http.NumberUtil;
import com.jiuyuan.util.intercept.merge.annotation.MergeMap;
import com.jiuyuan.util.intercept.split.annotation.SplitParam;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class MemcachedService {

	private static final Logger log = LoggerFactory.getLogger(MemcachedService.class);

	private MemcachedClient memcachedClient;

	private String realKeyPrefix = "_customize_it"; // you should overwrite this
													// via spring IOC

	private String realKeyPrefixCommon = "_common";

	/**
	 * 设置前缀
	 * 
	 * @param realKeyPrefix
	 */
	public void setRealKeyPrefix(String realKeyPrefix) {
		this.realKeyPrefix = realKeyPrefix;
	}

	/**
	 * 添加记录
	 * 
	 * @param groupKey
	 * @param key
	 * @param expiry
	 *            有效时间
	 * @param value
	 *            值
	 * @return
	 */
	public boolean add(String groupKey, String key, int expiry, Object value) {
		String realKey = makeRealKey(groupKey, key);
		try {
			return this.memcachedClient.add(realKey, expiry, value).get();
		} catch (Exception e) {
			log.error("Memcached Add Object failed with key: {}", realKey, e.getMessage());
			return false;
		}
	}

	public Object get(String groupKey, String key) {
		String realKey = makeRealKey(groupKey, key);
		try {
			return this.memcachedClient.get(realKey);
		} catch (Exception e) {
			log.error("Memcached Get object failed with key: {}", realKey, e.getMessage());
			return null;
		}
	}

	/**
	 * 小程序和app共用的get方法
	 * 
	 * @param groupKey
	 * @param key
	 * @return
	 */
	public Object getCommon(String groupKey, String key) {
		String realKey = makeRealKeyCommon(groupKey, key);
		try {
			log.info("getCommon共用realKey:"+realKey);
			return this.memcachedClient.get(realKey);
		} catch (Exception e) {
			log.error("Memcached Get object failed with key: {}", realKey, e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 减
	 * @param groupKey
	 * @param key
	 * @param size 数量
	 * @return
	 */
	public long decrCommon(String groupKey, String key, long size) {
		String realKey = makeRealKeyCommon(groupKey, key);
		return this.memcachedClient.decr(realKey, size);
	}
	/**
	 * 加
	 * @param groupKey
	 * @param key
	 * @param size 数量
	 * @return
	 */
	public long incrCommon(String groupKey, String key, long size) {
		String realKey = makeRealKeyCommon(groupKey, key);
		return this.memcachedClient.incr(realKey,size);
	}

	public String getStr(String groupKey, String key) {
		String value = null;
		Object obj = get(groupKey, key);
		if (obj != null) {
			value = (String) obj;
		}
		return value;
	}

	/**
	 * 批量获取键集合对应的值集合
	 * 
	 * @param groupKey
	 * @param keys
	 * @return
	 */
	@MergeMap
	public Map<String, Object> getBulk(String groupKey, @SplitParam Collection<String> keys) {
		List<String> realKeys = new ArrayList<String>();
		for (String key : keys) {
			realKeys.add(makeRealKey(groupKey, key));
		}
		Map<String, Object> multiObjects = this.memcachedClient.getBulk(realKeys);
		return multiObjects != null ? multiObjects : new HashMap<String, Object>();
	}

	/**
	 * 删除
	 * 
	 * @param groupKey
	 * @param key
	 * @return
	 */
	public boolean remove(String groupKey, String key) {
		String realKey = makeRealKey(groupKey, key);
		try {
			OperationFuture<Boolean> delete = this.memcachedClient.delete(realKey);
			return delete.get();
		} catch (Exception e) {
			log.error("Memcached Remove Object failed with key: {}", realKey, e.getMessage());
			return false;
		}
	}

	/**
	 * 添加记录(永久有效) expiry 单位秒
	 */
	public boolean set(String groupKey, String key, Object value) {
		return set(groupKey, key, DateConstants.SECONDS_FOREVER, value);
	}

	/**
	 * 添加记录 expiry 单位秒
	 */
	public boolean set(String groupKey, String key, int expiry, Object value) {
		String realKey = makeRealKey(groupKey, key);
		try {
			return this.memcachedClient.set(realKey, expiry, value).get();
		} catch (Exception e) {
			log.error("Memcached Set Object failed with key: {}", realKey, e.getMessage());
			return false;
		}
	}

	/**
	 * 小程序和app共用的添加记录 expiry 单位秒
	 */
	public boolean setCommon(String groupKey, String key, int expiry, Object value) {
		String realKey = makeRealKeyCommon(groupKey, key);
		try {
			return this.memcachedClient.set(realKey, expiry, value).get();
		} catch (Exception e) {
			log.error("Memcached Set Object failed with key: {}", realKey, e.getMessage());
			return false;
		}
	}

	public boolean setCounter(String groupKey, String key, long counter) {
		return set(groupKey, key, 0, String.valueOf(counter));
	}

	public long addCounter(String groupKey, String key, long counter) {
		return this.memcachedClient.incr(makeRealKey(groupKey, key), 0, counter);
	}

	public long getCounter(String groupKey, String key) {
		return NumberUtil.parseLong(get(groupKey, key), -1, false);
	}

	public long incrCounter(String groupKey, String key, int by, ValueSupplier<Void, Long> valueSupplier) {
		long ret = incr(groupKey, key, by);
		if (ret == -1) {
			long initValue = valueSupplier != null ? valueSupplier.get(null) : 0;
			addCounter(groupKey, key, initValue);
			ret = incr(groupKey, key, by);
			if (ret == -1) {
				throw new IllegalStateException();
			}
		}
		return ret;
	}
	
	private long incr(String groupKey, String key, int by) {
		return this.memcachedClient.incr(makeRealKey(groupKey, key), by);
	}

	private String makeRealKey(String groupKey, String key) {
		groupKey = StringUtils.defaultString(groupKey);
		return EncodeUtil.encodeURL(realKeyPrefix + groupKey + key, "UTF-8");
	}

	/**
	 * 小程序和app共用的realKey
	 * 
	 * @param groupKey
	 * @param key
	 * @return
	 */
	private String makeRealKeyCommon(String groupKey, String key) {
		groupKey = StringUtils.defaultString(groupKey);
		return EncodeUtil.encodeURL(realKeyPrefixCommon + groupKey + key, "UTF-8");
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
	
	
	/**
	 * 加
	 * @param groupKey
	 * @param key
	 * @param size 数量
	 * @return
	 */
	public long incr(String groupKey, String key, long size) {
		return this.memcachedClient.incr(makeRealKey(groupKey, key),size);
	}
	
	
	/**
	 * 减
	 * @param groupKey
	 * @param key
	 * @param size 数量
	 * @return
	 */
	public long decr(String groupKey, String key, long size) {
		return this.memcachedClient.decr(makeRealKey(groupKey, key), size);
	}
	
	public long getLong(String groupKey, String key) {
		long value = 0;
		Object obj = get(groupKey, key);
		if (obj != null) {
			value = (long) obj;
		}
		return value;
	}
}
