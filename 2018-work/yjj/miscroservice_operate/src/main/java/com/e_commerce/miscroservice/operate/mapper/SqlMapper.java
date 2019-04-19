package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/8 9:59
 * @Copyright 玖远网络
 */
@Mapper
public interface SqlMapper {

    int updShareShopQrImg(@Param( "storeId" ) Long storeId);

}
