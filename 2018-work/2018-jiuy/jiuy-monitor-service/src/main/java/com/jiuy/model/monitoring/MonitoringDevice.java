package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import java.util.Date;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *设备打开记录 
 **/ 
@ModelName(name="设备打开记录")
public class MonitoringDevice extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**设备id**/  
      @FieldName(name="设备id")  
      private  String deviceId;  
 
      /**打开设备时间**/  
      @FieldName(name="打开设备时间")  
      private  Date openTime;  
 
      /**关闭设备时间**/  
      @FieldName(name="关闭设备时间")  
      private  Date closeTime;  
 
      /**手机号码**/  
      @FieldName(name="手机号码")  
      private  String phone;  
 
      /**创建时间**/  
      @FieldName(name="创建时间")  
      private  Date createTime;  
 
      /**app版本号**/  
      @FieldName(name="app版本号")  
      private  String version;  
 
      /**系统名称**/  
      @FieldName(name="系统名称")  
      private  String osName;  
 
      /**app停留时间**/  
      @FieldName(name="app停留时间")  
      private  Long appStayTime;  
 
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

      public Date getOpenTime() { 
         return openTime; 
      }

      public void setOpenTime(Date openTime) { 
         this.openTime=openTime; 
      }

      public Date getCloseTime() { 
         return closeTime; 
      }

      public void setCloseTime(Date closeTime) { 
         this.closeTime=closeTime; 
      }

      public String getPhone() { 
         return phone; 
      }

      public void setPhone(String phone) { 
         this.phone=phone; 
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

      public Long getAppStayTime() { 
         return appStayTime; 
      }

      public void setAppStayTime(Long appStayTime) { 
         this.appStayTime=appStayTime; 
      }

 
 }