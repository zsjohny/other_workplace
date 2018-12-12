package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * 合约类型
 */
public enum InstTypeEnum {

    /**
     * Au(T+D)
     */
    AU("AU"),

    /**
     * Ag(T+D)
     */
    AG("AG"),

    /**
     * mAu(T+D)
     */
    MAU("MAU");

    String name;

    InstTypeEnum(String name) {
        this.name = name;
    }

    public String toName() {
        return this.name;
    }
}
