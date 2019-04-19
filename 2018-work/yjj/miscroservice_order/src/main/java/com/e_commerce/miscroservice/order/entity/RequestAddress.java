package com.e_commerce.miscroservice.order.entity;

import lombok.Data;

@Data
public class RequestAddress {
    private Long memberId;
    private String linkmanName;
    private String phoneNumber;
    private String location;
    private String address;
    private Integer defaultStatus;
    private Long deliveryAddressId;
}
