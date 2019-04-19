package com.jiuy.rb.service.impl.common;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.rb.service.common.IAppMsgService;
import com.jiuy.rb.service.impl.order.OrderServiceImpl;
import com.jiuy.rb.util.GeTuiVo;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.GetuiUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * app发送消息的service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/29 11:41
 * @Copyright 玖远网络
 */
@Service("appMsgService")
public class AppMsgServiceImpl implements IAppMsgService {

    private static final Log logger = LogFactory.get(OrderServiceImpl.class);

    /**
     * 向个推发送消息
     *
     * @param cid cid
     * @param geTuiVo msg
     * @author Aison
     * @date 2018/6/29 11:41
     * @return boolean
     */
    @Override
    public boolean geTui(String cid, GeTuiVo geTuiVo) {

        try{
            String result = GetuiUtil.pushGeTui(CollectionUtil.createList(cid),geTuiVo.toJsonObject());
            String ok = "{result=ok,";
            if (StringUtils.contains(result, ok)) {
                return true;
            } else {
                logger.error("推送失败。result：" + result);
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("发送推送消息有误userCID:"+cid);
            return false;
        }
    }
}
