package com.yujj.entity.order;

import com.yujj.business.assembler.composite.StoreComposite;
import com.yujj.entity.StoreBusiness;

public class OrderNewVO extends OrderNew implements StoreComposite{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2081063733643158831L;

	private String storeLogo;
	private String businessName;
	private long businessNumber;
	
	public String getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public long getBusinessNumber() {
		return businessNumber;
	}
	public void setBusinessNumber(long businessNumber) {
		this.businessNumber = businessNumber;
	}
	/* (non-Javadoc)
	 * @see com.yujj.business.assembler.composite.StoreComposite#getStoreId()
	 */
	@Override
	public long getStoreId() {
		return this.getBelongBusinessId();
	}
	/* (non-Javadoc)
	 * @see com.yujj.business.assembler.composite.StoreComposite#assemble(com.yujj.entity.StoreBusiness)
	 */
	@Override
	public void assemble(StoreBusiness store) {
		this.setBusinessName(store.getBusinessName());
		this.setBusinessNumber(store.getBusinessNumber());
		this.setStoreLogo(store.getStoreLogo());
	}
    
}
