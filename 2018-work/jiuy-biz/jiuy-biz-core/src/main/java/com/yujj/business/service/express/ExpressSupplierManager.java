/**
 * 
 */
package com.yujj.business.service.express;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author LWS
 */

// @Component
public class ExpressSupplierManager implements ISupplierManager {

    private static class ExpressSupplierManagerHolder {
        static ExpressSupplierManager instance = new ExpressSupplierManager();
    }

    private ExpressSupplierManager() {

    }

    public static ExpressSupplierManager getInstance() {
        return ExpressSupplierManagerHolder.instance;
    }

    @Override
    public IExpressQuery getSupplier(final String expressSupplier) {
        /*String supplierAPI = null;*/
        if (null == (/*supplierAPI = */supplierList.get(expressSupplier))) {
            return null;
        } else {
            IExpressQuery sup = null;
            sup = (IExpressQuery) beanFactory.getBean(expressSupplier);
            _supplierAPIMap.put(expressSupplier, sup);
            return sup;
        }
    }

    @Resource(name = "supplierList")
    private Map<String, String> supplierList;

    private Map<String, IExpressQuery> _supplierAPIMap = new HashMap<String, IExpressQuery>();

    private BeanFactory beanFactory = null;

    public Map<String, String> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(Map<String, String> supplierList) {
        this.supplierList = supplierList;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
