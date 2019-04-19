package com.jiuy.wxa.api.controller.wxa;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ShopMemberDeliveryAddress;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopMemberDeliveryAddressMapper;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 小程序会员收货地址表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-11
 */
@Controller
@RequestMapping("/shop/shopMemberDeliveryAddress")
public class ShopMemberDeliveryAddressController {
    private static final Log logger = LogFactory.get(ShopMemberDeliveryAddressController.class);
    @Autowired
    private ShopMemberDeliveryAddressMapper shopMemberDeliveryAddressMapper;

    /**
     * 添加收货地址
     *
     * @param phoneNumber
     * @param location
     * @return
     */
    @RequestMapping("/addDeliveryAddress")
    public JsonResponse addDeliveryAddress(
            @RequestParam("storeId") Long storeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "linkmanName", required = true) String linkmanName,
                                           @RequestParam(value = "phoneNumber", required = true) String phoneNumber,
                                           @RequestParam(value = "location", required = true) String location,
                                           @RequestParam(value = "address", required = true) String address) {
        logger.info("小程序会员添加收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
//            checkStore(shopDetail);
//            checkMember(memberDetail);
            ShopMemberDeliveryAddress shopMemberDeliveryAddress = new ShopMemberDeliveryAddress();
//			shopMemberDeliveryAddress.setShopMemberId(memberDetail.getId());
//			shopMemberDeliveryAddress.setStoreId(shopDetail.getId());
//			shopMemberDeliveryAddress.setLinkmanName(linkmanName);
//			shopMemberDeliveryAddress.setPhoneNumber(phoneNumber);
//			shopMemberDeliveryAddress.setLocation(location);
//			shopMemberDeliveryAddress.setAddress(address);
//			shopMemberDeliveryAddress.setLastUsedTime(System.currentTimeMillis());//保存
//			shopMemberDeliveryAddress.setCreateTime(System.currentTimeMillis());
//			shopMemberDeliveryAddress.setUpdateTime(System.currentTimeMillis());
//			shopMemberDeliveryAddressMapper.insert(shopMemberDeliveryAddress);
            shopMemberDeliveryAddressMapper.addDeliveryAddress(memberId, storeId, linkmanName, phoneNumber, location, address, shopMemberDeliveryAddress);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
            return jsonResponse.setSuccessful().setData(map);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return jsonResponse.setError("添加收货地址失败！");
        }
    }

    /**SecondBuyActivityMapper
     * 保存修改收货地址
     *
     * @param deliveryAddressId
     * @param linkmanName
     * @param phoneNumber
     * @param location
     * @return
     */
    @RequestMapping("/updateDeliveryAddressSave")
    public JsonResponse updateDeliveryAddressSave(
            @RequestParam("storeId") Long storeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "deliveryAddressId", required = true) long deliveryAddressId,
            @RequestParam(value = "linkmanName", required = false, defaultValue = "") String linkmanName,
            @RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
            @RequestParam(value = "location", required = false, defaultValue = "") String location,
            @RequestParam(value = "address", required = false, defaultValue = "") String address) {
        logger.info("小程序会员修改收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopMemberDeliveryAddress shopMemberDeliveryAddress = new ShopMemberDeliveryAddress();
            shopMemberDeliveryAddress.setId(deliveryAddressId);
            if (StringUtils.isNotEmpty(linkmanName)) {
                shopMemberDeliveryAddress.setLinkmanName(linkmanName);
            }
            if (StringUtils.isNotEmpty(phoneNumber)) {
                shopMemberDeliveryAddress.setPhoneNumber(phoneNumber);

            }
            if (StringUtils.isNotEmpty(location)) {
                shopMemberDeliveryAddress.setLocation(location);
            }
            if (StringUtils.isNotEmpty(address)) {
                shopMemberDeliveryAddress.setAddress(address);
            }
            shopMemberDeliveryAddress.setUpdateTime(System.currentTimeMillis());
            shopMemberDeliveryAddress.setLastUsedTime(System.currentTimeMillis());
            shopMemberDeliveryAddressMapper.updateById(shopMemberDeliveryAddress);

            return jsonResponse.setSuccessful().setData("ok");
        } catch (Exception e) {
            return jsonResponse.setError("修改收货地址失败！");
        }
    }

    /**
     * 修改收货地址
     *
     * @param deliveryAddressId
     * @return
     */
    @RequestMapping("/updateDeliveryAddress")
    public JsonResponse updateDeliveryAddress(
//            @RequestParam("storeId") Long storeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "deliveryAddressId", required = true) long deliveryAddressId) {
        logger.info("小程序会员修改收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopMemberDeliveryAddress shopMemberDeliveryAddress = shopMemberDeliveryAddressMapper.selectById(deliveryAddressId);
            if (shopMemberDeliveryAddress == null) {
                return jsonResponse.setError("收货地址不存在！");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
            map.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());
            map.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
            map.put("location", shopMemberDeliveryAddress.getLocation());
            map.put("address", shopMemberDeliveryAddress.getAddress());

            return jsonResponse.setSuccessful().setData(map);
        } catch (Exception e) {
            return jsonResponse.setError("修改收货地址失败！");
        }
    }


    /**
     * 删除收货地址
     *
     * @param deliveryAddressId
     */
    @RequestMapping("/deleteDeliveryAddress")
    public JsonResponse deleteDeliveryAddress(
//            @RequestParam("storeId") Long storeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "deliveryAddressId", required = true) long deliveryAddressId) {
        logger.info("小程序会员删除收货地址ShopMemberDeliveryAddressController:" + "memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            if (shopMemberDeliveryAddressMapper.findExistById(deliveryAddressId) == 0) {
                return jsonResponse.setError("收货地址不存在！");
            }
            shopMemberDeliveryAddressMapper.deleteById(deliveryAddressId);
            return jsonResponse.setSuccessful().setData("ok");
        } catch (Exception e) {
            return jsonResponse.setError("删除收货地址失败！");
        }
    }


}
