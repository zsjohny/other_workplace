package com.songxm.credit.controller;

import com.google.common.net.HttpHeaders;
import com.moxie.cloud.commons.controller.BaseController;
import com.songxm.credit.comon.credit.diversion.constant.AppConsts;
import com.songxm.credit.comon.credit.diversion.dto.LoginReqDTO;
import com.songxm.credit.comon.credit.diversion.dto.LoginpsReqDto;
import com.songxm.credit.comon.credit.diversion.dto.Token;
import com.songxm.credit.comon.credit.diversion.enums.AuthQRole;
import com.songxm.credit.compnent.AuthQScope;
import com.songxm.credit.service.LoginServvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by sxm on 17/3/18.
 */
@RestController
@RequestMapping(value = AppConsts.BASE_PATH + "/"+AppConsts.VERSION)
@Api(value = "登录注销改密相关ApI.",description = "用户注册登录,授权令牌,修改密码",tags = "Login Oprator API")
@CrossOrigin
@Slf4j
public class LoginController extends BaseController {
   @Autowired
   private LoginServvice loginServvice;



    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Boolean login(@Valid @ModelAttribute LoginReqDTO loginReqDTO) {
        return loginServvice.addLoginInfo(loginReqDTO);
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParam(paramType = "header", name = HttpHeaders.AUTHORIZATION, required = true, value = "user token", dataType = "string")
    @RequestMapping(value = "/auth/passwd", method = RequestMethod.PUT)
    @AuthQScope({AuthQRole.QIU_SERVICE})
    public Boolean login(@Valid @ModelAttribute LoginpsReqDto loginReqDTO, HttpServletRequest request) {


//        long now = System.currentTimeMillis();
//        long exp = now + Long.valueOf(env.getProperty(PropKeys.TENANT_TOKEN_EXPIRE_IN_DAY))* 24 * 3600 * 1000;
//
//        String jti = UUID.randomUUID().toString().replace("-", "");// jwt id
//        Map<String, Object> claims = ImmutableMap
//                .<String, Object>builder()
//                .put(JwtClaim.aud, env.getProperty(PropKeys.TENANT_TOKEN_EXPIRE_IN_DAY))
//                .put(JwtClaim.type, TokenType.TENANT.name())
//                .put(JwtClaim.iss, "QIUQIU")
//                .put(JwtClaim.iat, now / 1000)
//                .put(JwtClaim.nbf, now / 1000)
//                .put(JwtClaim.jti, jti)
//                .put(JwtClaim.userId,loginInfo.getId())
//                .put(JwtClaim.loginNo,loginInfo.getLoginName())
//                .put(JwtClaim.secVer, config.getTenantJwtSecretActiveVersion())
//                .build();
//        return JwtUtils.getToken(claims, (exp - now) / 1000, config.getSecret(TokenType.USER));
        return loginServvice.updateLoginInfo(loginReqDTO);
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query",name = "loginName",required = true,dataType = "string"), @ApiImplicitParam(paramType = "query",name = "passwd",required = true,dataType = "string")})
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Token login(@RequestParam("loginName") String login_name , @RequestParam("passwd") String passwd) {


        return loginServvice.login(login_name,passwd);
    }

    @ApiOperation(value = "用户注销(退出),退出之后动态令牌会清除掉")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", name = HttpHeaders.AUTHORIZATION, required = true, value = "user token", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "loginName", required = true, value = "登录账号", dataType = "string")
    })
    @RequestMapping(value = "/auth/logout", method = RequestMethod.DELETE)
    public Boolean logout(HttpServletRequest request,@RequestParam(value = "loginName" ,required = true) String loginName) {

        return loginServvice.logout(request,loginName);
    }
}
