/**
 * 
 */
package com.jiuy.core.spring;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.jiuy.core.exception.ServerUnknownException;
import com.jiuyuan.util.CommonConstant.Encoding;
import com.jiuyuan.util.IOUtil;

/**
 * 由于spring自带的SqlSessionFactoryBean不支持和成多个mybatis的ConfigLocation文件，故本类提供了一个简单的实现方案。
 * 当遇到这种情况时， 一般会在依赖工程有ConfigLocation文件， 而自身也有ConfigLocation文件时， 需要使用此类。
 * 使用此类方式有两种：
 * 1. 按照SqlSessionFactoryBean一模一样的方式使用；
 * 2. 不配置ConfigLocation， 配置configTypeAliasLocations， 类似portal工程， 
 * 	     每个ConfigLocation配置的是多行的<typeAlias alias="SasPropertySetting" type="com.sas.core.meta.SasPropertySetting" />，
 *   也就是说ConfigLocation内容不是完整的mybatis-config文件内容；
 *  如有不清楚，联系朱立明
 * @author zhuliming
 *
 */
public class SasSqlSessionFactoryBean extends SqlSessionFactoryBean {

	private static final Logger logger = Logger.getLogger(SasSqlSessionFactoryBean.class);
	
	private Resource[] configTypeAliasLocations;

	public Resource[] getConfigTypeAliasLocations() {
		return configTypeAliasLocations;
	}

	public void setConfigTypeAliasLocations(Resource[] configTypeAliasLocations) {
		this.configTypeAliasLocations = configTypeAliasLocations;
		if(ArrayUtils.isNotEmpty(configTypeAliasLocations))
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(IOUtil.lineSeparator())
				.append("<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"").append(IOUtil.lineSeparator())
				.append("\"http://mybatis.org/dtd/mybatis-3-config.dtd\">").append(IOUtil.lineSeparator())
				.append("<configuration>").append(IOUtil.lineSeparator())
				.append("<settings>").append(IOUtil.lineSeparator())
				.append("<setting name=\"logImpl\" value=\"LOG4J\"/>").append(IOUtil.lineSeparator())
				.append("</settings>").append(IOUtil.lineSeparator())
				.append("<typeAliases>").append(IOUtil.lineSeparator());				
			for(final Resource rs :configTypeAliasLocations)
			{
				//每个配置的是这样内容的文件:<typeAlias alias="ProductUpgradeGuide"            type="com.lvguanjia.meta.ProductUpgradeGuide" />
				final StringBuilder rsContent = this.readOneResource(rs, Encoding.UTF8);
				if(rsContent == null || rsContent.indexOf("<?xml version=") >= 0){
					throw new ServerUnknownException("Error configTypeAliasLocations of mybatis: content=" + rsContent);
				}				
				sb.append(rsContent);
			}
			sb.append("</typeAliases>").append(IOUtil.lineSeparator()).append("</configuration>");
			try {
				this.setConfigLocation(new ByteArrayResource(sb.toString().getBytes(Encoding.UTF8.type)));
			} catch (Exception e) {
				logger.error("Fail to setConfigLocation of mybatis: ex="+e.getMessage(), e);
				throw new ServerUnknownException("Fail to setConfigLocation of mybatis: ex="+e.getMessage(), e);
			}
		}
	}
	
	/***************
	 * 读取文件内容
	 * @param rs
	 * @param encoding
	 * @return
	 */
	private StringBuilder readOneResource(final Resource rs, final Encoding encoding)
	{
		int typeAliasLine = 0;
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(rs.getInputStream()));
			final StringBuilder result = new StringBuilder("");
			String line = null;
			while((line = reader.readLine()) != null){
				line = line.trim();
				if(line.length() > 0){
					result.append(line.trim());
					result.append(IOUtil.lineSeparator());
					typeAliasLine ++;
				}
			}
			return result;
		}catch(Exception ex){
			logger.error("Fail to read resource: file="+rs.getFilename(), ex);
			return null;
		}finally{
			logger.debug("mybatis config file: " + rs.getFilename()
					+ ", typeAlias lines: " + typeAliasLine);
			IOUtil.closeReaderWithoutException(reader);
		}
	}
	
	
	
}