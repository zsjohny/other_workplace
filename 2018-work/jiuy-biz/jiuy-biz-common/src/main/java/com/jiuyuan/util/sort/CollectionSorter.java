package com.jiuyuan.util.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * Samples:
 * "+id -name", "+" or "-"
 * 
 * "+" means ascending order, and "-" means descending order. "id" and "name" are property names.
 * Properties used in a sort expression must implement the {@link Comparable} interface.
 * </pre>
 * 
 */
public class CollectionSorter {

    public static <T> List<T> sort(Collection<T> collection, String sortExpression) {
        return sort(collection, parse(sortExpression));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> sort(Collection<T> collection, List<SortField> fields) {
        List<T> list = new ArrayList<T>();
        list.addAll(collection);

        Collections.sort(list, new SortFieldsComparator(fields));
        return list;
    }

    public static List<SortField> parse(String expression) {
        List<SortField> fields = new ArrayList<SortField>();

        if (StringUtils.isBlank(expression)) {
            return fields;
        }

        String[] fieldExpressions = StringUtils.split(expression, ' ');
        for (String fieldExpression : fieldExpressions) {
            char orderChar = fieldExpression.charAt(0);
            String fieldName = fieldExpression.substring(1);
            fields.add(new SortField(fieldName, orderChar == '-'));
        }

        return fields;
    }
}
