package com.e_commerce.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.activity.dao.*;
import com.e_commerce.miscroservice.activity.entity.ActivityImageShare;
import com.e_commerce.miscroservice.activity.entity.WxaShare;
import com.e_commerce.miscroservice.activity.entity.WxaShareLog;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUser;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans;
import com.e_commerce.miscroservice.activity.rpc.impl.DistributionRpcService;
import com.e_commerce.miscroservice.activity.rpc.impl.ShopMemberRpcService;
import com.e_commerce.miscroservice.activity.rpc.impl.ShopProductRpcService;
import com.e_commerce.miscroservice.activity.service.ShareRelationService;
import com.e_commerce.miscroservice.activity.service.ShareService;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.activity.ShareTypeEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.FileUtils;
import com.e_commerce.miscroservice.commons.utils.OSSFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

import static com.e_commerce.miscroservice.commons.enums.activity.ShareTypeEnum.WWA_SHARE_TO_HOME;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 15:59
 * @Copyright 玖远网络
 */
@Service
public class ShareServiceImpl implements ShareService {


    private Log logger = Log.getInstance(ShareServiceImpl.class);


    @Autowired
    private ChannelUserGatherDao channelUserGatherDao;
    @Autowired
    private ShareRelationService shareRelationService;

    @Autowired
    private ChannelUserDao channelUserDao;
    @Autowired
    private WxaShareLogDao wxaShareLogDao;
    @Autowired
    private DataDictionaryDao dataDictionaryDao;
    @Autowired
    private ShopMemberRpcService shopMemberRpcService;
    @Autowired
    private ChannelUserFansDao channelUserFansDao;
    @Autowired
    private WxaShareDao wxaShareDao;
    @Autowired
    private DistributionRpcService distributionRpcService;
    @Autowired
    private ShopProductRpcService shopProductRpcService;
    @Autowired
    private OSSFileUtil ossFileUtil;
    @Resource
    private ActivityImageShareDao activityImageShareDao;

    /**
     * 分享商品
     *
     * @param shareUserId     分享者用户id
     * @param shareType       分享类型
     * @param productId       商品id
     * @param beSharedUserId  被分享者用户id
     * @param isEffectiveFans 是否有效粉丝(有效粉丝可以获取收益)
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/21 17:38
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doShareProduct(Long shareUserId, Integer shareType, Long productId, Long beSharedUserId, boolean isEffectiveFans) {

        logger.info("用户={}分享={},给用户={}商品={},isNewUser={}", shareUserId, shareType, beSharedUserId, productId, isEffectiveFans);
        //一个人一天不能反复分享给另一个人的次数(现在不需要了,必须是新用户才行)
//        long todayCount = wxaShareLogDao.today2OtherUserCount (shareUserId, productId, beSharedUserId);
//        if (todayCount > 0) {
//            return EmptyEnum.map ();
//        }


        //分享人获得金币
        //被分享人获得金币
        //每人每天分享
        DataDictionary config = dataDictionaryDao.findDictionaryByCodeAndGroupCode(DataDictionaryEnums.SHARE_PRODUCT_CONFIG);
        ErrorHelper.declareNull(config, "没有找到分享收益配置");
        String comment = config.getComment();
        logger.info("分享收益配置 config={}", comment);

        JSONObject configJson = JSONObject.parseObject(comment);
        //=========================== 查询收益 ===========================
        Integer typeSwitch = configJson.getInteger("typeSwitch");
        //做个兼容,默认现金生效
        typeSwitch = typeSwitch == null ? 2 : typeSwitch;

        BigDecimal sendUserGoldEarnings = BigDecimal.ZERO;
        BigDecimal sendUserCashEarnings = BigDecimal.ZERO;
        BigDecimal higherGoldEarnings = BigDecimal.ZERO;
        BigDecimal higherCashEarnings = BigDecimal.ZERO;
        switch (typeSwitch) {
            case 1:
                sendUserCashEarnings = configJson.getBigDecimal("sendUserCashEarnings");
                if (sendUserCashEarnings == null || sendUserCashEarnings.compareTo(BigDecimal.ZERO) < 0) {
                    ErrorHelper.declare(false, "收益配置错误");
                }

                higherCashEarnings = configJson.getBigDecimal("higherCashEarnings");
                higherCashEarnings = higherCashEarnings == null ? new BigDecimal("1") : higherCashEarnings;
                break;
            case 2:
                sendUserGoldEarnings = configJson.getBigDecimal("sendUserGoldCoinEarnings");
                if (sendUserGoldEarnings == null || sendUserGoldEarnings.compareTo(BigDecimal.ZERO) < 0) {
                    ErrorHelper.declare(false, "收益配置错误");
                }

                higherGoldEarnings = configJson.getBigDecimal("higherGoldEarnings");
                higherGoldEarnings = higherGoldEarnings == null ? new BigDecimal("0") : higherGoldEarnings;
                break;
            default:
                ErrorHelper.declare(false, "未知的收益类型");
        }
        //是否有收益
        boolean hasEarnings = sendUserCashEarnings.add(sendUserGoldEarnings).compareTo(BigDecimal.ZERO) > 0;

        Integer maxShareCount = configJson.getInteger("maxShareCount");
        ErrorHelper.declareNull(maxShareCount, "没有当日最大收益次数限制");

        //记录日志
        WxaShareLog wxaShareLog = doInsertShareProductLog(shareType, shareUserId, productId, beSharedUserId, isEffectiveFans ? 1 : 0);

        //先查今日分享次数
        Long count = wxaShareLogDao.todayhasEarningsShareCount(shareUserId);
        boolean isOutOfMax = count > maxShareCount;
        logger.info("userId={},今日已分享{}次,是否有超上限={},是否有收益={},是否有效粉丝={}", shareUserId, count, isOutOfMax, hasEarnings, isEffectiveFans);

        //是否达到今日收益上线
        if (!isOutOfMax && hasEarnings && isEffectiveFans) {
            //账户分享者添加收益
            ShopMemAcctCashOutInQuery addInfo = new ShopMemAcctCashOutInQuery();
            addInfo.setType(CashOutInTypeEnum.SHARE.getCode());
            addInfo.setFromId(wxaShareLog.getId());
            addInfo.setUserId(shareUserId);
            addInfo.setOperCash(sendUserCashEarnings);
            addInfo.setOperGoldCoin(sendUserGoldEarnings);
            Map<String, Object> res = distributionRpcService.addCashOutInByType(addInfo);
            logger.info("分享商品--账户添加收益", res);
            ErrorHelper.declareNull(res != null && res.get("cashOutInId") != null, "获取收益失败");

            //寻找上级(上级,必须是有效粉丝)
//            Long higherLiverId = shareRelationService.findShareIdBySharedId(shareUserId);
            Long higherLiverId = wxaShareDao.findEffectiveHigherUserId(shareUserId);
            if (higherLiverId != null) {
                if (higherCashEarnings.add(higherGoldEarnings).compareTo(BigDecimal.ZERO) > 0) {
                    WxaShareLog wxaHigherShareLog = doInsertShareProductLog(ShareTypeEnum.FANS_SHARE.getCode(), shareUserId, productId, beSharedUserId, 2);
                    //账户分享者上级添加收益
                    ShopMemAcctCashOutInQuery higherAddInfo = new ShopMemAcctCashOutInQuery();
                    higherAddInfo.setType(CashOutInTypeEnum.SHARE.getCode());
                    higherAddInfo.setFromId(wxaHigherShareLog.getId());
                    higherAddInfo.setUserId(higherLiverId);
                    higherAddInfo.setOperCash(higherCashEarnings);
                    higherAddInfo.setOperGoldCoin(higherGoldEarnings);
                    Map<String, Object> resHigher = distributionRpcService.addCashOutInByType(higherAddInfo);
                    logger.info("分享商品--账户添加收益", resHigher);
                    ErrorHelper.declareNull(resHigher != null && resHigher.get("cashOutInId") != null, "获取收益失败");
                } else {
                    logger.info("分享者上级没有收益");
                }
            } else {
                logger.info("没有上级shareUserId={}", shareUserId);
            }
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("isOutOfMax", isOutOfMax);
        result.put("maxCountOfDay", maxShareCount);
        result.put("earningsGoldCoin", isOutOfMax ? 0 : sendUserGoldEarnings);
        result.put("earningsCash", isOutOfMax ? 0 : sendUserCashEarnings);
        result.put("typeSwitch", typeSwitch);
        return result;
    }


    /**
     * 生成一条分享日志
     *
     * @param shareType      分享类型
     * @param userId         分享者id
     * @param productId      商品id
     * @param beSharedUserId 被分享者
     * @param earningsType   收益了类型
     * @return com.e_commerce.miscroservice.activity.entity.WxaShareLog
     * @author Charlie
     * @date 2018/12/27 10:09
     */
    private WxaShareLog doInsertShareProductLog(Integer shareType, Long userId, Long productId, Long beSharedUserId, Integer earningsType) {
        //=========================== 生成日志(以后业务统计) ===========================
        WxaShareLog wxaShareLog = new WxaShareLog();
        wxaShareLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        wxaShareLog.setMemberId(userId);
        wxaShareLog.setShareType(shareType);
        wxaShareLog.setTargetId(productId);
        wxaShareLog.setBeSharedMemberId(beSharedUserId);
        wxaShareLog.setEarningsType(earningsType);
        //分享描述
        String description;
        if (ShareTypeEnum.PRODUCT.isThis(shareType)) {
            ShopProductQuery shopProductQuery = new ShopProductQuery();
            shopProductQuery.setId(productId);
            ShopProductQuery product = shopProductRpcService.selectOne(shopProductQuery);
            logger.info("查询商品 product=", product);
            ErrorHelper.declareNull(product, "没有商品信息");
            String clothesNumber = product.getClothesNumber();
            description = "分享商品：" + (clothesNumber == null ? EmptyEnum.string : clothesNumber) + " " + product.getName();
        } else if (ShareTypeEnum.WXA.isThis(shareType)) {
            description = "分享小程序";
        } else if (ShareTypeEnum.FANS_SHARE.isThis(shareType)) {
            description = "下级粉丝分享";
        } else {
            description = "其他";
        }

        wxaShareLog.setDescription(description);
        int rec = wxaShareLogDao.insert(wxaShareLog);
        ErrorHelper.declare(rec == 1, "记录分享失败");
        return wxaShareLog;
    }


    /**
     * 确认分享关系
     *
     * @param wxaShare       wxaShare
     * @param currentUserSex 当前登录用户性别
     * @author Aison
     * @date 2018/7/6 18:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shareFriend(WxaShare wxaShare, Integer currentUserSex) {
        logger.info("建立邀请关系 currentUserSex={}", currentUserSex);
        // 验证参数
        Integer shareType = wxaShare.getShareType();
        if (BeanKit.hasNull(wxaShare.getSourceUser(), wxaShare.getTargetUser(), shareType)) {
            throw ErrorHelper.me("请求参数不可为空");
        }
        wxaShare.setCreateTime(new Date());

        //被邀请者,也是当前登录的主体
        Long currUserId = wxaShare.getTargetUser();
        //邀请者
        Long sourceId = wxaShare.getSourceUser();
        // 自己不能分享给自己
        if (currUserId.equals(sourceId)) {
            logger.warn("自己分享给自己");
            return false;
        }

        // 将邀请者和被邀请者顺序换下来查询看是否能够查询到数据
        // 为了剔除掉 相互邀请的问题
        boolean isFriend = wxaShareDao.isFriend(sourceId, currUserId);
        ShopMemberQuery userQuery = new ShopMemberQuery();
        userQuery.setId(currUserId);
        ShopMemberVo user = shopMemberRpcService.findOne(userQuery);
        logger.info("被邀请者={}", user);
        //  targetId 被邀请者 sourceId 邀请者
        WxaShare share = wxaShareDao.findFriendBySourceIdUserId(sourceId, currUserId);
//        logger.info("邀请信息",share);
//        if (isFriend&&share!=null&&share.getWxNickname()!=null) {
//            logger.warn("已经是邀请");
//            return false;
//        }
        if (user == null) {
            throw new RuntimeException("没有当前用户信息");
        }

        userQuery.setId(sourceId);
        ShopMemberVo sourceUser = shopMemberRpcService.findOne(userQuery);
        logger.info("被邀请者={}", sourceUser);
        if (sourceUser == null) {
            throw new RuntimeException("没有邀请者用户信息");
        }
        if (! sourceUser.getStoreId().equals(user.getStoreId())) {
            throw new RuntimeException("不是一家店铺");
        }

        //todo:XX
//        if (user!=null&&user.getUserNickname()==null){
//            logger.warn("未授权={}", user);
//            return false;
//        }
        if (isFriend && user != null && user.getUserNickname() != null) {
            logger.warn("已经是邀请");
            return false;
        }
        //判断是否已经跟其他用户确认了关系
        boolean hasFriend = wxaShareDao.hasFriend(currUserId);
        if (hasFriend && user != null && user.getUserNickname() != null) {
            logger.warn("已经被其他用户邀请");
            return false;
        }

        // 查询被邀请者的用户来源(性别前端传,两个系统可能没同时同步)
        if (!currentUserSex.equals(0)) {
            user.setSex(currentUserSex);
        }
        // 如果不是邀请用户则返回
        if (!"1".equals(user.getSource())) {
            logger.warn("用户来源非分享");
            return false;
        }

        //用户是否是有效粉丝
        boolean isEffectiveFans = isEffectiveFans(user);

        logger.info("建立邀请关系 sourceUserId={},currentUserId={}, 是否是有效粉丝={}", sourceId, currUserId, isEffectiveFans);
        wxaShare.setCreateTime(new Timestamp(System.currentTimeMillis()));
        wxaShare.setFansType(isEffectiveFans ? 1 : 0);
        int rec = 0;
        if (share != null) {
            wxaShare.setId(share.getId());
            logger.info("更新用户关系={}", wxaShare);
            rec = wxaShareDao.updateWxaShare(wxaShare);
        } else {
            //sql
            logger.info("保存用户关系={}", wxaShare);
            rec = wxaShareDao.save(wxaShare);
        }

        if (rec != 1) {
            logger.warn("建立邀请关系 保存失败");
            return false;
        }

        if (isEffectiveFans) {
            //有效粉丝,被邀请收益,并绑定分销关系
            ShopMemAcctCashOutInQuery addInfo = new ShopMemAcctCashOutInQuery();
            addInfo.setType(CashOutInTypeEnum.NEW_USER_INVITEE.getCode());
            addInfo.setUserId(currUserId);
            addInfo.setFromId(sourceId);
            Map<String, Object> res = distributionRpcService.addCashOutInByType(addInfo);
            logger.info("绑定粉丝关系 res={}", res);
            ErrorHelper.declare(res != null && Boolean.parseBoolean(res.get("isOk").toString()), "增加收益失败");
        } else {
            //普通粉丝,仅仅绑定分销关系
            distributionRpcService.bindingFans(sourceId, currUserId);
        }

        //返回值,是否是有效粉丝, 有效粉丝才能获得收益
        return isEffectiveFans;
    }


    @Override
    public Long myEffectiveFans(Long shopMemberId) {
        if (shopMemberId == null) {
            return 0L;
        }

        return MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(WxaShare.class)
                        .eq(WxaShare::getFansType, 1)
                        .eq(WxaShare::getSourceUser, shopMemberId)
        );
    }


    /**
     * 渠道商分享
     *
     * @param channelUserId 渠道商用户id
     * @param currUserId    当前小程序用户id
     * @author Charlie
     * @date 2018/12/24 11:38
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shareFromChannel(Long channelUserId, Long currUserId) {
        //渠道商分享,也要往分项表插入记录,避免渠道商分享绑定关系,分销系统还可重复绑定
        logger.info("渠道商分享 邀请者={},被邀请者={}", channelUserId, currUserId);

        ChannelUser channel = channelUserDao.findNormalById(channelUserId);
        ErrorHelper.declareNull(channel, "未找到渠道商信息");
        if (channel.getPartnerStatus().equals(0)) {
            logger.info("合作终止");
            return;
        }


        //判断是否已经跟其他用户确认了关系
        boolean hasFriend = wxaShareDao.hasFriend(currUserId);
        if (hasFriend) {
            logger.info("用户已经被其他用户邀请");
            return;
        }


        // 查询被邀请者的用户来源(性别前端传,两个系统可能没同时同步)
        ShopMemberQuery userQuery = new ShopMemberQuery();
        userQuery.setId(currUserId);
        ShopMemberVo user = shopMemberRpcService.findOne(userQuery);
        // 如果不是邀请用户则返回
        if (!"1".equals(user.getSource())) {
            logger.info("用户来源非分享");
            return;
        }


        logger.info("更新渠道商粉丝数+1");
        channelUserGatherDao.appendByChannelUserId(channelUserId, 1, 0, 0, false);

        logger.info("保存分享关系");
        WxaShare wxaShare = new WxaShare();
        wxaShare.setChannelUserId(channelUserId);
        wxaShare.setTargetUser(currUserId);
        wxaShare.setFansType(0);
        wxaShare.setShareType(5);
        int rec = wxaShareDao.safeSave(wxaShare);
        ErrorHelper.declare(rec == 1, "邀请失败");

        ChannelUserFans historyFans = channelUserFansDao.findNormalByShopMemberId(currUserId);
        ErrorHelper.declare(historyFans == null, "粉丝已存在");

        logger.info("绑定关系");
        ChannelUserFans newFans = new ChannelUserFans();
        newFans.setChannelUserId(channelUserId);
        newFans.setShopMemberId(currUserId);
        int save = channelUserFansDao.save(newFans);
        ErrorHelper.declare(save == 1, "绑定粉丝关系失败");


    }
    /**
     * 生成保存合成图的临时目录,用完之后记得删除
     *
     * @return java.io.File
     * @author Charlie
     * @date 2018/7/17 12:57
     */
    private  File createTempDirectory() {
        String directoryName = UUID.randomUUID().toString();
//        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
//        ServletContext servletContext = webApplicationContext.getServletContext();
//        String filePath = servletContext.getRealPath("/"+directoryName+"/");
//        logger.info("获取小程序商品分享图片filePath:"+filePath);
        String filePath = System.getProperty("java.io.tmpdir") + "/pic/" + directoryName+ "/";

        File file =new File(filePath);
        //如果文件夹不存在则创建
        if (!file .exists() && !file .isDirectory())
        {
            file .mkdir();
        }
        return new File (filePath);
    }
    @Override
    public String getImage(Integer type, Integer shareType) {
        logger.info("分享图片生成 type={},shareType={}",type,shareType);

        ActivityImageShare pics = casePic(type,shareType);
        if (pics==null){
            throw new RuntimeException("分享类型错误：" + shareType);
        }


        //todo 二维码活动类型 -- 活动存放地址
        //展示的图片
        String showImg = pics.getMainMap();
//        String showImg = "http://img.zcool.cn/community/014565554b3814000001bf7232251d.jpg@1280w_1l_2o_100sh.png";
        //商品二维码
        String wxRQcode = pics.getWxImg();
//        String wxRQcode = "http://img.zcool.cn/community/01f9ea56e282836ac72531cbe0233b.jpg@2o.jpg";
        //合成图
//        String finalImg = "http://txt22263.book118.com/2017/0517/book107288/107287291.jpg";
        String finalImg = null;


        //临时目录
        File directory = createTempDirectory();
        try {
            //生成合成图到目录中
            String filePath = createCompositeImg(showImg, wxRQcode, directory);
            File file = new File(filePath);
            //上传图片到阿里云
            String imgUrl = saveOrUpdImgOnAliyun(finalImg, file);
            if (StringUtils.isEmpty(imgUrl)) {
                logger.error("小程序分享拼接图片, 上传到阿里云失败 imgUrl:'', shareType:" + shareType);
                throw new RuntimeException("小程序分享拼接图片, 上传到阿里云失败 imgUrl:'', shopProductId:" + shareType);
            }
            //将图片信息保存到商品中
            return imgUrl;
        } finally {
            FileUtils.deleteTempFile(directory);
            logger.info("成功删除临时目录 path:" + directory);
        }
    }

    public ActivityImageShare casePic(Integer type, Integer shareType){
        ActivityImageShare activityImageShare = null;

        if (shareType.equals(WWA_SHARE_TO_HOME.getCode())){
            //默认小程序分享至首页活动
            activityImageShare = activityImageShareDao.findActivityImageShareByType(type,shareType);
        }
        return activityImageShare;
    }
    /**
     * 创建保存合成图片到本地
     *
     * @param destDirectory 文件保存的目录
     * @return java.lang.String 合成后的图片的url
     * @author Charlie
     * @date 2018/7/16 20:05
     */
    public String createCompositeImg(String showImg, String rwxRQcode, File destDirectory) {
        //图片资源
        BufferedImage showImgBuf;
        BufferedImage wxRQcodeBuf;
        InputStream showImgIn = null, wxRQcode = null;
        try {
            showImgIn = FileUtils.getInputStreamByGet(showImg);
            wxRQcode = FileUtils.getInputStreamByGet(rwxRQcode);
            //橱窗图

            try {
                showImgBuf = FileUtils.imgFile2BufferedImg(showImgIn);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("生成分享图片将橱窗图读取为缓存失败 showImg={},rwxRQcode={}:", showImg, rwxRQcode);
                throw new RuntimeException("将橱窗图读取为缓存失败");
            }
            //二维码图
            try {
                wxRQcodeBuf = FileUtils.imgFile2BufferedImg(wxRQcode);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("生成分享图片将二维码读取为缓存失败 rwxRQcode={}", rwxRQcode);
                throw new RuntimeException("将二维码读取为缓存失败");
            }
        } finally {
            FileUtils.closeStream(showImgIn, wxRQcode);
        }

        //统一大小
        int w = 750;
        int h = new BigDecimal(w + "").divide(new BigDecimal(String.valueOf(showImgBuf.getWidth())), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(String.valueOf(showImgBuf.getHeight()))).intValue();
        showImgBuf = FileUtils.zoomImage(showImgBuf, w, h);

        //缩放二维码
        int rqMaxWith = showImgBuf.getWidth() / 10 * 3;
        wxRQcodeBuf = FileUtils.zoomImage(wxRQcodeBuf, rqMaxWith, rqMaxWith);
        //生成商品信息图
        //商品信息图片左边贴上二维码的空隙
        int xLeftSpace = 50;
        //二维码占用的左边的空间
        int unableWith = wxRQcodeBuf.getWidth() + xLeftSpace * 2;
        BufferedImage imgTemp = FileUtils.generateProductShareDescriptionImg(showImgBuf.getWidth(), w / 2, unableWith);
        //合并二维码
        int x = imgTemp.getWidth() - unableWith + xLeftSpace;
        int y = 50;
        imgTemp = FileUtils.overlapImage(imgTemp, wxRQcodeBuf, x, y);
        //合并橱窗图
        BufferedImage finalImage = FileUtils.mergeImage(false, showImgBuf, imgTemp);

        //输入图片内容,并保存到本地
        String localServerImgFile = destDirectory.getAbsolutePath() + "/" + System.currentTimeMillis() + hashCode() + ".jpg";
        logger.info("开始将合成图片暂时保存到本地... localServerImgFile = " + localServerImgFile);
        try {
            if (!FileUtils.bufferedImg2ImgFile(finalImage, localServerImgFile)) {
                logger.error("保存合成图片到本地失败");
                throw new RuntimeException("保存合成图片失败");
            }
            logger.info("成功将合成图片暂时保存到本地, localServerImgFile = " + localServerImgFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存合成图片到本地失败");
            throw new RuntimeException("保存合成图片失败");

        }
        return localServerImgFile;
    }

    /**
     * 上传图片到阿里云
     *
     * @param newImgFile 新合成的图片
     * @author Charlie
     * @date 2018/7/17 11:25
     */
    public String saveOrUpdImgOnAliyun(String historyPath, File newImgFile) {
        String path = "";
        try {
            //上传新的
            if (newImgFile.exists()&&newImgFile.length()==0) {
                logger.error("上传分享商品合成图, 文件对象为空，Filename:" + newImgFile.getName());
                return path;
            }
            path = ossFileUtil.uploadFile("yjj-img-www", newImgFile);

            //删除原来的
            if (StringUtils.isNotBlank(historyPath)) {
                historyPath = historyPath.trim();
                String[] pathElems = historyPath.split("/");
                String key = pathElems[pathElems.length - 1];
                ossFileUtil.removeFile("yjj-img-www", key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("上传分享商品合成图到阿里云 失败，Filename:" + newImgFile.getName());
        }

        logger.info("上传分享商品合成图到阿里云 success");
        return path;
    }

    /**
     * 是否是有效粉丝
     *
     * @param user user
     * @return boolean
     * @author Charlie
     * @date 2018/12/14 13:39
     */
    private boolean isEffectiveFans(ShopMemberVo user) {
        DataDictionary dataConfig = dataDictionaryDao.findDictionaryByCodeAndGroupCode(DataDictionaryEnums.SHARE_PRODUCT_CONFIG);
        String config = dataConfig.getComment();
        logger.info("是有是有效粉丝配置 config={}, 用户sex={}", config, user.getSex());
        JSONObject configJson = JSONObject.parseObject(config);
        //'0全部,1:男,2:女
        Integer limitSex = configJson.getInteger("fansLimitSex");
        //默认女
        limitSex = limitSex == null ? 2 : limitSex;

        if (limitSex == 0) {
            //不做性别限制
            return Boolean.TRUE;
        }

        return limitSex.equals(user.getSex());
    }


}
