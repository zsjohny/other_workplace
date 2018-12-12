package com.finace.miscroservice.user.entity.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyFinanceBidResponse implements Serializable {
    private String name;
    private String gmtCreate;
    private String endProfit;
    private String status;

    private Integer timeLimitDay;

    private Double buyAmt;
    private Double apr;
    private Double interest;
}
