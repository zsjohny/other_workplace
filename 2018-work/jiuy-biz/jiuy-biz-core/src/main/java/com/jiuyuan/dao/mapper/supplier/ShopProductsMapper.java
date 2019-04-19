package com.jiuyuan.dao.mapper.supplier;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.storeorder.StoreOrderLog;
import org.apache.ibatis.annotations.Param;


/**
  * 微信小程序-shop_product
  * @return
  * @author hyf
  * @date 2018/8/20 10:54
  */
@DBMaster
public interface ShopProductsMapper extends BaseMapper<StoreOrderLog> {


     void soldOut(@Param("productId") long productId);
 }