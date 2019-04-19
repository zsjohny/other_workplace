
package com.jiuy.store.tool.controller.mobile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.enums.PropertiesEnums;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.*;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.HttpClientUtils;
import com.jiuyuan.util.JiuyMultipartFile;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.*;
import com.store.util.sensitive.SensitiveWordUtil;
import com.util.ConstantId;
import com.util.PropertiesUtil;
import com.util.ServerPathUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.util.file.OSSFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.store.service.WxaProductService.weixinServiceUrl;


/**
 * 商家
 *
 * @author Administrator
 */
@Controller
@Login
@RequestMapping("/mobile/user")
@SuppressWarnings("unused")
public class MobileUserInfoController {

    @Autowired
    private OSSFileUtil ossFileUtil;

    private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;

    private static final String WXA_CODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

//    //2018年12月19日15:21:15
//    //添加
//    public static String weixinServiceUrl = WeixinPayConfig.getWeiXinServerUrl();
//    public static String getProductQrcodeUrl = "/code/getProductQrcodeUrl";

    @Autowired
    private WxaProductService wxaProductService;


    private static final Logger logger = LoggerFactory.getLogger(MobileUserInfoController.class);
    private static final int FREEZE_STATUS = 3;
    private static final int FIRST_LOGIN = 1;

    Log log = LogFactory.get();

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private HomeFacade homeFacade;

    @Autowired
    private StoreAuditServiceShop storeAuditService;

    @Autowired
    private StoreWxaService storeWxaService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopGlobalSettingService globalSettingService;

    @Autowired
    private NJShopMemberOrderService njShopMemberOrderService;

    @Autowired
    private IMyFinanceSupplierService myFinanceSupplierService;

    @Autowired
    private IOrderNewService supplierOrderService;

    @Autowired
    private IUserNewService supplierUserNewService;

    @Autowired
    private UserNewMapper userNewMapper;

    @Autowired
    private IStoreOrderNewService storeOrderNewService;

    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;

    @Autowired
    private IRefundOrderService refundOrderService;


    /**
     * 修改商家公告信息
     *
     * @return
     */
    @RequestMapping("/updateNotice/auth")
    @ResponseBody
    public JsonResponse updateNotice(@RequestParam(required = true) String storeNotice, UserDetail userDetail, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();

        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }


        //1、检测数据库设置的敏感词
        logger.info("修改商家公告时，检查敏感词storeNotice:" + storeNotice);
        String sensitiveWordStr = globalSettingService.getSetting(GlobalSettingName.SENSITIVE_WORD);
        if (StringUtils.isNotEmpty(sensitiveWordStr)) {
            String[] sensitiveWordArr = sensitiveWordStr.split(",");
            for (String sensitiveWord : sensitiveWordArr) {

                int index = storeNotice.indexOf(sensitiveWord);
                if (index > -1) {//大于-1时说明包含敏感词
                    logger.info("修改商家公告时，包含敏感词sensitiveWord：" + sensitiveWord + ",storeNotice:" + storeNotice);
//    				return jsonResponse.setResultCode(ResultCode.SENSITIVE_WORD_INCLUDED);
                    return jsonResponse.setError("内容包含敏感词“" + sensitiveWord + "”,请重新编辑");

                } else {
                    logger.info("修改商家公告时，不包含敏感词sensitiveWord：" + sensitiveWord + ",storeNotice:" + storeNotice);
                }
            }
        }
        //2、检测敏感词库中的敏感词***号代替
//    	logger.info("过滤敏感词前，storeNotice："+storeNotice);
        storeNotice = SensitiveWordUtil.replaceWords(storeNotice);
//    	logger.info("过滤敏感词后，storeNotice："+storeNotice);

        storeUserService.updateStoreNotice(storeId, storeNotice);

        //返回数据
        return jsonResponse.setSuccessful();
    }


    /**
     * 获取商家公告信息
     *
     * @return
     */
    @RequestMapping("/getNotice/auth")
    @ResponseBody
    public JsonResponse getNotice(UserDetail<StoreBusiness> userDetail, HttpServletResponse response,
                                  @ClientIp String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();

        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        StoreBusiness storeBusiness = userDetail.getUserDetail();

        Map<String, String> data = new HashMap<String, String>();
        data.put("storeId", String.valueOf(storeBusiness.getId()));
        data.put("storeNotice", storeBusiness.getStoreNotice());
        //返回数据
        return jsonResponse.setSuccessful().setData(data);
    }


    @RequestMapping("/demo")
    public JsonResponse demo(@RequestParam("storeId") Long storeId) {
        //共享
        logger.info("共享店铺查询二维码");
        //sql
        String path;
        Map<String, Object> auditEntity = userNewMapper.getShareShopLoginQr(storeId);
        if (auditEntity == null) {
            logger.info("没有店铺资料");
            path = "";
        } else {
            path = (String) auditEntity.get("shareQcCodeUrl");
            Long auditId = (Long) auditEntity.get("id");
            if (StringUtils.isBlank(path)) {
                logger.info("初始化共享店铺二维码start...");
                path = geneShareQrImg(storeId);
                int rec = userNewMapper.updateShareShopLoginQr(auditId, path);
                logger.info("初始化共享店铺二维码 rec={}", rec);
            }
        }
        return JsonResponse.successful(path);
    }

    /**
     * 获取商家信息
     *
     * @param userDetail
     * @param request
     * @return
     */
    @RequestMapping("/storeInfo/auth")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse storeInfo(UserDetail<StoreBusiness> userDetail, HttpServletRequest request) {
        /**
         * UserDetail:一个接口
         * StoreBusiness:门店商家实体类
         */
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        Map<String, Object> data = new HashMap<String, Object>();

        //销售订单
        long storeId = userDetail.getId();
        logger.info("获取商家信息,用户={}", storeId);

        // TODO: 2019/1/23 不用优化
        Map<String, Integer> memberOrderMap = njShopMemberOrderService.getMemberOrderStatusCount(storeId);
        storeBusiness = storeBusinessNewService.getById(storeId);
        //用户是否可用店铺 1可用,2不可用(暂时只判断是否冻结,不判断过期)
        Integer isOpenWxa = storeBusiness.getIsOpenWxa();
        data.put("isCanUseShop", isOpenWxa == 1 ? 1 : 0);


        data.put("memberOrderMap", memberOrderMap);
        data.put("storeBusiness", storeBusiness);

        //向老版本兼容 @see { data.put("tip", data.get("auditStatus")); } 是同一个值
        Integer dataAuditStatus = storeBusiness.getDataAuditStatus();
        if (dataAuditStatus == 0) {
            data.put("auditStatus", "审核中");
            data.put("auditStatusFlag", "0");
        } else if (dataAuditStatus == 1) {
            data.put("auditStatus", "已认证");
            data.put("auditStatusFlag", "1");
        } else if (dataAuditStatus == 2) {
            data.put("auditStatus", "未认证");
            data.put("auditStatusFlag", "2");
        } else if (dataAuditStatus == -1) {
            data.put("auditStatus", "已拒绝");
            data.put("auditStatusFlag", "-1");
        } else if (dataAuditStatus == -2) {
            data.put("auditStatus", "禁用");
            data.put("auditStatusFlag", "-2");
        } else {
            data.put("auditStatus", "未知");
            data.put("auditStatusFlag", "-9");
        }
        //向老版本兼容 店铺审核信息
        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");
        data.put("isBindWeixin", (StringUtils.isNotBlank(storeBusiness.getBindWeixinId())));
        //分享文案
        data.put("shareText", "关注小程序，获取更多精品女装。");
        data.put("orderCount", orderService.getUserOrderCount(storeBusiness.getId()));

        //分享小程序店铺
        Integer wxaBusinessType = storeBusiness.getWxaBusinessType();
        logger.info("店铺类型 {}", wxaBusinessType);
        if (wxaBusinessType == 1) {
            //共享
            String path;
            //sql
            Map<String, Object> auditEntity = userNewMapper.getShareShopLoginQr(storeId);
            logger.info("店铺资料={}", auditEntity);
            if (auditEntity == null) {
                path = "";
            } else {
                path = (String) auditEntity.get("shareQcCodeUrl");
                Long auditId = (Long) auditEntity.get("id");
                if (StringUtils.isBlank(path)) {
                    logger.info("初始化共享店铺二维码start...storeId" + storeId);
                    path = geneShareQrImg(storeId);
                    int rec = userNewMapper.updateShareShopLoginQr(auditId, path);
                    logger.info("初始化共享店铺二维码 rec={}", rec);
                }
            }
            data.put("qrCode", path);
        } else {
            data.put("qrCode", homeFacade.getQRCode(userDetail));
        }
        //分享url
        String shareHtml = ServerPathUtil.me().getServer2Url() + "/jstore/static/app/storeShareCode.html?storeId=" + storeId;
        logger.info("分享html-url={}", shareHtml);
        data.put("shareUrl", shareHtml);
        data.put("backImgUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/center/centerbg0919.png");
        String phonenumber = globalSettingService.getSetting(GlobalSettingName.PLATFORM_CUSTOMER_PHONENUMBER);
        if (!(StringUtils.isEmpty(phonenumber))) {
            data.put("phonenumber", phonenumber.toString());
        } else {
            data.put("phonenumber", "");
        }
        //获取供应商信息
        long supplierId = storeBusiness.getSupplierId() == null ? 0 : storeBusiness.getSupplierId();
        Map<String, Object> map = new HashMap<String, Object>();
        //如果存在供应商
        if (supplierId > 0) {
            //获取订单销售总额
            Map<String, Object> totalOrderAmountMap = myFinanceSupplierService.getTotalOrderAmount(supplierId);
            //获取待结算金额
            Map<String, Object> settlingAmountMap = myFinanceSupplierService.getSettlingAmount(supplierId);
            //获取可提现金额和提现申请订单数目
            double availableBalance = myFinanceSupplierService.getAvailableBalance(supplierId);
            int countOfDealingWDOrder = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
            map.put("totalOrderAmount", totalOrderAmountMap.get("totalOrderAmount"));
            map.put("availableBalance", availableBalance);
            map.put("countOfDealingWDOrder", countOfDealingWDOrder);
            map.put("settlingMoney", settlingAmountMap.get("settlingMoney"));
            //获取订单处理数目
            //获取待付款
            int unPaidCount = orderService.getSupplierOrderCount(supplierId, OrderStatus.UNPAID.getIntValue());
            //获取待发货
            int paidCount = orderService.getSupplierOrderCount(supplierId, OrderStatus.PAID.getIntValue());
            //获取待收货
            int deliverCount = orderService.getSupplierOrderCount(supplierId, OrderStatus.DELIVER.getIntValue());
            //获取已退单
            int closeCount = orderService.getSupplierOrderCount(supplierId, OrderStatus.CLOSED.getIntValue());
            //获取未结束的售后单个数
            int unfinishedRefundOrderCount = refundOrderService.getUnfinishedRefundOrderCount(supplierId);
            map.put("unPaidCount", unPaidCount);
            map.put("paidCount", paidCount);
            map.put("deliverCount", deliverCount);
            map.put("closeCount", closeCount);
            map.put("unfinishedRefundOrderCount", unfinishedRefundOrderCount);
        }
        data.put("supplierInfo", map);
        return new JsonResponse().setSuccessful().setData(data);
    }


    private String geneShareQrImg(Long storeId) {
        try {
            String id = PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(),PropertiesEnums.CONSTANTS_STORE_ID.getKey());
            String pageUrl = PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(), PropertiesEnums.PAGE_URL.getKey());
            String url = weixinServiceUrl + "/third/findAccessTokenByAppId";
            Map<String, Object> param = new HashMap<>(2);
            List<StoreWxa> storeWxas = userNewMapper.selectStoreWxaList(Long.parseLong(id));
            StoreWxa storeWxa;
            if (storeWxas.size() > 0) {
                String appId = storeWxas.get(0).getAppId();
                param.put("appId", appId);
                String token = HttpClientUtils.get(url, param);
                if (StringUtils.isBlank(token)) {
                    logger.error("没有获取到token, appId={}", appId);
                    return "";
                }
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("scene", "storeId=" + storeId + "&type=1");
                paramMap.put("page", pageUrl);
                paramMap.put("width", "430");

                String fileName = System.getProperty("java.io.tmpdir") + "/pic/" +
                        appId + "_storeId_" + storeId + "_type_1_page_index_index_.jpg";
                File file = HttpClientUtils.postInputStreamToFile(String.format(WXA_CODE_URL, token), JSONObject.toJSONString(paramMap),
                        fileName);
                if (file == null) {
                    log.error("生成商店二维码={}失败", storeId);
                    return null;
                }
                logger.info("fileName={},size={}", fileName, file.length());

                MultipartFile partFile = new JiuyMultipartFile(file);
                try {
                    file.createNewFile();
                    String path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, partFile);
                    logger.info("生成二维码成功!!!");
                    return path;
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("生成二维码失败" + e.getMessage());
                    return null;
                }
                //        }
            } else {
                logger.info("商家信息获取错误,");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 20; j++) {

                    String url = "http://weixinonline.yujiejie.com//third/findAccessTokenByAppId";
                    Map<String, Object> param = new HashMap<>(2);
                    StoreWxa storeWxa = null;
                    param.put("appId", "wx0436a7ecd9b6b9db");
                    String token = HttpClientUtils.get(url, param);

                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("scene", "storeId=" + 12928 + "&type=1");
                    paramMap.put("page", "page/index/index");
                    paramMap.put("width", "430");

                    long timeMillis = System.nanoTime();
                    HttpClientUtils.postInputStreamToFile(String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s"
                            , token), JSONObject.toJSONString(paramMap), "C:\\Users\\nessa\\Downloads\\" + timeMillis + ".jpg");

                }
            }).start();
        }
    }


    /**
     * 获取提现金额和最低提现额
     *
     * @param userDetail
     * @return
     */
    @RequestMapping("/getAvailableBalanceAndMinWithdraw/auth")
    @ResponseBody
    public JsonResponse getAvailableBalanceAndMinWithdraw(UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        long supplierId = storeBusiness.getSupplierId();
        if (supplierId <= 0) {
            logger.info("该商家未绑定供应商信息！");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        //获取供应商信息
        UserNew userNew = supplierUserNewService.getSupplierUserInfo(supplierId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("availableBalance", userNew.getAvailableBalance());
        data.put("minWithdrawal", userNew.getMinWithdrawal());
        return jsonResponse.setSuccessful().setData(data);
    }


    /**
     * 提交提现按钮
     *
     * @param applyMoney
     * @return
     */
    @RequestMapping("/submitApply/auth")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse submitApply(@RequestParam("applyMoney") double applyMoney,
                                    UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        long supplierId = storeBusiness.getSupplierId();
        if (supplierId <= 0) {
            logger.info("该商家未绑定供应商信息！");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        try {
            myFinanceSupplierService.submitApply(supplierId, applyMoney);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 提现记录列表接口
     *
     * @param minApplyMoney 申请金额下限
     * @param maxApplyMoney 申请金额上限
     * @param minCreateTime 格式：yyyy-MM-dd
     * @param maxCreateTime 格式：yyyy-MM-dd
     * @param status        -1：全部，0：未处理，1：处理完成，2：已拒绝，3:已冻结
     * @return
     */
    @RequestMapping("/getWithdrawList/auth")
    @ResponseBody
    public JsonResponse getWithdrawList(@RequestParam(value = "minApplyMoney", required = false, defaultValue = "0.0") double minApplyMoney,
                                        @RequestParam(value = "maxApplyMoney", required = false, defaultValue = "0.0") double maxApplyMoney,
                                        @RequestParam(value = "minCreateTime", required = false, defaultValue = "1970-1-1") String minCreateTime,
                                        @RequestParam(value = "maxCreateTime", required = false, defaultValue = "") String maxCreateTime,
                                        @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                        UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        long supplierId = storeBusiness.getSupplierId();
        if (supplierId <= 0) {
            logger.info("该商家未绑定供应商信息！");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        Page<Map<String, Object>> page1 = new Page<>(page, size);
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<WithdrawApplyNew> data = myFinanceSupplierService.getWithdrawOrderList(page1, supplierId, minApplyMoney, maxApplyMoney, minCreateTime, maxCreateTime, status);
            for (WithdrawApplyNew withdrawApply : data) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tradeId", withdrawApply.getTradeId());//提现订单号
                map.put("id", withdrawApply.getId());//提现订单主键ID
                map.put("applyMoney", withdrawApply.getApplyMoney());
                map.put("status", withdrawApply.getStatus());
                list.add(map);
            }
            page1.setRecords(list);
            return jsonResponse.setSuccessful().setData(page1);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 通过订单ID查看提现订单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/getWithdrawOrderInfo/auth")
    @ResponseBody
    public JsonResponse getWithdrawOrderInfo(@RequestParam("id") long id,
                                             UserDetail<StoreBusiness> userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        long supplierId = storeBusiness.getSupplierId();
        if (supplierId <= 0) {
            logger.info("该商家未绑定供应商信息！");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        WithdrawApplyNew data = myFinanceSupplierService.getWithdrawOrderInfo(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tradeId", data.getTradeId());//提现订单号
        map.put("id", data.getId());//提现订单主键ID
        map.put("applyMoney", data.getApplyMoney());
        map.put("status", data.getStatus());
        String createTime = "";
        String dealTime = "";
        if (data.getCreateTime() != null) {
            createTime = DateUtil.format(new Date(data.getCreateTime()), "yyyy-MM-dd HH:mm:ss");
        }
        map.put("createTime", createTime);

        if (data.getDealTime() != null) {
            dealTime = DateUtil.format(new Date(data.getDealTime()), "yyyy-MM-dd HH:mm:ss");
        }
        map.put("dealTime", dealTime);
        if (data.getStatus() == FREEZE_STATUS) {
            map.put("freezeTime", data.getFreezeTime());
        }
        String tradeWay = "无";
        if (data.getTradeWay() != null) {
            switch (data.getTradeWay()) {
                case 1:
                    tradeWay = "支付宝";
                    break;
                case 3:
                    tradeWay = "微信";
                    break;
                case 5:
                    tradeWay = "银行汇款";
                    break;
                case 6:
                    tradeWay = "现金交易";
                    break;
            }
        }
        map.put("tradeWay", tradeWay);
        map.put("tradeNo", data.getTradeNo());
        String remark = "无";
        if (data.getRemark() == null || data.getRemark().equals("")) {
            map.put("remark", remark);

        } else {
            remark = data.getRemark();
            map.put("remark", remark);
        }
        if (data.getMoney() != null) {
            map.put("tradeMoney", data.getMoney());
        }
        UserNew userNew = userNewMapper.selectById(data.getRelatedId());
        map.put("account", userNew.getAccount());
        return jsonResponse.setSuccessful().setData(map);
    }


    @RequestMapping(value = "/updatecid/auth")
    @ResponseBody
    public JsonResponse updateUserCid(@RequestParam("userCid") String userCid, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        if (userCid != null && userCid.length() > 0) {
            storeUserService.updateUserCid(userDetail.getId(), userCid);
        }
        return jsonResponse.setSuccessful();
    }

//    @RequestMapping(value = "/ucpaas/send")//, method = RequestMethod.POST
//    @ResponseBody
//    public JsonResponse ucpaasSendCode(
//    		@RequestParam("sendType") String sendType, UserDetail userDetail) {
//    	return userDelegator.ucpaasSendCode(sendType, userDetail);
//    }


    /**
     * 获取用户信息
     *
     * @param userDetail
     * @param request
     * @return
     */
    @RequestMapping("/auth")
    @ResponseBody
    public JsonResponse userInfo(UserDetail<StoreBusiness> userDetail, HttpServletRequest request) {
//        User user = userDetail.getUser();
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("user", user);
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("storeBusiness", storeBusiness);
// 		update by Charlie(唐静)
//        if(storeBusiness != null && storeBusiness.getActiveTime() > 0){
//        if(storeBusiness != null){
//        	data.put("auditStatus", "审核通过");
//        	data.put("auditStatusFlag", "1");
//        }else{
//        	int auditPassNum = storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.submit.getIntValue());
//        	if(auditPassNum > 0){
//        		data.put("auditStatus", "审核中");
//        		data.put("auditStatusFlag", "0");
//        	}else{
//        		data.put("auditStatus", "审核不通过");
//        		data.put("auditStatusFlag", "-1");
//        	}
//        }

        //分享文案
        data.put("shareText", "关注小程序，获取更多精品女装。");

        //分享url
        //https://online.yujiejie.com/jstore/static/app/storeShareCode.html?storeId=58
        data.put("shareUrl", Constants.SERVER_URL + "/static/app/storeShareCode.html?storeId=" + storeBusiness.getId());

        data.put("orderCount", orderService.getUserOrderCount(storeBusiness.getId()));

        data.put("qrCode", homeFacade.getQRCode(userDetail));

        data.put("backImgUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/center/centerbg0919.png");

        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");

        // System.out.println("REFERER:" + request.getHeader("REFERER"));

//        JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.SMS_IP_TIME_LIMIT);
//    	for (Object object : jsonArray) {
//			JSONObject jsonObject = (JSONObject)object;
//			int timeInSeconds = Integer.parseInt(jsonObject.get("timeInSeconds").toString());
//			int limitTimes = Integer.parseInt(jsonObject.get("limitTimes").toString());
//			System.out.println("timeInSeconds" + timeInSeconds);
//			System.out.println("limitTimes" + limitTimes);
//    	}

//        String groupKey = MemcachedKey.GROUP_KEY_VERIFY_YUNXIN_IP_LIMIT;
//    	String key = "yunxin";
//    	long obj =   memcachedService.getCounter(groupKey, key);
//    	
//    	if (obj == 1  ) {
//    		System.out.println("obj1:" + obj);
//    	//	memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE,  1);
//    		//memcachedService.setCounter(groupKey, key, 1);
//    		memcachedService.addCounter(groupKey, key, 2);
//    	}else{
//    		memcachedService.addCounter(groupKey, key, 2);
//    		System.out.println("obj2:" + obj );
//    	}

//        StoreOrder order = orderService.getUserOrderNewByNo(133, "472");
//        if(order != null){
//        	weixinChargeFacade.payRefund(order, "APP");
////        	weixinChargeFacade.prepayNew(order, "1.0.0.0", "APP");
//        }

//        StoreOrder storeOrder = orderNewMapper.getByOrderNo(216);
//        orderService.updateOrderPayStatus(storeOrder, "1111111", PaymentType.ALIPAY, OrderStatus.PAID,
//                OrderStatus.UNPAID, System.currentTimeMillis());

//        List<OrderItem> orderItemList = orderItemMapper.getOrderNewItemsByOrderNO(1,1);
//        storeProductMapper.insertStoreProduct(orderItemList);
//        Map<Long, OrderAfterSaleCountVO> afterSaleMap = afterSaleService.getOrderAfterSaleMap(1, 27);
        String phonenumber = globalSettingService.getSetting(GlobalSettingName.PLATFORM_CUSTOMER_PHONENUMBER);
        if (!(StringUtils.isEmpty(phonenumber))) {
            data.put("phonenumber", phonenumber.toString());
        } else {
            data.put("phonenumber", "");
        }
        boolean firstLogin = storeBusiness.getFirstLoginStatus() == FIRST_LOGIN ? true : false;
        data.put("firstLogin", firstLogin);
        return new JsonResponse().setSuccessful().setData(data);
    }

    /**
     * 增加门店头图
     *
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/addStoreBusinessStoreDisplayImages/auth")
    @ResponseBody
    public JsonResponse addStoreBusinessStoreDisplayImages(@RequestParam("storeDisplayImages") String storeDisplayImages, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }

        try {
            if (StringUtils.isEmpty(storeDisplayImages)) {
                storeDisplayImages = "";
            }
            logger.info("增加门店头图addStoreBusinessStoreDisplayImages，storeId：" + storeId + ",storeDisplayImages:" + storeDisplayImages);
            storeUserService.addStoreBusinessStoreDisplayImages(storeId, storeDisplayImages);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError("增加门店头图:" + e.getMessage());
        }
    }


//    private AtomicInteger count = new AtomicInteger(-99999);
//
//    @RequestMapping("qrImg")
//    public JsonResponse qrImg(Long storeId) {
//        try {
//            ArrayList<Runnable> tasks = new ArrayList<>(10);
//            for (int i = 0; i < 10; i++) {
//                storeId = count.incrementAndGet()+0L;
//                Long finalStoreId = storeId;
//                Runnable runnable = () -> {
//        System.out.println(" = = = = = = = = = = = = = = = = = = = = = = = = = = storeId("+finalStoreId+") = = = = = = = = = = = = = = = = = = = = = " );
//                    String url = "http://weixinonline.yujiejie.com/third/findAccessTokenByAppId";
//                    Map<String, Object> param = new HashMap<>(2);
//                    StoreWxa storeWxa;
//                    param.put("appId", "wx0436a7ecd9b6b9db");
//                    String token = HttpClientUtils.get(url, param);
//                    Map<String, String> paramMap = new HashMap<>();
//                    paramMap.put("scene", "storeId=" + finalStoreId + "&type=1");
//                    paramMap.put("page", "page/index/index");
//                    paramMap.put("width", "430");
//
//                    String fileName = System.getProperty("java.io.tmpdir") + "/pic/" +
//                            "wx0436a7ecd9b6b9db" + "_storeId_" + finalStoreId + "_type_1_page_index_index_.jpg";
//                    File file = HttpClientUtils.postInputStreamToFile(String.format(WXA_CODE_URL, token), JSONObject.toJSONString(paramMap),
//                            fileName);
//                    logger.info("fileName={},size={}", fileName, file.length());
//
//                    MultipartFile partFile = new JiuyMultipartFile(file);
//                    try {
//                        file.createNewFile();
//                        String path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, partFile);
//                        logger.info("生成二维码成功!!! path={}", path);
//                    } catch (IOException e) {
//                    }
//                };
//                tasks.add(runnable);
//            }
//
//
//            for (Runnable task : tasks) {
//                task.run();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return JsonResponse.successful();
//    }
}