package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.product.dao.SupplierUserProductConfigDao;
import com.e_commerce.miscroservice.product.mapper.SupplierUserProductConfigMapper;
import com.e_commerce.miscroservice.product.vo.SupplierProductDTO;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 13:37
 */
@Component
public class SupplierUserProductConfigDaoImpl implements SupplierUserProductConfigDao {

    @Autowired
    private SupplierUserProductConfigMapper supplierUserProductConfigMapper;

    @Override
    public Map<Long, String> listTholesaleTipByBrandIds(List<Long> brandIdList) {
        if (ObjectUtils.isEmpty(brandIdList)) {
            return Collections.emptyMap();
        }

        List<SupplierProductDTO > tholesales =   supplierUserProductConfigMapper.listTholesaleTipByBrandIds(brandIdList);
        if (tholesales.isEmpty()) {
            return Collections.emptyMap();
        }

        return tholesales.stream()
                .collect(Collectors.toMap(SupplierProductDTO::getBrandId, supplierProductDTO -> {
                    double wholesaleCost = supplierProductDTO.getWholesaleCost();
                    return generTip(supplierProductDTO.getWholesaleCount(), wholesaleCost, (int)wholesaleCost);
                }));
    }



    /**
     * 老系统搬来的...
     *
     * @param wholesaleCount wholesaleCount
     * @param wholesaleCost wholesaleCost
     * @param cost cost
     * @return java.lang.String
     * @author Charlie
     * @date 2019/2/15 14:06
     */
    public static String generTip(int wholesaleCount, double wholesaleCost, int cost) {
        String wholesaleLimitTip = "";
        if(wholesaleCount > 0 ){
            wholesaleLimitTip = "满" + wholesaleCount+"件";
        }
        if(wholesaleCost > 0){
            if(wholesaleLimitTip.length() > 0){
                wholesaleLimitTip = wholesaleLimitTip + "且" ;
            }
            if(wholesaleCost == cost){
                wholesaleLimitTip = wholesaleLimitTip +"满"+cost+"元订单总价";
            }else{
                wholesaleLimitTip = wholesaleLimitTip +"满"+wholesaleCost+"元";
            }
        }
        if(StringUtils.isNotBlank(wholesaleLimitTip)){
            wholesaleLimitTip = "全店"+wholesaleLimitTip+"可混批采购";
        }
        return wholesaleLimitTip;
    }
}
