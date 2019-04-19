package com.jiuy.wxa.api.controller.wxa;

import java.util.Map;

import com.jiuy.rb.mapper.user.StoreBusinessRbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.WxaMemberVisitService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 会员访问（足迹）Controller
* @author Qiuyuefan
*/
@Controller
//@Login
@RequestMapping("/miniapp/member/visit")
public class WxaMemberVisitController {

	private static final Log logger = LogFactory.get();
    @Autowired
    private WxaMemberVisitService shopMemberVisitService;

	@Autowired
	private StoreBusinessRbMapper storeBusinessRbMapper;


    /**
     * 添加或者修改会员访问记录
     * @param memberId
     * @param productId
     * @param type
     * @return
     */
    @RequestMapping("/addupdate")
    @ResponseBody
    public JsonResponse addUpdateMemberVisitRecord(ShopDetail shopDetail,MemberDetail memberDetail,
    		@RequestParam("productId")Long productId,
    		@RequestParam("memberId")Long memberId,
    		@RequestParam("storeId")Long storeId,
    		@RequestParam("type")Integer type) {
//    	logger.info("添加或者修改会员访问记录:"+shopDetail.getId()+"---"+memberDetail.getId());

//    	JsonResponse jsonResponse = new JsonResponse();
    	return JsonResponse.successful();
    	/*try {
//    		checkStore(shopDetail);
//   	 		checkMember(memberDetail);
			String appId = storeBusinessRbMapper.findAppId(storeId);
    		shopMemberVisitService.addUpdateMemberVisitRecord(memberId,productId,type,storeId,appId);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}*/
    }  
    
    /**
     * 获取会员访问列表
     * @param memberId
     * @param current
     * @param size
     * @return
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public JsonResponse getMemberVisitList(ShopDetail shopDetail,MemberDetail memberDetail,
    		@RequestParam(value="current",required=false,defaultValue="1")Integer current,
		   @RequestParam("memberId")Long memberId,
		   @RequestParam("storeId")Long storeId,
    		@RequestParam(value="size",required=false,defaultValue="100")Integer size) {
//    	logger.error("获取会员访问列表:"+shopDetail.getId()+"---"+memberDetail.getId());
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
////    		checkStore(shopDetail);
////   	 		checkMember(memberDetail);
//			String appId = storeBusinessRbMapper.findAppId(storeId);
//			Map<String,Object> data = shopMemberVisitService.getMemberVisitList(memberId,new Page(current,size),storeId, appId);
//    		return jsonResponse.setSuccessful().setData(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
		return JsonResponse.successful();
	}
    
//    /**
//     * 删除会员访问记录
//     * @param id
//     * @return
//     */
//    @RequestMapping("/delete")
//    @ResponseBody
//    public JsonResponse deleteMemberVisitRecord(ShopDetail shopDetail,MemberDetail memberDetail,
//    		@RequestParam("visitId")Long visitId) {
//    	JsonResponse jsonResponse = new JsonResponse();
//		try {
////			checkStore(shopDetail);
////   	 		checkMember(memberDetail);
//			shopMemberVisitService.deleteMemberVisitRecord(visitId);
//			return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
//    }
    

}
