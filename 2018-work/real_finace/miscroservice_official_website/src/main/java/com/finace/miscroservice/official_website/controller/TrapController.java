package com.finace.miscroservice.official_website.controller;


import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.CreditGoAccount;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.credit.CreditConstant;
import com.finace.miscroservice.official_website.rpc.UserRpcService;
import com.finace.miscroservice.official_website.service.TrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;


/**
 * 企业 红包 手续费账户 圈存圈提
 */
@RestController
@RequestMapping(value = "/tarp")
public class TrapController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TrapController.class);


    @Autowired
    private TrapService trapService;

    @Autowired
    private UserRpcService userRpcService;

    @Value("${h5.host}")
    private String htmlHost;


    /**
     * 获取账户余额
     *
     * @return
     */
    @RequestMapping(value = "getAccountBalance/auth")
    public Response getAccountBalance() {
        String userId = getUserId();

        return trapService.getAccountBalance(userId);
    }

    /**
     * 忘记密码
     *
     * @param msgCode
     * @return
     */
    @RequestMapping(value = "forgotPwdUrl/auth")
    public Response forgotPwdUrl(String msgCode) throws Exception{
        String userId = getUserId();
        log.info("用户{}重置密码", userId);

        CreditGoAccount account = userRpcService.getUserAccountByUserId(userId);
        if (account != null && account.getIsSetPass() != 0) {
            String req = trapService.resetPass(account, msgCode);

            return Response.success(req);
//            try {
//                outHtml(req);
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("用户{},忘记密码,异常信息：{}", userId, e);
//            }
        } else {
            log.warn("用户{},忘记密码,账户不存在", userId);
            return Response.errorMsg("账户不存在");
        }

    }

    /**
     * 跳转到忘记密码界面
     *
     * @param model
     */
    @RequestMapping(value = "toForgotPwdUrl")
    public void toForgotPwdUrl(Model model) {

        try {
            response.get().sendRedirect(htmlHost + "/newPCWeb/newPCWeb/html/wangjimima.html");
        } catch (Exception e) {
            log.error("跳转到忘记密码界面异常:{}", e);
        }
    }

    /**
     * 修改密码成功界面
     * @param model
     */
    @RequestMapping(value = "toForgotPwdSuccessUrl")
    public void toForgotPwdSuccessUrl(Model model) {
        try {
            response.get().sendRedirect(htmlHost + "/newPCWeb/newPCWeb/html/recharge.html");
        } catch (Exception e) {
            log.error("修改密码成功界面异常:{}", e);
        }
    }



    /**
     * 圈存
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "doTrap/auth")
    public Response doTrap(Model model, @RequestParam(value = "phone", required = false) String phone, String money, String accountType) {
        String userId = getUserId();

        log.info("用户{}开始圈存", userId);

        return trapService.doTrap(phone, money, accountType);
    }

    /**
     * 圈存成功回调
     *
     * @param model
     */
    @RequestMapping(value = "tarpSuccessfulUrl")
    public void tarpSuccessfulUrl(Model model) {
        String bgData = request.getParameter("bgData");
        log.info("圈存回调tarpSuccessfulUrl");

        Map map = this.getParamsMap(request);

        String txCode = map.get("txCode").toString();
        String retCode = map.get("retCode").toString();
        String seqNo = map.get("seqNo").toString();
        String txTime = map.get("txTime").toString();
        String txDate = map.get("txDate").toString();
        String productId = map.get("productId").toString();
        String txAmount = map.get("txAmount").toString();
        String retMsg = map.get("retMsg").toString();

        try {

            if (retCode.equals(CreditConstant.CREDIT_SUCCESS)) {
                response.get().sendRedirect("");
            } else {
                response.get().sendRedirect("");
            }

        } catch (Exception e) {
            log.error("圈存回调异常:{}", e);
        }
    }

    /**
     * 圈存业务回调
     *
     * @param model
     */
    @RequestMapping(value = "tarpNotifyUrl")
    public void tarpNotifyUrl(Model model) {
        String bgData = request.getParameter("bgData");
        log.info("圈存回调tarpNotifyUrl");
        try {
            response.get().getWriter().write("success");
        } catch (Exception e) {

        }
    }


    /**
     * 圈提
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "doRing/auth")
    @ResponseBody
    public Response doRing(Model model, @RequestParam(value = "phone", required = false) String phone, String money, String accountType) {
        String userId = getUserId();
        log.info("用户{}开始圈提", userId);

        return trapService.doRing(phone, money, accountType);
    }


    /**
     * 圈提成功回调
     *
     * @param model
     */
    @RequestMapping(value = "ringSuccessfulUrl")
    public void ringSuccessfulUrl(Model model) {
        String bgData = request.getParameter("bgData");
        log.info("圈提回调ringSuccessfulUrl");
        Map map = this.getParamsMap(request);

        String txCode = map.get("txCode").toString();
        String retCode = map.get("retCode").toString();
        String seqNo = map.get("seqNo").toString();
        String txTime = map.get("txTime").toString();
        String txDate = map.get("txDate").toString();
        String productId = map.get("productId").toString();
        String txAmount = map.get("txAmount").toString();
        String retMsg = map.get("retMsg").toString();

        try {
            if (retCode.equals(CreditConstant.CREDIT_SUCCESS)) {
                response.get().sendRedirect("");
            } else {
                response.get().sendRedirect("");
            }
        } catch (Exception e) {
            log.error("圈提回调异常:{}", e);
        }
    }

    /**
     * 圈提业务回调
     *
     * @param model
     */
    @RequestMapping(value = "ringNotifyUrl")
    public void ringNotifyUrl(Model model) {
        String bgData = request.getParameter("bgData");
        log.info("圈提回调ringNotifyUrl");
        try {
            response.get().getWriter().write("success");
        } catch (Exception e) {

        }
    }

}












