package com.finace.miscroservice.borrow.entity.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 汇付 本 息 类
 */
@Data
@ToString
public class HMoneyResponse implements Serializable {
    private Double hAccount; //汇付本金
    private Double hInterest; //汇付利息
}
