package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ProxyRefereeVo {

    private Long refereeUserId;

    private Integer recommonUserType;

    private Long recommonUserId;

    private Long refereParentId;

    private Integer status;

    private Integer delStatus;

    private Long updateUserId;

}
