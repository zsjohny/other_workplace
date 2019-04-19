/**
 * 
 */
package com.jiuy.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.service.AdService;
import com.jiuy.core.service.BrandService;
import com.jiuy.core.service.partner.PartnerCatManageService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ad.AdEnum;
import com.jiuyuan.entity.ad.AdVo;
import com.jiuyuan.entity.brand.BrandHomePage;
import com.jiuyuan.entity.brand.Partner;
import com.jiuyuan.entity.brand.PartnerCatManage;
import com.jiuyuan.entity.brand.PartnerCategoryVO;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.BrandCategoryVo;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.Brand;

@Controller
@Login
@RequestMapping("/brand")
public class BrandController extends AbstractController {

	public final int PAGE_SIZE = 4;
	
    @Autowired
    private BrandService brandService;

	@Resource
	private AdService adService;
	
	@Resource
	private PartnerCatManageService partnerCatManageServiceImpl;

    private static final long serialVersionUID = -3347111147699383367L;
    
    /**********************************************************/
    /*****              品牌馆首页相关功能                                  *****/
    /**********************************************************/
    
	@RequestMapping("/addbrandcategorys")
    @AdminOperationLog
	public String addBrandCategoryList(@RequestBody String brandsCatory, ModelMap modelMap) {
		int success = -1;
		List<BrandCategoryVo> brandCatorygoryList = JSON.parseArray(brandsCatory, BrandCategoryVo.class);
		
		success = brandService.addBrandCategoryList(brandCatorygoryList);
		modelMap.put("success", success);
		return "json";
	}

	@RequestMapping("/deletebrandcategory")
    @AdminOperationLog
	public String deleteBrandCategory(@RequestParam("categoryId") Long categotyId, ModelMap modelMap) {
		int success = -1;
		success = brandService.deleteBrandCategory(categotyId);
		modelMap.put("success", success);
		return "json";
	}

	@RequestMapping("/brandcategorylist")
	public String getBrandCategoryListByCategoryId(@RequestParam("categoryId") Long categotyId, ModelMap modelMap) {
		List<BrandCategoryVo> voList = brandService
				.getPartnerCategoryListByCategoryId(categotyId);
		modelMap.put("brandCategoryList", voList);
		return "json";
	}

	@RequestMapping("/custombrandcategorylist")
	public String getCustomBrandCategoryList(ModelMap modelMap) {
		List<BrandCategoryVo> voList = brandService.getCustomPartnerCategoryList();
		modelMap.put("brandCategoryList", voList);
		return "json";
	}

	@RequestMapping("/searchbrand")
	public String searchBrandList(@RequestParam("sq") String item, ModelMap modelMap) {
		List<Partner> brandList = brandService.searchBrand(item);
		modelMap.put("brandList", brandList);
		return "json";
	}

    /**********************************************************/
    /*****               品牌相关功能                                            *****/
    /**********************************************************/
    /**
     * 添加新的品牌
     * 
     * @param brandName 品牌名称
     * @param pageUrl 页面URL
     * @param templateNo 模板编号
     * @param instanceNo 模板实例编号
     * @param brandEngName 品牌英文名
     * @param summary 品牌摘要
     * @param Description 品牌详细描述
     * @param modelMap
     * @return
     */
    @RequestMapping("/addbrand")
    @AdminOperationLog
    public String addNewBrand(@RequestParam("brandname") String brandName,
                              @RequestParam(value = "brandenname", required = false) String brandEngName,
                              @RequestParam("summary") String summary,
                              @RequestParam("description") String description,
                              @RequestParam(value="pageurl",defaultValue="") String pageUrl,
                              @RequestParam("iconurl") String iconUrl,
                              @RequestParam(value = "templateNo", defaultValue = "1", required = false) int templateNo,
                              @RequestParam(value = "instanceNo", defaultValue = "0", required = false) int instanceNo,
                              ModelMap modelMap) {
        // 添加品牌信息
        Brand newBrand = new Brand();
        long currentTime = new Date().getTime();
        newBrand.setCreateTime(currentTime);
        newBrand.setDescription(description);
        newBrand.setEngName(brandEngName);
        newBrand.setIconUrl(iconUrl);
        newBrand.setName(brandName);
        newBrand.setOrderIndex(0);
        newBrand.setStatus(0);
        newBrand.setSummary(summary);
        newBrand.setUpdateTime(currentTime);
        // 添加主页信息
        BrandHomePage homePage = new BrandHomePage();
        homePage.setCreateTime(currentTime);
        homePage.setEditable(0);
        homePage.setPageUrl(pageUrl);
        homePage.setStatus(0);
        homePage.setTemplateInstanceId(instanceNo);
        homePage.setUpdateTime(currentTime);
        long brandId = brandService.addNewBrand(newBrand,homePage);
        modelMap.put("brandid", brandId);
        return "json";
    }
    
    /*************************************************************************************
     * 暂时给前台一个页面跳转，调试页面
     * @param modelMap
     * @return
     */
    @RequestMapping("/brandpage")
    public String brandPage(ModelMap modelMap) {
		List<AdVo> voList = adService.getAdLsit(AdEnum.BRANDPAGEAD.getType());
		modelMap.put("adList", voList);
		List<BrandCategoryVo> brandList = brandService.getAllBrandCategory();
		modelMap.put("brandList", brandList);
    	return "page/backend/brand";
    }
    
    @RequestMapping("/brandclassify")
    @AdminOperationLog
    public String brandClassify(ModelMap modelMap) {
    	return "page/backend/brandclassify";
    }

    /**************************************************************************************/
    
    /**
     * 获取主页设置的信息
     * @author Jeff.Zhan
     * @param partnerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/gethomesettinginfo")
    public String gethomesettinginfo(long partnerId, ModelMap modelMap) {
    	List<Map<String, Object>> info = brandService.getHomeSettingInfo(partnerId);
    	modelMap.put("info", info);
    	return "page/backend/brandindexsetting";
    }
    
    /**
     * 激活主页url
     * @param partnerId
     * @param brandUrl
     * @param template
     * @param modelMap
     * @return
     */
    @RequestMapping("/activebrandurl")
    @AdminOperationLog
    public String activeBrandUrl(long partnerId, ModelMap modelMap) {
    	int success = 0;
    	success = brandService.activeBrandUrl(partnerId);
    	modelMap.put("success", success);
    	return "json";
    }
    
    /**
     * 使品牌主页url失效
     * @param partnerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/deactivebrandurl")
    @AdminOperationLog
    public String deactiveBrandUrl(long partnerId, ModelMap modelMap) {
    	int success = 0;
    	success = brandService.deactiveBrandUrl(partnerId);
    	modelMap.put("success", success);
    	return "json";
    }
    
    /**
     * 存在--返回品牌馆自定义主页;不存在--返回错误页面 
     * @author Jeff.Zhan
     * @param brandUrl
     * @return
     */
    @RequestMapping(value="/yujiejie/{brandUrl}",method = RequestMethod.GET)  
    public String getTemplateUrl(@PathVariable("brandUrl") String brandUrl, ModelMap modelMap){ 
    	if(brandUrl.equals("favicon")) {
    		return "page/backend/errorpage";
    	}
    	String template = brandService.getTemplateUrl(brandUrl);
    	if(template.equals("") || template.equals("favicon")) {
    		return "page/backend/errorpage";
    	}
    	modelMap.put("template", template);
    	return template;  
    } 

    
    /**
     * 品牌馆-品牌管理-获取某个商家的信息
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value="/getpartnerbyid")
    public String getPartnerById(long id, ModelMap modelMap) {
    	List<Map<String, Object>> partnerInfo = brandService.getPartnerById(id);
    	modelMap.put("partnerInfo", partnerInfo);
    	return "json";
    }
    
    /**
     * 品牌馆-品牌管理-模糊查询商家
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
    @RequestMapping(value="/getpartnerbyname")
    public String getPartnerByName(String name, ModelMap modelMap) {
    	List<Map<String, Object>> brandManageInfo = brandService.getPartnerByName(name);
    	modelMap.put("brandManageInfo", brandManageInfo);
    	return "json";
    }
    
    /**
     * 品牌馆-品牌管理-删除商家品牌 
     * @author Jeff.Zhan
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("/deletebrandpartner")
    public String deleteBrandPartner(long id, ModelMap modelMap) {
    	int success = brandService.deleteBrandPartner(id);
    	modelMap.put("success", success);
    	return "json";
    }
    
    
    /**********************************************************/
    /*****              品牌馆优化+实现                                        *****/
    /**********************************************************/
    
    /**
     * 品牌馆首页-页面
     * @return
     */
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String brandHome() {
    	return "page/backend/brandHomePage";
    }
    
    /**
     * 品牌馆首页-数据
     * @return
     */
    @RequestMapping(value="/loadhome", method=RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadHome() {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
//    	List<List<PartnerCategoryVO>> classifies = partnerCategoryServiceImpl.loadHomeClassifies();
    	List<List<PartnerCategoryVO>> classifies = null;
    	List<AdVo> voList = adService.getAdLsit(AdEnum.BRANDPAGEAD.getType());
    	List<PartnerCatManage> partnerCats = partnerCatManageServiceImpl.loadHomeCat();
    	
    	data.put("classifies", classifies);
    	data.put("voList", voList);
    	data.put("partnerCats", partnerCats);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 品牌馆首页-模糊查询
     * @param name
     * @return
     */
    @RequestMapping("/searchpartner")
    @ResponseBody
    public JsonResponse searchPartner(@RequestParam(value="name", defaultValue="", required=false)String name) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	List<Partner> data = brandService.searchBrand(name);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    /**
     * 品牌馆管理-品牌管理-页面
     * @return
     */
    @AdminOperationLog
    @RequestMapping(value="/manage", method=RequestMethod.GET)
    public String brandManager() {
    	return "page/backend/brandmanage";
    }
    
     /**
      * 品牌馆管理-品牌管理-获取页面信息
      * @param name
      * 		品牌馆中文名
      * @param engName
      * 		品牌馆英文名
      * @return
      */
    @RequestMapping(value="/search", method=RequestMethod.GET)
    @ResponseBody
    public JsonResponse search(@RequestParam(value="page", required=false, defaultValue="1")int page, 
    		@RequestParam(value="name", required=false, defaultValue="")String name,
    		@RequestParam(value="eng_name", required=false, defaultValue="")String engName) {
    	Map<String, Object> data = new HashMap<String, Object>();
    	JsonResponse jsonResponse = new JsonResponse();
    	PageQuery query = new PageQuery(page, PAGE_SIZE);
    	PageQueryResult pageQueryResult = new PageQueryResult();
    	BeanUtils.copyProperties(query, pageQueryResult);
    	 
    	List<PartnerVO> list = brandService.search(query, name, engName);
    	int count = brandService.searchCount(name, engName);
    	List<PartnerCatManage> partnerCats = partnerCatManageServiceImpl.loadHomeCat();
    	
    	pageQueryResult.setRecordCount(count);
    	data.put("data", list);
    	data.put("total", pageQueryResult);
    	data.put("partnerCats", partnerCats);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 品牌馆-品牌管理-新增品牌
     * @param paVo
     * @return
     */
    @AdminOperationLog
    @RequestMapping(value="/add", method=RequestMethod.POST)
    @ResponseBody
    public JsonResponse addBrand(@RequestBody PartnerVO paVo) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = brandService.addBrand(paVo);
    	
    	return jsonResponse.setSuccessful().setData(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    }

    /**
     * 品牌馆-品牌管理-更新品牌
     * @param paVo
     * @return
     */
    @AdminOperationLog
    @RequestMapping(value="/update", method=RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateBrand2(@RequestBody PartnerVO paVo) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = brandService.updateBrand(paVo);
    	
    	return jsonResponse.setSuccessful().setData(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    }
    
    /**
     * 品牌管理-删除商家品牌 
     * @param ids
     * @return
     */
    @AdminOperationLog
    @RequestMapping(value="/remove", method=RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteBrandPartner(@RequestParam(value="ids")long[] ids) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = brandService.removeBrand(ids);	
    	
    	return jsonResponse.setSuccessful().setData(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    }
    
    /**
     * 品牌主页管理-添加品牌馆自定义主页
     */
    @AdminOperationLog
    @RequestMapping(value="/addUrl", method=RequestMethod.POST) 
    @ResponseBody
    public JsonResponse addPartnerUrl(@RequestParam(value="partner_id")long partnerId, @RequestParam(value="url")String url) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = brandService.addUrl(partnerId, url);
    	
    	return jsonResponse.setSuccessful().setData(success);
    }
    
}
