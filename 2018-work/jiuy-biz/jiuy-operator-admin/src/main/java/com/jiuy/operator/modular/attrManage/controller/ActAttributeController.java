package com.jiuy.operator.modular.attrManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;
import com.jiuyuan.service.common.IDynamicPropertyService;
import com.jiuyuan.service.common.IDynamicPropertyValueService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 属性管理控制器
 *
 * @author fengshuonan
 * @Date 2017-12-08 09:52:45
 */
@Controller
@RequestMapping("/actAttribute")
@Login
public class ActAttributeController extends BaseController {

	private String PREFIX = "/attrManage/actAttribute/";

	private static final Log logger = LogFactory.get(ActAttributeController.class);

	@Autowired
	private IDynamicPropertyService dynamicPropertyService;

	@Autowired
	private IDynamicPropertyValueService dynamicPropertyValueService;

	
	
	/**
     * 跳转到属性管理
     */
    @RequestMapping("/manageAttr")
    public String actAttributemanage() {

      return PREFIX + "manageAttr.html";
    }
	
	/**
	 * 跳转到属性管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "actAttribute.html";
	}

	/**
	 * 跳转到添加属性管理
	 */
	@RequestMapping("/actAttribute_add")
	public String actAttributeAdd() {

		return PREFIX + "actAttribute_add.html";
	}
	/**
	   * 跳转到编辑属性管理
	   */
	  @RequestMapping("/actAttribute_edit")
	  public String actAttributeEdit() {

	    return PREFIX + "actAttribute_edit.html";
	  }
    /**
     * 跳转到批量修改
     */
    @RequestMapping("/bultEditing")
    public String actAttributBatchUpdate() {

      return PREFIX + "bultEditing.html";
    }
	  
	/**
	 * 动态属性编辑
	 * @param id
	 * @return
	 */
	@RequestMapping("/toEdit")
	@ResponseBody
	public JsonResponse actAttributeUpdate(@RequestParam(value = "id", required = true) long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicProperty dynamicProperty = dynamicPropertyService.getDynamicPropertyById(id);
			if (dynamicProperty == null) {
				return jsonResponse.setError("动态属性不存在！");
			}
			Map<String, Object> dynaPropMap = new HashMap<>();
			dynaPropMap.put("id", dynamicProperty.getId());
			dynaPropMap.put("name", dynamicProperty.getName());
			dynaPropMap.put("weight", dynamicProperty.getWeight());
			dynaPropMap.put("dynaPropGroupId", dynamicProperty.getDynaPropGroupId());
			dynaPropMap.put("formType", dynamicProperty.getFormType());
			dynaPropMap.put("isFill", dynamicProperty.getIsFill());
			dynaPropMap.put("isDisplay", dynamicProperty.getIsDisplay());
			dynaPropMap.put("remark", dynamicProperty.getRemark());
			Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_id", id);
			List<DynamicPropertyValue> list = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);
			List<Map<String, Object>> dynamicPropertyValueList = new ArrayList<>();
			for (DynamicPropertyValue dynamicPropertyValue : list) {
				Map<String, Object> dynamicPropertyValueMap = new HashMap<>();
				dynamicPropertyValueMap.put("dynaPropValue", dynamicPropertyValue.getDynaPropValue());
				dynamicPropertyValueMap.put("dynaPropValueStatus", dynamicPropertyValue.getStatus());
				dynamicPropertyValueMap.put("dynaPropValueId", dynamicPropertyValue.getId());
				dynamicPropertyValueList.add(dynamicPropertyValueMap);
			}
			dynaPropMap.put("dynamicPropertyValueList", dynamicPropertyValueList);
			return jsonResponse.setSuccessful().setData(dynaPropMap);

		} catch (Exception e) {
			return jsonResponse.setError("修改动态属性e：" + e.getMessage());
		}
	}

	/**
	 * 获取动态属性列表
	 * @param name
	 * @param dynaPropValue
	 * @param formType
	 * @param isFill
	 * @param dynaPropGroupId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "dynaPropValue", required = false, defaultValue = "") String dynaPropValue,
			@RequestParam(value = "formType", required = false, defaultValue = "-1") int formType,
			@RequestParam(value = "isFill", required = false, defaultValue = "-1") int isFill,
			@RequestParam(value = "dynaPropGroupId", required = false, defaultValue = "-1") long dynaPropGroupId,
			@RequestParam(value = "status", required = false, defaultValue = "-1") int status) {

		JsonResponse jsonResponse = new JsonResponse();
		try {
			Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
			List<Map<String, Object>> list = dynamicPropertyService.selectPageList(name, dynaPropValue, formType,
					isFill, dynaPropGroupId, status, page);
			page.setRecords(list);
			return super.packForBT(page);
		} catch (Exception e) {
			return jsonResponse.setError("获取属性列表e：" + e.getMessage());
		}
	}

	/**
	 * 新增动态属性
	 * @param name
	 * @param weight
	 * @param dynaPropGroupId
	 * @param formType
	 * @param dynaPropValue
	 * @param isFill             
	 * @param isDisplay
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) int weight,
			@RequestParam(value = "dynaPropGroupId", required = false, defaultValue = "0") long dynaPropGroupId,
			@RequestParam(value = "formType", required = true) int formType,
			@RequestParam(value = "dynaPropValue", required = false, defaultValue = "") String dynaPropValue,
			@RequestParam(value = "isFill", required = false, defaultValue = "0") int isFill,
			@RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicProperty dynamicProperty = new DynamicProperty();
			dynamicProperty.setName(name);
			dynamicProperty.setWeight(weight);
			dynamicProperty.setDynaPropGroupId(dynaPropGroupId);
			dynamicProperty.setFormType(formType);
			dynamicProperty.setIsFill(isFill);
			dynamicProperty.setIsDisplay(isDisplay);
			dynamicProperty.setCreateTime(System.currentTimeMillis());
			dynamicProperty.setUpdateTime(System.currentTimeMillis());
			dynamicProperty.setRemark(remark);
			// 获得新增属性的id
			dynamicPropertyService.addAndGetId(dynamicProperty);
			long dynaPropId = dynamicProperty.getId();
			// 解析属性值
			if (formType == DynamicProperty.FORM_TYPE_TEXT || formType == DynamicProperty.FORM_TYPE_TEXTAREA) {
				DynamicPropertyValue dynamicPropertyValue = new DynamicPropertyValue();
				dynamicPropertyValue.setDynaPropId(dynaPropId);
				dynamicPropertyValue.setDynaPropValue(dynaPropValue);
				dynamicPropertyValue.setStatus(DynamicPropertyValue.DYNA_PROP_VALUE_STATUS_ON);// 属性值状态1:启用 0：禁用
				dynamicPropertyValue.setCreateTime(System.currentTimeMillis());
				dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.add(dynamicPropertyValue);
			} else {
				Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_id", dynaPropId);
				int count=dynamicPropertyValueService.selectCount(dynamicPropertyValueWrapper);
				String[] split = dynaPropValue.split(",");
				for(int i=0;i<split.length;i++){
					String value=split[i];
					//判断属性值是否相同 
					if (dynamicPropertyValueService.isExitsSameValue(value,dynaPropId)) {
						return jsonResponse.setError("属性值重复");
					}
					DynamicPropertyValue dynamicPropertyValue = new DynamicPropertyValue();
					dynamicPropertyValue.setDynaPropId(dynaPropId);
					dynamicPropertyValue.setDynaPropValue(value);
					dynamicPropertyValue.setStatus(DynamicPropertyValue.DYNA_PROP_VALUE_STATUS_ON);// 属性值状态1:启用 0：禁用
					dynamicPropertyValue.setWeight(count+i+1);
					dynamicPropertyValue.setCreateTime(System.currentTimeMillis());
					dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
					dynamicPropertyValueService.add(dynamicPropertyValue);
				}
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("添加属性e：" + e.getMessage());
		}
	}

	/**
	 * 动态属性禁用与启用
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/enableOrDisable")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse enableOrDisable(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "status", required = true) int status) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicProperty dynamicProperty1 = dynamicPropertyService.getDynamicPropertyById(id);
			if (dynamicProperty1 == null) {
				return jsonResponse.setError("动态属性不存在！");
			}
			DynamicProperty dynamicProperty = new DynamicProperty();
			dynamicProperty.setId(id);
			dynamicProperty.setStatus(status);
			dynamicProperty.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyService.update(dynamicProperty);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("动态属性禁用与启用e：" + e.getMessage());
		}
	}

	/**
	 * 动态属性修改保存
	 * @param id
	 * @param name
	 * @param weight
	 * @param formType
	 * @param dynaPropGroupId
	 * @param dynaPropValue
	 * @param isFill
	 * @param isDisplay
	 * @return
	 */
	@RequestMapping(value = "/saveUpdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse update(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "name", required = false,defaultValue="") String name,
			@RequestParam(value = "weight", required =false,defaultValue="-1") int weight,
			@RequestParam(value = "dynaPropGroupId",required=false,defaultValue="-1") long dynaPropGroupId,
			@RequestParam(value = "isFill", required = false,defaultValue="-1") int isFill,
			@RequestParam(value = "isDisplay", required = false,defaultValue="-1")int isDisplay,
			@RequestParam(value = "remark", required = false,defaultValue="") String remark) {

		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicProperty dynamicProperty1 = dynamicPropertyService.getDynamicPropertyById(id);
			if (dynamicProperty1 == null) {
				return jsonResponse.setError("动态属性不存在！");
			}
			DynamicProperty dynamicProperty = new DynamicProperty();
			dynamicProperty.setId(id);
			if (StringUtils.isNotEmpty(name)) {
				dynamicProperty.setName(name);
			}
			if (weight!=-1) {
				dynamicProperty.setWeight(weight);
			}
			if (dynaPropGroupId!=-1) {
				dynamicProperty.setDynaPropGroupId(dynaPropGroupId);
			}
			if (isFill!=-1) {
				dynamicProperty.setIsFill(isFill);
			}
			if (isDisplay!=-1) {
				dynamicProperty.setIsDisplay(isDisplay);
			}
			if (StringUtils.isNotEmpty(remark)) {
				dynamicProperty.setRemark(remark);;
			}
			dynamicProperty.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyService.update(dynamicProperty);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("编辑动态属性e：" + e.getMessage());
		}
	}
	/**
	 * 批量修改动态属性
	 * @param dynamicPropertyIds
	 * @param dynaPropGroupId
	 * @return
	 */
	@RequestMapping(value = "/batchUpdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse batchUpdate(
			@RequestParam(value="dynamicPropertyIds",required=true)String dynamicPropertyIds,
			@RequestParam(value="dynaPropGroupId",required=true)long dynaPropGroupId ) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			String[] dynamicPropertyIdsArr = dynamicPropertyIds.split(",");
			DynamicProperty dynamicProperty=null;
			for (String dynamicPropertyId : dynamicPropertyIdsArr) {
				dynamicProperty=new DynamicProperty();
				dynamicProperty.setId(Long.valueOf(dynamicPropertyId));
				dynamicProperty.setDynaPropGroupId(dynaPropGroupId);
				dynamicProperty.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyService.update(dynamicProperty);
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("批量修改动态属性组e:"+e.getMessage());
		}
	}
	/**
	 * 属性管理详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}
}
