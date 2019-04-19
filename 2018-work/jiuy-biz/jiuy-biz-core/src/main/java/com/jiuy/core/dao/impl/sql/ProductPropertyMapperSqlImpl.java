package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.modelv2.ProductPropertyMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropValue;
import com.store.entity.ProductPropVO;

public class ProductPropertyMapperSqlImpl extends SqlSupport implements ProductPropertyMapper {

    @Override
    public List<ProductProp> getOrderedProductProperties(long productId, boolean isSKU) {
        return null;
    }

    @Override
    public List<ProductProp> getProductProperties(long productId, Collection<Long> propertyIds) {
        return null;
    }

    @Override
    public int batchAdd(List<ProductPropVO> basics) {
        if(null == basics || basics.size() == 0){
            return 0;
        }
        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.insertBatch", basics);
    }
    
    @Override
    public int removeProductPropertiesByProductIds(Collection<Long> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        return getSqlSession().delete("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.deleteByProductIds", params);
    }

	@Override
	public int getPropertyByIds(long productId, long propertyNameId) {
		Map<String,Object> map = new HashMap<>();
		map.put("productId", productId);
		map.put("propertyNameId", propertyNameId);
		Integer result = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.getPropertyByIds", map);
		return result == null? -1 : result;
	}

	@Override
	public int updateProductProperty(int productPropertyId, long[] seasonArrayInt) {
		Map<String,Object> map = new HashMap<>();
		map.put("id", productPropertyId);
		map.put("propertyNameId", seasonArrayInt[0]);
		map.put("propertyValueId", seasonArrayInt[1]);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.updateProductProperty", map);
	}

	@Override
	public int addProductProperty(long productId, long[] seasonArrayInt) {
		Map<String,Object> map = new HashMap<>();
		map.put("productId", productId);
		map.put("propertyNameId", seasonArrayInt[0]);
		map.put("propertyValueId", seasonArrayInt[1]);
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.addProductProperty", map);
	}

	@Override
    public int updateProductBrand(int ppmId, long brandId) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("id", ppmId);
		param.put("brandId", brandId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.updateProductBrand", param);
	}

	@Override
    public int addProductBrand(long productId, long brand_property, long brandId) {
		Map<String, Object> param = new HashMap<String, Object>();
		long time = System.currentTimeMillis();
		
		param.put("productId", productId);
		param.put("propertyNameId", brand_property);
		param.put("brandId", brandId);
		param.put("createTime", time);
		param.put("updateTime", time);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.addProductBrand", param);
	}

	@Override
	public List<ProductPropValue> propertiesOfProductIds(Collection<Long> productIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("productIds", productIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.propertiesOfProductIds", param);
	}

	@Override
	public Map<Long, ProductProp> valueOfNameIdMap(long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("productId", id);
		
		return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.ProductPropertyMapperSqlImpl.valueOfNameIdMap", param, "propertyNameId");
	}


}
