/**
 * 
 */
package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;

/**
 * @author LWS
 *
 */
public interface MetaService {

    public ProductPropName getProductPropertyNameById(long id);
    
    public Collection<ProductPropValue> getProductPropertyValues(long nameId);
    
    /**
     * 返回带父级名称的类别分类数据
     * author:Jeff.Zhan
     * @return
     */
    public List<Map<String, Object>> getCategory();
    
}
