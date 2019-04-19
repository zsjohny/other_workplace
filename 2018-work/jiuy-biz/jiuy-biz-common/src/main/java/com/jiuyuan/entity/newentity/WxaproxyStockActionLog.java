package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 代理商用户
 * </p>
 */
@TableName("wxaproxy_stock_action_log")
public class WxaproxyStockActionLog extends Model<WxaproxyStockActionLog> {

	 private static final long serialVersionUID = 1L;

	    /**
	     * id
	     */
		@TableId(value="id", type= IdType.AUTO)
		private Long id;
	    /**
	     * 操作人ID
	     */
		@TableField("admin_id")
		private Long adminId;
	    /**
	     * 代理商ID
	     */
		@TableField("proxy_user_id")
		private Long proxyUserId;
	    /**
	     * 代理产品ID
	     */
		@TableField("proxy_product_id")
		private Long proxyProductId;
	    /**
	     * 新增库存量
	     */
		@TableField("incr_stock_count")
		private Integer incrStockCount;
	    /**
	     * 操作时间
	     */
		@TableField("create_time")
		private Long createTime;


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getAdminId() {
			return adminId;
		}

		public void setAdminId(Long adminId) {
			this.adminId = adminId;
		}

		public Long getProxyUserId() {
			return proxyUserId;
		}

		public void setProxyUserId(Long proxyUserId) {
			this.proxyUserId = proxyUserId;
		}

		public Long getProxyProductId() {
			return proxyProductId;
		}

		public void setProxyProductId(Long proxyProductId) {
			this.proxyProductId = proxyProductId;
		}

		public Integer getIncrStockCount() {
			return incrStockCount;
		}

		public void setIncrStockCount(Integer incrStockCount) {
			this.incrStockCount = incrStockCount;
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
