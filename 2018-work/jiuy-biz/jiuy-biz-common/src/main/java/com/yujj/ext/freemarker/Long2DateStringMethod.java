package com.yujj.ext.freemarker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;

public class Long2DateStringMethod implements TemplateMethodModelEx {

    private static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) {
        long time = ((SimpleNumber) arguments.get(0)).getAsNumber().longValue();
        if (time == 0) {
            return "";
        }

        String format = FORMAT_DEFAULT;
        if (arguments.size() > 1) {
            SimpleScalar formatArg = (SimpleScalar) arguments.get(1);
            format = StringUtils.defaultIfBlank(formatArg.getAsString(), FORMAT_DEFAULT);
        }

        return new SimpleDateFormat(format).format(new Date(time));
    }
}
