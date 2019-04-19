package com.e_commerce.miscroservice.publicaccount.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReferee;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyOrderRpcService;
import com.e_commerce.miscroservice.commons.utils.*;
import com.e_commerce.miscroservice.commons.utils.qr.ImgQrUtil;
import com.e_commerce.miscroservice.commons.utils.qr.QrImage;
import com.e_commerce.miscroservice.publicaccount.dao.ProxyCustomerAuditDao;
import com.e_commerce.miscroservice.publicaccount.dao.ProxyCustomerDao;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerAuditStatus;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerType;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyCustomerService;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyRefereeService;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:24
 * @Copyright 玖远网络
 */
@Service
public class ProxyCustomerServiceImpl implements ProxyCustomerService{

    private static final String PUBLIC_PROXY_SHARE_QC_IMG = "publicProxyShareQcImg";
    private Log logger = Log.getInstance (ProxyCustomerServiceImpl.class);

    /**
     * 删除状态:正常
     */
    private static final int NORMAL = 0;

    @Autowired
    private WebApplicationContext webApplicationConnect;
    @Autowired
    private OssKit ossKit;
    @Autowired
    private ProxyCustomerAuditDao proxyCustomerAuditDao;
    @Autowired
    private ProxyCustomerDao proxyCustomerDao;
    @Resource
    private ProxyRefereeService proxyRefereeService;
    @Resource
    private PublicAccountUserService publicAccountUserService;
    @Autowired
    private ProxyCustomerService proxyCustomerService;
    @Autowired
    ProxyOrderRpcService proxyOrderRpcService;


    @Value ("#{'${oss.upload.bucket.proxyShareQrcode}'.split(',')}")
    private List<String> proxyShareQrcodeList;

    /**
     * 注册代理商
     * <p>不做事务<p/>
     *
     * @param authCode  手机验证码
     * @param response  response
     * @param openToken
     * @param openId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/9/20 13:51
     */
    @Override
    public ProxyCustomerAudit doRegister(ProxyCustomerAudit audit, String authCode, HttpServletResponse response, String openToken, String openId) {
        String phone = audit.getPhone ();
        logger.info ("注册代理openId={},phone={}", openId, phone);
        //查询用户信息
        ErrorHelper.declare (BeanKit.notNull (phone), 530, "请填写手机号");
        ErrorHelper.declare (BeanKit.notNull (authCode), 530, "请填输入验证码");

        //当前手机号对应的账号
        PublicAccountUser user;

        //注册过的用户, 不会绑定关系
        boolean isNewUser = publicAccountUserService.findByPhone (phone) == null;
        if (! isNewUser) {
            audit.setRefereeUserId (null);
        }

        PublicAccountUser loginUser = publicAccountUserService.isLogin (openId, openToken);
        boolean hasLogin = loginUser != null;
        if (hasLogin) {
            //登录中,申请代理的手机号必须与当前账号手机号一致, 手机号也不用校验
            String currentPhone = loginUser.getPhone ();
            logger.info ("申请手机号={}, 当前账号手机号={}", phone, currentPhone);
            ErrorHelper.declare (ObjectUtils.nullSafeEquals (currentPhone, phone), "注册手机号与当前账号必须一致");
            user = loginUser;
        }
        else {
            //没登录,则隐式登录,并且设为主体账号
            PublicAccountUser addInfo = new PublicAccountUser ();
            addInfo.setPhone (phone);
            //这里有事务处理
            user = publicAccountUserService.loginUserByPhoneIfNullCreate (addInfo, authCode, response, openToken, openId, true);
            logger.info ("申请注册代理--创建一个新的用户 user:{}", user);
            ErrorHelper.declareNull (user, "申请注册代理失败");
        }


        //创建代理
        audit.setRecommonUserId (user.getId ());
        return proxyCustomerService.addProxyCustomerAudit (audit);
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ProxyCustomerAudit getAuditStatus(long userId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomerAudit (),
                new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                        .eq (ProxyCustomerAudit::getRecommonUserId, userId)
                        .orderBy (MybatisSqlWhereBuild.ORDER.DESC, ProxyCustomerAudit::getCreateTime)
        );
    }


    /**
     * 审核注册代理
     *
     * @param auditId    auditId
     * @param msg        审核意见
     * @param operUserId 审核人
     * @param isPass     是否通过
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.customer.ProxyCustomerAudit
     * @author Charlie
     * @date 2018/9/22 13:20
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void doAudit(Long auditId, String msg, Long operUserId, Boolean isPass) {
        logger.info ("审核注册代理 isPass:{},auditId:{},operUserId:{},msg:{}}", isPass, auditId, operUserId, msg);
        ProxyCustomerAudit audit = proxyCustomerAuditDao.selectById (auditId);
        ErrorHelper.declareNull (audit, "未找到审核记录");
        ErrorHelper.declare (ProxyCustomerAuditStatus.PROCESSING.isThis (audit.getAuditStatus ()), "审核状态错误");

        if (isPass) {
            verifyPhoneCanRegisterProxyCustomer (audit);
            //注册市级代理
            createProxyCustomer (audit);
        }

        //修改审核状态
        int rec = proxyCustomerAuditDao.audit (isPass ? ProxyCustomerAuditStatus.SUCCESS : ProxyCustomerAuditStatus.FAILED, auditId, msg, operUserId);
        logger.info ("审核注册代理 auditId:{},rec:{}", auditId, rec);
        ErrorHelper.declare (rec == 1, "修改审核状态失败");
    }


    /**
     * 删除一条审核记录
     *
     * @param auditId    审核id
     * @param operUserId 操作人员id
     * @author Charlie
     * @date 2018/9/25 10:57
     */
    @Override
    public Response delete(Long auditId, Long operUserId) {
        try {
            //ErrorHelper.declareNull (auditId, "未找到审核信息");
            ProxyCustomerAudit audit = MybatisOperaterUtil.getInstance ().findOne (
                    new ProxyCustomerAudit (),
                    new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                            .eq (ProxyCustomerAudit::getId, auditId)
            );

            ErrorHelper.declareNull (audit, "未找到审核信息");
            if (ObjectUtils.nullSafeEquals (audit.getDelStatus (), StateEnum.DELETE)) {
                logger.info ("删除审核记录,已删除不需要再操作 auditId:{}", audit);
                return Response.success ();
            }

//            ErrorHelper.declare (ProxyCustomerAuditStatus.PROCESSING.isThis (audit.getAuditStatus ()),
//                    "审核中的记录不允许删除"
//            );

            ProxyCustomerAudit updInfo = new ProxyCustomerAudit ();
            updInfo.setId (auditId);
            updInfo.setDelStatus (StateEnum.DELETE);
            updInfo.setUpdateUserId (operUserId);
            int rec = MybatisOperaterUtil.getInstance ().update (
                    updInfo,
                    new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                            .eq (ProxyCustomerAudit::getId, auditId)
            );
            logger.info ("删除审核记录 auditId:{},operUserId:{},rec:{}", auditId, operUserId, rec);
            return Response.success ();
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 查询代理
     *
     * @param proxyCustomerId recommonUserId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/25 15:39
     */
    @Override
    public ProxyCustomer selectByUserId(Long proxyCustomerId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomer (),
                new MybatisSqlWhereBuild (ProxyCustomer.class)
                        .eq (ProxyCustomer::getUserId, proxyCustomerId)
                        .eq (ProxyCustomer::getDelStatus, StateEnum.NORMAL)
        );
    }

    /**
     * 描述 1 市代理  0 客户 2 县代理
     *
     * @param proxyCustomerId
     * @return int
     * @author hyq
     * @date 2018/10/10 10:39
     */
    @Override
    public int judeCustomerRole(Long proxyCustomerId) {

        ProxyCustomer proxyCustomer = selectByUserId (proxyCustomerId);

        if (proxyCustomer != null) {
            return proxyCustomer.getType ();
        }
        return 0;
    }



    /**
     * 生成海报
     *
     * @param type   1邀请登录,2邀请注册代理商
     * @param userId 用户id
     * @param webUrl 域名
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/9/27 18:46
     */
    @Override
    public String posterQrcode(Integer type, Long userId, String webUrl) {
        String qrCodeUrl = null;
        int version = 383;
        logger.info ("生成海报 type:{},userId:{}", type, userId);
        ProxyCustomer proxyCustomer = proxyCustomerDao.selectByUserId (userId);
        ErrorHelper.declareNull (proxyCustomer, "没有生成海报的权限");
        //邀请登录
        File file = null;
        try {
            if (type == 1) {
                //直接获取
                qrCodeUrl = proxyCustomer.getCustomerQrCode ();
                if (StringUtils.isNotBlank (qrCodeUrl)) {
                    logger.info ("文件已存在");
                    return qrCodeUrl;
                }
                String realPath = webApplicationConnect.getServletContext ().getRealPath (PUBLIC_PROXY_SHARE_QC_IMG);
//            String img = deleteHistoryImg (qrCodeUrl, realPath);
                FileUtils.createDir (realPath);
                String img = realPath + File.separator + System.currentTimeMillis () + ".jpg";
                InputStream yjjLog = FileUtils.yjjLog ();
                ErrorHelper.declareNull (yjjLog, "没找到log地址");
                //重新生成,存入表中,返回项目域名下文件名URL
                QrImage para = new QrImage.Builder ()
                        .setFileOutputPath (img)
                        .setQrContent ("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx176676190b6d64ae&" +
                                "redirect_uri=https%3a%2f%2flocal.yujiejie.com%2fproxy%2f%23%2flogin%3frefereeUserId%3d" + userId +
                                "&response_type=code&scope=snsapi_base&state=123#wechat_redirect ")
                        .setQrHeight (300)
                        .setQrWidth (300)
                        .setQrIconFileStream (yjjLog)
                        .setTopWrodHeight (0)
                        .setWordContent ("")
                        .setWordSize (8).build ();
                file = ImgQrUtil.createQrWithFontsAbove (para);
                ErrorHelper.declareNull (file.getAbsolutePath (), 530, "生成二维码失败");
                qrCodeUrl = ossKit.simpleUpload (file.getAbsolutePath (), proxyShareQrcodeList, generateFileNameOnOss (proxyCustomer.getPhone (), version, "inviteLogin"));
                logger.info ("阿里云图片地址 ossImgUrl={}", qrCodeUrl);
                //同步数据库
                ProxyCustomer updateInfo = new ProxyCustomer ();
                updateInfo.setCustomerQrCode (qrCodeUrl);
                int rec = MybatisOperaterUtil.getInstance ().update (
                        updateInfo,
                        new MybatisSqlWhereBuild (ProxyCustomer.class)
                                .eq (ProxyCustomer::getId, proxyCustomer.getId ())
                );
                logger.info ("更新用户二维码 userId:{},rec:{}", userId, rec);
            }
            //邀请代理商

            if (type == 2) {
                ErrorHelper.declare (proxyCustomer.getType () == 1, "没有生成海报权限");
                qrCodeUrl = proxyCustomer.getProxyQrCode ();
                if (StringUtils.isNotBlank (qrCodeUrl)) {
                    logger.info ("文件已存在");
                    return qrCodeUrl;
                }
                String realPath = webApplicationConnect.getServletContext ().getRealPath (PUBLIC_PROXY_SHARE_QC_IMG);
                //            String img = deleteHistoryImg (qrCodeUrl, realPath);
                FileUtils.createDir (realPath);
                String img = realPath + File.separator + System.currentTimeMillis () + ".jpg";
                InputStream yjjLog = FileUtils.yjjLog ();
                ErrorHelper.declareNull (yjjLog, "没找到log地址");
                //重新生成,存入表中,返回项目域名下文件名URL
                QrImage para = new QrImage.Builder ()
                        .setFileOutputPath (img)
                        .setQrContent ("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx176676190b6d64ae&" +
                                "redirect_uri=https%3a%2f%2flocal.yujiejie.com%2fproxy%2f%23%2fregister%3frefereeUserId%3d" + userId +
                                "&response_type=code&scope=snsapi_base&state=123#wechat_redirect")
                        .setQrHeight (300)
                        .setQrWidth (300)
                        .setQrIconFileStream (yjjLog)
                        .setTopWrodHeight (0)
                        .setWordContent ("")
                        .setWordSize (8).build ();

                file = ImgQrUtil.createQrWithFontsAbove (para);
                ErrorHelper.declareNull (file.getAbsolutePath (), 530, "生成二维码失败");
                qrCodeUrl = ossKit.simpleUpload (file.getAbsolutePath (), proxyShareQrcodeList, generateFileNameOnOss (proxyCustomer.getPhone (), version, "inviteProxy"));
                logger.info ("阿里云图片地址 ossImgUrl={}", qrCodeUrl);
                //同步数据库
                ProxyCustomer updateInfo = new ProxyCustomer ();
                updateInfo.setProxyQrCode (qrCodeUrl);
                int rec = MybatisOperaterUtil.getInstance ().update (
                        updateInfo,
                        new MybatisSqlWhereBuild (ProxyCustomer.class)
                                .eq (ProxyCustomer::getId, proxyCustomer.getId ())
                );
                logger.info ("更新用户二维码 userId:{},rec:{}", userId, rec);
                file.delete ();
            }
        } finally {
            if (file != null) {
                file.delete ();
            }
        }

        return qrCodeUrl;
    }

    /**
     * 存放在阿里云上的文件名
     *
     * @param phone   phone
     * @param version 版本号,做个标识,以后好维护
     * @param prex    前缀
     * @return java.lang.String
     * @author Charlie
     * @date 2018/11/5 11:57
     */
    private String generateFileNameOnOss(String phone, int version, String prex) {
        return prex + version + phone + ".jpg";
    }


    /**
     * 删除历史文件,返回历史文件名,如果没有就生成新的文件名
     *
     * @param qrCodeUrl qrCodeUrl
     * @param realPath  realPath
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/27 23:50
     */
//    private String deleteHistoryImg(String qrCodeUrl, String realPath) {
//        FileUtils.createDir (realPath);
//        String img;
//        if (StringUtils.isNotBlank (qrCodeUrl)) {
//            //删除本地后更新
//            String[] paths = qrCodeUrl.split ("/");
//            File history = new File (realPath + File.separator + paths[paths.length - 1]);
//            img = history.getAbsolutePath ();
//            if (history.exists () && history.isFile ()) {
//                history.delete ();
//                logger.info ("删除历史文件");
//            }
//            else {
//                logger.warn ("删除历史二维码文件,文件不存在");
//            }
//        }
//        else {
//            img = randomFileName (realPath);
//        }
//        return img;
//    }

    /**
     * 随机文件名
     *
     * @param realPath 文件路径名
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/27 23:18
     */
    private String randomFileName(String realPath) {
        String time = new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ());
        return new StringBuilder ().append (realPath).append (File.separator).append (time).append ((int) (new Random ().nextDouble () * (99999 - 10000 + 1))).append (10000).append (".jpg").toString ();
    }


    /**
     * 申请代理
     *
     * @param param param
     * @return com.e_commerce.miscroservice.user.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/20 14:33
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public ProxyCustomerAudit addProxyCustomerAudit(ProxyCustomerAudit param) {
        ErrorHelper.declare (
                ! BeanKit.hasNull (BeanKit.hasNull (param.getType (), param.getAddressDetail (), param.getCounty (),
                        param.getCity (), param.getIdCardNo (), param.getPhone (), param.getProvince (),
                        param.getRefereeUserId ())),
                "请求参数不可为空");

        //用户审核中的记录
        ProxyCustomerAudit auditHis = MybatisOperaterUtil.getInstance ()
                .findOne (
                        new ProxyCustomerAudit (),
                        new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                                .eq (ProxyCustomerAudit::getPhone, param.getPhone ())
                                .eq (ProxyCustomerAudit::getDelStatus, NORMAL)
                                .eq (ProxyCustomerAudit::getAuditStatus, ProxyCustomerAuditStatus.PROCESSING.getCode ())
                );
        if (BeanKit.notNull (auditHis)) {
            logger.info ("申请代理---已有申请在审核中 auditId:{},phone:{}", auditHis.getId (), auditHis.getPhone ());
            throw ErrorHelper.me (530, "审核中，无法再次申请");
        }

        //校驗手机号是否可以注册代理
        verifyPhoneCanRegisterProxyCustomer (param);

        //注册
        ProxyCustomerAudit nowAudit = ProxyCustomerAuditDao.build (param);
        if (ProxyCustomerType.COUNTY.isThis (nowAudit.getType ())) {
            //注册县级代理,不需要审核,创建代理账号
            createProxyCustomer (nowAudit);
            //返回前端
            nowAudit.setAuditStatus (ProxyCustomerAuditStatus.SUCCESS.getCode ());
        }
        else if (ProxyCustomerType.CITY.isThis (nowAudit.getType ())) {
            //注册市级代理,需要审核通过
            int rec = MybatisOperaterUtil.getInstance ().save (nowAudit);
            logger.info ("申请注册市级代理 rec:{}", rec);
            ErrorHelper.declare (rec == 1, "创建代理失败");
            //返回前端
            nowAudit.setAuditStatus (ProxyCustomerAuditStatus.PROCESSING.getCode ());
        }
        else {
            throw ErrorHelper.me ("未知的注册类型");
        }
        return nowAudit;
    }


    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    @Override
    public List<ProxyCustomerAuditQuery> auditList(ProxyCustomerAuditQuery query) {
        return proxyCustomerAuditDao.auditList (query);
    }

    @Override
    public List<ProxyCustomerQuery> customerList(ProxyCustomerQuery query) {

        List<ProxyCustomer> proxyCustomers = proxyCustomerDao.customerList (query);

        List<ProxyCustomerQuery> proxyCustomerQueryList = new ArrayList<> ();

        proxyCustomers.stream ().forEach (action -> {

            ProxyCustomerQuery proxyCustomerQuery = new ProxyCustomerQuery ();

            try {
                BeanUtils.copyProperties (proxyCustomerQuery, action);
            } catch (Exception e) {
                e.printStackTrace ();
            }

            Long userId = action.getUserId ();

            PublicAccountUser byUserId = publicAccountUserService.findByUserId (userId);

            if (byUserId == null || byUserId.getDelStatus ().intValue () == 1) {
                proxyCustomerQuery.setDelStatus (1);
            }
            else {
                proxyCustomerQuery.setDelStatus (0);
            }

            long l = proxyRefereeService.allRefereeCount (1, userId);
            long i = proxyRefereeService.allRefereeCount (2, userId);

            //客户数
            long j = l + i;
            proxyCustomerQuery.setCustomerNum (j + "");
            proxyCustomerQuery.setAgentCustomerNum (i + "");

            //成单数
            int COrderNum = proxyRefereeService.orderNumByType (userId, 1);
            int AOrderNum = proxyRefereeService.orderNumByType (userId, 2);
            int ca = COrderNum + AOrderNum;
            proxyCustomerQuery.setCustomerOrderNum (ca + "");
            proxyCustomerQuery.setAgentCustomerOrderNum (AOrderNum + "");

            Map map = proxyRefereeService.orderMoneyBySelf (userId);
            Map map2 = proxyRefereeService.orderMoneyByType (userId, 2);
            //销售额 本人
            proxyCustomerQuery.setCustomerMoney (map.get ("orderMoney").toString ());
            proxyCustomerQuery.setCustomerMoneyReward (map.get ("rewardMoney").toString ());
            //销售额 代理
            proxyCustomerQuery.setAgentCustomerMoney (map2.get ("orderMoney").toString ());
            proxyCustomerQuery.setAgentCustomerMoneyReward (map2.get ("rewardMoney").toString ());

            proxyCustomerQuery.setNoSendMoney (proxyOrderRpcService.collectReward (userId, 0));
            proxyCustomerQuery.setAlreadyMoney (proxyOrderRpcService.collectReward (userId, 1));

            proxyCustomerQueryList.add (proxyCustomerQuery);

        });

        return proxyCustomerQueryList;
    }


    /**
     * 找最近的一个审核未通过或者失败的申请
     *
     * @param user user
     * @return boolean
     * @author Charlie
     * @date 2018/10/22 19:21
     */
    @Override
    public Map<String, Object> recentlyUnCheckOrFailedAudit(PublicAccountUser user) {
        Map<String, Object> result = new HashMap<> (2);
        if (user != null && user.getId () != null) {
            ProxyCustomerAudit audit = proxyCustomerAuditDao.recentlyUnCheckOrFailedAudit (user.getId ());
            if (audit != null) {
                result.put ("hasUnAuditProxy", 1);
                result.put ("auditStatus", audit.getAuditStatus ());
                result.put ("auditMsg", audit.getAuditMsg ());
                audit.setId (null);
                audit.setRefereeUserId (null);
                audit.setUpdateUserId (null);
                audit.setRecommonUserId (null);
                result.put ("audit", audit);
                return result;
            }

        }
        result.put ("hasUnAuditProxy", 0);
        result.put ("auditStatus", "");
        result.put ("auditMsg", "");
        return result;
    }

    /**
     * 该手机是否可以注册代理
     *
     * @param param param
     * @author Charlie
     * @date 2018/9/21 13:39
     */
    private void verifyPhoneCanRegisterProxyCustomer(ProxyCustomerAudit param) {
//        List<ProxyCustomer> pcList = proxyCustomerDao.selectByTerritoryAndType (param.getType (), param.getProvince (), param.getCity (), param.getCounty ());
//        if (! ObjectUtils.isEmpty (pcList)) {
//            logger.info ("申请代理---地域代理已被注册");
//            throw ErrorHelper.me ("该地域代理已被注册");
//        }

        //已有上级关系不能被邀请
        if (BeanKit.gt0 (param.getRefereeUserId ())) {
            ProxyReferee superior = proxyRefereeService.findSuperior (param.getRecommonUserId ());
            ErrorHelper.declare (superior == null, 530, "用户已有上级关系,不能被邀请注册,请从公账号页面进入重新注册");
        }

        //已有注册账号，不可重复注册
        ProxyCustomer history = MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomer (),
                new MybatisSqlWhereBuild (ProxyCustomer.class)
                        .eq (ProxyCustomer::getPhone, param.getPhone ())
                        .eq (ProxyCustomer::getDelStatus, NORMAL)
        );
        if (BeanKit.notNull (history)) {
            Integer historyType = history.getType ();
            if (ProxyCustomerType.CITY.isThis (historyType)) {
                throw ErrorHelper.me (530, "已是市级代理,不可重复注册");
            }
            if (ProxyCustomerType.COUNTY.isThis (historyType) && ProxyCustomerType.COUNTY.isThis (param.getType ())) {
                throw ErrorHelper.me (530, "已是县级代理,不可重复注册");
            }
        }

        //如果是县代理,申请的县是否属于推荐人的市代理范围
        if (param.getType () == 2 && BeanKit.gt0 (param.getRefereeUserId ())) {
            ProxyCustomer refProxyCustomer = proxyCustomerDao.selectByUserId (param.getRefereeUserId ());
            ErrorHelper.declareNull (refProxyCustomer, "未找到上级代理");
//            ErrorHelper.declare (
//                    ObjectUtils.nullSafeEquals (refProxyCustomer.getProvince (), param.getProvince ())
//                            && ObjectUtils.nullSafeEquals (refProxyCustomer.getCity (), param.getCity ()),
//                    "邀请县代理,所属县不属于被邀请者所属城市"
//            );
        }
    }


    /**
     * 创建代理商
     *
     * @return com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/20 19:13
     */
    @Transactional( rollbackFor = Exception.class )
    public ProxyCustomer createProxyCustomer(ProxyCustomerAudit audit) {
        Integer type = audit.getType ();
        ProxyCustomer proxyCustomer = ProxyCustomerDao.build (audit);
        if (ProxyCustomerType.COUNTY.isThis (type)) {
            //县级代理,直接创建
            int rec = MybatisOperaterUtil.getInstance ().save (proxyCustomer);
            logger.info ("创建一个县级代理商 rec:{}", rec);
            ErrorHelper.declare (rec == 1, "创建代理商失败");

            //解绑以前客户-县级代理关系
            rec = proxyRefereeService.unBindCustomer2CountyReferee (proxyCustomer.getUserId (), 0L);
            logger.info ("解绑以前客户-县级代理关系 rec:{}", rec);

            Long refereeUserId = audit.getRefereeUserId ();
            if (BeanKit.hasNull (refereeUserId)) {
                logger.info ("创建代理商,没有上级代理 auditId:{}", audit.getId ());
            }
            else {
                //绑定上级市代理关系
                ProxyCustomer history = MybatisOperaterUtil.getInstance ().findOne (
                        new ProxyCustomer (),
                        new MybatisSqlWhereBuild (ProxyCustomer.class)
                                .eq (ProxyCustomer::getUserId, refereeUserId)
                                .eq (ProxyCustomer::getType, ProxyCustomerType.CITY.getCode ())
                                .eq (ProxyCustomer::getDelStatus, NORMAL)
                );
                ErrorHelper.declareNull (history, "创建县代理商,没有找到上级市代理");
                proxyRefereeService.bindReferee (refereeUserId, proxyCustomer.getUserId (), 0L, 2);
            }
        }
        else if (ProxyCustomerType.CITY.isThis (type)) {
            List<ProxyCustomer> proxyCustomerList = proxyCustomerDao.selectUserProxy (audit.getPhone ());
            if (ObjectUtils.isEmpty (proxyCustomerList)) {
                //客户身份申请市级代理
                //创建代理账户
                int rec = MybatisOperaterUtil.getInstance ().save (proxyCustomer);
                logger.info ("创建一个市级代理商 rec:{}", rec);
                ErrorHelper.declare (rec == 1, "创建代理商失败");
            }
            else if (proxyCustomerList.size () == 1) {
                //县级代理身份申请市级代理
                //修改县级代理为市级代理
                ProxyCustomer proxyCustomerId = proxyCustomerList.get (0);
                int rec = proxyCustomerDao.updateProxyCounty2City (proxyCustomerId.getId ());
                logger.info ("将县级代理升级到市级代理 recommonUserId:{},rec:{}", proxyCustomerId.getId (), rec);
            }
            else {
                ErrorHelper.declare (Boolean.FALSE, "查找用户的代理账户,找到多个");
            }
            proxyRefereeService.unBindAllReferee (audit.getRecommonUserId ());

        }
        else {
            ErrorHelper.declare (false, "未知的代理类型");
        }

        //并发插入的情况(其实还是有些风险的,先这样)
//        List<ProxyCustomer> existProxyCustomerList = proxyCustomerDao.selectByTerritoryAndType (type, audit.getProvince (), audit.getCity (), audit.getCounty ());
//        if (existProxyCustomerList.size () > 1) {
//            logger.info ("创建一个代理商,找到多个代理 size:{},type:{},Province:{},City:{},County:{}", existProxyCustomerList.size (), type, audit.getProvince (), audit.getCity (), audit.getCounty ());
//            ErrorHelper.declare (Boolean.FALSE, "创建代理失败,该地域已有代理");
//        }
        return proxyCustomer;
    }

}
