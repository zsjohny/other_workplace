package com.tunnel.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by nessary on 16-10-10.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WbeSocketController implements WebSocketConfigurer {


    private String URL_PARAM = "/getQuotaByCode.do";


    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandlerAdaopt(), URL_PARAM).addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
    }
}
