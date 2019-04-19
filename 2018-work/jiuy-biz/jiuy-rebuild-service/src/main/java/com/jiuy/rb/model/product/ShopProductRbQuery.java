package com.jiuy.rb.model.product; 

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.ShopProductOwnEnum;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ShopProductRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 *
 * @author Aison
 * @version V1.0
 * @date 2018年07月05日 下午 05:51:46
 * @Copyright 玖远网络
 */
@Data
public class ShopProductRbQuery extends ShopProductRb{


    /**
     * 商品标签列表
     */
    private List<ShopTagRb> shopTagRbList;


    /**
     * sku json
     */
    private String skuJsonArray;
    /**
     * sku简单对象
     */
    private List<ProductSkuRbSimpleVo> skuList;
    /**
     * 尺码
     */
    private Map<Long, String> sizeIdName;
    /**
     * 颜色
     */
    private Map<Long, String> colorIdName;

    private String keyword;

    private List<String> ids;

    private String videoUrl;
    private Long videoFileId;
    private String videoImage;

    /**
     * 创建一个默认的自营同款商品
     *
     * @param param param
     * @return com.jiuy.rb.model.product.ShopProductRb
     * @author Charlie
     * @date 2018/9/4 11:26
     */
    public static ShopProductRb createShopProduct(ShopProductRbQuery param) {
        ShopProductRb newProduct = new ShopProductRb ();
        newProduct.setName (param.getName ());
        newProduct.setPrice (param.getPrice ());
        newProduct.setOwn (param.getOwn ());
        if (ShopProductOwnEnum.SELF_SAMPLE_STYLE.isThis (param.getOwn ())) {
            newProduct.setProductId (param.getProductId ());
        }
        newProduct.setClothesNumber (param.getClothesNumber ());
        newProduct.setTabType (1);
        long current = System.currentTimeMillis ();
        newProduct.setCreateTime (current);
        newProduct.setUpdateTime (current);
        newProduct.setStoreId (param.getStoreId ());
        newProduct.setTagIds (param.getTagIds ());
        newProduct.setFirstTimeOnSale (0L);
        return newProduct;
    }


    public static ShopProductRbQuery me(Long id, String name, Double price, String clothesNumber,
                                        String summaryImages, String shopOwnDetail, Integer soldOut, String videoDisplayUrl, Long videoFileId,
                                        String videoImage, String tagIds, String skuJsonArray, Integer own, Long storeId, String colorIds, String sizeIds, Long productId) {
        ShopProductRbQuery param = new ShopProductRbQuery ();
        param.setId (id);
        param.setName (name);
        param.setPrice (price == null ? null : new BigDecimal (String.valueOf (price)));
        param.setClothesNumber (clothesNumber);
        param.setSummaryImages (summaryImages);
        param.setShopOwnDetail (shopOwnDetail);
        param.setSoldOut (soldOut);
        param.setTagIds (tagIds);
        param.setVideoDisplayImage (videoImage);
        param.setVideoDisplayFileid (videoFileId);
        param.setVideoDisplayUrl (videoDisplayUrl);
        param.setSkuJsonArray (skuJsonArray);
        param.setOwn (own);
        if (StringUtils.isNotBlank (summaryImages)) {
            String[] imageArray = summaryImages.trim ().split (",");
            JSONArray jsonArray = (JSONArray) JSON.toJSON (imageArray);
            param.setSummaryImages (jsonArray.toJSONString ());
        }
        param.setStoreId (storeId);
        param.setColorIds (colorIds);
        param.setSizeIds (sizeIds);
        param.setProductId (productId);
        return param;
    }

}
