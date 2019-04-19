package com.jiuy.wxa.api.controller.wxa;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.order.ShopMemberOrderRbQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.order.IMemberOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.store.service.MemberService;
import com.store.service.StoreWxaService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.file.FileUtil;

/**
 * <p>
 * 小程序会员表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/miniapp/member")
public class WxaMemberController {
	private static final Logger logger = LoggerFactory.getLogger(WxaMemberController.class);

	Log log = LogFactory.get();

	private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;

	@Autowired
	MemberService memberService;

	@Autowired
	StoreWxaService storeWxaService;

	@Resource(name = "ossFileUtil")
	private FileUtil fileUtil;

	@Autowired
	private ICouponServerNew couponServerNew;

	@Resource(name = "memberOrderService")
	private IMemberOrderService memberOrderService;

	/**
	 * 获取我的信息
	 * @return
	 */
	@RequestMapping("/myInfo")
	@ResponseBody
	public JsonResponse getMyMemberInfo(Long storeId,Long memberId,HttpServletResponse response ,@ClientIp String ip,
										ClientPlatform client
	) {
		JsonResponse jsonResponse = new JsonResponse();
		try {

			Map<String,Object> data = memberService.getMineMemberInfo(memberId,storeId);

			Map<String,Object> param = new HashMap<>(4);
			param.put("memberId",memberId);
			param.put("sysType",CouponSysEnum.WXA.getCode());
			param.put("publishUserId",storeId);
			List<CouponTemplateNew> temps = couponServerNew.waitGetCoupon(param);

			int couponCount =0;
			for (CouponTemplateNew temp : temps) {
				if (temp==null){
					continue;
				}
//				long storeId = memberDetail.getMember().getStoreId().longValue();
				String getRule = temp.getGetRule();
				JSONObject sas = (JSONObject) JSONObject.parse(getRule);
				Object storeIdss = sas.get("storeIds");

				if (storeIdss == null) {
					//平台发布
					if (temp.getPublishUserId().longValue() == 0) {

					} else {
						if (temp.getPublishUserId().longValue() != storeId) {
							//只能本店领取
							continue;
						}
					}
				} else {
					if (storeId != Long.parseLong(storeIdss.toString())) {
						//只能指定门店领取
						continue;
					}
				}
				couponCount++;
			}

			CouponRbNewQuery query = new CouponRbNewQuery();
			query.setStatus(0);
			query.setAliveType(1);
			query.setMemberId(memberId);
			int count  = couponServerNew.useAbleCouponCount(query);

			Map<String,String> memberInfo = (Map<String, String>) data.get("memberInfo");

			ShopMemberOrderRbQuery query_team = new ShopMemberOrderRbQuery();

			query_team.setStoreId(storeId);
			query_team.setMemberId(memberId);
			Integer teamCount = memberOrderService.teamBuyActivityUnderwayCount(query_team);

			memberInfo.put("waitTeamActivityCount",teamCount.toString());

			memberInfo.put("waitGetShopCouponTemplateCount",couponCount+"");
			//会员可用优惠券数量
			memberInfo.put("myUsableCouponCount", String.valueOf(count));

			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 修改我的头像/昵称
	 * @param key	想要修改的字段 userNickname:用户昵称 userIcon:用户头像
	 * @param value	想要修改的字段对应的值
	 * @return
	 */
	@RequestMapping("/updatefieldvalue")
	@ResponseBody
	public JsonResponse updateFieldValue(HttpServletRequest request,Long memberId,
										 @RequestParam("key") String key,@RequestParam("value") String value,@RequestParam("oldPath") String oldPath) {
		JsonResponse jsonResponse = new JsonResponse();
		try {

			if("userIcon".equals(key)){
				if (request instanceof MultipartHttpServletRequest) {
					//覆盖旧路径则删除
					if(StringUtils.isNotEmpty(StringUtils.trim(oldPath)) && oldPath.contains("aliyuncs.com")){
						String oldKey = oldPath.split("/")[oldPath.split("/").length - 1];
						fileUtil.removeFile(DEFAULT_BASEPATH_NAME, oldKey);
					}
					MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
					MultipartFile file = multiservlet.getFile("userIcon");
					value = fileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
				}
			}
			memberService.updateFieldValue(memberId,key,value);
			return jsonResponse.setSuccessful();
		} catch (IOException e) {
			e.printStackTrace();
			return jsonResponse.setResultCode(ResultCode.USER_UPLOAD_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

}