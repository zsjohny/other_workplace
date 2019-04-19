package com.e_commerce.miscroservice.publicaccount.controller;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyAddress;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyRewardQuery;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyOrderRpcService;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller.BaseController;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 运营平台代理商品controller
 *
 * @author hyq
 * @date 2018/9/27 16:04
 */
@RestController
@RequestMapping("/public/proxy/pay")
public class ProxyPayController extends BaseController {

    @Autowired
    ProxyOrderRpcService proxyOrderRpcService;

    @Autowired
    private PublicAccountUserService publicAccountUserService;

    private Log logger = Log.getInstance(ProxyPayController.class);


    /**
     * 描述 进行订单的支付
     *
     * @param goodsId
     * @param addressId
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/25 17:02
     */
    @RequestMapping("payOrder")
    public String payOrder(long goodsId, long addressId) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();
        String openId = user.getOpenId ();

        logger.info ("进行订单的支付 goodsId={}, userId={}, addressId={}, openId={}", goodsId, userId, addressId, openId);
        return proxyOrderRpcService.payOrder(goodsId, userId, addressId, openId);
    }

    @RequestMapping("cancelOrder")
    public String cancelOrder(String orderId) {
        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        return JSON.toJSONString(Response.success(proxyOrderRpcService.cancelOrder(orderId)));
    }

    /**
     * 描述 获取订单支付前的详情
     *
     * @param goodsId 商品ID
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/21 9:51
     */
    @RequestMapping("getBeforeOrderDetail")
    public String getBeforeOrderDetail(long goodsId, HttpServletRequest request) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

        return proxyOrderRpcService.getBeforeOrderDetail(goodsId, userId);
    }

    /**
     * 描述 获取订单支付后的订单详情
     *
     * @param orderId 订单号
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/25 17:29
     */
    @RequestMapping("getAfterOrderDetail")
    public String getAfterOrderDetail(String orderId) {
        return proxyOrderRpcService.getAfterOrderDetail(orderId);
    }

    /**
     * 描述  根据用户查询订单列表
     *
     * @param type   1 客户 2 代理  3自己
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author hyq
     * @date 2018/9/27 16:56
     */
    @RequestMapping("getOrderDetailByUserId")
    public Response getOrderDetailByUserId(int type, int pageNum, int pageSize) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return ResponseHelper.noLogin();
        }

        Long userId = user.getId ();

        PageInfo<ProxyOrderQuery> orderDetailByUserId = proxyOrderRpcService.getOrderDetailByUserId(userId, type, pageNum, pageSize);
        return Response.success(orderDetailByUserId);
    }

    /**
     * 描述 收益首页金额
     * @author hyq
     * @date 2018/10/8 18:59
     * @return int
     */
    @RequestMapping("rewardInfoPage")
    public String collectReward(){

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

        Map<String,String> maps = new HashMap<>();

        maps.put("noReward",proxyOrderRpcService.collectReward(userId,0));
        maps.put("arealdyReward",proxyOrderRpcService.collectReward(userId,1));
        maps.put("allReward",proxyOrderRpcService.collectReward(userId,2));
        maps.put("toDayReward",proxyOrderRpcService.getTodayCollectReward(userId,2));

        return JSON.toJSONString(Response.success(maps));

    }

    /**
     * 描述 汇总个人收益
      * @param type  0未发  1 已经发  2总收益
     * @author hyq
     * @date 2018/10/8 18:59
     * @return int
     */
    @RequestMapping("collectReward")
    public String collectReward(int type){

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

        return proxyOrderRpcService.collectReward(userId, type);
    }

    /**
     * 描述 获取返利的订单详情
     *
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward>
     * @author hyq
     * @date 2018/10/8 16:59
     */
    @RequestMapping("getRewardOrderList")
    public String getRewardOrderList(int pageNum, int pageSize , String startTime ,String endTime) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

//        List<Integer> integerList = new ArrayList<>();
//        integerList.add(1);
//        integerList.add(0);

        PageInfo<ProxyRewardQuery> pageInfo = proxyOrderRpcService.getRewardOrderList(userId,startTime,endTime, pageNum, pageSize,"0,1");
        return JSON.toJSONString (Response.success (pageInfo));
    }

    /**
     * 描述 获取代理地址
     *
     * @param request
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/19 18:42
     */
    @RequestMapping("getAddressList")
    public String getAddressList(HttpServletRequest request) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

        return proxyOrderRpcService.getAddressList(userId);
    }

    /**
     * 描述  设置地址默认
     * @param id
     * @param delStatus  1 默认  0 取消
     * @author hyq
     * @date 2018/10/12 17:50
     * @return java.lang.String
     */
    @RequestMapping("editAddressDefault")
    public String editAddressDefault(Long id,Integer isDefault){
        return proxyOrderRpcService.editAddressDefault(id, isDefault);
    }

    /**
     * 描述 编辑代理地址
     *
     * @param id
     * @return java.lang.String
     * @author hyq
     * @date 2018/9/19 14:22
     */
    @RequestMapping("selectAddress")
    public String selectAddress(Long id) {
        return proxyOrderRpcService.selectAddress(id);
    }

    /**
     * 描述
     * @param id
    * @param userId
     * @param isDefault
     * @param province
     * @param city
     * @param county
     * @param receiverName
     * @param receiverPhone
    * @param receiverAddress
     * @author hyq
     * @date 2018/10/9 14:22
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @Consume(ProxyAddress.class)
    @RequestMapping("saveAddress")
    public String saveAddress(Long id,Integer isDefault,String province,String city,String county,String receiverName,String receiverPhone,String receiverAddress,Integer delStatus) {

        PublicAccountUser user = publicAccountUserService.isLogin(request);
        if (user == null) {
            return JSON.toJSONString(ResponseHelper.noLogin());
        }

        Long userId = user.getId ();

        return proxyOrderRpcService.saveAddress(id, userId, isDefault, province, city, county, receiverName, receiverPhone, receiverAddress,delStatus);
    }
}
