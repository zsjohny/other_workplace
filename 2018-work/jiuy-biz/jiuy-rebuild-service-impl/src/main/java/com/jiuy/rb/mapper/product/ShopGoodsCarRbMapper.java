package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.ShopGoodsCarRb; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 小程序商城购物车 的mapper 文件
 
 * @author Think
 * @version V1.0 
 * @date 2018年09月04日 下午 07:18:37
 * @Copyright 玖远网络 
 */
public interface ShopGoodsCarRbMapper extends BaseMapper<ShopGoodsCarRb>{

    // @Costom
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 将购物车设为失效
     *
     * @param storeId storeId
     * @param skuIds skuIds
     * @return int
     * @author Charlie
     * @date 2018/9/4 19:49
     */
    int updateGoodsCarDisabled(@Param ("storeId") Long storeId, @Param ("skuIds") List<Long> skuIds);
}