package com.jiuy.operator;


import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 运营平台
 * @version 1.1
 * @author Aison
 * @date 2018/5/19 20:40
 * @Copyright 玖远网络
 */
@SpringBootApplication
@ComponentScan("com.jiuy")
@MapperScan("com.jiuy.**.mapper")
@Log4j2
public class OperatorApplication {

   public static void main(String[] args) {
       SpringApplication.run(OperatorApplication.class, args);
   }



}
