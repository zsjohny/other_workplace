package com.tunnel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nessary on 16-10-10.
 */

public class WebSocketHandlerAdaopt implements WebSocketHandler {

    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandlerAdaopt.class);

    /**
     * 所用用户的集合
     */
    public static ConcurrentHashMap<String, WebSocketSession> clientsMap = new ConcurrentHashMap<String, WebSocketSession>();

    /**
     * 开始链接
     *
     * @param webSocketSession
     * @throws Exception
     */
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        logger.info("客户端{},开始连接行情服务端", webSocketSession.getRemoteAddress());
    }

    /**
     * 消息处理
     *
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        try {
            String contractCode = (String) webSocketMessage.getPayload();

            if (StringUtils.isEmpty(contractCode)) {
                logger.info("客户端{},订阅参数传递为空", webSocketSession.getRemoteAddress());
                return;
            }
            logger.info("客户端{},开始订阅行情{}", webSocketSession.getRemoteAddress(), contractCode);
            WebSocketSession session = clientsMap.get(contractCode);

            if (session == null) {
                clientsMap.put(contractCode, webSocketSession);
            }

        } catch (Exception e) {
            logger.info("客户端{},订阅参数传递异常", webSocketSession.getRemoteAddress(), e);
        }


    }

    /**
     * 消息异常处理
     *
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {


        //断开链接
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        logger.info("客户端{},连接行情服务端出现异常", webSocketSession.getRemoteAddress());
    }


    /**
     * 断开链接
     *
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        logger.info("客户端{},关闭行情服务端", webSocketSession.getRemoteAddress());
        try {  //去除map的客户端用户
            for (WebSocketSession session : Arrays.asList(new WebSocketSession[clientsMap.size()])) {
                if (session.equals(webSocketSession)) {
                    clientsMap.remove(session);
                }
            }
        } catch (Exception e) {
            logger.info("客户端{},关闭行情服务端出错", webSocketSession.getRemoteAddress(), e);
        }
    }

    public boolean supportsPartialMessages() {
        return false;
    }


    public static void sendMessageToAll(String msg) {

        for (WebSocketSession session : clientsMap.values()) {
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (Exception e) {
                logger.warn("发送客户端{}行情信息异常", session.getRemoteAddress(), e);
            }
        }

    }

    public static void main(String[] args) {
        WebSocketSession socketSession = new WebSocketSession() {
            public String getId() {
                return null;
            }

            public URI getUri() {
                return null;
            }

            public HttpHeaders getHandshakeHeaders() {
                return null;
            }

            public Map<String, Object> getAttributes() {
                return null;
            }

            public Principal getPrincipal() {
                return null;
            }

            public InetSocketAddress getLocalAddress() {
                return null;
            }

            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            public String getAcceptedProtocol() {
                return null;
            }

            public void setTextMessageSizeLimit(int i) {

            }

            public int getTextMessageSizeLimit() {
                return 0;
            }

            public void setBinaryMessageSizeLimit(int i) {

            }

            public int getBinaryMessageSizeLimit() {
                return 0;
            }

            public List<WebSocketExtension> getExtensions() {
                return null;
            }

            public void sendMessage(WebSocketMessage<?> webSocketMessage) throws IOException {

            }

            public boolean isOpen() {
                return false;
            }

            public void close() throws IOException {

            }

            public void close(CloseStatus closeStatus) throws IOException {

            }
        };
        clientsMap.put("sss", socketSession);

        System.out.println(Arrays.asList(new WebSocketSession[clientsMap.size()]));
    }

}
