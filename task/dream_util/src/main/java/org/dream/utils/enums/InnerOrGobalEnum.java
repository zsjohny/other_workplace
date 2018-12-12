package org.dream.utils.enums;

/**
 * 国内市场还是国外市场
 * Created by nessary on 16-8-31.
 */
public enum InnerOrGobalEnum {
    INNER("国内", 1),
    GOBALE("国外", 0);
    private String key;
    private Integer value;

    InnerOrGobalEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
