/**
 * 后台商品管理使用的控制器
 */
package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.service.AdService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.util.file.FileUtil;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ad.AdEnum;
import com.jiuyuan.dao.mapper.supplier.WhitePhoneMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ad.AdVo;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;

/**
 * @author LWS
 */
@Controller
@Login
public class ManagementController extends AbstractController {
	private static Logger logger = Logger.getLogger(ManagementController.class);
	private static final long serialVersionUID = -4900438947185909583L;

	//
	@Autowired
	private WhitePhoneService whitePhoneService;
	@Autowired
	WhitePhoneMapper whitePhoneMapper;

	/********************
	 * PRODUCT CLOTHES TYPE NUMBER
	 **********************************/
	private final int CLOTHES_REPEAT = -1;
	private final int CLOTHES_NOT_REPEAT = 0;
	private final int CLOTHES_NAME_ERROR = 1;

	/**
	 * 是否使用新的产品模型
	 */
	// private static final boolean _VERSION_SWITCH_TO_NEW = Boolean.TRUE;

	@Resource(name = "ossFileUtil")
	private FileUtil fileUtil;

	@Autowired
	private ProductService productService;

	// @Autowired
	// private PropertyService propertySer;
	//
	// @Autowired
	// private ProductServiceV1 prodService;

	@Autowired
	private AdService adService;

	@Autowired
	private GlobalSettingService globalSettingService;

	/**
	 * 获取系统中的服装分类 创建日期: 2015/07/05
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/classifications")
	public String getClassificationDefinition(HttpServletRequest request, ModelMap modelMap) {
		/*******************
		 * use new classification definition
		 *************************/
		List<Category> categoryDefs = productService.getCategoryDefinitions(true);
		List<Category> categories = new ArrayList<Category>();

		for (Category category : categoryDefs) {
			if (category.getCategoryType() == 0) {
				categories.add(category);
			}
		}

		modelMap.addAttribute("classifications", categories);
		return "json";
	}

	/**
	 * 获取系统中的字典定义:风格定义 创建日期: 2015/07/05
	 * 
	 * @param groupid
	 * @param modelMap
	 * @return
	 */
	// @RequestMapping(value = "/getstylelist")
	// public String getStyleList(HttpServletRequest request, ModelMap modelMap)
	// {
	// List<Dictionary> dicts = new ArrayList<Dictionary>();
	// if (_VERSION_SWITCH_TO_NEW) {
	// Map<String, ProductPropValue> ppMap = propertySer.getPropertyValueMap();
	// Collection<ProductPropValue> values = ppMap.values();
	// Collection<ProductPropValue> elementValues =
	// getPropertyByNameId(PropertyName.STYLE.getValue(), values);
	// PropertyTransformUtil.transformProperty2Dictionary(elementValues, dicts);
	// } else {
	// dicts = prodService.getDictionaries(STYLE_ID);
	// }
	// modelMap.addAttribute("dictionaries", dicts);
	// return "json";
	// }

	/**
	 * 获取系统中的字典定义:时尚元素定义 创建日期: 2015/07/05
	 * 
	 * @param groupid
	 * @param modelMap
	 * @return
	 */
	// @RequestMapping(value = "/getelementlist")
	// public String getElementList(HttpServletRequest request, ModelMap
	// modelMap) {
	// List<Dictionary> dicts = new ArrayList<Dictionary>();
	// if (_VERSION_SWITCH_TO_NEW) {
	// Map<String, ProductPropValue> ppMap = propertySer.getPropertyValueMap();
	// Collection<ProductPropValue> values = ppMap.values();
	// Collection<ProductPropValue> elementValues =
	// getPropertyByNameId(PropertyName.ELEMENT.getValue(), values);
	// PropertyTransformUtil.transformProperty2Dictionary(elementValues, dicts);
	// } else {
	// dicts = prodService.getDictionaries(ELEMENT_ID);
	// }
	// modelMap.addAttribute("dictionaries", dicts);
	// return "json";
	// }

	// private Collection<ProductPropValue> getPropertyByNameId(long id,
	// Collection<ProductPropValue> values) {
	// Collection<ProductPropValue> subSet = new ArrayList<ProductPropValue>();
	// if (null != values) {
	// for (ProductPropValue ppv : values) {
	// if (ppv.getPropertyNameId() == id) {
	// subSet.add(ppv);
	// }
	// }
	// return subSet;
	// }
	// return null;
	// }

	// @AdminOperationLog
	// @RequestMapping("/classify")
	// public String test(HttpServletRequest request) {
	// return "page/backend/classifies";
	// }

	/**
	 * 获取系统中的字典定义:地址定义 创建日期: 2015/07/05
	 * 
	 * @param groupid
	 * @param modelMap
	 * @return
	 */
	// @RequestMapping(value = "/getaddresslist")
	// public String getAddressList(HttpServletRequest request, String parentid,
	// ModelMap modelMap) {
	// if (StringUtils.isBlank(parentid)) {
	// parentid = "";
	// }
	// List<TreeDictionary> dicts = prodService.getTreeDictionaries(ADDRESS_ID,
	// parentid);
	// modelMap.addAttribute("dictionaries", dicts);
	// return "json";
	// }

	/***************************** 加载页面请求 *************************************/
	@RequestMapping(value = "/getmanagementpage", produces = { "application/json;charset=UTF-8" })
	@AdminOperationLog
	public String getManagementFromSession(HttpServletRequest request, ModelMap modelMap) {
		// return "page/backend/index";
		return "page/change_pwd";
	}

	@RequestMapping(value = "/loginpage")
	@NoLogin
	@AdminOperationLog
	public String getLoginPage(HttpServletRequest request, ModelMap modelMap) {
		return "page/login";
	}

	/**
	 * 跳转到审核用户搜索页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	// @RequestMapping(value = "/getorderusersearch", produces = {
	// "application/json;charset=UTF-8" })
	// public String getOrderUserSearch(HttpServletRequest request, ModelMap
	// modelMap) {
	// return "page/backend/search";
	// }
	//
	// @RequestMapping(value = "/getmanagemain", produces = {
	// "application/json;charset=UTF-8" })
	// public String getManagementMain(HttpServletRequest request, ModelMap
	// modelMap) {
	// return "page/productmain";
	// }

	@RequestMapping("/adview")
	@AdminOperationLog
	@Deprecated
	public String getAdList(HttpServletRequest request, ModelMap modelMap) {
		List<AdVo> voList = adService.getAdLsit(AdEnum.MAIN_BANNER.getType());
		GlobalSetting setting = globalSettingService.getItem(GlobalSettingName.NAV_BAR);
		List<GlobalSetting> globalSettings = new ArrayList<GlobalSetting>();
		globalSettings.add(setting);

		modelMap.put("adList", voList);
		modelMap.put("setting", globalSettings);

		JSONArray navigationBar = globalSettingService.getJsonArray(GlobalSettingName.NAV_BAR);
		JSONArray hotCategory = globalSettingService.getJsonArray(GlobalSettingName.INDEX_CATEGORY_MODULE);
		modelMap.put("navigationBar", navigationBar);
		modelMap.put("hotCategory", hotCategory);
		return "page/backend/ad";
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// public List<Map<String,Object>> String2Json(String jsonArrayData){
	// JSONArray jsonArray = JSONArray.fromObject(jsonArrayData);
	// List<Map<String,Object>> mapListJson = (List)jsonArray;
	// return mapListJson;
	// }

	@RequestMapping("/chkproductclothesnum")
	public String chkProductClothesNum(HttpServletRequest request, String clothesNum, ModelMap modelMap) {
		int clothesNumRepeat = CLOTHES_NOT_REPEAT;
		if (Pattern.matches("[a-zA-Z0-9]{1,25}", clothesNum)) {
			List<Integer> listName = null;
			listName = productService.chkProductClothesNum(clothesNum);
			// 如果搜到相同款号的产品,那么标识符置为-1,表示添加失败
			if (listName.size() > 0)
				clothesNumRepeat = CLOTHES_REPEAT;
		} else {
			clothesNumRepeat = CLOTHES_NAME_ERROR;
		}
		modelMap.put("clothesNumRepeat", clothesNumRepeat);
		return "json";
	}

	@RequestMapping("/adlist")
	public String getAdListData(Integer adType, ModelMap modelMap) {

		List<AdVo> voList = adService.getAdLsit(adType == null ? AdEnum.MAIN_BANNER.getType() : adType);
		modelMap.put("adList", voList);
		return "json";
	}

	@RequestMapping("/adcreate")
	@AdminOperationLog
	public String createAdListData(HttpServletRequest request, @RequestBody String adstr, ModelMap modelMap) {
		List<AdVo> adList = JSON.parseArray(adstr, AdVo.class);
		for (AdVo vo : adList) {
			if (vo.getAdOrder() == null) {
				vo.setAdOrder(0);
			}

			if (vo.getPartenerId() == null) {
				vo.setPartenerId(0L);
			}
		}
		adService.createAd(adList);
		modelMap.put("adList", adList);
		return "json";
	}

	@RequestMapping("/deletead")
	@AdminOperationLog
	public String deleteAd(HttpServletRequest request, Long id, ModelMap modelMap) {

		int success = adService.deleteAd(id);
		modelMap.put("success", success);

		return "json";
	}

	@RequestMapping("/updatead")
	@AdminOperationLog
	public String updateAd(HttpServletRequest request, AdVo vo, ModelMap modelMap) {
		int success = adService.updateAd(vo);
		modelMap.put("success", success);

		return "json";
	}

	/************************************************************************/

	/**
	 * 在修改产品页面 根据商品id 获得其所属分类、上架年份、季节、品牌 author:Jeff.Zhan
	 * 
	 * @param productId
	 * @return
	 */
	@Deprecated
	@RequestMapping("/productinformation")
	public String getProductInformation(HttpServletRequest request, long productid, ModelMap modelMap) {
		List<Map<String, Object>> categories = productService.getCategoriesInfo(productid);
		Map<String, Object> info = productService.getPropertyInfo(productid);
		List<String> description = productService.getDescription(productid);
		List<String> remark = productService.getRemark(productid);
		List<Product> products = productService.productOfIds(CollectionUtil.createList(productid));

		modelMap.put("categories", categories);
		modelMap.put("info", info);
		modelMap.put("remark", remark);
		modelMap.put("description", description);
		modelMap.put("product", products.size() > 0 ? products.get(0) : new Product());

		return "json";
	}

}
