package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import lombok.Data;

@Data
public class ProxyOrderVo extends ProxyOrder {

    private String oneLevelName;

    private String twoLevelName;

}
