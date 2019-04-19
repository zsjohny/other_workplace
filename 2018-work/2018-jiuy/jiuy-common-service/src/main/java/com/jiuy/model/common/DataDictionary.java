package com.jiuy.model.common;
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.annotation.PrimaryKey;
import com.jiuy.base.model.Model;

import java.util.Date;

 
 /** 
 *数据字典 
 **/ 
@ModelName(name="数据字典")
public class DataDictionary extends Model {
 
      /**主键:**/  
      @PrimaryKey
      @FieldName(name="主键:")  
      private  Long id;  
 
      /**编码唯一**/  
      @FieldName(name="编码唯一")
      private  String code;  
 
      /**分组编码**/  
      @FieldName(name="分组编码")  
      private  String groupCode;  
 
      /**值**/  
      @FieldName(name="值")  
      private  String val;  
 
      /**中文名称**/  
      @FieldName(name="中文名称")  
      private  String name;  
 
      /**描述**/  
      @FieldName(name="描述")  
      private  String comment;  
 
      /**启用状态:0 禁用 1启用**/  
      @FieldName(name="启用状态:0 禁用 1启用")  
      private  Integer status;  
 
      /**创建人:**/  
      @FieldName(name="创建人:")  
      private  String createUserId;  
 
      /**创建人姓名:**/  
      @FieldName(name="创建人姓名:")  
      private  String createUserName;  
 
      /**创建时间:**/  
      @FieldName(name="创建时间:")  
      private  Date createTime;  
 
      /**最后修改人id:**/  
      @FieldName(name="最后修改人id:")  
      private  String lastUserId;  
 
      /**最后修改人名:**/  
      @FieldName(name="最后修改人名:")  
      private  String lastUserName;  
 
      /**最后修改时间:**/  
      @FieldName(name="最后修改时间:")  
      private  Date lastUpdateTime;  
 
      private  Long parentId;  
 
      /**getters  and setters ***/
      public Long getId() { 
         return id; 
      }

      public void setId(Long id) { 
         this.id=id; 
      }

      public String getCode() { 
         return code; 
      }

      public void setCode(String code) { 
         this.code=code; 
      }

      public String getGroupCode() { 
         return groupCode; 
      }

      public void setGroupCode(String groupCode) { 
         this.groupCode=groupCode; 
      }

      public String getVal() { 
         return val; 
      }

      public void setVal(String val) { 
         this.val=val; 
      }

      public String getName() { 
         return name; 
      }

      public void setName(String name) { 
         this.name=name; 
      }

      public String getComment() { 
         return comment; 
      }

      public void setComment(String comment) { 
         this.comment=comment; 
      }

      public Integer getStatus() { 
         return status; 
      }

      public void setStatus(Integer status) { 
         this.status=status; 
      }

      public String getCreateUserId() { 
         return createUserId; 
      }

      public void setCreateUserId(String createUserId) { 
         this.createUserId=createUserId; 
      }

      public String getCreateUserName() { 
         return createUserName; 
      }

      public void setCreateUserName(String createUserName) { 
         this.createUserName=createUserName; 
      }

      public Date getCreateTime() { 
         return createTime; 
      }

      public void setCreateTime(Date createTime) { 
         this.createTime=createTime; 
      }

      public String getLastUserId() { 
         return lastUserId; 
      }

      public void setLastUserId(String lastUserId) { 
         this.lastUserId=lastUserId; 
      }

      public String getLastUserName() { 
         return lastUserName; 
      }

      public void setLastUserName(String lastUserName) { 
         this.lastUserName=lastUserName; 
      }

      public Date getLastUpdateTime() { 
         return lastUpdateTime; 
      }

      public void setLastUpdateTime(Date lastUpdateTime) { 
         this.lastUpdateTime=lastUpdateTime; 
      }

      public Long getParentId() { 
         return parentId; 
      }

      public void setParentId(Long parentId) { 
         this.parentId=parentId; 
      }

 
 }