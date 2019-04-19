package com.e_commerce.miscroservice.product.vo;

import com.e_commerce.miscroservice.product.entity.ProductSku;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/15 15:10
 * @Copyright 玖远网络
 */
@Data
public class SkuOfProductDTO{

    private Long shopProductId;
    private Long supplierProductId;

    /**
     * 库存
     */
    private Integer inventory;
    /**
     * sku
     */
    private List<ProductSku> skuList;


    /**
     * 将sku根据商品id分组
     *
     * @param skuList skuList
     * @param supplierElseWxa true按照供应商商品id分组,false按照小程序商品id分组
     * @return java.util.Map<java.lang.Long   ,   com.e_commerce.miscroservice.product.vo.SkuOfProductDTO>
     * @author Charlie
     * @date 2019/1/15 16:59
     */
    public static Map<Long, SkuOfProductDTO> groupByProductId(List<ProductSku> skuList, boolean supplierElseWxa) {
        if (ObjectUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        //按照商品id对sku分组
        Map<Long, List<ProductSku>> productSkuMap = skuList.stream().collect(Collectors.groupingBy(sku -> supplierElseWxa ? sku.getProductId() : sku.getWxaProductId()));
        //组装
        Map<Long, SkuOfProductDTO> retVal = new HashMap<>(productSkuMap.size());
        productSkuMap.forEach((productId, skus) -> {
            SkuOfProductDTO dto = new SkuOfProductDTO();
            dto.setSkuList(skus);
            //总库存
            int inventory = skus.stream().reduce(0, (sum, sku) -> sku.getRemainCount() + sum, (sum, next) -> sum );
            dto.setInventory(inventory);
            retVal.put(productId, dto);
        });
        return retVal;
    }

}
