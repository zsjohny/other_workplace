package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.DirContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

@Configuration
public class LdapConfig {


    public void check(LdapTemplate ldapTemplate) {


        try {
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectclass", "person")).
                    and(new EqualsFilter("cn", "admin"));

            ldapTemplate.authenticate("", filter.toString(), "985596");

        } catch (Exception e) {
            DirContextAdapter context = new DirContextAdapter();
            context.setAttributeValues("objectclass", new String[]{"person", "dcObject"});
            context.setAttributeValue("sn", "p");
            context.setAttributeValue("cn", "admin");
            context.setAttributeValue("dc", "domain");
            context.setAttributeValue("userpassword", "879227577");
            ldapTemplate.bind(context);
            System.out.println("____");
        }



    }

    @Bean
    public AbstractContextSource getContextSource() throws Exception {
        DirContextSource contextSource = new DirContextSource();
        contextSource.setUrl("ldap://192.168.89.137:1389");
        contextSource.setBase("dc=domain,dc=com");
        contextSource.setUserDn("cn=admin,dc=domain,dc=com");
        contextSource.setPassword("879227577");
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() throws Exception {
        LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
        ldapTemplate.setContextSource(getContextSource());
        check(ldapTemplate);

        return ldapTemplate;
    }
}
