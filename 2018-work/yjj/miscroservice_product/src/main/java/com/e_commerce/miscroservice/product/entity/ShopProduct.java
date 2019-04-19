package com.e_commerce.miscroservice.product.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品信息表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月06日 下午 01:55:46
 */
@Data
@Table("shop_product")
public class ShopProduct{

	@Id
	private Long id;

	/**
	 * 商家ID
	 */
	private Long storeId;

	/**
	 * 玖远平台商品表商品id
	 */
	private Long productId;

	/**
	 * 商品标题
	 */
	private String name;

	/**
	 * 零售价
	 */
	private BigDecimal price;

	/**
	 * 进货价
	 */
	private BigDecimal xprice;

	/**
	 * 市场价
	 */
	private BigDecimal marketPrice;

	/**
	 * 商品款号
	 */
	private String clothesNumber;

	/**
	 * 库存
	 */
	private Long stock;

	/**
	 * 所属分类id
	 */
	private Long categoryId;

	/**
	 * 1,2,3
	 */
	private String tagIds;

	/**
	 * 4,5,6
	 */
	private String sizeIds;

	/**
	 * 7,8,9
	 */
	private String colorIds;

	/**
	 * 主图最多4张 json数组
	 */
	private String summaryImages;

	/**
	 * 商品描述 json数组
	 */
	private String remark;

	/**
	 * 置顶时间（推荐）
	 */
	private Long topTime;

	/**
	 * 现货时间
	 */
	private Long stockTime;

	/**
	 * 0：店主精选，1：买手推荐
	 */
	private Integer tabType;

	/**
	 * 0：草稿，1：上架， 2：下架
	 */
	private Integer soldOut;

	/**
	 * 状态:-1：删除，0：正常
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 更新时间
	 */
	private Long updateTime;

	/**
	 * 商品视频url
	 */
	private String videoUrl;

	/**
	 * 上架时间 0:下架
	 */
	private Long groundTime;

	/**
	 * 商家自定义描述(原来2000)
	 */
	private String shopOwnDetail;

	/**
	 * 想要会员数量
	 */
	private Long wantMemberCount;

	/**
	 * 浏览数量
	 */
	private Long showCount;

	/**
	 * 是否是自有商品：0平台供应商商品, 1是用户自定义款，2用户自营平台同款
	 */
	private Integer own;

	/**
	 * 商品橱窗视频显示时候使用的图片
	 */
	private String videoDisplayImage;

	/**
	 * 商品橱窗视频url
	 */
	private String videoDisplayUrl;

	/**
	 * 商品橱窗视频fileId
	 */
	private Long videoDisplayFileid;

	/**
	 * 商品小程序二维码路径
	 */
	private String wxaqrcodeUrl;

	/**
	 * 商品分享长图
	 */
	private String wxaProductShareImage;

	/**
	 * 商品分享长图使用的橱窗图
	 */
	private String wxaProductShareOldImages;

	/**
	 * 颜色id
	 */
	private Long colorId;

	/**
	 * 尺码id
	 */
	private Long sizeId;

	/**
	 * 颜色
	 */
	private String colorName;

	/**
	 * 尺码
	 */
	private String sizeName;

	/**
	 * 商品分享合成图code,code计算一致,则图片信息不更新
	 */
	private Integer shareImgCode;

	/**
	 * 首次上架时间(当商品首次上架后,商品类型不再允许编辑)
	 */
	private Long firstTimeOnSale;

	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 


	@Column(value = "in_shop_share_img", length = 500, commit = "店中店分享合成图")
	private String inShopShareImg;

 }