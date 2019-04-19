package com.jiuy.model.brand; 

import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.model.Model;

import java.math.BigDecimal;
import java.math.BigInteger;

 /** 
 *品牌表 
 **/ 
@ModelName(name="品牌表")
public class Brand extends Model {
 
      /**品牌表主键**/  
      @FieldName(name="品牌表主键")
      private  Integer Id;  
 
      private  Long BrandId;
 
      /**品牌名称**/  
      @FieldName(name="品牌名称")  
      private  String BrandName;  
 
      /**品牌logo**/  
      @FieldName(name="品牌logo")  
      private  String Logo;  
 
      /**状态:1:禁用,0:正常,-1:删除**/  
      @FieldName(name="状态:1:禁用,0:正常,-1:删除")  
      private  Integer status;  
 
      /**创建时间**/  
      @FieldName(name="创建时间")  
      private  Long CreateTime;  
 
      /**更新时间**/  
      @FieldName(name="更新时间")  
      private  Long UpdateTime;  
 
      private  String CnName;  
 
      private  String Description;  
 
      /**权重-排序**/  
      @FieldName(name="权重-排序")  
      private  Integer Weight;  
 
      /**品牌标识**/  
      @FieldName(name="品牌标识")  
      private  String BrandIdentity;  
 
      /**是否优惠 0:否 1：是**/  
      @FieldName(name="是否优惠 0:否 1：是")  
      private  Integer IsDiscount;  
 
      /**满额立减条件**/  
      @FieldName(name="满额立减条件")  
      private  BigDecimal ExceedMoney;  
 
      /**满额立减数**/  
      @FieldName(name="满额立减数")  
      private  BigDecimal MinusMoney;  
 
      /**品牌商品款号前缀**/  
      @FieldName(name="品牌商品款号前缀")  
      private  String clothNumberPrefix;  
 
      /**品牌类型：0：未设置 1：高档，2：中档**/  
      @FieldName(name="品牌类型：0：未设置 1：高档，2：中档")  
      private  Integer brandType;  
 
      /**品牌推广图**/  
      @FieldName(name="品牌推广图")  
      private  String brandPromotionImg;  
 
      /**品牌档次类型：0未知、1高档、2低档**/  
      @FieldName(name="品牌档次类型：0未知、1高档、2低档")  
      private  Integer gradeType;  
 
      /**getters  and setters ***/
      public Integer getId() { 
         return Id; 
      }

      public void setId(Integer Id) { 
         this.Id=Id; 
      }



      public String getBrandName() { 
         return BrandName; 
      }

      public void setBrandName(String BrandName) { 
         this.BrandName=BrandName; 
      }

      public String getLogo() { 
         return Logo; 
      }

      public void setLogo(String Logo) { 
         this.Logo=Logo; 
      }

      public Integer getStatus() { 
         return status; 
      }

      public void setStatus(Integer status) { 
         this.status=status; 
      }

      public Long getCreateTime() { 
         return CreateTime; 
      }

      public void setCreateTime(Long CreateTime) { 
         this.CreateTime=CreateTime; 
      }

      public Long getUpdateTime() { 
         return UpdateTime; 
      }

      public void setUpdateTime(Long UpdateTime) { 
         this.UpdateTime=UpdateTime; 
      }

      public String getCnName() { 
         return CnName; 
      }

      public void setCnName(String CnName) { 
         this.CnName=CnName; 
      }

      public String getDescription() { 
         return Description; 
      }

      public void setDescription(String Description) { 
         this.Description=Description; 
      }

      public Integer getWeight() { 
         return Weight; 
      }

      public void setWeight(Integer Weight) { 
         this.Weight=Weight; 
      }

      public String getBrandIdentity() { 
         return BrandIdentity; 
      }

      public void setBrandIdentity(String BrandIdentity) { 
         this.BrandIdentity=BrandIdentity; 
      }

      public Integer getIsDiscount() { 
         return IsDiscount; 
      }

      public void setIsDiscount(Integer IsDiscount) { 
         this.IsDiscount=IsDiscount; 
      }

      public BigDecimal getExceedMoney() { 
         return ExceedMoney; 
      }

      public void setExceedMoney(BigDecimal ExceedMoney) { 
         this.ExceedMoney=ExceedMoney; 
      }

      public BigDecimal getMinusMoney() { 
         return MinusMoney; 
      }

      public void setMinusMoney(BigDecimal MinusMoney) { 
         this.MinusMoney=MinusMoney; 
      }

      public String getClothNumberPrefix() { 
         return clothNumberPrefix; 
      }

      public void setClothNumberPrefix(String clothNumberPrefix) { 
         this.clothNumberPrefix=clothNumberPrefix; 
      }

      public Integer getBrandType() { 
         return brandType; 
      }

      public void setBrandType(Integer brandType) { 
         this.brandType=brandType; 
      }

      public String getBrandPromotionImg() { 
         return brandPromotionImg; 
      }

      public void setBrandPromotionImg(String brandPromotionImg) { 
         this.brandPromotionImg=brandPromotionImg; 
      }

      public Integer getGradeType() { 
         return gradeType; 
      }

      public void setGradeType(Integer gradeType) { 
         this.gradeType=gradeType; 
      }

     public Long getBrandId() {
         return BrandId;
     }

     public void setBrandId(Long brandId) {
         BrandId = brandId;
     }
 }