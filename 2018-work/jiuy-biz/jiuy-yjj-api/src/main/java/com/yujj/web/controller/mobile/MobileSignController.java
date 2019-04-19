package com.yujj.web.controller.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.UserSignDelegator;

@Controller
@Login
@RequestMapping("/mobile/sign")
public class MobileSignController {

    @Autowired
    private UserSignDelegator userSignDelegator;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse signin(UserDetail userDetail, ClientPlatform clientPlatform) {
        long userId = userDetail.getUserId();
        return userSignDelegator.signin(userId,clientPlatform);
    }

    @RequestMapping(value = "/state")
    @ResponseBody
    public JsonResponse signState(UserDetail userDetail) {
        long userId = userDetail.getUserId();
        return userSignDelegator.getSignState(userId);
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public JsonResponse signInfo(UserDetail userDetail) {
        long userId = userDetail.getUserId();
        return userSignDelegator.getSignInfo(userId);
    }
}
