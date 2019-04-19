package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.meta.global.ProductSeasonWeight;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.GlobalSetting;

public interface GlobalSettingDao {
	
	public int deleteSetting(String propertyName);

	public int update(GlobalSetting globalSetting);

	public GlobalSetting getSettingByPropertyName(String propertyName);

	public List<GlobalSetting> getItems(Collection<String> propertyNames);

	public String getSetting(GlobalSettingName name);

	public int add(GlobalSetting globalSetting);

	public int update(String propertyName, String propertValue);
	
	public int resetProductSeasonWeight();
	
	public int addProductSeasonWeight(Collection<ProductSeasonWeight> productSeasonWeights);

	public String getSettingByStringPropertyName(String string);
}
