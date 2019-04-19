package com.jiuy.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.meta.homepage.HomeFloor;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuy.core.service.StatisticsService;
import com.jiuy.core.service.homepage.HomeFloorService;
import com.jiuy.core.service.homepage.HomeTemplateService;
import com.jiuy.core.util.file.FileUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.Statistics;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/homepage")
@Login
public class HomePageController {
	
	private static Logger logger = Logger.getLogger(HomePageController.class);
	
	private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;
	
	@Resource(name = "ossFileUtil")
	private FileUtil fileUtil;
	
	@Resource
	private HomeTemplateService homeTemplateService;
	
	@Resource
	private HomeFloorService homeFloorService;
	
	@Resource
	private StatisticsService statisticsService;
	
	@Autowired
    private ProductMapper productMapper;
	
	@Autowired
	private CouponTemplateDao couponTemplateDao;

	/**
	 * 
	 * @param sequence 0:降序 1:升序
	 * @param type
	 * @param relatedId
	 * @return
	 */
	@RequestMapping(value="/search")
	@ResponseBody
	public JsonResponse loadFloors(PageQuery pageQuery,
			@RequestParam(value = "name", required = false, defaultValue = "") String name, 
			@RequestParam(value = "sequence", required = false, defaultValue = "0") Integer sequence,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "related_id") Long relatedId) {
	  	JsonResponse jsonResponse = new JsonResponse();
		
	  	int recordCount = homeFloorService.searchCount(name, type, relatedId);
	    PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);

	    List<HomeFloorVO> list = homeFloorService.search(pageQuery, name, sequence, type, relatedId);
	    
	    Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", list);
		data.put("total", pageQueryResult);
	  	
	  	return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse addFloor(@RequestBody HomeFloor homeFloor2) {
		JsonResponse jsonResponse = new JsonResponse();
		homeFloorService.add(homeFloor2);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse removeFloor(@RequestParam(value = "id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		homeFloorService.remove(id);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateFloor(@RequestBody HomeFloor homeFloor2) {
		JsonResponse jsonResponse = new JsonResponse();
		homeFloorService.update(homeFloor2);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value="/publish", method=RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse publishFloors(@RequestParam("type") Integer type, @RequestParam("related_id") Long relatedId,
			@RequestParam(value = "homeFloorIds",defaultValue="",required=false) String homeFloorIds) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			if(!StringUtils.isEmpty(homeFloorIds)){
				String[] split = homeFloorIds.split(",");
				for (String homeFloorId : split) {
					if(StringUtils.isEmpty(homeFloorId)){
						continue;
					}
					homeFloorService.remove(Long.parseLong(homeFloorId));
				}
			}
			homeFloorService.publish(type, relatedId);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/preview")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse previewFloors(@RequestParam("type") Integer type, @RequestParam("related_id") Long relatedId) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = homeFloorService.preview(type, relatedId);
		data.put("list", list);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse addTemplate2(@RequestParam(value = "floor_id")long floorId, 
			@RequestParam(value = "content") String content, 
			@RequestParam(value = "name") String name, 
			@RequestParam(value = "imgurl") String imgUrl) {
		JsonResponse jsonResponse = new JsonResponse(); 
		HomeTemplate ht = new HomeTemplate();
		
		ht.setName(name);
		ht.setContent(content);
		ht.setImgUrl(imgUrl);
		
		homeTemplateService.add(floorId, ht); 
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value="/template/{id}/content",method = RequestMethod.GET)  
	@ResponseBody
	public JsonResponse homeContent(@PathVariable(value = "id") long floorId, 
			@RequestParam(value = "template_name") String templateName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		HomeFloorVO hf = homeFloorService.searchById(floorId);
		
		if(hf == null) {
			data.put("list", null);
            data.put("has_data", false);
		} else if(!StringUtils.equals(hf.getHomeTemplateName(), templateName)){
			data.put("list", null);
            data.put("has_data", false);
        } else {
            HomeTemplate homeTemplate = homeTemplateService.loadTemplate(hf.getNextHomeTemplateId());
            String carouseljson = homeTemplate.getContent();

            JSONArray jsonArray = StringUtils.isBlank(carouseljson) ? new JSONArray() : JSON.parseArray(carouseljson);
            
            JSONArray newJsonArray = generateNewJSON(jsonArray);

            data.put("list", newJsonArray);
            data.put("has_data", true);
		}
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 根据积分商城模板中 的 productId 或者 couponTemplateId 获取到对应数据重新封装JSON
	 */
	private JSONArray generateNewJSON(JSONArray jsonArray){
		//积分商城模板
        if(jsonArray.size()==0){
        	return jsonArray;
        }
        
        JSONObject object = jsonArray.getJSONObject(0);
    	if(!object.containsKey("jiuCoinMallType")){
    		return jsonArray;
    	}
    	
    	Integer jiuCoinMallType = (Integer) object.get("jiuCoinMallType");
		if(jiuCoinMallType == 1){	//商品
			if(object.containsKey("productList")){
				ArrayList<Product> products = new ArrayList<Product>();
				JSONArray productArray = object.getJSONArray("productList");
				for(int i=0;i<productArray.size();i++){
					JSONObject jsonObject = productArray.getJSONObject(i);
					if(jsonObject.containsKey("productId")){
						
						Long productId = jsonObject.getLong("productId");
						String productImg = jsonObject.getString("promotionImage");
						long code = 0L;
						if(jsonObject.containsKey("code")){
							code = jsonObject.getLong("code");
						}
						Product product = productMapper.getProductById(productId);
						product.setCode(code);
						if(productImg != null && productImg.trim().length() > 0){
							product.setPromotionImage(productImg);
						}
						products.add(product);
					}
				}
				Collections.sort(products, new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						if(o1.getPromotionSortTime() > o2.getPromotionSortTime()){
							return 1;
						}else if(o1.getPromotionSortTime() < o2.getPromotionSortTime()){
							return -1;
						}else return 0;
						
					}
				});
				
//				for(Product product: products){
//					System.out.println(product.getId()+"$$$"+product.getPromotionStartTime());
//					
//				}
				object.put("productList", products);
				
			}
		} else if(jiuCoinMallType == 2){	//优惠券
			if(object.containsKey("couponList")){
				ArrayList<Map<String, Object>> coupons = new ArrayList<>();
				JSONArray couponArray = object.getJSONArray("couponList");
				for(int i=0;i<couponArray.size();i++){
					JSONObject jsonObject = couponArray.getJSONObject(i);
					if(jsonObject.containsKey("couponTemplateId")){
						Long couponTemplateId = jsonObject.getLong("couponTemplateId");
						
						CouponTemplate couponTemplate = couponTemplateDao.search(couponTemplateId);
						
						HashMap<String, Object> map = new HashMap<String,Object>();
						map.put("couponImg", jsonObject.getString("couponImg"));
						map.put("couponTemplate", couponTemplate);
						
						coupons.add(map);
					}
				}
				
				object.put("couponList", coupons);
			}
		}
		return jsonArray;
	}
	
	@RequestMapping(value = "/template/image/upload", method = RequestMethod.POST)
    @AdminOperationLog
	public String uploadImageFromSession(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		String imgUrl = upload(request, response, modelMap);
		Integer pageId = Integer.parseInt(request.getParameter("pageId"));
		Long floorId = Long.parseLong(request.getParameter("floorId"));
		Long templateId = Long.parseLong(request.getParameter("templateId"));
		Integer position = Integer.parseInt(request.getParameter("position"));
	
		
		Statistics statistics = statisticsService.add(pageId,floorId, templateId, position, imgUrl);
		
		modelMap.put("code", statistics.getId() + "");
		
		return "json";
	}


	/**
	 * 上传图片到阿里云
	 */
	private String upload(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap ) {
		String storePath = null;
		String oldPath = request.getParameter("oldPath");

		try {
			if (request instanceof MultipartHttpServletRequest) {
				logger.debug("yes you are!");
				MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
				MultipartFile file = multiservlet.getFile("file");
				logger.debug("request file name :" + file.getName());
				storePath = fileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file, null);
				
				//覆盖旧路径则删除
				if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
					String key = oldPath.split("/")[oldPath.split("/").length - 1];
					fileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
				}
			} else {
				logger.debug("no wrong request!");
			}
			modelMap.addAttribute("images", storePath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("上传文件出现异常", e);
		} finally {

		}
		
		return storePath;
	}

    @RequestMapping("/floor/{id}")
    @ResponseBody
    public JsonResponse getFloor(@PathVariable("id") long id) {
        JsonResponse jsonResponse = new JsonResponse();

        HomeFloorVO hfvo = homeFloorService.search(id);

        return jsonResponse.setSuccessful().setData(hfvo);
    }
    
	@RequestMapping(value = "/template/image/code")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse getImageStatisticCode(@RequestParam("pageId") Integer pageId,
			@RequestParam("floor_id") Long floorId,
			@RequestParam("template_id") Long templateId,
			@RequestParam("position") Integer position,
			@RequestParam("element") String element) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Statistics statistics = statisticsService.add(pageId,floorId, templateId, position, element);
		
		return jsonResponse.setSuccessful().setData(statistics.getId() + "");
	}

}
