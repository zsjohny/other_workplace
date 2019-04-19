package com.yujj.dao.mapper;

import java.util.List;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.CategorySetting;

@DBMaster
public interface CategorySettingMapper {

	List<CategorySetting> load();

}
