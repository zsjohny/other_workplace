package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 商品挂靠的展示类目
 *
 * @author auto
 * @since 2.0
 */
public class ItemSellerCat extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品编号
	 */
	@ApiField("num_iid")
	private String numIid;

	/**
	 * 商品挂靠的展示类目集合
	 */
	@ApiListField("seller_cats")
	@ApiField("seller_cat")
	private List<SellerCat> sellerCats;

	public String getNumIid() {
		return this.numIid;
	}
	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public List<SellerCat> getSellerCats() {
		return this.sellerCats;
	}
	public void setSellerCats(List<SellerCat> sellerCats) {
		this.sellerCats = sellerCats;
	}

}