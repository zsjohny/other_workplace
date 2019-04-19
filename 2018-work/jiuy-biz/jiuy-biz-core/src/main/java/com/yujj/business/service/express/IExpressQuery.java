package com.yujj.business.service.express;

/**
 * 
 * @author LWS
 *
 */
public interface IExpressQuery {
    public boolean support(String expressSupplier);

    public String queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier);
}
