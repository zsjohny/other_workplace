package com.e_commerce.miscroservice.supplier.entity.request;

import lombok.Data;

@Data
public class Query {

    private Long parentId;

    private Integer stauts;

    private Long orderNo;
}
