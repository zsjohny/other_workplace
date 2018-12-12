package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.annotation.PostConstruct;
import javax.naming.ldap.LdapName;
import java.util.UUID;

@SpringBootApplication
public class Application {


    @Autowired
    private LdapTemplate ldapTemplate;

    @PostConstruct
    public void run() {


        String userName = UUID.randomUUID().toString();

        LdapName build = LdapNameBuilder.newInstance()
                .add("cn", userName)
                .build();

        DirContextAdapter context = new DirContextAdapter(build);

        context.setAttributeValues("objectclass", new String[]{"person", "dcObject"});
        context.setAttributeValue("sn", "p");
        context.setAttributeValue("dc", "domain");
        context.setAttributeValue("userpassword", "985596");
//        ldapTemplate.rebind(context);

        ldapTemplate.bind(context);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).
                and(new EqualsFilter("cn", userName));
        boolean cn = ldapTemplate.authenticate("", filter.toString(), "985596");

        System.out.println(cn);



        System.out.println("init success");
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
