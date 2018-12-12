package spring_hystrix_dashboard.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixDashboardController {


    @GetMapping("hi")
    @HystrixCommand(fallbackMethod = "error")
    public String hi(String name) {


        return "you name is " + name;
    }


    public String error(String name) {
        return "sorry forbidden " + name;
    }


}
