package com.store.entity;

import java.io.Serializable;

import com.jiuyuan.entity.newentity.StoreBusiness;




/**
 * 商家
 * @author Administrator
 *
 */
public class ShopDetail implements  Serializable {

    private static final long serialVersionUID = -1104663920836940976L;


    private StoreBusiness storeBusiness;


    public StoreBusiness getStoreBusiness() {
        return storeBusiness;
    }

    public void setStoreBusiness(StoreBusiness storeBusiness) {
        this.storeBusiness = storeBusiness;
    }

    public long getId() {
        return getStoreBusiness() == null ? 0 : getStoreBusiness().getId();
    }

   

    @Override
    public String toString() {
        return "" + getId();
    }
}
