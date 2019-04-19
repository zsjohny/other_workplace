package com.jiuy.operator.Config;

import com.jiuy.model.file.OssConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * 一些配置信息
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 10:47
 * @Copyright 玖远网络
 */
@Configuration
public class Config {


    @Value("${oss.ossUrl}")
    private String ossUrl;

    @Value("${oss.ossBucket}")
    private String ossBucket;

    @Value("${oss.ossAccessKeyId}")
    private String ossAccessKeyId;

    @Value("${oss.ossAccessKeySecret}")
    private String ossAccessKeySecret;

    /**
     * oss的配置bean
     * @author Aison
     * @date 2018/6/11 10:51
     */
    @Bean("ossConfig")
    public OssConfigVo ossConfig() {

        OssConfigVo ossConfig = new OssConfigVo();
        ossConfig.setOssAccessKeyId(ossAccessKeyId);
        ossConfig.setOssAccessKeySecret(ossAccessKeySecret);
        ossConfig.setOssBucket(ossBucket);
        ossConfig.setOssUrl(ossUrl);
        return ossConfig;
    }




}
