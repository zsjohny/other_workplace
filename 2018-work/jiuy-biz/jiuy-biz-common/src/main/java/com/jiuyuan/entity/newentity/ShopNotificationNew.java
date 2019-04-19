package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-11
 */
@TableName("shop_notification")
public class ShopNotificationNew extends Model<ShopNotificationNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 个推标题
     */
	private String title;
    /**
     * 摘要
     */
	private String Abstracts;
    /**
     * 推送状态 0待推送 1已推送
     */
	private Integer PushStatus;
    /**
     * 浏览量
     */
	private Integer PageView;
    /**
     * 定时推送的时间
     */
	private Long PushTime;
    /**
     * 推广图片
     */
	private String Image;
    /**
     * 跳转类型
0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
     */
	private Integer Type;
    /**
     * 跳转内容(跳转地址)
0 ：未选择， 1：URL-网址， 2： 文章-文章id，3：商品-商品id，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流OrderNo 10：售后服务编号Id
     */
	private String LinkUrl;
    /**
     * 状态:0正常,-1删除
     */
	private Integer Status;
	private Long CreateTime;
	private Long UpdateTime;
    /**
     * storeBusiness表的ID的集合，格式:",1,2,3"
     */
	private String storeIdArrays;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstracts() {
		return Abstracts;
	}

	public void setAbstracts(String Abstracts) {
		this.Abstracts = Abstracts;
	}

	public Integer getPushStatus() {
		return PushStatus;
	}

	public void setPushStatus(Integer PushStatus) {
		this.PushStatus = PushStatus;
	}

	public Integer getPageView() {
		return PageView;
	}

	public void setPageView(Integer PageView) {
		this.PageView = PageView;
	}

	public Long getPushTime() {
		return PushTime;
	}

	public void setPushTime(Long PushTime) {
		this.PushTime = PushTime;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String Image) {
		this.Image = Image;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}

	public String getLinkUrl() {
		return LinkUrl;
	}

	public void setLinkUrl(String LinkUrl) {
		this.LinkUrl = LinkUrl;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public String getStoreIdArrays() {
		return storeIdArrays;
	}

	public void setStoreIdArrays(String storeIdArrays) {
		this.storeIdArrays = storeIdArrays;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
