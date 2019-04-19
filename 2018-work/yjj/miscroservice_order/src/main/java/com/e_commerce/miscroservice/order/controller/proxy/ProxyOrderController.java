package com.e_commerce.miscroservice.order.controller.proxy;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.proxy.*;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.PayResult;
import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyGoodsRpcService;
import com.e_commerce.miscroservice.commons.utils.IdGenerator;
import com.e_commerce.miscroservice.commons.utils.pay.WxCommonUtil;
import com.e_commerce.miscroservice.commons.utils.pay.XmlUtil;
import com.e_commerce.miscroservice.order.mapper.ProxyOrderMapper;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyCustomerRpcService;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyPayRpcService;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyUserRpcService;
import com.e_commerce.miscroservice.order.service.ConfigService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyAddressService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyOrderService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyRewardService;
import com.e_commerce.miscroservice.order.utils.enums.OrderSourceEnum;
import com.e_commerce.miscroservice.order.utils.enums.OrderStatusEnum;
import com.e_commerce.miscroservice.order.utils.enums.PayWayEnum;
import com.e_commerce.miscroservice.order.vo.ProxyRefereeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static com.e_commerce.miscroservice.commons.utils.HttpUtils.getIpAddress;

/**
 * 描述 代理订单controller
 *
 * @date 2018/9/19 18:31
 * @return
 */
@RestController
@RequestMapping("/order/proxy/order")
public class ProxyOrderController {

    @Autowired
    ProxyAddressService proxyAddressService;

    @Autowired
    ProxyGoodsRpcService proxyGoodsRpcService;

    @Autowired
    ProxyOrderService proxyOrderService;

    @Autowired
    ProxyPayRpcService proxyPayRpcService;

    @Autowired
    ConfigService configService;

    @Autowired
    ProxyRewardService proxyRewardService;

    @Autowired
    ProxyCustomerRpcService proxyCustomerRpcService;

    @Autowired
    ProxyUserRpcService proxyUserRpcService;

    @Autowired
    private ProxyOrderMapper proxyOrderMapper;

    private Log logger = Log.getInstance(ProxyOrderController.class);

    /**
     * 描述 获取订单支付前的详情
     *
     * @param goodsId 商品ID
     * @param userId  用户id
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/21 9:51
     */
    @RequestMapping("getBeforeOrderDetail")
    public String getBeforeOrderDetail(long goodsId, long userId) {

        Map<String, Object> res = new HashMap<>();

        List<ProxyAddress> addressByUserId = proxyAddressService.getAddressByUserId(userId, 1);

        ProxyGoods proxyGoods = proxyGoodsRpcService.selectById(goodsId);
        if(addressByUserId==null || addressByUserId.size()<1){
            res.put("address", "");
        }else {
            res.put("address", addressByUserId.get(0));
        }

        res.put("goods", proxyGoods);

        return JSON.toJSONString(Response.success(res));
    }

    /**
     * 描述 进行订单的支付
     *
     * @param goodsId
     * @param userId
     * @param addressId
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/25 17:02
     */
    @RequestMapping("payOrder")
    public String payOrder(long goodsId, long userId, long addressId, String openid, HttpServletRequest request) {

        ProxyAddress proxyAddress = proxyAddressService.selectByIdREntity(addressId);

        if (proxyAddress == null) {
            return JSON.toJSONString(Response.errorMsg("地址不存在"));
        }

        ProxyGoods proxyGoods = proxyGoodsRpcService.selectById(goodsId);

        if (proxyGoods == null) {
            return JSON.toJSONString(Response.errorMsg("商品不存在"));
        }

        ProxyOrder proxyOrder = new ProxyOrder();

        String currentTimeId = IdGenerator.getCurrentTimeId("");
        proxyOrder.setOrderNo(currentTimeId);
        proxyOrder.setUserId(userId);
        proxyOrder.setGoodsId(goodsId);
        proxyOrder.setGoodsName(proxyGoods.getGoodsName());
        proxyOrder.setGoodsPrice(proxyGoods.getGoodsPrice());
        proxyOrder.setGoodsTimeNum(proxyGoods.getTimeNum());
        proxyOrder.setGoodsTimeUnit(proxyGoods.getTimeUnit());
        proxyOrder.setGoodsImages(proxyGoods.getMainImages());
        proxyOrder.setGoodsTypeId(proxyGoods.getTypeId());
        proxyOrder.setSource(OrderSourceEnum.GZH.getCode());
        proxyOrder.setIsGrant(0);
        proxyOrder.setStatus(OrderStatusEnum.PROCESS.getCode());
        proxyOrder.setPayWay(PayWayEnum.WX.getCode());
        //proxyOrder.setPayOrderNo(currentTimeId);
        proxyOrder.setCount(1);
        proxyOrder.setPrice(proxyGoods.getGoodsPrice());
        proxyOrder.setFavorablePrice(BigDecimal.ZERO);
        proxyOrder.setActualPrice(proxyGoods.getGoodsPrice());
        proxyOrder.setReceiverName(proxyAddress.getReceiverName());
        proxyOrder.setReceiverPhone(proxyAddress.getReceiverPhone());
        proxyOrder.setReceiverAddress(proxyAddress.getProvince()+" "+proxyAddress.getCity()+" "+proxyAddress.getCounty()+" "+proxyAddress.getReceiverAddress());

        //这里需要根据userid来进行判断
        //没有推荐关系
        ProxyRefereeVo refereeUser = proxyOrderService.getRefereeUser(userId);
        if (refereeUser == null) {
            proxyOrder.setIsProfit(0);
        } else {
            proxyOrder.setIsProfit(1);
        }


        boolean insert = proxyOrderService.insert(proxyOrder);

        System.out.println(proxyOrder.getId());

        if (insert) {

            BigDecimal amount = proxyGoods.getGoodsPrice().multiply(BigDecimal.valueOf(100));
            //必填  组件名+mapping
            String attch = "ORDER@_@/order/proxy/order/returnPayDetail";

            PreOrderResult preOrderResult = proxyPayRpcService.createPreOrder(amount, "购买商品", currentTimeId, attch, getIpAddress(request) ,openid);
            logger.info ("预支付 preOrderResult={}", preOrderResult);
            if ("SUCCESS".equals(preOrderResult.getReturn_code())) {

                SortedMap<Object, Object> brandWCPayRequest = WxCommonUtil.getBrandWCPayRequest(preOrderResult);

                //订单ID
                brandWCPayRequest.put("orderId", currentTimeId);

                return JSON.toJSONString(Response.success(brandWCPayRequest));

            } else {

                ProxyOrder order = proxyOrderService.getOrderByOrderId(currentTimeId).get(0);

                ProxyOrder order1 = new ProxyOrder();
                order1.setId(order.getId());
                order1.setStatus(OrderStatusEnum.FAIL.getCode());
                proxyOrderService.updateSelectiveById(order1);

                return JSON.toJSONString(Response.errorMsg("保存订单失败"));
            }

        } else {
            return JSON.toJSONString(Response.errorMsg("保存订单失败"));
        }

    }

    @RequestMapping("cancelOrder")
    public int cancelOrder(String orderId) {
        List<ProxyOrder> orderByOrderId = proxyOrderService.getOrderByOrderId(orderId);
        boolean b=false;
        if (orderByOrderId!=null&&orderByOrderId.size()>1){
            ProxyOrder order = orderByOrderId.get(0);

            ProxyOrder newProxyOrder = new ProxyOrder();
            newProxyOrder.setId(order.getId());
            newProxyOrder.setStatus(2);
            b = proxyOrderService.updateSelectiveById(newProxyOrder);
        }

        return b?1:0;
    }

    /**
     * 描述 代理会员支付异步
     *
     * @param result 原始参数
     * @return void
     * @author hyq
     * @date 2018/9/26 19:02
     */
    @RequestMapping("returnPayDetail")
    public synchronized Boolean returnPayDetail(String result) {
        try {
            proxyOrderService.doUpdOrderAfterPay (result);
        } catch (Exception e) {
            e.printStackTrace ();
            logger.error ("代理会员支付异步 异常!");
            return false;
        }
        return true;
    }

    @RequestMapping("getOrderByOrderId")
    public List<ProxyOrder> getOrderByOrderId(String orderId) {
        return proxyOrderService.getOrderByOrderId(orderId);
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

        if (StringUtils.isEmpty(orderId)) {
            JSON.toJSONString(Response.errorMsg("订单号为必填项"));
        }

        List<ProxyOrder> orderByOrderId = proxyOrderService.getOrderByOrderId(orderId);

        List<ProxyOrderQuery> orderQueries = new ArrayList<>();
        orderByOrderId.stream().forEach(action->{

            ProxyOrderQuery proxyOrderQuery = new ProxyOrderQuery();

            try {
                BeanUtils.copyProperties(proxyOrderQuery,action);
            } catch (Exception e) {
                return;
            }

            Long userId1 = action.getUserId();

            //下单者
            proxyOrderQuery.setSelfName(getName(userId1));
            proxyOrderQuery.setSelfRole(proxyCustomerRpcService.judeCustomerRole(userId1));


            //获取推荐关系
            ProxyRefereeVo refereeUser = proxyOrderService.getRefereeUser(userId1);
            if(refereeUser!=null){
                proxyOrderQuery.setRewardMoney(getRewardMoney(refereeUser.getRefereeUserId(), action.getOrderNo()));
            }


            orderQueries.add(proxyOrderQuery);

        });

        return JSON.toJSONString(Response.success(orderQueries));

    }

    /**
     * 描述  根据用户查询订单列表
     *
     * @param userId 用户ID
     * @param type   1 客户 2 代理  3自己
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author hyq
     * @date 2018/9/27 16:56
     */
    @RequestMapping("getOrderDetail")
    public PageInfo<ProxyOrderQuery> getOrderDetailByUserId(long userId, int type, int pageNum, int pageSize) {
        List<ProxyOrderQuery> result;
        if (type == 3) {
            result = proxyOrderService.getOrderByUserId(userId, pageNum, pageSize);
        } else {
            //过滤掉空的数据

            List<ProxyOrderQuery> orderByUserIdAndType = proxyOrderService.getOrderByUserIdAndType(userId, type, pageNum, pageSize);

            List<ProxyOrderQuery> orderQueries = new ArrayList<>();

            for(ProxyOrderQuery action : orderByUserIdAndType){
                if(action != null && StringUtils.isNotEmpty(action.getOrderNo())){

                    Long userId1 = action.getUserId();

                    //下单者
                    action.setSelfName(getName(userId1));
                    action.setSelfRole(proxyCustomerRpcService.judeCustomerRole(userId1));

                    //获取推荐关系
                    //ProxyRefereeVo refereeUser = proxyOrderService.getRefereeUser(userId1);

//                    Long refereeUserId = action.getRefereeUserId();
                    Long refereeUserId = userId;
                    String name = getName(refereeUserId);

                    action.setOneLevelName(name);
                    action.setOneLevelSelfRole(proxyCustomerRpcService.judeCustomerRole(refereeUserId));

                    action.setRewardMoney(getRewardMoney(userId, action.getOrderNo()));

                    orderQueries.add(action);
                }
            }
//
//            List<ProxyOrderQuery> collect = proxyOrderService.getOrderByUserIdAndType(userId, type, pageNum, pageSize)
//                    .stream().filter(action -> action != null && StringUtils.isNotEmpty(action.getOrderNo())).collect(Collectors.toList());

            result = orderQueries;
        }
        return new PageInfo<> (result);
    }

    /**
     * 描述 获取是否是收益的订单
     *
     * @param isProfit
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder>
     * @author hyq
     * @date 2018/10/8 16:54
     */
    @RequestMapping("getProfitOrderList")
    public PageInfo<ProxyOrder> getProfitOrderList(int isProfit, int pageNum, int pageSize) {
        return proxyOrderService.getProfitOrderList(isProfit, pageNum, pageSize);
    }


    /**
     * 描述 根据userId 订单号。获取收益金额
     * @param userId
     * @param orderNo
     * @author hyq
     * @date 2018/10/10 21:25
     * @return java.lang.String
     */
    @RequestMapping("getRewardMoney")
    public String getRewardMoney(long userId, String orderNo) {

        ProxyReward rewardMoney = proxyRewardService.getRewardMoney(userId, orderNo);

        if(rewardMoney==null){
            return "0";
        }else {
            return rewardMoney.getRewardMoney().toString();
        }
    }

    /**
     * 描述 获取返利的订单详情
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward>
     * @author hyq
     * @date 2018/10/8 16:59
     */
    @RequestMapping("getRewardOrderList")
    public PageInfo<ProxyRewardQuery> getRewardOrderList(long userId, String startTime ,String endTime,int pageNum, int pageSize,String isGrants) {

        List<Integer> integerList = new ArrayList<>();
        if (StringUtils.isNotEmpty(isGrants)){
            String[] split = isGrants.split(",");
            for(String action :split){
                integerList.add(Integer.parseInt(action));
            }
        }

        List<ProxyReward> rewardOrderList = proxyOrderService.getRewardOrderList(userId,startTime,endTime, pageNum, pageSize,integerList);
        Page page = (Page) rewardOrderList;
        List<ProxyRewardQuery> rewardVos = new ArrayList<>();

        rewardOrderList.forEach(action->{

            ProxyRewardQuery proxyRewardVo = new ProxyRewardQuery();
            try {
                BeanUtils.copyProperties(proxyRewardVo,action);
            } catch (Exception e) {
                logger.error ("转换异常");
                return;
            }

            List<ProxyOrder> orderList = proxyOrderService.getOrderByOrderId(action.getOrderNo());

            proxyRewardVo.setGoodsName(orderList.get(0).getGoodsName());
            proxyRewardVo.setPayOrderNo(orderList.get(0).getPayOrderNo());

            //获取推荐关系
            ProxyRefereeVo refereeUser = proxyOrderService.getAllRefereeUser(action.getOrderUserId());

            Long refereeUserId = refereeUser.getRefereeUserId();
            String name = getName(refereeUserId);

            proxyRewardVo.setSelfName(getName(action.getOrderUserId()));
            proxyRewardVo.setSelfRole(proxyCustomerRpcService.judeCustomerRole(action.getOrderUserId()));

            proxyRewardVo.setOneLevelName(name);
            proxyRewardVo.setOneLevelSelfRole(proxyCustomerRpcService.judeCustomerRole(refereeUserId));
            rewardVos.add(proxyRewardVo);
        });
        page.clear ();
        page.addAll (rewardVos);
        return new PageInfo<> (page);
    }

    /**
     * 描述 获取订单列表
     *
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/25 18:01
     */
    @RequestMapping("getOrderList")
    public PageInfo<ProxyOrderQuery> getOrderList(@RequestBody ProxyOrderQuery query) {

//        DataGrid grid = new DataGrid();
//        grid.setPageNum(pageNum);
//        grid.setPageSize(pageSize);
//
//        List<ProxyOrder> proxyOrders = proxyOrderService.listForDataGridL(grid);

        List<ProxyOrder> proxyOrders=proxyOrderService.getOrderList(query);
        //这里打个补丁,没有做分页
        Page proxyOrderPage = ((Page) proxyOrders);

        List<ProxyOrderQuery> proxyOrderQueryList = new ArrayList<>();
        proxyOrders.stream().forEach(action -> {


            ProxyOrderQuery proxyOrderQuery = new ProxyOrderQuery();

            //获取推荐关系
            ProxyRefereeVo refereeUser = proxyOrderService.getAllRefereeUser(action.getUserId());

            if(refereeUser==null){
                try {
                    BeanUtils.copyProperties(proxyOrderQuery, action);

                    proxyOrderQuery.setSelfName(getName(action.getUserId()));

                    proxyOrderQueryList.add(proxyOrderQuery);
                    return;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            Long recommonUserId = refereeUser.getRecommonUserId();
            Long refereeUserId = refereeUser.getRefereeUserId();
            Long refereParentId = refereeUser.getRefereParentId();

            //查询过滤
            String selfName = query.getSelfName();
            if(StringUtils.isNotEmpty(selfName)){
                if (!selfName.equals(getName(recommonUserId))){
                    return;
                }
            }
            String oneLevelName = query.getOneLevelName();
            if(StringUtils.isNotEmpty(oneLevelName)){
                if (!oneLevelName.equals(getName(refereeUserId))){
                    return;
                }
            }
            String twoLevelName = query.getTwoLevelName();
            if(StringUtils.isNotEmpty(twoLevelName)){
                if (!twoLevelName.equals(getName(refereParentId))){
                    return;
                }
            }

            proxyOrderQuery.setSelfName(getName(recommonUserId));
            proxyOrderQuery.setOneLevelName(getName(refereeUserId));
            proxyOrderQuery.setOneLevelSelfRole(proxyCustomerRpcService.judeCustomerRole(refereeUserId));
            proxyOrderQuery.setTwoLevelName(getName(refereParentId));

            try {
                BeanUtils.copyProperties(proxyOrderQuery, action);
                proxyOrderQueryList.add(proxyOrderQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        proxyOrderPage.clear ();
        proxyOrderPage.addAll (proxyOrderQueryList);
        return new PageInfo<> (proxyOrderPage);
    }

    /**
     * 描述 根据userId获取代理名称或者昵称
     *
     * @param userId
     * @return java.lang.String
     * @author hyq
     * @date 2018/10/8 10:23
     */
    private String getName(Long userId) {

        if (userId ==null ||userId < 1) {
            return "";
        }
        String refereeCustomerName = proxyOrderService.getRefereeCustomerName(userId);
        String refereePublicName = proxyOrderService.getRefereePublicName(userId);
        return StringUtils.isEmpty(refereeCustomerName) ? refereePublicName : refereeCustomerName;
    }

    /**
     * 描述 汇总个人收益
     * @param userId
     * @param type  0未发  1 已经发  2总收益
     * @author hyq
     * @date 2018/10/8 18:59
     * @return int
     */
    @RequestMapping("collectReward")
    public String collectReward(long userId,int type){

        List<Integer> isGrants = new ArrayList<>();
        if(type==2){
            isGrants.add(1);
            isGrants.add(0);
        }else {
            isGrants.add(type);
        }
        return proxyOrderService.getCollectReward(userId,isGrants).toString();
    }

    /**
     * 描述 汇总当日个人收益
     * @param userId
     * @param type  0未发  1 已经发  2总收益
     * @author hyq
     * @date 2018/10/8 18:59
     * @return int
     */
    @RequestMapping("getTodayCollectReward")
    public String getTodayCollectReward(long userId,int type){

        List<Integer> isGrants = new ArrayList<>();
        if(type==2){
            isGrants.add(1);
            isGrants.add(0);
        }else {
            isGrants.add(type);
        }
        return proxyOrderService.getTodayCollectReward(userId,isGrants).toString();
    }

    /**
     * 描述 根据收益id，修改发送状态
     *
     * @param id
     * @return int
     * @author hyq
     * @date 2018/10/8 10:24
     */
    @RequestMapping("doOrderReward")
    public int doOrderReward(long id) {

        Example example = new Example(ProxyReward.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);

        ProxyReward proxyReward = new ProxyReward();
        proxyReward.setIsGrant(1);

        int i = proxyRewardService.updateByExampleSelective(proxyReward, example);

        return i;
    }

    /**
     * 描述 根据收益id，修改发送状态
     *
     * @param userId
     * @return int
     * @author hyq
     * @date 2018/10/8 10:24
     */
    @RequestMapping("doOrderRewardByUser")
    public int doOrderRewardByUser(long userId) {

        Example example = new Example(ProxyReward.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("isGrant",0);

        ProxyReward proxyReward = new ProxyReward();
        proxyReward.setIsGrant(1);

        int i = proxyRewardService.updateByExampleSelective(proxyReward, example);

        return i;
    }

    //Integer getCustomerType(long userId);

    /**
     * 描述 获取代理地址
     *
     * @param userId
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyq
     * @date 2018/9/19 18:42
     */
    @RequestMapping("getAddressList")
    public String getAddressList(long userId) {

        List<ProxyAddress> addressByUserId = this.proxyAddressService.getAddressByUserId(userId);

        return JSON.toJSONString(Response.success(addressByUserId));
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
    public String saveAddress(Long id,Long userId,Integer isDefault,String province,String city,String county,String receiverName,String receiverPhone,String receiverAddress,Integer delStatus) {
    //public Response saveAddress(ProxyAddress proxyAddress) {

        ProxyAddress proxyAddress =(ProxyAddress)ConsumeHelper.getObj();
        if (proxyAddress.getId() != null) {
            this.proxyAddressService.updateSelectiveById(proxyAddress);
            return JSON.toJSONString(Response.success());
        }

        proxyAddress.setIsDefault (isDefault);
//        proxyAddressService.insertSelective(proxyAddress);

        proxyAddressService.updateAddress (proxyAddress);
        return JSON.toJSONString(Response.success());
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
        proxyAddressService.assignDefaultAddress (id, isDefault);
        return JSON.toJSONString(Response.success());
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
        if (id != null) {
            return JSON.toJSONString(Response.success(this.proxyAddressService.selectById(id).orElseThrow(() -> new RuntimeException("未找到记录"))));
        }
        return JSON.toJSONString(Response.success());
    }

}
