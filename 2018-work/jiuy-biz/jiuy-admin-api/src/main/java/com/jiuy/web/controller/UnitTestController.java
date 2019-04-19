package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.dao.UserCoinDao;
import com.jiuy.core.dao.mapper.BinaryDataDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.PropertyNameMapper;
//import com.jiuy.core.meta.order.Order;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.ExpressSupplierService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.core.service.task.AfterSalesFreeze;
import com.jiuy.core.service.task.CategorySaleDailyReport;
import com.jiuy.core.service.task.CouponCountAdjustJob;
import com.jiuy.core.service.task.NotificationDisposingJob;
import com.jiuy.core.service.task.OrderDisposingJob;
import com.jiuy.core.service.task.OrderSuccessJob;
import com.jiuy.core.service.task.QianMiTokenJob;
import com.jiuy.core.service.task.StoreDailyReport;
import com.jiuy.core.service.task.StoreOrderMergeJob;
import com.jiuy.core.service.task.StoreOrderSuccessJob;
import com.jiuy.core.service.task.WdtLogisticsJob;
import com.jiuy.core.service.task.WdtOrderJob;
import com.jiuy.core.service.template.TemplateService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuy.web.controller.util.QrcodeUtils;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuy.web.delegate.OrderNewDelegator;
import com.jiuyuan.entity.BinaryData;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.service.common.GroundConditionRuleService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.GetuiUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/test")
@Login
@SuppressWarnings("unused")
public class UnitTestController {
	
	private static final Logger logger = LoggerFactory.getLogger("WDT");

	@Resource
	private TemplateService templateServiceImpl;
	
	@Autowired
	private OrderOldService orderNewService;
	
	@Resource
	private OrderNewFacade orderNewFacade;
	
    @Autowired
    private ErpDelegator erpDelegator;
	
    @Autowired
    private GlobalSettingService glService;
    
    @Autowired
    private ProductSKUService productSKUService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private ExpressSupplierService expressSupplierService;
    
	@Autowired
	private PropertyNameMapper propertyNameMapper;
	
	@Autowired
	private GlobalSettingService globalSettingService;
   
	
	@Autowired
	private OrderDisposingJob orderDisposingJob;
	
	@Autowired
	private CouponUseLogDao couponUseLogDao;
	
	@Autowired
	private OrderNewDelegator orderNewDelegator;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private BinaryDataDao binaryDataDao;
	
	@Autowired
	private WdtLogisticsJob wdtLogisticsJob;
	
	@Autowired
	private QianMiTokenJob qianMiTokenJob;

	@Autowired
	private WdtOrderJob wdtOrderJob;

	@Autowired
	private YunXinSmsService yunXinSmsService;
	
	@Autowired
	private NotificationDisposingJob notificationDisposingJob;
	
	@Autowired
	private CouponCountAdjustJob couponCountAdjustJob;
	
	@Autowired
	private UserCoinDao userCoinDao;
	
	@Autowired
	private OrderSuccessJob orderSuccessJob;
	
	@Autowired
	private AfterSalesFreeze afterSalesFreeze; 
	
	@Autowired
	private StoreDailyReport storeDailyReport;
	
	
	@Autowired
	private CategorySaleDailyReport categorySaleDailyReport;
	
	@Autowired
	private StoreOrderMergeJob storeOrderMergeJob;
	
	@Autowired
	private StoreOrderSuccessJob storeOrderSuccessJob;
	
	@Autowired
	private StoreOrderSuccessJob couponNotificationJob;
	
	@Autowired
	private GroundConditionRuleService roundConditionRuleService;
	
	@RequestMapping("/test")
	public String test(HttpServletResponse response, ModelMap modelMap) throws IOException, ParseException {
		wdtOrderJob.execute();
		return "json";
	}
	@RequestMapping("/testU")
	public UserTimeRule getUserTimeRule(HttpServletResponse response, ModelMap modelMap) throws IOException, ParseException {
		UserTimeRule userTimeRule = roundConditionRuleService.getUserTimeRule();
		return userTimeRule;
	}
	
	
	@RequestMapping("/test2")
	public String test2(HttpServletResponse response, ModelMap modelMap) throws IOException {
		wdtLogisticsJob.execute();
		modelMap.put("ss", "ss");
		return "json";
	}
	
	@RequestMapping("/test3")
	public void test3(HttpServletResponse response, ModelMap modelMap) throws Exception {
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("title", "这个是标题");
	    jsonObject.put("abstracts", "这个是描述");
	    jsonObject.put("url", "这个是url");
	    jsonObject.put("image", "这个是图片地址");
	    jsonObject.put("pushTime", System.currentTimeMillis());
	    String cid = "9dee8245259f323532278ded37c57750";
	    GetuiUtil.pushGeTui(CollectionUtil.createList(cid), jsonObject);
	}
	
	@RequestMapping("/test4")
	@ResponseBody
	public String test4() throws Exception {
		orderDisposingJob.execute();
		return "sss";
	}
	
	@RequestMapping("/test5")
	@ResponseBody
	public void test5() {
		List<Long> orderNos = Arrays.asList(new Long[]{65L});
		List<OrderNew> orderNews = orderNewService.orderNewsOfOrderNos(orderNos);
		orderNewDelegator.retunCoupon(orderNews);
	}
	
	@RequestMapping("/test6")
	@ResponseBody
	public void test6(HttpServletResponse response, HttpServletRequest request ) {
		QrcodeUtils.getFile(request, response, "http://yujiejie.com", "ss", 1180, 1181);
	}
	
	@RequestMapping("/test7")
	@ResponseBody
	public JsonResponse test7() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		BinaryData binaryData = binaryDataDao.getById(3L);
		data.put("data", binaryData);
		data.put("size", binaryData.getContent().length);
		
		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping("/test8")
	@ResponseBody
	public void Token() {
		JSONArray params = new JSONArray();
		params.add("test");
    	yunXinSmsService.send("18657970685", params, 8224);
	}
	
	@RequestMapping("/test9")
	@ResponseBody
	public void test9() {
		notificationDisposingJob.execute();
	}
	
	@RequestMapping("/test10")
	@ResponseBody
	public void test10() throws ParseException {
		couponCountAdjustJob.execute();
	}
	
	@RequestMapping("/test11")
	@ResponseBody
	public void test11() {
		orderSuccessJob.execute();
	}
	
	@RequestMapping("/test12")
	@ResponseBody
	public void test12() {
		afterSalesFreeze.execute();
	}
	
	@RequestMapping("/test13")
	@ResponseBody
	public void test13() {
		storeDailyReport.execute();
	}
	
	@RequestMapping("/test14")
	@ResponseBody
	public void test14() {
		storeOrderMergeJob.execute();
	}
	
	@RequestMapping("/test15")
	@ResponseBody
	public void test15() {
		storeOrderSuccessJob.execute();
	}
	
	
	@RequestMapping("/test16")
	@ResponseBody
	public void test16() {
		couponNotificationJob.execute();
	}
}


