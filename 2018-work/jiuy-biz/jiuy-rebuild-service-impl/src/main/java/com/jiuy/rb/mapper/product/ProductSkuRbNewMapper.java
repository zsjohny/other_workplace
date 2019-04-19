package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.ProductSkuRbNew; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 *  的mapper 文件
 
 * @author Administrator
 * @version V1.0 
 * @date 2018年09月03日 下午 03:22:34
 * @Copyright 玖远网络 
 */
public interface ProductSkuRbNewMapper extends BaseMapper<ProductSkuRbNew>{


    // @Costom
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面



    /**
     * 删除小程序商品的sku
     *
     * @param shopProductId shopProductId
     * @return int
     * @author Charlie
     * @date 2018/9/4 19:11
     */
    int deleteShopProductSkuByShopProductId(@Param ("shopProductId") Long shopProductId);

    /**
     * 批量删除sku
     *
     * @param skuIds skuIds
     * @author Charlie
     * @date 2018/9/4 21:37
     */
    int deleteShopProductSkuByIds(@Param ("skuIds") List<Long> skuIds);



}