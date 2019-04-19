package com.yujj.business.assembler.composite;

import com.yujj.entity.Brand;

public interface BrandComposite {

    long getBrandId();
    
    void assemble(Brand brand);

}
