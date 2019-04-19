package com.yujj.ext.freemarker;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.yujj.util.uri.UriBuilder;

//import com.jiuyuan.web.interceptor.UriBuilder;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UriBuilderCreateMethod implements TemplateMethodModelEx {
    
    private static List<String> paramsToDiscard;

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Object arg = arguments.get(0);
        if (arg instanceof StringModel) {
            StringModel model = (StringModel) arg;
            Object obj = model.getWrappedObject();
            if (obj instanceof UriBuilder) {
                return discardCertainParams((UriBuilder) obj);
            }
        }

        String uri = ((SimpleScalar) arg).getAsString();
        if (StringUtils.isBlank(uri)) {
            throw new IllegalArgumentException("Missing parameter: uri");
        }

        String charset = UriBuilder.DEFAULT_CHARSET;
        boolean ignoreEmptyParam = false;
        if (arguments.size() > 1) {
            charset = ((SimpleScalar) arguments.get(1)).getAsString();
        }
        if (arguments.size() > 2) {
            ignoreEmptyParam = ((TemplateBooleanModel) arguments.get(2)).getAsBoolean();
        }

        UriBuilder builder = new UriBuilder(uri, charset, ignoreEmptyParam);
        return discardCertainParams(builder);
    }

    public UriBuilder discardCertainParams(UriBuilder builder) {
        if (paramsToDiscard != null) {
            for (String param : paramsToDiscard) {
                builder.remove(param);
            }
        }
        return builder;
    }

    public static void setParamsToDiscard(List<String> paramsToDiscard) {
        UriBuilderCreateMethod.paramsToDiscard = paramsToDiscard;
    }
}
