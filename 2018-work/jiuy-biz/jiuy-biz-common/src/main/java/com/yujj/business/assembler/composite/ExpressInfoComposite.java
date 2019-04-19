package com.yujj.business.assembler.composite;

import com.yujj.entity.order.ExpressInfo;

public interface ExpressInfoComposite {

    long getOrderItemGroupId();
    
    void assemble(ExpressInfo expressInfo);

}
