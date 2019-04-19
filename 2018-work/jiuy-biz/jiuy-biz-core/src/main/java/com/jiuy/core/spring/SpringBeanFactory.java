/**
 * 
 */
package com.jiuy.core.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.jiuy.core.exception.ServerUnknownException;

/**
 * 容器代理
 * @author zhuliming
 *
 */
public class SpringBeanFactory implements BeanFactoryAware {

	private static final Logger logger = Logger.getLogger(SpringBeanFactory.class);
	
	private static BeanFactory beanFactory;  
  
	private static final Map<String, Object> beans = new HashMap<String, Object>();
	
    public void setBeanFactory(BeanFactory factory) throws BeansException {  
    	SpringBeanFactory.beanFactory = factory;  
    }  
  
    /** 
     * 根据beanName名字取得bean 
     *  
     * @param beanName 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	protected synchronized static <T extends Object> T getBean(final String beanName) {  
    	T bean = (T)beans.get(beanName);
    	if(bean != null){
    		return bean;
    	}
    	if(beanFactory == null){
    		logger.error("Fail to getBean for null SpringFactory of beanName="+beanName, new Exception());
    		throw new ServerUnknownException("Fail to getBean for null SpringFactory of beanName="+beanName, new Exception());
    	}
    	bean = (T) beanFactory.getBean(beanName);  
        if(bean == null){
        	logger.error("Fail to getBean from SpringFactory of beanName="+beanName, new Exception());
    		throw new ServerUnknownException("Fail to getBean from SpringFactory of beanName="+beanName, new Exception());
        }
        beans.put(beanName, bean);
        return bean;  
    } 
    
//    /********************
//     * 获取对用户进行认证和权限管理service
//     * @return
//     */
//    public static final URLAuthorityService getURLAuthorityService(){
//    	return SpringBeanFactory.getBean("urlAuthorityService");
//    }
//    
//    /**********
//     * 获取spring factory
//     * @return
//     */
//    public static final FullTextSearchService getFullTextSearchService(){
//    	return SpringBeanFactory.getBean("fullTextSearchService");
//    }
}
