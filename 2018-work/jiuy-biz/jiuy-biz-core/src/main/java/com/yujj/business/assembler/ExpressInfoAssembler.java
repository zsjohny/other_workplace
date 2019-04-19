package com.yujj.business.assembler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.business.assembler.composite.ExpressInfoComposite;
import com.yujj.business.service.ExpressInfoService;
import com.yujj.entity.order.ExpressInfo;

@Service
public class ExpressInfoAssembler {

    @Autowired
    private ExpressInfoService expressInfoService;

    public void assemble(Collection<? extends ExpressInfoComposite> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> ids = new HashSet<Long>();
        for (ExpressInfoComposite composite : composites) {
            ids.add(composite.getOrderItemGroupId());
        }

        Map<Long, ExpressInfo> map = expressInfoService.getExpressInfoMap(ids);

        for (ExpressInfoComposite composite : composites) {
            ExpressInfo expressInfo = map.get(composite.getOrderItemGroupId());
            composite.assemble(expressInfo);
        }
    }
}
