package project.config.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.jiuy.supplier", "com.admin.core","project.config","com.jiuyuan.service"})
public class AppConfig {
}