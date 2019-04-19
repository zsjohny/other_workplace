/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProxyCustomerMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyProductMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyUserMapper;
import com.jiuyuan.entity.newentity.ProxyCustomer;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.entity.newentity.ProxyUser;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.TipsMessageException;

/**
 * 新商品SKU服务
 */

@Service
public class ProxyCustomerService implements IProxyCustomerService  {
	private static final Logger logger = LoggerFactory.getLogger(ProxyCustomerService.class);
	@Autowired
	private ProxyCustomerMapper proxyCustomerMapper;
	

	@Autowired
	private ProxyUserMapper proxyUserMapper;
	
	@Autowired
	private ProxyOrderMapper proxyOrderMapper;
	
	@Autowired
	private ProxyProductMapper proxyProductMapper;
	@Autowired
	private ProxyProductService proxyProductService;
	@Autowired
	private StoreBusinessNewService storeBusinessNewService;
	
	
	public void addProxyCustomer(ProxyOrder order) {
		String phone = order.getApplyPhone();
		long proxyUserId = order.getProxyUserId();
		long proxyProductId = order.getProxyProductId();
		StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessByPhone(phone);
		ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
		long now = System.currentTimeMillis();//产品服务开通时间
		
		ProxyCustomer proxyCustomer = getProxyCustomerByPhone(phone,proxyUserId);
		if(proxyCustomer == null ){
			ProxyCustomer addProxyCustomer = new ProxyCustomer();
			addProxyCustomer.setStoreId(storeBusiness.getId());
			addProxyCustomer.setName(order.getApplyFullName());//客户姓名
			addProxyCustomer.setPhone(phone);//客户手机号
			addProxyCustomer.setProxyProductId(order.getProxyProductId());//开通服务产品ID
			addProxyCustomer.setProxyProductName(order.getProxyProductName());//开通服务产品名称
			addProxyCustomer.setProxyUserId(proxyUserId);//代理商ID
			
			int proxyProductCount = order.getProxyProductCount();//申请开通服务数量
			addProxyCustomer.setProxyProductCount(proxyProductCount);//开通服务数量
			addProxyCustomer.setProductOpenTime(now);//产品服务开通时间
			addProxyCustomer.setProductTotalOpenDay(proxyProduct.buildProductTotalOpenDay(proxyProductCount));	
			addProxyCustomer.setProductRenewProtectCloseTime(storeBusiness.getWxaRenewProtectCloseTime());	//产品服务续约保护截止时间
			addProxyCustomer.setProductCloseTime(storeBusiness.getWxaCloseTime());
			logger.info("添加代理商客户！");
			proxyCustomerMapper.insert(addProxyCustomer);
		}else{
			ProxyCustomer updProxyCustomer = new ProxyCustomer();
			updProxyCustomer.setId(proxyCustomer.getId());
			int proxyProductCount = order.getProxyProductCount();//申请开通服务数量
			updProxyCustomer.setProxyProductCount(proxyProductCount);//开通服务数量
			updProxyCustomer.setProductOpenTime(now);//产品服务开通时间
			updProxyCustomer.setProductTotalOpenDay(proxyProduct.buildProductTotalOpenDay(proxyProductCount));	
			updProxyCustomer.setProductRenewProtectCloseTime(storeBusiness.getWxaRenewProtectCloseTime());	//产品服务续约保护截止时间
			updProxyCustomer.setProductCloseTime(storeBusiness.getWxaCloseTime());
			proxyCustomerMapper.updateById(updProxyCustomer);
		}
	}

	public ProxyCustomer getProxyCustomerByPhone(String phone, long proxyUserId) {
		Wrapper<ProxyCustomer> wrapper = new EntityWrapper<ProxyCustomer>();
		wrapper.eq("proxy_user_id",proxyUserId);//代理商ID
		wrapper.eq("phone",phone);//手机号
		List<ProxyCustomer> list = proxyCustomerMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取代理商的客户数
	 */
	public int getProxyCustomerCount(long proxyUserId){
		Wrapper<ProxyCustomer> wrapper = new EntityWrapper<ProxyCustomer>();
		wrapper.eq("proxy_user_id",proxyUserId);//代理商ID
		int count = proxyCustomerMapper.selectCount(wrapper);
		return count;
	}
	/**
	 * 获取代理商的客户列表
	 */
	@Override
	public List<Map<String,Object>> list(String customerName, String customerPhone, int status, Integer maxSurplusDays,
			Integer minSurplusDays, long proxyUserId, Page<Map<String,Object>> page) {
		Long maxDays = null;
		Long minDays = null;
		if(maxSurplusDays != null){
			maxDays = maxSurplusDays.longValue()*24*60*60*1000;
		}
		if(minSurplusDays != null){
			minDays = minSurplusDays.longValue()*24*60*60*1000;
		}
		
		List<ProxyCustomer> list = proxyCustomerMapper.list(customerName, customerPhone, status, maxDays, minDays, proxyUserId, page);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(ProxyCustomer proxyCustomer : list){
        	Map<String,Object> map = new HashMap<String,Object>();
        	map.put("name", proxyCustomer.getName());//客户姓名
        	map.put("phone", proxyCustomer.getPhone());//客户手机号码
        	map.put("status", proxyCustomer.buildStatusName());//客户状态：已签约(0)、续约保护期中(1)
        	map.put("proxyProductName", proxyCustomer.getProxyProductName());//开通服务产品
        	map.put("productOpenTime", DateUtil.parseLongTime2Str(proxyCustomer.getProductOpenTime()));//开通时间
        	map.put("productCloseTime", DateUtil.parseLongTime2Str(proxyCustomer.getProductCloseTime()));//使用截止时间
        	map.put("productSurpluslOpenDay", proxyCustomer.getProductSurpluslOpenDay());//剩余使用天数
        	map.put("id", proxyCustomer.getId());//客户ID
        	result.add(map);
        }
		return result;
	}
	@Override
	public Map<String, Object> detail(long proxyUserId, long proxyCustomerId) {
		ProxyCustomer proxyCustomer = proxyCustomerMapper.selectById(proxyCustomerId);
		long currentTime = System.currentTimeMillis();
		Map<String,Object> map = new HashMap<>();
		map.put("phone", proxyCustomer.getPhone());//申请人手机号码
		map.put("name", proxyCustomer.getName());//申请人姓名
		map.put("status", proxyCustomer.buildStatusName());
		map.put("id", proxyCustomer.getId());
		//开通的服务产品
		map.put("proxyProductName", proxyCustomer.getProxyProductName());//开通服务产品名称
		map.put("proxyProductId", proxyCustomer.getProxyProductId());//开通服务产品ID
		
		map.put("productOpenTime", DateUtil.parseLongTime2Str(proxyCustomer.getProductOpenTime()));//产品服务开通时间
		map.put("productCloseTime", DateUtil.parseLongTime2Str(proxyCustomer.getProductCloseTime()));//服务使用截止时间
		
		map.put("proxyProductId", proxyCustomer.getProductSurpluslOpenDay()+"天");//剩余开通使用天数
		map.put("productSurpluslOpenDay", proxyCustomer.getProductSurpluslOpenDay());//服务剩余使用天数
		long productCloseTime =  proxyCustomer.getProductCloseTime();
		long storeId = proxyCustomer.getStoreId();
		// 需要添加申请信息提交
		if(currentTime >= proxyCustomer.getProductCloseTime()){
			map.put("proxyProductCount", 0);//开通服务数量
			map.put("totalConsumeAmount", 0.00);//门店宝进货消费总额
		}else{
			Double totalConsumeAmount = proxyCustomerMapper.getTotalConsumeAmount(storeId, productCloseTime);
			map.put("totalConsumeAmount", totalConsumeAmount);//门店宝进货消费总额
			map.put("proxyProductCount", proxyCustomer.getProxyProductCount());//开通服务数量
		}
		return map;
	}

	@Override
	public void applyMiniprogram(String applyName, String registerPhoneNumber, String comfirmPhoneNumber,
			long proxyUserId) {
		//校验
		//校验参数
		checkParam(registerPhoneNumber, comfirmPhoneNumber, proxyUserId);
		//校验申请
		checkApplyOrder(registerPhoneNumber, proxyUserId);
		//生成代理销售订单
		generateOrder(registerPhoneNumber, applyName, proxyUserId);
	}
	
	/**
	 * 生成代理销售订单
	 */
	private void generateOrder(String registerPhoneNumber, String applyName, long proxyUserId) {
		long currentTime = System.currentTimeMillis();
		ProxyOrder proxyOrder = new ProxyOrder();
		proxyOrder.setOrderState(ProxyOrder.orderState_new);//订单状态
		proxyOrder.setApplyFullName(applyName);//申请人姓名
		proxyOrder.setApplyPhone(registerPhoneNumber);//申请人手机号码
		ProxyUser proxyUser = proxyUserMapper.selectById(proxyUserId);//代理商
		proxyOrder.setProxyProductId(proxyUser.getProxyProductId());//申请开通服务产品ID
		ProxyProduct proxyProduct = proxyProductMapper.selectById(proxyUser.getProxyProductId());
		proxyOrder.setProxyProductName(proxyProduct.getName());//代理产品名称
		proxyOrder.setProxyProductCount(1);//申请开通服务数量
		proxyOrder.setProxyUserId(proxyUserId);//代理商ID
		proxyOrder.setProxyUserName(proxyUser.getProxyUserName());//代理商名称
		proxyOrder.setProxyUserNo(proxyUser.getProxyUserNum());//代理商编号
		proxyOrder.setCreateTime(currentTime);//创建时间
		proxyOrder.setUpdateTime(currentTime);//更新时间
		proxyOrderMapper.insert(proxyOrder);
	}
	/**
	 * 校验申请
	 */
	private void checkApplyOrder(String registerPhoneNumber, long proxyUserId) {
		long currentTime = System.currentTimeMillis();
		//正在处理中的申请单
		Wrapper<ProxyOrder> proxyOrderWrapper = new EntityWrapper<ProxyOrder>();
		proxyOrderWrapper.eq("apply_phone", registerPhoneNumber)
		                 .in("order_state","0,1");
		List<ProxyOrder> proxyOrderList = proxyOrderMapper.selectList(proxyOrderWrapper);
		if(proxyOrderList.size() > 0){
			logger.info("您有一个正在处理中的申请单，请耐心等待开通服务，若有更多疑问您也可以联系,proxyUserId:"+proxyUserId+",registerPhoneNumber:"+registerPhoneNumber+",用户已申请，且有一个未完成或未关闭的申请单");
			throw new TipsMessageException("1");//由于前端对提示语文案进行修改，只能传状态值
		}
		//查看用户正常使用小程序,由原代理商代理,其他代理商无法代理
		Wrapper<ProxyCustomer> proxyCustomerWrapper = new EntityWrapper<ProxyCustomer>();
		proxyCustomerWrapper.eq("phone", registerPhoneNumber)
		                    .ne("proxy_user_id",proxyUserId)
		                    .le("product_open_time", currentTime)
		                    .ge("product_close_time", currentTime);
		List<ProxyCustomer> proxyCustomerList = proxyCustomerMapper.selectList(proxyCustomerWrapper);
		if(proxyCustomerList.size() >0){
			logger.info("提交失败！请联系您的签约服务商家申请续费服务，若有更多疑问您也可以联系!registerPhoneNumber:"+registerPhoneNumber+",该用户正常使用小程序服务中，且服务合约生效中时");
			throw new TipsMessageException("2");//由于前端对提示语文案进行修改，只能传状态值
		}
		//小程序服务终止后，并且在商家签约保护期限内
		Wrapper<ProxyCustomer> proxyCustomerWrapper2 = new EntityWrapper<ProxyCustomer>();
		proxyCustomerWrapper2.eq("phone", registerPhoneNumber)
		                    .ne("proxy_user_id",proxyUserId)
		                    .lt("product_close_time", currentTime)
		                    .ge("product_renew_protect_close_time", currentTime);
		List<ProxyCustomer> proxyCustomerList2 = proxyCustomerMapper.selectList(proxyCustomerWrapper2);
		if(proxyCustomerList2.size() > 0){
			logger.info("提交失败！请联系您的原签约服务商家申请开通服务，若有更多疑问您也可以联系!registerPhoneNumber:"+registerPhoneNumber+",小程序服务终止后，并且在商家签约保护期限内");
			throw new TipsMessageException("3");//由于前端对提示语文案进行修改，只能传状态值
		}
		
	}
	/**
	 * 校验参数
	 */
	private void checkParam(String registerPhoneNumber, String comfirmPhoneNumber, long proxyUserId) {
		//手机号码
		if(!registerPhoneNumber.equals(comfirmPhoneNumber)){
			logger.info("注册手机号码与确认手机号码不匹配！");
			throw new TipsMessageException("注册手机号码与确认手机号码不匹配！请重新输入");
		}
		//代理商
		ProxyUser proxyUser = proxyUserMapper.selectById(proxyUserId);
		if(proxyUser == null){
			logger.info("该代理商不存在！proxyUserId:"+proxyUserId);
			throw new TipsMessageException("该代理商不存在！");
		}
		
		
	}


//	@Override
//	public Map<String, Object> getCustomerStatistics(long proxyUserId) {
//		long currentTime = System.currentTimeMillis();
//		Map<String,Object> map = new HashMap<String,Object>();
//		//获取客户总数
//		Wrapper<ProxyCustomer> totalCountWrapper = new EntityWrapper<ProxyCustomer>();
//		totalCountWrapper.eq("proxy_user_id", proxyUserId)
//		       .gt("product_renew_protect_close_time", currentTime);
//		int totalCount = proxyCustomerMapper.selectCount(totalCountWrapper);
//		//获取今日新增客户数
//		Wrapper<ProxyCustomer> todayAddWrapper = new EntityWrapper<ProxyCustomer>();
//		todayAddWrapper.eq("proxy_user_id", proxyUserId)
//		               .gt("product_renew_protect_close_time", currentTime)
//		               .ge("product_open_time", DateUtil.getTodayStart())
//		               .le("product_open_time", DateUtil.getTodayEnd());
//		int todayAddCount = proxyCustomerMapper.selectCount(todayAddWrapper);
//		//获取续约保护期客户
//		Wrapper<ProxyCustomer> protectPeriodWrapper = new EntityWrapper<ProxyCustomer>();
//		protectPeriodWrapper.eq("proxy_user_id", proxyUserId)
//                            .gt("product_renew_protect_close_time", currentTime)
//                            .le("product_close_time", currentTime);
//		int protectPeriodCount = proxyCustomerMapper.selectCount(protectPeriodWrapper);
//		
//		
//		map.put("totalCount", totalCount);
//		map.put("todayAddCount", todayAddCount);
//		map.put("protectPeriodCount", protectPeriodCount);
//		return map;
//	}
	
	

	@Override
	public Map<String, Object> getCustomerStatistics(long proxyUserId) {
		long currentTime = System.currentTimeMillis();
		Map<String,Object> map = new HashMap<String,Object>();
		//获取客户总数
		int totalCount = getTotalCount(proxyUserId, currentTime);
		
		//获取今日新增客户数
		int todayAddCount = getTodayAddCount(proxyUserId, currentTime);
		
		//获取续约保护期客户
		int protectPeriodCount = getProtectPeriodCustomerCount(proxyUserId, currentTime);
		
		
		map.put("totalCount", totalCount);
		map.put("todayAddCount", todayAddCount);
		map.put("protectPeriodCount", protectPeriodCount);
		return map;
	}
	/**
	 * 获取客户总数
	 * @param proxyUserId
	 * @param currentTime
	 * @return
	 */
	public int getTotalCount(long proxyUserId, long currentTime) {
		Wrapper<ProxyCustomer> totalCountWrapper = new EntityWrapper<ProxyCustomer>();
		totalCountWrapper.eq("proxy_user_id", proxyUserId)
		       .gt("product_renew_protect_close_time", currentTime);
		int totalCount = proxyCustomerMapper.selectCount(totalCountWrapper);
		return totalCount;
	}

	/**
	 * 获取今日新增客户数
	 * @param proxyUserId
	 * @param currentTime
	 * @return
	 */
	public int getTodayAddCount(long proxyUserId, long currentTime) {
    	Wrapper<ProxyCustomer> todayAddWrapper = new EntityWrapper<ProxyCustomer>();
		todayAddWrapper.eq("proxy_user_id", proxyUserId)
		               .gt("product_renew_protect_close_time", currentTime)
		               .ge("product_open_time", DateUtil.getTodayStart())
		               .le("product_open_time", DateUtil.getTodayEnd());
		int todayAddCount = proxyCustomerMapper.selectCount(todayAddWrapper);
		return todayAddCount;
	}

	/**
     * 获取续约保护期客户数
     * @param proxyUserId
     * @param currentTime
     */
	public int getProtectPeriodCustomerCount(long proxyUserId, long currentTime) {
		Wrapper<ProxyCustomer> protectPeriodWrapper = new EntityWrapper<ProxyCustomer>();
		protectPeriodWrapper.eq("proxy_user_id", proxyUserId)
                            .gt("product_renew_protect_close_time", currentTime)
                            .le("product_close_time", currentTime);
		int protectPeriodCount = proxyCustomerMapper.selectCount(protectPeriodWrapper);
		return protectPeriodCount;
		
	}
	
	/**
	 * 获取使用中的客户数
	 * @param proxyUserId
	 * @param currentTime
	 * @return
	 */
	public int getUsingPeriodCustomerCount(long proxyUserId, long currentTime){
		Wrapper<ProxyCustomer> usingPeriodCustomerCountWrapper = new EntityWrapper<ProxyCustomer>();
		usingPeriodCustomerCountWrapper.eq("proxy_user_id", proxyUserId)
                            .gt("product_close_time", currentTime)
                            .le("product_open_time", currentTime);
		int usingPeriodCustomerCount = proxyCustomerMapper.selectCount(usingPeriodCustomerCountWrapper);
		return usingPeriodCustomerCount;
	}
}