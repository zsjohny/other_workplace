package com.jiuy.operator.modular.supplierNoticeManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
import com.jiuy.operator.modular.setManage.controller.FunctionSetController;
import com.jiuyuan.entity.newentity.HomeMenu;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.PlacardApplyAudit;
import com.jiuyuan.entity.newentity.SupplierPlacard;
import com.jiuyuan.service.common.IHomeMenuService;
import com.jiuyuan.service.common.IPlacardApplyAuditService;
import com.jiuyuan.service.common.IPlacardApplyService;
import com.jiuyuan.service.common.ISupplierPlacardService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 供应商公告管理控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:39:52
 */
@Controller
@RequestMapping("/allNotice")
public class AllNoticeController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(AllNoticeController.class);
  

    private String PREFIX = "/supplierNoticeManage/allNotice/";
    
    @Autowired
    private ISupplierPlacardService supplierPlacardService;

    @Autowired
    private IPlacardApplyService placardApplyService;

    @Autowired
    private IPlacardApplyAuditService placardApplyAuditService;
    
    private List<Map<String, String>> buildPlacardApplyAuditMapList(List<PlacardApplyAudit> placardApplyAuditList) {
    	List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
    	for(PlacardApplyAudit placardApplyAudit: placardApplyAuditList){
    		PlacardApply placardApply = placardApplyService.getPlacardApplyinfo(placardApplyAudit.getPlacardApplyId());
    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
    		supplierPlacardMap.put("placardApplyAuditId", String.valueOf(placardApplyAudit.getId()));//公告申请审核ID
    		supplierPlacardMap.put("placardApplyId", String.valueOf(placardApplyAudit.getPlacardApplyId()));//公告申请ID
    		supplierPlacardMap.put("placardId", String.valueOf(placardApplyAudit.getPlacardId()));//公告ID
    		supplierPlacardMap.put("applyCoutent", placardApplyAudit.getApplyCoutent());//报名信息
    		supplierPlacardMap.put("titleNote", placardApplyAudit.getTitleNote());//审核说明
    		supplierPlacardMap.put("auditState", String.valueOf(placardApplyAudit.getState()));//公告申请状态：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("auditStateName", placardApplyAudit.buildStateName());//公告申请状态名称：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("applyUserName", placardApplyAudit.getApplyUserName());//审核人名称
    		supplierPlacardMap.put("auditTime", DateUtil.parseLongTime2Str3(placardApplyAudit.getAuditTime()));//审核时间
    		supplierPlacardMap.put("applyTime", DateUtil.parseLongTime2Str3(placardApply.getApplyTime()));//申请时间
    		supplierPlacardMapList.add(supplierPlacardMap);
    	}
		return supplierPlacardMapList;
	}
    
    /**
     * 审核公告申请
     */
    @RequestMapping(value = "/auditPlacardApply")
    @ResponseBody 
    public JsonResponse auditPlacardApply(
    		@RequestParam(value = "placardApplyId",required = true) long placardApplyId,//公告ID
    		@RequestParam(value = "state",required = true) int state,//公告申请状态：0待审核、1已通过、2已拒绝
    		@RequestParam(value = "titleNote",required = false,defaultValue = "") String titleNote//审核说明
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ShiroUser user = ShiroKit.getUser();
			String applyUserName = user.getName();
			long applyUserNameId = user.getId() ;
			placardApplyService.auditPlacardApply(placardApplyId,state,titleNote,applyUserName,applyUserNameId);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取公告申请信息:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
   
    
    
    /**
     * 获取公告申请信息
     */
    @RequestMapping(value = "/getPlacardApplyinfo")
    @ResponseBody 
    public JsonResponse getPlacardApplyinfo(
    		@RequestParam(value = "placardApplyId",required = true) long placardApplyId//公告申请ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			PlacardApply placardApply  = placardApplyService.getPlacardApplyinfo(placardApplyId);
			Map<String,String> supplierPlacardMap = new HashMap<String,String>();
    		supplierPlacardMap.put("placardApplyId", String.valueOf(placardApply.getId()));//公告申请ID
    		supplierPlacardMap.put("placardId", String.valueOf(placardApply.getPlacardId()));//公告ID
    		supplierPlacardMap.put("placardTitle", String.valueOf(placardApply.getPlacardTitle()));//公告标题
    		supplierPlacardMap.put("coutent", placardApply.getCoutent());//报名信息
    		supplierPlacardMap.put("titleNote", placardApply.getTitleNote());//审核说明
    		supplierPlacardMap.put("state", String.valueOf(placardApply.getState()));//公告申请状态：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("stateName", placardApply.buildStateName());//公告申请状态名称：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("auditTime", DateUtil.parseLongTime2Str3(placardApply.getAuditTime()));//审核时间
    		supplierPlacardMap.put("applyTime", DateUtil.parseLongTime2Str3(placardApply.getApplyTime()));//申请时间
    		supplierPlacardMap.put("applyUserName", "");//审核人名称
			return jsonResponse.setSuccessful().setData(supplierPlacardMap);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取公告申请信息:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 获取公告申请审核信息
     */
    @RequestMapping(value = "/getPlacardApplyAuditInfo")
    @ResponseBody 
    public JsonResponse getPlacardApplyAuditInfo(
    		@RequestParam(value = "placardApplyAuditId",required = true) long placardApplyAuditId//公告申请审核ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			PlacardApplyAudit placardApplyAudit  = placardApplyAuditService.getPlacardApplyAuditInfo(placardApplyAuditId);
			long placardApplyId = placardApplyAudit.getPlacardApplyId();
			PlacardApply placardApply  = placardApplyService.getPlacardApplyinfo(placardApplyId);
			
			Map<String,String> supplierPlacardMap = new HashMap<String,String>();
			supplierPlacardMap.put("placardApplyAuditId", String.valueOf(placardApplyAudit.getId()));//公告申请审核ID
    		supplierPlacardMap.put("placardApplyId", String.valueOf(placardApplyAudit.getPlacardApplyId()));//公告申请ID
    		supplierPlacardMap.put("placardId", String.valueOf(placardApplyAudit.getPlacardId()));//公告ID
    		if(placardApply != null ){
    			supplierPlacardMap.put("placardTitle", placardApply.getPlacardTitle());//公告标题
        		supplierPlacardMap.put("coutent", placardApply.getCoutent());//报名信息
    		}else{
    			supplierPlacardMap.put("placardTitle","");//公告标题
        		supplierPlacardMap.put("coutent", "");//报名信息
        		logger.info("获取公告申请信息为空，placardApplyId："+placardApplyId); 
    		}
    		supplierPlacardMap.put("titleNote", placardApplyAudit.getTitleNote());//审核说明
    		supplierPlacardMap.put("state", String.valueOf(placardApplyAudit.getState()));//公告申请状态：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("stateName", placardApplyAudit.buildStateName());//公告申请状态名称：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("auditTime", DateUtil.parseLongTime2Str3(placardApplyAudit.getAuditTime()));//审核时间
    		supplierPlacardMap.put("applyTime", DateUtil.parseLongTime2Str3(placardApplyAudit.getApplyTime()));//申请时间
    		supplierPlacardMap.put("applyUserName", placardApplyAudit.getApplyUserName());//审核人名称
			return jsonResponse.setSuccessful().setData(supplierPlacardMap);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取公告申请审核信息:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    


	/**
     * 获取指定公告申请审核列表(该接口被getPlacardApplyAuditList替代)
     */
    @RequestMapping(value = "/getPlacardApplyListByPlacardId")
    @ResponseBody 
    public Object getPlacardApplyListByPlacardId(
    		@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId,//公告ID
    		@RequestParam(value = "supplierId",required = true) long supplierId//供应商ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			List<PlacardApplyAudit> placardApplyAuditList  = placardApplyAuditService.getPlacardApplyAuditList(supplierPlacardId,supplierId);
			
			
			page.setRecords(buildPlacardApplyAuditMapList(placardApplyAuditList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取指定公告申请审核列表(该接口被getPlacardApplyAuditList替代):"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 获取指定公告申请审核列表
     */
    @RequestMapping(value = "/getPlacardApplyAuditList")
    @ResponseBody 
    public Object getPlacardApplyAuditList(
    		@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId,//公告ID
    		@RequestParam(value = "supplierId",required = true) long supplierId//供应商ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			List<PlacardApplyAudit> placardApplyAuditList  = placardApplyAuditService.getPlacardApplyAuditList(supplierPlacardId,supplierId);
			
			
			page.setRecords(buildPlacardApplyAuditMapList(placardApplyAuditList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取指定公告申请审核列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
   

	/**
     * 供应商公告申请列表
     */
    @RequestMapping(value = "/getPlacardApplyList")
    @ResponseBody 
    public Object getPlacardApplyList(
    		@RequestParam(value = "supplierPlacardId",required = false,defaultValue = "0") long supplierPlacardId,//公告ID
    		@RequestParam(value = "title",required = false,defaultValue = "") String title,//公告标题
    		@RequestParam(value = "supplierName",required = false,defaultValue = "") String supplierName,//供应商名称
    		@RequestParam(value = "brandName",required = false,defaultValue = "") String brandName,//品牌名称
    		@RequestParam(value = "applyTimeBegin",required = false,defaultValue = "0") long applyTimeBegin,//申请时间开始
   			@RequestParam(value = "applyTimeEnd",required = false,defaultValue = "0") long applyTimeEnd,//申请时间结束
   			
   			
   			@RequestParam(value = "applyEndTimeBegin",required = false,defaultValue = "0") long applyEndTimeBegin,//申请结束时间开始
   			@RequestParam(value = "applyEndTimeEnd",required = false,defaultValue = "0") long applyEndTimeEnd,//申请结束时间结束
   			@RequestParam(value = "state",required = false,defaultValue = "-1") int state,//公告申请状态：0待审核、1已通过、2已拒绝
   			@RequestParam(value = "coutent",required = false,defaultValue = "") String coutent,//报名信息
   			@RequestParam(value = "titleNote",required = false,defaultValue = "") String titleNote//审核说明
		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			logger.error("供应商公告申请列表参数，supplierPlacardId:"+supplierPlacardId
					+",title:"+title+",supplierName:"+supplierName+",brandName:"+brandName
					+",applyTimeBegin:"+applyTimeBegin+",applyTimeEnd:"+applyTimeEnd+",applyEndTimeBegin:"+applyEndTimeBegin
					+",state:"+state+",coutent"+coutent+",titleNote"+titleNote);
			List<PlacardApply> placardApplyList  = placardApplyService.getPlacardApplyList(page,
					 supplierPlacardId, title, supplierName, brandName, applyTimeBegin,  applyTimeEnd,  applyEndTimeBegin,
					 applyEndTimeEnd, state,  coutent, titleNote);
			page.setRecords(buildPlacardApplyMapList(placardApplyList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商公告申请列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    private List<Map<String, String>> buildPlacardApplyMapList(List<PlacardApply> placardApplyList) {
    	List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
    	for(PlacardApply placardApply: placardApplyList){
    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
    		supplierPlacardMap.put("placardApplyId", String.valueOf(placardApply.getId()));//公告申请ID
    		supplierPlacardMap.put("placardId", String.valueOf(placardApply.getPlacardId()));//公告ID
    		supplierPlacardMap.put("supplierId", String.valueOf(placardApply.getSupplierId()));//供应商ID
    		supplierPlacardMap.put("placardTitle", placardApply.getPlacardTitle());//公告标题
    		supplierPlacardMap.put("supplierName", placardApply.getSupplierName());//供应商名称
    		supplierPlacardMap.put("brandName", placardApply.getBrandName());//品牌名称
    		supplierPlacardMap.put("state", String.valueOf(placardApply.getState()));//公告申请状态：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("stateName", placardApply.buildStateName());//公告申请状态名称：1待审核、2已通过、3已拒绝
    		supplierPlacardMap.put("coutent", placardApply.getCoutent());//报名信息
    		supplierPlacardMap.put("titleNote", placardApply.getTitleNote());//审核说明
    		supplierPlacardMap.put("auditTime", DateUtil.parseLongTime2Str3(placardApply.getAuditTime()));//审核时间
    		supplierPlacardMap.put("applyTime", DateUtil.parseLongTime2Str3(placardApply.getApplyTime()));//申请时间
    		supplierPlacardMapList.add(supplierPlacardMap);
    	}
		return supplierPlacardMapList;
	}
    
    /**
     * 获取供应商公告
     */
    @RequestMapping(value = "/getSupplierPlacardInfo")
   	@ResponseBody
   	public JsonResponse getSupplierPlacardInfo(
   			@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId//菜单ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			SupplierPlacard  supplierPlacard = supplierPlacardService.getSupplierPlacardInfo(supplierPlacardId);
   			Map<String,String> supplierPlacardMap = new HashMap<String,String>();
   			
   			
   			supplierPlacardMap.put("id", String.valueOf(supplierPlacard.getId()));//公告ID
   			supplierPlacardMap.put("title", supplierPlacard.getTitle());//公告标题
   			supplierPlacardMap.put("type", String.valueOf(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启） 
   			supplierPlacardMap.put("state", String.valueOf(supplierPlacard.getState()));//公告状态:1已发布、2通知中、3已停止 
   			supplierPlacardMap.put("content", supplierPlacard.getContent());//公告内容
   			supplierPlacardMap.put("notifyCount", String.valueOf(supplierPlacard.getNotifyCount()));//系统弹窗通知的总数
   			supplierPlacardMap.put("readCount", String.valueOf(supplierPlacard.getReadCount()));//阅读数 
   			supplierPlacardMap.put("publishTime", DateUtil.parseLongTime2Str((supplierPlacard.getPublishTime())));//发布时间
   			supplierPlacardMap.put("stopTime", DateUtil.parseLongTime2Str(supplierPlacard.getStopTime()));//停止时间
   			supplierPlacardMap.put("createTime", DateUtil.parseLongTime2Str(supplierPlacard.getCreateTime()));//创建时间	
   			supplierPlacardMap.put("publishType", String.valueOf(supplierPlacard.getPublishType()));//发布类型：0立即、1定时
   			supplierPlacardMap.put("isSendAdvice", String.valueOf(supplierPlacard.getIsSendAdvice()));//是否发送站内通知：0不通知、1通知
   			supplierPlacardMap.put("applyEndTime", DateUtil.parseLongTime2Str(supplierPlacard.getApplyEndTime()));//报名结束时间
   			supplierPlacardMap.put("adminId", String.valueOf(supplierPlacard.getAdminId()));//创建人ID
   			
    		return jsonResponse.setSuccessful().setData(supplierPlacardMap);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}
    
    /**
     * 供应商公告列表
     */
    @RequestMapping(value = "/getSupplierPlacardList")
    @ResponseBody 
    public Object getSupplierPlacardList(@RequestParam(value = "supplierPlacardId",required = false,defaultValue = "0") long supplierPlacardId,//公告ID
   			@RequestParam(value = "title",required = false,defaultValue = "") String title,//公告标题
   			@RequestParam(value = "content",required = false,defaultValue = "") String content,//公告内容
   			@RequestParam(value = "publishTimeBegin",required = false,defaultValue = "0") long publishTimeBegin,//发布时间开始
   			@RequestParam(value = "publishTimeEnd",required = false,defaultValue = "0") long publishTimeEnd,//发布时间结束
   			@RequestParam(value = "state",required = false,defaultValue = "-1") int state,//公告状态:-1全部，0编辑中、1已发布、2通知中、3已停止
   			@RequestParam(value = "type",required = false,defaultValue = "-1") int type//公告类型:-1全部0普通公告、1报名公告
		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			//获取所有订单
			List<SupplierPlacard> supplierPlacardList  = supplierPlacardService.getSupplierPlacardList(page, supplierPlacardId,  title, content,publishTimeBegin,  publishTimeEnd,  state,  type);
			
			page.setRecords(buildSupplierPlacardMapList(supplierPlacardList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("供应商活动搜索列表:"+e.getMessage());
    		return jsonResponse.setError( e.getMessage());
		}
    }
    
    
   
    
    
    private List<Map<String,String>> buildSupplierPlacardMapList(List<SupplierPlacard> supplierPlacardList) {
    	List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
    	for(SupplierPlacard supplierPlacard: supplierPlacardList){
    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
    		supplierPlacardMap.put("supplierPlacardId", String.valueOf(supplierPlacard.getId()));//公告ID
    		supplierPlacardMap.put("title", supplierPlacard.getTitle());//公告标题
    		supplierPlacardMap.put("content", supplierPlacard.getContent());//公告内容
    		supplierPlacardMap.put("type", String.valueOf(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
    		supplierPlacardMap.put("typeName", supplierPlacard.buildTypeName(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
    		supplierPlacardMap.put("state", String.valueOf(supplierPlacard.getState()));//公告状态:0编辑中、1已发布、2通知中、3已停止
    		supplierPlacardMap.put("stateName", supplierPlacard.buildStateName());//公告状态:0编辑中、1已发布、2通知中、3已停止
    		supplierPlacardMap.put("notifyCount", String.valueOf(supplierPlacard.getNotifyCount()));//系统弹窗通知的总数
    		supplierPlacardMap.put("readCount", String.valueOf(supplierPlacard.getReadCount()));//阅读数
    		supplierPlacardMap.put("publishTime", DateUtil.parseLongTime2Str3(supplierPlacard.getPublishTime()));//发布时间
    		supplierPlacardMap.put("stopTime", DateUtil.parseLongTime2Str3(supplierPlacard.getStopTime()));//停止时间
    		supplierPlacardMap.put("createTime", DateUtil.parseLongTime2Str3(supplierPlacard.getCreateTime()));//创建时间
    		supplierPlacardMapList.add(supplierPlacardMap);
    	}
		return supplierPlacardMapList;
	}
    
    /**
     * 查看供应商公告
     */
    @RequestMapping(value = "/showSupplierPlacardInfo")
   	@ResponseBody
   	public JsonResponse showSupplierPlacardInfo(
   			@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId//菜单ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			Map<String,String> map = new HashMap<String,String>();
   			SupplierPlacard  supplierPlacard = supplierPlacardService.getSupplierPlacardInfo(supplierPlacardId);
   			map.put("supplierPlacardId", String.valueOf(supplierPlacard.getId()));
   			map.put("type", String.valueOf(supplierPlacard.getType()));//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
   			map.put("title", supplierPlacard.getTitle());//公告标题
   			map.put("content", supplierPlacard.getContent());//公告内容
   			map.put("applyEndTime", DateUtil.parseLongTime2Str3(supplierPlacard.getApplyEndTime()));//报名结束时间
   			map.put("applyState", String.valueOf(supplierPlacard.buildApplyState()));//报名活动状态:0报名进行中、1报名已经结束、2无
   			map.put("applyStateName", supplierPlacard.buildApplyStateName());//报名活动状态名称:0报名进行中、1报名已经结束、2无
   			map.put("state", String.valueOf(supplierPlacard.getState()));//公告状态:1已发布、2通知中、3已停止
   			map.put("stateName", supplierPlacard.buildStateName());//公告状态:1已发布、2通知中、3已停止
   			return jsonResponse.setSuccessful().setData(map);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}

	/**
   	 * 添加供应商公告
   	 */
    @RequestMapping(value = "/addSupplierPlacard" )
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse addSupplierPlacard(
   			@RequestParam(value = "title",required = true) String title,//公告标题
   			@RequestParam(value = "content",required = true) String content,//公告内容
   			@RequestParam(value = "type",required = true) int type,//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
   			@RequestParam(value = "isSendAdvice",required = true) int isSendAdvice,//是否发送站内通知：0不通知、1通知
   			@RequestParam(value = "publishType",required = true) int publishType,//发布类型：0立即、1定时
   			@RequestParam(value = "publishTime",required = false,defaultValue = "0") long publishTime,//定时发布时间,立即发布类型时传0
   			@RequestParam(value = "applyEndTime",required = false,defaultValue = "0") long applyEndTime//报名结束时间
   	     ) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {   
   			long adminId = ShiroKit.getUser().getId();
   			//1、添加公告记录
   			supplierPlacardService.addSupplierPlacard(adminId, title,content, type ,isSendAdvice,publishType,publishTime,applyEndTime);
   			
   			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return jsonResponse.setError( e.getMessage());
		}
   	}
    
    /**
   	 * 修改供应商公告
   	 */
    @RequestMapping(value = "/updateSupplierPlacard" )
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse updateSupplierPlacard(
   			@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId,//公告ID
   			@RequestParam(value = "title",required = true) String title,//公告标题
   			@RequestParam(value = "content",required = true) String content,//公告内容
   			@RequestParam(value = "type",required = true) int type,//公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
   			@RequestParam(value = "isSendAdvice",required = true) int isSendAdvice,//是否发送站内通知：0不通知、1通知
   			@RequestParam(value = "publishType",required = true) int publishType,//发布类型：0立即、1定时
   			@RequestParam(value = "publishTime",required = false,defaultValue = "0") long publishTime,//定时发布时间,立即发布类型时传0
   			@RequestParam(value = "applyEndTime",required = false,defaultValue = "0") long applyEndTime//报名结束时间
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			supplierPlacardService.updateSupplierPlacard(supplierPlacardId, title, content, type, isSendAdvice,publishType,publishTime,applyEndTime);
   			return jsonResponse.setSuccessful();
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}
    
    /**
   	 * 停止供应商公告
   	 */
    @RequestMapping(value = "/stopSupplierPlacard")
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse stopSupplierPlacard(
   			@RequestParam(value = "supplierPlacardId",required = true) long supplierPlacardId//公告ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			supplierPlacardService.stopSupplierPlacard(supplierPlacardId);
   			return jsonResponse.setSuccessful();
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}

    
    
   
    
   
    
    /**
     * 	获取供应商公告报名申请统计信息
     */
    @RequestMapping(value = "/getPlacardApplyStatisticsInfo")
   	@ResponseBody
   	public JsonResponse getPlacardApplyStatisticsInfo() {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			Map<String,String> statisticsInfoMap = new HashMap<String,String>();
   			int  totalCount = placardApplyService.getPlacardApplyTotalCount();//报名单总数
   			int  waitAuditCount = placardApplyService.getPlacardApplyWaitAuditCount();//待审核总数
   			statisticsInfoMap.put("totalCount", String.valueOf(totalCount));
   			statisticsInfoMap.put("waitAuditCount", String.valueOf(waitAuditCount));
   			return jsonResponse.setSuccessful().setData(statisticsInfoMap);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
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
   			Map<String,String> statisticsInfoMap = new HashMap<String,String>();
   			int  totalCount = supplierPlacardService.getTotalCount();//公告总数
   			int  notifyCount = supplierPlacardService.getNotifyCount();//通知中公告总数
   			statisticsInfoMap.put("totalCount", String.valueOf(totalCount));
   			statisticsInfoMap.put("notifyCount", String.valueOf(notifyCount));
   			return jsonResponse.setSuccessful().setData(statisticsInfoMap);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}
   
    /**
     * 跳转到供应商公告管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allNotice.html";
    }

    /**
     * 跳转到添加供应商公告管理
     */
    @RequestMapping("/allNotice_add")
    public String allNoticeAdd() {
        return PREFIX + "allNotice_add.html";
    }

    /**
     * 跳转到修改供应商公告管理
     */
    @RequestMapping("/allNotice_update")
    public String allNoticeUpdate() {
        return PREFIX + "allNotice_edit.html";
    }
    /**
     * 跳转到查看供应商公告管理
     */
    @RequestMapping("/allNotice_look")
    public String allNoticelook() {
        return PREFIX + "allNotice_look.html";
    }

    /**
     * 获取供应商公告管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增供应商公告管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除供应商公告管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改供应商公告管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 供应商公告管理详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
