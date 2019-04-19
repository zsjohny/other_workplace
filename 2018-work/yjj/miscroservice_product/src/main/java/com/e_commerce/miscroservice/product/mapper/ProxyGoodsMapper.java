package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述
 * @date 2018/9/18 17:25
 * @return
 */
@Mapper
public interface ProxyGoodsMapper extends BaseMapper<ProxyGoods> {

    @Select("SELECT * FROM yjj_proxy_goods order by id")
    List<ProxyGoods> getGoodsList();

}
