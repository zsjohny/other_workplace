package com.jiuyuan.service.shop;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopMemberReservationsOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ShopNotificationMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ShopMemberReservationsOrder;
import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.util.GetuiUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 预约订单Service
*/

@Service
public class ShopMemberReservationsOrderService implements IShopMemberReservationsOrderService{
	
	private static final Log logger = LogFactory.get();

	@Autowired
	private ShopMemberReservationsOrderMapper shopMemberReservationsOrderMapper;
	
	@Autowired
	private ShopNotificationMapper shopNotificationMapper;
	
	@Autowired
	private ShopProductMapper shopProductMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Override
	public void addSubscribeOrder(long storeId, long memberId, long shopProductId, long platformProductSkuId,
								  String shopProductSizeName, String shopProductColorName, String shopMemberName, String shopMemberPhone,
								  String appointmentTime, String userCID) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ShopMemberReservationsOrder shopMemberReservationsOrder = new ShopMemberReservationsOrder();
		shopMemberReservationsOrder.setStoreId(storeId);
		shopMemberReservationsOrder.setShopMemberId(memberId);
		shopMemberReservationsOrder.setShopProductId(shopProductId);
		shopMemberReservationsOrder.setPlatformProductSkuId(platformProductSkuId);
		shopMemberReservationsOrder.setShopProductSizeName(shopProductSizeName);
		shopMemberReservationsOrder.setShopProductColorName(shopProductColorName);
		shopMemberReservationsOrder.setShopMemberName(shopMemberName);
		shopMemberReservationsOrder.setShopMemberPhone(shopMemberPhone);
		shopMemberReservationsOrder.setAppointmentTime(simpleDateFormat.parse(appointmentTime).getTime());
		shopMemberReservationsOrder.setCreateTime(System.currentTimeMillis());
		shopMemberReservationsOrder.setUpdateTime(shopMemberReservationsOrder.getCreateTime());
		
		ShopProduct shopProduct = shopProductMapper.selectById(shopProductId);
		shopMemberReservationsOrder.setOwn(shopProduct.getOwn());
		shopMemberReservationsOrder.setPrice(shopProduct.getPrice());
		String name = shopProduct.getName();
//		logger.info("预约试穿:name:"+name);
		
		if(shopProduct.getOwn()==0){//是否是自有商品：1是自有商品，0平台商品
			long productId = shopProduct.getProductId();
			ProductNew product = productNewMapper.selectById(productId);
			shopMemberReservationsOrder.setClothesNumber(product.getClothesNumber());
			shopMemberReservationsOrder.setShopProductName(product.getName());
			logger.info("添加预约订单时验证主图是否为空productId:"+productId+",product.getMainImg():"+product.getMainImg()+",product:"+JSON.toJSONString(product));
			shopMemberReservationsOrder.setSummaryImage(product.getMainImg());
		}else{
			shopMemberReservationsOrder.setClothesNumber(shopProduct.getClothesNumber());
			shopMemberReservationsOrder.setShopProductName(shopProduct.getName());
			shopMemberReservationsOrder.setSummaryImage(shopProduct.getFirstImage());
		}
		
		int record = shopMemberReservationsOrderMapper.insert(shopMemberReservationsOrder);
		if(record==1){
			boolean ret = GetuiUtil.pushGeTui(userCID, "您有一个新预约试穿订单请查看", "", shopMemberReservationsOrder.getId()+"", "", "17",
					System.currentTimeMillis() + "");
			if(!ret){
				logger.error("添加预约试穿订单发送推送消息有误userCID:"+userCID
				+",type："+shopMemberReservationsOrder.getId()+",+shopMemberReservationsOrderId:"+shopMemberReservationsOrder.getId());
			}
			ShopNotification shopNotification = new ShopNotification();
			shopNotification.setTitle("");
			shopNotification.setAbstracts("您有一个新预约试穿订单，客户姓名："+shopMemberName+"，商品名称："+name);
			shopNotification.setPushStatus(1);
			shopNotification.setImage("");
			shopNotification.setType(17);
			shopNotification.setStatus(0);
			shopNotification.setLinkUrl(shopMemberReservationsOrder.getId()+"");
			shopNotification.setStoreIdArrays(","+storeId);
			shopNotification.setCreateTime(System.currentTimeMillis());
			shopNotification.setUpdateTime(shopNotification.getCreateTime());
			shopNotification.setPushTime(shopNotification.getCreateTime());
			int count = shopNotificationMapper.insert(shopNotification);
			if(count!=1){
				logger.error("添加预约试穿订单发送系统消息有误storeId:"+storeId+";memberId:"+memberId+";shopProductId:"+shopProductId
						+",type："+shopMemberReservationsOrder.getId()+",+shopMemberReservationsOrderId:"+shopMemberReservationsOrder.getId());
			}
		}else{
			throw new RuntimeException("添加预约试穿订单有误storeId:"+storeId+";memberId:"+memberId+";shopProductId:"+shopProductId+
					";platformProductSkuId:"+platformProductSkuId+";shopProductSizeName:"+shopProductSizeName+";shopProductColorName:"+shopProductColorName+
					";shopMemberName:"+shopMemberName+";shopMemberPhone:"+shopMemberPhone+";appointmentTime:"+appointmentTime);
		}
	}

	@Override
	public List<ShopMemberReservationsOrder> getReservationsOrderList(long storeId, String keyWord, Page<ShopMemberReservationsOrder> page) {
		Wrapper<ShopMemberReservationsOrder> wrapper = new EntityWrapper<ShopMemberReservationsOrder>().eq("store_id", storeId);
		if(!StringUtils.isEmpty(keyWord)){
			wrapper.and(" (shop_product_name like '%"+keyWord+"%' or shop_member_name like '%"+keyWord+"%' or shop_member_phone like '%"+keyWord+"%') ");
		}
		wrapper.orderBy("create_time", false);
		return shopMemberReservationsOrderMapper.selectPage(page, wrapper);
	}

	@Override
	public ShopMemberReservationsOrder getReservationsOrderInfo(long shopMemberReservationsOrderId) {
		return shopMemberReservationsOrderMapper.selectById(shopMemberReservationsOrderId);
	}

}