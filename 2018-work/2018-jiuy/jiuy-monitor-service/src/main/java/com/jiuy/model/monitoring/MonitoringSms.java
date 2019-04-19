package com.jiuy.model.monitoring; 
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.PrimaryKey;
import java.util.Date;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;
 
 /** 
 *短信打开url记录表 
 **/ 
@ModelName(name="短信打开url记录表")
public class MonitoringSms extends Model{  
 
      /**页面名称**/  
      @PrimaryKey  
      @FieldName(name="页面名称")  
      private  Long id;  
 
      /**页面编码**/  
      @FieldName(name="页面编码")  
      private  String pageCode;  
 
      /**页面名称**/  
      @FieldName(name="页面名称")  
      private  String pageName;  
 
      /**打开页面的时间**/  
      @FieldName(name="打开页面的时间")  
      private  Date openTime;  
 
      /**创建时间**/  
      @FieldName(name="创建时间")  
      private  Date createTime;  
 
      /**推广url**/  
      @FieldName(name="推广url")  
      private  String url;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public String getPageCode() { 
         return pageCode; 
      }

      public void setPageCode(String pageCode) { 
         this.pageCode=pageCode; 
      }

      public String getPageName() { 
         return pageName; 
      }

      public void setPageName(String pageName) { 
         this.pageName=pageName; 
      }

      public Date getOpenTime() { 
         return openTime; 
      }

      public void setOpenTime(Date openTime) { 
         this.openTime=openTime; 
      }

      public Date getCreateTime() { 
         return createTime; 
      }

      public void setCreateTime(Date createTime) { 
         this.createTime=createTime; 
      }

      public String getUrl() { 
         return url; 
      }

      public void setUrl(String url) { 
         this.url=url; 
      }

 
 }