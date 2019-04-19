package com.e_commerce.miscroservice.publicaccount.entity.vo;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 20:02
 * @Copyright 玖远网络
 */
@Data
public class MyProxyCustomerInfo{
    /**
     * 1.客户,2.代理
     */
    private Integer type;

    private PageInfo<ProxyRefereeUserInfo> userList;
    /**
     * 今日新增人数
     */
    private Long todayCreateCount;
    /**
     * 代理商自己的信息
     */
    private ProxyCustomer proxyCustomer;
}
