package com.yujj.business.assembler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yujj.business.assembler.composite.BrandComposite;
import com.yujj.business.service.BrandService;
import com.yujj.entity.Brand;

@Service
public class BrandAssembler {

    @Autowired
    private BrandService branService;

    public void assemble(Collection<? extends BrandComposite> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> ids = new HashSet<Long>();
        for (BrandComposite composite : composites) {
            ids.add(composite.getBrandId());
        }

        Map<Long, Brand> map = branService.getBrandMap(ids);

        for (BrandComposite composite : composites) {
            Brand brand = map.get(composite.getBrandId());
            composite.assemble(brand);
        }
    }
}
