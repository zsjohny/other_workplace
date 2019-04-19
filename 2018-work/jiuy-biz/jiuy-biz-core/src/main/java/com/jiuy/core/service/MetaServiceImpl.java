/**
 * 
 */
package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;

/**
 * @author LWS
 *
 */
@Service("metaService")
public class MetaServiceImpl implements MetaService {
    
    @Autowired
    private PropertyService propertySer;
    
    @Autowired
    private CategoryService categoryService;
    
    // 这里的类名和变量名不一致是由于前面写反的原因.
    @SuppressWarnings("unused")
	@Autowired
    private ProductCategoryMapper propertyValueMapper;

    @Override
    public ProductPropName getProductPropertyNameById(long id) {
        Collection<Long> c = new ArrayList<Long>();
        c.add(id);
        return propertySer.getPropertyNames().get(id);
    }

    @Override
    public Collection<ProductPropValue> getProductPropertyValues(long nameId) {
        Map<String, ProductPropValue> ppMap = propertySer.getPropertyValueMap();
        Collection<ProductPropValue> values = ppMap.values();
        Collection<ProductPropValue> valuesRet = getPropertyByNameId(nameId, values);
        return valuesRet;
    }
    
    private Collection<ProductPropValue> getPropertyByNameId(long id, Collection<ProductPropValue> values) {
        Collection<ProductPropValue> subSet = new ArrayList<ProductPropValue>();
        if (null != values) {
            for (ProductPropValue ppv : values) {
                if (ppv.getPropertyNameId() == id) {
                    subSet.add(ppv);
                }
            }
            return subSet;
        }
        return null;
    }

    
	@Override
	public List<Map<String, Object>> getCategory() {
		return categoryService.getCategoriesParentName();
	}

}
