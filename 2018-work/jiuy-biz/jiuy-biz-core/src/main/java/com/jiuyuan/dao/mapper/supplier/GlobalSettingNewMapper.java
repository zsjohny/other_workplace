package com.jiuyuan.dao.mapper.supplier;

import com.jiuyuan.entity.GlobalSetting;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.dao.annotation.DBMaster;

@DBMaster
public interface GlobalSettingNewMapper {

    String getSetting(@Param("name") GlobalSettingName name);

    /**
     * 根据属性名获取对象
     *
     * @param: propertyName
     * @return: com.jiuyuan.entity.GlobalSetting
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 6:25
     */
    GlobalSetting getSettingByPropertyName(@Param( "propertyName" ) String propertyName);


    /**
     * 更新
     *
     * @param: globalSetting
     * @return: int
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 6:31
     */
    int updateBySetting(@Param( "globalSetting" ) GlobalSetting globalSetting);

}
