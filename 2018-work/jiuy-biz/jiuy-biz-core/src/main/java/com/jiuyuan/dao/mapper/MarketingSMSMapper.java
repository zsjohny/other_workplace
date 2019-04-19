/**
 * 
 */
package com.jiuyuan.dao.mapper;

import java.util.List;
import java.util.Map;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreBusiness;

/**
 * @author LWS
 *
 */
@DBMaster
public interface MarketingSMSMapper {

	List<StoreBusiness> getTestStores();

	List<Map<String, String>> getOtherStores();
}
