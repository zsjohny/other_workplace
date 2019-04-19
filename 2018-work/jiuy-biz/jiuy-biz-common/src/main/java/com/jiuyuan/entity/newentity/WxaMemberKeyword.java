package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员搜索词
 * </p>
 *
 * @author 赵兴林
 * @since 2018-04-03
 */
@TableName("store_wxa_member_keyword")
public class WxaMemberKeyword extends Model<WxaMemberKeyword> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 搜索词
     */
	private String keyword;
	
	/**
     * 门店ID
     */
	@TableField("store_id")
	private Long storeId;
	/**
     * 小程序会员ID
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
//	public WxaMemberKeyword() {
//	}
//	public WxaMemberKeyword(long storeId,long memberId, String word, long createTime) {
//		this.keyword = word;
//		this.memberId = memberId;
//		this.storeId = storeId;
//		this.createTime = createTime;
//	}

	

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
