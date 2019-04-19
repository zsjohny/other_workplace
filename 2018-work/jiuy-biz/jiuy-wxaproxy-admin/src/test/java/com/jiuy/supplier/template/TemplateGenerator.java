package com.jiuy.supplier.template;

import java.io.IOException;

import com.admin.core.template.config.ContextConfig;
import com.admin.core.template.engine.SimpleTemplateEngine;
import com.admin.core.template.engine.base.GunsTemplateEngine;

/**
 * 客户端
 *
 * @author fengshuonan
 * @date 2017-05-09 20:27
 */
public class TemplateGenerator {

    public static void main(String[] args) throws IOException {
        ContextConfig contextConfig = new ContextConfig();
        contextConfig.setBizChName("通知");
        contextConfig.setBizEnName("notice");

        GunsTemplateEngine gunsTemplateEngine = new SimpleTemplateEngine();
        gunsTemplateEngine.setContextConfig(contextConfig);
        gunsTemplateEngine.start();
    }

}
