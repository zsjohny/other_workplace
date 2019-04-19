/**
 * 
 */
package com.jiuy.core.service;

import org.springframework.stereotype.Service;

/**
 * @author LWS
 *
 */
@Service
public class AuditServiceImpl implements AuditService {
    
//    @Autowired
//    private DictionaryDao dictionaryDao;
//    
//    private static final String EXPRESS_GROUP_ID = "EXPRESS";
//
//    /* (non-Javadoc)
//     * @see com.jiuy.core.service.AuditService#getSendBackOrder(int, java.lang.String)
//     */
//    @Override
//    public Order getSendBackOrder(int supplier, String expressOrderNo) {
//    	ExpressSupplier es = ParseIntToEnumUtil.parse(supplier, ExpressSupplier.class);
//    	String supplierStr = es.getDesc();
//    	List<Dictionary> suppierDictList = dictionaryDao.loadDictionaryByGroup(EXPRESS_GROUP_ID,supplierStr);
//    	if(null == suppierDictList || suppierDictList.size() == 0){
//    		return null;
//    	}
//    	
//        return null;
//    }

}
