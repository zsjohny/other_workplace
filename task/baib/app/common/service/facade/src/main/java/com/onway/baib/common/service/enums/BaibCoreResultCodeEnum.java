package com.onway.baib.common.service.enums;

/**
 * onway.com Inc.
 * Copyright (c) 2013-2013 All Rights Reserved.
 */

import com.onway.platform.common.enums.EnumBase;

/**
 * 系统错误码
 * 
 * @author li.hong
 * @version $Id: baibCoreResultCodeEnum.java, v 0.1 2016年9月2日 下午6:00:33 li.hong Exp $
 */
public enum BaibCoreResultCodeEnum implements EnumBase {

    CREATE_BEANCOPIER_ERROR("CREATE_BEANCOPIER_ERROR", "创建BeanCopier异常"),

    FILE_UPLOAD_ERROR("FILE_UPLOAD_ERROR", "文件上传异常"),

    FORCE_LOG_OUT("FORCE_LOG_OUT", "账号已在其他设备上登录，为了账号安全，请重新登录"),

    ERROR_CHECK_CODE("ERROR_CHECK_CODE", "验证码输入错误，请重新输入！"),

    NOT_EQUAL_LOGIN_PASSWD("NOT_EQUAL_LOGIN_PASSWD", "您两次输入的密码不相符，请重新输入"),

    ERROR_LOGIN_PWD("ERROR_LOGIN_PWD", "登录密码不正确，请重新输入"),

    INVALID_REQUEST("INVALID_REQUEST", "非法请求"),

    USER_NOT_EXSIT("USER_NOT_EXSIT", "系统不存在此用户"),

    USERID_NOT_BLANK("USERID_NOT_BLANK", "用户号不正确"),

    TOKEN_NOT_BLANK("TOKEN_NOT_BLANK", "token不正确"),

    /** 操作成功 */
    REGISTER_FAIL("REGISTER_FAIL", "注册失败"),


    /** 操作成功 */
    SUCCESS("SUCCESS", "操作成功"),

    /** 保存失败 */
    SAVE_FAILURE("SAVE_FAILURE", "保存失败"),

    /** 图片为空 */
    IMAGE_IS_NULL("IMAGE_IS_NULL", "图片为空"),
    
    /** 图片格式异常*/
    IMAGE_FORMMAT_ERROR("IMAGE_FORMMAT_ERROR", "图片格式异常"),

    /** 系统异常*/
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常，请联系管理员！"),

    /** 外部接口调用异常*/
    INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR", "外部接口调用异常，请联系管理员！"),

    /** 业务连接处理超时 */
    CONNECT_TIME_OUT("CONNECT_TIME_OUT", "系统超时，请稍后再试!"),

    /** 非法请求 */
    ILLEGAL_REQUEST("ILLEGAL_REQUEST", "非法请求"),

    /** 系统错误 */
    SYSTEM_FAILURE("SYSTEM_FAILURE", "系统错误"),

    /** 参数为空 */
    NULL_ARGUMENT("NULL_ARGUMENT", "参数为空"),

    /** 参数为空 */
    NULL_OBJECT("NULL_OBJECT", "对象为空"),

    /** 用户ID为空  */
    NULL_USER_ID("NULL_USER_ID", "用户ID为空"),

    ERROR_USER_DATA("ERROR_USER_DATA", "用户不存在"),
    
    /**数据不存在*/
    NO_RECORD("NO_RECORD", "没有相关数据"),

    /** 密码和用户名不匹配*/
    FAIL_QEURY_USER_CELL_AND_PASSWORD("FAIL_QEURY_USER_CELL_AND_PASSWORD", "密码和用户名不匹配"),

    /** 用户名不存在  */
    FAIL_QEURY_USER_BY_CELL("FAIL_USER_CELL", "用户名不存在"),

    /** */
    FAIL_QEURY_USER_BY_ID("FAIL_QEURY_USER_BY_ID", "用户ID不存在"),

    USER_CELL_EXIST("USER_CELL_EXIST", "手机号已被注册"),

    /** 手机号码为空  */
    NULL_USER_CELL("NULL_USER_CELL", "手机号码为空"),

    /** 证件号码为空  */
    NULL_CERT_NO("NULL_CERT_NO", "证件号码为空"),

    GOOD_UPLOAD_ERROR("GOOD_UPLOAD_ERROR", "商品上传异常"),

    ENTERPRISESERVICE_EDIT_ERROR("ENTERPRISESERVICE_EDIT_ERROR", "企业服务信息编辑异常"),

    REQUEST_TO_BEAN_ERROR("REQUEST_TO_BEAN_ERROR", "把request参数值封装到Bean失败"),

    POST_DATA_NOT_EXIT("POST_DATA_NOT_EXIT", "post记录信息不存在"),

    URL_DECODER_ERROR("URL_DECODER_ERROR", "转码异常"),

    /** 参数不正确 */
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不正确"),

    MNG_USER_UNLISTED("UNLISTED", "请登录"),

    MNG_BEYOND_THE_WAITING_TIME("BWAT", "超出系统等待时间，请重新登录"),

    MNG_USER_UNAUTHORIZED("UNAUTHORIZED", "没有权限"),

    /** 逻辑错误 */
    LOGIC_ERROR("LOGIC_ERROR", "逻辑错误"),

    /** 数据异常 */
    DATA_ERROR("DATA_ERROR", "数据异常"),

    /** 暂不支持的操作 */
    UN_SUPPORT_OPERATER("UN_SUPPORT_OPERATER", "暂不支持的操作"),

    /** 用户已认证*/
    ALREADY_CERTIFY("ALREADY_CERTIFY", "用户已认证"),

    /** 无效用户 不予认证*/
    NO_VALID_USER("NO_VALID_USER", "无效用户 不予认证"),

    /** 锁定用户 不予认证*/
    LOCKED_USER("LOCKED_USER", "锁定用户 不予认证"),

    /** 无效用户 不予认证*/
    ID_PHOTO_OPERATE_FAIL("ID_PHOTO_OPERATE_FAIL", "实名认证身份证照片 字节——》照片 转换失败"),

    /** 实名认证成功*/
    CERTIFY_SUCCESS("CERTIFY_SUCCESS", "实名认证成功"),

    /** 实名认证失败*/
    CERTIFY_FAIL("CERTIFY_FAIL", "实名认证失败"),

    /** 实名认证失败  身份证已经绑定用户*/
    CERTIFY_FAIL_ID_AREADY_EXSIT("CERTIFY_FAIL_ID_AREADY_EXSIT", "身份证已经绑定用户 "),

    /** 登录密码解密失败*/
    LOGIN_PASSWORD_DECRYPT_ERROR("PASSWORD_DECRYPT_ERROR", "登录密码解密失败"),

    FAIL_SMS_SEND_OVER_TEN_TIME_PER_HOUR("FAIL_SMS_SEND_OVER_TEN_TIME_PER_HOUR", "每个小时最多发送10次验证码短信"),

    FAIL_SMS_SEND_CHECK_CODE_ERROR("FAIL_SMS_SEND_CHECK_CODE_ERROR", "验证码错误"),

    FAIL_SMS_SEND_CHECK_CODE_OVERDUE("FAIL_SMS_SEND_CHECK_CODE_OVERDUE", "验证码过期"),

    FAIL_SMS_SEND_CHECK_CODE_CONCELL("FAIL_SMS_SEND_CHECK_CODE_CONCELL", "验证码发送已超过十次，一个小时之后重试"),

    /** 手机号码修改失败*/
    FAIL_CELL_MODIFY("FAIL_CELL_MODIFY", "手机号码修改失败"),

    /** 状态修改失败*/
    FAIL_STATE_MODIFY("FAIL_STATE_MODIFY", "状态修改失败"),

    /** 登录密码修改失败*/
    FAIL_LOGIN_PWD_MODIFY("FAIL_STATE_MODIFY", "登录密码修改失败"),

    /** 支付密码修改失败*/
    FAIL_PAY_PWD_MODIFY("FAIL_STATE_MODIFY", "支付密码修改失败"),

    FAIL_VERIFY_NAME_MODIFY("FAIL_VERIFY_NAME_MODIFY", "更新用户实名认证信息失败"),

    /***************************************用户收货地址异常码**************************************************/
    USER_ADDRESS_AMOUNT_EXCESS("USER_ADDRESS_AMOUNT_EXCESS", "用户总的收货地址数超过限制"),

    FAIL_USER_ADDRESS_CREATE("FAIL_USER_ADDRESS_CREATE", "用户收货地址创建失败"),
    
    /** 采购订单生成失败*/
    FAIL_ORDER_CREAT("FAIL_ORDER_CREAT","采购订单生成失败");

    /** 枚举编号 */
    private String code;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     * 
     * @param code         枚举编号
     * @param message      枚举详情
     */
    private BaibCoreResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static BaibCoreResultCodeEnum getResultCodeEnumByCode(String code) {
        for (BaibCoreResultCodeEnum param : values()) {
            if (param.getCode().equals(code)) {
                return param;
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>message</tt>.
     * 
     * @return property value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for property <tt>message</tt>.
     * 
     * @param message value to be assigned to property message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#message()
     */
    @Override
    public String message() {
        return message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#value()
     */
    @Override
    public Number value() {
        return null;
    }
}
