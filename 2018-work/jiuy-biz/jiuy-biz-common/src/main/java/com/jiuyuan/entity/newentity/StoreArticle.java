package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 门店文章表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-02
 */
@TableName("store_article")
public class StoreArticle extends Model<StoreArticle> {

    private static final long serialVersionUID = 1L;
    public static final int article_status_delete = 0;//删除
    public static final int article_status_normal = 1;//正常
    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家id
     */
	@TableField("store_id")
	private Long storeId;
	/**
	 * 文章标题
	 */
	@TableField("article_title") 
	private String articleTitle;
	/**
	 * 文章头图
	 */
	@TableField("head_image") 
	private String headImage;
    /**
     * 文章内容
     */
	@TableField("article_context")
	private String articleContext;
    /**
     * 状态 0：删除，1：正常
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Long updateTime;
	/**
     * 公开状态 1:公开,2私密
     */
	@TableField("public_state")
	private Integer publicState;
    /**
     * 置顶 0:不置顶,1:置顶
     */
	@TableField("top")
	private Integer top;

	public Integer getPublicState() {
		return publicState;
	}

	public void setPublicState(Integer publicState) {
		this.publicState = publicState;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String title) {
		this.articleTitle = title;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getArticleContext() {
		return articleContext;
	}

	public void setArticleContext(String articleContext) {
		this.articleContext = articleContext;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
