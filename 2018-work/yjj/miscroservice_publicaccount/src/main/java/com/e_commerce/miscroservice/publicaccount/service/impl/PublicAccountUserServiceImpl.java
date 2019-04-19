package com.e_commerce.miscroservice.publicaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReferee;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.WxUserInfo;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.*;
import com.e_commerce.miscroservice.commons.utils.wx.publicaccount.AuthUtils;
import com.e_commerce.miscroservice.commons.utils.wx.publicaccount.SNSUserInfo;
import com.e_commerce.miscroservice.publicaccount.dao.PublicAccountUserDao;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerType;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyCustomerService;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyRefereeService;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.e_commerce.miscroservice.commons.utils.BeanKit.replaceEmoji;
import static com.e_commerce.miscroservice.commons.utils.PubAccountLoginUtils.COOKIE_OPEN_ID;
import static com.e_commerce.miscroservice.commons.utils.PubAccountLoginUtils.COOKIE_TOKEN;
import static com.e_commerce.miscroservice.commons.utils.PubAccountLoginUtils.createUserToken;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/21 18:23
 * @Copyright 玖远网络
 */
@Service
public class PublicAccountUserServiceImpl implements PublicAccountUserService{
    private static final String DELETE_TIP = "由于违规您已被禁用，如有疑问请联系400-118-0099";
    private Log logger = Log.getInstance (PublicAccountUserServiceImpl.class);

    /**
     * 用户没有微信昵称时默认的名称
     */
    @Value( "${publicaccount.user.defaultName}" )
    private String userDefaultName;

    @Value( "${publicaccount.appid}" )
    private String appId;

    @Value( "${publicaccount.secret}" )
    private String secret;

    @Autowired
    private PublicAccountUserService publicAccountUserService;
    @Autowired
    private PublicAccountUserDao publicAccountUserDao;

    @Autowired
    private ProxyRefereeService proxyRefereeService;

    @Autowired
    private ProxyCustomerService proxyCustomerService;

    @Resource( name = "strRedisTemplate" )
    private RedisTemplate<String, String> strRedisTemplate;


    /**
     * 获登录一个用户,如果没有则新建
     *
     * @param user      user
     * @param authCode  短信验证码
     * @param response  response
     * @param openToken
     * @param openId
     * @param isSubjectAccount 是否是主体账号
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/24 14:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicAccountUser loginUserByPhoneIfNullCreate(PublicAccountUser user, String authCode, HttpServletResponse response, String openToken, String openId, boolean isSubjectAccount) {
        ErrorHelper.declare (! BeanKit.hasNull (user.getPhone (), authCode), "手机号,验证码不可为空");
        logger.info ("采用手机号登录, 如果没有则新建用户 phone={},openId={}", user.getPhone (), openId);
        //有就校验验证码
        boolean isLegal = SmsUtils.verifyCode (user.getPhone (), authCode);
        ErrorHelper.declare (isLegal, 530, "验证码错误");

        PublicAccountUser history = findByPhone (user.getPhone ());
        boolean isNewUser = ObjectUtils.isEmpty (history);
        logger.info ("isNewUser = {}", isLegal);

        //微信信息
        PublicAccountUser wxInfo = PubAccountLoginUtils.checkUserToken (strRedisTemplate.opsForValue (), openId, openToken);
        ErrorHelper.declareNull (wxInfo, "绑定微信失败,没有获取到微信信息");

        if (isNewUser) {
            //创建账号
            logger.info ("创建新的公众号账户, phone:{}", user.getPhone ());
            user.setOpenId (wxInfo.getOpenId ());
            user.setWxUserIcon (wxInfo.getWxUserIcon ());
            //如果没有微信授权默认 "用户"+手机号后四位
            String wxName = wxInfo.getWxName ();
            String phone = user.getPhone ();
            user.setWxName (BeanKit.notNull (wxName) ? wxName : userDefaultName (phone));
            user.setStoreId (0L);
            int rec = MybatisOperaterUtil.getInstance ().save (user);
            logger.info ("创建公众号用户 user:{}, rec:{}", user, rec);
        }
        else {
            //更新账号微信信息
            logger.info ("创建公众号账户,账号已存在 phone:{}", user.getPhone ());
            ErrorHelper.declare (StateEnum.DELETE != history.getDelStatus (), 531, DELETE_TIP);
            PublicAccountUser updInfo = new PublicAccountUser ();
            updInfo.setOpenId (wxInfo.getOpenId ());
            updInfo.setWxName (wxInfo.getWxName ());
            updInfo.setWxUserIcon (wxInfo.getWxUserIcon ());
            MybatisOperaterUtil.getInstance ().update (updInfo, new MybatisSqlWhereBuild (PublicAccountUser.class).eq (PublicAccountUser::getId, history.getId ()));
        }

        if (isSubjectAccount) {
            /* 该手机号设为该openId下的主体账号 */
            //关闭openId的其他主体账号
            logger.info ("该登录手机号为主题账号");
            publicAccountUserDao.closeSubjectAccountByOpenId (openId);
            //设置当前账号为主体账号
            int rec = publicAccountUserDao.openSubjectAccountByPhone (user.getPhone ());
            ErrorHelper.declare (rec == 1, "设置主体账号失败");

            //更新登录的缓存
            if (BeanKit.notNull (history)) {
                boolean isOpenIdUpd = !ObjectUtils.nullSafeEquals (history.getOpenId (), openId);
                if (isOpenIdUpd && StringUtils.isNotBlank (history.getOpenId ())) {
                    //踢出其他登录
                    strRedisTemplate.delete (PubAccountLoginUtils.userCacheKey (history.getOpenId ()));
                }

            }
            //放入缓存
            PublicAccountUser subjectUser = publicAccountUserDao.findSubjectByOpenId (openId);
            putInCacheAndResponse (subjectUser, response);
        }

        //返回登录手机号登录的对象
        return findByPhone (user.getPhone ());
    }


    /**
     * 用户信息放入缓存.token返回给前端
     *
     * @param user     user
     * @param response response
     * @author Charlie
     * @date 2018/9/29 11:54
     */
    @Override
    public String putInCacheAndResponse(PublicAccountUser user, HttpServletResponse response) {
        //response放入token
        String openId = user.getOpenId ().trim ();
        if (response != null) {
            logger.info ("将openId和token放到消息头");
            PubAccountLoginUtils.putTokenInResponse (openId, response);
        }
        //放入redis
        String key = PubAccountLoginUtils.userCacheKey (openId);
        ValueOperations<String, String> cache = strRedisTemplate.opsForValue ();
        cache.set (key, JSONObject.toJSONString (user), 30, TimeUnit.DAYS);
        logger.info ("将用户信息放入缓存 key={},user={}", key, user);
        return key;
    }


    /**
     * 根据手机号修改用户信息
     *
     * @param updateInfo updateInfo
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/10/7 16:49
     */
    @Override
    public PublicAccountUser updateByPhone(PublicAccountUser updateInfo) {
        PublicAccountUser history = MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getPhone, updateInfo.getPhone ())
                        .eq (PublicAccountUser::getDelStatus, StateEnum.NORMAL)
        );

        if (BeanKit.hasNull (history)) {
            return null;
        }

        MybatisOperaterUtil.getInstance ().update (
                updateInfo,
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getId, history.getId ())
        );

        return MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getId, history.getId ())
        );
    }

    /**
     * @param code code
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     * @author Charlie
     * @date 2018/10/22 17:32
     */
    @Override
    public Map<String, Object> loginByWeiXin(String code) {
        WxUserInfo wxUserInfo = httpGetWxUserInfo (code);
        ErrorHelper.declareNull (wxUserInfo, "授权失败");

        String openId = wxUserInfo.getOpenId ();
        String wxName = wxUserInfo.getWxName ();
        wxName = StringUtils.isNotBlank (wxName) ? replaceEmoji(wxName, "") : wxName;
        //用户信息放到缓存
        PublicAccountUser user = publicAccountUserDao.findSubjectByOpenId (openId);
        boolean isNeedBindPhone = user == null;
        if (isNeedBindPhone) {
            //登录授权时获取微信信息
            logger.info ("将wxUserInfo放入缓存");
            user = new PublicAccountUser ();
            user.setWxName (wxName);
            user.setWxUserIcon (wxUserInfo.getWxUserIcon ());
            user.setOpenId (openId);
        }
        else {
            //校验是否登录过
            ErrorHelper.declare (StateEnum.DELETE != user.getDelStatus (), 531, DELETE_TIP);
            logger.info ("将user放入缓存");
        }
        putInCacheAndResponse (user, null);
        //返还openId
        Map<String, Object> result = new HashMap<> (2);
        result.put ("openToken", createUserToken(openId));
        result.put ("openId", openId);
        result.put ("isNeedBindPhone", isNeedBindPhone);
        result.put ("isSuccess", ! isNeedBindPhone);
        result.put ("wxUserIcon", wxUserInfo.getWxUserIcon ());
        result.put ("wxName", wxUserInfo.getWxName ());
        result.put ("isDel", Boolean.FALSE);
        return result;
    }


    /**
     * 微信认证,换取用户微信信息
     *
     * @param code 公众号授权code
     * @return com.e_commerce.miscroservice.publicaccount.entity.vo.WxUserInfo
     * @author Charlie
     * @date 2018/9/25 15:00
     */
    private WxUserInfo httpGetWxUserInfo(String code) {
        ErrorHelper.declare (BeanKit.notNull (code), "微信验证失败");
        SNSUserInfo snsUserInfo = AuthUtils.getSNSUserInfo (appId, secret, code);
        ErrorHelper.declareNull (snsUserInfo, "微信授权失败");
        return WxUserInfo.build (snsUserInfo.getOpenId (), snsUserInfo.getNickname (), snsUserInfo.getHeadImgUrl ());
    }


    /**
     * 通过手机号查询
     *
     * @param phone phone
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/24 17:25
     */
    @Override
    public PublicAccountUser findByPhone(String phone) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getPhone, phone)
//                        .eq (PublicAccountUser::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public PublicAccountUser findByUserId(long userId) {
        return publicAccountUserDao.findById (userId);
    }


    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/9/24 23:34
     */
    @Override
    public int updateById(PublicAccountUser updInfo) {
        logger.info ("更新账户信息 userId={}", updInfo.getId ());
        return publicAccountUserDao.updateByPrimaryKeySelective (updInfo);
    }


    /**
     * 查询用户信息
     *
     * @param query query
     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery>
     * @author Charlie
     * @date 2018/9/25 11:51
     */
    @Override
    public PageInfo<PublicAccountUserQuery> listUser(PublicAccountUserQuery query) {
        //查询条件
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<PublicAccountUserQuery> publicAccountUserResult = publicAccountUserDao.listUser (query);
        for (PublicAccountUser user : publicAccountUserResult) {
            user.setCreateTimeReadable (TimeUtils.stamp2Str (user.getCreateTime ()));
        }
        return new PageInfo<> (publicAccountUserResult);
    }


    /**
     * 手机认证
     *
     * @param refereeUserId 推荐人id
     * @param phone         phone
     * @param authCode      authCode
     * @param openToken     openToken
     * @param openId        openId
     * @param response      response
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/25 14:33
     */
    @Override
    public Map<String, Object> authentication(Long refereeUserId, String phone, String authCode, String openToken, String openId, HttpServletResponse response) {
        PublicAccountUser user = MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getPhone, phone));

        if (user != null) {
            if (ObjectUtils.nullSafeEquals (user.getDelStatus (), 1)) {
                throw ErrorHelper.me (531, DELETE_TIP);
            }
        }


        //认证
        user = doAuthentication (refereeUserId, phone, authCode, response, openToken, openId);


        //获取用户的代理信息
        ProxyCustomer proxyCustomer = proxyCustomerService.selectByUserId (user.getId ());

        Map<String, Object> result = new HashMap<> (6);
        result.put (COOKIE_OPEN_ID, response.getHeader (COOKIE_OPEN_ID));
        result.put (COOKIE_TOKEN, response.getHeader (COOKIE_TOKEN));
        result.put ("wxUserIcon", user.getWxUserIcon ());
        result.put ("wxName", user.getWxName ());
        //0:不是代理
        int proxyCustomerType = BeanKit.hasNull (proxyCustomer) ? 0 : proxyCustomer.getType ();
        //代理姓名
        String proxyCustomerName = BeanKit.hasNull (proxyCustomer) ? "" : proxyCustomer.getName ();
        result.put ("proxyCustomerType", proxyCustomerType);
        result.put ("proxyCustomerName", proxyCustomerName);
        return result;
    }

    /**
     * 手机认证
     *
     * @param refereeUserId 推荐人id
     * @param phone         phone
     * @param authCode      authCode
     * @param response      response
     * @param openToken     openToken
     * @param openId        openId
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/25 14:33
     */
    @Override
    public PublicAccountUser doAuthentication(Long refereeUserId, String phone, String authCode, HttpServletResponse response, String openToken, String openId) {
        //校验推荐人
        ProxyCustomer proxyCustomer = null;
        if (BeanKit.gt0 (refereeUserId)) {
            //查询是否有上级代理,没有则新建
            proxyCustomer = proxyCustomerService.selectByUserId (refereeUserId);
            ErrorHelper.declareNull (proxyCustomer, "未找到代理商信息");
        }

        PublicAccountUser history = findByPhone (phone);

        PublicAccountUser user = new PublicAccountUser ();
        user.setPhone (phone);
        //登录,有事务
        user = publicAccountUserService.loginUserByPhoneIfNullCreate (user, authCode, response, openToken, openId, true);

        if (history != null) {
            //已有账号,不需要再绑定关系
            return user;
        }

        //创建邀请关系,与首次登录无关
        if (BeanKit.notNull (proxyCustomer)) {
            //历史关系,账号都没有,更不会有历史关系

            if (ProxyCustomerType.COUNTY.isThis (proxyCustomer.getType ())) {
                //如果是县级代理,再查找市级代理
                ProxyReferee cityProxyRef = MybatisOperaterUtil.getInstance ().findOne (
                        new ProxyReferee (),
                        new MybatisSqlWhereBuild (ProxyReferee.class)
                                .eq (ProxyReferee::getRecommonUserId, proxyCustomer.getUserId ())
                                .eq (ProxyReferee::getRecommonUserType, 2)
                                .eq (ProxyReferee::getDelStatus, StateEnum.NORMAL)
                                .eq (ProxyReferee::getStatus, 0)
                );
                logger.info ("绑定关系 cityProxyRef:{}", cityProxyRef);

                Long refereParentId = null;
                if (BeanKit.notNull (cityProxyRef)) {
                    ProxyCustomer cityProxy = MybatisOperaterUtil.getInstance ().findOne (
                            new ProxyCustomer (),
                            new MybatisSqlWhereBuild (ProxyCustomer.class)
                                    .eq (ProxyCustomer::getUserId, cityProxyRef.getRefereeUserId ())
                                    .eq (ProxyCustomer::getDelStatus, StateEnum.NORMAL)
                    );
                    if (BeanKit.notNull (cityProxy)) {
                        refereParentId = cityProxy.getUserId ();
                    }
                }
                proxyRefereeService.bindReferee (proxyCustomer.getUserId (), user.getId (), refereParentId, 1);
            }
            else if (ProxyCustomerType.CITY.isThis (proxyCustomer.getType ())) {
                //市代理
                proxyRefereeService.bindReferee (proxyCustomer.getUserId (), user.getId (), 0L, 1);
            }
            else {
                ErrorHelper.me ("未知的代理类型");
            }
        }

        return user;
    }


    /**
     * 退出登录
     *
     * @param userId userId
     * @return boolean
     * @author Charlie
     * @date 2018/9/28 13:53
     */
    @Override
    public boolean logout(Long userId) {
        PublicAccountUser user = publicAccountUserDao.findById (userId);
        ErrorHelper.declareNull (user, "未找到用户信息");
        String historyOpenId = user.getOpenId ();
        user.setWxUserIcon ("");
        String phone = user.getPhone ();
        user.setWxName (userDefaultName (phone));
        user.setSubjectAccount (0);
        user.setOpenId ("");
        int rec = publicAccountUserDao.updateByPrimaryKeySelective (user);
        logger.info ("退出登录 userId:{},rec:{}", userId, rec);

        //redis清空用户id,但是保存openId信息
        PublicAccountUser cacheUser = PubAccountLoginUtils.getByOpenId (strRedisTemplate.opsForValue (), historyOpenId);
        if (BeanKit.notNull (cacheUser)) {
            PublicAccountUser newCacheUser = new PublicAccountUser ();
            newCacheUser.setWxUserIcon (cacheUser.getWxUserIcon ());
            newCacheUser.setWxName (cacheUser.getWxName ());
            newCacheUser.setOpenId (cacheUser.getOpenId ());
            putInCacheAndResponse (newCacheUser, null);
        }
        return true;
    }

    /**
     * 用户是否登录
     *
     * @param request request
     * @return null 没有登录
     * @author Charlie
     * @date 2018/9/28 19:54
     */
    @Override
    public PublicAccountUser isLogin(HttpServletRequest request) {
        return isLogin (request.getParameter (COOKIE_OPEN_ID), request.getParameter (COOKIE_TOKEN));
    }

    /**
     * 获取用户名的默认名
     * <p>前缀+手机号后四位</p>
     *
     * @param phone phone
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/30 13:41
     */
    private String userDefaultName(String phone) {
        return phone != null && phone.length () >= 4 ? userDefaultName + phone.substring (phone.length () - 4, phone.length ()) : userDefaultName;
    }


    /**
     * 用户是否登录
     *
     * @param openId openId
     * @param openToken openToken
     * @return null 没有登录
     * @author Charlie
     * @date 2018/9/28 19:54
     */
    @Override
    public PublicAccountUser isLogin(String openId, String openToken) {
        try {
            PublicAccountUser user = PubAccountLoginUtils.checkUserToken (strRedisTemplate.opsForValue (), openId, openToken);
            //缓存中放的是openId,但用户不一定认证登录过,所以id可能为空
            if (user != null
                    && ObjectUtils.nullSafeEquals (user.getDelStatus (), StateEnum.NORMAL)
                    && BeanKit.notNull (user.getId ())
                    && ObjectUtils.nullSafeEquals (user.getSubjectAccount (), 1)) {
                return user;
            }
        } catch (ErrorHelper e) {
            logger.warn ("用户没有登录 msg:{}", e.getMsg ());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int stopCustomer(Long userId, Integer type) {
        PublicAccountUser publicAccountUser = MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getId, userId)
        );

        if (publicAccountUser == null || publicAccountUser.getDelStatus ().equals (type)) {
            return 1;
        }

        PublicAccountUser publicAccountUser1 = new PublicAccountUser ();
        publicAccountUser1.setId (userId);
        publicAccountUser1.setDelStatus (type);

        int rec = MybatisOperaterUtil.getInstance ().update (
                publicAccountUser1,
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getId, userId));


        logger.info ("禁用用户 userId:{},rec:{}", userId, rec);

        PublicAccountUser cacheUser = PubAccountLoginUtils.getByOpenId (strRedisTemplate.opsForValue (), publicAccountUser.getOpenId ());
        if (BeanKit.notNull (cacheUser)) {
            PublicAccountUser newCacheUser = new PublicAccountUser ();
            newCacheUser.setWxUserIcon (cacheUser.getWxUserIcon ());
            newCacheUser.setWxName (cacheUser.getWxName ());
            newCacheUser.setOpenId (cacheUser.getOpenId ());
            putInCacheAndResponse (newCacheUser, null);
        }
        return rec;
    }


}
