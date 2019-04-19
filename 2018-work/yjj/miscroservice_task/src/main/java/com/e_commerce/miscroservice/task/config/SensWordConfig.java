package com.e_commerce.miscroservice.task.config;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.SensitiveWordUtil;
import com.e_commerce.miscroservice.task.service.SensWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/29 10:42
 */
@Configuration
public class SensWordConfig {

    private Log logger = Log.getInstance(SensWordConfig.class);

    @Autowired
    private SensWordService sensWordService;

    @Bean
    public SensitiveWordUtil initSensWord() {
        List<String> sensList =  sensWordService.findAll();
        SensitiveWordUtil.init(new HashSet<>(sensList));
        logger.info("初始化敏感词库");
        return null;
    }

}
