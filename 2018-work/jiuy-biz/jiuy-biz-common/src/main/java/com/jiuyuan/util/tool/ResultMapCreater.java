package com.jiuyuan.util.tool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

public class ResultMapCreater {

    public static List<String> from(Class<?> clazz) {
        List<String> lines = new ArrayList<String>();
        lines.add("<resultMap id=\"${ResultMapName}\" type=\"" + clazz.getSimpleName() + "\">");

        List<Field> fields = new ArrayList<Field>(getAllCustomFields(clazz));
        Collections.sort(fields, new Comparator<Field>() {

            @Override
            public int compare(Field a, Field b) {
                return a.getName().compareTo(b.getName());
            }
        });

        List<String> ignoredFields = new ArrayList<String>();
        ignoredFields.add("serialVersionUID");
        ignoredFields.add("log");
        ignoredFields.add("logger");

        for (Field field : fields) {
            if (!ignoredFields.contains(field.getName())) {
                lines.add("\t<result property=\"" + field.getName() + "\" column=\"" +
                    StringUtils.capitalize(field.getName()) + "\" />");
            }
        }

        lines.add("</resultMap>");
        return lines;
    }

    public static Set<Field> getAllCustomFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<Field>();
        Class<?> clz = clazz;
        while (clz != null && clz != Object.class) {
            for (Field field : clz.getDeclaredFields()) {
                fields.add(field);
            }
            clz = clz.getSuperclass();
        }
        return fields;
    }

}
