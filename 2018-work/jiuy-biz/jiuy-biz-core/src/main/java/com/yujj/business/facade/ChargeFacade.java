/**
 * 
 */
package com.yujj.business.facade;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.InvitedUserActionLog;
import com.jiuyuan.entity.JiuCoinExchangeLog;
import com.jiuyuan.entity.UserInviteRewardLog;
import com.jiuyuan.entity.account.UserBankCardSign;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayNotify;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.newentity.alipay.direct.mobile.MobileAlipayNotify;
import com.jiuyuan.entity.newentity.alipay.direct.mobile.RSA;
import com.jiuyuan.entity.notification.StoreNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.util.PayUtil;
import com.jiuyuan.util.bankcardpay.BankCardPayConfig;
import com.jiuyuan.util.bankcardpay.MobileBankcardNotify;
import com.yujj.business.adapter.DataminingAdapter;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.InvitedUserActionLogService;
import com.yujj.business.service.NotificationService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.UserBankCardSignService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserInviteRewardLogService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.JiuCoinExchangeLogMapper;
import com.yujj.dao.mapper.OrderLogMapper;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderNew;
import com.yujj.util.uri.UriParams;

import cmb.MerchantCode;



/**
 * @author LWS
 */
@Service
public class ChargeFacade {
    private static final Logger logger = LoggerFactory.getLogger("PAY");

    @Autowired
    private OrderService orderService;
    
    @Autowired
    StoreBusinessMapper storeBusinessMapper;
    
    @Autowired
    NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private UserBankCardSignService userBankCardSignService;
    
    @Autowired
    private DataminingAdapter dataminingAdapter;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private OrderCouponService orderCouponService;
    
    @Autowired
    private OrderLogMapper orderLogMapper;
    
    @Autowired
    private InvitedUserActionLogService invitedUserActionLogService;
    
    @Autowired
    private UserInviteRewardLogService userInviteRewardLogService;
    
    @Autowired
    private JiuCoinExchangeLogMapper jiuCoinExchangeLogMapper;
    
    public String buildRequestParaString4Mobile(OrderNew orderNew) {
    	//TODO 金额做了修改,注意测试
    	String total_fee = String.valueOf(orderNew.getUserPracticalPayMoneyOfYuan());
        StringBuilder builder = new StringBuilder();
        // 签约合作者身份ID
        builder.append("partner=\"").append(AlipayConfig.partner).append("\"");
        // 签约卖家支付宝账号
        builder.append("&seller_id=\"").append(AlipayConfig.seller_email).append("\"");
        // 商户网站唯一订单号
        builder.append("&out_trade_no=\"").append(orderNew.getOrderNo()).append("\"");
        // 商品名称
        builder.append("&subject=\"").append(_SUBJECT).append("\"");
        // 商品详情
        builder.append("&body=\"").append(_BODY).append("\"");
        // 商品金额
        builder.append("&total_fee=\"").append(total_fee).append("\"");
        // 服务器异步通知页面路径
        builder.append("&notify_url=\"").append(AlipayConfig.notify_url).append("\"");
        // 服务接口名称， 固定值
        builder.append("&service=\"mobile.securitypay.pay\"");
        // 支付类型， 固定值
        builder.append("&payment_type=\"").append(AlipayConfig.payment_type).append("\"");
        // 参数编码， 固定值
        builder.append("&_input_charset=\"").append(AlipayConfig.input_charset).append("\"");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        builder.append("&return_url=\"").append(AlipayConfig.return_url).append("\"");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        // 对订单做RSA 签名
        String sign = RSA.sign(builder.toString(), AlipayConfig.rsa_private_key, "UTF-8");
        // 仅需对sign 做URL编码
        sign = EncodeUtil.encodeURL(sign, "UTF-8");

        // 完整的符合支付宝参数规范的订单信息
        return builder.append("&sign=\"").append(sign).append("\"").append("&sign_type=\"RSA\"").toString();
	}
    
    //售后补差价
    public String buildBankCardRequestParaString4AfterSale(ServiceTicket serviceTicket, String ip) {
    	String signUrl = BankCardPayConfig.MERCHANT_SIGN_URL;
        String payUrl = BankCardPayConfig.MERCHANT_URL;
        String retUrl = BankCardPayConfig.MERCHANT_RET_URL;
        String mchNo = BankCardPayConfig.MCH_NO;
        String branchId = BankCardPayConfig.BRANCH_ID;
        String coNo = BankCardPayConfig.CO_NO;
        String amount = serviceTicket.getProcessMoney() + ""; //待修改
        String userId = serviceTicket.getUserId() + "";
        String key = BankCardPayConfig.KEY;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String tradeDate = UtilDate.getDate();
//        if(Integer.parseInt(sdf.format(new Date())) == 23){
//        	tradeDate = UtilDate.getDate(UtilDate.getPlanDayFromDate(new Date(), 1));
//        }
        String billNo = String.format("%010d", serviceTicket.getId());
		
    	StringBuilder builder = new StringBuilder();
    	
//   	测试联调环境支付URL
    	builder.append(BankCardPayConfig.PAY_REQUEST_URL);

    	
    	// 支付商户开户分行号
    	builder.append("BranchID=").append(BankCardPayConfig.BRANCH_ID);
    	// 支付商户号
    	builder.append("&CoNo=").append(BankCardPayConfig.CO_NO);
    	 		
    		// 商户网站唯一订单号
    	builder.append("&BillNo=").append(billNo);
    	
    	// 商品金额
    	builder.append("&Amount=").append(amount);
    	
    	// 交易日期
    	builder.append("&Date=").append(tradeDate);
    	// 服务器异步通知页面路径
    	builder.append("&MerchantUrl=").append(payUrl);
    	// 服务器异步通知页面参数
    	builder.append("&MerchantPara=").append("out_trade_no=").append(serviceTicket.getId()); //待修改
    	// 服务器RETURN通知页面参数
    	builder.append("&MerchantRetPara=").append("out_trade_no=").append(serviceTicket.getId()); //待修改
    	// 服务器异步通知页面参数
    	//builder.append("&MerchantPara=").append(order.getOrderNo());
    	// 设置未付款交易的超时时间
    	// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    	
    	
    	// 处理完请求后，当前页面跳转到商户指定页面的路径，可空
    	builder.append("&MerchantRetUrl=").append(retUrl);
    	
    	
    	String seq = UtilDate.getOrderNum().substring(8)+(int)(Math.random() * 100000);
    	String pno = serviceTicket.getUserId()+UtilDate.getOrderNum().substring(7);
    	
    	UserBankCardSign userBankCardSign = null;
         // getMemCache
         String groupKey = MemcachedKey.GROUP_KEY_BANK_CARD_SIGN;
         String memKey = serviceTicket.getUserId() + "";
         Object obj = memcachedService.get(groupKey, memKey);
         if (obj != null ) {
        	 userBankCardSign = (UserBankCardSign) obj;
         }
         else {
        	 userBankCardSign = userBankCardSignService.getUserBankCardSign(serviceTicket.getUserId());
        	 memcachedService.set(groupKey, memKey, DateConstants.SECONDS_TEN_MINUTES, userBankCardSign);
         }
    	
    	
    	if(userBankCardSign != null){
    		seq = userBankCardSign.getSeq();
    		pno = userBankCardSign.getPno();
    	}   	
        String strReserved = "";
        String ts = UtilDate.getOrderNum();


        strReserved = "<Protocol><PNo>" + pno + "</PNo><TS>" + ts + "</TS><MchNo>" + mchNo + "</MchNo><Seq>" + seq + "</Seq><URL>" + signUrl + "</URL><Para></Para><MUID></MUID><Mobile></Mobile><LBS></LBS><RskLvl></RskLvl></Protocol>";
        // 生成校验码
        //MerchantPara="out_trade_no="+
        //MerchantCode mc = new MerchantCode();
        
        //正式String merchantCode = MerchantCode.genMerchantCode(BankCardPayConfig.KEY,UtilDate.getDate(),BankCardPayConfig.BRANCH_ID,BankCardPayConfig.CO_NO,billNo,PayUtil.formatPrice(order.getPayAmountInCents()),"out_trade_no="+order.getOrderNo(),BankCardPayConfig.MERCHANT_URL,order.getUserId()+"","111",ip,"54011600",strReserved);
        String merchantCode = MerchantCode.genMerchantCode(key, tradeDate, branchId, coNo, billNo, amount, "out_trade_no=" + serviceTicket.getId(), payUrl, userId ,"", ip, "54011600", strReserved);
        
    	//商户验证码
        builder.append("&MerchantCode=").append(merchantCode);
    	// 完整的符合支付宝参数规范的订单信息
    	return builder.toString();
    }
    
  //订单付款拼凑付款链接
    public String buildBankCardRequestParaString4Mobile(OrderNew order, String ip) {
    	String signUrl = BankCardPayConfig.MERCHANT_SIGN_URL;
    	String payUrl = BankCardPayConfig.MERCHANT_URL;
    	String retUrl = BankCardPayConfig.MERCHANT_RET_URL;
    	String mchNo = BankCardPayConfig.MCH_NO;
    	String branchId = BankCardPayConfig.BRANCH_ID;
    	String coNo = BankCardPayConfig.CO_NO;
    	String amount = order.getTotalPay() + order.getTotalExpressMoney() + "";
    	String userId = order.getUserId()+"";
    	String key = BankCardPayConfig.KEY;
    	SimpleDateFormat sdf = new SimpleDateFormat("HH");
    	String tradeDate = UtilDate.getDate();
//        if(Integer.parseInt(sdf.format(new Date())) == 23){
//        	tradeDate = UtilDate.getDate(UtilDate.getPlanDayFromDate(new Date(), 1));
//        }
    	String billNo = String.format("%010d", order.getOrderNo());
    	
    	StringBuilder builder = new StringBuilder();
    	
//   	测试联调环境支付URL
    	builder.append(BankCardPayConfig.PAY_REQUEST_URL);
    	
//    	 支付商户开户分行号
//    	builder.append("BranchID=\"").append(BankCardPayConfig.BRANCH_ID).append("\"");
//    	// 支付商户号
//    	builder.append("&CoNo=\"").append(BankCardPayConfig.CO_NO).append("\"");
//    	if(order.getOrderNo().length()>18){  		
//    	// 商户网站唯一订单号
//    	builder.append("&BillNo=\"").append(billNo).append("\"");
//    	}
//    	// 商品金额
//    	builder.append("&Amount=\"").append(PayUtil.formatPrice(order.getPayAmountInCents())).append("\"");
//    	// 服务器异步通知页面路径
//    	builder.append("&MerchantUrl=\"").append(BankCardPayConfig.MERCHANT_URL).append("\"");
//    	// 服务器异步通知页面参数
//    	builder.append("&MerchantPara=\"").append("out_trade_no=").append(order.getOrderNo()).append("\"");
//    	// 设置未付款交易的超时时间
//    	// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//    	
//    	
//    	// 处理完请求后，当前页面跳转到商户指定页面的路径，可空
//    	builder.append("&MerchantRetUrl=\"").append(BankCardPayConfig.MERCHANT_RET_URL).append("\"");
    	
    	// 支付商户开户分行号
    	builder.append("BranchID=").append(BankCardPayConfig.BRANCH_ID);
    	// 支付商户号
    	builder.append("&CoNo=").append(BankCardPayConfig.CO_NO);
    	
    	// 商户网站唯一订单号
    	builder.append("&BillNo=").append(billNo);
    	
    	// 商品金额
    	builder.append("&Amount=").append(amount);
    	
    	// 交易日期
    	builder.append("&Date=").append(tradeDate);
    	// 服务器异步通知页面路径
    	builder.append("&MerchantUrl=").append(payUrl);
    	// 服务器异步通知页面参数
    	builder.append("&MerchantPara=").append("out_trade_no=").append(order.getOrderNo());
    	// 服务器RETURN通知页面参数
    	builder.append("&MerchantRetPara=").append("out_trade_no=").append(order.getOrderNo());
    	// 服务器异步通知页面参数
    	//builder.append("&MerchantPara=").append(order.getOrderNo());
    	// 设置未付款交易的超时时间
    	// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    	
    	
    	// 处理完请求后，当前页面跳转到商户指定页面的路径，可空
    	builder.append("&MerchantRetUrl=").append(retUrl);
    	
    	
    	String seq = UtilDate.getOrderNum().substring(8)+(int)(Math.random() * 100000);
    	String pno = order.getUserId()+UtilDate.getOrderNum().substring(7);
    	
    	UserBankCardSign userBankCardSign = null;
    	// getMemCache
    	String groupKey = MemcachedKey.GROUP_KEY_BANK_CARD_SIGN;
    	String memKey = order.getUserId() + "";
    	Object obj = memcachedService.get(groupKey, memKey);
    	if (obj != null ) {
    		userBankCardSign = (UserBankCardSign) obj;
    	}
    	else {
    		userBankCardSign = userBankCardSignService.getUserBankCardSign(order.getUserId());
    		memcachedService.set(groupKey, memKey, DateConstants.SECONDS_TEN_MINUTES, userBankCardSign);
    	}
    	
    	
    	if(userBankCardSign != null){
    		seq = userBankCardSign.getSeq();
    		pno = userBankCardSign.getPno();
    	}   	
    	String strReserved = "";
    	String ts = UtilDate.getOrderNum();
    	//seq一天中唯一
//        保留字段，长度限制为1024字节。
//        以下为一网通支付协议相关字段：
//        <Protocol>
//        <PNo>客户协议号</PNo>非空
//        <TS>交易时间</TS>非空
//        <MchNo>协议商户企业编号</MchNo>非空
//
//        <Seq>协议开通请求流水号</Seq>协议开通时非空
//        <URL>协议开通结果通知命令请求地址</URL>协议开通时非空
//        <Para>协议开通结果通知命令参数</Para>可空
//
//        <MUID>协议用户ID</MUID>可空
//        <Mobile>协议手机号</Mobile>可空
//        <LBS>地理位置</LBS>可空
//        <RskLvl>客户风险等级</RskLvl>可空
//        </Protocol>
    	
    	strReserved = "<Protocol><PNo>" + pno + "</PNo><TS>" + ts + "</TS><MchNo>" + mchNo + "</MchNo><Seq>" + seq + "</Seq><URL>" + signUrl + "</URL><Para></Para><MUID></MUID><Mobile></Mobile><LBS></LBS><RskLvl></RskLvl></Protocol>";
    	// 生成校验码
    	//MerchantPara="out_trade_no="+
    	//MerchantCode mc = new MerchantCode();
    	
    	//正式String merchantCode = MerchantCode.genMerchantCode(BankCardPayConfig.KEY,UtilDate.getDate(),BankCardPayConfig.BRANCH_ID,BankCardPayConfig.CO_NO,billNo,PayUtil.formatPrice(order.getPayAmountInCents()),"out_trade_no="+order.getOrderNo(),BankCardPayConfig.MERCHANT_URL,order.getUserId()+"","111",ip,"54011600",strReserved);
    	String merchantCode = MerchantCode.genMerchantCode(key, tradeDate, branchId, coNo, billNo, amount, "out_trade_no=" + order.getOrderNo(), payUrl, userId ,"", ip, "54011600", strReserved);
    	
    	//商户验证码
    	builder.append("&MerchantCode=").append(merchantCode);
    	// 完整的符合支付宝参数规范的订单信息
    	return builder.toString();
    }
    
    
    
    
    //拼凑一网通直连接口信息
    public String buildBankCardRequestParaString4Query(OrderNew order) {
    	String signUrl = BankCardPayConfig.MERCHANT_SIGN_URL;
    	String payUrl = BankCardPayConfig.MERCHANT_URL;
    	String retUrl = BankCardPayConfig.MERCHANT_RET_URL;
    	String mchNo = BankCardPayConfig.MCH_NO;
    	String branchId = BankCardPayConfig.BRANCH_ID;
    	String coNo = BankCardPayConfig.CO_NO;
    	String amount = order.getTotalPay() + order.getTotalExpressMoney() + "";
    	String userId = order.getUserId()+"";
    	String key = BankCardPayConfig.KEY;
    	SimpleDateFormat sdf = new SimpleDateFormat("HH");
    	String tradeDate = UtilDate.getDate();
//        if(Integer.parseInt(sdf.format(new Date())) == 23){
//        	tradeDate = UtilDate.getDate(UtilDate.getPlanDayFromDate(new Date(), 1));
//        }
    	String billNo = String.format("%010d", order.getOrderNo());
    	
    	StringBuilder builder = new StringBuilder();
    	

    	
    	String seq = UtilDate.getOrderNum().substring(8)+(int)(Math.random() * 100000);
    	String pno = order.getUserId()+UtilDate.getOrderNum().substring(7);
    	
    	UserBankCardSign userBankCardSign = null;
    	// getMemCache
    	String groupKey = MemcachedKey.GROUP_KEY_BANK_CARD_SIGN;
    	String memKey = order.getUserId() + "";
    	Object obj = memcachedService.get(groupKey, memKey);
    	if (obj != null ) {
    		userBankCardSign = (UserBankCardSign) obj;
    	}
    	else {
    		userBankCardSign = userBankCardSignService.getUserBankCardSign(order.getUserId());
    		memcachedService.set(groupKey, memKey, DateConstants.SECONDS_TEN_MINUTES, userBankCardSign);
    	}
    	
    	
    	if(userBankCardSign != null){
    		seq = userBankCardSign.getSeq();
    		pno = userBankCardSign.getPno();
    	}   	
    	String strReserved = "";
    	String ts = UtilDate.getOrderNum();

    	
    	strReserved = "<Protocol><PNo>" + pno + "</PNo><TS>" + ts + "</TS><MchNo>" + mchNo + "</MchNo><Seq>" + seq + "</Seq><URL>" + signUrl + "</URL><Para></Para><MUID></MUID><Mobile></Mobile><LBS></LBS><RskLvl></RskLvl></Protocol>";
    	String requestPara = "";
    	
    	requestPara="<Request><Head><BranchNo>" + branchId + "</BranchNo><MerchantNo>" + mchNo + "</MerchantNo><Operator>001186</Operator><Password>001186</Password><TimeStamp>" + UtilDate.getTimeStrMs() + "</TimeStamp><Command>QuerySettledOrderBySettledDate</Command></Head><Body><BeginDate>" + UtilDate.getDate() + "</BeginDate><EndDate>" +  UtilDate.getDate() + "</EndDate><Count>20</Count><Operator>001186</Operator><pos></pos></Body><Hash>哈希值的16进制字符串，对“商户秘钥+Head+Body”的哈希值，算法是：SHA1</Hash></Request>";
    	
    	
    	
//    	<Request>
//    	<Head>
//    		<BranchNo>4位分行号</BranchNo>
//    		<MerchantNo>6位商户号</MerchantNo>
//    		<TimeStamp>请求发起的时间，精确到毫秒</TimeStamp>
//    		<Command>QuerySettledOrderBySettledDate</Command>
//    	</Head>
//    	<Body>
//    		<BeginDate>起始日期</BeginDate>
//    <EndDate>终止日期</EndDate>
//    <Count>查询条数</Count>
//    <Operator>操作员号</Operator>
//    <pos>续传包</pos>
//    	</Body>
//    	<Hash>哈希值的16进制字符串，对“商户秘钥+Head+Body”的哈希值，算法是：SHA1</Hash>
//    </Request>
    	

    	//正式String merchantCode = MerchantCode.genMerchantCode(BankCardPayConfig.KEY,UtilDate.getDate(),BankCardPayConfig.BRANCH_ID,BankCardPayConfig.CO_NO,billNo,PayUtil.formatPrice(order.getPayAmountInCents()),"out_trade_no="+order.getOrderNo(),BankCardPayConfig.MERCHANT_URL,order.getUserId()+"","111",ip,"54011600",strReserved);
    	String merchantCode = MerchantCode.genMerchantCode(key, tradeDate, branchId, coNo, billNo, amount, "out_trade_no=" + order.getOrderNo(), payUrl, userId ,"", "", "54011600", strReserved);
    	
    	//商户验证码
    	builder.append("&MerchantCode=").append(merchantCode);
    	// 完整的符合支付宝参数规范的订单信息
    	return builder.toString();
    }
    
    public void facePayCallbackProcess(OrderNew order, Long time) throws Exception {
        StoreNotification storeNotification = new StoreNotification();
        storeNotification.setTitle("俞姐姐商家版");
        StringBuilder abstracts = new StringBuilder();
        User user = userMapper.getUser(order.getUserId());
        
        abstracts.append("您好！俞姐姐友情提示：用户");
        
        String userName = user.getUserNickname();
        if (user.getUserType() == UserType.PHONE)
        	userName = user.getUserName();
        else {
        	if (user.getBindPhone().length() == 11) {
            	userName = user.getBindPhone().substring(0, 3) + "****" + user.getBindPhone().substring(7);
        	}
        }
        
        abstracts.append(userName);
        abstracts.append(" 俞姐姐号");
        abstracts.append(user.getyJJNumber());
        abstracts.append("已通过扫描二维码支付方式向您支付人民币");
        abstracts.append(order.getTotalPay());
        abstracts.append("元。感谢您对俞姐姐的支持！谢谢");
        
        storeNotification.setAbstracts(abstracts.toString());
        storeNotification.setType("7");
	    class LinkUrl {
	    	@SuppressWarnings("unused")
			public String getFirstname() {
				return firstname;
			}
			@SuppressWarnings("unused")
			public String getFirstmoney() {
				return firstmoney;
			}
			@SuppressWarnings("unused")
			public String getSecondname() {
				return secondname;
			}
			public String firstname;
			public String firstmoney;
			public String secondname;
	    }
        
        LinkUrl linkUrl = new LinkUrl();
        linkUrl.firstname = user.getyJJNumber() + "";
        linkUrl.firstmoney = "￥" + order.getTotalPay();
        linkUrl.secondname = "扫码交易";
        storeNotification.setLinkUrl(JSONObject.toJSONString(linkUrl));
        storeNotification.setCreateTime(time);
        storeNotification.setUpdateTime(time);
        storeNotification.setImage("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14666814381681466681438168.png");
        storeNotification.setPushStatus(1);
        storeNotification.setPushTime(time);
        StoreBusiness store = storeBusinessMapper.getById(order.getBelongBusinessId());
        
        // 发送门店通知
        notificationService.pushCID(Client.STORE_GETUI_APP_ID,
        		Client.STORE_GETUI_APP_KEY,
        		Client.STORE_GETUI_MASTER_SECRET,storeNotification, store                  		
        		);
    }
    
    
    public String mobilePayCallbackDispose(UriParams uriParams) {
    	if(uriParams == null){
    		return "";
    	}
        logger.info("pay callback log:" + uriParams);
        
        String trade_status = uriParams.getSingle("trade_status");
        //3个月回调空处理
        if (trade_status != null && trade_status.equals("TRADE_FINISHED")){
        	return "";
        }
        
        String out_trade_no = uriParams.getSingle("out_trade_no");
        OrderNew order = orderService.getUserOrderNewByNo(out_trade_no);
        if (order == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        try { 
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            // 支付宝交易号
            String trade_no = uriParams.getSingle("trade_no");
            logger.debug("支付宝订单号：" + trade_no);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            // 计算得出通知验证结果
            boolean verify_result = MobileAlipayNotify.verify(uriParams.asSingleValueMap(","));
            if (verify_result) {// 验证成功
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                // 交易状态
                trade_status = uriParams.getSingle("trade_status");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 设置返回显示
                    // add by LWS
                    // 更新订单状态
                    if (order.getOrderStatus() == OrderStatus.UNPAID) {
                        long time = System.currentTimeMillis();
                        PaymentType paymentType = PaymentType.ALIPAY_SDK;
                        
                        //如果是单面付订单直接交易成功
                        if (order.getOrderType() == 2){
                            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.SUCCESS,
                                OrderStatus.UNPAID, time);
                            facePayCallbackProcess(order, time);
                            
                        } else {
                            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.PAID,
                                OrderStatus.UNPAID, time);
                        }                       
                            
                         //如果是售后订单，同步更新售后表信息
    					if(order.getOrderType() == 1){
    						int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
    					}
                    }
                    // String payinfoAfter = makePaymentFromAlipay(params);
                    // logger.info(payinfoAfter);
                }
                // 该页面可做页面美工编辑

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                // 支付成功页面
                // ////////////////////////////////////////////////////////////////////////////////////////
                logger.debug("验证成功");
            } else {
                // 该页面可做页面美工编辑
                logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", out_trade_no, trade_no);
                // 支付失败页面
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return out_trade_no;
    }
    
    //银行卡支付回调
    public String mobileBankcardPayCallbackDispose(UriParams uriParams) {
    	//logger.info("pay bankcard callback log1:" + uriParams);
    	logger.info("pay bankcard string callback log2:" + uriParams.toUnEncodeString());
    	logger.info("path:"+MobileBankcardNotify.class.getClassLoader().getResource("/public.key").getPath());

    											   
    	String merchantPara = uriParams.getSingle("MerchantPara");
//    	logger.info("merchantPara:" + merchantPara);
    	if( merchantPara != null && merchantPara.length() > 0 ){
    		
    		merchantPara = merchantPara.replaceAll("%3D", "=");
    		String[] splitArray = merchantPara.split("=");
    		//logger.info("splitArray length:" + splitArray.length);
    		
    		if( splitArray != null && splitArray.length >= 2){
    		//logger.info("splitArray[1]:" + splitArray[1]);
    		
    		String out_trade_no = splitArray[1];
    		OrderNew order = orderService.getUserOrderNewByNo(out_trade_no);
    		if (order == null) {
    				//order = orderService.getOrderByNo("2015120710203249283722189");
    				String msg = "can not find order11 of " + out_trade_no;
    				logger.error(msg);
    				throw new IllegalStateException(msg);
    			}else{
    				//logger.info("orderNo1:"+order.getOrderNo());
    			}
    			
    		
    	
    	try {
    		// 获取通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
    		// 商户订单号
    		// 一网通交易号
    		String billNo = uriParams.getSingle("BillNo");
    		//一网通支付优惠字段
    		String discountFlag = uriParams.getSingle("DiscountFlag");
    		String discountAmt = uriParams.getSingle("DiscountAmt");
    		
    		if(discountFlag == null || discountFlag.length() == 0){
    			discountFlag = "N";
    		}
    		if(discountAmt == null || discountAmt.length() == 0){
    			discountAmt = "0";
    		}
    		logger.info("一网通支付订单号：" + billNo);
    		// 获取一网通的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
    		// 计算得出通知验证结果
    		boolean verify_result = MobileBankcardNotify.verifySign(uriParams.toUnEncodeString());
    		logger.info("支付结果：" + verify_result);
    		if (verify_result) {// 验证成功
    			// ////////////////////////////////////////////////////////////////////////////////////////
    			// 请在这里加上商户的业务逻辑程序代码
    			
    			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
    			// 交易状态
    			String msg = uriParams.getSingle("Msg");
    			String serialNumber="";
    			if(msg!=null&&msg.length()>=38){
    				//交易序列号
    				serialNumber=msg.substring(18,38);
    			}
    			logger.info("支付流水号：" + serialNumber);
    			
    			String succeed = uriParams.getSingle("Succeed");
    			
    			String amount = uriParams.getSingle("Amount");
    			    			
    			logger.info("succeed：" + succeed + "@amount："+amount);
    			if (succeed.equals("Y") && (amount.equals(order.getTotalPay() + order.getTotalExpressMoney() + "") || amount.equals(order.getTotalPay() + order.getTotalExpressMoney() - Double.parseDouble(discountAmt) + ""))) {
    				// 判断该笔订单是否在商户网站中已经做过处理
    				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
    				// 如果有做过处理，不执行商户的业务程序
    				
    				// 设置返回显示
    				// 更新订单状态
    				if (order.getOrderStatus() == OrderStatus.UNPAID) {
    					long time = System.currentTimeMillis();
    					PaymentType paymentType = PaymentType.BANKCARD_PAY;    					
    					
                        //如果是单面付订单直接交易成功
                        if (order.getOrderType() == 2){
    						orderService.updateOrderPayStatus(order, billNo, paymentType, OrderStatus.PAID,
    								OrderStatus.SUCCESS, time);
                            facePayCallbackProcess(order, time);  						
                        } else {

    						// 如果是普通直购订单，则将状态更新为已支付状态
    						orderService.updateOrderPayStatus(order, billNo, paymentType, OrderStatus.PAID,
    								OrderStatus.UNPAID, time);
                        }

    					if(userBankCardSignService.selectUnSignCount(order.getUserId())>0){
    						
	    					userBankCardSignService.updateUserBankCardSign(order.getUserId(), 1);
		    				logger.debug("用户签约成功");
    					}
    					//记录一网通支付优惠信息
    					userBankCardSignService.addBankCardPayDiscount(order.getUserId(), order.getOrderNo(), discountFlag, Double.parseDouble(discountAmt), billNo, order.getTotalPay() + order.getTotalExpressMoney());
    					
    					//如果是售后订单，同步更新售后表信息
    					if(order.getOrderType() == 1){
    						int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
    					}
    					
    				}
    				// String payinfoAfter = makePaymentFromAlipay(params);
    				// logger.info(payinfoAfter);
    			}
    			// 该页面可做页面美工编辑
    			
    			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
    			// 支付成功页面
    			// ////////////////////////////////////////////////////////////////////////////////////////
    			logger.debug("验证成功");
    		} else {
    			// 该页面可做页面美工编辑
    			logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", out_trade_no, billNo);
    			// 支付失败页面
    		}
    	} catch (Exception e) {
    		logger.error("", e);
    	}
    		}else {
    			String msg = "can not find order2  "+splitArray[0];
			logger.error(msg);
			throw new IllegalStateException(msg);
			}
    		
    	} else{
    		String msg = "can not find order33  ";
			logger.error(msg);
			throw new IllegalStateException(msg);
    	}
    	
    	return merchantPara;
    }
    //银行卡签约回调
    public String mobileBankcardSignCallbackDispose(UriParams uriParams) {
    	logger.info("sign callback log:" + uriParams);
    	//logger.info("sign callback tostring log:" + uriParams.toString());
    	
    	String merchantPara = uriParams.getSingle("MerchantRetPara");
    	//logger.info("merchantPara:" + merchantPara);
    	if( merchantPara != null && merchantPara.length() > 0 ){
    		
    		merchantPara = merchantPara.replaceAll("%3D", "=");
    		String[] splitArray = merchantPara.split("=");
    		//logger.info("splitArray length:" + splitArray.length);
    		
    		if( splitArray != null && splitArray.length >= 2){
    		//logger.info("splitArray[1]:" + splitArray[1]);
    		
    		String out_trade_no = splitArray[1];
    		OrderNew order = orderService.getUserOrderNewByNo(out_trade_no);
    		if (order == null) {
    				//order = orderService.getOrderByNo("2015120710203249283722189");
    				String msg = "can not find order11 of " + out_trade_no;
    				logger.error(msg);
    				throw new IllegalStateException(msg);
    			}else{
    				//logger.info("orderNo1:"+order.getOrderNo());
    			}
		    	try {
		    		// 获取通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		    		// 商户订单号
		    		// 一网通交易号
		    		String billNo = uriParams.getSingle("BillNo");
		    		//logger.debug("一网通支付订单号：" + billNo);
		    		// 获取一网通的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		    		// 计算得出通知验证结果
		    		boolean verify_result = MobileBankcardNotify.verifySign(uriParams.toUnEncodeString());
		    		//logger.debug("签约结果：" + verify_result+",uriParams.toString:"+uriParams.toString());
		    		if (verify_result) {// 验证成功
		    			// ////////////////////////////////////////////////////////////////////////////////////////
		    			// 请在这里加上商户的业务逻辑程序代码
		    			
		    			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
		    			
		    			String succeed = uriParams.getSingle("Succeed");
		    			//标记为已经签约成功
		    			if (succeed.equals("Y")) {
		    				if(userBankCardSignService.selectUnSignCount(order.getUserId())>0){
	    						
		    					userBankCardSignService.updateUserBankCardSign(order.getUserId(), 1);
			    				logger.debug("用户签约成功");
	    					}
		    				
		    				
		    			}           
		    			
		    			
		    			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
		    			
		    			// ////////////////////////////////////////////////////////////////////////////////////////
		    			logger.debug("验证成功");
		    		} else {
		    			// 该页面可做页面美工编辑
		    			logger.error("签约回调验证失败, ", "", billNo);
		    			// 支付失败页面
		    		}
		    	} catch (Exception e) {
		    		logger.error("", e);
		    	}
    		}else {
    			String msg = "签约回调 can not find order 1 ";
			logger.error(msg);
			throw new IllegalStateException(msg);
			}
    		
    	} else{
    		String msg = "签约回调 can not find order 2";
			logger.error(msg);
			throw new IllegalStateException(msg);
    	}
    	//等待完成
    	return "";
    }

    /**
     * 处理支付请求参数
     * 
     * @param order
     * @param payconfig
     * @param response
     * @return
     * @throws IOException
     */
    public Map<String, String> payDispose(long orderNo, String ip) {
    	String out_trade_no = String.valueOf(orderNo);
    	OrderNew orderNew = orderService.getUserOrderNewByNo(out_trade_no);
//    	   String total_fee = PayUtil.formatPrice(order.getPayAmountInCents());
    		//删除就表示做了修改，从新表中获取金额 TODO	请注意测试
    	   String total_fee = PayUtil.formatPrice(orderNew.getUserPracticalPayMoneyOfFen());
        
        // 防钓鱼时间戳
        String anti_phishing_key = String.valueOf(System.currentTimeMillis());
        // 非局域网的外网IP地址，如：221.0.0.1
        String exter_invoke_ip = ip;
        // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", _SUBJECT);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", _BODY);
        sParaTemp.put("show_url", _SHOW_URL);
        sParaTemp.put("anti_phishing_key", anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
        
        /**
        // 记录支付信息到文件
        String payinfo = makePaymentFromOrder(order);
        logger.info(payinfo);
        // 更新支付日志
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", order.getId());
        params.put("orderNo", order.getOrderNo());
        params.put("userId", order.getUserId());
        params.put("paymentCount", -1);
        params.put("createTime", new Date().getTime());
        params.put("updateTime", new Date().getTime());
        params.put("operationPoint", Constants.CREATE_LOG_OPERATION);
        try {
            paymentLogMapper.createBeforePay(params);
        } catch (Exception e) {
            logger.info("重复订单，无需处理");
        }
        **/
        return sParaTemp;
    }

    /*private String makePaymentFromOrder(Order order) {
        if (null == order) {
            return "";
        }
        StringBuffer sbMsg = new StringBuffer();
        // 设置时间
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sbMsg.append("<starting to pay via ali direct pay on>:" + formatter.format(new Date()) + "\t");
        // 设置订单号
        sbMsg.append("<order number>:" + order.getOrderNo());
        // 设置付款金额：分
        sbMsg.append("<order need to pay total>:" + order.getPayAmountInCents());

        return sbMsg.toString();
    }*/

    /**
     * 同步回调处理逻辑
     * 
     * @param request
     * @param params
     * @return
     */
    public String payCallbackDispose(UriParams uriParams) {
        logger.info("pay callback log:" + uriParams);

        String out_trade_no = uriParams.getSingle("out_trade_no");
        OrderNew orderNew = orderService.getUserOrderNewByNo(out_trade_no);
        if (orderNew == null) {
            String msg = "can not find order of " + out_trade_no;
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            // 支付宝交易号
            String trade_no = uriParams.getSingle("trade_no");
            logger.debug("支付宝订单号：" + trade_no);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            // 计算得出通知验证结果
            boolean verify_result = AlipayNotify.verify(uriParams.asSingleValueMap(","));
            if (verify_result) {// 验证成功
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                // 交易状态
                String trade_status = uriParams.getSingle("trade_status");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 设置返回显示
                    // add by LWS
                    // 更新订单状态
                    if (orderNew.getOrderStatus() == OrderStatus.UNPAID) {
                        long time = System.currentTimeMillis();
                        PaymentType paymentType = PaymentType.ALIPAY;

                        // 如果是普通直购订单，则将状态更新为已支付状态
                        orderService.updateOrderPayStatus(orderNew, trade_no, paymentType, OrderStatus.PAID,
                            OrderStatus.UNPAID, time);
                    }
                    // String payinfoAfter = makePaymentFromAlipay(params);
                    // logger.info(payinfoAfter);
                }
                // 该页面可做页面美工编辑

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                // 支付成功页面
                // ////////////////////////////////////////////////////////////////////////////////////////
                logger.debug("验证成功");
            } else {
                // 该页面可做页面美工编辑
                logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", out_trade_no, trade_no);
                // 支付失败页面
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return out_trade_no;
    }

    /*private String makePaymentFromAlipay(Map<String, String> params) {
        if (null == params) {
            return "";
        }
        StringBuffer sbMsg = new StringBuffer();
        // 设置时间
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sbMsg.append("<get return from ali direct pay on>:" + formatter.format(new Date()) + "\t");
        // 设置订单号
        sbMsg.append("<order number>:" + params.get("out_trade_no"));
        // 设置支付金额
        sbMsg.append("<total fee>:" + params.get("total_fee"));
        // 设置买家支付宝
        sbMsg.append("<buyer email>:" + params.get("buyer_email"));
        // 设置交易号
        sbMsg.append("<trade number>:" + params.get("trade_no"));
        // 设置交易状态
        sbMsg.append("<trade status>:" + params.get("trade_status"));
        return sbMsg.toString();
    }*/

    private final String _SUBJECT = "俞姐姐平台支付订单";

    private final String _BODY = _SUBJECT + ":用于折扣购买【俞姐姐】官网商品";

    private final String _SHOW_URL = Constants.SERVER_URL + "/order/list.do";

	public Map<String, Object> paySuceess(String orderNo, long userId) {
		Map<String, Object> data = new HashMap<String, Object>();
		OrderNew orderNew = orderService.getUserOrderNewByNo(orderNo);
		
//		Order order = orderService.getOrderByNo(orderNo);
		
		if(orderNew !=null) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			String buyer = "";
			String address = "";
			
			String expressInfo = orderNew.getExpressInfo();
			if(expressInfo != null){
				
			String[] expressInfoArray = expressInfo.split(",");
				data.put("orderNoStr", orderNew.getOrderNo());
		        
				map.put("orderNo", orderNew.getOrderNo());
				map.put("totalMoney", orderNew.getTotalMoney());
				if(expressInfo.length() > 2) {
					buyer = expressInfoArray[0];
					address = expressInfoArray[2];
				}
				
				map.put("buyer", buyer);
				map.put("address", address);
	//		data.put("order", orderService.getOrderByNo(orderNo));
				data.put("order", map);
//				data.put("buyAlsoProduct", dataminingAdapter.getBuyAlsoProduct(userId, order.getId(), new PageQuery(1, 4)));
				
				data.put("buyAlsoProduct", dataminingAdapter.getBuyAlsoProductNew(userId, orderNew, new PageQuery(1, 4)));
			}
		}
		
		return data;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> paySuceessNew(String orderNo, long userId, ClientPlatform clientPlatform) {
//		logger.error("paySuceessNew first--------------------orderNo:" + orderNo + " userId" + userId);
		Map<String, Object> data = new HashMap<String, Object>();
		OrderNew order = orderService.getUserOrderNewByNo(orderNo);
		long currentTime = System.currentTimeMillis();
		if(order !=null) {
			InvitedUserActionLog actionLog = invitedUserActionLogService.getByUserId(userId, 0);
			
			/**
			 * 处理玖币
			 */
			handleJiucoin(actionLog, currentTime, order, orderNo, userId, clientPlatform);
			
			Map<String, Object> map = new HashMap<String, Object>();
			String buyer = "";
			String address = "";
			
			String expressInfo = order.getExpressInfo();
			if(expressInfo != null){
				
				String[] expressInfoArray = expressInfo.split(",");
				data.put("orderNoStr", order.getOrderNoStr());
				
				map.put("orderNo", order.getOrderNo());
				map.put("totalMoney", order.getTotalExpressMoney() + order.getTotalPay());//总付款金额
				if(expressInfo.length() > 2) {
					buyer = expressInfoArray[0];
					address = expressInfoArray[2];
				}
				
				map.put("buyer", buyer);
				map.put("address", address);
				//		data.put("order", orderService.getOrderByNo(orderNo));
				data.put("order", map);
				data.put("buyAlsoProduct", dataminingAdapter.getBuyAlsoProductNew(userId, order, new PageQuery(1, 4)));
			}
			
			if (actionLog != null) {

				
				//获取邀请有礼时间范围
				long start_time = 0;
				long end_time = 0;
				try {

			        JSONObject inviteGift_setting = globalSettingService.getJsonObject(GlobalSettingName.INVITE_GIFT_SETTING);
			        start_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("start_time"));
			        end_time = DateUtil.parseStrTime2Long(inviteGift_setting.getString("end_time"));
			        
			        //活动过期
			        if (System.currentTimeMillis() > end_time || System.currentTimeMillis() < start_time) return data;
			        
				} catch (Exception e) {
					return data;
				}

				//记录
				long invitorId = actionLog.getInvitor();
				User user = userService.getUser(invitorId);
				long lastInviteOrderTime = user.getLastInviteOrderTime();
				int weekInviteOrderCount = user.getWeekInviteOrderCount();
				if (lastInviteOrderTime < DateUtil.getWeekStart().getTime()) {
					weekInviteOrderCount = 1;
				} else {
					weekInviteOrderCount++;
				}
				userService.updateWeekInviteOrderCount(invitorId, weekInviteOrderCount);				
				
				//被邀请人行为记录表
				InvitedUserActionLog invitedUserActionLog = new InvitedUserActionLog();
				invitedUserActionLog.setAction(1);
				invitedUserActionLog.setUserId(userId);
				invitedUserActionLog.setInvitor(invitorId);
				invitedUserActionLog.setRelatedId(order.getOrderNo());
				invitedUserActionLog.setCreateTime(currentTime);
				invitedUserActionLogService.add(invitedUserActionLog);
				
				
		        /**
				 * @author Jeff.Zhan
				 * 邀请有礼规则2：被邀请人在注册后每X个成功订单送邀请人代金券
				 */
				JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INVITE_GIFT_2);
				for (Object object : jsonArray) {
					JSONObject jsonObject = (JSONObject)object;
					int expired_days = Integer.parseInt(jsonObject.get("expired_days").toString());
					long createTime = actionLog.getCreateTime();
					long currenTime = System.currentTimeMillis();
					
					long expiredTime = expired_days * DateUtils.MILLIS_PER_DAY;	
					
					if (currenTime - createTime <= expiredTime) {
						int orderCount = invitedUserActionLogService.getNewInvitedOrderCount(invitorId, 1, start_time, expiredTime);
						
						int every_order_count = Integer.parseInt(jsonObject.get("every_order_count").toString());
						
						if (orderCount != 0 && orderCount % every_order_count == 0) {
							int week_limit_time = Integer.parseInt(jsonObject.get("week_limit_time").toString());
							
							if (weekInviteOrderCount <= week_limit_time) {
//								int jiuCoin = Integer.parseInt(jsonObject.get("jiuCoin").toString());
								int coupon_count = Integer.parseInt(jsonObject.get("coupon_count").toString());
								long coupon_template_id = Long.parseLong(jsonObject.get("coupon_template_id").toString());
								
								userCoinService.updateUserCoin(invitorId, 0, 0, "inviteGift_2 rule", currenTime, UserCoinOperation.REGISTER_GRANT, null, clientPlatform.getVersion());
								
								//发放代金券
								try {
									orderCouponService.getCoupon(coupon_template_id, coupon_count, invitorId, CouponGetWay.INVITE, true);
								} catch (Exception e) {
									e.printStackTrace();
									logger.error("com.yujj.business.facade.ChargeFacade 规则二代金券发放失败，模板id:" + coupon_template_id);
								}
								
								UserInviteRewardLog userInviteRewardLog = new UserInviteRewardLog();
						        userInviteRewardLog.setCount(coupon_count);
						        userInviteRewardLog.setCouponTemplateId(coupon_template_id);
						        userInviteRewardLog.setJiuCoin(0);
						        userInviteRewardLog.setCreateTime(currentTime);
						        userInviteRewardLog.setUserId(invitorId);
						        userInviteRewardLogService.add(userInviteRewardLog);
							} 
						}
					}
				}
			}
		}
		
		return data;
	}

	private void handleJiucoin(InvitedUserActionLog actionLog, long currentTime, OrderNew order, String orderNo, long userId, ClientPlatform clientPlatform) {
		/**
		 * 加入玖币兑换记录表
		 */
		List<OrderItem> orderItems = orderService.getOrderNewItemsByOrderNO(userId, order.getOrderNo());
		List<JiuCoinExchangeLog> jiuCoinExchangeLogs = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getTotalUnavalCoinUsed() > 0) {
				JiuCoinExchangeLog jiuCoinExchangeLog = new JiuCoinExchangeLog();
				jiuCoinExchangeLog.setCreateTime(currentTime);
				jiuCoinExchangeLog.setRelatedId(orderItem.getSkuId());
				jiuCoinExchangeLog.setCount(orderItem.getBuyCount());
				jiuCoinExchangeLog.setType(2);
				jiuCoinExchangeLog.setUserId(order.getUserId());
				jiuCoinExchangeLog.setJiuCoin(orderItem.getTotalUnavalCoinUsed());
				
				jiuCoinExchangeLogs.add(jiuCoinExchangeLog);
			}
		}
		if (jiuCoinExchangeLogs.size() > 0) {
			jiuCoinExchangeLogMapper.batchAdd(jiuCoinExchangeLogs);
		}
		
	}
	
//	public Map<String, Object> paySuceess(String orderNo, long userId) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		Order order = orderService.getOrderByNo(orderNo);
//		Map<String, Object> map = new HashMap<String, Object>();
//		String buyer = "";
//		String address = "";
//		
//		String expressInfo = order.getExpressInfo();
//		String[] expressInfoArray = expressInfo.split(",");
//		
//		map.put("orderNo", order.getOrderNo());
//		map.put("totalMoney", order.getTotalMoney());
//		if(expressInfo.length() > 2) {
//			buyer = expressInfoArray[0];
//			address = expressInfoArray[2];
//		}
//		
//		map.put("buyer", buyer);
//		map.put("address", address);
////		data.put("order", orderService.getOrderByNo(orderNo));
//		data.put("order", map);
//		data.put("buyAlsoProduct", dataminingAdapter.getBuyAlsoProduct(userId, order.getId(), new PageQuery(1, 4)));
//		
//		return data;
//	}
	
	

	
	//1客户协议查询
	//2客户协议取消
	//3签约结果回调通知
	//用之前还得调试过
//
//	   public  Map<String, String> checkResult(final Order order) {
//	        final Map<String, String> result = new HashMap<String, String>();
//
//	        httpClientService.execute(new HttpClientQuery("checkResult") {
//	            
//	            @Override
//	            public void initLog(LogBuilder log) {
//	                log.append("orderId", order.getId());
//	            }
//	            
//	            @Override
//	            public CachedHttpResponse sendRequest() throws Exception {
//	               
//	                String BUSDAT="<xml><merch_date>"+UtilDate.getDate()+"</merch_date><merch_time>"+UtilDate.getOrderNum().substring(8)+"</merch_time><merch_serial>10130915190</merch_serial><cust_argno>697101309</cust_argno></xml>";
//	                String url = "http://58.61.30.110/CmbBank_B2B/UI/DIDI/DoBusiness.ashx";
//	                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//	                packageParams.add(new BasicNameValuePair("NTBNBR", BankCardPayConfig.MCH_NO));
//	                packageParams.add(new BasicNameValuePair("TRSCOD", "CMCX"));
//	                packageParams.add(new BasicNameValuePair("BUSDAT", BUSDAT));
//	                packageParams.add(new BasicNameValuePair("DATLEN", BUSDAT.length()+""));
//	                
//	                
//	                packageParams.add(new BasicNameValuePair("COMMID", "787878"));
//	                packageParams.add(new BasicNameValuePair("SIGTIM", UtilDate.getOrderNum()+"1234"));
//	              //  packageParams.add(new BasicNameValuePair("SIGDAT", ""));
//
//	                packageParams.add(new BasicNameValuePair("SIGDAT", new sun.misc.BASE64Encoder().encode(genSign(packageParams).getBytes())));
//
//	                HttpEntity entity = new StringEntity(new String(WeixinPayCore.toXml(packageParams).getBytes(), "UTF-8"));
//	                CachedHttpResponse response = httpClientService.post(url, entity);
//	                logger.debug("response:{}", response.getResponseText());
//	                return response;
//	            }
//	            
//	            @Override
//	            public boolean readResponse(String responseText, LogBuilder errorLog) {
//	                return result.containsKey("prepay_id");
//	            }
//	        });
//
//	        return result;
//	    }
//	   
//	 
//	   public static String genSign(List<NameValuePair> params) {
//	        StringBuilder sb = new StringBuilder();
//
//	        for (int i = 0; i < params.size(); i++) {
//	            sb.append(params.get(i).getName());
//	            sb.append('=');
//	            sb.append(params.get(i).getValue());
//	            if(i!=params.size()-1){
//	            	sb.append('&');	            	
//	            }
//	        }
//	        String packageSign = sb.toString().toUpperCase();
//	        return packageSign;
//	    }

	
}
