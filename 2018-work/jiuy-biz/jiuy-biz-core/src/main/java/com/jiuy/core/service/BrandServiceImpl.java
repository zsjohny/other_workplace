/**
 * 
 */
package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.jiuy.core.dao.BrandCategoryDao;
import com.jiuy.core.dao.BrandHomeUrlDao;
import com.jiuy.core.dao.BrandPartnerDao;
import com.jiuy.core.dao.modelv2.CategoryMapper;
import com.jiuy.core.service.partner.PartnerInnerCatService;
import com.jiuy.core.service.template.TemplateService;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.brand.BrandHomePage;
import com.jiuyuan.entity.brand.Partner;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.BrandCategoryVo;
import com.jiuyuan.entity.brandcategory.PartnerCategory;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.template.Template;
import com.store.entity.Brand;

@Service("brandService")
public class BrandServiceImpl implements BrandService{

	@Resource
	private BrandCategoryDao brandCategoryDao;
	
	@Resource 
	private BrandHomeUrlDao brandHomeUrlDao;
	
	@Resource
	private BrandPartnerDao brandPartnerDao;
	
	@Resource
    private CategoryService categoryService;
	
	@Resource
	private CategoryMapper categoryMapper;
	
	@Resource
	private TemplateService templateServiceImpl;
	
	@Resource
	private PartnerInnerCatService partnerInnerCatServiceImpl;

    @Override
    public long addNewBrand(Brand brand,BrandHomePage homePage) {
        return 0;
    }

	@Override
	public int addBrandCategoryList(List<BrandCategoryVo> voList) {

		List<PartnerCategory> pcList = new ArrayList<PartnerCategory>();

		List<Long> deleteList=new ArrayList<Long>();
		
		for (BrandCategoryVo vo : voList) {
			PartnerCategory pc = new PartnerCategory();
			BeanUtils.copyProperties(vo, pc);
			pcList.add(pc);
		}

		List<PartnerCategory> allList = brandCategoryDao.getAllBrandCategory();
         
		if (CollectionUtils.isNotEmpty(pcList)) {

			SetMultimap<Long, Long> newSetMap = SetMultimapBuilder.hashKeys().hashSetValues().build();

			for (PartnerCategory pc : pcList) {
				newSetMap.put(pc.getCategoryId(), pc.getPartnerId());
			}

			for(PartnerCategory dbpc:allList){
				if (newSetMap.containsKey(dbpc.getCategoryId())) {
					Set<Long> partenerIds = newSetMap.get(dbpc.getCategoryId());
					if (CollectionUtils.isEmpty(partenerIds) || !partenerIds.contains(dbpc.getPartnerId())) {
						deleteList.add(dbpc.getId());
					}
				}
			}
			// 删除去掉的类目下的品牌
			if (CollectionUtils.isNotEmpty(deleteList)) {
				brandCategoryDao.deleteBrandCategory(deleteList);
			}

			return brandCategoryDao.addBrandCategoryList(pcList);
		}

		return 0;
	}

	@Override
	public int deleteBrandCategory(Long id) {
		return brandCategoryDao.deleteBrandCategory(Arrays.asList(id));
	}

	@Override
	public List<BrandCategoryVo> getPartnerCategoryListByCategoryId(
			Long categoryId) {
		List<BrandCategoryVo> voList = new ArrayList<BrandCategoryVo>();
		List<PartnerCategory> pcList = brandCategoryDao
				.getBrandCategoryByCategoryId(categoryId);
		for (PartnerCategory pc : pcList) {
			BrandCategoryVo vo = new BrandCategoryVo();
			BeanUtils.copyProperties(pc, vo);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public List<BrandCategoryVo> getCustomPartnerCategoryList() {
		List<BrandCategoryVo> voList = new ArrayList<BrandCategoryVo>();
		List<PartnerCategory> pcList = brandCategoryDao.getCustomBrandCategory();
		for (PartnerCategory pc : pcList) {
			BrandCategoryVo vo = new BrandCategoryVo();
			BeanUtils.copyProperties(pc, vo);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public List<Partner> searchBrand(String item) {

		if (StringUtils.isEmpty(item)) {
			item = null;
		}

		return brandCategoryDao.getWildBrand(item);
	}

	@Override
	public List<BrandCategoryVo> getAllBrandCategory() {
		List<BrandCategoryVo> voList = new ArrayList<BrandCategoryVo>();
		List<PartnerCategory> pcList = brandCategoryDao.getAllBrandCategory();
		for (PartnerCategory pc : pcList) {
			BrandCategoryVo vo = new BrandCategoryVo();
			BeanUtils.copyProperties(pc, vo);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public int addBrandUrl(long partnerId, String brandUrl, String template) {
		return brandHomeUrlDao.addBrandUrl(partnerId, brandUrl,template);
	}

	@Override
	public String getTemplateUrl(String brandUrl) {
		return brandHomeUrlDao.getTemplateUrl(brandUrl);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int addBrand(Brand brand, long categoryId) {
		
		//根据逻辑需要更新两张相关联的表
		int count1 = brandPartnerDao.addBrand(brand);
		int partnerId = brandPartnerDao.getIdByName(brand.getName());
		int count2 = brandCategoryDao.addBrand(categoryId, partnerId);
		
		return count1&count2;
	}

	@Override
	public List<Category> getPartnerCategories() {
		return categoryService.getPartnerCategories();
	}

	@Override
	public List<Map<String, Object>> getPartnerByName(String name) {
		return brandPartnerDao.getPartnerByName(name);
	}

	@Override
	public int deleteBrandPartner(long id) {
		int success = brandPartnerDao.removeBrand(id);
		int success2 = brandCategoryDao.deleteBrandPartner(id);
		return success&success2;
	}

	@Override
	public List<Map<String, Object>> getPartnerById(long id) {
		return brandPartnerDao.getPartnerById(id);
	}

	@Override
	public List<Map<String, Object>> getHomeSettingInfo(long partnerId) {
		return brandHomeUrlDao.getHomeSettingInfo(partnerId);
	}

	@Override
	public int activeBrandUrl(long partnerId) {
		return brandHomeUrlDao.activeBrandUrl(partnerId);
	}

	@Override
	public int deactiveBrandUrl(long partnerId) {
		return brandHomeUrlDao.deactiveBrandUrl(partnerId);
	}
	
	@Override
	public List<PartnerVO> search(PageQuery query, String name, String engName) {
		return brandPartnerDao.search(query, name, engName);
	}

	@Override
	public int searchCount(String name, String engName) {
		return brandPartnerDao.searchCount(name, engName);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addBrand(PartnerVO paVo) {
		paVo.setCreateTime(System.currentTimeMillis());
		paVo.setUpdateTime(System.currentTimeMillis());
		
		//新增template
		Template elem = templateServiceImpl.add(paVo.getId());
		//默认新增内部分类：当季新品和人气单品
		//是否已存在
		int count = partnerInnerCatServiceImpl.searchVirtualCat(paVo.getId());
		if(count == 0) {
			int row = partnerInnerCatServiceImpl.addVirtualCat(paVo.getId());
			if(row == 0) {
				return false;
			}
		}
		
		paVo.setTemplateId(elem.getId());
		
		return brandPartnerDao.addBrand(paVo) > 0 ? true : false;
	}

	@Override
	public boolean updateBrand(PartnerVO paVo) {
		
		int row = brandPartnerDao.updateBrand(paVo);
		
		return row > 0 ? true : false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean removeBrand(long[] ids) {
		for(int i = 0; i < ids.length; i ++) {
			int row = brandPartnerDao.removeBrand(ids[i]);
			if(row < 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addUrl(long partnerId, String url) {
		String existUrl = brandPartnerDao.getPartnerUrl(partnerId);
		if(existUrl != null) {
			return false;
		}
		int row = brandPartnerDao.addPartnerUrl(partnerId, url);
		return row > 0 ? true : false;
	}

}
