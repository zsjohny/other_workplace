package com.finace.miscroservice.authorize.dao.impl;

import com.finace.miscroservice.authorize.config.LdapConfig;
import com.finace.miscroservice.authorize.dao.LdapCurdDao;
import com.finace.miscroservice.commons.entity.UserAuth;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

import javax.naming.ldap.LdapName;
import java.util.List;

import static com.finace.miscroservice.authorize.config.LdapConfig.DC_FILED;
import static com.finace.miscroservice.authorize.config.LdapConfig.OBJECT_FILED;

/**
 * 用户的crud的实现类
 */
@Component
public class LdapCurdDaoImpl implements LdapCurdDao {
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private LdapConfig ldapConfig;

    private Log log = Log.getInstance(LdapCurdDaoImpl.class);

    @Override
    public Boolean insertUsers(List<UserAuth> users) {

        Boolean insertFlag = Boolean.FALSE;

        for (UserAuth user : users) {
            try {
                ldapTemplate.bind(getDirContext(user));
                insertFlag = Boolean.TRUE;
                log.info("插入用户={}成功", user.getName());

            } catch (NameAlreadyBoundException e) {
                insertFlag = Boolean.FALSE;
                log.warn("用户={}已经存在", user.getName());
            }


        }
        return insertFlag;


    }

    @Override
    public void updateUser(UserAuth user) {

        ldapTemplate.rebind(getDirContext(user));
        log.info("更新用户={}成功", user.getName());
    }


    /**
     * 根据user获取跟新的上下文
     *
     * @param user 用户的实体
     * @return
     */
    private DirContextAdapter getDirContext(UserAuth user) {
        LdapName build = LdapNameBuilder.newInstance()
                .add("cn", user.getName())
                .build();

        DirContextAdapter context = new DirContextAdapter(build);
        context.setAttributeValues("objectclass", ldapConfig.getObjectClass());
        context.setAttributeValue("sn", user.getNickName() == null ? user.getName() : user.getNickName());
        context.setAttributeValue("dc", DC_FILED);
        context.setAttributeValue("uid", user.getUid().toString());
        context.setAttributeValue("userpassword", user.getPass());
        return context;
    }

    @Override
    public void deleteUser(UserAuth user) {
        LdapName build = LdapNameBuilder.newInstance()
                .add("cn", user.getName())
                .build();
        ldapTemplate.unbind(build);
        log.info("删除用户={}成功", user.getName());
    }

    @Override
    public Boolean checkUserPass(UserAuth user) {

        Boolean result = Boolean.FALSE;


        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", OBJECT_FILED)).
                and(new EqualsFilter("cn", user.getName()));

        List<String> search = ldapTemplate.search("", filter.toString(), (AttributesMapper<String>) attributes -> new String((byte[]) attributes.get("userpassword").get()));

        //查询是否存在 不存在则创建
        if (search == null || search.isEmpty()) {

            try {
                ldapTemplate.bind(getDirContext(user));
                log.info("重新插入用户={}成功", user.getName());
                result = Boolean.TRUE;
            } catch (NameAlreadyBoundException e) {

                log.warn("重新插入用户={}已经存在", user.getName());
            }

        } else {
            //等待所有用户转移结束后取消不存在即创建 直接利用验证
            // result = ldapTemplate.authenticate("", filter.toString(), user.getPass());

            result = search.get(0).equals(user.getPass());

            if (result) {
                log.info("用户={} 验证成功", user.getName());
            } else {
                log.warn("用户={} 验证失败", user.getName());
            }

        }


        return result;

    }

    @Override
    public UserAuth findUserByName(String userName) {

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", OBJECT_FILED)).
                and(new EqualsFilter("cn", userName));

        List<UserAuth> search = ldapTemplate.search("", filter.toString(), (AttributesMapper<UserAuth>) attributes ->

                {
                    UserAuth userAuth = new UserAuth();
                    userAuth.setName((String) attributes.get("cn").get());
                    userAuth.setNickName((String) attributes.get("sn").get());
                    userAuth.setUid(Integer.parseInt(attributes.get("uid").get().toString()));
                    userAuth.setPass(new String((byte[]) attributes.get("userpassword").get()));

                    return userAuth;
                }
        );
        log.info("查询用户={}信息成功", userName);
        return (search == null || search.isEmpty()) ? null : search.get(0);
    }
}
