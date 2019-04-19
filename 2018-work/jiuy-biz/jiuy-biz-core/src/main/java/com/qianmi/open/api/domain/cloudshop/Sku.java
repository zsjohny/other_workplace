package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 货品信息
 *
 * @author auto
 * @since 2.0
 */
public class Sku extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * SKU级别的条形码
	 */
	@ApiField("barcode")
	private String barcode;

	/**
	 * sku成本价
	 */
	@ApiField("cost_price")
	private String costPrice;

	/**
	 * 上架时间, 格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("created")
	private String created;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * sku级别价
	 */
	@ApiField("level_price")
	private String levelPrice;

	/**
	 * sku市场价
	 */
	@ApiField("market_price")
	private String marketPrice;

	/**
	 * 下架时间, 格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("modified")
	private String modified;

	/**
	 * Item编号, p开头
	 */
	@ApiField("num_iid")
	private String numIid;

	/**
	 * sku对应的商家外部编号
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * sku售价, 单位: 元, 两位小数
	 */
	@ApiField("price")
	private String price;

	/**
	 * sku的销售属性组合字符串, 格式为:pid1:vid1;pid2:vid2
	 */
	@ApiField("properties")
	private String properties;

	/**
	 * sku对应的属性名称, 格式为:pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2
	 */
	@ApiField("properties_name")
	private String propertiesName;

	/**
	 * sku数量
	 */
	@ApiField("quantity")
	private Integer quantity;

	/**
	 * 副标题(卖点)
	 */
	@ApiField("sell_point")
	private String sellPoint;

	/**
	 * 单品销售类型 0-非代销  1-代销
	 */
	@ApiField("sku_flag")
	private Integer skuFlag;

	/**
	 * sku编号, g开头
	 */
	@ApiField("sku_id")
	private String skuId;

	/**
	 * sku图片列表
	 */
	@ApiListField("sku_imgs")
	@ApiField("sku_img")
	private List<SkuImg> skuImgs;

	/**
	 * 货源库存
	 */
	@ApiField("source_stock")
	private Integer sourceStock;

	/**
	 * sku状态: normal正常, delete删除
	 */
	@ApiField("status")
	private String status;

	/**
	 * sku重量
	 */
	@ApiField("weight")
	private String weight;

	public String getBarcode() {
		return this.barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCostPrice() {
		return this.costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getLevelPrice() {
		return this.levelPrice;
	}
	public void setLevelPrice(String levelPrice) {
		this.levelPrice = levelPrice;
	}

	public String getMarketPrice() {
		return this.marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getModified() {
		return this.modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getNumIid() {
		return this.numIid;
	}
	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public String getOuterId() {
		return this.outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getProperties() {
		return this.properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getPropertiesName() {
		return this.propertiesName;
	}
	public void setPropertiesName(String propertiesName) {
		this.propertiesName = propertiesName;
	}

	public Integer getQuantity() {
		return this.quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSellPoint() {
		return this.sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Integer getSkuFlag() {
		return this.skuFlag;
	}
	public void setSkuFlag(Integer skuFlag) {
		this.skuFlag = skuFlag;
	}

	public String getSkuId() {
		return this.skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public List<SkuImg> getSkuImgs() {
		return this.skuImgs;
	}
	public void setSkuImgs(List<SkuImg> skuImgs) {
		this.skuImgs = skuImgs;
	}

	public Integer getSourceStock() {
		return this.sourceStock;
	}
	public void setSourceStock(Integer sourceStock) {
		this.sourceStock = sourceStock;
	}

	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getWeight() {
		return this.weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}

}