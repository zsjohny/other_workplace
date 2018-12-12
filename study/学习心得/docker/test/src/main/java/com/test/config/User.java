package com.test.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@RestController
public class User {

    // 自定义规则注解
    private String phone;


    @GetMapping
    public String init(HttpServletResponse response) {
        response.addHeader("Refresh", "10;URL=http://localhost:8080/servlet/example.htm");
        return "ok";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws FileNotFoundException {

        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                '}';
    }

    public static void main(String[] args) {
    }
}
