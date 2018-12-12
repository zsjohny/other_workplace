package com.goldplusgold.td.sltp.core.auth.exception;

/**
 * jwt token在其生命周期中出现的异常
 */
public class JwtTokenException extends AbstractRestException {

    public JwtTokenException(String errorCode, String errorMsg, String viewInfo) {
        this(errorCode, errorMsg, null, viewInfo);
    }

    public JwtTokenException(String errorCode, String errorMsg, Exception e, String viewInfo) {
        super(errorCode, errorMsg, e, viewInfo);
    }

    public enum Info {

        CREATE_JWT_PARAM_MISS("JTE01", "创建 JWT TOKEN 时，缺少创建 JWT TOKEN 的参数"),
        VERIFY_JWT_ERROR("JTE02", "JWT TOKEN 验证异常"),
        VERIFY_JWT_PARAM_MISS("JTE03", "验证 JWT TOKEN 时，解析出来的参数出现缺失"),
        REFRESH_JWT_PARAM_MISS("JTE04", "刷新 JWT TOKEN 时，发现缺少了创建 JWT TOKEN 的参数"),
        REQUEST_GET_JWT_ERROR("JTE05", "没有从访问请求中获取到 JWT TOKEN 信息"),
        ACCESS_ERROR("JTE06", "从访问请求中，获取 JWT TOKEN 信息，并验证权限时出错"),
        JZJ_TOKEN_NOT_FOUND("JTE07", "请先登录金专家."),
        JZJ_TOKEN_EXPIRATION("JTE08", "金专家 TOKEN已经过期"),
        TD_ACCOUNT_NOT_FOUND("JTE09", "TD账户未与金专家账户绑定.");

        /**
         * 异常编码
         */
        private String code;
        /**
         * 异常信息
         */
        private String info;


        Info(String code, String info) {

            this.code = code;
            this.info = info;
        }

        public String toCode() {
            return this.code;
        }

        public String toInfo() {
            return this.info;
        }
    }
}
