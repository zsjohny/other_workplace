package com.songxm.credit.service;

import com.alibaba.fastjson.JSON;
import com.github.abel533.entity.Example;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.moxie.commons.BaseBeanUtils;
import com.moxie.commons.BaseStringUtils;
import com.songxm.credit.comon.credit.diversion.constant.ErrorKeys;
import com.songxm.credit.comon.credit.diversion.constant.JwtClaim;
import com.songxm.credit.comon.credit.diversion.constant.PropKeys;
import com.songxm.credit.comon.credit.diversion.dto.LoginReqDTO;
import com.songxm.credit.comon.credit.diversion.dto.LoginpsReqDto;
import com.songxm.credit.comon.credit.diversion.dto.Token;
import com.songxm.credit.comon.credit.diversion.enums.LogStatus;
import com.songxm.credit.comon.credit.diversion.enums.TokenType;
import com.songxm.credit.comon.credit.diversion.utils.JwtUtils;
import com.songxm.credit.comon.credit.diversion.utils.Md5Util;
import com.songxm.credit.compnent.RedisComponent;
import com.songxm.credit.config.BaseConfig;
import com.songxm.credit.dao.credit.deversion.domain.TUserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import moxie.cloud.service.common.constants.BaseErrorKeys;
import moxie.cloud.service.server.ServerException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sxm on 17/4/3.
 */
@Slf4j
@Service
public class LoginServvice extends BaseService<TUserLoginInfo> {
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private Environment env;


    /**
     * 新增登录实体信息
     * @param loginReqDTO
     * @rturn
     */
    public Boolean addLoginInfo(LoginReqDTO loginReqDTO){

        log.info("注册信息  loginReqDTO:{}",loginReqDTO);
        Object code = redisComponent.get(loginReqDTO.getLoginName());
        log.info("缓存中获取到的smsCode:{}",code);

        Preconditions.checkArgument(code==null?false:true,"验证码已经失效,请重新获取!");


        Example example = new Example(TUserLoginInfo.class);
        example.createCriteria().andEqualTo("loginName",loginReqDTO.getLoginName());
        List<TUserLoginInfo> list = this.getMapper().selectByExample(example);
        if(null == list || list.size()>0){
            log.info("已存在的用户:{}",list);
            throw ServerException.fromKey(ErrorKeys.LOGIN_CREATE_FAIL);
        }
        try {
            TUserLoginInfo loginInfo = new TUserLoginInfo();
            BaseBeanUtils.copyProperties(loginReqDTO,loginInfo);
            log.info("拷贝之后的TUserLoginInfo:{}",loginInfo);
            loginInfo.setId(BaseStringUtils.digitalUUID());
            loginInfo.setLastLoginTime(new Date());
            loginInfo.setLoginSalt(Md5Util.getSalt());
            loginInfo.setLoginPass(Md5Util.password(loginInfo.getLoginPass(),loginInfo.getLoginSalt()));
            loginInfo.setCreatedAt(new Date());
            loginInfo.setUpdatedAt(new Date());
            loginInfo.setLoginStatus(LogStatus.OFFLINE.toString());
            loginInfo.setCreatedBy(loginInfo.getLoginName());
            loginInfo.setUpdatedBy(loginInfo.getUpdatedBy());
            this.save(loginInfo);
        }catch (Exception e){
            log.error("服务器内部错误:{}",e);
            throw ServerException.fromKey(BaseErrorKeys.INTERNAL_SERVER_ERROR);
        }

        log.info("成功创建账号实体:{}",Boolean.TRUE);
        return Boolean.TRUE;
    }

    /**
     * 修改密码
     * @param loginReqDTO
     * @return
     */
    public Boolean updateLoginInfo(LoginpsReqDto loginReqDTO){
      log.info("修改密码请求参数信息:{}",loginReqDTO);
      Preconditions.checkArgument(loginReqDTO.getLoginPass().equals(loginReqDTO.getConfim_pass()),"两次密码不一致,请重试!");
        Object code = redisComponent.get(loginReqDTO.getLoginName());
        log.info("缓存中获取到的smsCode:{}",code);
        Preconditions.checkArgument(code==null?false:true,"验证码已经失效,请重新获取!");

        if (!loginReqDTO.getSmsCode().equals((String)code)){
            log.error("验证码错误,请求参数:{},缓存中的:{}",loginReqDTO.getSmsCode(),code);
            throw ServerException.fromKey(ErrorKeys.SMS_CODE);
        }
       // String oldLogin = l
        TUserLoginInfo tUserLoginInfo = new TUserLoginInfo();
        tUserLoginInfo.setLoginName(loginReqDTO.getLoginName());
        tUserLoginInfo = this.getMapper().selectOne(tUserLoginInfo);

        if(null==tUserLoginInfo){
            throw ServerException.fromKey(ErrorKeys.USER_NON_EXIST);
        }
        if(tUserLoginInfo.getLoginStatus().equalsIgnoreCase("lock")){
            throw ServerException.fromKey(ErrorKeys.USER_IS_LOCK);
        }
        tUserLoginInfo.setLoginPass(Md5Util.password(loginReqDTO.getLoginPass(),tUserLoginInfo.getLoginSalt()));
        this.updateByIdSelective(tUserLoginInfo);
        log.info("密码修改成功,请重新登录");
        return Boolean.TRUE;
    }

    /**
     * 登录操作,登录成功返回 token令牌
     * @param login_name
     * @param passwd
     * @return
     */
    public Token login(String login_name,String passwd){
     log.info("请求参数 login_name:{},login_pass",login_name,passwd);
      Preconditions.checkArgument(StringUtils.isNotBlank(login_name),"登录账户不能为空");
        Preconditions.checkArgument(login_name.matches("^0?1\\d{10}$"),"手机号不正确");
        TUserLoginInfo tUserLoginInfo = new TUserLoginInfo();
        tUserLoginInfo.setLoginName(login_name);
        tUserLoginInfo =  this.getMapper().selectOne(tUserLoginInfo);
        if(null == tUserLoginInfo){
            throw ServerException.fromKey(ErrorKeys.USER_NON_EXIST);
        }

        if(tUserLoginInfo.getLoginStatus().equalsIgnoreCase("lock")){
            throw ServerException.fromKey(ErrorKeys.USER_IS_LOCK);
        }

        log.info("tUserLoginInfo.getLoginPass:{}",tUserLoginInfo.getLoginPass());
        log.info("传来的明文passwd:{}",passwd);
        log.info("传来的密文:{}",Md5Util.password(passwd,tUserLoginInfo.getLoginSalt()));
        log.info("判断情况:{}",tUserLoginInfo.getLoginPass().equals(Md5Util.password(passwd,tUserLoginInfo.getLoginSalt())));
        if(!tUserLoginInfo.getLoginPass().equals(Md5Util.password(passwd,tUserLoginInfo.getLoginSalt()))){
            throw ServerException.fromKey(ErrorKeys.LOGIN_PASSWD);
        }

        try {


                long now = System.currentTimeMillis();
        long exp = now + Long.valueOf(env.getProperty(PropKeys.TENANT_TOKEN_EXPIRE_IN_DAY))* 24 * 3600 * 1000;

        String jti = UUID.randomUUID().toString().replace("-", "");// jwt id
        Map<String, Object> claims = ImmutableMap
                .<String, Object>builder()
                .put(JwtClaim.aud, env.getProperty(PropKeys.TENANT_TOKEN_EXPIRE_IN_DAY))
                .put(JwtClaim.type, TokenType.TENANT.name())
                .put(JwtClaim.iss, "QIUQIU")
                .put(JwtClaim.iat, now / 1000)
                .put(JwtClaim.nbf, now / 1000)
                .put(JwtClaim.jti, jti)
                .put(JwtClaim.userId,tUserLoginInfo.getId())
                .put(JwtClaim.loginNo,tUserLoginInfo.getLoginName())
                .put(JwtClaim.secVer, "1.0.0")
                .build();
             Token token = JwtUtils.getToken(claims, (exp - now) / 1000, BaseConfig.TOKEN_SECURECT);

             log.info("key----->:{}",JSON.toJSONString(token));
             redisComponent.set(JSON.toJSONString(token.getToken()),tUserLoginInfo.getLoginName() ,(exp - now) / 1000);
            return token;
        }catch (Exception e){
            log.error("获取动态令牌异常:{}",e);
            throw ServerException.fromKey(BaseErrorKeys.INTERNAL_SERVER_ERROR);
        }

    }

    public Boolean logout(HttpServletRequest request,String loginName){
        Preconditions.checkArgument(StringUtils.isNotBlank(loginName),"登录账户不能为空");
        Preconditions.checkArgument(loginName.matches("^0?1\\d{10}$"),"手机号不正确");
        String jwtoken = request.getHeader("Authorization");
         if(null!=jwtoken){
            Object obj =  redisComponent.get(jwtoken);
             if(null!=obj && obj.toString().equals(loginName)) {
                 redisComponent.del(JSON.toJSONString(jwtoken));
             }
         }

        TUserLoginInfo tUserLoginInfo = new TUserLoginInfo();
        tUserLoginInfo.setLoginName(loginName);
        tUserLoginInfo = this.getMapper().selectOne(tUserLoginInfo);
        tUserLoginInfo.setLoginStatus(LogStatus.OFFLINE.toString());
        this.updateByIdSelective(tUserLoginInfo);

        return Boolean.TRUE;

    }
}
