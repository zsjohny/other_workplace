package com.jiuyuan.util.sort;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public class SortFieldsComparator implements Comparator {

    private List<SortField> fields;

    public SortFieldsComparator(List<SortField> fields) {
        this.fields = fields;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object o1, Object o2) {
        try {
            for (SortField field : fields) {
                String fieldName = field.getField();
                int factor = field.isDescending() ? -1 : 1;

                Object target1 = o1;
                Object target2 = o2;
                if (StringUtils.isNotBlank(fieldName)) {
                    target1 = PropertyUtils.getProperty(o1, fieldName);
                    target2 = PropertyUtils.getProperty(o2, fieldName);
                }

                Comparable comparable1 = (Comparable) target1;
                Comparable comparable2 = (Comparable) target2;

                int result = comparable1.compareTo(comparable2);
                if (result != 0) {
                    return result * factor;
                }
            }
            return 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
