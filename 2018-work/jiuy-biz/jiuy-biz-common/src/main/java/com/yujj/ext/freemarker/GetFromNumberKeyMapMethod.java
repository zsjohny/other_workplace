package com.yujj.ext.freemarker;

import java.util.List;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 需要在ftl模板里使用?int、?long等built in把数值转换为和map key一致的数值类型。
 * 
 */
public class GetFromNumberKeyMapMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        SimpleHash hash = (SimpleHash) arguments.get(0);
        SimpleNumber number = (SimpleNumber) arguments.get(1);
        if (number == null) {
            return null;
        }
        return hash.toMap().get(number.getAsNumber());
    }
}
