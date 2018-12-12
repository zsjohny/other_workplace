package org.dream.utils.enums;

/**
 * Created by nessary on 16-7-19.
 */
public enum MultipMapLoadType {
    multimapByMoney("multimapByMoney", 1),
    multimapByPoint("multimapByPoint", 0);


    private String key;
    private Integer value;

    MultipMapLoadType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


}
