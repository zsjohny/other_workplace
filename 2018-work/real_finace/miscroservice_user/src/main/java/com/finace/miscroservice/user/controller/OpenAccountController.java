package com.finace.miscroservice.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.user.po.CreditGoAccountPO;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.service.OpenAccountLogService;
import com.finace.miscroservice.user.service.OpenAccountService;
import com.finace.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
@RestController
@RefreshScope
public class OpenAccountController extends BaseController {


    @Value("${user.credit2go.version}")
    protected String version;

    @Value("${user.credit2go.bankcode}")
    protected String bankcode;

    @Value("${user.credit2go.instcode}")
    protected String instcode;

    @Value("${user.credit2go.coinstchannl}")
    protected String coinstchannl;

    @Value("${creditgo.url}")
    protected String host;
    @Autowired
    @Lazy
    private MqTemplate mqTemplate;
    @Autowired
    private OpenAccountService openAccountService;

    @Autowired
    private OpenAccountLogService openAccountLogService;

    @Autowired
    private UserService userService;

    /**
     * 开户页面
     * @param idNo 身份证
     * @param name 姓名
     * @param acctUse
     */
    @RequestMapping("open/account/auth")
    public void openAccount(String idNo,String name,@RequestParam(required = false,value = "acctUse",defaultValue = "00000")String acctUse,
                            @RequestParam(required = false,value = "identity",defaultValue = "1")String identity){
        String userId = getUserId();
        UserPO userPO =userService.findUserOneById(userId);

        String reqStr = openAccountService.openAccount(userId,idNo,name,userPO.getPhone(),acctUse,identity);
        try {
            outHtml(reqStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开户页面 前验证
     * @param idNo 身份证
     */
    @RequestMapping("open/account/pre/auth")
    public Response openAccountPre(String idNo){
        String userId = getUserId();
       return openAccountService.findOpenAccountByIdNo(idNo,userId);

    }
    @RequestMapping("open/account/ret")
    public Response openAccountRet(){
        String accountId = request.getParameter("accountId");
        String txCode = request.getParameter("txCode");

        return Response.errorMsg("请求失败");
    }

    /**
     * 业务回调
     */
    @RequestMapping("open/account/notify")
    public void openAccountNotify(){
        String bgData=request.getParameter("bgData");
       openAccountService.openAccountNotify(bgData,response.get());
    }

    /**
     * 设置密码
     */
    @RequestMapping("set/pass/auth")
    public void setPass(){
        String userId = getUserId();
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
//        CreditGoAccountPO account = openAccountService.findOpenAccountByAccountId(accountId);
        if (account!=null&&StringUtils.isEmpty(account.getAccountId())){
            Map map = openAccountService.findAccountIdByIdNo(userId,account.getIdNo(),account.getIdType());
            String acctState = map.get("acctState").toString();
            String accountId = map.get("accountId").toString();
            account.setAccountId(accountId);
        }
        System.out.println(account.getAccountId());
        if (account!=null&&account.getAccountId()!=null){
            String reqStr = openAccountService.setPass(account);
            try {
                outHtml(reqStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置密码
     * @param msgCode
     */
    @RequestMapping("reset/pass/auth")
    public void resetPass(String msgCode){
        String userId = getUserId();
//        String userId = "60410";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getIsSetPass()!=0){
            String req = openAccountService.resetPass(account,msgCode);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绑定银行卡页面
     */
    @RequestMapping("bind/card/auth")
    public void bindCard(HttpServletRequest request){
        String userId = getUserId();
        String ip = Iptools.gainRealIp(request);
//        String ip = "112.17.92.53";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&StringUtils.isNotEmpty(account.getAccountId())){
            String req = openAccountService.bindCard(account,ip);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解绑银行卡
     */
    @RequestMapping("unbind/card/auth")
    public Response unbindCard(HttpServletRequest request){
//        String ip = Iptools.gainRealIp(request);
//        System.out.println("ip地址"+ip);
        String userId = getUserId();
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&StringUtils.isNotEmpty(account.getAccountId())){
          Map map  = openAccountService.unbindCard(account);

            String retCode = map.get("retCode").toString();
            if ("00000000".equals(retCode)){
                return Response.success();
            }else {
                return Response.errorMsg(map.get("retCode").toString()+"--"+map.get("retMsg").toString());
            }
        }else {
            return Response.errorMsg("解绑银行卡失败");
        }

    }


    /**
     * 充值页面
     */
    @RequestMapping("direct/recharge/auth")
    public void directRecharge(Double txAmount){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&StringUtils.isNotEmpty(account.getAccountId())){
            String req = openAccountService.directRecharge(account,txAmount);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绑卡关系查询
     * @param state 默认：0 查询全部  1：查询当前绑定
     * @return
     */
    @RequestMapping("card/bind/query/auth")
    public Response cardBindDetailsQuery(@RequestParam(value = "state",defaultValue = "0",required = false)String state){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&StringUtils.isNotEmpty(account.getAccountId())){
            Map map  = openAccountService.cardBindDetailsQuery(account.getAccountId(),state);
            return Response.success(map);
        }else {
            return Response.errorMsg("绑卡关系查询失败");
        }
    }

    /**
     * 电子账户查询
     * @return
     */
    @RequestMapping("balance/query/auth")
    public Response balanceQuery(){
        String userId = getUserId();
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&StringUtils.isNotEmpty(account.getAccountId())){
            Map map  = openAccountService.balanceQuery(account.getAccountId());
            return Response.success(map);
        }else {
            return Response.errorMsg("电子账户查询失败");
        }
    }


    /**
     * 发送短信
     * @param srvTxCode 2:重置密码
     * @param smsType smsType=1是即信短信文本验证码
    smsType=2是即信短信语音验证码
     * @return
     */
    @RequestMapping("sms/code/apply/auth")
    public Response smsCodeApply(Integer srvTxCode,@RequestParam(value = "smsType",required = false,defaultValue = "1") String smsType){
        String userId = getUserId();
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getAccountId()!=null){
            openAccountService.smsCodeApply(srvTxCode,account.getMobile(),smsType,account);
        }else {
            return Response.errorMsg("用户未开户");
        }
        return Response.success();
    }

    /**
     * 提现前 信息
     * @return
     */
    @RequestMapping("withdraw/pre/auth")
    public Response withdrawPre(){
        String userId = getUserId();
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
         JSONObject js = openAccountService.withdrawPre(userId,account);
        return Response.success(js);
    }


    /**
     * 提现
     * @param txAmount 提现金额
     */
    @RequestMapping("credit/withdraw/auth")
    public void creditWithdrow(Double txAmount,@RequestParam(required = false,value = "routeCode",defaultValue = "")String routeCode){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);

        if (account!=null&&account.getAccountId()!=null){
            String req = openAccountService.creditWithdrow(account,txAmount,routeCode);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 修改手机号
     * @param phone
     * @param smsCode
     * @return
     */
    @RequestMapping("update/phone/auth")
    public Response updatePhone(String phone,String smsCode){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getAccountId()!=null){
            return openAccountService.updatePhone(phone,smsCode,account);
        }
        return Response.errorMsg("修改手机号失败");
    }


    /**
     * 缴费授权
     * @return
     */
    @RequestMapping("payment/auth")
    public void paymentAuthPage(){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getAccountId()!=null){
            String req = openAccountService.paymentAuthPage(account);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 还款授权
     * @return
     */
    @RequestMapping("repay/auth")
    public void repayAuthPage(){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getAccountId()!=null){
            String req = openAccountService.repayAuthPage(account);
            try {
                outHtml(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * P2P产品缴费授权解约
     * @return
     */
    @RequestMapping("payment/chancel/auth")
    public void paymentAuthCancel(){
        String userId = getUserId();
//        String userId = "60109";
        CreditGoAccountPO account = openAccountService.findOpenAccountByUserId(userId);
        if (account!=null&&account.getAccountId()!=null){
            Map map = openAccountService.paymentAuthCancel(account);

        }
    }


    /**
     * 资金流水
     * @return
     */
    @RequestMapping("moneyFlowingWater/auth")
    public Response moneyFlowingWater(Integer page,Integer code){

//        String userId = "60410";
        String userId = getUserId();
        return openAccountLogService.findMoneyFlowingWaterByUserId(userId,page,code);
    }


//    @RequestMapping("voucher/pay/auth")
//    public Response voucherPay(){
//        String userId = getUserId();
//
//    }


    @RequestMapping("find/account")
    public Response findAccountByIdNo(String idNo,String idType){


        Map map = openAccountService.findAccountIdByIdNo("60109",idNo,idType);
        return Response.success(map);
    }
//
    @PostConstruct
    public void  test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seqNo","124578");
        jsonObject.put("txCode","withdraw");
        jsonObject.put("txTime","152158");
        TimerScheduler timerScheduler = new TimerScheduler();
        //订单是否成功处理
        timerScheduler.setType(TimerSchedulerTypeEnum.WITHDRAW_DEPOSIT_BANK_DELAY_TASK.toNum());
        timerScheduler.setName(TimerSchedulerTypeEnum.WITHDRAW_DEPOSIT_BANK_DELAY_TASK.toChar() + UUIdUtil.generateUuid());
//                timerScheduler.setCron(sdf.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr()) + 20*60), "0")));
        timerScheduler.setParams(JSONObject.toJSONString(jsonObject));
        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
    }

}
