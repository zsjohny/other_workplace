/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.JsonArray;
import com.jiuyuan.dao.mapper.supplier.PlacardApplyAuditMapper;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.PlacardApplyAudit;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.xiaoleilu.hutool.json.JSONArray;

/**
 * 公告报名申请审核记录
 */
@Service
public class PlacardApplyAuditService implements IPlacardApplyAuditService  {
	private static final Logger logger = LoggerFactory.getLogger(PlacardApplyAuditService.class);
	@Autowired
	private PlacardApplyAuditMapper placardApplyAuditMapper;
	
	public List<PlacardApplyAudit> getPlacardApplyAuditList(long supplierPlacardId, long supplierId){
		Wrapper<PlacardApplyAudit> wrapper = new EntityWrapper<PlacardApplyAudit>();
		wrapper.eq("placard_id", supplierPlacardId);
		wrapper.eq("supplier_id", supplierId);
		wrapper.orderBy("apply_time", false);
		List<PlacardApplyAudit> list = placardApplyAuditMapper.selectList(wrapper);
		return list;
	}
	
	public PlacardApplyAudit getPlacardApplyAuditInfo(long placardApplyAuditId){
		return placardApplyAuditMapper.selectById(placardApplyAuditId);
	}

}