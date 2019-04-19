/**
 * 
 */
package com.store.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.store.entity.IncomeComposite;

/**
* @author DongZhong
* @version 创建时间: 2016年12月13日 下午1:42:37
*/
@Service
public class IncomeAssembler {
	
//	@Autowired
//	private StoreBusinessService storeBusinessService;
	
	public void assemble(Collection<? extends IncomeComposite> composites, UserDetail<StoreBusiness> userDetail) {
		// TODO Auto-generated method stub
        if (composites.isEmpty()) {
            return;
        }

        // 获取商家百分比        
//        double percent = storeBusinessService.getPercentById(storeBusinessId);
    	double percent = 0;
    	
    	if(userDetail != null){
    		StoreBusiness storeBusiness = userDetail.getUserDetail();
    		if(storeBusiness != null && storeBusiness.getCommissionPercentage() > 0){
    			percent = storeBusiness.getCommissionPercentage();
    		}
    	}
    	
        for (IncomeComposite composite : composites) {
        	composite.assemble(percent);
        }

	}

	public void assemble(Map<Long, ? extends IncomeComposite> composites, UserDetail<StoreBusiness> userDetail) {
		// TODO Auto-generated method stub
        if (composites.isEmpty()) {
            return;
        }

        // 获取商家百分比        
//        double percent = storeBusinessService.getPercentById(storeBusinessId);
    	double percent = 0;
    	
    	if(userDetail != null){
    		StoreBusiness storeBusiness = userDetail.getUserDetail();
    		if(storeBusiness != null && storeBusiness.getCommissionPercentage() > 0){
    			percent = storeBusiness.getCommissionPercentage();
    		}
    	}
    	
    	for(Map.Entry<Long, ? extends IncomeComposite> entry : composites.entrySet()){    
    		IncomeComposite composite = entry.getValue();
    		composite.assemble(percent);
    	   //  System.out.println(entry.getKey()+"--->"+entry.getValue());    
    	} 
    	
//        for (IncomeComposite composite : composites) {
//        	composite.assemble(percent);
//        }

	}
	
}
