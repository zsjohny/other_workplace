package com.e_commerce.miscroservice.store.entity.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import java.math.BigDecimal;
import lombok.Data;
 @Data
 @Table("store_orderitem")
 public class StoreOrderitem {
@Id
private Long id;
private Long orderno;
private Long storeid;
private Long productid;
private Long skuid;
private BigDecimal totalmoney;
private BigDecimal totalexpressmoney;
private BigDecimal money;
private BigDecimal expressmoney;
private Integer totalunavalcoinused;
private Integer unavalcoinused;
private Integer buycount;
private String skusnapshot;
private Integer status;
private Long createtime;
private Long updatetime;
private Long brandid;
private Long lowarehouseid;
private BigDecimal totalpay;
private BigDecimal totalmarketprice;
private BigDecimal marketprice;
private BigDecimal totalavailablecommission;
private String position;
private Long supplierid;
private Integer memberPackageType;
}