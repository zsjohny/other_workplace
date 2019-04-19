package com.jiuy.operator.modular.attrManage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;
import com.jiuyuan.service.common.DynamicPropertyService;
import com.jiuyuan.service.common.IDynamicPropertyService;
import com.jiuyuan.service.common.IDynamicPropertyValueService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 
 * 动态属性值控制器
 *
 */
@Controller
@RequestMapping("/ActAttributeValue")
@Login
public class ActAttributeValueController extends BaseController {
	@Autowired
	private IDynamicPropertyValueService dynamicPropertyValueService;
	@Autowired
	private IDynamicPropertyService dynamicPropertyService;
	
	
	
	/**
	 * 属性值禁用与启用
	 * @param dynaPropValueId
	 * @param status
	 * @return
	 */
	@RequestMapping("/enableOrDisable")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse enableOrDisable(@RequestParam(value = "dynaPropValueId", required = true) long dynaPropValueId,
			@RequestParam(value = "status", required = true) int status) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicPropertyValue dynamicPropertyValue1 = dynamicPropertyValueService.selectById(dynaPropValueId);
			if (dynamicPropertyValue1 == null) {
				return jsonResponse.setError("属性值不存在！");
			}
			DynamicPropertyValue dynamicPropertyValue = new DynamicPropertyValue();
			dynamicPropertyValue.setId(dynaPropValueId);
			dynamicPropertyValue.setStatus(status);
			dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyValueService.update(dynamicPropertyValue);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("修改动态属性值状态e：" + e.getMessage());
		}

	}
	
	/**
	 * 属性值管理
	 * @param dynaPropId
	 * @return
	 */
	@RequestMapping("/dynamicPropertyValueManager")
	@ResponseBody
	public JsonResponse dynamicPropertyValueManager(@RequestParam(value = "dynaPropId", required = true) long dynaPropId) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicProperty dynamicProperty= dynamicPropertyService.getDynamicPropertyById(dynaPropId);
			if (dynamicProperty==null) {
				return jsonResponse.setError("动态属性不存在！");
			}
				Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_id", dynaPropId).orderBy("weight", true);
				List<DynamicPropertyValue> list = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);
				Map<String,Object> map=new HashMap<>();
				List<Map<String, Object>> dynamicPropertyValueList = new ArrayList<>();
				if (dynamicProperty.getFormType()!=DynamicProperty.FORM_TYPE_TEXT && dynamicProperty.getFormType()!=DynamicProperty.FORM_TYPE_TEXTAREA) {
				for (DynamicPropertyValue dynamicPropertyValue : list) {
					Map<String, Object> dynamicPropertyValueMap = new HashMap<>();
					dynamicPropertyValueMap.put("dynaPropValue", dynamicPropertyValue.getDynaPropValue());
					dynamicPropertyValueMap.put("dynaPropValueStatus", dynamicPropertyValue.getStatus());
					dynamicPropertyValueMap.put("dynaPropValueId", dynamicPropertyValue.getId());
					dynamicPropertyValueMap.put("weight", dynamicPropertyValue.getWeight());
					dynamicPropertyValueList.add(dynamicPropertyValueMap);
				}
				map.put("dynaPropValueList", dynamicPropertyValueList);
			}else{
				if (list.size()>0) {
					DynamicPropertyValue dynamicPropertyValue = list.get(0);
					map.put("dynaPropValue", dynamicPropertyValue.getDynaPropValue());
				}
			}
			map.put("dynaPropName", dynamicProperty.getName());
			map.put("formType", dynamicProperty.getFormType());
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("动态属性管理e：" + e.getMessage());
		}

	}
	/**
	 * 属性值排序
	 * @param dynaPropValueId
	 * @return
	 */
	@RequestMapping("/dynamicPropertyValueSort")
	@ResponseBody
	public JsonResponse dynamicPropertyValueSort(@RequestParam(value = "dynaPropValueId", required = true) long dynaPropValueId,
			@RequestParam(value="direct",required=true) int direct) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//下移
			if (direct==0) {
				DynamicPropertyValue smallDynamicPropertyValue = dynamicPropertyValueService.selectById(dynaPropValueId);
				Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("weight", smallDynamicPropertyValue.getWeight()+1).eq("dyna_prop_id", smallDynamicPropertyValue.getDynaPropId());
				List<DynamicPropertyValue> dynamicPropertyValueList = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);
				if (dynamicPropertyValueList.size()<1) {
					return jsonResponse.setError("此属性值排序值最大，无法下移");
				}
				DynamicPropertyValue dynamicPropertyValue=new DynamicPropertyValue();
				//将上面属性值的排序值设置为当前属性值的排序值
				DynamicPropertyValue bigDynamicPropertyValue = dynamicPropertyValueList.get(0);
				dynamicPropertyValue.setId(bigDynamicPropertyValue.getId());
				dynamicPropertyValue.setWeight(smallDynamicPropertyValue.getWeight());
				dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.update(dynamicPropertyValue);
				//将当前属性值的排序值设置上面属性值的排序值
				dynamicPropertyValue=new DynamicPropertyValue();
				dynamicPropertyValue.setId(smallDynamicPropertyValue.getId());
				dynamicPropertyValue.setWeight(bigDynamicPropertyValue.getWeight());
				dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.update(dynamicPropertyValue);
			}
			
			//上移
			if (direct==1) {
				DynamicPropertyValue upDynamicPropertyValue = dynamicPropertyValueService.selectById(dynaPropValueId);
				Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("weight", upDynamicPropertyValue.getWeight()-1).eq("dyna_prop_id", upDynamicPropertyValue.getDynaPropId());
				List<DynamicPropertyValue> dynamicPropertyValueList = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);
				if (dynamicPropertyValueList.size()<1) {
					return jsonResponse.setError("此属性值排序值最小，无法上移");
				}
				DynamicPropertyValue dynamicPropertyValue=new DynamicPropertyValue();
				//将下面属性值的排序值设置为当前属性值的排序值
				DynamicPropertyValue underDynamicPropertyValue = dynamicPropertyValueList.get(0);
				dynamicPropertyValue.setId(underDynamicPropertyValue.getId());
				dynamicPropertyValue.setWeight(upDynamicPropertyValue.getWeight());
				dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.update(dynamicPropertyValue);
				//将当前属性值的排序值设置上面属性值的排序值
				dynamicPropertyValue=new DynamicPropertyValue();
				dynamicPropertyValue.setId(upDynamicPropertyValue.getId());
				dynamicPropertyValue.setWeight(underDynamicPropertyValue.getWeight());
				dynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.update(dynamicPropertyValue);
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("动态属性值排序e：" + e.getMessage());
		}

	}
	/**
	 * 添加属性值（单选，多选，下拉单选，下拉多选）及属性值修改（单行文本，多行文本）
	 * @param dynaPropValueId
	 * @return
	 */
	@RequestMapping("/dynamicPropertyValueAdd")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse dynamicPropertyValueAdd(@RequestParam(value = "dynaPropId", required = true) long dynaPropId,
			@RequestParam(value="dynaPropValue",required=true)String dynaPropValue) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_id", dynaPropId);
			DynamicProperty dynamicProperty = dynamicPropertyService.getDynamicPropertyById(dynaPropId);
			if (dynamicProperty==null) {
				return jsonResponse.setError("原属性不存在");
			}
			boolean flag=false;
			if (dynamicProperty.getFormType()!=DynamicProperty.FORM_TYPE_TEXT && dynamicProperty.getFormType()!=DynamicProperty.FORM_TYPE_TEXTAREA) {
				//当前属性下属性值数量
				int count=dynamicPropertyValueService.selectCount(dynamicPropertyValueWrapper);
				String[] split = dynaPropValue.split(",");
				for(int i=0;i<split.length;i++){
					String value=split[i];
					//判断属性值是否相同 TODO
					if (dynamicPropertyValueService.isExitsSameValue(value,dynaPropId)) {
						flag=true;
						continue;
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
			}else{
				List<DynamicPropertyValue> dynamicPropertyValueList = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);
				if (dynamicPropertyValueList.size()<1) {
					return jsonResponse.setError("原属性值不存在");
				}
				DynamicPropertyValue dynamicPropertyValue = dynamicPropertyValueList.get(0);
				DynamicPropertyValue newdynamicPropertyValue=new DynamicPropertyValue();
				newdynamicPropertyValue.setId(dynamicPropertyValue.getId());
				newdynamicPropertyValue.setDynaPropValue(dynaPropValue);
				newdynamicPropertyValue.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyValueService.update(newdynamicPropertyValue);
			}
			return jsonResponse.setSuccessful().setData(flag);
		} catch (Exception e) {
			return jsonResponse.setError("动态属性值添加e：" + e.getMessage());
		}

	}
	

}
