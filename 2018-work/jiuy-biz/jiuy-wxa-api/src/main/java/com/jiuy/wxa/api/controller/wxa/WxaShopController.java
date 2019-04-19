package com.jiuy.wxa.api.controller.wxa;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.store.entity.BusinessInformation;
import com.store.service.IBusinessInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.MemberService;
import com.store.service.StoreUserService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.util.file.FileUtil;

/**
 * <p>
 * 小程序商家相关
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/miniapp/shop")
public class WxaShopController {
    private static final Logger logger = LoggerFactory.getLogger(WxaShopController.class);
    
    Log log = LogFactory.get();
    
    private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;
    
    @Autowired
    MemberService memberService;
    @Autowired
    private StoreUserService storeUserService;
    
    @Resource(name = "ossFileUtil")
    private FileUtil fileUtil;

	@Autowired
	private IBusinessInformationService businessInformationService;



	/**
     * 获取商家信息
     * @return
	 * ShopDetail shopDetail,MemberDetail memberDetail,
     */
    @RequestMapping("/getShopInfo")
    @ResponseBody
    public JsonResponse getMyMemberInfo(Long storeId,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	try {
    		Map<String, Object> data = new HashMap(8);
    		StoreBusiness storeBusiness =businessInformationService.selectStoreBusiness(storeId);
    		data.put("storeId", String.valueOf(storeBusiness.getId()));
    		data.put("name", storeBusiness.getBusinessName());
    		data.put("address", storeBusiness.getBusinessAddress());
    		data.put("phone", storeBusiness.getPhoneNumber());
    		data.put("wxaType", String.valueOf(storeBusiness.getWxaType()));//小程序类型：0个人、1企业小程序

			BusinessInformation bInfo = businessInformationService.findInformationByUserId (storeBusiness.getId ());
			if (null != bInfo) {
				data.put ("businessInfo", bInfo);
			}
			else {
				data.put ("businessInfo", "");
			}

			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		logger.info("获取商家信息错误请排查问题！！！！！！！！！！");
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

}