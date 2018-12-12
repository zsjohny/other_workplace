package com.finace.miscroservice.authorize.config;

import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.DirContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import static com.finace.miscroservice.authorize.config.LdapConfig.DC_FILED;
import static com.finace.miscroservice.authorize.config.LdapConfig.OBJECT_FILED;

@Configuration
public class LdapTemplateConfig {

    @Autowired
    private LdapConfig ldapConfig;

    private Log log = Log.getInstance(LdapTemplateConfig.class);


    /**
     * 创建context的启动类
     *
     * @return
     */
    @Bean
    public AbstractContextSource getContextSource() {
        DirContextSource contextSource = new DirContextSource();
        contextSource.setUrls(ldapConfig.getUrls());
        contextSource.setBase(ldapConfig.getBase());
        contextSource.setUserDn("cn=" + ldapConfig.getUserName() + "," + ldapConfig.getBase());
        contextSource.setPassword(ldapConfig.getUserPass());
        contextSource.afterPropertiesSet();
        return contextSource;
    }


    /**
     * 创建操作ldap的template
     *
     * @return
     */
    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
        ldapTemplate.setContextSource(getContextSource());
        init(ldapTemplate);
        return ldapTemplate;
    }

    /**
     * 初始化任务节点
     *
     * @param ldapTemplate
     */
    private void init(LdapTemplate ldapTemplate) {


        try {
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectclass", OBJECT_FILED)).
                    and(new EqualsFilter("cn", ldapConfig.getUserName()));
            ldapTemplate.authenticate("", filter.toString(), ldapConfig.getUserPass());

        } catch (Exception e) {
            DirContextAdapter context = new DirContextAdapter();
            context.setAttributeValues("objectclass", ldapConfig.getObjectClass());
            context.setAttributeValue("cn", ldapConfig.getUserName());
            context.setAttributeValue("sn", ldapConfig.getUserName());
            context.setAttributeValue("uid", "0");
            context.setAttributeValue("dc", DC_FILED);
            context.setAttributeValue("userpassword", ldapConfig.getUserPass());
            ldapTemplate.bind(context);
            log.info("初始化ldap用户数据成功");
        }


    }
}
