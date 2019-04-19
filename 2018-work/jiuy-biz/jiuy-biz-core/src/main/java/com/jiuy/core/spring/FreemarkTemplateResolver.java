/**
 * 
 */
package com.jiuy.core.spring;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.jiuy.core.exception.ServerUnknownException;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker对应的视图引擎
 * @author zhuliming
 *
 */
public class FreemarkTemplateResolver implements BeanFactoryAware {

	private static final Logger logger = Logger.getLogger(SpringBeanFactory.class);
	
	private static BeanFactory beanFactory;  
	private static FreeMarkerViewResolver freeMarkerViewResolver;
	private static FreeMarkerConfigurer freeMarkerConfigurer;

    public void setBeanFactory(BeanFactory factory) throws BeansException {  
    	FreemarkTemplateResolver.beanFactory = factory;  
    }  
	
    /**************
     * 获取模板对应的freemarker视图解析器
     * @return
     */
	private static final FreeMarkerViewResolver getTemplateFreeMarkerViewResolver(){
		if(freeMarkerViewResolver == null){
			freeMarkerViewResolver = (FreeMarkerViewResolver)beanFactory.getBean("viewResolver");
		}
		return freeMarkerViewResolver;
	}

	/**************
	 * 获取模板对应的模板配置
	 * @return
	 */
	private static final FreeMarkerConfigurer getTemplateFreeMarkerConfigurer(){
		if(freeMarkerConfigurer == null){
			freeMarkerConfigurer = (FreeMarkerConfigurer)beanFactory.getBean("freemarkerConfig");
		}
		return freeMarkerConfigurer;
	}
	
	/********************
	 * 使用freemarker引擎解析页面， 并返回具体的内容
	 * @param pathUnderViewsDirectory:  /web-inf/views下面的文件路径， 例如/email/a代表/web-inf/views/email/a.ftl
	 * @return
	 */
	public static final String resolveFreemarkerView(final String pathUnderViewsDirectory, Map<String, Object> module){
		if(StringUtils.isBlank(pathUnderViewsDirectory)){
			logger.error("Fail to resolveView by freemarker engine: empty path!", (new Exception()));
			throw new ServerUnknownException("Fail to resolveView by freemarker engine: empty path!", (new Exception()));
		}
		if(module == null){
			module = new HashMap<String, Object>();
		}
		final FreeMarkerViewResolver freeMarkerViewResolver = FreemarkTemplateResolver.getTemplateFreeMarkerViewResolver();
		final FreeMarkerConfigurer freeMarkerConfigurer = FreemarkTemplateResolver.getTemplateFreeMarkerConfigurer();		 
		final Configuration configureation = freeMarkerConfigurer.getConfiguration();
		try{
			final FreeMarkerView view = (FreeMarkerView)freeMarkerViewResolver.resolveViewName(pathUnderViewsDirectory, configureation.getLocale());
	        final Template template = configureation.getTemplate(view.getUrl());
	        final StringWriter writer = new StringWriter();
	        template.process(module, writer);
	        writer.flush();
	        return writer.toString();
		}catch(Throwable ex){
			logger.error("Fail to resolveView by freemarker engine: error="+ex.getMessage(), ex);
			throw new ServerUnknownException("Fail to resolveView by freemarker engine: error="+ex.getMessage(), ex);
		}
	}
    
}
