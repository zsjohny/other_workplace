package com.songxm.credit;

import com.songxm.credit.comon.credit.diversion.constant.AppConsts;
import moxie.cloud.service.server.MicroService;
import moxie.cloud.service.server.ServiceInfo;
import moxie.cloud.service.server.config.BaseConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sxm
 */
@EnableAsync
@SpringBootApplication
public class CreditDiversionMicroService extends BaseConfig {
    public static void main(String[] args) {
        System.setProperty("service.tag", "1");
        System.setProperty("server.port", "8087");
        System.setProperty("BASE_LOG_PATH", "./");

        MicroService service = new MicroService();
        service.start(CreditDiversionMicroService.class, args);
    }


    public ServiceInfo getServiceInfo() {
        return new ServiceInfo("信贷导流", "Risk Service API", AppConsts.SERVICE_NAME, "v1");
    }

    @Bean
    public Executor storeDataAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Async-Thread");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
