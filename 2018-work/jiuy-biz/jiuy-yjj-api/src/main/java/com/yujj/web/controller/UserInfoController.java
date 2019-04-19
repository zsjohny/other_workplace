package com.yujj.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.util.anno.Login;
import com.yujj.business.service.UserCoinService;
import com.yujj.entity.account.UserDetail;

@Controller
@Login
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserCoinService userCoinService;

    @RequestMapping("/coin")
    public String userCoin(UserDetail userDetail, Map<String, Object> model) {
        long userId = userDetail.getUserId();

        UserCoin userCoin = userCoinService.getUserCoin(userId);
        model.put("userCoin", userCoin);

        return "userinfo/coin";
    }

}
