package com.jiuyuan.util.oauth.sns.common.response;


public enum SnsResponseType {

    /** 成功 **/
    SUCCESS(0, "成功"),

    /** 重复发表 */
    ERROR_REPEAT(1, "重复发表"),

    /** 连接错误 */
    ERROR_CONNECTION(2, "连接错误"),

    /** 频率过快 */
    ERROR_FREQUENCY(3, "频率过快"),

    /** 内容不允许发布（违禁词、广告或者违禁图片等） */
    ERROR_CONTENT_FORBIDDEN(4, "内容不允许发布"),

    /** 认证错误 */
    ERROR_AUTH(5, "认证错误"),

    /** 非法IP */
    ERROR_INVALID_IP(6, "非法ip"),

    /** 禁止访问 */
    ERROR_FORBIDDEN(7, "禁止访问"),

    /** 内容长度越界 */
    ERROR_CONTENT_LENGTH(8, "内容长度越界"),

    /** 图片类型不支持 */
    ERROR_IMAGE_TYPE(9, "图片类型不支持"),

    /** 文件大小越界 */
    ERROR_FILE_SIZE(10, "文件大小越界"),

    /** 未知错误 */
    ERROR_UNKNOWN(-1, "未知错误");

    private SnsResponseType(int intValue, String message) {
        this.intValue = intValue;
        this.message = message;
    }

    private int intValue;

    private String message;

    public int getIntValue() {
        return intValue;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return this == SUCCESS;
    }
}
