package com.jiuy.supplier.modular.platformNotice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.SupplierPlacard;
import com.jiuyuan.service.common.IPlacardApplyService;
import com.jiuyuan.service.common.ISupplierPlacardService;
import com.jiuyuan.service.common.PlacardReadService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 所有公告控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:14:58
 */
@Controller
@RequestMapping("/allNotice")
public class AllNoticeController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AllNoticeController.class);


    private String PREFIX = "/platformNotice/allNotice/";

    
    @Autowired
    private IPlacardApplyService placardApplyService;
    @Autowired
	private PlacardReadService placardReadService;
    
    @Autowired
    private ISupplierPlacardService supplierPlacardService;
    
    /**
     * 获取供应商公告报名申请统计信息
     */
    @RequestMapping(value = "/getPlacardApplyStatisticsInfo")
   	@ResponseBody
   	public JsonResponse getPlacardApplyStatisticsInfo() {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			
   			Map<String,String> statisticsInfoMap = new HashMap<String,String>();
   			int  applyTotalCount = placardApplyService.getApplyCountByState(-1,supplierId);//报名单总数
   			int  waitAuditCount = placardApplyService.getApplyCountByState(PlacardApply.state_wait_audit,supplierId);//待审核总数
   			int  auditYesCount = placardApplyService.getApplyCountByState(PlacardApply.state_audit_yes,supplierId);//已通过总数
   			int  auditNoCount = placardApplyService.getApplyCountByState(PlacardApply.state_audit_no,supplierId);//未通过总数
   			statisticsInfoMap.put("applyTotalCount", String.valueOf(applyTotalCount));//报名单总数
   			statisticsInfoMap.put("waitAuditCount", String.valueOf(waitAuditCount));//待审核总数
   			statisticsInfoMap.put("auditYesCount", String.valueOf(auditYesCount));//已通过总数
   			statisticsInfoMap.put("auditNoCount", String.valueOf(auditNoCount));//未通过总数
   			return jsonResponse.setSuccessful().setData(statisticsInfoMap);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError("获取供应商活动：" + e.getMessage());
   		}
   	}
    
    /**
     * 获取供应商公告统计信息
     */
    @RequestMapping(value = "/getSupplierPlacardStatisticsInfo")
   	@ResponseBody
   	public JsonResponse getSupplierPlacardStatisticsInfo() {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			
   			Map<String,String> statisticsInfoMap = new HashMap<String,String>();
   			int  noReadCount = supplierPlacardService.getNoReadCount(supplierId);//未读公告总数
   			statisticsInfoMap.put("noReadCount", String.valueOf(noReadCount));
   			return jsonResponse.setSuccessful().setData(statisticsInfoMap);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError("获取供应商活动：" + e.getMessage());
   		}
   	}
    
    /**
     * 供应商公告申请
     */
    @RequestMapping(value = "/supplierGetPlacardApply")
    @ResponseBody 
    public JsonResponse supplierGetPlacardApply(
    		@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId,//公告ID
    		@RequestParam(value = "applyContent",required = false,defaultValue = "") String applyContent//申请说明
		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			
			
			
			logger.error("供应商公告申请，supplierPlacardId:"+supplierPlacardId+",applyContent:"+applyContent);
			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			String supplierName =	supplier.getBusinessName();
			long brandId = supplier.getBrandId();
			String brandName =	supplier.getBrandName();
			placardApplyService.supplierGetPlacardApply(supplierPlacardId,applyContent,supplierId,supplierName,brandId,brandName);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商公告申请:"+e.getMessage());
    		return jsonResponse.setError( e.getMessage());
		}
    }
    
    /**
     * 查看供应商公告
     */
    @RequestMapping(value = "/showSupplierPlacardInfo")
   	@ResponseBody
   	public JsonResponse showSupplierPlacardInfo(
   			@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId//公告ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
   			
   			//1、组装公告信息
   			Map<String,String> map = new HashMap<String,String>();
   			SupplierPlacard  supplierPlacard = supplierPlacardService.getSupplierPlacardInfo(supplierPlacardId);
   			map.put("supplierPlacardId", String.valueOf(supplierPlacard.getId()));
   			map.put("type", String.valueOf(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
   			map.put("title", supplierPlacard.getTitle());//公告标题
   			map.put("content", supplierPlacard.getContent());//公告内容
   			map.put("applyEndTime", DateUtil.parseLongTime2Str3(supplierPlacard.getApplyEndTime()));//报名结束时间
   			map.put("applyState", String.valueOf(supplierPlacard.buildApplyState()));//报名活动状态:0报名进行中、1报名已经结束、2无
   			map.put("applyStateName", supplierPlacard.buildApplyStateName());//报名活动状态名称:0报名进行中、1报名已经结束、2无
   			map.put("placardState", String.valueOf(supplierPlacard.getState()));//公告状态:1已发布、2通知中、3已停止
   			map.put("placardStateName", supplierPlacard.buildStateName());//公告状态:1已发布、2通知中、3已停止
   			map.put("publishTime", DateUtil.parseLongTime2Str3(supplierPlacard.getPublishTime()));//发布时间
    		
   		    //2、组装公告申请信息
   			PlacardApply placardApply = placardApplyService.getPlacardApply(supplierPlacardId,supplierId);
   			if(placardApply!=null){
   				map.put("placardApplyId",String.valueOf(placardApply.getId()));//报名信息ID
   				map.put("placardApplyCoutent",placardApply.getCoutent() );//报名信息
   	   			map.put("auditState", String.valueOf(placardApply.getState()));//报名状态\公告申请状态：1待审核、2已通过、3已拒绝
   	   			map.put("auditStateName", placardApply.buildStateName());//报名状态\公告申请状态：1待审核、2已通过、3已拒绝
   	   			map.put("titleNote",placardApply.getTitleNote() );//审核说明/未通过原因
   			}else{
   				map.put("placardApplyId",String.valueOf(0));//报名信息ID
   				map.put("placardApplyCoutent","");//报名信息
   				map.put("auditState", "");//报名状态\公告申请状态：1待审核、2已通过、3已拒绝
   				map.put("auditStateName", "");//报名状态\公告申请状态：1待审核、2已通过、3已拒绝
   				map.put("titleNote","");//审核说明/未通过原因
   				
   			}
   			
   			//3、添加阅读记录
   			placardReadService.setPlacardIsRead(supplierPlacardId,supplierId);
   			
   			//3、增加公告阅读记录数
   			supplierPlacardService.increaseReadCount(supplierPlacardId);
   			
   			return jsonResponse.setSuccessful().setData(map);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}
    
    /**
     * 供应商公告申请列表
     */
    @RequestMapping(value = "/supplierGetPlacardApplyList")
    @ResponseBody 
    public Object supplierGetPlacardApplyList(
   			@RequestParam(value = "title",required = false,defaultValue = "") String title,//公告标题
   			@RequestParam(value = "auditState",required = false,defaultValue = "-1") int auditState,//公告申请状态：0待审核、1已通过、2已拒绝
   			@RequestParam(value = "applyTimeBegin",required = false,defaultValue = "0") long applyTimeBegin,//申请时间开始
   			@RequestParam(value = "applyTimeEnd",required = false,defaultValue = "0") long applyTimeEnd//申请时间结束
		){
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			logger.info("供应商获取我的报名单列表：supplierGetPlacardApplyList：title："+title
					+",auditState:"+auditState+",applyTimeBegin:"+applyTimeBegin+",applyTimeEnd:"+applyTimeEnd);
			List<PlacardApply> placardApplyList  = placardApplyService.supplierGetPlacardApplyList(page,title,auditState,applyTimeBegin,applyTimeEnd);
			
			page.setRecords(buildPlacardApplyMapList(placardApplyList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商活动搜索列表:"+e.getMessage());
    		throw new RuntimeException("供应商活动搜索列表:"+e.getMessage());
		}
    }
    
   
    
    
    
    /**
     * 通知获取未读公告
     * 说明：获取最新五条通知供应商中未读公告
     */
    @RequestMapping(value = "/adviceGetNoReadPlacard")
    @ResponseBody 
    public JsonResponse adviceGetNoReadPlacard(){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			Map<String,String> data  = supplierPlacardService.getNoReadPlacard(supplierId);
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商活动搜索列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
	/**
     * 供应商公告列表
     */
    @RequestMapping(value = "/supplierGetPlacardList")
    @ResponseBody 
    public Object supplierGetPlacardList(
   			@RequestParam(value = "title",required = false,defaultValue = "") String title,//公告标题
   			@RequestParam(value = "readState",required = false,defaultValue = "-1") int readState,//阅读状态:-1全部0未读、1已读
   			@RequestParam(value = "applyState",required = false,defaultValue = "-1") int applyState,//报名活动状态:-1全部、0报名进行中、1报名已经结束
   			@RequestParam(value = "type",required = false,defaultValue = "-1") int type,//公告类型:-1全部0普通公告、1报名公告
   			@RequestParam(value = "publishTimeBegin",required = false,defaultValue = "0") long publishTimeBegin,//发布时间开始
   			@RequestParam(value = "publishTimeEnd",required = false,defaultValue = "0") long publishTimeEnd//发布时间结束
//   			@RequestParam(value = "content",required = false,defaultValue = "") String content,//公告内容
//   			@RequestParam(value = "state",required = false,defaultValue = "-1") int state,//公告状态:-1全部，0编辑中、1已发布、2通知中、3已停止
   			
   			
   			
		){
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			
			List<SupplierPlacard> supplierPlacardList  = supplierPlacardService.supplierGetPlacardList(page,title,type, publishTimeBegin, publishTimeEnd, applyState,readState,supplierId);
			
			page.setRecords(buildSupplierPlacardMapList(supplierPlacardList,supplierId));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商活动搜索列表:"+e.getMessage());
    		throw new RuntimeException("供应商活动搜索列表:"+e.getMessage());
		}
    }
		
    
    private List<Map<String, String>> buildPlacardApplyMapList(List<PlacardApply> placardApplyList) {
    	List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
    	for(PlacardApply placardApply: placardApplyList){
    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
    		supplierPlacardMap.put("placardApplyId", String.valueOf(placardApply.getId()));//公告申请ID
    		supplierPlacardMap.put("placardId", String.valueOf(placardApply.getPlacardId()));//公告ID
    		supplierPlacardMap.put("placardTitle", placardApply.getPlacardTitle());//公告标题
    		supplierPlacardMap.put("applyCoutent", placardApply.getCoutent());//报名信息
    		supplierPlacardMap.put("titleNote", placardApply.getTitleNote());//审核说明
    		supplierPlacardMap.put("state", String.valueOf(placardApply.getState()));//公告申请状态：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("stateName", placardApply.buildStateName());//公告申请状态名称：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("auditTime", DateUtil.parseLongTime2Str3(placardApply.getAuditTime()));//审核时间
    		supplierPlacardMap.put("applyTime", DateUtil.parseLongTime2Str3(placardApply.getApplyTime()));//申请时间
    		supplierPlacardMapList.add(supplierPlacardMap);
    	}
//    	logger.info("supplierPlacardMapList:"+JSON.toJSONString(supplierPlacardMapList));
		return supplierPlacardMapList;
	}
    
    
	private List<Map<String,String>> buildSupplierPlacardMapList(List<SupplierPlacard> supplierPlacardList,long supplierId) {
		    	List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
		    	for(SupplierPlacard supplierPlacard: supplierPlacardList){
		    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
		    		supplierPlacardMap.put("supplierPlacardId", String.valueOf(supplierPlacard.getId()));//公告ID
		    		supplierPlacardMap.put("title", supplierPlacard.getTitle());//公告标题
		    		supplierPlacardMap.put("content", supplierPlacard.getContent());//公告内容
		    		supplierPlacardMap.put("type", String.valueOf(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
		    		supplierPlacardMap.put("typeName", supplierPlacard.buildTypeName(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
		    		supplierPlacardMap.put("state", String.valueOf(supplierPlacard.getState()));//公告状态:0已发布、1通知中、2已停止
		    		supplierPlacardMap.put("stateName", supplierPlacard.buildStateName());//公告状态:0编辑中、1已发布、2通知中、3已停止
		    		supplierPlacardMap.put("notifyCount", String.valueOf(supplierPlacard.getNotifyCount()));//系统弹窗通知的总数
		    		supplierPlacardMap.put("publishTime", DateUtil.parseLongTime2Str3(supplierPlacard.getPublishTime()));//发布时间
		    		int readState = placardReadService.getReadState(supplierPlacard.getId(),supplierId);
		    		supplierPlacardMap.put("readState", String.valueOf(readState));//阅读状态：0未读、1已读
		    		supplierPlacardMap.put("readStateName", supplierPlacard.buildReadStateName(readState));//阅读状态：0未读、1已读
		    		supplierPlacardMap.put("applyState", String.valueOf(supplierPlacard.buildApplyState()));//报名活动状态:0报名进行中、1报名已经结束、2无
		    		supplierPlacardMap.put("applyStateName", supplierPlacard.buildApplyStateName());//报名活动状态:0报名进行中、1报名已经结束、2无
		    		supplierPlacardMapList.add(supplierPlacardMap);
		    	}
//		    	logger.info("supplierPlacardMapList:"+JSON.toJSONString(supplierPlacardMapList));
				return supplierPlacardMapList;
	}

   


	/**
     * 跳转到所有公告首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allNotice.html";
    }

    /**
     * 跳转到添加所有公告
     */
    @RequestMapping("/allNotice_add")
    public String allNoticeAdd() {
        return PREFIX + "allNotice_add.html";
    }

    /**
     * 跳转到修改所有公告
     */
    @RequestMapping("/allNotice_detail")
    public String allNoticeUpdate() {
        return PREFIX + "allNotice_detail.html";
    }

    /**
     * 获取所有公告列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增所有公告
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除所有公告
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改所有公告
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 所有公告详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
