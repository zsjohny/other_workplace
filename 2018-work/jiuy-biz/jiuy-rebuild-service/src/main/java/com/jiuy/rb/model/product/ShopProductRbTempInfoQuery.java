package com.jiuy.rb.model.product; 

import lombok.Data; 

/**
 * ShopProductRbTempInfo的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Think
 * @version V1.0  
 * @date 2018年09月06日 下午 04:02:56
 * @Copyright 玖远网络 
*/
@Data
public class ShopProductRbTempInfoQuery extends ShopProductRbTempInfo {

    public static ShopProductRbTempInfo build(Long shopProductId, String skuJsonArray) {
        ShopProductRbTempInfo tempInfo = new ShopProductRbTempInfo ();
        tempInfo.setShopProductId (shopProductId);
        tempInfo.setSkuJson (skuJsonArray);
        tempInfo.setStatus (1);
        return tempInfo;
    }
}
