package org.dream.utils.livi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 手机端日志记录
 * Created by Ness on 2016/10/17.
 */
public class WebRecordLog {

    private Logger logger = LoggerFactory.getLogger(WebRecordLog.class);

    public void recordLogs(Integer userId, String msg) {
        try {
            logger.info("开始发送给web端直播老师Id{},发送消息{}", userId, msg);
        } catch (Exception e) {
            logger.warn("发送给web端消息出错,id={},msg={}", userId, msg, e);
        }

    }


}
