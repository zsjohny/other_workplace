/**
 * 
 */
package com.store.entity;

import java.io.Serializable;

import com.jiuyuan.entity.Product;

/**
 * CREATE TABLE `store_OrderProduct` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `StoreId` bigint(20) NOT NULL COMMENT '用户id',
  `ProductId` bigint(20) NOT NULL COMMENT '对应Product表的id',
  `OrderNo` bigint(20) DEFAULT NULL COMMENT '新订单表OrderNo',
  `TotalMoney` decimal(10,2) NOT NULL COMMENT '订单金额总价，不包含邮费',
  `Money` decimal(10,2) NOT NULL COMMENT '商品单价，不包含邮费',
  `BuyCount` int(11) NOT NULL COMMENT '订购数量',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `BrandId` bigint(20) DEFAULT '0' COMMENT '关联的品牌id',
  `WarehouseId` bigint(20) NOT NULL DEFAULT '0' COMMENT '仓库id',
  `TotalMarketPrice` decimal(10,2) DEFAULT '0.00' COMMENT '市场价',
  `MarketPrice` decimal(10,2) DEFAULT '0.00',
  `Commission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提成金额',
  `TotalCommission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总提成金额',
  `Image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `BrandLogo` varchar(255) DEFAULT NULL COMMENT '品牌logo',
  PRIMARY KEY (`Id`),
  KEY `idx_userid` (`StoreId`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COMMENT='用户订单细目表';


* @author DongZhong
* @version 创建时间: 2016年12月15日 下午9:54:48
*/
public class OrderProduct implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 6147408243462399696L;

	private long id;
	private long storeId;
	private long productId;
	private double totalMoney;
	private double money;
	private int buyCount;
	private int status;
	private long createTime;
	private long updateTime;
	private long brandId;
	private long warehouseId;
	private long orderNo;
	private double totalMarketPrice;
	private double marketPrice;
	private double commission;
	private double totalCommission;
	private String  image;
//	平台商品状态:0已上架、1已下架、2已删除
	private String  platformProductState;
	private String  brandLogo;
	private Product  product;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}
	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	public double getTotalCommission() {
		return totalCommission;
	}
	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getBrandLogo() {
		return brandLogo;
	}
	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getPlatformProductState() {
		return platformProductState;
	}
	public void setPlatformProductState(String platformProductState) {
		this.platformProductState = platformProductState;
	}
	
}
