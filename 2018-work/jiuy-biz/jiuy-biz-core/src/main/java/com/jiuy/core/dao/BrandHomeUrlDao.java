package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

public interface BrandHomeUrlDao {

	int addBrandUrl(long partnerId, String brandUrl, String template);

	String getTemplateUrl(String brandUrl);

	List<Map<String, Object>> getHomeSettingInfo(long partnerId);

	int activeBrandUrl(long partnerId);

	int deactiveBrandUrl(long partnerId);

}
