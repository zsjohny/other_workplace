package com.e_commerce.miscroservice.task.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/29 10:51
 */
@Mapper
public interface SensWordMapper {

    /**
     * 查询所有
     *
     * @return java.util.List<java.lang.String>
     * @author Charlie
     * @date 2019/1/29 10:48
     */
    List<String> findAll();

}
