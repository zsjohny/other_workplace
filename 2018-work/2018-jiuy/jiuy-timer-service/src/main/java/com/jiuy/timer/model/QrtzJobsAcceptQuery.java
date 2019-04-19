package com.jiuy.timer.model; 

import com.jiuy.base.util.Biz;
import lombok.Data;

import java.util.Date;

/**
 * QrtzJobsAccept的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年05月31日 下午 04:10:08
 * @Copyright 玖远网络 
*/
@Data
public class QrtzJobsAcceptQuery extends QrtzJobsAccept {


    /**
     * job的类型
     **/
    private Integer jobType;

    /**
     * 通过记录生成一个日志
     * @author Aison
     * @date 2018/5/29 14:26
     */
    public QrtzOptLog logInstance() {

        QrtzOptLog qrtzOptLog = new QrtzOptLog();
        qrtzOptLog.setFeedbackParam(this.getFeedbackData());
        qrtzOptLog.setFeedbackUrl(this.getFeedbackUrl());
        qrtzOptLog.setJobComment(this.getJobComment());
        qrtzOptLog.setJobName(this.getJobName());
        qrtzOptLog.setJobGroup(this.getJobGroup());
        qrtzOptLog.setJobSnapshot(Biz.obToJson(this));
        qrtzOptLog.setOptTimeStart(new Date());
        qrtzOptLog.setOptType(this.getJobType());
        qrtzOptLog.setJobGroup(this.getJobGroup());
        qrtzOptLog.setJobName(this.getJobName());
        return qrtzOptLog;
    }

    /**
     * 填充一个日志
     * @param qrtzOptLog
     * @author Aison
     * @date 2018/5/29 14:26
     */
    public QrtzOptLog fill(QrtzOptLog qrtzOptLog) {

        qrtzOptLog.setFeedbackParam(this.getFeedbackData());
        qrtzOptLog.setFeedbackUrl(this.getFeedbackUrl());
        qrtzOptLog.setJobComment(this.getJobComment());
        qrtzOptLog.setJobName(this.getJobName());
        qrtzOptLog.setJobGroup(this.getJobGroup());
        qrtzOptLog.setJobSnapshot(Biz.obToJson(this));
        qrtzOptLog.setOptType(this.getJobType());
        qrtzOptLog.setJobGroup(this.getJobGroup());
        qrtzOptLog.setJobName(this.getJobName());
        return qrtzOptLog;
    }

    /**
     * 填充一个日志
     * @param qrtzOptLog
     * @author Aison
     * @date 2018/5/29 14:26
     */
    public static QrtzOptLog fill(QrtzJobsAccept accept ,QrtzOptLog qrtzOptLog) {

        qrtzOptLog.setFeedbackParam(accept.getFeedbackData());
        qrtzOptLog.setFeedbackUrl(accept.getFeedbackUrl());
        qrtzOptLog.setJobComment(accept.getJobComment());
        qrtzOptLog.setJobName(accept.getJobName());
        qrtzOptLog.setJobGroup(accept.getJobGroup());
        qrtzOptLog.setJobSnapshot(Biz.obToJson(accept));
        qrtzOptLog.setJobGroup(accept.getJobGroup());
        qrtzOptLog.setJobName(accept.getJobName());
        return qrtzOptLog;
    }

    /**
     * 执行状态
     **/
    private String jobStatus;
} 
