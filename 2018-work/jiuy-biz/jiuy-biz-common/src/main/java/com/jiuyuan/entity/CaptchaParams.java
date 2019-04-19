package com.jiuyuan.entity;

/**
 * <pre>
 * 图形验证码相关请求参数
 * 
 * 验证码图片生成后，code存入memcached。为避免匿名用户的cache key冲突，增加了nonce和time参数。
 * 为避免用户构造nonce和time参数从而导致冲突，增加signature参数用于校验。
 * </pre>
 * 
 */
public class CaptchaParams {

    /** 图形验证码 */
    private String code;

    /** 随机数，由后端生成，客户端在提交图形验证码时需同时提交这个参数 */
    private String nonce;

    /** 时间，由后端生成，客户端在提交图形验证码时需同时提交这个参数 */
    private long time;

    /** 后端根据userId + nonce + time + ip计算出的签名，客户端在提交图形验证码时需同时提交这个参数 */
    private String signature;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
