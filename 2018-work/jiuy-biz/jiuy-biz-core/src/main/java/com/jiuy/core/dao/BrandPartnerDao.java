package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.Brand;

public interface BrandPartnerDao {


	 /**
     * 品牌馆-品牌管理-新增品牌
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	int addBrand(Brand brand);

	/**
	 * 品牌馆-根据品牌名字获取partner表中的ID
	 * @author Jeff.Zhan
	 * @param name
	 * @return
	 */
	int getIdByName(String name);

	/**
     * 品牌馆-品牌管理-模糊查询商家
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	List<Map<String, Object>> getPartnerByName(String name);

    /**
     * 品牌馆-品牌管理-获取某个商家的信息
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
	List<Map<String, Object>> getPartnerById(long id);

	
	/**
	 * 品牌馆-品牌管理-获取信息
	 * @author Jeff.Zhan
	 * @param query 
	 * @param modelMap
	 * @return
	 */
	List<PartnerVO> search(PageQuery query, String name, String engName);

	int searchCount(String name, String engName);

	int addBrand(PartnerVO paVo);

	int updateBrand(PartnerVO paVo);

	int removeBrand(long id);

	String getPartnerUrl(long partnerId);

	int addPartnerUrl(long partnerId, String url);

}
