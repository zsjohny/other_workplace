package com.finace.miscroservice.commons.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ConditionalOnExpression("${spring.security.enabled}")
public class ApiAccessSecurityInterceptor extends WebSecurityConfigurerAdapter {

    /**
     * http拦截请求设置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                //表明 /可以访问 其他的禁止访问 需要认证
//                .antMatchers("/").permitAll().
//                anyRequest().authenticated().and().formLogin();
        //表示 /需要认证 其他请求不需要权限 关闭feign的访问验证
//        http.authorizeRequests().antMatchers("/").authenticated().
//                anyRequest().permitAll().and().formLogin().and().csrf().disable();


        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();

    }


    /**
     * mvc请求拦截设置
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().anyRequest();
        super.configure(web);
    }
}