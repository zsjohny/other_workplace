package com.e_commerce.miscroservice.distribution.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.distribution.service.DataDictionaryService;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * 分销收益
 */
@RestController
@RequestMapping("/distribution/cashOutIn")
public class DstbCashOutInController {

    private Log logger = Log.getInstance(DstbCashOutInController.class);

    @Value("${dstb.xiaochengxu2dstb.salt}")
    private String xiaochengxu2DstbSalt;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ShopMemberAccountService shopMemberAccountService;

    /**
     * 新增流水
     *
     * @param orderNumber 订单编号
     * @param memberId    会员id
     * @param storeId     门店id
     * @param realPay     实际消费
     * @param payTime     订单支付时间
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/9 12:02
     */
    @RequestMapping("addDstbFromOrder")
    public Response addDstbFromOrder(
            String orderNumber,
            Long memberId,
            Long storeId,
            BigDecimal realPay,
            Long payTime,
            HttpServletRequest request
    ) {
        boolean isLegal = HttpUtils.simpleVerify(request, xiaochengxu2DstbSalt, "wx2DstbSign", "orderNumber");
        if (!isLegal) {
            logger.warn("addDstbFromOrder--请求验证未通过 storeId={},memberId={}, orderNumber={}", storeId, memberId, orderNumber);
            return Response.error("请求验证未通过");
        }
        ShopMemberOrderVo vo = new ShopMemberOrderVo();
        vo.setOrderNo(orderNumber);
        vo.setMemberId(memberId);
        vo.setStoreId(storeId);
        vo.setRealPay(realPay);
        vo.setPayTime(payTime);
        try {
            shopMemberAccountService.addDstbFromOrder(vo);
            return Response.success();
        } catch (ErrorHelper e) {
            //记录日志
            e.printStackTrace();
            return ResponseHelper.errorHandler(e);
        }
    }


    /**
     * 个人中心 收益统计
     *
     * @param userId id
     *               <p>
     *               {
     *               "todayCoin": 1,  今日金额
     *               "wait": 1, 待结算
     *               "already": 1, 已结算 可用金额 可提现
     *               "count": 200, 总金额
     *               "goldCash": 1 金币
     *               }
     * @return
     */
    @RequestMapping("/user/income/statistics")
    public Response getUserIncomeStatistics(Long userId) {
        JSONObject jsonObject = shopMemberAccountService.getUserIncomeStatistics(userId);
        return Response.success(jsonObject);
    }

    /**
     * 账户金额 -总金额-订单佣金-管理佣金
     *
     * @param userId id
     *               <p>
     *               {
     *               "commissionBill": { 佣金账单信息(分享商品的收益也在订单佣金中)
     *               "wait": 1, 待结算
     *               "already": 1, 已结算
     *               "today": 1, 今日新增
     *               "count": 200 总金额
     *               },
     *               "manageBill": { 管理账单信息
     *               "wait": 1,待结算
     *               "already": 1,已结算
     *               "today": 1,今日新增
     *               "count": 200总金额
     *               }
     *               }
     * @return
     */
    @RequestMapping("/count/bill")
    public Response countBill(Long userId) {
        return shopMemberAccountService.countBill(userId);
    }

    /**
     * 金币-金币信息
     *
     * @param userId id
     *               <p>
     *               {
     *               "wait": 1, 待结算
     *               "already": 1, 已结算
     *               "today": 1, 今日新增
     *               "count": 200 总金额
     *               }
     * @return
     */
    @RequestMapping("/count/gold/bill")
    public Response countGoldBill(Long userId) {
        JSONObject jsonObject = shopMemberAccountService.countGoldBill(userId);
        return Response.success(jsonObject);
    }

    /**
     * 账单明细
     *
     * @param type      类型 "0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账," +
     *                  "10.分销商的团队收益入账,11.合伙人的团队收益入账," +
     *                  "20.签到,21.签到阶段奖励," +
     *                  "50.提现-总额,51.提现-佣金,52提现-管理金"
     * @param inOutType 1 进账  2出账
     * @param status    "状态 0:失效,1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态),10提现中,11提现成功,12提现失败"
     * @param userId    id
     * @param choose    0，现金1，金币
     * @param page      页码
     *                  <p>
     *                  private Long id;
     *                  (value = "in_out_type", commit = "1:进账,2:出账", isNUll = false, length = 4)
     *                  (value = "user_id", commit = "小程序用户id", isNUll = false, length = 20)
     *                  (value = "order_no", commit = "下单订单号/提现订单号", length = 40)
     *                  (value = "payment_no", commit = "微信返回订单号,没有则为平台自动生成", length = 64)
     *                  (value = "status", commit = "1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态)", isNUll = false, length = 4)
     *                  (value = "oper_cash",commit = "操作现金", defaultVal = "0",length = 7,precision = 2)
     *                  (value = "balance_cash", commit = "操作后现金余额", defaultVal = "0",length = 7,precision = 2)
     *                  (value = "oper_gold_coin",commit = "操作金币", defaultVal = "0",length = 7,precision = 2)
     *                  (value = "strTime",commit = "操作时间(时间戳)", length = 20, defaultVal = "0")
     *                  (value = "original_gold_coin", commit = "原有金币", length = 7,precision = 2, defaultVal = "0")
     *                  (value = "original_cash", commit = "原有现金", length = 7,precision = 2, defaultVal = "0")
     *                  (value = "order_earnings_snapshoot", commit = "当时订单收益快照", length = 7,precision = 2, defaultVal = "0")
     *                  (value = "earnings_ratio",commit = "收益比例", defaultVal = "0",length = 7,precision = 2)
     *                  private BigDecimal earningsRatio;
     *                  (value = "currency_ratio",commit = "货币比例", defaultVal = "0",length = 7,precision = 2)
     *                  (value = "type",
     *                  commit = "0订单佣金,1管理奖金,2提现,3签到",
     *                  isNUll = false, length = 4)
     *                  ( value = "remark", commit = "备注", length = 500 )
     *                  (value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
     *                  (value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
     * @return
     */
    @RequestMapping("/bill/details")
    public Response findBillDetails(@RequestParam(value = "choose", defaultValue = "0") Integer choose, @RequestParam(value = "type", required = false) Integer type, @RequestParam(value = "inOutType", required = false) Integer inOutType,
                                    @RequestParam(value = "status", required = false) Integer status, Long userId, Integer page) {
        return shopMemberAccountService.findBillDetails(choose, type, inOutType, status, userId, page);
    }

    /**
     * 分销成功
     * <p>
     * 确认收货后,分销返利结算,分销管理奖待结算,粉丝升级店长
     * </p>
     *
     * @param orderNumber      订单编号
     * @param orderSuccessTime 确认收货时间
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/9 12:02
     */
    @RequestMapping("dstbSuccess")
    public Response dstbSuccess(
            String orderNumber,
            Long orderSuccessTime,
            HttpServletRequest request
    ) {
        logger.info("分销成功 orderNumber={}, orderSuccessTime={}", orderNumber, orderSuccessTime);
        boolean isLegal = HttpUtils.simpleVerify(request, xiaochengxu2DstbSalt, "wx2DstbSign", "orderNumber");
        if (!isLegal) {
            logger.warn("dstbSuccess--请求验证未通过 orderNumber={}", orderNumber);
//            return Response.error("请求验证未通过");
        }
        ShopMemberOrderVo vo = new ShopMemberOrderVo();
        vo.setOrderSuccessTime(orderSuccessTime);
        vo.setOrderNo(orderNumber);
        try {
            shopMemberAccountService.dstbSuccess(vo);
        } catch (ErrorHelper e) {
            logger.error(e.getMsg());
            return ResponseHelper.errorHandler(e);
        }
        return Response.success();
    }


    /**
     * 团队收益待结算到可用
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/10 21:44
     */
    @RequestMapping("teamInWait2Alive")
    public Response teamInWait2Alive() {
        try {
            shopMemberAccountService.teamInWait2Alive();
        } catch (ErrorHelper e) {
            logger.error(e.getMsg());
            return ResponseHelper.errorHandler(e);
        }
        return Response.success();
    }


    /**
     * 提现规则
     *
     * @param groupCode groupCode
     * @param code      code
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/17 15:28
     */
    @RequestMapping("cashOutRule")
    public Response cashOutRule(
            @RequestParam(value = "groupCode", defaultValue = "cashOut") String groupCode,
            @RequestParam(value = "code", defaultValue = "limitMoney") String code,
            @RequestParam(value = "userId", defaultValue = "0") String userId) {
        
        //测试账号
        if (userId.equals("66576")) {
            groupCode = "cashOut_defaults";
        }
        return Response.success(dataDictionaryService.findByCodeAndGroupCode(code, groupCode));
    }


    /**
     * 提现
     *
     * @param userId    用户id
     * @param operMoney 提现金额
     * @return{ "msg": "",
     * "code": 200,
     * "data": {
     * "cashOutOk": false, //提现成功或者失败
     * "money": "20", //提现金
     * "createTime": "2018-10-19 14:38:34", //提现时间
     * "username": "施云云云云云云云" //用户昵称
     * }
     * }
     * @author Charlie
     * @date 2018/10/13 16:41
     */
    @RequestMapping("cashOut")
    public Response cashOut(Long userId, BigDecimal operMoney, HttpServletRequest request) {
        try {
            return Response.success(shopMemberAccountService.cashOut(userId, operMoney, request));
        } catch (ErrorHelper e) {
            logger.error(e.getMsg());
            return ResponseHelper.errorHandler(e);
        }
    }


    /**
     * 团队订单-团队订单信息
     *
     * @param userId id
     *               <p>
     *               todaySize 今日新增
     *               coin 金币
     *               money 现金
     *               countOrder  订单
     * @return
     */
    @RequestMapping("/team/order/count")
    public Response teamOrderCount(Long userId) {
        return shopMemberAccountService.teamOrderCount(userId);
    }

    /**
     * 团队订单-订单列表
     *
     * @param userId  id
     * @param page    页码
     * @param orderNo 订单编号
     *                <p>
     *                totalCash分销团队总收益(现金)
     *                totalGoldCoin分销团队总收益(金币)
     *                userNickname昵称
     *                orderNumber订单编号
     *                orderMoney订单金额
     * @return
     */
    @RequestMapping("/team/order")
    public Response teamOrderList(Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "orderNo", required = false) String orderNo) {
        return shopMemberAccountService.teamOrderList(userId, page, orderNo);
    }

    /**
     * 团队订单信息详情
     *
     * @param userId  id
     * @param orderNo 订单号
     *                <p>
     *                totalCommissionCash分销团队(佣金)总收益(现金
     *                totalCommissionGoldCoin分销团队(佣金)总收益(金币)
     *                totalManagerCash分销团队(管理金)总收益(现金)
     *                totalManagerGoldCoin分销团队(管理金)总收益(金币)
     *                订单号
     *                private String orderNumber;
     *                订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     *                private Integer orderStatus;
     *                订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
     *                private Integer orderType;
     *                昵称
     *                private String userNickname;
     *                支付时间
     *                private String payTime;
     *                确认收货时间
     *                private String confirmSignedTime;
     *                商品主图
     *                private String summaryImages;
     *                商品标题
     *                private String name;
     *                商品颜色名称
     *                private String color;
     *                商品尺码名称
     *                private String size;
     *                总数
     *                private Integer count;
     *                支付金额
     *                private Double payMoney;
     *                邮费
     *                private Double expressMoney;
     *                优惠金额
     *                private Double saleMoney;
     *                收益 现金
     *                private Double money;
     *                收益 金币
     *                private Double coin;
     *                收益比率
     *                private Double orderEarningsSnapshoot;
     *                金币
     *                private Double goldCoin;
     *                金币现金比
     *                private Double earningsRatio;
     *                操作金币
     *                private Double operGoldCoin;
     *                操作现金
     *                private Double operCash;
     *                操作时间
     *                private String operTime;
     *                流水
     *                private String paymentNo;
     *                0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,10.分销商的团队收益入账,11.合伙人的团队收益入账,20.签到,21.签到阶段奖励,30.订单取消,31.订单抵扣,50.提现-总额,51.提现-佣金,52提现-管理金
     *                private Integer type;
     *                1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态)
     *                private Integer status;
     * @return
     */
    @RequestMapping("/team/order/detail")
    public Response teamOrderDetail(Long userId, String orderNo) {
        return shopMemberAccountService.teamOrderDetail(userId, orderNo);
    }


    /**
     * 账户金额-详情-收支详情
     *
     * @param id     id
     * @param userId 用户id
     *               <p>
     *               orderNumber;订单号
     *               orderStatus;订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     *               userNickname昵称
     *               payTime;支付时间
     *               confirmSignedTime确认收货时间;
     *               summaryImages;商品主图
     *               name;商品标题
     *               color;商品颜色名称
     *               size商品尺码名称;
     *               count总数;
     *               payMoney支付金额;
     *               expressMoney邮费;
     *               saleMoney优惠金额;
     *               money;收益现金
     *               coin;收益金币
     *               orderEarningsSnapshoot;收益比率
     *               goldCoin;金币
     *               earningsRatio;金币现金比
     *               operGoldCoin;操作金币
     *               operCash操作现金
     *               operTime操作时间
     *               paymentNo流水
     *               type，0订单佣金,1管理奖金,2提现,3签到
     *               status，1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态)
     * @return
     */
    @RequestMapping("/order/account/details")
    public Response findOrderAccountDetails(Long id, Long userId) {
        return shopMemberAccountService.findOrderAccountDetails(id, userId);
    }
}
