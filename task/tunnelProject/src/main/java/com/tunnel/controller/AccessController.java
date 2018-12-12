package com.tunnel.controller;

import com.tunnel.util.WebSocketHandlerAdaopt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by nessary on 16-10-10.
 */
@Controller
public class AccessController {
    @ResponseBody
    @RequestMapping("/test")
    public String hello() {

        WebSocketHandlerAdaopt.sendMessageToAll("hello....");
        return "hello";
    }

    public static void main(String[] args) {
    }
}
