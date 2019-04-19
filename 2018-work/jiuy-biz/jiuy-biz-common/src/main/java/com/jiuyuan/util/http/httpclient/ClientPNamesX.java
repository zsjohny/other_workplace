package com.jiuyuan.util.http.httpclient;

public interface ClientPNamesX {

    /**
     * 重试次数
     */
    String RETRY_COUNT = "httpx.retry.count";

    /**
     * 在请求已发送后，是否允许重试（对于非idempotent的请求，请求发出后，服务端状态已发生了变化。重新发送请求可能导致问题，比如可能重复上传多个文件）
     */
    String REQUEST_SNET_RETRY_ENABLED = "httpx.request.sent.retry.enabled";

    /**
     * 在发生超时异常时，是否允许重试
     */
    String TIMEOUT_RETRY_ENABLED = "httpx.timeout.retry.enabled";
}
