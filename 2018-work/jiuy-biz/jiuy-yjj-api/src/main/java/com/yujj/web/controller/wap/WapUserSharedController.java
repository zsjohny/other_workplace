/**
 * 微信分享相关页面
 */
package com.yujj.web.controller.wap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSharedRecord;

import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ProductFacade;
import com.yujj.business.service.GlobalSettingParseService;
import com.yujj.business.service.IdService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserSharedService;
import com.yujj.business.service.globalsetting.bean.InvitationSetting;
import com.yujj.business.service.globalsetting.bean.JiucoinGlobalSetting;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;

/**
* @author Qiu Yuefan
* @version 创建时间: 2017年4月11日 上午11:10:40
*/
@Login
@Controller
@RequestMapping("/wap/shared")
public class WapUserSharedController {

    private static final Logger logger = LoggerFactory.getLogger("WapUserSharedController");
    @Autowired
    private IdService idService;
     @Autowired
     private UserSharedService userSharedService;
     @Autowired
     private ProductService productService;

     @Autowired
     private ProductFacade productFacade;
    
     @Autowired
 	 private GlobalSettingParseService globalSettingParseService;

     
     
     
     
     /**
      * 添加用户分享记录
      * @param type	分享内容类型 0：其他 1：商品 2:文章
      * @param relatedId	相关id
      * @param channel	分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博
      * @param userDetail
      * @return
      */
    @NoLogin
    @RequestMapping(value = "/add")
    @ResponseBody
    public JsonResponse add(@RequestParam("relatedId") long relatedId,@RequestParam("channel") int channel,@RequestParam("userSharedRecordId") long userSharedRecordId,@RequestParam("type") int type,UserDetail userDetail) {
    	UserSharedRecord userSharedRecord = new UserSharedRecord();
    	userSharedRecord.setId(userSharedRecordId);
    	userSharedRecord.setType(type);
    	userSharedRecord.setRelatedId(relatedId);
    	userSharedRecord.setChannel(channel);
     	userSharedRecord.setUserId(userDetail.getUserId());//分享记录用户ID为0表示用户未登陆
    	return userSharedService.addUserSharedRecord(userSharedRecord);
    }
     
     /**
      * 初始化商品分享信息
      * @param productId	商品ID
      * @param channel 暂时没有用到	分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博
      * @param userDetail
      * @return
      */
    @NoLogin
    @RequestMapping(value = "/initShareInfo")
    @ResponseBody
    public JsonResponse initShareInfo(@RequestParam("productId") int productId,@RequestParam("channel") int channel ,UserDetail userDetail) {
    	//TODO 待处理
    	 JsonResponse jsonResponse = new JsonResponse();
    	 Product product = productService.getProductById(productId);
         if (product == null) {
             return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
         }
        //分享信息  map：userSharedRecordId（预生成分享记录ID）,title（标题）,imageUrl（图标地址）,description（描述）,url（链接地址）,grant_coin（分享得到玖币数量）
        Map<String, String> shareInfo = userSharedService.getProductUserShareInfo(userDetail.getUserId(), productId, ClientPlatform.getWeiXinClient());
        //获得分享信息
        Map<String, Object> incomeConfigInfo = new HashMap<>();
        //TODO 从全局配置中获取中获取
        JiucoinGlobalSetting jiucoinGlobalSetting = globalSettingParseService.getJiucoin_global_setting();
        InvitationSetting invitationSetting  = jiucoinGlobalSetting.getInvitationSetting();
        incomeConfigInfo.put("registerCoinCount", invitationSetting.getEachObtain());
        incomeConfigInfo.put("orderCoinCount", invitationSetting.getReturnPercentage());
        Map<String, Object> data = new HashMap<>();
        data.put("shareInfo", shareInfo);
        data.put("incomeConfigInfo", incomeConfigInfo);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    
    /**
     * 获取当前用户的分享列表
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/mySharedList", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse mySharedList(UserDetail userDetail) {
    	return userSharedService.mySharedList(userDetail.getUserId());
    }
}
