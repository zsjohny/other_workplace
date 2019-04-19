package com.jiuy.rb.model.product;


import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/8 22:09
 * @Copyright 玖远网络
 */
@Data
public class ProductSkuRbSimpleVo{

    /**
     * skuId
     */
    private Long id;

    /**
     * 库存
     */
    private Integer remainCount;

    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private Integer status;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 尺码ID
     */
    private Long sizeId;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 尺码名称
     */
    private String sizeName;

    /**
     * build
     *
     * @param sku sku
     * @return com.jiuy.rb.model.product.ProductSkuRbSimpleVo
     * @author Charlie
     * @date 2018/9/8 22:15
     */
    public static ProductSkuRbSimpleVo build(ProductSkuRbNew sku) {
        ProductSkuRbSimpleVo vo = new ProductSkuRbSimpleVo ();
        if (vo != null) {
            vo.setColorId (sku.getColorId ());
            vo.setColorName (sku.getColorName ());
            vo.setSizeId (sku.getSizeId ());
            vo.setSizeName (sku.getSizeName ());
            vo.setStatus (sku.getStatus ());
            vo.setId (sku.getId ());
            vo.setRemainCount (sku.getRemainCount ());
        }
        return vo;
    }

}
