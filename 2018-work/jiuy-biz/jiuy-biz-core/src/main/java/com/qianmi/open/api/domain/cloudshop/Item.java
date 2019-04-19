package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 商品信息
 *
 * @author auto
 * @since 2.0
 */
public class Item extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * onsale 在售，instock在库中
	 */
	@ApiField("approve_status")
	private String approveStatus;

	/**
	 * 品牌编号
	 */
	@ApiField("brand_id")
	private String brandId;

	/**
	 * 品牌名称
	 */
	@ApiField("brand_name")
	private String brandName;

	/**
	 * 购买模板编号
	 */
	@ApiField("buy_template_id")
	private String buyTemplateId;

	/**
	 * 千米网标准类目ID
	 */
	@ApiField("cid")
	private String cid;

	/**
	 * 成本价,单位元，保留2位小数
	 */
	@ApiField("cost_price")
	private String costPrice;

	/**
	 * 商品发布时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("created")
	private String created;

	/**
	 * d2c商品上下架状态，onsale 在售，instock在库中
	 */
	@ApiField("d2c_approve_status")
	private String d2cApproveStatus;

	/**
	 * d2p商品上下架状态，onsale 在售，instock在库中
	 */
	@ApiField("d2p_approve_status")
	private String d2pApproveStatus;

	/**
	 * 下架时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("delist_time")
	private String delistTime;

	/**
	 * 运费模板编号
	 */
	@ApiField("delivery_template_id")
	private String deliveryTemplateId;

	/**
	 * 商品描述
	 */
	@ApiField("desc")
	private String desc;

	/**
	 * 扩展属性属性值，格式为：epid1:evid1:epid_name1:evid_name1;epid2:evid2:epid_name2:evid_name2
	 */
	@ApiField("ext_props")
	private String extProps;

	/**
	 * 标准商品编号
	 */
	@ApiField("from_num_iid")
	private String fromNumIid;

	/**
	 * 商品数据来源：1来自标准商品 2用户自建
	 */
	@ApiField("from_source")
	private String fromSource;

	/**
	 * 是否为自定义商品，0否 1是
	 */
	@ApiField("is_custom")
	private String isCustom;

	/**
	 * 是否分销商品:非分销0 分销1
	 */
	@ApiField("is_fenxiao")
	private String isFenxiao;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 是否虚拟商品, 0实物 1虚拟
	 */
	@ApiField("is_virtual")
	private String isVirtual;

	/**
	 * 商品图片列表
	 */
	@ApiListField("item_imgs")
	@ApiField("item_img")
	private List<ItemImg> itemImgs;

	/**
	 * 商品的体积, 单位: 立方米
	 */
	@ApiField("item_size")
	private String itemSize;

	/**
	 * 商品的重量, 单位: 克
	 */
	@ApiField("item_weight")
	private String itemWeight;

	/**
	 * 关键词
	 */
	@ApiField("keywords")
	private String keywords;

	/**
	 * 级别价 单位元，保留2位小数
	 */
	@ApiField("level_price")
	private String levelPrice;

	/**
	 * 上架时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("list_time")
	private String listTime;

	/**
	 * 市场价，单位元，保留2位小数
	 */
	@ApiField("mkt_price")
	private String mktPrice;

	/**
	 * 商品修改时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("modified")
	private String modified;

	/**
	 * 商品数量
	 */
	@ApiField("num")
	private Integer num;

	/**
	 * 商品编号
	 */
	@ApiField("num_iid")
	private String numIid;

	/**
	 * 商家的外部编码
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 属性参数，格式为：字符串或者groupName1:name1:value1;groupName2:name2:value2;groupName2:name3:value3
	 */
	@ApiField("param_props")
	private String paramProps;

	/**
	 * 商品主图地址
	 */
	@ApiField("pic_url")
	private String picUrl;

	/**
	 * 商品价格 单位元 两位小数
	 */
	@ApiField("price")
	private String price;

	/**
	 * 属性名称，格式为：pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2
	 */
	@ApiField("props_name")
	private String propsName;

	/**
	 * 千米网展示目录列表
	 */
	@ApiField("seller_cids")
	private String sellerCids;

	/**
	 * 商品所属站点分类，0：云订货和云商城，1：云订货(D2P)，2：云商城(D2C),3：未关联任何销售渠道
	 */
	@ApiField("site")
	private Integer site;

	/**
	 * sku列表
	 */
	@ApiListField("skus")
	@ApiField("sku")
	private List<Sku> skus;

	/**
	 * 是否拍下减库存: 0拍下减库存 1付款减库存
	 */
	@ApiField("sub_stock")
	private String subStock;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

	/**
	 * 商品类型编号
	 */
	@ApiField("type_id")
	private String typeId;

	/**
	 * 商品类型名称
	 */
	@ApiField("type_name")
	private String typeName;

	/**
	 * 商品计量单位
	 */
	@ApiField("unit")
	private String unit;

	/**
	 * 卖家编号,对应千米网帐号
	 */
	@ApiField("user_nick")
	private String userNick;

	public String getApproveStatus() {
		return this.approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getBrandId() {
		return this.brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return this.brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBuyTemplateId() {
		return this.buyTemplateId;
	}
	public void setBuyTemplateId(String buyTemplateId) {
		this.buyTemplateId = buyTemplateId;
	}

	public String getCid() {
		return this.cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
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

	public String getD2cApproveStatus() {
		return this.d2cApproveStatus;
	}
	public void setD2cApproveStatus(String d2cApproveStatus) {
		this.d2cApproveStatus = d2cApproveStatus;
	}

	public String getD2pApproveStatus() {
		return this.d2pApproveStatus;
	}
	public void setD2pApproveStatus(String d2pApproveStatus) {
		this.d2pApproveStatus = d2pApproveStatus;
	}

	public String getDelistTime() {
		return this.delistTime;
	}
	public void setDelistTime(String delistTime) {
		this.delistTime = delistTime;
	}

	public String getDeliveryTemplateId() {
		return this.deliveryTemplateId;
	}
	public void setDeliveryTemplateId(String deliveryTemplateId) {
		this.deliveryTemplateId = deliveryTemplateId;
	}

	public String getDesc() {
		return this.desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExtProps() {
		return this.extProps;
	}
	public void setExtProps(String extProps) {
		this.extProps = extProps;
	}

	public String getFromNumIid() {
		return this.fromNumIid;
	}
	public void setFromNumIid(String fromNumIid) {
		this.fromNumIid = fromNumIid;
	}

	public String getFromSource() {
		return this.fromSource;
	}
	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}

	public String getIsCustom() {
		return this.isCustom;
	}
	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
	}

	public String getIsFenxiao() {
		return this.isFenxiao;
	}
	public void setIsFenxiao(String isFenxiao) {
		this.isFenxiao = isFenxiao;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getIsVirtual() {
		return this.isVirtual;
	}
	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}

	public List<ItemImg> getItemImgs() {
		return this.itemImgs;
	}
	public void setItemImgs(List<ItemImg> itemImgs) {
		this.itemImgs = itemImgs;
	}

	public String getItemSize() {
		return this.itemSize;
	}
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	public String getItemWeight() {
		return this.itemWeight;
	}
	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}

	public String getKeywords() {
		return this.keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLevelPrice() {
		return this.levelPrice;
	}
	public void setLevelPrice(String levelPrice) {
		this.levelPrice = levelPrice;
	}

	public String getListTime() {
		return this.listTime;
	}
	public void setListTime(String listTime) {
		this.listTime = listTime;
	}

	public String getMktPrice() {
		return this.mktPrice;
	}
	public void setMktPrice(String mktPrice) {
		this.mktPrice = mktPrice;
	}

	public String getModified() {
		return this.modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public Integer getNum() {
		return this.num;
	}
	public void setNum(Integer num) {
		this.num = num;
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

	public String getParamProps() {
		return this.paramProps;
	}
	public void setParamProps(String paramProps) {
		this.paramProps = paramProps;
	}

	public String getPicUrl() {
		return this.picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getPropsName() {
		return this.propsName;
	}
	public void setPropsName(String propsName) {
		this.propsName = propsName;
	}

	public String getSellerCids() {
		return this.sellerCids;
	}
	public void setSellerCids(String sellerCids) {
		this.sellerCids = sellerCids;
	}

	public Integer getSite() {
		return this.site;
	}
	public void setSite(Integer site) {
		this.site = site;
	}

	public List<Sku> getSkus() {
		return this.skus;
	}
	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public String getSubStock() {
		return this.subStock;
	}
	public void setSubStock(String subStock) {
		this.subStock = subStock;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTypeId() {
		return this.typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return this.typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUserNick() {
		return this.userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

}