package com.spring_hystrix_turbine_client1.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Component
public class CrossConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//     1   registry.addMapping("/**");
        //2 或者
        registry.addMapping("/hi/**").
                allowedOrigins("http://localhost:63342").allowedMethods("GET", "POST").
                allowCredentials(false).maxAge(36);

    }
}
