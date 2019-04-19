package com.jiuyuan.util.freemarker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;

public class FreeMarkerUtil {

    private static final Logger log = LoggerFactory.getLogger(FreeMarkerUtil.class);

    public static Object getStaticModel(Class<?> clazz) {
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
        try {
            return wrapper.getStaticModels().get(clazz.getName());
        } catch (TemplateModelException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
