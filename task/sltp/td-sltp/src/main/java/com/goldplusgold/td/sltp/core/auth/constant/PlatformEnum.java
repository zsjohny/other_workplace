package com.goldplusgold.td.sltp.core.auth.constant;

/**
 * 客户端平台类型
 */
public enum PlatformEnum {

    IOS("ios", 0),
    ANDROID("android", 1);


    private String name;
    private Integer value;

    PlatformEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static PlatformEnum getPlatform(String platform) {

        PlatformEnum platfromEnum = null;
        if (ANDROID.toName().equals(platform)) {
            platfromEnum = ANDROID;
        } else if (IOS.toName().equals(platform)) {
            platfromEnum = IOS;
        }

        return platfromEnum;
    }


    public String toName() {
        return this.name;
    }

    public Integer toValue() {
        return this.value;
    }
}
