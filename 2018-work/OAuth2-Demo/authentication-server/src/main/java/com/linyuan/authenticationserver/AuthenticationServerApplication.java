package com.linyuan.authenticationserver;

import com.linyuan.oauth2config.config.annotation.EnableAuthJWTTokenStore;
import com.linyuan.oauth2config.config.annotation.EnableDBClientDetailsService;
import com.linyuan.oauth2config.config.annotation.EnableDBTokenStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAuthJWTTokenStore    // 使用 JWT 存储令牌
//@EnableDBClientDetailsService //从 JDBC 加载客户端详情,需配置在启动类上，若在子类上会出现顺序问题，导致 Bean 创建失败
public class AuthenticationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServerApplication.class, args);
	}
}
