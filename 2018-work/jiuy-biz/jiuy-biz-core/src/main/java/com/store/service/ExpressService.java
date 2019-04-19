/**
 * 
 */
package com.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.service.common.MemcachedService;

/**
 * @author LWS
 * u: help@yujiejie.com
 * p: lws2015
 * 
 * update at 2015/10/16 by LWS
 * 1、 增加通过不同的物流匹配使用不同的物流API功能
 */
@Service
public class ExpressService {
    
    @Autowired
    private List<IExpressQuery> expressQueries;

    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private ExpressSupplierService expressSupplierService;

    public JSON queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier) {
        String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
        String jsonExpress =   "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\""+expressSupplier+"\",\"com\":\""
		        +expressSupplier+"\",\"no\":\""
		        +orderNoWithSupplier+"\"}}";
        JSONObject json1 = JSON.parseObject(jsonExpress);
        String key = expressSupplier+":"+orderNoWithSupplier;
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
        	return (JSON) obj;
        }
        else {
        	boolean noResult = false;
        	ExpressSupplier expressSupplierTemp = expressSupplierService.getExpressSupplierByEngName(expressSupplier);
        	if(expressSupplierTemp != null && expressSupplierTemp.getCnName() != null  && expressSupplierTemp.getCnName().length() > 0){
	        	jsonExpress =   "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\""+expressSupplierTemp.getCnName()+"\",\"com\":\""
	    		        +expressSupplier+"\",\"no\":\""
	    		        +orderNoWithSupplier+"\"}}";
	        	json1 = JSON.parseObject(jsonExpress);
        	}
            for (IExpressQuery query : expressQueries) {
                if (query.support(expressSupplier)) {
                    String text = query.queryExpressInfo(expressSupplier, orderNoWithSupplier);
                    JSONObject json = JSON.parseObject(text.toString());
                    if (json == null || json.get("result") == null || ((JSONObject)json.get("result")).get("company") == null){
                    	noResult = true;
                    	json = json1;
                    }                    	
                    memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_HOUR, json);
                    return json;
                }
            }
        }
        return json1;
    }
    
}
