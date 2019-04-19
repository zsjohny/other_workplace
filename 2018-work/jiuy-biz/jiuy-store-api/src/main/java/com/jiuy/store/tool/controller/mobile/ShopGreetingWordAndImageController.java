package com.jiuy.store.tool.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家
 * @author Administrator
 *
 */
@Controller
@Login
@RequestMapping("/shop/greeting")
public class ShopGreetingWordAndImageController {
	
//	private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;
	
	private static final Logger logger = LoggerFactory.getLogger(ShopGreetingWordAndImageController.class);
	
    Log log = LogFactory.get();
	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
//	
//	@Autowired
//	private OSSFileUtil ossFileUtil;
	
	 /**
     * 设置小程序问候消息
     * greetingSendType:问候消息发送类型:0:没有设置;1:问候语;2:问候图片
     * @return
     */
    @RequestMapping("/addShopGreetingMessage/auth")
    @ResponseBody
    public JsonResponse addShopGreetingMessage(@RequestParam(name="greetingImage") String greetingImage,
    		@RequestParam(name="greetingWords") String greetingWords,
    		@RequestParam(name="greetingSendType") Integer greetingSendType,//问候消息发送类型:0:没有设置;1:问候语;2:问候图片
    		UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//设置小程序问候消息
       	 	logger.info("设置小程序问候消息storeId:"+storeId+":greetingImage:"+greetingImage+":greetingWords:"+greetingWords
       	 			+";greetingSendType:"+greetingSendType);
       	 	int record = storeBusinessNewService.addShopGreetingMessage(storeId,greetingImage,greetingWords,greetingSendType);
       	 	if(record!=1){
       	 		throw new RuntimeException("设置小程序问候消息有误");
       	 	}
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("设置小程序问候消息exception:"+e.getMessage());
			return jsonResponse.setError("设置小程序问候消息有误");
		}
    	
    }
    
    /**
     * 删除小程序问候语图片
     * @return
     */
    @RequestMapping("/delShopGreetingImage/auth")
    @ResponseBody
    public JsonResponse delShopGreetingImage(UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//删除小程序问候语图片
       	 	logger.info("删除小程序问候语图片storeId:"+storeId);
       	 	int record = storeBusinessNewService.delShopGreetingImage(storeId);
       	 	if(record!=1){
       	 		throw new RuntimeException("删除小程序问候语图片有误");
       	 	}
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("删除小程序问候语图片exception:"+e.getMessage());
			return jsonResponse.setError("删除小程序问候语图片有误");
		}
    	
    }
    
    /**
     * 小程序问候语和图片回显
     * @return
     */
    @RequestMapping("/getShopGreetingMessage/auth")
    @ResponseBody
    public JsonResponse getShopGreetingMessage(UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		//小程序问候语和图片回显
       	 	logger.info("小程序问候语和图片回显storeId:"+storeId);
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
    		StoreBusiness storeBusiness = userDetail.getUserDetail();
       	 	
       	 	if(storeBusiness==null){
       	 		throw new RuntimeException("小程序问候语和图片回显有误");
       	 	}
       	 	Map<String,Object> data = new HashMap<String,Object>();
       	 	Integer greetingSendType = storeBusiness.getGreetingSendType();
	       	if(greetingSendType==null){
	       		greetingSendType = 0;
	       	}
       	 	data.put("greetingSendType", greetingSendType);
       	 	data.put("greetingWords", storeBusiness.getGreetingWords());
       	 	data.put("greetingImage", storeBusiness.getGreetingImage());
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("小程序问候语和图片回显exception:"+e.getMessage());
			return jsonResponse.setError("小程序问候语和图片回显有误");
		}
    	
    }
    
//    /**
//	 * 上传文件到阿里云OSS上
//	 *  创建内容: 1）上传文件到OSS中 
//	 *  2） 将文件名存储到session中
//	 *
//	 * @param request
//	 * @param response
//	 * @return 例子：{"filename":https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15087254452311508725445231.jpg}
//	 *  method = RequestMethod.POST
//	 */
//	@RequestMapping(value = "/ossUpload")
////    @AdminOperationLog
//	@ResponseBody
////	public Map<String, String> uploadImageFromSession(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
//	public Map<String, String> ossUpload(@RequestPart("file") MultipartFile file, ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) {
//		String path = null;
//		String oldPath = request.getParameter("oldPath");
////		String needWaterMark = request.getParameter("need_water_mark");
//		Map<String, String> result = new HashMap<String, String>();
//		
//		try {
//			if (request instanceof MultipartHttpServletRequest) {
//				logger.debug("yes you are!");
////				MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
////				MultipartFile file = multiservlet.getFile("file");
//				if (file == null) {
//					logger.error("请求中没有file对象，请排查问题" );
//					logger.error("request file null oldPath:" + oldPath);
//					return result;
//				}
//				logger.debug("request file name :" + file.getName());
//				path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
//				
//				//覆盖旧路径则删除
//				if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
//					String key = oldPath.split("/")[oldPath.split("/").length - 1];
//					ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
//				}
//			} else {
//				logger.debug("no wrong request!");
//			}
//			modelMap.addAttribute("images", path);
//			result.put("filename", path);
//			logger.info("上传文件接口返回数据，result:"+result.toString());
//			return result;
//		} catch (IOException e) {
//			logger.error("上传文件出现异常", e);
//		}
//		return result;
//	}
}