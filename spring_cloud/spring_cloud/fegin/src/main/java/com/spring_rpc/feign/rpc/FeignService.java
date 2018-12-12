package com.spring_rpc.feign.rpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "CLIENT",fallback = FeignFallBack.class)
public interface FeignService {

    //方式1
    //这里的只支持 post 并且 @RequestParam 这个值必须写
//    @RequestMapping(value = "hi", method = RequestMethod.POST)
    @PostMapping("hi")
    String hi(@RequestParam("name") String name);

    //方式2
//    @RequestLine("GET /get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    String get(@RequestParam("name") String name);


}
