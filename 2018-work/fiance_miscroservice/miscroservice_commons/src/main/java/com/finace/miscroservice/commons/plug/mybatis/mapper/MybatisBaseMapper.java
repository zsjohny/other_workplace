package com.finace.miscroservice.commons.plug.mybatis.mapper;

import com.finace.miscroservice.commons.plug.mybatis.handler.MybatisAnnotationHandler;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * mybatis的通用mapper
 */
public interface MybatisBaseMapper {


    @SelectProvider(type = MybatisAnnotationHandler.class, method = "selectOne")
    Map selectOne(String sql);


    @UpdateProvider(type = MybatisAnnotationHandler.class, method = "update")
    int update(String sql);

    @SelectProvider(type = MybatisAnnotationHandler.class, method = "selectAll")
    List<Map> selectAll(String sql);

    @InsertProvider(type = MybatisAnnotationHandler.class, method = "save")
    int save(String sql);

}
