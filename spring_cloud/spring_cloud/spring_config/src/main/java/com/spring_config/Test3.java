package com.finace.miscroservice.commons.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class Test3 {


    @Bean
    public static PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() throws IOException {
        Test2 pspc = new Test2();
        Resource[] resources = new ClassPathResource[]{new ClassPathResource("application.yml"), new ClassPathResource("jdbc.properties")};
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(Boolean.TRUE);
        pspc.setLocalOverride(Boolean.TRUE);
        pspc.setProperties(new Propertiess("server.port"));
        return pspc;

    }

    private static class Propertiess extends Properties {
        public Propertiess(String... agrs) throws IOException {


            this.setProperty("jasypt.encryptor.password", "d1d29ed7a1524828690db0702518fcf0");

        }
    }


}
