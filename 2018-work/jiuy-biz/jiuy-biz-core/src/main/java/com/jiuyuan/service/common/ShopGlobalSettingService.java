package com.jiuyuan.service.common;

import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.GlobalSettingNewMapper;
import com.jiuyuan.service.common.MemcachedService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopGlobalSettingService {

    @Autowired
    private GlobalSettingNewMapper globalSettingMapper;
    
    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    public JSONObject getJsonObjectNoCache(GlobalSettingName name) {
        String value = getSettingNoCache(name);
        return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
    }
    
	public String getSettingNoCache(GlobalSettingName name) {
		return globalSettingMapper.getSetting(name);
	}

	public int getInt(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
    }
	
	public String getSetting(GlobalSettingName name) {
        String groupKey = MemcachedKey.GROUP_KEY_GLOBAL_SETTING;
        String key = name.getStringValue();
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (String) obj;
        }

        String setting = globalSettingMapper.getSetting(name);
        if (setting != null) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, setting);
        }
        return setting;
    }

	public JSONArray getJsonArray(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? new JSONArray() : JSON.parseArray(value);
    }

	public double getDouble(GlobalSettingName name) {
    	String value = getSetting(name);
    	return StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
    }

    public <T> T getBean(GlobalSettingName name, Class<T> clazz) {
        String value = getSetting(name);
        T obj = null;
        try {
        	obj = StringUtils.isBlank(value) ? clazz.newInstance() : JSON.parseObject(value, clazz);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return obj;
    }
	
    public JSONObject getJsonObject(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
    }

    /**
     * 获取价类 和进货渠道的选项
     * @param storeBusiness
     * @date:   2018/4/27 14:28
     * @author: Aison
     */
    public Map<String,List<Map<String,Object>>> getStorePriceAndPurc(StoreBusiness storeBusiness) {
        List<DataDictionary> dataDictionaries =  dataDictionaryService.getDictionaryGroups(new String[]{"purchaseChannel","priceLevel"});
        Map<String,List<Map<String,Object>>> map = new HashMap<>();
        List<Map<String,Object>> priceList = new ArrayList<>();
        List<Map<String,Object>> purchase = new ArrayList<>();
        String purchaseChannelCode = storeBusiness.getPurchaseChannel();
        String priceLevelCode = storeBusiness.getPriceLevel();

        for (DataDictionary dataDictionary : dataDictionaries) {
            Map<String,Object> itemMap = BizUtil.bean2Map(dataDictionary);
            if("purchaseChannel".equals(dataDictionary.getGroupCode())) {
                if(purchaseChannelCode!=null && purchaseChannelCode.indexOf(dataDictionary.getCode())>=0) {
                    itemMap.put("checked",true);
                }
                purchase.add(itemMap);
            }
            if("priceLevel".equals(dataDictionary.getGroupCode())) {
                if(priceLevelCode!=null && priceLevelCode.indexOf(dataDictionary.getCode())>=0) {
                    itemMap.put("checked",true);
                }
                priceList.add(itemMap);
            }
        }
        map.put("purchaseChannels",purchase);
        map.put("priceLevels",priceList);

        return map;
    }

    /**
     * 通过属性名查找
     *
     * @param: globalSettingName
     * @return: com.jiuyuan.entity.GlobalSetting
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 6:28
     */
    public GlobalSetting getItem(GlobalSettingName globalSettingName) {
        String gss = globalSettingName.getStringValue();
        return globalSettingMapper.getSettingByPropertyName(gss);
    }


    /**
     * 更新
     *
     * @param: globalSetting
     * @return: int
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 6:31
     */
    public int update(GlobalSetting globalSetting) {
        return globalSettingMapper.updateBySetting(globalSetting);
    }
}