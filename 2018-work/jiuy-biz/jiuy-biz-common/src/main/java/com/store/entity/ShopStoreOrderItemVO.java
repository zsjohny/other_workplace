package com.store.entity;

import com.jiuyuan.entity.Product;

public class ShopStoreOrderItemVO extends ShopStoreOrderItem implements ProductComposite {

    private static final long serialVersionUID = 4213597407531077439L;
//    /**
//     * 无售后按钮
//     */
//    public static int unApplyAfterSaleButton = 0;
//    /**
//     * 有售后按钮
//     */
//    public static int applyAfterSaleButton = 1;
    
    private Product product;
    private String colorStr;
    private String sizeStr;
    //平台商品状态:0已上架、1已下架、2已删除
    private String platformProductState;
    
//    //订单对应的orderItem状态
//    private String orderItemStatus;
    
//    //是否显示售后按钮
//    private int isApplyAfterSaleButton;
//    
//    //售后单号
//    private String refundOrderNo;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

	@Override
    public void assemble(Product product) {
        setProduct(product);
    }

	public String getColorStr() {
		return colorStr;
	}

	public void setColorStr(String colorStr) {
		this.colorStr = colorStr;
	}

	public String getSizeStr() {
		return sizeStr;
	}

	public void setSizeStr(String sizeStr) {
		this.sizeStr = sizeStr;
	}

	public String getPlatformProductState() {
		return platformProductState;
	}

	public void setPlatformProductState(String platformProductState) {
		this.platformProductState = platformProductState;
	}

//	public String getOrderItemStatus() {
//		return orderItemStatus;
//	}
//
//	public void setOrderItemStatus(String orderItemStatus) {
//		this.orderItemStatus = orderItemStatus;
//	}
//
//	public int getIsApplyAfterSaleButton() {
//		return isApplyAfterSaleButton;
//	}
//
//	public void setIsApplyAfterSaleButton(int isApplyAfterSaleButton) {
//		this.isApplyAfterSaleButton = isApplyAfterSaleButton;
//	}
//
//	public String getRefundOrderNo() {
//		return refundOrderNo;
//	}
//
//	public void setRefundOrderNo(String refundOrderNo) {
//		this.refundOrderNo = refundOrderNo;
//	}

}