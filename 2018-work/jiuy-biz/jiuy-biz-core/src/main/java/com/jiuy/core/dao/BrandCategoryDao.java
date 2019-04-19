package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.brand.Partner;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.PartnerCategory;

public interface BrandCategoryDao extends DomainDao<PartnerCategory, Long> {

	/**
	 * 添加品牌分类
	 * 
	 * @param list
	 * @return
	 */
	public int addBrandCategoryList(List<PartnerCategory> list);

	/**
	 * 删除某个分类品牌记录
	 * 
	 * @param id
	 * @return
	 */
	public int deleteBrandCategory(List<Long> ids);

	/**
	 * 获取某个分类下的品牌
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<PartnerCategory> getBrandCategoryByCategoryId(Long categoryId);

	/**
	 * 获取自定义品牌分类
	 * 
	 * @return
	 */
	public List<PartnerCategory> getCustomBrandCategory();

	/**
	 * 获取所有的品牌类目信息
	 * 
	 * @return
	 */
	public List<PartnerCategory> getAllBrandCategory();

	/**
	 * 模糊查询品牌
	 * 
	 * @param brandName
	 * @return
	 */
	public List<Partner> getWildBrand(String brandName);

	 /**
     * 品牌馆-品牌管理-新增品牌-PartnerCategory表
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	public int addBrand(long categoryId, int partnerId);


	 /**
     * 品牌馆-品牌管理-删除商家品牌 
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
	public int deleteBrandPartner(long id);

	public int updateBrand(PartnerVO paVo);
}
