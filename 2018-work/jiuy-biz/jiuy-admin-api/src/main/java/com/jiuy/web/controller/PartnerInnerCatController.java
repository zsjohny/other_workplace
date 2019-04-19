package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.partner.PartnerInnerCatService;
import com.jiuyuan.entity.brand.PartnerInnerCat;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/partnerInnerCat")
@Login
public class PartnerInnerCatController {
	
	public final int PAGE_SIZE = 4;
	
	@Resource
	private PartnerInnerCatService partnerInnerCatServiceImpl;
    
    /**
     * 品牌管理-品牌自定义分类-页面
     * @return
     */
    @AdminOperationLog
    @RequestMapping("/index")
    public String customCategory() {
    	return "page/backend/innercat";
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResponse innerCat(@RequestParam(value="page", required=false, defaultValue="1")int page,
    		@RequestParam(value="name", required=false, defaultValue="")String name,
    		@RequestParam(value="partner_id")long partnerId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	PageQuery pageQuery = new PageQuery(page, PAGE_SIZE);
    	PageQueryResult pageQueryResult = new PageQueryResult();
    	BeanUtils.copyProperties(pageQuery, pageQueryResult);
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	List<PartnerInnerCat> list = partnerInnerCatServiceImpl.search(name, partnerId);
    	int count = partnerInnerCatServiceImpl.searchCount(name, partnerId);
    	pageQueryResult.setRecordCount(count);
    	
    	data.put("list", list);
    	data.put("total", pageQueryResult);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/add")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse addInnerCat(@RequestBody PartnerInnerCat pic) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = partnerInnerCatServiceImpl.addInnerCat(pic);
    	
    	return jsonResponse.setSuccessful().setData(success);
    }
    
    @RequestMapping("/update")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updateInnerCat(@RequestBody PartnerInnerCat pic) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = partnerInnerCatServiceImpl.updateInnerCat(pic);
    	
    	return jsonResponse.setSuccessful().setData(success);
    }
    
    @RequestMapping("/remove")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse removeInnerCat(@RequestParam(value="id") long id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	boolean success = partnerInnerCatServiceImpl.removeInnerCat(id);
    	
    	return jsonResponse.setSuccessful().setData(success);
    }
    
}
