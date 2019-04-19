package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrderNewLog implements Serializable {

    private static final long serialVersionUID = -8701127740259286937L;
    
    private long Id;
    
    private long storeId;
    
    private long orderNo;
    
    private int oldStatus;
    
    private int newStatus;
    
    private long createTime;
    
    private long userId;


}
