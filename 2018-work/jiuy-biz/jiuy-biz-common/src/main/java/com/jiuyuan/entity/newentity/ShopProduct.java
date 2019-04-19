package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 商品信息表
 * @author QiuYuefan
 * CREATE TABLE `shop_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '玖远平台商品表商品id',
  `name` VARCHAR(200) DEFAULT NULL COMMENT '商品标题',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '零售价',
  `xprice` DECIMAL(10,2) DEFAULT NULL COMMENT '进货价',
  `market_price` DECIMAL(10,2) DEFAULT NULL COMMENT '市场价',
  `clothes_number` VARCHAR(45) DEFAULT NULL COMMENT '商品款号',
  `stock` BIGINT(20) DEFAULT NULL COMMENT '库存',
  `category_id` BIGINT(20) DEFAULT NULL COMMENT '所属分类id',
  `tag_ids` VARCHAR(1024) DEFAULT NULL COMMENT '1,2,3',
  `size_ids` VARCHAR(1024) DEFAULT NULL COMMENT '4,5,6',
  `color_ids` VARCHAR(1024) DEFAULT NULL COMMENT '7,8,9',
  `summary_images` VARCHAR(1024) DEFAULT '[]' COMMENT '主图最多4张 json数组',
  `remark` TEXT COMMENT '商品描述 json数组',
  `top_time` BIGINT(20) DEFAULT '0' COMMENT '置顶时间（推荐）',
  `stock_time` BIGINT(20) DEFAULT '0' COMMENT '现货时间',
  `tab_type` TINYINT(4) DEFAULT '0' COMMENT '0：店主精选，1：买手推荐',
  `sold_out` TINYINT(4) DEFAULT '0' COMMENT '0：草稿，1：上架， 2：下架',
  `status` TINYINT(4) DEFAULT '0' COMMENT '状态:-1：删除，0：正常',
  `create_time` BIGINT(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` BIGINT(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

 * <p>
 * 商品信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-10
 */
@TableName("shop_product")
public class ShopProduct extends Model<ShopProduct> {

	public static int  sold_out_draft  = 0 ;//草稿
	public static int  sold_out_up  = 1 ;//上架
	public static int  sold_out_down  = 2 ;//下架
	
	public static int del_status = -1;//删除
	
	public static int normal_status = 0;//正常
	
	public static int  tab_type_shop_sift  = 0 ;//购买过
	public static int  tab_type_buyer_recommend  = 1 ;//未购买过
	
	public static int  platform_product  = 0 ;//平台商品
	public static int  own_product  = 1 ;//自有商品
	
//	public static int max_product_recommend_count = 10;//最大商品推荐位数量
	
    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 玖远平台商品表商品id
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 商品标题
     */
	private String name;
    /**
     * 零售价
     */
	private Double price;
    /**
     * 进货价
     */
	private Double xprice;
    /**
     * 市场价
     */
	@TableField("market_price")
	private Double marketPrice;
    /**
     * 商品款号
     */
	@TableField("clothes_number")
	private String clothesNumber;
    /**
     * 库存
     */
	private Long stock;
    /**
     * 所属分类id
     */
	@TableField("category_id")
	private Long categoryId;
    /**
     * 1,2,3
     */
	@TableField("tag_ids")
	private String tagIds;
    /**
     * 4,5,6
     */
	@TableField("size_ids")
	private String sizeIds;
    /**
     * 7,8,9
     */
	@TableField("color_ids")
	private String colorIds;
    /**
     * 主图最多4张 json数组
     */
	@TableField("summary_images")
	private String summaryImages;
    /**
     * 商品描述 json数组
     */
	private String remark;
    /**
     * 置顶时间（推荐）
     */
	@TableField("top_time")
	private Long topTime;
    /**
     * 现货时间
     */
	@TableField("stock_time")
	private Long stockTime;
    /**
     * 是否下单：0：店主精选，1：买手推荐
     */
	@TableField("tab_type")
	private Integer tabType;
    /**
     * 0：草稿，1：上架， 2：下架
     */
	@TableField("sold_out")
	private Integer soldOut;
    /**
     * 状态:-1：删除，0：正常
     */
	private Integer status;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 商家自定义描述
     */
	@TableField("shop_own_detail")
	private String shopOwnDetail;
    /**
     * 商品视频url
     */
	@TableField("video_url")
	private String videoUrl;
    /**
     * 想要会员数量
     */
	@TableField("want_member_count")
	private Long wantMemberCount;
    /**
     * 浏览数量
     */
	@TableField("show_count")
	private Long showCount;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 上架时间0:下架
     */
	@TableField("ground_time")
	private Long groundTime;
    /**
     * 商品所属：0平台供应商商品, 1是用户自定义款，2用户自营平台同款
     */
	private Integer own;
	/**
     * 平台商品状态:0已上架、1已下架、2已删除
     */
	@TableField(exist = false)
	private String platformProductState;
	/**
     * 商品橱窗视频显示时候使用的图片
     */
	@TableField("video_display_image")
	private String videoDisplayImage;
	/**
     * 商品橱窗视频fileId
     */
	@TableField("video_display_fileId")
	private Long videoDisplayFileId;
	/**
     * 商品橱窗视频url
     */
	@TableField("video_display_url")
	private String videoDisplayUrl;

	/**
     * 商品小程序二维码路径
     */
	@TableField("wxaqrcode_url")
	private String wxaqrcodeUrl;
	
	/**
     * 商品分享长图
     */
	@TableField("wxa_product_share_image")
	private String wxaProductShareImage;

	/**
     * 商品分享长图使用的橱窗图
     */
	@TableField("wxa_product_share_old_images")
	private String wxaProductShareOldImages;

	/**
	 * 是否下单：0：已下单，1：未下单(0：店主精选	1：买手推荐)
	 */
	@TableField("color_id")
	private Long colorId;
	/**
	 * 是否下单：0：已下单，1：未下单(0：店主精选	1：买手推荐)
	 */
	@TableField("color_name")
	private String colorName;
	/**
	 * 是否下单：0：已下单，1：未下单(0：店主精选	1：买手推荐)
	 */
	@TableField("size_id")
	private Long sizeId;
	/**
	 * 是否下单：0：已下单，1：未下单(0：店主精选	1：买手推荐)
	 */
	@TableField("size_name")
	private String sizeName;

	/**
	 * 商品分享合成图code,code计算一致,则图片信息不更新
	 */
	@TableField( "share_img_code" )
	private Integer shareImgCode;


	/**
	 * 店中店分享图片
	 */
	@TableField("product_new_img")
	private String productNewImg;


	/**
	 * 店中店分享图片
	 */
	@TableField("in_shop_share_img")
	private String inShopShareImg;

	/**
	 * 首次上架时间(当商品首次上架后,商品类型不再允许编辑)
	 */
	@TableField("first_time_on_sale")
	private Long firstTimeOnSale;

	public Integer getShareImgCode() {
		return shareImgCode;
	}

	public void setShareImgCode(Integer shareImgCode) {
		this.shareImgCode = shareImgCode;
	}

	public String getInShopShareImg() {
		return inShopShareImg;
	}

	public void setInShopShareImg(String inShopShareImg) {
		this.inShopShareImg = inShopShareImg;
	}

	public Long getColorId() {
		return colorId;
	}

	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Long getSizeId() {
		return sizeId;
	}

	public void setSizeId(Long sizeId) {
		this.sizeId = sizeId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getWxaqrcodeUrl() {
		return wxaqrcodeUrl;
	}

	public void setWxaqrcodeUrl(String wxaqrcodeUrl) {
		this.wxaqrcodeUrl = wxaqrcodeUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFirstTimeOnSale() {
		return firstTimeOnSale;
	}

	public void setFirstTimeOnSale(Long firstTimeOnSale) {
		this.firstTimeOnSale = firstTimeOnSale;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getXprice() {
		return xprice;
	}

	public void setXprice(Double xprice) {
		this.xprice = xprice;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getSizeIds() {
		return sizeIds;
	}

	public void setSizeIds(String sizeIds) {
		this.sizeIds = sizeIds;
	}

	public String getColorIds() {
		return colorIds;
	}

	public void setColorIds(String colorIds) {
		this.colorIds = colorIds;
	}

	public String getSummaryImages() {
		return summaryImages;
	}

	public void setSummaryImages(String summaryImages) {
		this.summaryImages = summaryImages;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTopTime() {
		return topTime;
	}

	public void setTopTime(Long topTime) {
		this.topTime = topTime;
	}

	public Long getStockTime() {
		return stockTime;
	}

	public void setStockTime(Long stockTime) {
		this.stockTime = stockTime;
	}

	public Integer getTabType() {
		return tabType;
	}

	public void setTabType(Integer tabType) {
		this.tabType = tabType;
	}

	public Integer getSoldOut() {
		return soldOut;
	}

	public void setSoldOut(Integer soldOut) {
		this.soldOut = soldOut;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getShopOwnDetail() {
		return shopOwnDetail;
	}

	public void setShopOwnDetail(String shopOwnDetail) {
		this.shopOwnDetail = shopOwnDetail;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Long getWantMemberCount() {
		return wantMemberCount;
	}

	public void setWantMemberCount(Long wantMemberCount) {
		this.wantMemberCount = wantMemberCount;
	}

	public Long getShowCount() {
		return showCount;
	}

	public void setShowCount(Long showCount) {
		this.showCount = showCount;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getGroundTime() {
		return groundTime;
	}

	public void setGroundTime(Long groundTime) {
		this.groundTime = groundTime;
	}

	public Integer getOwn() {
		return own;
	}

	public void setOwn(Integer own) {
		this.own = own;
	}
	
	public String getPlatformProductState() {
		return platformProductState;
	}

	public void setPlatformProductState(String platformProductState) {
		this.platformProductState = platformProductState;
	}
	public String getVideoDisplayImage() {
		return videoDisplayImage;
	}

	public void setVideoDisplayImage(String videoDisplayImage) {
		this.videoDisplayImage = videoDisplayImage;
	}

	public Long getVideoDisplayFileId() {
		return videoDisplayFileId;
	}

	public void setVideoDisplayFileId(Long videoDisplayFileId) {
		this.videoDisplayFileId = videoDisplayFileId;
	}

	public String getVideoDisplayUrl() {
		return videoDisplayUrl;
	}

	public void setVideoDisplayUrl(String videoDisplayUrl) {
		this.videoDisplayUrl = videoDisplayUrl;
	}

	public String getWxaProductShareImage() {
		return wxaProductShareImage;
	}

	public void setWxaProductShareImage(String wxaProductShareImage) {
		this.wxaProductShareImage = wxaProductShareImage;
	}

	public String getWxaProductShareOldImages() {
		return wxaProductShareOldImages;
	}

	public void setWxaProductShareOldImages(String wxaProductShareOldImages) {
		this.wxaProductShareOldImages = wxaProductShareOldImages;
	}

	public String getProductNewImg() {
		return productNewImg;
	}

	public void setProductNewImg(String productNewImg) {
		this.productNewImg = productNewImg;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getFirstImage(){
		String[] detailImageArray = getDetailImageArray();
		if (detailImageArray != null && detailImageArray.length > 0) {
			return detailImageArray[0];
		}
		return "";
	}
	
	public String[] getDetailImageArray() {
    	try {
            JSONArray array = JSON.parseArray(this.getSummaryImages());
            if (array == null) {
                return new String[]{};
            }
            return array.toArray(new String[]{});
		} catch (Exception e) {
			return null;
		}
    }
	
}
