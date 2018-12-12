package com.finace.miscroservice.commons.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FilePermission;
import java.security.Permission;

/**
 * 反序列化的配置中心
 */
@Configuration
@ConditionalOnProperty("${deSerialized.enabled}")
public class DeSerializedConfig {


    /**
     * 创建权限管理的securityManager
     *
     * @return
     */
    @Bean
    public SecurityManager createSecurityManager() {
        SecurityManager securityManager = System.getSecurityManager();


        if (securityManager != null) {
            return securityManager;
        }
        securityManager = new SecurityManager() {

            private void check(Permission perm) {

                if (perm instanceof FilePermission) {
                    String actions = perm.getActions();

                    if (actions != null && actions.equals("execute")) {

                        throw new SecurityException("defined use runtime!!");
                    }
                } else if (perm instanceof RuntimePermission) {
                    String name = perm.getName();
                    if (name != null && name.equals("setSecurityManager")) {

                        throw new SecurityException("set new SecurityManager defined!!");
                    }
                }
            }

            @Override
            public void checkPermission(Permission perm) {
                check(perm);

            }

            @Override
            public void checkPermission(Permission perm, Object context) {
                check(perm);
            }
        };

        System.setSecurityManager(securityManager);

        return securityManager;
    }

}
