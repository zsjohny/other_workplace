package com.jiuy.wxa.api.controller.wxa;

import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.mapper.coupon.CouponRbNewMapper;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.order.ShopGoodsCar;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.service.ShopGoodsCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序购物车的controller
 *
 * @version V1.0
 * @Title: WxaShopCarController.java
 * @Package com.jiuy.wxa.api.controller.wxa
 * @author: Aison
 * @date: 2018年4月19日 下午6:37:15
 * @Copyright: 玖远网络
 */
@Controller
@RequestMapping("/miniapp/car")
public class WxaShopCarController {


    @Autowired
    private ShopGoodsCarService shopGoodsCarService;

    @Autowired
    ICouponServerNew couponServerNew;

    /**
     * @param memberId 用户会员信息
     * @throws
     * @Title: shopCarList
     * @Description: 获取某个用户的购物车清单
     * @return: JsonResponse
     */
    @ResponseBody
    @RequestMapping("/shopCarList")
    public JsonResponse shopCarList(Long memberId) {
        return new JsonResponse().setSuccessful().setData(shopGoodsCarService.shopCarList(memberId));
    }


    /**
     * 加入购物车
     * (加入直播间后, 将改用{@link WxaShopCarController#shopCarAddGoodsWithLive(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long)})
     * @param memberId  memberId 用户会员信息
     * @param skuId     商品id
     * @param skuNumber 商品的数量
     * @param storeId   商家id
     * @date: 2018/4/20 10:14
     * @author: Aison
     */
    @RequestMapping("/shopCarAddGoods")
    @ResponseBody
    public JsonResponse shopCarAddGoods(Long memberId, Long skuId, Long skuNumber, Long storeId, Long productId) {
        try {
            ShopGoodsCar car = new ShopGoodsCar();
            car.setMemberId(memberId);
            car.setProductSkuId(skuId);
            car.setSkuNumber(skuNumber);
            car.setStoreId(storeId);
            car.setProductId(productId);
            shopGoodsCarService.joinShopCar(car);
            return new JsonResponse().setSuccessful().setData(null);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }

    }

    /**
     * 加入购物车
     *
     * @param memberId  memberId 用户会员信息
     * @param skuId     商品id
     * @param skuNumber 商品的数量
     * @param storeId   商家id
     * @param liveProductId 直播商品id 如果是直播商品则必填,没有则不传
     * @date: 2018/4/20 10:14 update by Charlie 加入直播商品,这个业务暂没优化,先不放到新系统,
     * @author: Aison
     */
    @RequestMapping("/shopCarAddGoodsWithLive")
    @ResponseBody
    public JsonResponse shopCarAddGoodsWithLive(Long memberId, Long skuId, Long skuNumber, Long storeId, Long productId,
                                        @RequestParam(value = "liveProductId", defaultValue = "0") Long liveProductId) {
        try {
            ShopGoodsCar car = new ShopGoodsCar();
            car.setMemberId(memberId);
            car.setProductSkuId(skuId);
            car.setSkuNumber(skuNumber);
            car.setStoreId(storeId);
            car.setProductId(productId);
            car.setLiveProductId(liveProductId);
            shopGoodsCarService.joinShopCarWithLive(car);
            return new JsonResponse().setSuccessful().setData(null);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }

    }

    /**
     * 修改购物车 目前只支持修改 商品数量
     *
     * @param memberId  会员信息
     * @param carId     购物车id
     * @param skuNumber 商品数量
     * @date: 2018/4/20 10:12
     * @author: Aison
     */
    @RequestMapping("/updateCar")
    @ResponseBody
    public JsonResponse updateCar(Long memberId, Long carId, Long skuNumber,Integer selected) {

        try {
            skuNumber = skuNumber == null || skuNumber < 1 ? 1 : skuNumber;
            ShopGoodsCar newCar = new ShopGoodsCar();
            newCar.setId(carId);
            newCar.setMemberId(memberId);
            newCar.setSkuNumber(skuNumber);
            newCar.setSelected(selected);
            shopGoodsCarService.updateShopCar(newCar);
            return new JsonResponse().setSuccessful();
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 在购物车中删除某条记录
     *
     * @param memberId
     * @param carId
     * @date: 2018/4/20 10:34
     * @author: Aison
     */
    @RequestMapping("/deleteCarGoods")
    @ResponseBody
    public JsonResponse deleteCarGoods(Long memberId, Long carId) {
        try {
            shopGoodsCarService.deleteCarGoods(memberId, carId);
            return new JsonResponse().setSuccessful();
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     *描述 购物车点击结算的时候，进行库存的判断
     *
     * @author hyq
     * @date 2018/9/6 15:40
     * @return com.jiuyuan.web.help.JsonResponse
     */
    @ResponseBody
    @RequestMapping("/judgeCardsInventory")
    public JsonResponse judgeCardsInventory(Long memberId, @RequestParam("carId") Long[] carIds) {

        try {
            List<Map<String, Object>> maps = shopGoodsCarService.judgeCardsInventory(memberId, carIds);

            if(maps==null || maps.size() <1){

            }else {
                Map<String, Object> stringObjectMap = maps.get(0);

                Object isOk = stringObjectMap.get("isOk");

                if(!(Boolean) isOk){
                    return new JsonResponse().setResultCode(-704,"无法结算！非常抱歉，您晚了一步，所选商品中有的库存不足了，请重新选择您要购买的商品").setData(maps);
                }
            }

            return new JsonResponse().setSuccessful().setData(maps);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 获取购物车中的数据..传递给订单确认接口
     *
     * @param memberId
     * @param carIds   购物车中需要生成订单的记录的ids  11_321_33
     * @date: 2018/4/20 10:40
     * @author: Aison
     */
    public JsonResponse confirmOrderData(Long memberId, @RequestParam("carId") Long[] carIds) {
        try {
            Object rsMap = shopGoodsCarService.confirmOrderFromCar(memberId, carIds);
            return new JsonResponse().setSuccessful().setData(rsMap);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 从购物车跳转到订单确认页面
     * @param request
     * @param memberId
     * @param carIds
     * @param orderType
     * @param shopMemberDeliveryAddressId
     * @date:   2018/4/25 14:02
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/confirmOrderFromCar")
    public JsonResponse confirmOrderFromCar(HttpServletRequest request, Long memberId, String carIds,
                                            @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType,
                                            @RequestParam(value = "shopMemberDeliveryAddressId", required = false, defaultValue = "0") long shopMemberDeliveryAddressId) {

        try {
            String version = request.getHeader("version");
            String[] carId = carIds.split(",");
            List<Map<String, Object>> rsMaps = shopGoodsCarService.confirmDataFromCar(memberId, carId, version, orderType, shopMemberDeliveryAddressId);

//            //永远只有一个商家
            Map<String, Object> objectMap = rsMaps.get(0);
//
//            Object isOK = objectMap.get("isOk");
//
//            if( isOK!=null && !(Boolean) isOK){
//                return new JsonResponse().setResultCode(-704,"无法结算！非常抱歉，您晚了一步，所选商品中有的库存不足了，请重新选择您要购买的商品")
//                        .setData(objectMap);
//            }

            StoreBusiness storeBusiness = (StoreBusiness)objectMap.get("storeBusiness");

            //新的优惠券填充
            couponServerNew.fillCoupon(objectMap,storeBusiness.getId(),memberId,CouponSysEnum.WXA,true,1,100);
            return new JsonResponse().setSuccessful().setData(rsMaps.get(0));
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


}
