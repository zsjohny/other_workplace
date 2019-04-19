package com.jiuy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 处理日志的线程池
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/6 10:01
 * @Copyright 玖远网络
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 处理日志的线程池
     * @author Aison
     * @date 2018/6/6 9:29
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Logs-Executor");
        executor.setMaxPoolSize(10);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler((r, executor1) -> {
            // .....
        });
        // 使用预定义的异常处理类
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
