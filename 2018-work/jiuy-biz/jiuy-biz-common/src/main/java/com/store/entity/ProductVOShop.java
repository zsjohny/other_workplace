package com.store.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.entity.Product;
import com.yujj.web.helper.VersionControl;

@VersionControl("2.0.0.0")
public class ProductVOShop extends Product implements IncomeComposite {

    private static final long serialVersionUID = 166676858413714226L;

    private double income;
    
    public void setIncome(double income) {
		this.income = income;
	}

	private int count;
	
	private int uploadNum;
    
    private List<ShopStoreOrderItemVO> orderItemVOList;

    private Set<String> colors;
    private Set<String> sizes;
    
    private List<String> colorList;
    private Map<String, String> colorMap;
    
    private Map<String, String> sizeMap;
    
    private List<String> sizeList;

    private Long salesVolume ;
    private Integer memberLevel;
    private String memberLadderPriceJson ;

	@Override
	public Integer getMemberLevel() {
		return memberLevel;
	}

	@Override
	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}

	@Override
	public String getMemberLadderPriceJson() {
		return memberLadderPriceJson;
	}

	@Override
	public void setMemberLadderPriceJson(String memberLadderPriceJson) {
		this.memberLadderPriceJson = memberLadderPriceJson;
	}

	public Long getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Long salesVolume) {
		this.salesVolume = salesVolume;
	}

	/**

	/* (non-Javadoc)
	 * @see com.yujj.business.assembler.composite.IncomeComposite#getIncome()
	 */
	@Override
	public double getIncome() {
		// TODO Auto-generated method stub
		return Double.parseDouble(new java.text.DecimalFormat("#.00").format(income));
	}

	/* (non-Javadoc)
	 * @see com.yujj.business.assembler.composite.IncomeComposite#assemble(double)
	 */
	@Override
	public void assemble(double percent) {
		// TODO Auto-generated method stub
		this.income = this.getCash() * percent/100;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Set<String> getColors() {
		return colors;
	}

	public void setColors(Set<String> colors) {
		this.colors = colors;
	}

	public Set<String> getSizes() {
		return sizes;
	}

	public void setSizes(Set<String> sizes) {
		this.sizes = sizes;
	}
	
	public Map<String, Object> toSimpleMap15() {
	        return toSimpleMap15(false);
	}
	
	public Map<String, Object> toSimpleMap15(int buyCount) {
        return toSimpleMap15(false,buyCount);
	}
	 
	 public Map<String, Object> toSimpleMapIndex() {
		 return  super.toSimpleMapIndex(false,-1);
	 }
	
	public Map<String, Object> toSimpleMap15(boolean promotionImage,int buyCount ) {
		
		Map<String, Object>  map = super.toSimpleMapIndex(promotionImage,buyCount);
//        map.put("income", getIncome());
        map.put("uploadNum", getUploadNum());
        return map;
    }

	public List<ShopStoreOrderItemVO> getOrderItemVOList() {
		return orderItemVOList;
	}

	public void setOrderItemVOList(List<ShopStoreOrderItemVO> orderItemVOList) {
		this.orderItemVOList = orderItemVOList;
	}

	public List<String> getColorList() {
		return colorList;
	}

	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}

	public List<String> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}

	public Map<String, String> getColorMap() {
		return colorMap;
	}

	public void setColorMap(Map<String, String> colorMap) {
		this.colorMap = colorMap;
	}

	public Map<String, String> getSizeMap() {
		return sizeMap;
	}

	public void setSizeMap(Map<String, String> sizeMap) {
		this.sizeMap = sizeMap;
	}

	public int getUploadNum() {
		return uploadNum;
	}

	public void setUploadNum(int uploadNum) {
		this.uploadNum = uploadNum;
	}

	@Override
	public String toString() {
		return "ProductVO [income=" + income + ", count=" + count + ", uploadNum=" + uploadNum + ", orderItemVOList="
				+ orderItemVOList + ", colors=" + colors + ", sizes=" + sizes + ", colorList=" + colorList
				+ ", colorMap=" + colorMap + ", sizeMap=" + sizeMap + ", sizeList=" + sizeList + "]";
	}

	
}