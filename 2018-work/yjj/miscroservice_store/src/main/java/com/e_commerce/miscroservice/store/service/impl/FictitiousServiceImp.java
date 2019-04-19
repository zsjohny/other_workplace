package com.e_commerce.miscroservice.store.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.StoreOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.StoreOrderItem;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrder;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderItem;
import com.e_commerce.miscroservice.store.entity.vo.ShopPayTypeVO;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrderNew;
import com.e_commerce.miscroservice.store.mapper.FictitiousMapper;
import com.e_commerce.miscroservice.store.service.FictitiousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FictitiousServiceImp  implements FictitiousService {
    private Log logger=Log.getInstance(FictitiousServiceImp.class);
    @Autowired
    private FictitiousMapper fictitiousMapper;

    @Override
    public Response selectMoney(Long storeId){
        YjjStoreBusinessAccount yjjStoreBusinessAccount = fictitiousMapper.selectMoney(storeId);
        if (yjjStoreBusinessAccount==null){
            logger.info("暂无虚拟资金");
            return Response.errorMsg("暂无虚拟资金");
        }
        Map<String,Double> map=new HashMap<>();
        map.put("waitMoney",yjjStoreBusinessAccount.getWaitInMoney());
        return Response.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response selectMoney(Long orderNo, Long storeId,Double money) {
        //先查询余额是否够用
        YjjStoreBusinessAccount yjjStoreBusinessAccount = fictitiousMapper.selectMoney(storeId);
        if (yjjStoreBusinessAccount==null){
            return Response.errorMsg("该用户没有资金账户");
        }
        Double waitInMoney = yjjStoreBusinessAccount.getWaitInMoney();
        List<StoreOrderNew> storeOrderNews = fictitiousMapper.selectOrderByOrderNo(orderNo);
        if (storeOrderNews.size()==0){
            return Response.errorMsg("暂无该订单任何信息");
        }
        Double totalMoney=money;
//        if (storeOrderNews.size()>0){
//            for (StoreOrderNew storeOrderNew : storeOrderNews) {
//                totalMoney=totalMoney+(storeOrderNew.getTotalPay()+storeOrderNew.getTotalExpressMoney());
//            }
//        }
        if (waitInMoney>=totalMoney){
//            waitInMoney=waitInMoney-totalMoney;
            yjjStoreBusinessAccount.setWaitInMoney(waitInMoney-totalMoney);
            yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney()+yjjStoreBusinessAccount.getRealUseMoney());
//            int i=MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,);
            int i = fictitiousMapper.updateyjjStoreBusinessAccount(yjjStoreBusinessAccount.getWaitInMoney(),yjjStoreBusinessAccount.getCountMoney(), storeId);
            if (i!=1){
                logger.info("进行虚拟资金扣除失败");
                return Response.errorMsg("付款失败,请稍后重试");
            }
            //更新订单状态
            for (StoreOrderNew storeOrderNew : storeOrderNews) {
                if (storeOrderNew.getParentId()==-1){
                    List<StoreOrderNew> storeOrderNews1 = fictitiousMapper.selectOrderByParentId(orderNo);
                    for (StoreOrderNew orderNew : storeOrderNews1) {
                        orderNew.setPaymentType(4);//4虚拟资金支付类型
                        orderNew.setOrderStatus(10);//修改订单状态
                        orderNew.setPayTime(System.currentTimeMillis());//付款时间
                        int i1 = fictitiousMapper.updateOrder(orderNew);
                        if (i1!=1){
                            logger.info("订单更新失败");
                            return Response.errorMsg("订单更新失败");
                        }
                    }
                }else {
                    storeOrderNew.setPaymentType(4);//4虚拟资金支付类型
                    storeOrderNew.setOrderStatus(10);//修改订单状态
                    storeOrderNew.setPayTime(System.currentTimeMillis());//付款时间
                    int i1 = fictitiousMapper.updateOrder(storeOrderNew);
                    if (i1!=1){
                        logger.info("订单更新失败");
                        return Response.errorMsg("订单更新失败");
                    }
                }
            }
            //记录明细日志
           YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog=new  YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setOrderNo(Long.toString(orderNo));
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.CONSUME.getValue()+"-APP购买商品");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.CONSUME_SUCCESS.getCode());
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setOperMoney(totalMoney);
            yjjStoreBusinessAccountLog.setUserId(storeId);
            yjjStoreBusinessAccountLog.setRemainderMoney(fictitiousMapper.selectMoney(storeId).getWaitInMoney());
            yjjStoreBusinessAccountLog.setAboutOrderNo(Long.toString(orderNo));
            int save = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
            if (save!=1){
                logger.info("日志添加失败");
                return Response.errorMsg("日志添加失败");
            }
            return Response.success("下单成功");
        }else {
            return Response.errorMsg("余额不足,请充值");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response selectRealMoney(Long orderNo, Long storeId) {
        //先查询余额是否够用
        YjjStoreBusinessAccount yjjStoreBusinessAccount = fictitiousMapper.selectMoney(storeId);
        if (yjjStoreBusinessAccount==null){
            return Response.errorMsg("该用户没有资金账户");
        }
        Double realMoney = yjjStoreBusinessAccount.getRealUseMoney();
        List<StoreOrderNew> storeOrderNews = fictitiousMapper.selectOrderByOrderNo(orderNo);
        if (storeOrderNews.size()==0){
            return Response.errorMsg("暂无该订单任何信息");
        }
        Double totalMoney=0.0;
        if (storeOrderNews.size()>0){
            for (StoreOrderNew storeOrderNew : storeOrderNews) {
                totalMoney=totalMoney+(storeOrderNew.getTotalPay()+storeOrderNew.getTotalExpressMoney());
            }
        }
        if (realMoney>=totalMoney){
//            waitInMoney=waitInMoney-totalMoney;
            yjjStoreBusinessAccount.setRealUseMoney(realMoney-totalMoney);
            yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney()+yjjStoreBusinessAccount.getRealUseMoney());
//            int i=MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,);
            int i = fictitiousMapper.updateyjjStoreBusinessAccount(yjjStoreBusinessAccount.getRealUseMoney(),yjjStoreBusinessAccount.getCountMoney(), storeId);
            if (i!=1){
                logger.info("进行虚拟资金扣除失败");
                return Response.errorMsg("付款失败,请稍后重试");
            }
            //更新订单状态
            for (StoreOrderNew storeOrderNew : storeOrderNews) {
                if (storeOrderNew.getParentId()==-1){
                    List<StoreOrderNew> storeOrderNews1 = fictitiousMapper.selectOrderByParentId(orderNo);
                    for (StoreOrderNew orderNew : storeOrderNews1) {
                        orderNew.setPaymentType(4);//4虚拟资金支付类型
                        orderNew.setOrderStatus(10);//修改订单状态
                        orderNew.setPayTime(System.currentTimeMillis());//付款时间
                        int i1 = fictitiousMapper.updateOrder(orderNew);
                        if (i1!=1){
                            logger.info("订单更新失败");
                            return Response.errorMsg("订单更新失败");
                        }
                    }
                }else {
                    storeOrderNew.setPaymentType(5);//4虚拟资金支付类型
                    storeOrderNew.setOrderStatus(10);//修改订单状态
                    storeOrderNew.setPayTime(System.currentTimeMillis());//付款时间
                    int i1 = fictitiousMapper.updateOrder(storeOrderNew);
                    if (i1!=1){
                        logger.info("订单更新失败");
                        return Response.errorMsg("订单更新失败");
                    }
                }
            }
            //记录明细日志
            YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog=new  YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setOrderNo(Long.toString(orderNo));
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.CONSUME.getValue()+"-APP购买商品");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.APP_PAY_MONEY.getCode());
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setOperMoney(totalMoney);
            yjjStoreBusinessAccountLog.setUserId(storeId);
            yjjStoreBusinessAccountLog.setRemainderMoney(fictitiousMapper.selectMoney(storeId).getRealUseMoney());
            yjjStoreBusinessAccountLog.setAboutOrderNo(Long.toString(orderNo));
            int save = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
            if (save!=1){
                logger.info("日志添加失败");
                return Response.errorMsg("日志添加失败");
            }
            return Response.success("下单成功");
        }else {
            return Response.errorMsg("余额不足,请充值");
        }
    }

    @Override
    public Response selectPay(Long storeId,Long orderNo,Long blend) {
        Map<String, Object> data = new HashMap<String, Object>();
        YjjStoreBusinessAccount yjjStoreBusinessAccount = fictitiousMapper.selectMoney(storeId);
        Integer type = fictitiousMapper.selectTypeBystoreId(storeId);
        List<ShopPayTypeVO>  payTypeVOList= new ArrayList<ShopPayTypeVO>();
        ShopPayTypeVO payTypeVO;
        payTypeVO = new ShopPayTypeVO();

//        payTypeVO.setIcon("http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/FE19F80B-5C73-478D-B698-8DD372E25B0C.jpg");
//        payTypeVO.setName("余额支付");
//        payTypeVO.setType(4);
//        if (yjjStoreBusinessAccount==null&&type!=1){
//            logger.info("该用户暂无资金账户");
//        }else{
//            if (blend==1){
//                /**
//                 * 使用待结算付款的时候只有 用户在商家买了相同的衣服以后
//                 */
//                //这个订单下必须只有一种类型的商品且不能混批切购买的数量必须与用户在商家购买的数量相同
//                //根据orderNo查询订单商品的sku
//                List<StoreOrderItem> storeOrderItems = fictitiousMapper.selectOrderItem(storeId, orderNo);
//                if (storeOrderItems.size()==1){
//                    //说明改订单只有一种sku商品
//                    StoreOrderItem storeOrderItem = storeOrderItems.get(0);
//                    Long skuId = storeOrderItem.getSkuId();//商品的skuId
//                    Integer buyCount = storeOrderItem.getBuyCount();//商品的购买数量
//                    //根据storeId 去小程序订单里面去查询订单
//                    List<ShopMemberOrder> shopMemberOrders = fictitiousMapper.selectMemberOrder(storeId);
//                    if (shopMemberOrders.size()>0){
//                        for (ShopMemberOrder shopMemberOrder : shopMemberOrders) {
//                            String orderNumber = shopMemberOrder.getOrderNumber();
//                            List<ShopMemberOrderItem> shopMemberOrderItems = fictitiousMapper.selectMemberOrderItem(orderNumber,skuId);
//                            if (shopMemberOrderItems.size()==1){//说明不是混购这个订单下只有一中类型的sku商品
//                                ShopMemberOrderItem shopMemberOrderItem = shopMemberOrderItems.get(0);
//                                Integer count = shopMemberOrderItem.getCount();
//                                if (buyCount-count!=0){
//                                    logger.info("用户在商家购买的商品,与商家到平台购买商品的数量不一致");
//                                }else {
//                                    if (type==1&&yjjStoreBusinessAccount==null){
//                                        YjjStoreBusinessAccount yjjStoreBusinessAccount1=new YjjStoreBusinessAccount();
//                                        yjjStoreBusinessAccount1.setUserId(storeId);
//                                        int save = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccount1);
//                                        if (save!=1){
//                                            logger.info("创建资金面板出错");
//                                        }
//                                        yjjStoreBusinessAccount.setWaitInMoney(0.00);
//                                    }
//                                    payTypeVO.setMoney(yjjStoreBusinessAccount.getWaitInMoney());
//                                    payTypeVOList.add(payTypeVO);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//


        payTypeVO = new ShopPayTypeVO();
        payTypeVO.setIcon("http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/FE19F80B-5C73-478D-B698-8DD372E25B0C.jpg");
        payTypeVO.setName("余额支付");
        payTypeVO.setType(4);
        if (yjjStoreBusinessAccount==null&&type!=1){
            logger.info("该用户暂无资金账户");
        }else{
            if (type==1&&yjjStoreBusinessAccount==null){
                YjjStoreBusinessAccount yjjStoreBusinessAccount1=new YjjStoreBusinessAccount();
                yjjStoreBusinessAccount1.setUserId(storeId);
                int save = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccount1);
                if (save!=1){
                    logger.info("创建资金面板出错");
                }
                yjjStoreBusinessAccount.setRealUseMoney(0.00);
            }
            payTypeVO.setMoney(yjjStoreBusinessAccount.getRealUseMoney());
            payTypeVOList.add(payTypeVO);
        }

        payTypeVO = new ShopPayTypeVO();
        payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/weixin.jpg");
        payTypeVO.setName("微信支付");
        payTypeVO.setType(3);
        payTypeVOList.add(payTypeVO);


        payTypeVO = new ShopPayTypeVO();
        payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/zhifubao.jpg");
        payTypeVO.setName("支付宝支付");
        payTypeVO.setType(2);
        payTypeVOList.add(payTypeVO);
        data.put("paylist", payTypeVOList);
        return Response.success(data);
    }

}
