package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringPageReport; 
import  java.util.*;
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface MonitoringPageReportMapper extends BaseMapper<MonitoringPageReport>{  

     /***  @Costom ***/
    /**
     * 删除某一天的所有统计
     * @param date
     * @date:   2018/4/19 11:14
     * @return  int 返回删除了多少行
     * @author: Aison
     */
    int deleteByDate(Date date);

    /**
     * 某段时间内的汇总
     * @param beginDate
     * @param endDate
     * @date:   2018/4/26 11:35
     * @author: Aison
     */
    List<MonitoringPageReport> sumPageReport(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

}
