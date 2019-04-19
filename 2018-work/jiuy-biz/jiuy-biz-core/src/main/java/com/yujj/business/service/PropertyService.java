package com.yujj.business.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.dao.mapper.PropertyNameMapper;
import com.yujj.dao.mapper.PropertyValueMapper;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropValue;

@Service
public class PropertyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyService.class);
    @Autowired
    private PropertyNameMapper propertyNameMapper;

    @Autowired
    private PropertyValueMapper propertyValueMapper;

    @Autowired
    private MemcachedService memcachedService;

    public ProductPropName getPropertyName(long id) {
        Map<Long, ProductPropName> map = getPropertyNames(CollectionUtil.createList(id));
        return map.get(id);
    }

    public Map<Long, ProductPropName> getPropertyNames(Collection<Long> ids) {
        String groupKey = MemcachedKey.GROUP_KEY_PROPERTY_NAME;

        Map<Long, ProductPropName> result = new HashMap<Long, ProductPropName>();
        List<Long> idsNotCached = new ArrayList<Long>();
        for (Long id : ids) {
            String key = String.valueOf(id);
            Object obj = memcachedService.get(groupKey, key);
            if (obj != null) {
                result.put(id, (ProductPropName) obj);
            } else {
                idsNotCached.add(id);
            }
        }

        if (!idsNotCached.isEmpty()) {
            Map<Long, ProductPropName> noCachedMap = propertyNameMapper.getPropertyNames(idsNotCached);

            for (long id : noCachedMap.keySet()) {
                String key = String.valueOf(id);
                memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, noCachedMap.get(id));
            }
            result.putAll(noCachedMap);
        }
        return result;
    }
    
    public List<ProductPropName> getPropertyNamesListByIds(Collection<Long> ids) {
    	
    	return propertyNameMapper.getPropertyNamesListByIds(ids);
    	
    	
    }

    public Map<Long, ProductPropValue> getPropertyValues(Collection<Long> ids) {
        String groupKey = MemcachedKey.GROUP_KEY_PROPERTY_VALUE;

        Map<Long, ProductPropValue> result = new HashMap<Long, ProductPropValue>();
        List<Long> idsNotCached = new ArrayList<Long>();
        for (Long id : ids) {
            String key = String.valueOf(id);
            Object obj = memcachedService.get(groupKey, key);
            if (obj != null) {
                try {
                    result.put(id, (ProductPropValue) obj);
                } catch (Exception e) {
                    //ignore... 老的系统, 不知道为什么缓存取的是个数值类型, 如果是数值类型, 忽略重新重库里取, 覆盖
                    LOGGER.error ("com.yujj.business.service.PropertyService#getPropertyValues,从memcache中获取value转化成ProductPropValue失败 groupKey[{}].key[{}].value[{}]", groupKey, key, obj);
                    idsNotCached.add (id);
                }
            } else {
                idsNotCached.add(id);
            }
        }

        if (!idsNotCached.isEmpty()) {
            Map<Long, ProductPropValue> noCachedMap = propertyValueMapper.getPropertyValues(idsNotCached);

            for (long id : noCachedMap.keySet()) {
                String key = String.valueOf(id);
                memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, noCachedMap.get(id));
            }
            result.putAll(noCachedMap);
        }
        return result;
    }
    
    public List<ProductPropValue> getPropertyValuesByNameIds(Collection<Long> ids) {
    	return propertyValueMapper.getPropertyValuesByNameIds(ids);
    }
    
    public Map<Long, ProductPropValue> getValues() {
    	return propertyValueMapper.getValues();
    }
}
