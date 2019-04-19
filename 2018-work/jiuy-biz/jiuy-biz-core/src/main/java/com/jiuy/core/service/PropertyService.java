package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.modelv2.PropertyNameMapper;
import com.jiuy.core.dao.modelv2.PropertyValueMapper;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.service.common.MemcachedService;

@Service
public class PropertyService {

    @Autowired
    private PropertyNameMapper propertyNameMapper;

    @Autowired
    private PropertyValueMapper propertyValueMapper;
    
    @Autowired
    private MemcachedService memcachedService;

    @SuppressWarnings("unchecked")
	public Map<Long, ProductPropName> getPropertyNames() {
    	String groupKey = MemcachedKey.GROUP_KEY_PROPERTY_NAME;
    	String key = "all";
    	Object obj = memcachedService.get(groupKey, key);
    	if (obj != null) {
			return (Map<Long, ProductPropName>) obj;
		}

    	Map<Long, ProductPropName> result = new HashMap<Long, ProductPropName>();
        Map<Long, ProductPropName> noCachedMap = propertyNameMapper.getPropertyNames();
        result.putAll(noCachedMap);
        
        memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, result);
        
        return result;
    }

    @SuppressWarnings("unchecked")
	public Map<Long, ProductPropValue> getPropertyValues() {
    	String groupKey = MemcachedKey.GROUP_KEY_PROPERTY_VALUE;
    	String key = "all";
    	Object obj = memcachedService.get(groupKey, key);
    	if (obj != null) {
			return (Map<Long, ProductPropValue>) obj;
		}
    	
        Map<Long, ProductPropValue> result = new HashMap<Long, ProductPropValue>();
        Map<Long, ProductPropValue> noCachedMap = propertyValueMapper.getPropertyValues();
        result.putAll(noCachedMap);
        
        memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, result);
        
        return result;
    }

    public ProductPropValue getPropertyValue(String propertValue) {
        return getPropertyValueMap().get(propertValue);
    }

    public Map<String, ProductPropValue> getPropertyValueMap() {
        Map<String, ProductPropValue> map = propertyValueMapper.getPropertyValueMap();
        return map;
    }
    
    public Collection<ProductPropValue> getPropertyByNameId(PropertyName propertyName) {
        Map<String, ProductPropValue> ppMap = getPropertyValueMap();
        Collection<ProductPropValue> values = ppMap.values();
        Collection<ProductPropValue> subSet = new ArrayList<ProductPropValue>();
        if (null != values) {
            for (ProductPropValue ppv : values) {
            	long propertyId = propertyName.getValue();
                if (ppv.getPropertyNameId() == propertyId) {
                    subSet.add(ppv);
                }
            }
            return subSet;
        }
        return null;
    }

    public int getPropertyNameIdByName(String propertyName) {
        return propertyNameMapper.getPropertyNameIdByName(propertyName);
    }

    /**
     * 获取所有品牌对应的id
     * @return
     */
	public List<Integer> getBrandIds() {
		return propertyValueMapper.getBrandIds();
	}

	public List<Map<String, Object>> getColors() {
		return propertyValueMapper.getColors();
	}

	public List<Map<String, Object>> getBrands() {
		return propertyValueMapper.getBrands();
	}

	public ProductPropValue addBrand(String brandName, long brand_id) {
		ProductPropValue ppv = new ProductPropValue();
		long time = System.currentTimeMillis();
		
		ppv.setPropertyValue(brandName);
		ppv.setPropertyNameId(brand_id);
		ppv.setCreateTime(time);
		ppv.setUpdateTime(time);
		
		return propertyValueMapper.addBrand(ppv);
	}

	@Transactional(rollbackFor = Exception.class)
	public void remove(Collection<Long> brandIds) {
		propertyValueMapper.remove(brandIds);
	}

	public boolean chkRepeat(int propertyNameId, String name) {
		return propertyValueMapper.chkRepeat(propertyNameId, name) > 0 ? false : true;
	}

	public Map<Long, ProductPropValue> getValueMap(Collection<Long> propertyNameIds) {
		if(propertyNameIds.size() < 1) {
			return new HashMap<Long, ProductPropValue>();
		}
		Map<Long, ProductPropValue> map = new HashMap<Long, ProductPropValue>();
		List<ProductPropValue> productPropValues = propertyValueMapper.getValueMap(propertyNameIds);
		
		for(ProductPropValue productPropValue : productPropValues) {
			map.put(productPropValue.getId(), productPropValue);
		}
		
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<Long, List<ProductPropValue>> propertyValuesOfNameIdMap(Collection<Long> propertyNameIds) {
		String groupKey = MemcachedKey.GROUP_KEY_PROPERTY_VALUE; 
		StringBuilder key = new StringBuilder("");
		for (Long propertyNameId : propertyNameIds) {
			key.append(propertyNameId);
		}
		Object obj = memcachedService.get(groupKey, key.toString());
		List<ProductPropValue> propertyValues = null;
		if (obj != null) {
			propertyValues = (List<ProductPropValue>) obj;
		} else {
			propertyValues = propertyValueMapper.getPropertyValuesByNameId(propertyNameIds);
			memcachedService.set(groupKey, key.toString(), DateConstants.SECONDS_FIVE_MINUTES, propertyValues);
		}
		
		long lastNameId = 0;
		List<ProductPropValue> productPropValues = null;
		Map<Long, List<ProductPropValue>> propertyValueByNameId = new HashMap<Long, List<ProductPropValue>>();
		for(ProductPropValue productPropValue : propertyValues) {
			long nameId = productPropValue.getPropertyNameId();
			if(nameId != lastNameId) {
				lastNameId = nameId;
				productPropValues = new ArrayList<ProductPropValue>();
				propertyValueByNameId.put(lastNameId, productPropValues);
			}
			productPropValues.add(productPropValue);
		}
		
		return propertyValueByNameId;
	}

	public ProductPropValue add(String colorName, PropertyName propertyName) {
		ProductPropValue ppv = new ProductPropValue();
		long time = System.currentTimeMillis();
		
		ppv.setPropertyValue(colorName);
		ppv.setPropertyNameId(propertyName.getValue());
		ppv.setCreateTime(time);
		ppv.setUpdateTime(time);
		
		return propertyValueMapper.add(ppv);
	}

	public List<ProductPropValue> search(Long propertyNameId, String propertyValue) {
		List<ProductPropValue> productPropValues = new ArrayList<>();
		if (propertyValue != null) {
			for (ProductPropValue productPropValue : getPropertyValues().values()) {
				if (productPropValue.getPropertyNameId() == propertyNameId && StringUtils.equals(propertyValue, productPropValue.getPropertyValue())) {
					productPropValues.add(productPropValue);
					break;
				}
			}
			return productPropValues;
		}
		
		for (ProductPropValue productPropValue : getPropertyValues().values()) {
			if (productPropValue.getPropertyNameId() == propertyNameId) {
				productPropValues.add(productPropValue);
			}
		}
		
		return productPropValues;
	}

	/**
	 * 更新颜色的OrderIndex 用于拼音排序
	 */
	public void updateColorOrderIndex(){
		List<Map<String,Object>> colors = getColors();
		for (int i = 0; i < colors.size(); i++) {
			Map<String, Object> map = colors.get(i);
			String colorName = (String) map.get("PropertyValue");
			
		}
	}
}
