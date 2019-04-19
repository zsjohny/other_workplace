package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuy.core.service.homepage.HomeFloorService;
import com.jiuy.core.service.homepage.HomeTemplateService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/template")
@Login
public class TemplateController{
	
	@Resource
	private HomeTemplateService homeTemplateService;
	
	@Resource
	private HomeFloorService homeFloorService;
	
	/* 后期用HomePageController 里的接口 /template/{id}/content */
	@RequestMapping(value="/{id}/content",method = RequestMethod.GET)  
	@ResponseBody
	public JsonResponse homeContent(@PathVariable(value = "id")long floorId, 
			@RequestParam(value = "template_name") String templateName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		HomeFloorVO hf = homeFloorService.searchById(floorId);
		
		if(hf == null) {
			data.put("list", null);
            data.put("has_data", false);
            // data.put("template_id", 0);
			
		} else if(!StringUtils.equals(hf.getHomeTemplateName(), templateName)){
			data.put("list", null);
            data.put("has_data", false);
            // data.put("template_id", hf.getNextHomeTemplateId());
			
        } else {
            HomeTemplate homeTemplate = homeTemplateService.loadTemplate(hf.getNextHomeTemplateId());
            String carouseljson = homeTemplate.getContent();

            JSONArray jsonArray = StringUtils.isBlank(carouseljson) ? new JSONArray() : JSON.parseArray(carouseljson);
            data.put("list", jsonArray);
            data.put("has_data", true);
            // data.put("template_id", hf.getNextHomeTemplateId());
		}
		
		return jsonResponse.setSuccessful().setData(data);
	}

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @AdminOperationLog
    @Deprecated
    public JsonResponse addTemplate(@RequestParam(value = "floor_id") long floorId,
                                    @RequestParam(value = "content") String content,
                                    @RequestParam(value = "name") String name,
                                    @RequestParam(value = "imgurl") String imgUrl) {
        JsonResponse jsonResponse = new JsonResponse();
        HomeTemplate ht = new HomeTemplate();

        ht.setName(name);
        ht.setContent(content);
        ht.setImgUrl(imgUrl);

        homeTemplateService.addTemplate(floorId, ht);

        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }

}
