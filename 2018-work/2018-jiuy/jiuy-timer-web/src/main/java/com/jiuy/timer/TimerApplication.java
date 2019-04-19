package com.jiuy.timer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * 定时任务入口类
 *
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/5/28 13:05
 */
@SpringBootApplication
@ComponentScan("com.jiuy")
@MapperScan("com.jiuy.**.mapper")
public class TimerApplication  {

	public static void main(String[] args) {
		SpringApplication.run(TimerApplication.class, args);
	}

}
