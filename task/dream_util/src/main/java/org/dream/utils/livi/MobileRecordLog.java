package org.dream.utils.livi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 手机端日志记录
 * Created by Ness on 2016/10/17.
 */
public class MobileRecordLog {

    private Logger logger = LoggerFactory.getLogger(MobileRecordLog.class);

    public void recordLogs(Integer userId, String msg) {
        try {
            logger.info("开始发送给移动端直播老师Id{},发送消息{}", userId, msg);
        } catch (Exception e) {
            logger.warn("发送给移动端消息出错,id={},msg={}", userId, msg, e);
        }

    }


}
