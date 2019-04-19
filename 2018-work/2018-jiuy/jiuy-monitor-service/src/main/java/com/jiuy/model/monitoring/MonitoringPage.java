package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import java.util.Date;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *每个页面的访问记录 
 **/ 
@ModelName(name="每个页面的访问记录")
public class MonitoringPage extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**设备id**/  
      @FieldName(name="设备id")  
      private  String deviceId;  
 
      /**用户手机号**/  
      @FieldName(name="用户手机号")  
      private  String phone;  
 
      /**进入页面的时间**/  
      @FieldName(name="进入页面的时间")  
      private  Date enterTime;  
 
      /**离开时间**/  
      @FieldName(name="离开时间")  
      private  Date leaveTime;  
 
      /**点击/事件次数**/  
      @FieldName(name="点击/事件次数")  
      private  Integer eventCount;  
 
      /**页面名称**/  
      @FieldName(name="页面名称")  
      private  String pageName;  
 
      /**页面编码**/  
      @FieldName(name="页面编码")  
      private  String pageCode;  
 
      /**创建时间**/  
      @FieldName(name="创建时间")  
      private  Date createTime;  
 
      /**app版本号**/  
      @FieldName(name="app版本号")  
      private  String version;  
 
      /**系统名称**/  
      @FieldName(name="系统名称")  
      private  String osName;  
 
      /**页面停留时间**/  
      @FieldName(name="页面停留时间")  
      private  Long pageStayTime;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public String getDeviceId() { 
         return deviceId; 
      }

      public void setDeviceId(String deviceId) { 
         this.deviceId=deviceId; 
      }

      public String getPhone() { 
         return phone; 
      }

      public void setPhone(String phone) { 
         this.phone=phone; 
      }

      public Date getEnterTime() { 
         return enterTime; 
      }

      public void setEnterTime(Date enterTime) { 
         this.enterTime=enterTime; 
      }

      public Date getLeaveTime() { 
         return leaveTime; 
      }

      public void setLeaveTime(Date leaveTime) { 
         this.leaveTime=leaveTime; 
      }

      public Integer getEventCount() { 
         return eventCount; 
      }

      public void setEventCount(Integer eventCount) { 
         this.eventCount=eventCount; 
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

      public Date getCreateTime() { 
         return createTime; 
      }

      public void setCreateTime(Date createTime) { 
         this.createTime=createTime; 
      }

      public String getVersion() { 
         return version; 
      }

      public void setVersion(String version) { 
         this.version=version; 
      }

      public String getOsName() { 
         return osName; 
      }

      public void setOsName(String osName) { 
         this.osName=osName; 
      }

      public Long getPageStayTime() { 
         return pageStayTime; 
      }

      public void setPageStayTime(Long pageStayTime) { 
         this.pageStayTime=pageStayTime; 
      }

 
 }