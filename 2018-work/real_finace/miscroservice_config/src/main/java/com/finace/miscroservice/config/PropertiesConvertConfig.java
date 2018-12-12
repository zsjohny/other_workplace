package com.finace.miscroservice.config;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.PropertyResourceBundle;

/**
 * properties的转换配置(主要解决中文乱码)
 */
public class PropertiesConvertConfig implements PropertySourceLoader {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"properties"};
    }

    @Override
    public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
        if (profile == null) {
            Properties properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream(), "UTF-8");
            PropertyResourceBundle bundle = new PropertyResourceBundle(inputStreamReader);
            Enumeration<String> keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                properties.setProperty(key, bundle.getString(key));
            }
            if (!properties.isEmpty()) {
                return new PropertiesPropertySource(name, properties);
            }
            inputStreamReader.close();

        }
        return null;
    }
}
