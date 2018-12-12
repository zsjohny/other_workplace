package com.tunnel.util;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by nessary on 16-10-10.
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {

        return true;
    }


    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpHResponse, WebSocketHandler webSocketHandler, Exception e) {


    }
}
