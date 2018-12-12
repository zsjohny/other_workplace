package spring_hystrix_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrix
@EnableEurekaClient
@EnableHystrixDashboard
@SpringBootApplication
public class HystrixDashboardMain {


    //开启的步骤 是使用 注解后  在界面 访问 localhost:port/hystrix 即可访问界面
    //访问界面后输入  localhost:port/hystrix.stream 就可以点击看到所需要的数据
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardMain.class, args);
    }
}
