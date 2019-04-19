package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringSms; 
import  java.util.*;
import com.jiuy.base.mapper.BaseMapper;

public interface MonitoringSmsMapper extends BaseMapper<MonitoringSms>{  

     /***  @Costom ***/
    /**
     * 查询打开记录 sql报表
     * @param param
     * @date:   2018/4/24 16:13
     * @author: Aison
     */
    List<Map<String,Object>> selectSmsReport(Map<String,Object> param);
}