package com.onway.baib.core.cache.service;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.onway.cif.common.service.publiser.UserOperatorEventSender;
import com.onway.common.enums.UserLogActionEnum;
import com.onway.common.event.UserOperatorEvent;
import com.onway.platform.common.base.QueryResult;
import com.onway.platform.common.utils.LogUtil;

public class UserActionLogComponentImpl implements UserActionLogComponent {

    private static final Logger logger = Logger.getLogger(UserActionLogComponentImpl.class);

    UserOperatorEventSender     userOperatorEventSender;

    @Override
    public void publishUserActionLog(UserLogActionEnum actionEnum, String ip, String userId,
                                     String token, boolean res) {

        String bizNo = actionEnum.name() + "_" + userId;
        UserOperatorEvent userOperatorEvent = new UserOperatorEvent(bizNo);
        userOperatorEvent.setAction(actionEnum);
        userOperatorEvent.setIp(ip);
        userOperatorEvent.setMemo(actionEnum.message() + ":" + (res ? "成功" : ("失败")));
        userOperatorEvent.setUserId(userId);
        userOperatorEvent.setToken(token);
        userOperatorEvent.setObject(null);
        QueryResult<String> sendResult = userOperatorEventSender.sendEvent(userOperatorEvent);
        if (!sendResult.isSuccess()) {
            LogUtil.error(
                logger,
                MessageFormat.format("日志记录异常,类型:{0},msg:{1}", new Object[] { actionEnum.message(),
                        sendResult.getMessage() }));
        }

    }

    public void setUserOperatorEventSender(UserOperatorEventSender userOperatorEventSender) {
        this.userOperatorEventSender = userOperatorEventSender;
    }

}
