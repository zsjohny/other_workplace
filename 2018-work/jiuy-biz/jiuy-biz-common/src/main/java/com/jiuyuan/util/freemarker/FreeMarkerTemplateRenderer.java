package com.jiuyuan.util.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.jiuyuan.util.adapter.ValueSupplier;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerTemplateRenderer {

    private Configuration config;

    public FreeMarkerTemplateRenderer(Resource[] templatePaths, String defaultEncoding, String urlEscapingCharset,
                                      int templateUpdateDelay) throws IOException {
        config = new Configuration();
        List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
        for (Resource path : templatePaths) {
            File file = path.getFile();
            templateLoaders.add(new FileTemplateLoader(file));
        }
        int loaderCount = templateLoaders.size();
        if (loaderCount == 0) {
            throw new IllegalStateException("template loader can not be 0");
        } else if (loaderCount == 1) {
            config.setTemplateLoader(templateLoaders.get(0));
        } else {
            config.setTemplateLoader(new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[loaderCount])));
        }
        config.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        config.setDefaultEncoding(defaultEncoding);
        config.setURLEscapingCharset(urlEscapingCharset);
        config.setNumberFormat("0.##########");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLocale(Locale.US);
        config.setTemplateUpdateDelay(templateUpdateDelay);
    }

    public String processTemplate(String templateName, Map<String, Object> rendererContext) throws IOException,
        TemplateException {
        return processTemplateInternal(templateName, config.getDefaultEncoding(), rendererContext);
    }

    public String processTemplate(String templateName, ValueSupplier<String, Object> supplier) throws IOException,
        TemplateException {
        return processTemplateInternal(templateName, config.getDefaultEncoding(), new SimpleHashEx(supplier));
    }

    private String processTemplateInternal(String templateName, String encoding, Object rootMap) throws IOException,
        TemplateException {
        Template template = config.getTemplate(templateName, encoding);
        StringWriter writer = new StringWriter();
        template.process(rootMap, writer);
        writer.flush();
        return writer.toString();
    }

    private static class SimpleHashEx extends SimpleHash {

        private static final long serialVersionUID = -8453291168733320181L;

        private ValueSupplier<String, Object> supplier;

        public SimpleHashEx(ValueSupplier<String, Object> supplier) {
            this.supplier = supplier;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            TemplateModel model = super.get(key);
            if (model != null) {
                return model;
            }

            Object obj = supplier.get(key);
            return wrap(obj);
        }

    }
}
