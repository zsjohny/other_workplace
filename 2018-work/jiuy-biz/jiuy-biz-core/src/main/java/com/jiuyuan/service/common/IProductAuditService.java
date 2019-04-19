package com.jiuyuan.service.common;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProductAudit;

public interface IProductAuditService {

	/**
	 * 运营平台获取商品审核查询列表
	 * @param page
	 * auditState:-2客服全部、-1买手全部、0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
	 * @return
	 */
	List<ProductAudit> getSearchProductAuditList(Page page, int auditState, long productId, String productName,
			String priceBegin, String priceEnd, String clothesNumber, long submitAuditTimeBegin,
			long submitAuditTimeEnd, String brandName);

	ProductAudit getProductAuditById(long productAuditId);

	void productAuditNoPass(long productAuditId, String noPassReason);

	/**
	 * 商品审核通过
	 * @param productAuditId
	 */
	void productAuditPass(long productAuditId);

	void addServerProductAudit(long productId);

}