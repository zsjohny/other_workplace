package org.dream.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.PriorityOrdered;

/**
 * @author Boyce
 * 2016年6月23日 上午11:48:01 
 */
public class SpringTest2  implements BeanFactoryPostProcessor,InitializingBean,BeanNameAware, BeanFactoryAware{

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(1);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println(2);
	}
	public void test(){
		System.out.println("tset");
	}
public static void main(String[] args) {
	ApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:/test.xml");
	while(true){}
}

@Override
public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
	// TODO Auto-generated method stub
	
}

@Override
public void setBeanName(String name) {
	// TODO Auto-generated method stub
	
}

}
