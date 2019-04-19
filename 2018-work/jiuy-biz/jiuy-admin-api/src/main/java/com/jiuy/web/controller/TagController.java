package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.TagFacade;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.tag.TagService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.tag.TagVO;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/tag")
@Controller
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private TagFacade tagFacade;
	
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestParam("name") String name,
			@RequestParam("priority") Integer priority,
			@RequestParam(value = "groupId", defaultValue = "-1") Long groupId,
			@RequestParam(value = "description", required = false) String description) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Tag tag = new Tag(name, priority, groupId, description);
		try {
			tagService.add(tag);
		} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(@RequestParam("id") Long id,
			@RequestParam("group_id") Long groupId) {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			tagFacade.delete(id, groupId);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse update(@RequestParam("id") Long id,
			@RequestParam("name") String name,
			@RequestParam("priority") Integer priority,
			@RequestParam("group_id") Long groupId,
			@RequestParam(value = "description", required = false) String description) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Tag tag = new Tag(id, name, priority, groupId, description);
		try {
			tagService.update(tag);
		} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
    /**
     * @param pageQuery
     * @param name 标签名
     * @param groupName 标签组名
     * @param countMin
     * @param countMax
     * @param productCountMin
     * @param productCountMax
     * @param groupId 标签组页面搜索为-1，标签页面搜索为0。为了标签页和标签组页共用一条搜索SQL语句
     * @return
     */
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "group_name", required = false) String groupName,
			@RequestParam(value = "count_min", required = false) Integer countMin,
			@RequestParam(value = "count_max", required = false) Integer countMax,
			@RequestParam(value = "product_count_min", required = false) Integer productCountMin,
			@RequestParam(value = "product_count_max", required = false) Integer productCountMax,
			@RequestParam(value = "group_id") Integer groupId,
			@RequestParam(value = "isTop", required = false,defaultValue="0") Integer isTop) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		int recordCount = tagFacade.searchCount(name, countMin, countMax, productCountMin, productCountMax, groupId, groupName,isTop);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
		
		List<TagVO> tagsVO = tagFacade.search(pageQuery, name, countMin, countMax, productCountMin, productCountMax, groupId, groupName,isTop);
		
		data.put("list", tagsVO);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	

	
	/**
     * 不分页查询
     * 
     * @param groupId null：所有标签和标签组，-1：所有组合标签搜索，>0：某一标签组下的标签
     * @return
     */
	@RequestMapping("/nonsort/search")
	@ResponseBody
	public JsonResponse nonsortSortSearch(@RequestParam(value = "group_id", required = false) Long groupId,
			@RequestParam(value = "isTop", required = false,defaultValue="0") int isTop) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Tag> tags = tagService.search(groupId, null,isTop);
		data.put("tags", tags);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 推荐/取消推荐
	 * @param tagId
	 * @param isTop
	 * @return
	 */
	@RequestMapping("/updTagTop")
	@ResponseBody
	public JsonResponse updTagTop(@RequestParam("tagId")long tagId,
			@RequestParam("isTop") long isTop) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int record = tagService.updTagTop(tagId,isTop);
			if(record!=1){
				return jsonResponse.setError("推荐/取消推荐失败");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("推荐/取消推荐失败");
		}
	}
}
