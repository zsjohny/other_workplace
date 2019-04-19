package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.jiuyuan.util.HttpRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.product.ProductSKUService;
import com.jiuyuan.util.VideoSignatureUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;


@Controller
@RequestMapping("/video")
public class VideoSignatureController {

	private static final Log logger = LogFactory.get("VideoSignatureController");
	@Autowired
    private ProductSKUService productSKUService;
    
	/**
	 * 获取上传视频签名
	 * @return
	 */
    @RequestMapping(value = "/getsign")
    @ResponseBody
    public JsonResponse getsign() {
    	JsonResponse jsonResponse = new JsonResponse();

    	VideoSignatureUtil sign = new VideoSignatureUtil();
        sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
        sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";
        sign.m_qwNowTime = System.currentTimeMillis() / 1000;
        sign.m_iRandom = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.m_iSignValidDuration = 3600 * 24 * 2;

    	Map<String,Object> data = new HashMap<String,Object>();
    	data.put("sign", sign.getUploadSignature());
        return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
	 * 删除视频接口
	 * @return
	 */
//    @RequestMapping(value = "/deletevideo")
//    @ResponseBody
//    public JsonResponse deleteVideo(@RequestParam("productId")long productId) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
//    		productSKUService.deleteVideo(productId);
//            return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//	        return jsonResponse.setError("删除视频失败"+e.getMessage());
//		}
//    }
    
    /**
     * 删除视频签名接口
     * @param productId
     * @param fileId
     * @param priority
     * @return
     */
    @RequestMapping(value = "/deletevideo")
    @ResponseBody
    public JsonResponse getDeleteVideoSign(@RequestParam("productId")long productId,
    		@RequestParam("fileId")long fileId,@RequestParam(value="priority",required=false,defaultValue="1")int priority) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		productSKUService.deleteVideo(productId);
            
        	VideoSignatureUtil sign = new VideoSignatureUtil();
        	sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
            sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";

        	Map<String,Object> data = new HashMap<String,Object>();
        	data.put("url", sign.getDeleteSignatureG(fileId,priority));
            return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
	        return jsonResponse.setError("获取删除视频签名失败"+e.getMessage());
		}
    }


	/**
	 * 删除视频签名接口
	 * @param fileId
	 * @param priority
	 * @return
	 */
	@RequestMapping(value = "/deletevideoOnly")
	@ResponseBody
	public JsonResponse deletevideoOnly(@RequestParam("fileId")long fileId,@RequestParam(value="priority",required=false,defaultValue="1")int priority) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			VideoSignatureUtil sign = new VideoSignatureUtil();
			sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
			sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";
			Map<String,Object> data = new HashMap<String,Object>();
			String url = sign.getDeleteSignatureG(fileId,priority);
			data.put("url", url);
			HttpRequest.sendGet(url,"","utf-8");
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取删除视频签名失败"+e.getMessage());
		}
	}
}