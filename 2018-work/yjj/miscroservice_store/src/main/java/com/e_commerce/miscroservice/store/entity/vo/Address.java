package com.e_commerce.miscroservice.store.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 116542063818076543L;

    private int userId;//会员用
    
    private long storeId;//门店

    private int addrId;

    private String receiverName;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String addrDetail;

    private String mailCode;

    private String telephone;

    private String fixPhone;

    private String addrFull;

    //@JsonIgnore
    private int status;

    private int isDefault;

    //@JsonIgnore
    private long createTime;

   // @JsonIgnore
    private long updateTime;




}
