package com.jiuy.rb.service.impl.account;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.AccountTypeEnum;
import com.jiuy.rb.enums.CoinsDetailTypeEnum;
import com.jiuy.rb.enums.ShareTypeEnum;
import com.jiuy.rb.mapper.coupon.ShareCoinsRuleMapper;
import com.jiuy.rb.mapper.coupon.WxaShareLogMapper;
import com.jiuy.rb.mapper.coupon.WxaShareMapper;
import com.jiuy.rb.mapper.product.SecondBuyActivityRbMapper;
import com.jiuy.rb.mapper.product.TeamBuyActivityRbMapper;
import com.jiuy.rb.model.account.CoinsVo;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.coupon.*;
import com.jiuy.rb.model.product.SecondBuyActivityRb;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.model.product.TeamBuyActivityRb;
import com.jiuy.rb.model.user.ShopMemberRb;
import com.jiuy.rb.model.user.ShopMemberRbQuery;
import com.jiuy.rb.service.account.ICoinsAccountService;
import com.jiuy.rb.service.account.IShareService;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuy.rb.service.user.IShopMemberService;
import com.jiuyuan.util.HttpClientUtils;
import com.jiuyuan.util.current.ExecutorService;
import com.jiuyuan.util.current.ExecutorTask;
import com.util.CallBackUtil;
import com.util.ServerPathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分享的业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 17:09
 * @Copyright 玖远网络
 */
@Service("shareService")
public class ShareServiceImpl implements IShareService {
    private Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);
    @Resource(name = "wxaShareMapper")
    private WxaShareMapper wxaShareMapper;

    @Resource(name = "wxaShareLogMapper")
    private WxaShareLogMapper wxaShareLogMapper;

    @Resource(name = "shareCoinsRuleMapper")
    private ShareCoinsRuleMapper shareCoinsRuleMapper;

    @Resource(name = "coinsAccountService")
    private ICoinsAccountService coinsAccountService;

    @Resource(name = "shopProductServiceRb")
    private IShopProductService shopProductService;

    @Resource(name = "teamBuyActivityRbMapper")
    private TeamBuyActivityRbMapper teamBuyActivityRbMapper;

    @Resource(name = "secondBuyActivityRbMapper")
    private SecondBuyActivityRbMapper secondBuyActivityRbMapper;

    @Resource(name = "dataDictionaryServiceRb")
    private IDataDictionaryService dataDictionaryService;

    @Resource(name = "shopMemberService")
    private IShopMemberService shopMemberService;

    /**
     * 获取分享规则
     *
     * @param shareType shareType
     * @author Aison
     * @date 2018/7/6 15:57
     * @return com.jiuy.rb.model.coupon.ShareCoinsRule
     */
    @Override
    public ShareCoinsRule getRule(ShareTypeEnum shareType) {

        ShareCoinsRuleQuery ruleQuery = new ShareCoinsRuleQuery();
        ruleQuery.setType(shareType.getCode());
        ruleQuery.setStatus(1);
        ruleQuery.setOrderBy("create_time desc");
        List<ShareCoinsRule> shareCoinsRuleList =  shareCoinsRuleMapper.selectList(ruleQuery);
        return shareCoinsRuleList.size()>0 ? shareCoinsRuleList.get(0) : null;
    }

    /**
     * 是否超出了有效收益分享次数
     *
     * @param userSession userSession
     * @author Aison
     * @date 2018/7/6 10:46
     * @return boolean
     */
    private boolean isOutOfMax(UserSession userSession) {
        WxaShareLogQuery logQuery = new WxaShareLogQuery();
        logQuery.setMemberId(userSession.getMemberId());
        logQuery.setToday(Biz.formatDate(new Date(),"yyyy-MM-dd")+" 00:00:00");
        int count =  wxaShareLogMapper.selectCount(logQuery);

        DataDictionaryRb dataDictionaryRb = dataDictionaryService.getByCode("shareMaxCount","share");
        Integer maxCount = Integer.valueOf(dataDictionaryRb.getVal());
        return count>=maxCount;
    }

    /**
     * 添加分享日志
     *
     * @param userSession userSession
     * @param targetId targetId
     * @param des des
     * @author Aison
     * @date 2018/7/6 10:49
     *
     */
    private void addShareLog(UserSession userSession,Long targetId,String des) {

        // 添加分享记录
        WxaShareLog wxaShareLog = new WxaShareLog();
        wxaShareLog.setCreateTime(new Date());
        wxaShareLog.setDescription(des);
        wxaShareLog.setMemberId(userSession.getMemberId());
        wxaShareLog.setShareType(ShareTypeEnum.PRODUCT_SHARE.getCode());
        wxaShareLog.setTargetId(targetId);
        wxaShareLogMapper.insertSelective(wxaShareLog);
    }

    /**
     * 分享商品
     *
     * @param productId 商品id
     * @param userSession 当前用户
     * @author Aison
     * @date 2018/7/5 17:11
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> shareProduct(Long productId, UserSession userSession) {

        ShopProductRb productRb = shopProductService.getById(productId);
        if(productRb == null) {
            throw BizException.instance(GlobalsEnums.PARAM_ERROR);
        }
        // 添加玖币
        boolean isOutOfMax = isOutOfMax (userSession);
        if(! isOutOfMax) {
            // 计算就玖币值
            ShareCoinsRuleQuery ruleQuery = new ShareCoinsRuleQuery();
            ruleQuery.setType(ShareTypeEnum.PRODUCT_SHARE.getCode());
            ruleQuery.setStatus(1);
            ruleQuery.setOrderBy("create_time desc");
            List<ShareCoinsRule> shareCoinsRuleList =  shareCoinsRuleMapper.selectList(ruleQuery);
            Long coinsCount = 1L;
            BigDecimal proportion = BigDecimal.ZERO;
            if(shareCoinsRuleList != null && shareCoinsRuleList.size()>0) {
                ShareCoinsRule rule = shareCoinsRuleList.get(0);
                int countType = rule.getCountType();
                if(countType==0) {
                    coinsCount = rule.getCount();
                } else {
                    BigDecimal price = productRb.getPrice();
                    price = price == null ? BigDecimal.ZERO : price;
                    proportion = rule.getProportion();
                    coinsCount = proportion.multiply(price).setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
                }
            }
            if(coinsCount>0) {
                coinsCount = coinsCount> 10L  ? 10 : coinsCount;
                //给用户添加玖币
                CoinsVo coinsVo = CoinsVo.instance(CoinsDetailTypeEnum.SHARE_IN,"",coinsCount,null,productId,"分享商品: "+productRb.getName(),
                        "分享商品入账 当前收入为商品价格的"+proportion.doubleValue(),userSession,
                        userSession.getMemberId(),AccountTypeEnum.WXA_USER).create(true);
                coinsAccountService.acceptCoins(coinsVo);
            }
        }

        String clothesNumber = productRb.getClothesNumber();
        addShareLog(userSession,productId,"分享商品："+(clothesNumber==null ? "" : clothesNumber)+" "+productRb.getName());

        Map<String, Object> result = new HashMap<> (2);
        result.put ("isOutOfMax", isOutOfMax);
        return result;
    }


    /**
     * 分享活动
     *
     * @param activityId activityId
     * @param  userSession 用户信息
     * @param type 活动类型  1 限时抢购 2 拼团活动商品
     * @author Aison
     * @date 2018/7/6 10:45
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> shareActivity(Long activityId,UserSession userSession,Integer type) {


        BigDecimal price = BigDecimal.ZERO;
        String productName = "";
        switch (type) {
            case 1 :
                SecondBuyActivityRb secondBuyActivity =  secondBuyActivityRbMapper.selectByPrimaryKey(activityId);
                price = secondBuyActivity.getActivityPrice();
                productName = "分享限时抢购商品："+secondBuyActivity.getShopProductName();
                break;
            case 2:
                TeamBuyActivityRb teamBuyActivity =  teamBuyActivityRbMapper.selectByPrimaryKey(activityId);
                price = teamBuyActivity.getActivityPrice();
                productName = "分享团购商品："+teamBuyActivity.getShopProductName();
                break;
            default:
                break;
        }
        price = price == null ? BigDecimal.ZERO : price;

        // 添加玖币
        boolean isOutOfMax = isOutOfMax (userSession);
        if(! isOutOfMax) {
            // 计算就玖币值
            ShareCoinsRuleQuery ruleQuery = new ShareCoinsRuleQuery();
            // 分享活动与分享商品是一样的
            ruleQuery.setType(ShareTypeEnum.PRODUCT_SHARE.getCode());
            ruleQuery.setStatus(1);
            ruleQuery.setOrderBy("create_time desc");
            List<ShareCoinsRule> shareCoinsRuleList =  shareCoinsRuleMapper.selectList(ruleQuery);
            Long coinsCount = 1L;
            if(shareCoinsRuleList != null && shareCoinsRuleList.size()>0) {
                ShareCoinsRule rule = shareCoinsRuleList.get(0);
                int countType = rule.getCountType();
                if(countType==0) {
                    coinsCount = rule.getCount();
                } else {
                    coinsCount = rule.getProportion().multiply(price).setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
                }
            }
            if(coinsCount>0) {
                coinsCount = coinsCount> 10L  ? 10 : coinsCount;
                //给用户添加玖币
                CoinsVo coinsVo = CoinsVo.instance(CoinsDetailTypeEnum.SHARE_IN,"",coinsCount,null,activityId,productName,
                        "活动商品的入账规则?",userSession,
                        userSession.getMemberId(),AccountTypeEnum.WXA_USER).create(true);
                coinsAccountService.acceptCoins(coinsVo);
            }
        }

        addShareLog(userSession,activityId,productName);

        Map<String, Object> result = new HashMap<> (2);
        result.put ("isOutOfMax", isOutOfMax);
        return result;
    }


    /**
     * 分享优惠券
     *
     * @param couponId couponId
     * @author Aison
     * @date 2018/7/6 10:45
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> shareCoupon(Long couponId,UserSession userSession) {
        // 添加玖币
        boolean isOutOfMax = isOutOfMax (userSession);
        if(! isOutOfMax) {
            // 计算就玖币值
            ShareCoinsRuleQuery ruleQuery = new ShareCoinsRuleQuery();
            ruleQuery.setType(ShareTypeEnum.COUPON_SHARE.getCode());
            ruleQuery.setStatus(1);
            ruleQuery.setOrderBy("create_time desc");
            List<ShareCoinsRule> shareCoinsRuleList =  shareCoinsRuleMapper.selectList(ruleQuery);
            Long coinsCount = 1L;
            if(shareCoinsRuleList != null && shareCoinsRuleList.size()>0) {
                ShareCoinsRule rule = shareCoinsRuleList.get(0);
                int countType = rule.getCountType();
                if(countType==0) {
                    coinsCount = rule.getCount();
                }
            }
            if(coinsCount>0) {
                coinsCount = coinsCount> 10L  ? 10 : coinsCount;
                //给用户添加玖币
                CoinsVo coinsVo = CoinsVo.instance(CoinsDetailTypeEnum.SHARE_IN,"",coinsCount,null,couponId,"分享优惠券 ","分享优惠券",userSession,
                        userSession.getMemberId(),AccountTypeEnum.WXA_USER).create(true);
                coinsAccountService.acceptCoins(coinsVo);
            }
        }
        addShareLog(userSession,couponId,"分享优惠券");

        Map<String, Object> result = new HashMap<> (2);
        result.put ("isOutOfMax", isOutOfMax);
        return result;
    }


    /**
     * 确认分享关系
     *
     * @param wxaShare wxaShare
     * @author Aison
     * @date 2018/7/6 18:12
     *
     */
    @Override
    public void shareFriend(WxaShare wxaShare, UserSession userSession) {

        wxaShare.setTargetUser(userSession.getMemberId());

        // 验证参数
        Declare.notNull(GlobalsEnums.PARAM_ERROR,wxaShare.getSourceUser(),wxaShare.getTargetUser(),wxaShare.getShareType(),wxaShare.getWxNickname());
        wxaShare.setCreateTime(new Date());

        Long targetId = userSession.getMemberId();
        Long sourceId = wxaShare.getSourceUser();

        // 将邀请者和被邀请者顺序换下来查询看是否能够查询到数据
        // 为了剔除掉 相互邀请的问题
        WxaShareQuery query1 = new WxaShareQuery();
        query1.setSourceUser(targetId);
        query1.setTargetUser(sourceId);
        int xh =  wxaShareMapper.selectCount(query1);
        if(xh>0) {
            return ;
        }

        // 自己不能分享给自己
        if(targetId.equals(sourceId)) {
            return ;
        }
        WxaShareQuery query = new WxaShareQuery();
        //判断是否已经跟其他用户确认了关系
        query.setTargetUser(targetId);
        int targetCount =  wxaShareMapper.selectCount(query);
        // 如果已经确认了关系则返回
        if(targetCount>0) {
            return ;
        }
        // 判断是否已经是好友了
        query.setSourceUser(wxaShare.getSourceUser());
        query.setTargetUser(userSession.getMemberId());
        int count = wxaShareMapper.selectCount(query);
        if(count>0) {
            return ;
        }

        // 查询被邀请者的用户来源
        ShopMemberRbQuery query2 = new ShopMemberRbQuery();
        query2.setId(targetId);
        ShopMemberRb shopMember = shopMemberService.getShopMember(query2);
        // 如果不是邀请用户则返回
        String source = "1";
        if(!source.equals(shopMember.getSource())) {
            return ;
        }

        wxaShareMapper.insertSelective(wxaShare);
        String url =  "/distribution/distribution/binding/fans";
        JSONObject map = new JSONObject();
        map.put("userId",sourceId);
        map.put("fans",targetId);
        CallBackUtil.send(map.toJSONString(),url,"get");

    }

    /**
     * 分享列表
     *
     * @param query query
     * @author Aison
     * @date 2018/7/6 18:27
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.coupon.WxaShareQuery>
     */
    @Override
    public MyPage<WxaShareQuery> shareFriendList(WxaShareQuery query) {
        query.setOrderBy("create_time desc");
        return  MyPage.copy2Child(wxaShareMapper.selectList(query),WxaShareQuery.class,(source,target)->{
            ShareTypeEnum item =  ShareTypeEnum.getIem(source.getShareType());
            target.setShareTypeName(item==null ? "" :item.getName());
            target.setCreateTimeReadable(Biz.formatDate(source.getCreateTime(),null));
        });
    }

    /**
     * 获取用户的邀请数量
     *
     * @param userId userId
     * @author Aison
     * @date 2018/7/11 17:51
     * @return java.util.Map<java.lang.Long,java.lang.Long>
     */
    @Override
    public Map<Long, Long> inviteCountMap(Set<Long> userId) {

        Map<Long,Map<String,Object>> counts = wxaShareMapper.selectWxaCountGroup(userId);
        if(counts==null  || counts.size() == 0) {
            return new HashMap<>(0);
        } else {
            Map<Long, Long> countMap = new HashMap<>(20);
            counts.forEach((key,val)-> countMap.put(key,Long.valueOf(val.get("count").toString())));
            return countMap;
        }
    }


    /**
     * 查询单个分享
     *
     * @param query query
     * @author Aison
     * @date 2018/7/17 11:39
     * @return com.jiuy.rb.model.coupon.WxaShare
     */
    @Override
    public List<WxaShare> getShare(WxaShareQuery query) {

        return wxaShareMapper.selectList(query);
    }


}
