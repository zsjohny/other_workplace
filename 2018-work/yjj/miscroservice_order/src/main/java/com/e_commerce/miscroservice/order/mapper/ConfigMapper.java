package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.colligate.YjjConfig;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 描述
 * @date 2018/9/18 17:25
 * @return
 */
@Mapper
public interface ConfigMapper extends BaseMapper<YjjConfig> {

    @Select("SELECT * FROM `yjj_config` con where con.key_name=#{name}")
    YjjConfig getConfigValue(@Param("name")String name) ;
}
