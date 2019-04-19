/**
 * 
 */
package com.yujj.business.service.express;

import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author Never
 *
 */
public interface ISupplierManager extends BeanFactoryAware{
    public IExpressQuery getSupplier(final String expressSupplier);
}
