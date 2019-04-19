package com.jiuy.rb.model.product; 

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * SalesVolumeProductRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月15日 上午 10:55:45
 * @Copyright 玖远网络 
*/
@Data
public class SalesVolumeProductRbQuery extends SalesVolumeProductRb {


    /**
     * 商品id集合 null则不查询
     */
    private List<Long> productIds;




    public static class Builder{


        public static SalesVolumeProductRb newInstance(Long productId, Integer productType, Long salesVolume, Long collectionCount, Long starCount, Long orderSuccessCount, Long refundCount, Long orderCount, Long viewCount) {
            SalesVolumeProductRb entity = new SalesVolumeProductRb ();
            entity.setProductId (productId);
            entity.setProductType (productType);
            entity.setSalesVolume (salesVolume);
            entity.setCollectionCount (collectionCount);
            entity.setStarCount (starCount);
            entity.setOrderSuccessCount (orderSuccessCount);
            entity.setRefundCount (refundCount);
            entity.setOrderCount (orderCount);
            entity.setViewCount (viewCount);
            entity.setUpdateTime (new Date ());
            return entity;
        }

    }
}
