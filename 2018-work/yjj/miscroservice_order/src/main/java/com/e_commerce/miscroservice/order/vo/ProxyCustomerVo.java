package com.e_commerce.miscroservice.order.vo;

import lombok.Data;

@Data
public class ProxyCustomerVo {

    private String idCardNo;

    private String name;

    private String phone;

    private Integer type;

    private Long userId;

    private String province;

    private String city;

    private String county;

    private String proxyQrCode;

    private String customerQrCode;

    private String addressDetail;

    private Integer delStatus;


}
