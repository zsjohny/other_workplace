package com.yujj.web.controller.delegate;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.CodeUseage;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;

@Service
public class SecurityDelegator {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterFacade registerFacade;

    public JsonResponse sendPhoneVerifyCode(String phone) {
        JsonResponse jsonResponse = new JsonResponse();

//        User user = userService.getUserByRelatedName(phone, UserType.PHONE);
        User user = userService.getUserByAllWay(phone);
        
        if (user == null) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_PHONE_NOT_REGISTED);
        }

        boolean success = registerFacade.sendPhoneVerifyCode(phone, CodeUseage.PHONE_RESET_PASSWORD);
        return jsonResponse.setResultCode(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    }

    public JsonResponse resetPasswordCommit(String phone, String password, String verifyCode) {
        JsonResponse jsonResponse = new JsonResponse();

        if (StringUtils.length(password) < 6) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (!registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_RESET_PASSWORD)) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_PHONE_VERIFY_CODE_INVALID);
        }

//        User user = userService.getUserByRelatedName(phone, UserType.PHONE);
        User user = userService.getUserByAllWay(phone);

        if (user == null) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_PHONE_NOT_REGISTED);
        }

        userService.updateUserPassword(user.getUserId(), DigestUtils.md5Hex(password));

        return jsonResponse.setSuccessful();
    }
}
