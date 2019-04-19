package com.jiuy.wxa.api.controller.wxa;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jiuy.base.util.HttpUtils;
import com.jiuyuan.service.common.area.ActivityInfoCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.service.WxaqrCodeImageUtil;
import com.jiuyuan.service.common.ShopProductNewService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 小程序商品分享图片
 */
@Controller
@RequestMapping("/shopProduct/detail/share")
public class WxaProductDetailShareController {
	
	private static final Log logger = LogFactory.get("小程序商品分享图片");
	
//	private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;
	
	@Autowired
    private ShopProductNewService shopProductService;
	

	
	/**
	 * 获取小程序商品分享图片 3.7.7以前版本
	 * @param shopDetail
	 * @param memberDetail
	 * @param shopProductId
	 * @param oldPath
	 * @return
	 */
//	@RequestMapping(value = "/getImage")
//	@ResponseBody
//	@Deprecated
//	public JsonResponse getShopProductShareImage_bak(ShopDetail shopDetail,MemberDetail memberDetail,
//    		@RequestParam("shopProductId")Long shopProductId,HttpServletRequest request, @RequestParam(value = "oldPath", required = false) String oldPath) {
////		checkStore(shopDetail);
////		checkMember(memberDetail);
//		logger.info("小程序商品分享图片:shopProductId:"+shopProductId+",storeId:"+shopDetail.getId()+",memberId:"+memberDetail.getId());
//		JsonResponse jsonResponse = new JsonResponse();
//		try {
//			Map<String,Object> data = new HashMap<String,Object>();
//			ShopProduct shopProductOld = shopProductService.getShopProduct(shopProductId);
//			String wxaProductShareImage = shopProductOld.getWxaProductShareImage();
//			String wxaProductShareOldImages = shopProductOld.getWxaProductShareOldImages();
//			if(StringUtils.isEmpty(wxaProductShareImage)){
//				//分享图片为空
//				//小程序分享拼接图片上传
//				wxaProductShareImage = shopProductService.getShareImage(shopProductOld,request);
//			}else{
//				//判断商品分享长图使用的橱窗图是否有变化
//				if(StringUtils.isEmpty(wxaProductShareOldImages)){
//					logger.error("小程序商品分享图片:没有商品分享长图使用的橱窗图：shopProductId:"+shopProductId+"wxaProductShareOldImages："+wxaProductShareOldImages);
//				}else{
//					boolean flag = shopProductService.checkShopProducImages(shopProductOld);
//					if(flag){
//						//小程序分享拼接图片上传更新
//						wxaProductShareImage = shopProductService.getShareImage(shopProductOld,request);
//					}
//				}
//			}
//			logger.info("小程序商品分享图片返回数据data:"+JSON.toJSONString(data)+":shopProductId:"+shopProductId+",storeId:"+shopDetail.getId()+",memberId:"+memberDetail.getId());
//
//			//阿里云图片地址转为服务器本地地址
//			String serverImg = WxaqrCodeImageUtil.aliImgToServerImg(wxaProductShareImage,request);
//
//
//			data.put("wxaProductShareImage", serverImg);//商品分享长图
////			data.put("wxaProductShareImageServerImg", serverImg);//商品分享长图服务器地址
//			logger.info("获取小程序商品分享图片返回数据data："+JSON.toJSONString(data));
//			return jsonResponse.setSuccessful().setData(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("小程序商品分享图片:"+e.getMessage());
//			return jsonResponse.setError(e.getMessage());
//		}
//
//	}


	/**
	 * 获取商品的分享图片 377版本(向以前版本兼容)
	 *
	 * @param shopProductId shopProductId
	 * @param request request
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/7/17 17:31
	 */
	@RequestMapping(value = "/getImage")
	@ResponseBody
	public JsonResponse getShopProductShareImageV377(Long storeId,Long memberId,
		 @RequestParam("shopProductId")Long shopProductId,
		 @RequestParam(value = "activityId", required = false)Long activityId,
		 @RequestParam(value = "targetType", required = false)Integer targetType,
		 HttpServletRequest request)
	{

//		checkStore(shopDetail);
//		checkMember(memberDetail);
		logger.info("小程序商品分享图片:shopProductId:"+shopProductId+",storeId:"+storeId+",memberId:"+memberId);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//获取图片
			//版本
			Integer version = HttpUtils.wxVersion (request.getHeader ("version"), 140);
			//放入活动和version,做版本兼容
			ActivityInfoCache.createInstance (activityId, targetType, version);
			String wxaProductShareImage = shopProductService.getShareCompositeImgV377 (shopProductId,storeId);
			logger.info("小程序商品分享图片返回数据data:shopProductId:"+shopProductId+",storeId:"+storeId+",memberId:"+memberId);
			String[] fileNames = wxaProductShareImage.split("/");

			//阿里云图片地址转为服务器本地地址
			String serverImg = "https://wxaonline.yujiejie.com/download/"+fileNames[fileNames.length-1];
//			String serverImg = WxaqrCodeImageUtil.aliImgToServerImg(wxaProductShareImage,request);

			Map<String,Object> data = new HashMap();
			data.put("wxaProductShareImage", serverImg);//商品分享长图
			logger.info("获取小程序商品分享图片返回数据data："+JSON.toJSONString(data));
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("小程序商品分享图片:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		} finally {
			ActivityInfoCache.clear ();
		}

	}

	public static void main(String[] args) {
		String[] split = "https://wxaonline.yujiejie.com/download/15466061534731546606153473.jpg".split("/");
		System.out.println(split[split.length-1]);
	}


	/**
	 * 检验门店是否为空
	 */
	private void checkStore(ShopDetail shopDetail) {
	 	if(shopDetail==null || shopDetail.getId() == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
	 	}
	}
	
	/**
	 * 检验会员是否为空
	 */
	private void checkMember(MemberDetail memberDetail) {
	 	if(memberDetail==null || memberDetail.getId() == 0){
	 		logger.info("会员信息不能为空，该接口需要登陆，请排除问题");
	 		throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
	 	}
	}

}