package com.jiuy.store.api.tool.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ShopMemberReservationsOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.shop.IShopMemberReservationsOrderService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 预约试穿订单
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@Login
@RequestMapping("/shop/reservations/order")
public class ShopReservationsOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ShopReservationsOrderController.class);
    Log log = LogFactory.get();
    
	@Autowired
	private IShopMemberReservationsOrderService shopMemberReservationsOrderService;
	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	
	/**
	 * 开启/关闭预约试穿功能
	 * @param status 预约试穿功能状态：0：关闭；1：开启
	 * @param userDetail
	 * @return
	 */
    @RequestMapping("/switchShopReservations/auth")
    @ResponseBody
    public JsonResponse switchShopReservations(@RequestParam(value="status")int status,
    		UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		logger.info("开启/关闭预约试穿功能storeId:"+storeId+",status:"+status);
    		if(storeId<=0){
    			throw new RuntimeException("开启/关闭预约试穿功能storeId:"+storeId+",status:"+status);
    		}
            int record = storeBusinessNewService.switchShopReservations(storeId,status);
            if(record!=1){
            	throw new RuntimeException("开启/关闭预约试穿功能storeId:"+storeId+",status:"+status+",record:"+record);
            }
            
            return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("开启/关闭预约试穿功能有误:"+e.getMessage());
			return jsonResponse.setError("开启/关闭预约试穿功能有误");
		}
    }
	
    /**
     * 获取预约试穿订单管理列表
     * @param userDetail
     * @param page
     * @param keyWord
     * @return
     */
    @RequestMapping("/getReservationsOrderList/auth")
    @ResponseBody
    public JsonResponse getReservationsOrderList(UserDetail<StoreBusiness> userDetail,Page<ShopMemberReservationsOrder> page,
    		@RequestParam(value="keyWord",required=false,defaultValue="") String keyWord) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		logger.info("获取预约试穿订单管理列表storeId:"+storeId+";keyWord:"+keyWord);
    		if(storeId<=0){
    			throw new RuntimeException("获取预约试穿订单管理列表无法获取到用户信息storeId:"+storeId);
    		}
        	Map<String,Object> data = new HashMap<String,Object>();
            List<ShopMemberReservationsOrder> shopMemberReservationsOrderList = shopMemberReservationsOrderService.getReservationsOrderList(storeId,keyWord,page);
            List<Map<String,Object>> shopMemberReservationsOrderListData = this.encapsulatedShopMemberReservationsOrderListData(shopMemberReservationsOrderList);
            Page<Map<String,Object>> pageRecord = new Page<Map<String,Object>>(page.getCurrent(), page.getSize());
            pageRecord.setTotal(page.getTotal());
            
            pageRecord.setRecords(shopMemberReservationsOrderListData);
            data.put("page", pageRecord);
            
            return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取预约试穿订单管理列表有误:"+e.getMessage());
			return jsonResponse.setError("获取预约试穿订单管理列表有误");
		}
    }

    /**
     * 获取预约试穿订单管理列表
     * @param shopMemberReservationsOrderList
     * @return
     */
	private List<Map<String, Object>> encapsulatedShopMemberReservationsOrderListData(
			List<ShopMemberReservationsOrder> shopMemberReservationsOrderList) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, Object>> shopMemberReservationsOrderListData = new ArrayList<Map<String,Object>>();
		for (ShopMemberReservationsOrder shopMemberReservationsOrder : shopMemberReservationsOrderList) {
			Map<String,Object> shopMemberReservationsOrderData = new HashMap<String,Object>();
			shopMemberReservationsOrderData.put("shopMemberReservationsOrderId", shopMemberReservationsOrder.getId());//预约单号ID
			shopMemberReservationsOrderData.put("createTime", simpleDateFormat.format(new Date(shopMemberReservationsOrder.getCreateTime())));//预约试穿订单创建时间
			shopMemberReservationsOrderData.put("shopMemberName", shopMemberReservationsOrder.getShopMemberName());//预约人姓名
			shopMemberReservationsOrderData.put("shopMemberPhone", shopMemberReservationsOrder.getShopMemberPhone());//预约人电话号码
			shopMemberReservationsOrderData.put("appointmentTime", simpleDateFormat.format(new Date(shopMemberReservationsOrder.getAppointmentTime())));//预约时间
			if(shopMemberReservationsOrder.getOwn()==0){//平台商品
				shopMemberReservationsOrderData.put("shopProductInfo", shopMemberReservationsOrder.getShopProductName()+
						"(颜色:"+shopMemberReservationsOrder.getShopProductColorName()+",尺码:"+shopMemberReservationsOrder.getShopProductSizeName()+")");//预约商品
			}else if(shopMemberReservationsOrder.getOwn()==1){//自有商品
				shopMemberReservationsOrderData.put("shopProductInfo", shopMemberReservationsOrder.getShopProductName());//预约商品
			}
			shopMemberReservationsOrderListData.add(shopMemberReservationsOrderData);
		}
		return shopMemberReservationsOrderListData;
	}
	
	/**
     * 获取预约试穿订单详情
     * @param userDetail
     * @return
     */
    @RequestMapping("/getReservationsOrderInfo/auth")
    @ResponseBody
    public JsonResponse getReservationsOrderInfo(UserDetail<StoreBusiness> userDetail,
    		@RequestParam(value="shopMemberReservationsOrderId") long shopMemberReservationsOrderId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		logger.info("获取预约试穿订单详情shopMemberReservationsOrderId:"+shopMemberReservationsOrderId);
    		if(storeId<=0){
    			throw new RuntimeException("获取预约试穿订单详情无法获取到用户信息storeId:"+storeId);
    		}
        	Map<String,Object> data = new HashMap<String,Object>();
            ShopMemberReservationsOrder shopMemberReservationsOrder = shopMemberReservationsOrderService.getReservationsOrderInfo(shopMemberReservationsOrderId);
            Map<String,Object> shopMemberReservationsOrderInfo = this.encapsulatedShopMemberReservationsOrderData(shopMemberReservationsOrder);
            data.put("shopMemberReservationsOrder", shopMemberReservationsOrderInfo);
            
            return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取预约试穿订单详情有误:"+e.getMessage());
			return jsonResponse.setError("获取预约试穿订单详情有误");
		}
    }

    /**
     * 获取预约试穿订单详情
     * @param shopMemberReservationsOrder
     * @return
     */
	private Map<String, Object> encapsulatedShopMemberReservationsOrderData(
			ShopMemberReservationsOrder shopMemberReservationsOrder) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Map<String,Object> shopMemberReservationsOrderData = new HashMap<String,Object>();
		shopMemberReservationsOrderData.put("shopMemberReservationsOrderId", shopMemberReservationsOrder.getId());//预约单号ID
		shopMemberReservationsOrderData.put("shopProductId", shopMemberReservationsOrder.getShopProductId());//小程序商品ID
		shopMemberReservationsOrderData.put("createTime", simpleDateFormat.format(new Date(shopMemberReservationsOrder.getCreateTime())));//预约试穿订单创建时间
		shopMemberReservationsOrderData.put("shopMemberName", shopMemberReservationsOrder.getShopMemberName());//预约人姓名
		shopMemberReservationsOrderData.put("shopMemberPhone", shopMemberReservationsOrder.getShopMemberPhone());//预约人电话号码
		shopMemberReservationsOrderData.put("appointmentTime", simpleDateFormat.format(new Date(shopMemberReservationsOrder.getAppointmentTime())));//预约时间
		int own = shopMemberReservationsOrder.getOwn();
		shopMemberReservationsOrderData.put("own", own);//是否是自有商品：1是自有商品，0平台商品
		if(own==0){//平台商品
			shopMemberReservationsOrderData.put("shopProductInfo", shopMemberReservationsOrder.getShopProductName()+
					"(颜色:"+shopMemberReservationsOrder.getShopProductColorName()+",尺码:"+shopMemberReservationsOrder.getShopProductSizeName()+")");//预约商品
			shopMemberReservationsOrderData.put("color", shopMemberReservationsOrder.getShopProductColorName());//颜色
			shopMemberReservationsOrderData.put("size", shopMemberReservationsOrder.getShopProductSizeName());//尺码
		}else if(own==1){//自有商品
			shopMemberReservationsOrderData.put("shopProductInfo", shopMemberReservationsOrder.getShopProductName());//预约商品
		}
		shopMemberReservationsOrderData.put("shopProductName", shopMemberReservationsOrder.getShopProductName());//商品标题
		shopMemberReservationsOrderData.put("clothesNumber", shopMemberReservationsOrder.getClothesNumber());//商品款号
		shopMemberReservationsOrderData.put("summarImage", shopMemberReservationsOrder.getSummaryImage());//商品图片
		Double price = shopMemberReservationsOrder.getPrice();
		shopMemberReservationsOrderData.put("price", price==null?"":price);//门店价
		return shopMemberReservationsOrderData;
	}
}