package com.jiuy.model.monitoring;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @version V1.0
 * @Package com.jiuy.model.monitoring
 * @Description:
 * @author: Aison
 * @date: 2018/4/19 14:14
 * @Copyright: 玖远网络
 */
@Data
public class MonitoringTotalReportQuery extends  MonitoringTotalReport {

    /**
     * @see: 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;
    /**
     * @see: 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

}
