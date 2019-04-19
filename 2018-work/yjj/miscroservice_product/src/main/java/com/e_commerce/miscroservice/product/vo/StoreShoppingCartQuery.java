package com.e_commerce.miscroservice.product.vo;

import com.e_commerce.miscroservice.product.entity.StoreShoppingCart;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/30 17:34
 * @Copyright 玖远网络
 */
@Data
public class StoreShoppingCartQuery extends StoreShoppingCart{

    private Integer pageSize;
    private Integer pageNumber;

    private List<Long> ids;

    public void setIdsArr(Long[] cartIds) {
        ids = Arrays.asList (cartIds);
    }
}
