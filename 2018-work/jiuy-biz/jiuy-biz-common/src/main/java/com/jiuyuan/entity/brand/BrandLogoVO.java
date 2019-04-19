package com.jiuyuan.entity.brand;

import java.util.List;

import com.jiuyuan.entity.shopping.DiscountInfo;

public class BrandLogoVO extends BrandLogo {

    /**
     * 
     */
    private static final long serialVersionUID = -695805275816142816L;

    private List<DiscountInfo> discountInfos;

    public List<DiscountInfo> getDiscountInfos() {
        return discountInfos;
    }

    public void setDiscountInfos(List<DiscountInfo> discountInfos) {
        this.discountInfos = discountInfos;
    }


}
