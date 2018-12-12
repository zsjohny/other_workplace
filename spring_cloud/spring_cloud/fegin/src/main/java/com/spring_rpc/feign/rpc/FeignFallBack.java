package com.spring_rpc.feign.rpc;

import org.springframework.stereotype.Component;

@Component
public class FeignFallBack implements FeignService {
    @Override
    public String hi(String name) {
        return "fallback ERROR " + name;
    }

    @Override
    public String get(String name) {
        return null;
    }
}
