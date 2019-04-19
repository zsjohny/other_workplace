package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.user.StoreWxaDataQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 15:43
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreWxaMapper{


    List<Map<String,Object>> listAll(StoreWxaDataQuery query);


}
