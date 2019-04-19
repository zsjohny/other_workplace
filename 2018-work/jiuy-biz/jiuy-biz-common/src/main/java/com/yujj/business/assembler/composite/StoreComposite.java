package com.yujj.business.assembler.composite;

import com.yujj.entity.StoreBusiness;

public interface StoreComposite {

    long getStoreId();
    
    void assemble(StoreBusiness store);

}
