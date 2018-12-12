package com.finace.miscroservice.user.controller;

import com.finace.miscroservice.commons.plug.mybatis.util.MybatisOperaterUtil;
import com.finace.miscroservice.commons.plug.mybatis.util.MybatisSqlDefaultUtil;
import com.finace.miscroservice.commons.plug.mybatis.util.MybatisSqlWhereBuildUtil;
import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class AppController {


    @Autowired
    private UserService  userService;

    @PostConstruct
    public void init() throws Exception {


        if (1 == 1) {
            UserPO userPO = new UserPO();
            userPO.setPhone("131");
            userService.insterUser(userPO);
            return;
        }


        Pid pid = new Pid();
        pid.setUserNamePass("admin");
        pid.setUserPassWord("admin");
        pid.setUserPass(1);
        int save = MybatisOperaterUtil.getInstance().save(pid);
        System.out.println(save);

        pid = new Pid();

        pid.setUserNamePass("admin123");
        pid.setUserPassWord("admin123");
        pid.setUserPass(2);
        int update = MybatisOperaterUtil.getInstance().update(pid,
                MybatisSqlWhereBuildUtil.getInstance().eq("user_name_pass", "admin").like("user_name_pass", "%ad"));
        System.out.println(update);
        pid = new Pid();
        pid.setUserPassWord(MybatisSqlDefaultUtil.STRING_DEFAULT_VALUE);
        pid.setUserNamePass(MybatisSqlDefaultUtil.STRING_DEFAULT_VALUE);
        pid.setUserPass(MybatisSqlDefaultUtil.INTEGER_DEFAULT_VALUE);
        List<Pid> one = MybatisOperaterUtil.getInstance().finAll(new Pid(),
                MybatisSqlWhereBuildUtil.getInstance().like("user_name_pass", "admin").page(1, 10));
        System.out.println(one);
        System.out.println("_______________");
    }


}
