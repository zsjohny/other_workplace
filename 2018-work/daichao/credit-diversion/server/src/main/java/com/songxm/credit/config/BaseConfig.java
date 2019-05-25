//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.songxm.credit.config;

import com.moxie.cloud.commons.service.RedisService;
import com.songxm.credit.comon.credit.diversion.constant.AppConsts;
import com.xinbang.cif.gateway.client.CifGateWayClient;
import com.xinbang.commonservice.client.CommonServiceClient;
import com.xinbang.commonservice.client.CommonServiceClientFactory;
import moxie.cloud.service.server.ServiceInfo;
import moxie.cloud.service.server.filter.*;
import moxie.cloud.service.server.filter.ratelimit.RateLimitFilter;
import moxie.cloud.service.server.service.BaseEnvironment;
import moxie.cloud.service.server.service.BaseErrorContentService;
import moxie.cloud.service.server.service.BaseProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.statsd.StatsdMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableScheduling
@Configuration
@EnableSwagger2
public abstract class BaseConfig extends WebMvcConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(BaseConfig.class);
    @Autowired(
        required = false
    )
    private BaseEnvironment env;
    @Autowired(
        required = false
    )
    private RedisService redis;
    @Autowired(
        required = false
    )
    private BaseErrorContentService errorContentService;
    @Autowired(
        required = false
    )
    private XbAuthFilter xbAuthFilter;
    @Autowired(
        required = false
    )
    private BaseMetricsFilter2 metrisFilter;
    @Autowired(
        required = false
    )
    private RequestRecordFilter requestRecordFilter;

    public static final String TOKEN_SECURECT = "123456";
    public BaseConfig() {
    }

    @PostConstruct
    private void init() {
        log.info("服务[{}]初始化成功", BaseProps.serviceName());
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
    }

    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON).favorPathExtension(false);
    }

    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(Boolean.valueOf(false)).setUseTrailingSlashMatch(Boolean.valueOf(true));
    }

    @Bean
    public CifGateWayClient cifClient() {
        return CifGateWayClient.instance(BaseProps.clientOptions(), BaseProps.cifTag(), BaseProps.serviceToken());
    }

    public void addInterceptors(InterceptorRegistry registry) {
        if(this.requestRecordFilter != null) {
            registry.addInterceptor(this.requestRecordFilter);
        }

        if(this.metrisFilter != null) {
            registry.addInterceptor(this.metrisFilter);
        }

        if(this.xbAuthFilter != null) {
            registry.addInterceptor(this.xbAuthFilter);
        }

    }

    @Bean
    @ConditionalOnProperty(
        name = {"ratelimit.enabled"},
        havingValue = "true"
    )
    public FilterRegistrationBean rateLimitFilter() {
        RateLimitFilter rateLimitFilter = new RateLimitFilter(this.getServiceInfo().getServiceName(), this.redis, this.env, this.errorContentService);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(rateLimitFilter);
        registrationBean.setOrder(-2147483645);
        return registrationBean;
    }

    public  ServiceInfo getServiceInfo(){
        return new ServiceInfo("信贷导流", "Risk Service API", AppConsts.SERVICE_NAME, "v1");
    }

    @Bean
    @ConditionalOnProperty(
        name = {"error.mapping.enabled", "common-service.tag", "org.code"}
    )
    public FilterRegistrationBean errorMappingFilterRegistrationBean() {
        String tag = this.env.getProperty("common-service.tag");
        CommonServiceClient commonServiceClient = CommonServiceClientFactory.instance(BaseProps.clientOptions(), tag, BaseProps.serviceToken());
        ErrorMappingFilter errorMappingFilter = new ErrorMappingFilter(commonServiceClient, this.env.getProperty("org.code"), this.getServiceInfo().getServiceName());
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(errorMappingFilter);
        registrationBean.setOrder(-2147483646);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean cacheControlFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CacheControlFilter cacheControlFilter = new CacheControlFilter();
        registrationBean.setFilter(cacheControlFilter);
        registrationBean.setOrder(-2147483648);
        return registrationBean;
    }

    @Bean
    @ExportMetricWriter
    @ConditionalOnProperty({"metrics.statsd.host"})
    MetricWriter metricWriter() {
        return new StatsdMetricWriter(BaseProps.serviceName(), BaseProps.statsdHost(), BaseProps.statsdPort().intValue());
    }



    @PreDestroy
    private void destroy() {
        log.info("服务[{}]关闭", BaseProps.serviceName());
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.songxm.credit.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("S信贷导流 APIs")
                .description("信贷导流")
                .contact("xuanming song")
                .version("V1.0")
                .build();
    }
}
