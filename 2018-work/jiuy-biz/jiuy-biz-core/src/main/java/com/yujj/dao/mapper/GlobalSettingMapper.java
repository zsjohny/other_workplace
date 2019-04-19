package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.dao.annotation.DBMaster;

@DBMaster
public interface GlobalSettingMapper {

    String getSetting(@Param( "name" ) GlobalSettingName name);

    Long getUpdateTime(@Param( "name" ) GlobalSettingName name);

    int update(@Param( "propertyName" ) String propertyName, @Param( "propertyValue" ) String propertyValue);

}
