package org.dream.utils;

import org.dream.utils.prop.SpringProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Boyce 2016年6月23日 上午11:48:01
 */
public class SpringTest implements BeanFactoryPostProcessor, InitializingBean {
	SpringTest2 test2;
	SpringProperties basePropertyPlaceHolder;

	public void setBasePropertyPlaceHolder(SpringProperties basePropertyPlaceHolder) {
		this.basePropertyPlaceHolder = basePropertyPlaceHolder;
	}

	/**
	 * @param test2
	 *            the test2 to set
	 */
	public void setTest2(SpringTest2 test2) {
		this.test2 = test2;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(basePropertyPlaceHolder.getProperty("cfg1"));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/test.xml");
		while (true) {
		}
	}
}
