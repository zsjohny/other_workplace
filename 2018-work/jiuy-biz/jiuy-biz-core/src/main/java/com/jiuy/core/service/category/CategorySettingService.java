package com.jiuy.core.service.category;

import java.util.List;

import com.jiuyuan.entity.CategorySetting;
import com.jiuyuan.entity.query.PageQuery;

public interface CategorySettingService {

	int searchCount(String name);

	List<CategorySetting> search(PageQuery pageQuery, String name);

	int add(CategorySetting categorySetting);

	int remove(Long id);

	int update(CategorySetting categorySetting);

	int update(Long id, String content);

	CategorySetting search(Long id);

	List<CategorySetting> search(Integer linkType);

}
