package com.wxa.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.log.AccessLog;
import com.jiuy.rb.service.common.ILogRbService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.member.ShopMember;
import com.store.service.MemberService;
import com.store.service.StoreUserService;

import java.util.Date;


/**
 * 初始化当前会员和当前门店
 * @author Administrator
 *
 */
public class UserDetailInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(UserDetailInterceptor.class);
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private StoreUserService userService;
    
	@Autowired
	private StoreBusinessNewService storeBusinessNewService;

	@Resource(name = "logRbService")
	private ILogRbService logRbService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	 ShopDetail shopDetail = new ShopDetail();
    	 MemberDetail memberDetail = new MemberDetail();
    	// 这里可以根据appid 和 memberId初始化当前用户和当前门店
    	
    	 String storeId = request.getParameter("storeId");
    	 String memberId = request.getParameter("memberId");
    	 String appId = request.getParameter("appId");
    	 logger.info("storeId={},memberId={},appId={},",storeId,memberId,appId);
    	if(StringUtils.isNotEmpty(storeId)){
    		StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(Long.parseLong(storeId));
    		if(storeBusiness != null){
    			shopDetail.setStoreBusiness(storeBusiness);
    		}else{
    			if(StringUtils.isNotEmpty(appId)){
    	    		StoreBusiness store = userService.getStoreBusinessByWxaAppId(appId);
    	    		if(store != null){
    	    			shopDetail.setStoreBusiness(store);
    	    		}
    	    	}
    		}
    	}
    	
    	if(StringUtils.isNotEmpty(memberId)){
    		ShopMember member = memberService.getMemberById(Long.parseLong(memberId));
    		if(member != null){
    			memberDetail.setMember(member);
    		}
    	}
        //放入请求中
    	request.setAttribute(Constants.KEY_WXA_SHOP_DETAIL, shopDetail);
    	request.setAttribute(Constants.KEY_WXA_MEMBER_DETAIL, memberDetail);


		ShopMember member = memberDetail.getMember();
		StoreBusiness storeBusiness = shopDetail.getStoreBusiness();
		if(member!=null  && storeBusiness!=null) {
			UserSession userSession = new UserSession();
			userSession.setMemberId(memberDetail.getMember().getId());
			userSession.setStoreId(shopDetail.getStoreBusiness().getId());
			userSession.setId(memberDetail.getMember().getId());
			UserSession.setUserSession(userSession);
		}


        return true;
    }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		UserSession.remove();
	}
}
