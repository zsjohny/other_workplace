package com.jiuy.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.SyncDatabaseFacade;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 同步数据库的数据
 * 
 * @author jeff.zhan
 * @version 2016年10月25日 下午2:17:12
 * 
 */
@Controller
@RequestMapping("/syncdatabase")
public class SyncDatabaseUtil {
	
	@Autowired
	private SyncDatabaseFacade syncDatabaseFacade;
	
	/**
	 * 现已在普通分类新建“50以下”“51-100”“101-200”“201-500”“500以上”5个虚拟分类，
	 * 现需技术部门将平台所有商品的价格与上述5个虚拟分类的价格区间进行匹配，将价格符合区间分类的商品加入到该匹配的虚拟分类中。
	 * （如：一款商品49元，就将改商品放入”50以下”的虚拟分类中）
	 * @return
	 */
	@RequestMapping("/add/bind/prodcategory")
	@ResponseBody
	public JsonResponse addVirtualProduct() {
		JsonResponse jsonResponse = new JsonResponse();
		
		syncDatabaseFacade.bindCategory();
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 	同价位段的商品，若推荐指数越高的，生成的虚拟销售量越多
	 *	20元以下的商品推广销量随机30~50件（SKU）
	 *	20-50元商品推广销量随机20~40件（SKU）
	 *	50-100元商品推广销量随机15~30件（SKU）
	 *	100-200元商品推广销量随机5~15件（SKU）
	 *	200~500元商品推广销量随机3~10件（SKU）
	 *	500元以上商品推广销量随机1~5件（SKU）
	 * @return
	 */
	@RequestMapping("/update/sku/promotion")
	@ResponseBody
	public JsonResponse updateSkuPromotion() {
		JsonResponse jsonResponse = new JsonResponse();
		
		syncDatabaseFacade.randomUpdate();
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
}
