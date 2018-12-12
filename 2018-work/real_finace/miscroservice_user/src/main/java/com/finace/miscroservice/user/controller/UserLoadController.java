package com.finace.miscroservice.user.controller;


import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.CodeGenerateUtil;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.config.LoadRealm;
import com.finace.miscroservice.user.dao.PcUserDao;
import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.po.UserPO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * user登陆服务控制层:用户登录,生成图形验证码
 */
@RestController
public class UserLoadController extends BaseController {

    private Log logger = Log.getInstance(UserLoadController.class);

    @Autowired
    private LoadRealm loadRealm;

    @Autowired
    private PcUserDao pcUserDao;

    @Autowired
    private UserDao userDao;

       /**
     * 用户登录
     *
     * @param phone   手机号码
     * @param pass    密码
     * @param channel h5需要传送的channel标识
     *                uid--设备识别号每次请求都需要传
     * @return 201 临时用户注册
     */
    @RequestMapping("load")
    public Response load(@RequestParam(value = "pushId", required = false) String pushId,
                         @RequestParam(value = "channel", required = false) String channel,
                         @RequestParam(value = "userTmp", required = false) Integer userTmp,
                         String phone, String pass, HttpServletRequest res, HttpServletResponse req) {

        logger.info("用户={} 开始登录pushId={}", phone, pushId);

        String uid = res.getHeader(JwtToken.UID);
        //若userTmp==0则为PC， 临时表 存在 则为临时用户
        Register register = pcUserDao.findRegisterTmp(phone);
        if (register!=null&&userTmp==0){
            return Response.successRegisterTmp();
        }
        if (StringUtils.isNoneEmpty(phone, pass, uid)) {
            if (loadRealm.load(phone, pass, uid, WebUtils.toHttp(req), pushId)) {
                logger.info("用户={} 登陆成功", phone);
                UserPO userPO = userDao.getUserByUserPhone(phone);
                return Response.successMsg(String.valueOf(userPO.getTypeId()));
            } else {
                logger.info("用户={} 密码={} 错误", phone, pass);
                return Response.errorMsg("密码错误");
            }

        }
        logger.warn("用户登陆没有参数 uid={} ,pass={},phone={}", uid, pass, phone);
        return Response.errorByArray();
    }


    /**
     * 生成图形验证码
     *
     * @param uid      app设备的uid
     * @param did      浏览器的唯一标识(app使用不到)
     * @param uri      方法名称
     * @param response 响应体
     *                 使用方法 ==>生成后在需要验证的方法的请求header添加 key(code):value(用户获取此验证码填写的内容)
     */
    @RequestMapping("generate")
    public void generate(@RequestHeader(JwtToken.UID) String uid,
                         @RequestParam(value = "did", required = false) String did,
                         String uri, HttpServletResponse response) {
        logger.info("验证图形验证码,客户端的设备id={}", did);

        uid = uid + (StringUtils.isEmpty(did) ? "" : ":" + did);

        logger.info("用户={} 开始生成验证码, uri={}", uid, uri);
        CodeGenerateUtil.generate(uid, uri, response);
    }


    /**
     * 验证码验证
     *
     * @param uid  app设备的uid
     * @param code 验证码
     * @param uri  方法名称
     * @param did  浏览器的唯一标识(app使用不到)
     * @return
     */
    @RequestMapping("verify")
    public Response verify(@RequestHeader(JwtToken.UID) String uid, String code, String uri,
                           @RequestParam(value = "did", required = false) String did) {

        logger.info("用户={} 开始验证code={}, uri={}, did={}", uid, code, uri, did);


        if (!CodeGenerateUtil.verify(uid + (StringUtils.isEmpty(did) ? "" : ":" + did), code, uri)) {
            logger.info("用户={} 验证码验证不通过", uid);
            return Response.errorMsg("验证码验证不通过");
        } else {
            logger.info("用户={} 验证码验证通过", uid);
            return Response.success();
        }
    }


}
