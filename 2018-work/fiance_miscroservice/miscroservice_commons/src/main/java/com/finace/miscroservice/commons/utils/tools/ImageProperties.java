package com.finace.miscroservice.commons.utils.tools;

import com.finace.miscroservice.commons.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ImageProperties extends TextUtil{

	private Log log = Log.getInstance(ImageProperties.class);
	public static ImageProperties imageProperties = null;
	
	private Properties properties;
	
	private static final String PROP_PATH = "/conf/image.properties";
	
	private ImageProperties(){
		init();
	}
	
	private void init(){
		if(properties == null){
			properties = new Properties();
		}
		try {
			properties.load(getInputStream());
		} catch (IOException e) {
			log.info("加载图片属性文件错误：" + e.getMessage());
		}
	}
	
	public static ImageProperties getInstance(){
		if(imageProperties == null){
			imageProperties = new ImageProperties();
		}
		return imageProperties;
	}
	
	private InputStream getInputStream(){
		return ImageProperties.class.getResourceAsStream(PROP_PATH);
	}
	
	public String get(String key){
		return properties.getProperty(key);
	}

}
