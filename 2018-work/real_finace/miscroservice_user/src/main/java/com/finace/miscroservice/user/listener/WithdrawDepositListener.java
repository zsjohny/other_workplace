package com.finace.miscroservice.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.enums.TxCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.user.dao.OpenAccountLogDao;
import com.finace.miscroservice.user.service.OpenAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class WithdrawDepositListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(WithdrawDepositListener.class);

    @Autowired
    private OpenAccountService openAccountService;
    @Autowired
    private OpenAccountLogDao openAccountLogDao;
    @Override
    protected void transferTo(String transferData) {
        logger.info(String.format("【提现回调处理】任务接收中...，transferData为[%s]", transferData));
        if (StringUtils.isEmpty(transferData)) {
            logger.warn(String.format("【提现回调处理】任务接收失败，transferData为[%s]", transferData));
            return;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(transferData);
//            System.out.println(jsonObject);
            String seqNo = jsonObject.get("seqNo").toString();
            String txCode = jsonObject.get("txCode").toString();
            String txTime = jsonObject.get("txTime").toString();
            if (txCode.equals(TxCodeEnum.WITHDRAW.getCode())){
               openAccountService.upOpenAccountLogBySeqNo(seqNo,txTime);
            }


        } catch (Exception e) {
            logger.error("【提现回调处理】发送异常，系统异常:{}", e);
            e.printStackTrace();
        }
    }




}
