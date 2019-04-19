package com.store.service.coupon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.DateUtil;
import com.store.entity.coupon.ShopCouponTemplate;
import com.store.entity.member.ShopMember;
import com.store.enumerate.Member48MarkEnum;
import com.store.service.MemberService;
import com.store.service.StoreBusinessServiceShop;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 发送商家优惠券模板通知
*/

@Service
public class SendShopCouponNotificationFacade{
	private static final Log logger = LogFactory.get("SendShopCouponNotificationService");
	
	@Autowired
	private ShopCouponTemplateService shopCouponTemplateService;
	@Autowired
	private StoreBusinessServiceShop storeBusinessService;
	
	@Autowired
	private MemberService memberService;
	
	public void sendShopCouponNotification() {
		//1、获取全部开通小程序商家列表
		List<StoreBusiness> storeBusinessList = storeBusinessService.getAllOpenWxaStoreList();
		
		//2、循环全部开通小程序商家列表
		for(StoreBusiness storeBusiness: storeBusinessList){
			long storeId = storeBusiness.getId();
			int isOpenWxa = storeBusiness.getIsOpenWxa();
			if(isOpenWxa != 1){
				logger.info("商家未开通小程序无需发送通知");
			}else{
				//logger.info("开通小程序了,storeBusiness:"+JSONObject.toJSONString(storeBusiness));
				//3、获取商家上一天创建的优惠券信息
				long startCreateTime =0; //DateUtil.getTodayStart();//上一天凌晨
				long endCreateTime = 0;//DateUtil.getTodayEnd();//今天凌晨
				
				List<ShopCouponTemplate> canUseShopCouponTemplateList = shopCouponTemplateService.getCanUseShopCouponTemplateList(storeId,startCreateTime,endCreateTime);
				sendToShopMember(canUseShopCouponTemplateList,storeId);
			}
			
		}
		
		//3、将优惠券进行发送模板通知
		
	}
	/**
	 * 发送通知到商家会员
	 * @param canUseShopCouponTemplateList
	 */
	private void sendToShopMember(List<ShopCouponTemplate> canUseShopCouponTemplateList,long storeId) {
		if(canUseShopCouponTemplateList.size() <=0){
			//logger.info("该商家上一天没有创建的优惠券无需发送通知");
		}else{
			//logger.info("该商家上一天创建了优惠券张数:"+canUseShopCouponTemplateList.size());
			//4、做发送内容
			String sendContent = "发现"+canUseShopCouponTemplateList.size()+"张优惠券";
			
			//4、获取商家小程序会员列表
			List<ShopMember> memberList = memberService.getMemberList(Member48MarkEnum.all, storeId);
			for(ShopMember member : memberList){
				//5、发送通知
				send(member,sendContent);
				//6、记录发送通知 TODO v2.2
			}
		}
	}
	
	/**
	 * 发送通知到商家会员
	 * @param member
	 * @param sendContent
	 */
	private void send(ShopMember member, String sendContent) {
		//logger.info("发送通知到商家会员待实现！！！！！！！！！！！！！！");
	}
	public static void main(String[] args) {
		long startCreateTime = DateUtil.getTodayStart();//上一天凌晨
		long endCreateTime = DateUtil.getTodayEnd();//今天凌晨
		System.out.println("startCreateTime:"+startCreateTime);
		System.out.println("endCreateTime:"+endCreateTime);
	}
}