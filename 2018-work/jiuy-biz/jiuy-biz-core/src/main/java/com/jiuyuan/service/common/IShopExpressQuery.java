package com.jiuyuan.service.common;

/**
 * 
 * @author LWS
 *
 */
public interface IShopExpressQuery {
    public boolean support(String expressSupplier);

    public String queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier);
}
