package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.constant.TokenUrlType;
import com.yujj.entity.account.UserDetail;

@Controller
@Login
@RequestMapping("/mobile/token")
public class MobileTokenController {

    @Autowired
    private MemcachedService memcachedService;

    @RequestMapping("/url")
    @ResponseBody
    public JsonResponse getTokenUrl(@RequestParam("type") TokenUrlType tokenUrlType, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        if (tokenUrlType == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        String token = UUID.randomUUID().toString();
        long userId = userDetail.getUserId();
        memcachedService.set(MemcachedKey.GROUP_KEY_TOKEN_LOGIN, token, DateConstants.SECONDS_FIVE_MINUTES, userId);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("tokenUrl", tokenUrlType.genUrl(token));
        return jsonResponse.setSuccessful().setData(data);
    }

}
