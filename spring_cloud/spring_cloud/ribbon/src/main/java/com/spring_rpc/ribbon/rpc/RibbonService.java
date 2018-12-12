package com.spring_rpc.ribbon.rpc;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.applet.AppletContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "error")
    public String hi(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return restTemplate.postForObject("http://CLIENT/hi?name=", name, String.class);
//        return restTemplate.postForObject("http://CLIENT/hi", params, String.class);
    }

    public String error(String name) {

        return "SORRY CLIENT HAVE CLOSED " + name;
    }

}
