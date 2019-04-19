package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *设备打开记录报表 
 **/ 
@ModelName(name="设备打开记录报表")
public class MonitoringDeviceReport extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**打开设备总数**/  
      @FieldName(name="打开设备总数")  
      private  Long openCount;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public Long getOpenCount() { 
         return openCount; 
      }

      public void setOpenCount(Long openCount) { 
         this.openCount=openCount; 
      }

 
 }