package com.e_commerce.miscroservice.product.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 13:37
 */
public interface SupplierUserProductConfigDao {

    /**
     * 查询混批提示
     *
     * @param brandIdList brandIdList
     * @return java.util.Map<java.lang.Long   ,   java.lang.String>
     * @author Charlie
     * @date 2019/2/15 13:43
     */
    Map<Long,String> listTholesaleTipByBrandIds(List<Long> brandIdList);
}
