package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.BrandMapper;
import com.jiuyuan.dao.mapper.supplier.BrandNewMapper;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.Brand;

@Service
public class XBrandNewService implements IBrandNewService {
	private static final Logger logger = Logger.getLogger(BrandNewService.class);
	private static final int NORMAL_STATUS = 0;
	@Autowired
	private ISupplierCustomer supplierCustomerService;
	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private IUserNewService userNewService;
	@Autowired
	private BrandNewMapper brandNewMapper;

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IBrandNewService#getBrandMap()
	 */
	@Override
	public Map<Long, BrandLogo> getBrandMap() {
		Map<Long, BrandLogo> brandMap = new HashMap<Long, BrandLogo>();

		List<BrandLogo> brandLogos = brandMapper.getBrandLogos();
		for(BrandLogo brandLogo : brandLogos) {
			brandMap.put(brandLogo.getBrandId(), brandLogo);
		}

		return brandMap;
	}


	@Override
	public List<BrandVO> getBrandListShow(String searchBrand, PageQuery pageQuery, int type ,long userId,int brandType) {
    	return brandMapper.getBrandListShow(searchBrand, pageQuery, type , userId,brandType);
    }


	@Override
	public Brand getBrand(long brandId) {
		return brandMapper.getBrand(brandId);
	}
	/**
	 * 获取品牌信息
	 */
	@Override
	public BrandNew getBrandByBrandId(long brandId) {
		Wrapper<BrandNew> wrapper = new EntityWrapper<BrandNew>();
	    wrapper.eq("BrandId",brandId);
		List<BrandNew> list = brandNewMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}

	}

	@Override
	public Map<String,Object> CheckBrandList(List<String> idList) {
        Map<String,Object> map = new HashMap<String,Object>();
		Wrapper<BrandNew> wrapper = new EntityWrapper<BrandNew>().eq("STATUS", NORMAL_STATUS);
		for(int i=0 ;; i++){
			String id = idList.get(i);
			wrapper.eq("BrandId", id);
			if(i ==idList.size()-1){
				break;
			}
			wrapper.or();
		}
		List<BrandNew> list = brandNewMapper.selectList(wrapper);
		if(idList.size() !=list.size()){
		    //获取每个品牌的Id,进行提醒
			map.put("equal", false);
		}else{
			map.put("equal", true);
		}
		Set<String> rightIdSet = new TreeSet<String>();
		Set<String> rightNameSet = new TreeSet<String>();
		List<String> rightIdList = new ArrayList<>();
		for(BrandNew brandNew :list){
			String brandId = brandNew.getBrandId()+"";
			String brandName = brandNew.getBrandName();
			rightIdSet.add(brandId);
			rightNameSet.add(brandName);
			rightIdList.add(brandId);
		}
		idList.removeAll(rightIdList);
		map.put("rightIdSet", rightIdSet);
		map.put("wrongIdSet", idList);
		map.put("rightNameSet", rightNameSet);
		return map;

	}


	@Override
	public BrandNew getBrandNew(long brandId) {
		Wrapper<BrandNew> wrapper = new EntityWrapper<BrandNew>().eq("BrandId", brandId);
		return brandNewMapper.selectList(wrapper).get(0);
	}


	@Override
	public Map<String,Object> checkBrowsePermission( String phoneNumber , long brandId,
			Map<String, Object> data) {
			UserNew userNew = userNewService.getSupplierByBrandId(brandId);
			Long supplierId = userNew.getId();
			// 判断用户权限
			//品牌宣传图
//			String campaignImage = userNew.getCampaignImage();
//			data.put("campaignImage", campaignImage);
			//商品权限
			String productPermission = userNew.getProductPermission();
			if ("0".equals(productPermission)) {// 供应商商品权限为公开
//				logger.info("全部可见，所以你看到了");
				data.put("hasPermission", 1);
//				data.put("campaignImage", campaignImage);
			} else if ("-1".equals(productPermission)) {// 全部不可见
//				logger.info("全部不可见");
				data.put("hasPermission", 0);
//				data.put("campaignImage", campaignImage);
			} else {
                /* 部分客户可见 */
                SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByPhone(phoneNumber, supplierId);
				if (supplierCustomer == null) {
				    // 不是供应商客户
					logger.info("部分客户可见，不是供应商客户！！！phoneNumber:"+phoneNumber);
					data.put("hasPermission", 0);
//					data.put("campaignImage", campaignImage);
				} else {
				    //是供应商客户
                    // 获取分组id
					Long groupId = supplierCustomer.getGroupId();
					boolean result = productPermission.contains(String.valueOf(groupId));
					if (result) {
						// 有权限
						logger.info("部分客户可见，你还有权限phoneNumber:"+phoneNumber);
						data.put("hasPermission", 1);
//						data.put("campaignImage", campaignImage);
					} else {
						// 没有权限
						logger.info("你是客户，级别还不够！phoneNumber:"+phoneNumber);
						data.put("hasPermission", 0);
					}
				}
			}
			return data;
		}

	@Override
	public int checkBrowsePermission(String phoneNumber , long brandId) {
			int hasPermission = 0;
			UserNew userNew = userNewService.getSupplierByBrandId(brandId);
			Long supplierId = userNew.getId();
			// 判断用户权限
			SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByPhone(phoneNumber, supplierId);
			//品牌宣传图
//			String campaignImage = userNew.getCampaignImage();
//			data.put("campaignImage", campaignImage);
			//商品权限
			String productPermission = userNew.getProductPermission();
			if ("0".equals(productPermission)) {// 供应商商品权限为公开
//				logger.info("全部可见，所以你看到了");
				hasPermission = 1;
//				data.put("campaignImage", campaignImage);
			} else if ("-1".equals(productPermission)) {// 全部不可见
//				logger.info("全部不可见");
				hasPermission = 0;
//				data.put("campaignImage", campaignImage);
			} else {// 部分客户可见
				if (supplierCustomer == null) {// 不是供应商客户
					logger.info("部分客户可见，不是供应商客户！！！phoneNumber:"+phoneNumber);
					hasPermission = 0;
//					data.put("campaignImage", campaignImage);
				} else {//是供应商客户
					// 获取分组id
					Long groupId = supplierCustomer.getGroupId();
					boolean result = productPermission.contains(String.valueOf(groupId));
					if (result) {
						// 有权限
						logger.info("部分客户可见，你还有权限phoneNumber:"+phoneNumber);
						hasPermission = 1;
//						data.put("campaignImage", campaignImage);
					} else {
						// 没有权限
						logger.info("你是客户，级别还不够！phoneNumber:"+phoneNumber);
						hasPermission = 0;
					}
				}
			}
			return hasPermission;
		}

	@Override
	public List<BrandVO> getHsitory(long userId, PageQuery pageQuery) {
		return brandMapper.getBrandListShowHistory(userId,pageQuery);
	}


}