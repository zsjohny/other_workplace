package com.store.service;

/**
 * 
 * @author LWS
 *
 */
public interface IExpressQuery {
    public boolean support(String expressSupplier);

    public String queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier);
}
