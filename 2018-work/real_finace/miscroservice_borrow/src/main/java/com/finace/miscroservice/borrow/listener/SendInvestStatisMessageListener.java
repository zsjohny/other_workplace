package com.finace.miscroservice.borrow.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowTenderService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.entity.BorrowTender;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Regular;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.commons.utils.tools.message.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RefreshScope
public class SendInvestStatisMessageListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(SendInvestStatisMessageListener.class);

    @Autowired
    private BorrowTenderService borrowTenderService;

    @Value("${borrow.sms.phone}")
    private String phones;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Override
    protected void transferTo(String transferData) {
        logger.info(String.format("【发送投资短信】任务接收中...，transferData为[%s]", transferData));
        if (StringUtils.isEmpty(transferData)) {
            logger.warn(String.format("【发送投资短信】任务接收失败，transferData为[%s]", transferData));
            return;
        }

        try {
            // type  1--给老板发送平台统计短信 2--给用户发红包过期提醒信息
            JSONObject jsonObject = JSONObject.parseObject(transferData);
            String type = jsonObject.getString("type");

            if ("1".equals(type)) {
                this.sendStatisMsg();
            } else if ("2".equals(type)) {
                //this.sendHbOverdueMsg();
            }


        } catch (Exception e) {
            logger.error("【发送投资短信】发送统计短信失败，系统异常:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 给用户发红包过期提醒信息
     */
    private void sendHbOverdueMsg(){
        try {
            List<String> userIdList = activityRpcService.getWillExpiredUserId();
            for (String userId : userIdList) {
               String phone = userRpcService.getUserPhoneByUserId(userId);
               if( null != phone && Regular.checkPhone(phone)){
                    boolean sendSuccess = SendMessageUtil.sendSMS(phone, "您有优惠券即将过期，别忘了使用哦~投资用券收益更高！");
                    if (sendSuccess) {
                        logger.info("给用户{}发红包过期提醒信息,发送红包过期通知成功", userId, phone);
                    }
               }
            }
        } catch (Exception e) {
            logger.error("给用户发红包过期提醒信息,发送短信失败，系统异常:{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 每天凌晨2点发短信给老板回馈统计信息
     *
     * @throws Exception
     */
    private void sendStatisMsg() {
        try {
            String phonesArray[] = phones.trim().split(",");
            String today = DateUtils.getNowDateStr2();

            //昨日投资总金额*纯富友
            double daySum = borrowTenderService.getTenderNowDaySum();
            //昨日投资人数*纯富友
            int userNum = borrowTenderService.getUserNowDayNum();
            //昨日投资订单笔数*纯富友
            int invesNum = borrowTenderService.getTenderNowDay();
            //昨日首投笔数*纯富友
            int tenderFirstNum = borrowTenderService.tenderFirstNum();
            //回款笔数
            int repayNum = borrowTenderService.getTenderRepayNowDay();
            //回款总金额
            BigDecimal repaySum = new BigDecimal(borrowTenderService.getTendeRepayrNowDaySum());
            //新增注册人数
            int newUserNum = userRpcService.getCountNewUserNum();
            //新用户投资笔数
            int tenderNumofNewUser = borrowTenderService.getTenderofNewuser();
            //存量总金额
            BigDecimal allCollect = new BigDecimal(borrowTenderService.getAllCollectSum() / 10000);
            //富友投资存量金额
            BigDecimal allCollect2 = new BigDecimal(borrowTenderService.getAllCollectFidSum() / 10000);
    //		//昨日投资笔数富友
    //		int tenderNum = borrowTenderService.getTenderNum();

//            总回款金额
            BorrowTender repayInfo = borrowTenderService.getRepayInfoOfToday();

//            回款本金

            String msg = today
                    + "投资总额:" + daySum + "元,"
                    + "投资人数" + userNum + "位;"
                    + "共有" + invesNum + "笔投资,"
                    + "其中首投" + tenderFirstNum + "笔;"
                    + "回款总金额:" + repayInfo.getAccountPast() + "元,"
                    + "其中本金:" + (repayInfo.getAccountPast()- repayInfo.getInterestPast()) + "元,"
                    + "回款笔数:" + repayInfo.getRepayTimes() + "笔,"
//                    + "富友预计回款:" + "元;"
                    + "新增注册" + newUserNum + "人,"
                    + "其中" + tenderNumofNewUser + "人完成首投;"
//                    + "存量总金额:" + allCollect.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "万元;"
                    + "富友存量:" + allCollect2.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "万元;";

            logger.info(String.format("【发送投资短信】开始发送统计短信，短信内容为：[%s]", msg));

            for (String phone : phonesArray) {
                if (!phone.equals("000")) {
                    SendMessageUtil.sendSMS(phone, msg);
                    logger.info("向{}发送投资短信,发送统计短信失败，接收手机号码为空", phone);
                } else {
                    logger.warn(String.format("【发送投资短信】发送统计短信失败，接收手机号码为空"));

                }
            }

        } catch (Exception e) {
            logger.error("【发送投资短信】发送统计短信失败，系统异常:{}", e);
            e.printStackTrace();
        }
    }


}
