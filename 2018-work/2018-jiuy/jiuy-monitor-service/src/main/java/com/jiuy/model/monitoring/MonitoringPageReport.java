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
public class MonitoringPageReport extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**页面名称**/  
      @FieldName(name="页面名称")  
      private  String pageName;  
 
      /**页面编码**/  
      @FieldName(name="页面编码")  
      private  String pageCode;  
 
      /**页面uv**/  
      @FieldName(name="页面uv")  
      private  Long pageUv;  
 
      /**页面pv**/  
      @FieldName(name="页面pv")  
      private  Long pagePv;  
 
      /**页面平均停留时间单位s**/  
      @FieldName(name="页面平均停留时间单位s")  
      private  Long pageStayEvg;  
 
      /**页面跳失率**/  
      @FieldName(name="页面跳失率")  
      private  Double pageLostPercent;  
 
      /**页面总停留时间s**/  
      @FieldName(name="页面总停留时间s")  
      private  Long pageStayTotal;  
 
      /**页面总点击次数**/  
      @FieldName(name="页面总点击次数")  
      private  Long eventCount;  
 
      /**页面uv点击率**/  
      @FieldName(name="页面uv点击率")  
      private  Double eventUvPercent;  
 
      /**页面pv点击率**/  
      @FieldName(name="页面pv点击率")  
      private  Double eventPvPercent;  
 
      /**统计时间**/  
      @FieldName(name="统计时间")  
      private  Date reportTime;  
 
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

      public String getPageName() { 
         return pageName; 
      }

      public void setPageName(String pageName) { 
         this.pageName=pageName; 
      }

      public String getPageCode() { 
         return pageCode; 
      }

      public void setPageCode(String pageCode) { 
         this.pageCode=pageCode; 
      }

      public Long getPageUv() { 
         return pageUv; 
      }

      public void setPageUv(Long pageUv) { 
         this.pageUv=pageUv; 
      }

      public Long getPagePv() { 
         return pagePv; 
      }

      public void setPagePv(Long pagePv) { 
         this.pagePv=pagePv; 
      }

      public Long getPageStayEvg() { 
         return pageStayEvg; 
      }

      public void setPageStayEvg(Long pageStayEvg) { 
         this.pageStayEvg=pageStayEvg; 
      }

      public Double getPageLostPercent() { 
         return pageLostPercent; 
      }

      public void setPageLostPercent(Double pageLostPercent) { 
         this.pageLostPercent=pageLostPercent; 
      }

      public Long getPageStayTotal() { 
         return pageStayTotal; 
      }

      public void setPageStayTotal(Long pageStayTotal) { 
         this.pageStayTotal=pageStayTotal; 
      }

      public Long getEventCount() { 
         return eventCount; 
      }

      public void setEventCount(Long eventCount) { 
         this.eventCount=eventCount; 
      }

      public Double getEventUvPercent() { 
         return eventUvPercent; 
      }

      public void setEventUvPercent(Double eventUvPercent) { 
         this.eventUvPercent=eventUvPercent; 
      }

      public Double getEventPvPercent() { 
         return eventPvPercent; 
      }

      public void setEventPvPercent(Double eventPvPercent) { 
         this.eventPvPercent=eventPvPercent; 
      }

      public Date getReportTime() { 
         return reportTime; 
      }

      public void setReportTime(Date reportTime) { 
         this.reportTime=reportTime; 
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