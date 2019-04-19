package com.jiuy.rb.service.common;

import com.jiuy.rb.util.GeTuiVo;

/**
 * 给app发送消息的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/29 11:40
 * @Copyright 玖远网络
 */
public interface IAppMsgService {

    /**
     * 向个推发送消息
     * @param cid cid
     * @param geTuiVo msg
     * @author Aison
     * @date 2018/6/29 11:41
     * @return boolean
     */
    boolean geTui(String cid,GeTuiVo geTuiVo);

}
