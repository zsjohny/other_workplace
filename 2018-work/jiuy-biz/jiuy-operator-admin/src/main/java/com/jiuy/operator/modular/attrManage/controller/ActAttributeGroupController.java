package com.jiuy.operator.modular.attrManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;
import com.jiuyuan.service.common.IDynamicPropertyGroupService;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 属性管理控制器
 *
 * @author fengshuonan
 * @Date 2017-12-08 10:30:14
 */
@Controller
@RequestMapping("/actAttributeGroup")
@Login
public class ActAttributeGroupController extends BaseController {

	private String PREFIX = "/attrManage/actAttributeGroup/";

	private static final Log logger = LogFactory.get(ActAttributeGroupController.class);
	@Autowired
	private IDynamicPropertyGroupService dynamicPropertyGroupService;
	@Autowired
	private IDynamicPropertyService dynamicPropertyService;

	/**
	 * 跳转到属性管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "actAttributeGroup.html";
	}

	/**
	 * 跳转到添加属性管理
	 */
	@RequestMapping("/actAttributeGroup_add")
	public String actAttributeGroupAdd() {
		return PREFIX + "actAttributeGroup_add.html";
	}

	/**
	   * 跳转到属性组编辑
	   */
	  @RequestMapping("/actAttributeGroup_edit")
	  public String actAttributeGroupEdit() {
	    return PREFIX + "actAttributeGroup_edit.html";
	  }
	
	/**
	 * 动态属性组修改
	 * @param id
	 * @return
	 */
	@RequestMapping("/toUpdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse actAttributeGroupUpdate(@RequestParam(value = "id") long id) {
		JsonResponse jsonResponse = new JsonResponse();
		DynamicPropertyGroup dynamicPropertyGroup = dynamicPropertyGroupService.getDynaPropGroupById(id);
		try {
			if (dynamicPropertyGroup == null) {
				return jsonResponse.setError("要修改动态属性组不存在！");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dynamicPropertyGroup.getId());
			map.put("name", dynamicPropertyGroup.getName());
			map.put("weight", dynamicPropertyGroup.getWeight());
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("修改动态属性组e：" + e.getMessage());
		}
	}

	/**
	 * 获取属性组列表
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "name", required = false, defaultValue = "") String name) {

		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		try {
			List<Map<String, Object>> selectPageList = dynamicPropertyGroupService.selectPageList(name, page);
			page.setRecords(selectPageList);
			return super.packForBT(page);
		} catch (Exception e) {
			return jsonResponse.setError("获取动态属性组列表e：" + e.getMessage());
		}
	}

	/**
	 * 新增动态属性组
	 * 
	 * @param name
	 * @param weight
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) int weight) {

		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicPropertyGroup dynamicPropertyGroup = new DynamicPropertyGroup();
			dynamicPropertyGroup.setName(name);
			dynamicPropertyGroup.setWeight(weight);
			dynamicPropertyGroup.setCreateTime(System.currentTimeMillis());
			dynamicPropertyGroup.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyGroupService.add(dynamicPropertyGroup);
			jsonResponse.setSuccessful().setData("ok");
			return jsonResponse;
		} catch (Exception e) {
			return jsonResponse.setError("添加动态属性组e：" + e.getMessage());
		}
	}

	/**
	 * 检查动态属性组能否删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/checkDelete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse checkDelete(@RequestParam(value = "id", required = true) long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Wrapper<DynamicProperty> wrapper = new EntityWrapper<DynamicProperty>().eq("dyna_prop_group_id", id);
			List<DynamicProperty> list = dynamicPropertyService.getDynamicProperty(wrapper);
			boolean flag = false;
			if (list.size() > 0) {
				jsonResponse.setSuccessful().setData(flag);
			}
			if (list.size() < 1) {
				flag = true;
				jsonResponse.setSuccessful().setData(flag);
			}
			return jsonResponse;
		} catch (Exception e) {
			return jsonResponse.setError("检查动态属性组能否删除e：" + e.getMessage());
		}
	}

	/**
	 * 删除属性组
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse delete(@RequestParam(value = "id", required = true) long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicPropertyGroup dynamicPropertyGroup1 = dynamicPropertyGroupService.getDynaPropGroupById(id);
			if (dynamicPropertyGroup1 == null) {
				return jsonResponse.setError("动态属性组不存在！");
			}
			Wrapper<DynamicProperty> wrapper = new EntityWrapper<DynamicProperty>().eq("dyna_prop_group_id", id);
			List<DynamicProperty> list = dynamicPropertyService.getDynamicProperty(wrapper);
			if (list.size() < 1) {
				dynamicPropertyGroupService.delete(id);
			} 
			if(list.size()>0){
				DynamicProperty newDynamicProperty=null;
				for (DynamicProperty dynamicProperty : list) {
					newDynamicProperty=new DynamicProperty();
					newDynamicProperty.setId(dynamicProperty.getId());
					newDynamicProperty.setDynaPropGroupId(-2L);
					dynamicPropertyService.update(newDynamicProperty);
				}
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("删除动态属性组e：" + e.getMessage());
		}
	}

	/**
	 * 修改属性组
	 * @param id
	 * @param name
	 * @param weight
	 * @return
	 */
	@RequestMapping(value = "/saveUpdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse update(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "weight", required = true) int weight) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicPropertyGroup dynamicPropertyGroup1 = dynamicPropertyGroupService.getDynaPropGroupById(id);
			if (dynamicPropertyGroup1 == null) {
				return jsonResponse.setError("动态属性组不存在！");
			}
			DynamicPropertyGroup dynamicPropertyGroup = new DynamicPropertyGroup();
			dynamicPropertyGroup.setId(id);
			dynamicPropertyGroup.setName(name);
			dynamicPropertyGroup.setWeight(weight);
			dynamicPropertyGroup.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyGroupService.update(dynamicPropertyGroup);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("保存修改动态属性组e：" + e.getMessage());
		}
	}

	/**
	 * 获取属性组
	 * @return
	 */
	@RequestMapping(value = "/getDynaPropGroupList")
	@ResponseBody
	public JsonResponse getDynaPropGroupList() {
		JsonResponse jsonResponse = new JsonResponse();
		Wrapper<DynamicPropertyGroup> wrapper = new EntityWrapper<DynamicPropertyGroup>().orderBy("weight", false).ge("id", "1");
		try {
			List<DynamicPropertyGroup> list = dynamicPropertyGroupService.getDynaPropGroupList(wrapper);
			List<Map<String, Object>> listMap = new ArrayList<>();
			for (DynamicPropertyGroup dynamicPropertyGroup : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", dynamicPropertyGroup.getId());
				map.put("name", dynamicPropertyGroup.getName());
				listMap.add(map);
			}
			jsonResponse.setSuccessful().setData(listMap);
			return jsonResponse;
		} catch (Exception e) {
			return jsonResponse.setError("获取动态属性组e：" + e.getMessage());
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
