package com.yujj.entity.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.yujj.entity.order.OrderItemVO;
import com.yujj.web.helper.VersionControl;

@VersionControl("2.0.0.0")
public class ProductVO extends Product  {

    private static final long serialVersionUID = 166676858413714226L;

    private double income;
    
    public void setIncome(double income) {
		this.income = income;
	}

	private int count;
    
    private List<OrderItemVO> orderItemVOList;

    private Set<String> colors;
    private Set<String> sizes;
    
    private List<String> colorList;
    private Map<String, String> colorMap;
    
    private Map<String, String> sizeMap;
    
    private List<String> sizeList;
    
//    /**
//
//	/* (non-Javadoc)
//	 * @see com.yujj.business.assembler.composite.IncomeComposite#getIncome()
//	 */
//	@Override
//	public double getIncome() {
//		return Double.parseDouble(new java.text.DecimalFormat("#.00").format(income));
//	}
//
//	/* (non-Javadoc)
//	 * @see com.yujj.business.assembler.composite.IncomeComposite#assemble(double)
//	 */
//	@Override
//	public void assemble(double percent) {
//		this.income = this.getCash() * percent/100;
//	}

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
	
//	public Map<String, Object> toSimpleMap15(boolean promotionImage) {
//		
//		Map<String, Object>  map = super.toSimpleMap15(promotionImage);
//        map.put("income", getIncome());
//        return map;
//    }

	public List<OrderItemVO> getOrderItemVOList() {
		return orderItemVOList;
	}

	public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
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

	

	
}
