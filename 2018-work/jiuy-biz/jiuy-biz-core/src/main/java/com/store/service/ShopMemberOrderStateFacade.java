package com.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import com.jiuyuan.entity.order.OrderStateVO;
import com.jiuyuan.service.common.StoreTeamBuyActivityService;
import com.jiuyuan.util.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;


@Service
public class ShopMemberOrderStateFacade {
	private static final Log logger = LogFactory.get("ShopMemberOrderFacade");
	
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
	
	@Autowired
	private ShopProductService shopProductService;
	
	@Autowired
	private TeamBuyActivityMapper teamBuyActivityMapper;
	

	/**
	 * 获取订单状态信息
	 * @param orderId
	 * @return
	 */
	public List<OrderStateVO> getOrderStateInfo(long orderId) {
		List<OrderStateVO> list = new ArrayList<OrderStateVO>();
		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(orderId);
		//订单状态：0:待付款;1:已付款;2:退款中;3:订单关闭;4:订单完成
		int orderStatus = shopMemberOrder.getOrderStatus();
		
		switch (orderStatus) {
		case 0://0:待付款;
			list.add(getDaiFuKuan(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		case 1://1:已付款;
			list.add(getDaiTiHuo(shopMemberOrder));
			list.add(getYiFuKuan(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		case 2://2:退款中;
			logger.info("订单状态暂无，请尽快排查问题！！！！");
			break;
		case 3://3:订单关闭;
			list.add(getJiaoYiGuanBi(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		case 4://4:订单完成
			list.add(getJiaoYiWanCheng(shopMemberOrder));
			if (shopMemberOrder.getOrderType() == ShopMemberOrder.order_type_delivery) {
				list.add(getYiFaHuo(shopMemberOrder));
			}
			list.add(getYiFuKuan(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		case 5://5:待发货
			list.add(getDaiFaHuo(shopMemberOrder));
			list.add(getYiFuKuan(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		case 6://6:已发货/待收货
			list.add(getYiFaHuo(shopMemberOrder));
			list.add(getYiFuKuan(shopMemberOrder));
			list.add(getXiaDanCengGou(shopMemberOrder));
			break;
		default:
			logger.info("订单状态无法识别，请尽快排查问题！！！！");
			break;
		}
		return list;
	}
	/**
	 * 下单成功
	 * @return
	 */
	private OrderStateVO getXiaDanCengGou(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setName("下单成功");
		orderStateVO.setTime(DateUtil.parseLongTime2Str(shopMemberOrder.getCreateTime()));
		orderStateVO.setTip1("订单号："+shopMemberOrder.getOrderNumber());
//		订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
		int orderType = shopMemberOrder.getOrderType();
		String orderTypeName = "到店提货";
		if(orderType == 1){
			orderTypeName = "送货上门";
		}
		orderStateVO.setTip2( "配送方式："+orderTypeName);
		
		//购买方式
		int buyWay = shopMemberOrder.getBuyWay();
		String buyWayName ="";
		if (buyWay == ShopMemberOrder.buy_way_miaosha) {
			buyWayName ="秒杀";
			orderStateVO.setTip3( "购买方式："+buyWayName);
		}
		if (buyWay == ShopMemberOrder.buy_way_tuangou) {
			buyWayName ="团购";
			orderStateVO.setTip3( "购买方式："+buyWayName);
		}
		
		return orderStateVO;
	}
	/**
	 * 待付款
	 * @return
	 */
	private OrderStateVO getDaiFuKuan(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_DaiFuKuan);
		orderStateVO.setName("待付款");
		orderStateVO.setTime( DateUtil.parseLongTime2Str(shopMemberOrder.getCreateTime()));
		
//		orderStateVO.setTip1( "超过24小时未支付的订单将被自动取消");
//		orderStateVO.setTip2( "订单为您保留");
//		long overdueTime = shopMemberOrder.getCreateTime()+ 24*60*60*1000;
//		long nowTime = System.currentTimeMillis();
//		orderStateVO.setSurplusPayTime(String.valueOf(overdueTime - nowTime) );
		
		orderStateVO.setTip2( "订单为您保留");
		if (shopMemberOrder.getBuyWay() ==ShopMemberOrder.buy_way_putong) {
			orderStateVO.setTip1( "超过24小时未支付的订单将被自动取消");
			long overdueTime = shopMemberOrder.getCreateTime()+ 24*60*60*1000;
			long nowTime = System.currentTimeMillis();
			orderStateVO.setSurplusPayTime(String.valueOf(overdueTime - nowTime) );
		}
		if (shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_miaosha) {
			orderStateVO.setTip1( "超过2小时未支付的订单将被自动取消");
			long overdueTime = shopMemberOrder.getCreateTime()+ 2*60*60*1000;
			long nowTime = System.currentTimeMillis();
			orderStateVO.setSurplusPayTime(String.valueOf(overdueTime - nowTime) );
		}
		if (shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_tuangou) {
			//查询团购结束时间
			TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId());
			Long activityEndTime = teamBuyActivity.getActivityEndTime();
			orderStateVO.setTip1( "活动结束时未支付的订单将被自动取消");
			orderStateVO.setSurplusPayTime(String.valueOf(activityEndTime-System.currentTimeMillis()));
		}
		
		return orderStateVO;
	}
	/**
	 * 已付款
	 * @return
	 */
	private OrderStateVO  getYiFuKuan(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_YiFuKuan);
		orderStateVO.setName( "已付款");
		orderStateVO.setTime( DateUtil.parseLongTime2Str(shopMemberOrder.getPayTime()));
		orderStateVO.setTip1( "交易编号："+shopMemberOrder.getPaymentNo());
		orderStateVO.setTip2("实付金额：￥"+shopMemberOrder.getPayMoney());
		return orderStateVO;
	}
	/**
	 * 待发货
	 * @return
	 */
	private OrderStateVO  getDaiFaHuo(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_DaiFaHuoFuo);
		orderStateVO.setName( "待卖家发货");
		orderStateVO.setTip1("收货人："+shopMemberOrder.getReceiverName());
		orderStateVO.setTip2("收货地址："+shopMemberOrder.getReceiverAddress());
		return orderStateVO;
	}
	
	/**
	 * 已发货
	 * @return
	 */
	private OrderStateVO  getYiFaHuo(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_YiFaHuoFuo);
		orderStateVO.setName( "卖家已发货");
		orderStateVO.setTime(DateUtil.parseLongTime2Str(shopMemberOrder.getDeliveryTime()));//发货时间
		orderStateVO.setTip1("收货人："+shopMemberOrder.getReceiverName());
		orderStateVO.setTip2("收货地址："+shopMemberOrder.getReceiverAddress());
		//倒计时信息
		long remainTime = 15*24*60*60*1000+ shopMemberOrder.getDeliveryTime() - System.currentTimeMillis();
		int remainDay= (int) (remainTime/(24*60*60*1000));
		int remainHour = (int) ((remainTime-remainDay*24*60*60*1000)/(60*60*1000));
		orderStateVO.setTip3("还剩"+remainDay+"天"+remainHour+"时自动确认收货");
		//物流信息
		return orderStateVO;
	}
	/**
	 * 交易完成
	 * @return
	 */
	private OrderStateVO getJiaoYiWanCheng(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_JiaoYiWanCheng);
		orderStateVO.setName( "交易完成");
		orderStateVO.setTime(DateUtil.parseLongTime2Str(shopMemberOrder.getOrderFinishTime()));
		return orderStateVO;
	}
	/**
	 * 交易关闭
	 * @return
	 */
	private OrderStateVO getJiaoYiGuanBi(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_JiaoYiGuanBi);
		orderStateVO.setName("交易关闭");
		orderStateVO.setTime(DateUtil.parseLongTime2Str(shopMemberOrder.getOrderStopTime()));
		//取消类型：0无、1会员取消、2商家取消、3系统自动取消
		int cancelReasonType = shopMemberOrder.getCancelReasonType();
		switch (cancelReasonType) {
		case 0:
			logger.info("订单状态取消类型无，请尽快排查问题！！！！");
			break;
		case 1:
			orderStateVO.setTip1("您已取消订单");
			break;
		case 2:
			orderStateVO.setTip1("抱歉，您购买的商品目前缺货，有货了再通知您！退款请联系客服");
			break;
		case 3:
			//如果购买方式不为普通方式
			if (shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_tuangou) {
				orderStateVO.setTip1("活动已结束，订单已被系统自动取消");
				break;
				}
			if (shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_miaosha) {
				orderStateVO.setTip1("超过2小时未支付，订单已被系统自动取消");
				break;
				}
			orderStateVO.setTip1("超过24小时未支付，订单已被系统自动取消");
			break;
		case 4:
			orderStateVO.setTip1("活动已结束，订单已被系统自动取消");
			break;
		default:
			logger.info("订单状态取消类型无法识别，请尽快排查问题！！！！"+"cancelReasonType:"+cancelReasonType);
			break;
		}
		return orderStateVO;
	}
	
	/**
	 * 待提货
	 * @return
	 */
	private OrderStateVO getDaiTiHuo(ShopMemberOrder shopMemberOrder) {
		OrderStateVO orderStateVO= new OrderStateVO();
		orderStateVO.setOrderStatus(shopMemberOrder.getOrderStatus());
		orderStateVO.setType(OrderStateVO.type_DaiTiHuo);
		orderStateVO.setName( "待提货");
		return orderStateVO;
	}
	
	
}

