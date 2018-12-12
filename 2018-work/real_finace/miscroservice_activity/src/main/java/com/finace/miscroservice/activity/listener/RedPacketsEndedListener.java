package com.finace.miscroservice.activity.listener;

import java.text.SimpleDateFormat;

import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.utils.tools.message.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;

/**
 * 红包过期处理
 */
@Component
public class RedPacketsEndedListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(RedPacketsEndedListener.class);

    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
    private UserRpcService userRpcService;

    @Override
    protected void transferTo(String transferData) {
        logger.info("红包到期任务：任务开始处理...，transferData={}", transferData);
        if (StringUtils.isEmpty(transferData)) {
            logger.warn("红包到期任务：任务处理失败...，transferData={}", transferData);
            return;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(transferData);
            if (null == jsonObject) {
                return;
            }

            //1--红包过期  2--红包过期提醒
            String type = jsonObject.getString("type");
            if ("1".equals(type)) {
                String hbid = jsonObject.getString("hbid");
                if (null != hbid) {
                    userRedPacketsService.updateUserRedPacketsEnded(hbid);
                    logger.info("红包到期任务：任务处理完毕，红包{}被标记为已过期", hbid);
                } else {
                    logger.info("红包到期任务：任务处理失败，红包{}", hbid);
                }
            } else if ("2".equals(type)) {
                String userId = jsonObject.getString("userId");
                User user = userRpcService.getUserByUserId(userId);
                if (null != user && user.getPhone() != null) {
                    boolean sendSuccess = SendMessageUtil.sendSMS(user.getPhone(), "【一桶金】您有优惠券即将过期，别忘了使用哦~投资用券收益更高！");
                    if (sendSuccess) {
                        logger.info("向用户{}发送红包过期通知成功");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("红包到期任务：任务处理失败，系统异常:{}", e);
        }

    }


    public static void main(String arf[]) throws Exception{

        SimpleDateFormat sdf1 = new SimpleDateFormat("s m H d M ? y");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

         System.out.println("-----------"+sdf1.format(sdf.parse("2018-03-15 23:58:59")));


    }

}
