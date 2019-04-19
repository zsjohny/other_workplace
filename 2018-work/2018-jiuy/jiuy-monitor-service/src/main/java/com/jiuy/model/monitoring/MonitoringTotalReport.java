package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import java.util.Date;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *app概况 
 **/ 
@ModelName(name="app概况")
public class MonitoringTotalReport extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**打开app的总数**/  
      @FieldName(name="打开app的总数")  
      private  Long openAppCount;  
 
      /**打开app的设备的总数**/  
      @FieldName(name="打开app的设备的总数")  
      private  Long openAppDeviceCount;  
 
      /**页面访问总数**/  
      @FieldName(name="页面访问总数")  
      private  Long pageQueryCount;  
 
      /**app平均停留时间**/  
      @FieldName(name="app平均停留时间")  
      private  Long appStayAvg;  
 
      /**app平均访问页面数**/  
      @FieldName(name="app平均访问页面数")  
      private  Long pageQueryAvg;  
 
      /**统计时间**/  
      @FieldName(name="统计时间")  
      private  Date reportTime;  
 
      /**app总停留时间**/  
      @FieldName(name="app总停留时间")  
      private  Long appStayTimeCount;  
 
      /**最后统计时间**/  
      @FieldName(name="最后统计时间")  
      private  Date lastReportTime;  
 
      /**是那天的数据**/  
      @FieldName(name="是那天的数据")  
      private  Date reportDay;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public Long getOpenAppCount() { 
         return openAppCount; 
      }

      public void setOpenAppCount(Long openAppCount) { 
         this.openAppCount=openAppCount; 
      }

      public Long getOpenAppDeviceCount() { 
         return openAppDeviceCount; 
      }

      public void setOpenAppDeviceCount(Long openAppDeviceCount) { 
         this.openAppDeviceCount=openAppDeviceCount; 
      }

      public Long getPageQueryCount() { 
         return pageQueryCount; 
      }

      public void setPageQueryCount(Long pageQueryCount) { 
         this.pageQueryCount=pageQueryCount; 
      }

      public Long getAppStayAvg() { 
         return appStayAvg; 
      }

      public void setAppStayAvg(Long appStayAvg) { 
         this.appStayAvg=appStayAvg; 
      }

      public Long getPageQueryAvg() { 
         return pageQueryAvg; 
      }

      public void setPageQueryAvg(Long pageQueryAvg) { 
         this.pageQueryAvg=pageQueryAvg; 
      }

      public Date getReportTime() { 
         return reportTime; 
      }

      public void setReportTime(Date reportTime) { 
         this.reportTime=reportTime; 
      }

      public Long getAppStayTimeCount() { 
         return appStayTimeCount; 
      }

      public void setAppStayTimeCount(Long appStayTimeCount) { 
         this.appStayTimeCount=appStayTimeCount; 
      }

      public Date getLastReportTime() { 
         return lastReportTime; 
      }

      public void setLastReportTime(Date lastReportTime) { 
         this.lastReportTime=lastReportTime; 
      }

      public Date getReportDay() { 
         return reportDay; 
      }

      public void setReportDay(Date reportDay) { 
         this.reportDay=reportDay; 
      }

 
 }