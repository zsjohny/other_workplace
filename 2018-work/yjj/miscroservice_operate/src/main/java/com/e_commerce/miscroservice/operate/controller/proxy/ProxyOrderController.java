package com.e_commerce.miscroservice.operate.controller.proxy;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller.BaseController;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyOrderRpcService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述 运营平台代理商品controller
 *
 * @author hyq
 * @date 2018/9/18 16:04
 */
@RestController
@RequestMapping("/operate/proxy/order")
public class ProxyOrderController extends BaseController {

    @Autowired
    ProxyOrderRpcService proxyOrderRpcService;

    private Log logger = Log.getInstance(ProxyOrderController.class);

    /**
     * 描述 订单列表
     * @param pageNum
     * @param pageSize
     * @param orderNo 订单号
     * @param goodsName  商品名称
     * @param selfName  归属名
     * @param oneLevelName  县代理
     * @param twoLevelName  市代理
     * @param minMoney   最小金额
     * @param maxMoney   最大金额
     * @param startTime
     * @param endTime
     * @author hyq
     * @date 2018/10/16 14:40
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("orderList")
    public Response orderList(int pageNum,@RequestParam(defaultValue = "10") int pageSize,String orderNo,String goodsName,
                                String selfName,String oneLevelName,String twoLevelName,String minMoney,String maxMoney,
                                String startTime,String endTime){

        ProxyOrderQuery proxyOrderQuery = new ProxyOrderQuery();
        proxyOrderQuery.setPageNum(pageNum);
        proxyOrderQuery.setPageSize(pageSize);
        proxyOrderQuery.setOrderNo(orderNo);
        proxyOrderQuery.setGoodsName(goodsName);
        proxyOrderQuery.setSelfName(selfName);
        proxyOrderQuery.setOneLevelName(oneLevelName);
        proxyOrderQuery.setTwoLevelName(twoLevelName);
        proxyOrderQuery.setMaxMoney(maxMoney);
        proxyOrderQuery.setMinMoney(minMoney);
        proxyOrderQuery.setStartTime(startTime);
        proxyOrderQuery.setEndTime(endTime);

        return Response.success(proxyOrderRpcService.getOrderList(proxyOrderQuery));
    }

    /**
     * 描述  根据用户查询订单列表
     * @param userId  用户ID
    * @param type  1 客户 2 代理  3自己
     * @author hyq
     * @date 2018/9/27 16:56
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("getOrderDetailByUserId")
    public Response getOrderDetailByUserId(int pageNum,@RequestParam(defaultValue = "10") int pageSize,long userId,int type) {

        PageInfo<ProxyOrderQuery> orderDetailByUserId = proxyOrderRpcService.getOrderDetailByUserId(userId, type,pageNum,pageSize);

        return Response.success(orderDetailByUserId);
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
     * 描述 收益首页金额
     * @author hyq
     * @date 2018/10/8 18:59
     * @return int
     */
    @RequestMapping("rewardInfoPage")
    public String collectReward(long userId){

        Map<String,String> maps = new HashMap<>();

        maps.put("noReward",proxyOrderRpcService.collectReward(userId,0));
        maps.put("arealdyReward",proxyOrderRpcService.collectReward(userId,1));
        maps.put("allReward",proxyOrderRpcService.collectReward(userId,2));
        maps.put("toDayReward",proxyOrderRpcService.getTodayCollectReward(userId,2));

        return JSON.toJSONString(Response.success(maps));
    }

    /**
     * 描述 获取个人收入的收益列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @author hyq
     * @date 2018/9/29 20:25
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("getRewardOrderList")
    public Response getRewardOrderList(int pageNum, @RequestParam(defaultValue = "10") int pageSize, long userId, String startTime , String endTime,String isGrants) {

//        List<Integer> integerList = new ArrayList<>();
//        if (StringUtils.isNotEmpty(isGrants)){
//            String[] split = isGrants.split(",");
//            for(String action :split){
//                integerList.add(Integer.parseInt(action));
//            }
//        }

        return Response.success(proxyOrderRpcService.getRewardOrderList(userId,startTime,endTime,pageNum,pageSize,isGrants));
    }

    /**
     * 描述 开始发放收益
     * @param id
     * @author hyq
     * @date 2018/9/29 20:25
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("doOrderReward")
    public Response doOrderReward(long id) {

        int i =proxyOrderRpcService.doOrderReward(id);

        return Response.success(i);
    }

    /**
     * 描述 开始发放收益
     * @param id
     * @author hyq
     * @date 2018/9/29 20:25
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @RequestMapping("doOrderRewardByUser")
    public Response doOrderRewardByUser(long userId) {

        int i =proxyOrderRpcService.doOrderRewardByUser(userId);

        return Response.success(i);
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
    @RequestMapping("saveAddress")
    public String saveAddress(Long id,Integer isDefault,String province,String city,String county,String receiverName,String receiverPhone,String receiverAddress,Integer delStatus) {
        return proxyOrderRpcService.saveAddress(id, null, isDefault, province, city, county, receiverName, receiverPhone, receiverAddress,delStatus);

    }
}
