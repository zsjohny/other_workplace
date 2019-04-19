package com.jiuy.operator.modular.attrManage.controller;

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
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;
import com.jiuyuan.service.common.IDynamicPropertyCategoryService;
import com.jiuyuan.service.common.IDynamicPropertyGroupService;
import com.jiuyuan.service.common.IDynamicPropertyService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
/**
 * 
 * 动态属性组与商品分类控制器
 *
 */
@Controller
@RequestMapping("/actAttributeGroupCategory")
@Login
public class ActAttributeCategoryController extends BaseController {
	
	
	
	
	@Autowired
	private IDynamicPropertyGroupService dynamicPropertyGroupService;
	@Autowired
	private IDynamicPropertyCategoryService dynamicPropertyCategoryService;
	@Autowired
	private IDynamicPropertyService dynamicPropertyService;
	
	/**
	 * 类目关联动态属性设置
	 * @param cateGoryId
	 * @param status
	 * @return
	 */
	@RequestMapping("/getDynamicPropertyAndGroup")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse getDynamicPropertyAndGroup(@RequestParam(value="cateId",required=true)long cateId,
			@RequestParam(value="status",required=false,defaultValue="-1")int status){
		JsonResponse jsonResponse = new JsonResponse();
		
		Wrapper<DynamicPropertyGroup> wrapper = new EntityWrapper<DynamicPropertyGroup>().orderBy("weight", false);
		//获取动态属性组
		List<DynamicPropertyGroup> dynaPropGroupList = dynamicPropertyGroupService.getDynaPropGroupList(wrapper);
		//获取已选中的动态属性
		List<Map<String,Object>> dynamicPropertyCategorylist = dynamicPropertyCategoryService.getChoosedDynamicProperty(cateId,status);
		//对数据进行组装
		Map<String,Object> map=dynamicPropertyCategoryService.getDynamicPropertyAndGroup(dynamicPropertyCategorylist,dynaPropGroupList);
		
		return jsonResponse.setData(map).setSuccessful();
		
	}
	/**
	 * 根据关联状态进行筛选
	 * @param cateGoryId
	 * @param status
	 * @return
	 */
	@RequestMapping("/screen")
	@ResponseBody
	public JsonResponse screen(@RequestParam(value="cateId",required=true)long cateId,
			@RequestParam(value="openStatus",required=true)int openStatus,
			@RequestParam(value="offStatus",required=true)int offStatus){
		JsonResponse jsonResponse = new JsonResponse();
		List<Map<String,Object>> dynamicPropertyCategorylist=null;
		if (openStatus == 1 && offStatus == 1) {
			dynamicPropertyCategorylist = dynamicPropertyCategoryService.getChoosedDynamicProperty(cateId,-1);
		}
		if(openStatus ==1 && offStatus==0){
			dynamicPropertyCategorylist = dynamicPropertyCategoryService.getChoosedDynamicProperty(cateId,openStatus);
		}
		if(openStatus==0 && offStatus==1){
			dynamicPropertyCategorylist = dynamicPropertyCategoryService.getChoosedDynamicProperty(cateId,0);
		}
		if(openStatus==0 && offStatus==0){
			dynamicPropertyCategorylist = dynamicPropertyCategoryService.getChoosedDynamicProperty(cateId,-1);
		}
		return jsonResponse.setData(dynamicPropertyCategorylist).setSuccessful();
	}
	
	
	
	/**
	 * 启用或者禁用动态属性与分类关联关系
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/enableOrDisable")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse enableOrDisable(
			@RequestParam(value="dynaPropCateId",required=true)long dynaPropCateId,
			@RequestParam(value="status",required=true)int status){
		JsonResponse jsonResponse = new JsonResponse();
		try {
			DynamicPropertyCategory dynamicPropertyCategory1 =dynamicPropertyCategoryService.selectById(dynaPropCateId);
			if (dynamicPropertyCategory1==null ) {
				return jsonResponse.setError("分类与属性关联关系不存在");
			}
			DynamicPropertyCategory dynamicPropertyCategory=new DynamicPropertyCategory();
			dynamicPropertyCategory.setId(dynaPropCateId);
			dynamicPropertyCategory.setStatus(status);
			dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
			dynamicPropertyCategoryService.update(dynamicPropertyCategory);
			return jsonResponse.setData("ok").setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("启用或者禁用分类与属性关联关系"+e.getMessage());
		}
		
	}
	/**
	 * 新增动态属性与分类关联关系
	 * @param dynaPropIds
	 * @param cateId
	 * @return
	 */
	@RequestMapping("/addDynamicPropertyCategory")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addDynamicPropertyCategory(@RequestParam(value="dynaPropIds",required=true)String dynaPropIds,
			@RequestParam(value="cateId",required=true)long cateId){
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Wrapper<DynamicPropertyCategory> wrapper = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", cateId);
			
			int count = dynamicPropertyCategoryService.selectCount(wrapper);
			String[] dynaPropIdsArr = dynaPropIds.split(",");
			DynamicPropertyCategory dynamicPropertyCategory =null;
			for(int i=0;i<dynaPropIdsArr.length;i++){
				String dynaPropId=dynaPropIdsArr[i];
				DynamicProperty DynamicProperty = dynamicPropertyService.getDynamicPropertyById(Long.valueOf(dynaPropId));
				if (DynamicProperty==null) {
					return jsonResponse.setError("动态属性不存在 dynaPropID:"+dynaPropId);
				}
				Wrapper<DynamicPropertyCategory> wrapper1 = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", cateId).eq("dyna_prop_id", Long.valueOf(dynaPropId));
				List<DynamicPropertyCategory> propertyCategoryList=	dynamicPropertyCategoryService.selectList(wrapper1);
				if (propertyCategoryList.size()>0) {
					return jsonResponse.setError("该关联关系已存在 dynaPropID:"+dynaPropId);
				}
				dynamicPropertyCategory= new DynamicPropertyCategory();
				dynamicPropertyCategory.setCateId(cateId);
				dynamicPropertyCategory.setWeight(count+i+1);
				dynamicPropertyCategory.setCreateTime(System.currentTimeMillis());
				dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyCategory.setDynaPropId(Long.valueOf(dynaPropId));
				dynamicPropertyCategory.setStatus(1);//1:启用  0：禁用
				dynamicPropertyCategoryService.insert(dynamicPropertyCategory);
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("添加分类与动态属性关联关系e"+e.getMessage());
		}
		
		
	}
	
	/**
	 * 分类与属性关联关系排序
	 * @param dynaPropCateId
	 * @param direct
	 * @return
	 */
	@RequestMapping("/dynamicPropertyCategorySort")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse dynamicPropertyCategorySort(@RequestParam(value = "dynaPropCateId", required = true) long dynaPropCateId,
			@RequestParam(value="direct",required=true) int direct){
		
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//下移
			if (direct==0) {
				DynamicPropertyCategory smallDynamicPropertyCategory = dynamicPropertyCategoryService.selectById(dynaPropCateId);
				Wrapper<DynamicPropertyCategory> dynamicPropertyCategoryWrapper = new EntityWrapper<DynamicPropertyCategory>().eq("weight", smallDynamicPropertyCategory.getWeight()+1).eq("cate_id", smallDynamicPropertyCategory.getCateId());
				List<DynamicPropertyCategory> dynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(dynamicPropertyCategoryWrapper);
				if (dynamicPropertyCategoryList.size()<1) {
					return jsonResponse.setError("此属性排序值最大，无法下移");
				}
				DynamicPropertyCategory dynamicPropertyCategory=new DynamicPropertyCategory();
				//将上面属性值的排序值设置为当前属性值的排序值
				DynamicPropertyCategory bigDynamicPropertyCategory = dynamicPropertyCategoryList.get(0);
				
				dynamicPropertyCategory.setId(bigDynamicPropertyCategory.getId());
				dynamicPropertyCategory.setWeight(smallDynamicPropertyCategory.getWeight());
				dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyCategoryService.update(dynamicPropertyCategory);
				//将当前属性值的排序值设置上面属性值的排序值
				dynamicPropertyCategory=new DynamicPropertyCategory();
				dynamicPropertyCategory.setId(smallDynamicPropertyCategory.getId());
				dynamicPropertyCategory.setWeight(bigDynamicPropertyCategory.getWeight());
				dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyCategoryService.update(dynamicPropertyCategory);
			}
			
			//上移
			if (direct==1) {
				DynamicPropertyCategory upDynamicPropertyCategory = dynamicPropertyCategoryService.selectById(dynaPropCateId);
				Wrapper<DynamicPropertyCategory> dynamicPropertyCategoryWrapper = new EntityWrapper<DynamicPropertyCategory>().eq("weight", upDynamicPropertyCategory.getWeight()-1).eq("cate_id", upDynamicPropertyCategory.getCateId());
				List<DynamicPropertyCategory> dynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(dynamicPropertyCategoryWrapper);
				if (dynamicPropertyCategoryList.size()<1) {
					return jsonResponse.setError("此属性排序值最小，无法上移");
				}
				DynamicPropertyCategory dynamicPropertyCategory=new DynamicPropertyCategory();
				//将下面属性的排序值设置为当前属性值的排序值
				DynamicPropertyCategory underDynamicPropertyCategory = dynamicPropertyCategoryList.get(0);
				dynamicPropertyCategory.setId(underDynamicPropertyCategory.getId());
				dynamicPropertyCategory.setWeight(upDynamicPropertyCategory.getWeight());
				dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyCategoryService.update(dynamicPropertyCategory);
				//将当前属性的排序值设置上面属性值的排序值
				dynamicPropertyCategory=new DynamicPropertyCategory();
				dynamicPropertyCategory.setId(upDynamicPropertyCategory.getId());
				dynamicPropertyCategory.setWeight(underDynamicPropertyCategory.getWeight());
				dynamicPropertyCategory.setUpdateTime(System.currentTimeMillis());
				dynamicPropertyCategoryService.update(dynamicPropertyCategory);
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("分类与动态属性排序e：" + e.getMessage());
		}
	}
	
	
}
