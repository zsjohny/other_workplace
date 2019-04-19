package com.yujj.business.assembler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yujj.business.assembler.composite.BrandComposite;
import com.yujj.business.assembler.composite.StoreComposite;
import com.yujj.business.service.BrandService;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.entity.Brand;
import com.yujj.entity.StoreBusiness;

@Service
public class StoreAssembler {

    @Autowired
    private StoreBusinessMapper storeMapper;

    public void assemble(Collection<? extends StoreComposite> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> ids = new HashSet<Long>();
        for (StoreComposite composite : composites) {
        	if (composite.getStoreId() != 0)
        		ids.add(composite.getStoreId());
        }
        
        if (ids.isEmpty()) return;
        
        Map<Long, StoreBusiness> map = storeMapper.getStoreMap(ids);

        for (StoreComposite composite : composites) {
        	if (composite.getStoreId() != 0) {
	            StoreBusiness store = map.get(composite.getStoreId());
	            composite.assemble(store);
        	}
        }
    }
}
