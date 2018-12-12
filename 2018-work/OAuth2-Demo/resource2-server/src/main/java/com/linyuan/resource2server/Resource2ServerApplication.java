package com.linyuan.resource2server;

import com.linyuan.oauth2config.config.annotation.EnableResJWTTokenStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableResJWTTokenStore //OAuth2 使用 JWT 解析令牌
public class Resource2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Resource2ServerApplication.class, args);
	}
}
