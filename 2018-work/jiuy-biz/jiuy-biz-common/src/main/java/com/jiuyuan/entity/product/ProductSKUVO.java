package com.jiuyuan.entity.product;

import java.util.List;

import com.jiuy.core.meta.product.ProductVO;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.store.entity.ProductPropVO;


public class ProductSKUVO extends ProductSKU {

    private static final long serialVersionUID = 2243401956378183913L;
    

    private long productId;

    private List<ProductPropVO> skuProperties;
    
    private ProductVO productVO;
    
    private LOWarehouse loWarehouse;
	private String childCategoryName ;
	
	private long parentCategoryId ;
	
	private String parentCategoryName ;

	private String propertyIds ;

	private int remainCount ;
	private long skuNo ;
	private String  skuNoStr ;
	
//	private ProductSKU productSKU;
//	private String position;		//货架
//	private int remainCount2;		//副仓库存  
//	private long lOWarehouseId2;	//副仓id
//    private int remainKeepTime;		//库存保留时间
//    private double costPrice;		//成本价
//    private int sort;
	
    public String getSkuNoStr() {
		return String.valueOf(skuNo);
	}

	public void setSkuNoStr(String skuNoStr) {
		this.skuNoStr = skuNoStr;
	}

	
    

    public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public List<ProductPropVO> getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(List<ProductPropVO> skuProperties) {
        this.skuProperties = skuProperties;
    }

	public ProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}
	
	public LOWarehouse getLoWarehouse() {
		return loWarehouse;
	}

	public void setLoWarehouse(LOWarehouse loWarehouse) {
		this.loWarehouse = loWarehouse;
	}

	public String getSkuSnapshot() {
		StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProperties) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
	}
	
	public String getColor() {
		if(skuProperties.size() > 0) {
			return skuProperties.get(0).getValue();
		} 
		return "无颜色";
	}
	
	public String getSize() {
		if(skuProperties.size() > 1) {
			return skuProperties.get(1).getValue();
		} 
		return "无尺寸";
	}





	public String getChildCategoryName() {
		return childCategoryName;
	}

	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}

	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getPropertyIds() {
		return propertyIds;
	}

	public void setPropertyIds(String propertyIds) {
		this.propertyIds = propertyIds;
	}

	public int getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}

	public long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(long skuNo) {
		this.skuNo = skuNo;
	}

//	public String getPosition() {
//		return position;
//	}
//
//	public void setPosition(String position) {
//		this.position = position;
//	}
//
//	public int getRemainCount2() {
//		return remainCount2;
//	}
//
//	public void setRemainCount2(int remainCount2) {
//		this.remainCount2 = remainCount2;
//	}
//
//	public long getlOWarehouseId2() {
//		return lOWarehouseId2;
//	}
//
//	public void setlOWarehouseId2(long lOWarehouseId2) {
//		this.lOWarehouseId2 = lOWarehouseId2;
//	}
//
//	public int getRemainKeepTime() {
//		return remainKeepTime;
//	}
//
//	public void setRemainKeepTime(int remainKeepTime) {
//		this.remainKeepTime = remainKeepTime;
//	}
//
//	public double getCostPrice() {
//		return costPrice;
//	}
//
//	public void setCostPrice(double costPrice) {
//		this.costPrice = costPrice;
//	}
//
//	public int getSort() {
//		return sort;
//	}
//
//	public void setSort(int sort) {
//		this.sort = sort;
//	}
	
//	public ProductSKU getProductSKU() {
//		return productSKU;
//	}
//
//	public void setProductSKU(ProductSKU productSKU) {
//		this.productSKU = productSKU;
//	}

}