package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.SupplierExpressMapper;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class ExpressNewService implements IExpressNewService{
	
	private static final Log logger = LogFactory.get("SupplierExpressService");

	@Autowired
	private SupplierExpressMapper supplierExpressMapper;
	
	//获取邮寄公司名称列表
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IExpressNewService#getAllExpressCompanyNames()
	 */
	@Override
	public List<Map<String,Object>> getAllExpressCompanyNames() {
		Wrapper<ExpressSupplier> wrapper = 
				new EntityWrapper<ExpressSupplier>().eq("status", 0);
		List<ExpressSupplier> expressSupplierList = supplierExpressMapper.selectList(wrapper);
		List<Map<String,Object>> expressCompanyList = new ArrayList<Map<String,Object>>();
		for (ExpressSupplier expressSupplier : expressSupplierList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("CnName", expressSupplier.getCnName());
			map.put("EngName", expressSupplier.getEngName());
			map.put("Id", expressSupplier.getId());
			expressCompanyList.add(map);
			
		}
		return expressCompanyList;
	}
}