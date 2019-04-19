package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import java.util.Date;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *与app交互的元数据 
 **/ 
@ModelName(name="与app交互的元数据")
public class MonitoringMetadata extends Model{  
 
      /**主键**/  
      @PrimaryKey  
      @FieldName(name="主键")  
      private  Long id;  
 
      /**文件数据**/  
      @FieldName(name="文件数据")  
      private  String data;  
 
      /**状态:1处理成功，0未处理，2处理失败**/  
      @FieldName(name="状态:1处理成功，0未处理，2处理失败")  
      private  Integer status;  
 
      /**处理次数**/  
      @FieldName(name="处理次数")  
      private  Integer parseCount;  
 
      /**创建时间**/  
      @FieldName(name="创建时间")  
      private  Date createTime;  
 
      private  String parseResult;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public String getData() { 
         return data; 
      }

      public void setData(String data) { 
         this.data=data; 
      }

      public Integer getStatus() { 
         return status; 
      }

      public void setStatus(Integer status) { 
         this.status=status; 
      }

      public Integer getParseCount() { 
         return parseCount; 
      }

      public void setParseCount(Integer parseCount) { 
         this.parseCount=parseCount; 
      }

      public Date getCreateTime() { 
         return createTime; 
      }

      public void setCreateTime(Date createTime) { 
         this.createTime=createTime; 
      }

      public String getParseResult() { 
         return parseResult; 
      }

      public void setParseResult(String parseResult) { 
         this.parseResult=parseResult; 
      }

 
 }