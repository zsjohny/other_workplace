package com.yujj.web.controller.mobile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.comment.CommentGroupVO;
import com.jiuyuan.entity.comment.CommentVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.CommentFacade;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.CommentService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.UserCoinService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.comment.Comment;
import com.yujj.entity.order.OrderItem;

@Login
@Controller
@RequestMapping("/mobile/comment")
public class MobileCommentController {
	
	@Autowired
	private CommentFacade commentFacade;

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserCoinService userCoinService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private CommentService commentService;
    
	@NoLogin
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse listComment(@RequestParam("productId") long productId, PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
        int totalCount = commentFacade.getProductCommentCount(productId);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        
		List<CommentVO> comments = commentFacade.getCommentList(productId, pageQuery);
		
		int aveLiker = 0;
		for (CommentVO commentVO : comments) {
			aveLiker += commentVO.getLiker();
		}
		if (comments.size() != 0)
			aveLiker /= comments.size();

		data.put("data", comments);
		data.put("aveLiker", aveLiker);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public JsonResponse updateComment(@RequestParam("commentId") long id,
			@RequestParam("liker") int liker,
			@RequestParam("content") String content,
			@RequestParam("imageUrl") String imageUrl,
			@RequestParam("anonymity") int anonymity,
			UserDetail userDetail,ClientPlatform clientPlatform) {
		
		JsonResponse jsonResponse = new JsonResponse();
		int nRet = 0;
		
		Comment comment = new Comment();
		comment.setId(id);
		comment.setLiker(liker);
		comment.setContent(content);
		comment.setImageUrl(imageUrl);
		comment.setAnonymity(anonymity);
		
		nRet = commentFacade.updateComment(comment);

        Map<String, Object> data = new HashMap<String, Object>();
        
        if (nRet > 0) {
        	String activityCode = "comment";

            Activity activity = activityService.getActivity(activityCode);
            
            if (activity == null) {
            	return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_ACTIVITY_NOT_EXISTS);
            }

            long time = System.currentTimeMillis();
//            UserCoinOperation operation = UserCoinOperation.ACTIVITY;
//            int grantAmount = 0;
////           	grantAmount = RandomUtils.nextInt(activity.getGrantAmountMin(), activity.getGrantAmountMax());
//           	if(VersionUtil.compareVersion(clientPlatform.getVersion() , "1.8.11") < 0){
//           		
//           		userCoinService.updateUserCoin(userDetail.getUserId(), 0, grantAmount, activityCode, time, operation, null);
//           	}else{
//           		userCoinService.updateUserCoin(userDetail.getUserId(), 0, 0, activityCode, time, operation, null);
//           		
//           	}
           	
           	Comment cm = commentService.getById(id);
    		double totalMoney = 0.00;
    		List<OrderItem> ois = orderService.getOrderNewItems(userDetail.getUserId(), Arrays.asList(new Long[]{cm.getOrderNo()}));
    		for (OrderItem orderItem : ois) {
    			if (orderItem.getProductId() == cm.getProductId()) {
    				totalMoney += orderItem.getTotalPay();
    			}
    		}
    		
    		JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    		JSONObject shoppingJiuCoinSetting = jiucoin_global_setting.getJSONObject("shoppingJiuCoinSetting");
    		int evaluationPercentage = shoppingJiuCoinSetting.getInteger("evaluationPercentage");
    		JSONObject jiuCoinAttribute = jiucoin_global_setting.getJSONObject("jiuCoinAttribute");
			double worthRmb = jiuCoinAttribute.getDouble("worthRmb");
    		
    		int grantAmount = (int)(totalMoney * evaluationPercentage / 100 / worthRmb);
    		userCoinService.updateUserCoin(userDetail.getUserId(), 0, grantAmount, id + "", time, UserCoinOperation.COMMENT, null, clientPlatform.getVersion());
        	
            data.put("grantAmount", grantAmount);	
    		return jsonResponse.setSuccessful().setCode(nRet).setData(data);
        }
        else {
        	return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }		
	}
	//删除旧表
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResponse addComment(@RequestParam("productId") long productId,
//			@RequestParam("skuId") long skuId,
//			@RequestParam("brandId") long brandId, 
//			@RequestParam("orderId") long orderId, 
//			UserDetail userDetail) {
//		
//		JsonResponse jsonResponse = new JsonResponse();
//		int nRet = 0;
//		
//		List<OrderItem> orderItems = new ArrayList<OrderItem>();
//		OrderNew orderNew = orderService.queryOrderNewFromOrderId(orderId);
//		
//		OrderItem orderItem = new OrderItem();
//		orderItem.setOrderId(orderId);
//		orderItem.setOrderNo(orderNew.getOrderNo());
//		orderItem.setUserId(userDetail.getUserId());
//		orderItem.setProductId(productId);
//		orderItem.setSkuId(skuId);
//		orderItem.setBrandId(brandId);
//
//		orderItems.add(orderItem);
//		orderItems.add(orderItem);
//		orderItems.add(orderItem);
//		orderItems.add(orderItem);
//		orderItems.add(orderItem);
//		nRet = commentFacade.addComment(orderItems);
//		
//		return jsonResponse.setSuccessful().setCode(nRet);
//	}
	
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse productComment(PageQuery pageQuery, UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> banner = new HashMap<String, Object>();

        int totalCount = commentFacade.getUserCommentCount(userDetail.getUserId());
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
		
		List<CommentGroupVO> commentGroups = commentFacade.getCommentProduct(userDetail.getUserId(), pageQuery);
		
        banner.put("url", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/comment/xiu.png");
        banner.put("linkurl", Constants.SERVER_URL + "/static/app/article.html?id=13");
		
		data.put("data", commentGroups);
		data.put("banner", banner);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse DeleteComment(@RequestParam("commentId") long id,
			UserDetail userDetail) {
		
		JsonResponse jsonResponse = new JsonResponse();
		int nRet = 0;
		
		nRet = commentFacade.deleteComment(id);
		
		return jsonResponse.setSuccessful().setCode(nRet);
	}	
}
