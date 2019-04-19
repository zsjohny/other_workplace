package com.e_commerce.miscroservice.order.service.wx;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.order.entity.*;
import com.e_commerce.miscroservice.order.method.WxPayResultData;

import java.util.List;
import java.util.SortedMap;

public interface MemberService {
    /**
     * 根据订单id查询现详情
     * @param orderId
     * @return
     */
      ShopMemberOrder getMemberById(Long orderId);

    /**
     * 根据秒杀id 查询详情
     */
    SecondBuyActivity selectById(Long secondId);

    /**
     * 根据团购id查询详情
     */
    TeamBuyActivity selectTeamId(Long teamId);

    /**
     * 获取商家绑定的小程序
     * @return
     */
    StoreWxa getStoreWxaByStoreId(String storeId);

    /**
     * 查询商品详情
     */
    List<ShopMemberOrderItem> getMemberOrderItemList(Long orderId);

    /**
     * 发送请求调用微信统一下单
     * @param orderNo
     * @param productName
     * @param total_fee
     * @param publicOpenId
     * @param businessName
     * @param appId
     * @param mchId
     * @param payKey
     * @param user_ip
     * @return
     */
    public  String unifiedorder(String orderNo, String productName, int total_fee, String publicOpenId, String businessName, String appId, String mchId, String payKey, String user_ip,Integer type);



    //3、解析微信下单接口返回的XML信息
    WxPayResultData buildWxPayResultData(String result);
    /**
     * 组装签名数据
     * @param resultData
     * @return
     */
    public SortedMap<Object, Object> buildSignData(WxPayResultData resultData, String payKey);

    /**
     * 修改订单支付标识码
     * @param orderId
     * @param prepay_id
     */
    public int updatePrepayId(long orderId, String prepay_id);

    /**
     * 根据memeberId获取会员信息
     */
    ShopMember selectShopMember(Long memberId);

    /**
     * 根据storeId查询商家信息
     */
    StoreBusiness selectStoreBusiness(Long storeId);

    Long selectOppenId(String bindWeixin,Long storeId);
}

