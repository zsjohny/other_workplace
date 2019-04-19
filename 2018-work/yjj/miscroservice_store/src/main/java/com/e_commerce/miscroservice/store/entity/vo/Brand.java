package com.e_commerce.miscroservice.store.entity.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;
 @Data
 @Table("yjj_brand")
 public class Brand {
@Id
private Integer id;
private String brandname;
private String logo;
private Integer status;
private Long createtime;
private Long updatetime;
private String cnname;
private Integer weight;
private String brandidentity;
private Integer isdiscount;
private String clothNumberPrefix;
private Integer brandType;
private String brandPromotionImg;
}