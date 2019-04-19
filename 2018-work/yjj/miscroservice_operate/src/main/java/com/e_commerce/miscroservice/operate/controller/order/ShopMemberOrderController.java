package com.e_commerce.miscroservice.operate.controller.order;

import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.service.order.ShopMemberOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 18:07
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "/operate/shopMemberOrder" )
public class ShopMemberOrderController{


    @Autowired
    private ShopMemberOrderService shopMemberOrderService;


    /**
     * 小程序订单查询
     *
     * @param pageNumber      分页
     * @param pageSize        分页
     * @param createTimeCeil  下单时间,低值
     * @param createTimeFloor 下单时间,高值
     * @param orderStatus     订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     * @param receiverName    收货人姓名
     * @param expressNo       物流单号
     * @param storeName       商品商家
     * @param memberName      下单会员昵称
     * @param memberId        下单会员id
     * @param memberGrade     下单会员分销角色
     * @return "data": {
     * "shopOrderList": {
     * "pages": 6,
     * "list": [
     * {
     * "orderNumber": "15415839944699580", //订单号
     * "payTime": "", //支付时间
     * "receiverName": "凄凄切切", //收货人
     * "count": 1, //sku数量
     * "totalMoney": 0.01, //订单原价
     * "goldCoinEarning": 0.00, //金币总收益
     * "orderStatus": 0, //订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     * "updateTime": "2018-11-07 17:46:34", //更新时间
     * "storeId": 3, //商户id
     * "userId": 171, //下单人id
     * "paymentType": 0, //第三方支付类型：0(无)、1微信小程序、2微信公众号
     * "payMoney": 0.01, //实际支付
     * "receiverPhone": "18842680139", //收货人手机号
     * "createTime": "2018-11-07 17:46:34", //下单时间
     * "grade": 1, //角色 分销级别 (0 无等级 1 店长 2分销商 3合伙人)
     * "storeName": "一直么1", //商户名
     * "expressNo": 123123123, //物流单号
     * "userName": "下单人名称", //商户名
     * "id": 203, // 订单id
     * "cashEarning": 0.00 //现金收益
     * }
     * ]
     * }
     * }
     * @author Charlie
     * @date 2018/11/8 19:10
     */
    @RequestMapping( "listOrder" )
    public Response listOrder(
            Integer pageNumber,
            Integer pageSize,
            @RequestParam( value = "createTimeCeil", required = false ) String createTimeCeil,
            @RequestParam( value = "createTimeFloor", required = false ) String createTimeFloor,
            @RequestParam( value = "orderStatus", required = false ) Integer orderStatus,
            @RequestParam( value = "receiverName", required = false ) String receiverName,
            @RequestParam( value = "expressNo", required = false ) String expressNo,
            @RequestParam( value = "storeName", required = false ) String storeName,
            @RequestParam( value = "memberName", required = false ) String memberName,
            @RequestParam( value = "memberId", required = false ) Long memberId,
            @RequestParam( value = "memberGrade", required = false ) Integer memberGrade
    ) {
        ShopMemberOrderQuery query = new ShopMemberOrderQuery ();
        query.setPageNumber(pageNumber);
        query.setPageSize(pageSize);
        query.setCreateTimeCeilStr (createTimeCeil);
        query.setCreateTimeFloorStr (createTimeFloor);
        query.setOrderStatus(orderStatus);
        query.setReceiverName(receiverName);
        query.setExpressNo(expressNo);
        query.setStoreName(storeName);
        query.setMemberName(memberName);
        query.setMemberId(memberId);
        query.setMemberGrade(memberGrade);
        return Response.success (shopMemberOrderService.listOrder (query));
    }


    /**
     * 订单详情
     *
     * @param orderNo 订单id(注意不是订单编号)
     * @return "data": {
     * "id": 199,
     * "orderNumber": "15415738569738372", //订单编号
     * "storeId": 3,
     * "memberId": 168,
     * "totalExpressAndMoney": 0.01,
     * "totalMoney": 0.01,
     * "payMoney": 0.01,
     * "saleMoney": 0.00,
     * "expressMoney": 0.00,
     * "count": 1,
     * "summaryImages": "https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877978591528787797859.jpg",
     * "orderType": 1,
     * "orderStatus": 4,
     * "paymentNo": "4200000238201811074644052492", //流水号
     * "cancelReasonType": 0,
     * "cancelReason": "",
     * "orderStopTime": 0,
     * "userNickname": "桑里桑气的厨子",
     * "couponId": 0,
     * "couponName": "",
     * "couponLimitMoney": 0.00,
     * "sendMessage": 0,
     * "payFormId": "wx071457373029130987d509c01934006603",
     * "paymentType": 1,
     * "expreeSupplierCnname": "EMS经济快递/EMS",
     * "expressSupplier": "ems",
     * "expressNo": "12345678",
     * "receiverName": "bug", //收货人
     * "receiverPhone": "13208156421", //收件人电话
     * "receiverAddress": "北京市北京市东城区不太好的",
     * "confirmSignedDate": 20181107,
     * "confirmSignedTime": 1541573975348,
     * "remark": "",
     * "buyWay": 0,
     * "goldCoin": 0.00,
     * "storeName": "一直么1",  //商户名
     * "createTimeStr": "2018-11-07 14:57:36", //下单时间
     * "updateTimeStr": "2018-11-07 14:57:42",  //修改时间
     * "deliveryTimeStr": "2018-11-07 14:59:25", //发货时间
     * "orderStopTimeStr": "",
     * "orderFinishTimeStr": "2018-11-07 14:59:35", //订单完成时间
     * "takeDeliveryTimeStr": "", //提货时间
     * "shopMemberOrderItemQueryList": [
     * {
     * "id": 382,
     * "productSkuId": 43537,
     * "count": 1,
     * "summaryImages": "[\"https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877978591528787797859.jpg\",\"https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877977881528787797788.jpg\",\"https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877977241528787797724.jpg\",\"https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877976341528787797634.jpg\"]",
     * "color": "黑色白帽",
     * "size": "2XL",
     * "price": 0.01,
     * "productName": "韩国定制 松紧腰",
     * "clothesNumber": "媧恩2392"
     * }
     * ],
     * "shouldBePayMoney": 0.01, //应付
     * "realPayMoney": 0.01, //实付金额
     * "realPayGoldCoin": 0, //实付金币
     * "cashGoldCoinRate": "", //金币现金贿赂
     * "earningCount": 0, //收益数量
     * "cashEarning": 0, //现金收益金额
     * "goldCoinEarning": 0 //金币收益金额
     * }
     * @author Charlie
     * @date 2018/11/8 19:50
     */
    @RequestMapping( "orderDetail" )
    public Response orderDetail(String orderNo) {
        return Response.success (shopMemberOrderService.orderDetailByOrderNo (orderNo));
    }



    /**
     * 订单收益明细
     *
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @param id 收益id
     * @param type 收益类型
     * @param status 1:待结算,2:已结算
     * @param operCashCeil 收益金额,高值
     * @param operCashFloor 收益金额,低值
     * @param operGoldCoinCeil 收益金币,高值
     * @param operGoldCoinFloor 收益金币,低值
     * @param userName 受益人名称
     * @param userMemberId 受益人id (受益人角色就不查了,总共5条数据,没意义)
     * @param orderNo 订单编号(必填)
     * @return "data": {
     *     "orderDetail": { //订单详情
     *       "id": 201,
     *       "orderNumber": "15415751955074903", //订单编号
     *       "storeId": 3,
     *       "memberId": 169,
     *       "totalExpressAndMoney": 0.01,
     *       "totalMoney": 0.01,
     *       "payMoney": 0.01, //实付
     *       "saleMoney": 0.00,
     *       "expressMoney": 0.00,
     *       "count": 1,
     *       "summaryImages": "https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15287877978591528787797859.jpg",
     *       "orderType": 1,
     *       "orderStatus": 6,
     *       "paymentNo": "4200000217201811079109329327",
     *       "cancelReasonType": 0,
     *       "cancelReason": "",
     *       "orderStopTime": 0,
     *       "takeDeliveryTime": 0,
     *       "orderFinishTime": 0,
     *       "userNickname": "七七八八",
     *       "couponId": 0,
     *       "couponName": "",
     *       "couponLimitMoney": 0.00,
     *       "sendMessage": 0,
     *       "payFormId": "wx071519560315363165356c612348773187",
     *       "paymentType": 1,
     *       "expreeSupplierCnname": "顺丰快递",
     *       "expressSupplier": "shunfeng",
     *       "expressNo": "12345678",
     *       "receiverName": "哈哈哈",
     *       "receiverPhone": "18667913990",
     *       "receiverAddress": "北京市北京市东城区哈哈哈哈",
     *       "deliveryTime": 1540280067000,
     *       "confirmSignedDate": 0,
     *       "confirmSignedTime": 0,
     *       "payTimeStr": "2017-10-10 10:10:10", //下单时间
     *       "remark": "",
     *       "expressInfo": "{\"result\":{\"com\":\"shunfeng\",\"no\":\"12345678\",\"ischeck\":true,\"data\":[{\"context\":\"卖家发货，如长时间未更新建议咨询快递公司或卖家。联系电话：[95338]\",\"time\":\"2017-11-25 12:41:17\"},{\"context\":\"商品已经下单\",\"time\":\"2017-11-08 09:39:46\"},{\"context\":\"您的包裹已出库\",\"time\":\"2017-10-21 16:45:55\"},{\"context\":\"您的订单开始处理\",\"time\":\"2017-10-21 16:12:02\"},{\"context\":\"当前订单已确认收货，交易成功。如未收到包裹，请及时联系卖家解决\",\"time\":\"2017-08-30 20:52:57\"},{\"context\":\"您已在成都华润凤凰城二期6栋店完成取件，感谢使用菜鸟驿站，期待再次为您服务。\",\"time\":\"2017-07-26 16:14:28\"},{\"context\":\"您的包裹已打包\",\"time\":\"2017-07-17 09:20:35\"},{\"context\":\"您的包裹已出库\",\"time\":\"2017-07-17 09:20:35\"},{\"context\":\"您的订单信息审核通过\",\"time\":\"2017-07-16 20:55:38\"},{\"context\":\"您的订单开始处理\",\"time\":\"2017-07-16 18:46:14\"},{\"context\":\"当前订单已确认收货，交易成功。如未收到包裹，请及时联系卖家解决\",\"time\":\"2017-07-06 11:00:08\"},{\"context\":\"您的快件已被玛斯兰德格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15852946283\",\"time\":\"2017-05-01 14:00:42\"},{\"context\":\"您的快件已被理想家园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-05-01 13:15:31\"},{\"context\":\"您的快件已被盛城东星速递易【自提柜】代收，请及时取件。有问题请联系派件员18821774027\",\"time\":\"2017-05-01 12:51:09\"},{\"context\":\"您的快件已被荷康花园A05/A06（乐城）速递易【自提柜】代收，请及时取件。有问题请联系派件员13590257377\",\"time\":\"2017-05-01 12:22:25\"},{\"context\":\"您的快件已被新东方龙湾B区【自提柜】代收，请及时取件。有问题请联系派件员15552133078\",\"time\":\"2017-05-01 12:18:22\"},{\"context\":\"您的快件已被竹溪苑【自提柜】代收，请及时取件。有问题请联系派件员18577767689\",\"time\":\"2017-05-01 11:51:07\"},{\"context\":\"您的快件已被锦绣天下速递易【自提柜】代收，请及时取件。有问题请联系派件员13367171777\",\"time\":\"2017-05-01 11:49:01\"},{\"context\":\"您的快件已被新月鸿锦苑【自提柜】代收，请及时取件。有问题请联系派件员13120825218\",\"time\":\"2017-05-01 11:44:07\"},{\"context\":\"您的快件已被麟瑞商务广场速递易【自提柜】代收，请及时取件。有问题请联系派件员18563901120\",\"time\":\"2017-05-01 11:14:55\"},{\"context\":\"您的快件已被绵阳高新区倍特领尚一期【自提柜】代收，请及时取件。有问题请联系派件员18989287030\",\"time\":\"2017-05-01 11:09:55\"},{\"context\":\"您的快件已被蓝钻100格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15015919595\",\"time\":\"2017-05-01 10:56:13\"},{\"context\":\"您的快件已被众安隐龙湾商业广场1幢速递易【自提柜】代收，请及时取件。有问题请联系派件员15958117230\",\"time\":\"2017-05-01 10:40:31\"},{\"context\":\"您的快件已被万邦国际名城速递易【自提柜】代收，请及时取件。有问题请联系派件员18906553378\",\"time\":\"2017-05-01 10:39:31\"},{\"context\":\"您的快件已被新中苑格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15914073024\",\"time\":\"2017-05-01 10:38:30\"},{\"context\":\"您的快件已被东皇先锋北区【自提柜】代收，请及时取件。有问题请联系派件员15543538250\",\"time\":\"2017-05-01 10:12:34\"},{\"context\":\"您的快件已被东山花园B区速递易【自提柜】代收，请及时取件。有问题请联系派件员15571710588\",\"time\":\"2017-05-01 10:04:33\"},{\"context\":\"您的快件已被龙湖三千城一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13981905979\",\"time\":\"2017-05-01 10:04:08\"},{\"context\":\"您的快件已被丝绸小区【自提柜】代收，请及时取件。有问题请联系派件员13656100859\",\"time\":\"2017-05-01 09:37:59\"},{\"context\":\"您的快件已被锦尚春天b区速递易【自提柜】代收，请及时取件。有问题请联系派件员17508140103\",\"time\":\"2017-05-01 09:24:06\"},{\"context\":\"您的快件已被自贡市汇景苑【自提柜】代收，请及时取件。有问题请联系派件员18882016315\",\"time\":\"2017-05-01 09:14:40\"},{\"context\":\"您的快件已被雨花村大门口【自提柜】代收，请及时取件。有问题请联系派件员13770589971\",\"time\":\"2017-05-01 07:43:10\"},{\"context\":\"您的快件已被成都青羊区上河新城九栋【自提柜】代收，请及时取件。有问题请联系派件员13540194034\",\"time\":\"2017-05-01 07:33:43\"},{\"context\":\"您的快件已被东原D7速递易【自提柜】代收，请及时取件。有问题请联系派件员13883423396\",\"time\":\"2017-05-01 00:08:25\"},{\"context\":\"您的快件已被托乐嘉街区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18668075017\",\"time\":\"2017-04-30 22:07:43\"},{\"context\":\"您的快件已被托乐嘉街区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18668075017\",\"time\":\"2017-04-30 22:07:01\"},{\"context\":\"您的快件已被领海国际一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13153631355\",\"time\":\"2017-04-30 21:02:56\"},{\"context\":\"您的快件已被理想家园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 19:27:31\"},{\"context\":\"您的快件已被景明佳园四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13451811474\",\"time\":\"2017-04-30 19:21:30\"},{\"context\":\"您的快件已被景明佳园四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13451811474\",\"time\":\"2017-04-30 19:20:30\"},{\"context\":\"您的快件已被柳州供电段单位宿舍速递易【自提柜】代收，请及时取件。有问题请联系派件员15007728956\",\"time\":\"2017-04-30 18:14:07\"},{\"context\":\"您的快件已被成都金科天籁城10栋架空层1号机【自提柜】代收，请及时取件。有问题请联系派件员15884553228\",\"time\":\"2017-04-30 17:25:57\"},{\"context\":\"您的快件已被上游三区速递易【自提柜】代收，请及时取件。有问题请联系派件员15007728956\",\"time\":\"2017-04-30 16:35:52\"},{\"context\":\"您的快件已被澳门花园东区速递易【自提柜】代收，请及时取件。有问题请联系派件员15153269321\",\"time\":\"2017-04-30 16:28:48\"},{\"context\":\"您的快件已被澳门花园东区速递易【自提柜】代收，请及时取件。有问题请联系派件员15153269321\",\"time\":\"2017-04-30 16:27:23\"},{\"context\":\"您的快件已被广安市世博花园【自提柜】代收，请及时取件。有问题请联系派件员15982606686\",\"time\":\"2017-04-30 16:19:38\"},{\"context\":\"您的快件已被海棠佳苑B速递易【自提柜】代收，请及时取件。有问题请联系派件员17508140103\",\"time\":\"2017-04-30 15:36:57\"},{\"context\":\"您的快件已被理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 14:28:08\"},{\"context\":\"您的快件已被理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 14:27:54\"},{\"context\":\"您的快件已被华昱家园速递易【自提柜】代收，请及时取件。有问题请联系派件员18500914002\",\"time\":\"2017-04-30 13:38:11\"},{\"context\":\"您的快件已被龙禧园四区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13821581300\",\"time\":\"2017-04-30 13:28:57\"},{\"context\":\"您的快件已被青龙鹭苑速递易【自提柜】代收，请及时取件。有问题请联系派件员15983825944\",\"time\":\"2017-04-30 13:13:54\"},{\"context\":\"您的快件已被滨江苑二期速递易【自提柜】代收，请及时取件。有问题请联系派件员15171249240\",\"time\":\"2017-04-30 12:50:44\"},{\"context\":\"您的快件已被云翔·卡纳源筑一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13573893040\",\"time\":\"2017-04-30 12:49:56\"},{\"context\":\"您的快件已被安德利迎海花园速递易【自提柜】代收，请及时取件。有问题请联系派件员13255456577\",\"time\":\"2017-04-30 12:46:30\"},{\"context\":\"您的快件已由成都华润凤凰城二期6栋店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-04-07 18:57:10\"},{\"context\":\"您的快件已由金盛路天地新城天一座01栋19号车库菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-03-29 12:30:07\"},{\"context\":\"您的快件已由福州连江官坂大街糕兴西饼屋店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-03-11 17:04:56\"},{\"context\":\"您的快件已被SX金色丽城A格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-18 15:33:12\"},{\"context\":\"您的快件已由成都东苑C区店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-02-17 14:55:30\"},{\"context\":\"您的快件已被协信阿卡迪亚E2组团格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-13 17:52:17\"},{\"context\":\"您的快件已被锦西人家四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-11 10:41:06\"},{\"context\":\"您的快件已由济南盛福花园46号楼6单元店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-01-24 16:06:18\"},{\"context\":\"您的快件已被金雅苑三期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-19 23:09:33\"},{\"context\":\"您的快件已被南源花园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-19 09:11:36\"},{\"context\":\"您的快件已被BJ理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-13 10:54:04\"},{\"context\":\"您的快件已被千禧园格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-12 10:10:53\"},{\"context\":\"您的快件已被BJ晓月六里格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-12 08:47:35\"},{\"context\":\"您的快件已被HB国瑞园中区入口西侧格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-10 09:48:59\"}],\"company\":\"顺丰速运\",\"updatetime\":\"2018-11-07 15:31:19\"},\"reason\":\"成功\",\"error_code\":0}",
     *       "buyWay": 0,
     *       "goldCoin": 0.00,
     *       "storeName": "一直么1" //所属商家
     *     },
     *     "dataList": {
     *       "pageNum": 1,
     *       "pageSize": 1,
     *       "size": 1,
     *       "startRow": 1,
     *       "endRow": 1,
     *       "total": 2,
     *       "pages": 2,
     *       "list": [
     *         {
     *           "shouldInGoldCoin": 32.00, //应收金币
     *           "earningsCash": 12.80, //收益现金
     *           "currencyRatio": 0.20, //收益金币比例
     *           "earningsRatio": 0.16, //收益现金比例
     *           "shouldInCash": 12.80, //应收现金
     *           "userName": "桑里桑气的厨子", //受益人
     *           "type": 1, //0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,10.分销商的团队收益入账,11.合伙人的团队收益入账
     *           "orderEarningsSnapShoot": 16.00, //订单总收益
     *           "userId": 168, //受益人id
     *           "orderSuccessTime": "", //订单完成时间
     *           "earningsGoldCoin": 32.00, //收益金币
     *           "orderPayTime": "2018-11-07 15:20:57", //订单支付时间
     *           "id": 116, //收益id
     *           "status": 1, //1:待结算,2:已结算
     *           "operTime": "" //结算时间
     *         }
     *       ]
     *     },
     *     "statistics": { //汇总信息
     *       "realCashEarnings": 0, //已结算现金收益(实发)
     *       "cashEarnings": 12.80, //现金收益
     *       "earningsCount": 1,  //收益数量
     *       "waitGoldCoinEarnings": 32.00,//待结算金币收益
     *       "realGoldCoinEarnings": 0,//已结算金币收益(实发)
     *       "goldCoinEarnings": 32.00,//金币收益
     *       "waitCashEarnings": 12.80 //待结算现金收益
     *     }
     *   }
     * @author Charlie
     * @date 2018/11/12 9:42
     */
    @RequestMapping( "orderEarningDetail" )
    public Response orderEarningDetail(
            Integer pageNumber,
            Integer pageSize,
            String userName,
            String orderNo,
            Long userMemberId,
            Integer type,
            Integer status,
            Long id,
            BigDecimal operCashCeil,
            BigDecimal operCashFloor,
            BigDecimal operGoldCoinCeil,
            BigDecimal operGoldCoinFloor
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        query.setId (id);
        query.setType (type);
        query.setStatus (status);
        query.setOperCashCeil (operCashCeil);
        query.setOperCashFloor (operCashFloor);
        query.setOperGoldCoinCeil (operGoldCoinCeil);
        query.setOperGoldCoinFloor (operGoldCoinFloor);
        query.setUserMemberId (userMemberId);
        query.setUserName (userName);
        query.setOrderNo (orderNo);
        return Response.success (shopMemberOrderService.orderEarningDetail (query));
    }

}
