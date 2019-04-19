/**
 * 
 */
package com.jiuy.core.service;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.brand.BrandHomePage;
import com.jiuyuan.entity.brand.Partner;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.BrandCategoryVo;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.Brand;

/**
 * @author Never
 *
 */
public interface BrandService {

    /**
     * 添加品牌 
     * 
     * @param brand
     * @param homePage 
     * @return
     */
    public long addNewBrand(Brand brand, BrandHomePage homePage);

	/**
	 * 添加品牌分类
	 * 
	 * @param list
	 * @return
	 */
	public int addBrandCategoryList(List<BrandCategoryVo> list);

	/**
	 * 删除某一个分类下的品牌
	 * 
	 * @param id
	 * @return
	 */
	public int deleteBrandCategory(Long id);

	/**
	 * 获取某一个分类下的品牌
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<BrandCategoryVo> getPartnerCategoryListByCategoryId(
			Long categoryId);

	/**
	 * 获取自定义分类下的品牌分类
	 * 
	 * @return
	 */
	public List<BrandCategoryVo> getCustomPartnerCategoryList();

	/**
	 * 模糊查询品牌
	 * 
	 * @param item
	 * @return
	 */
	public List<Partner> searchBrand(String item);

	/**
	 * 获取所有分类下的品牌
	 * 
	 * @return
	 */
	public List<BrandCategoryVo> getAllBrandCategory();

	/**
     * 添加品牌馆自定义主页
	 * @param partnerId 
     * @param brandUrl
     * @param template
     * @return
     */
	public int addBrandUrl(long partnerId, String brandUrl, String template);
	
	/**
     * 存在--返回品牌馆自定义主页;不存在--返回错误页面 
     * @param brandUrl
     * @return
     */
	public String getTemplateUrl(String brandUrl);

	/**
     * 品牌馆-品牌管理-新增品牌
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	public int addBrand(Brand brand, long categoryId);


	  /**
     * 品牌馆-获取商家定义的分类
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	public List<Category> getPartnerCategories();

	/**
     * 品牌馆-品牌管理-模糊查询商家
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	public List<Map<String, Object>> getPartnerByName(String name);

	/**
     * 品牌馆-品牌管理-删除商家品牌 
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
	public int deleteBrandPartner(long id);

	/**
     * 品牌馆-品牌管理-获取某个商家的信息
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
	public List<Map<String, Object>> getPartnerById(long id);

	public List<Map<String, Object>> getHomeSettingInfo(long partnerId);

	public int activeBrandUrl(long partnerId);

	public int deactiveBrandUrl(long partnerId);
	
	
	
	 /**
     * 品牌馆-品牌管理-获取信息
     * @author Jeff.Zhan
	 * @param query 
     * @param modelMap
     * @return
     */
	public List<PartnerVO> search(PageQuery query, String name, String engName);

	public int searchCount(String name, String engName);

	public boolean addBrand(PartnerVO paVo);

	public boolean updateBrand(PartnerVO paVo);

	public boolean removeBrand(long[] ids);

	public boolean addUrl(long partnerId, String url);

}
