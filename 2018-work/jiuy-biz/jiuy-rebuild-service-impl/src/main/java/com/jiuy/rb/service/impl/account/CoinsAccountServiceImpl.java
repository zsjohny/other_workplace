package com.jiuy.rb.service.impl.account;

import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ConstMy;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.base.util.PaymentBiz;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.account.*;
import com.jiuy.rb.mapper.user.StoreBusinessRbMapper;
import com.jiuy.rb.model.account.*;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.user.ShopMemberRb;
import com.jiuy.rb.model.user.ShopMemberRbQuery;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.service.account.ICoinsAccountService;
import com.jiuy.rb.service.account.IReleaseCoinsService;
import com.jiuy.rb.service.account.IShareService;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.user.IShopMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 * 玖币账户相关的业务实现
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 9:43
 * @Copyright 玖远网络
 */
@Service("coinsAccountService")
public class CoinsAccountServiceImpl implements ICoinsAccountService {


    private static final Logger logger = LoggerFactory.getLogger(CoinsAccountServiceImpl.class);

    @Autowired
    private CoinsMapper coinsMapper;

    @Autowired
    private CoinsDetailMapper coinsDetailMapper;

    @Autowired
    private CoinsLogMapper coinsLogMapper;

    @Resource(name = "shareService")
    private IShareService shareService;

    @Resource(name = "releaseCoinsService")
    private IReleaseCoinsService releaseCoinsService;

    @Resource(name = "dataDictionaryServiceRb")
    private IDataDictionaryService dataDictionaryService;

    @Resource(name = "coinsCashOutMapper")
    private CoinsCashOutMapper coinsCashOutMapper;

    @Resource(name = "shopMemberService")
    private IShopMemberService shopMemberService;

    @Resource(name = "storeBusinessRbMapper")
    private StoreBusinessRbMapper storeBusinessRbMapper;

    @Resource(name = "wxaPayConfigMapper")
    private WxaPayConfigMapper wxaPayConfigMapper;


    /**
     * 查询某个用户的玖币账户
     *
     * @param userId          用户id
     * @param accountTypeEnum 用户类型
     * @return com.jiuy.rb.model.account.Coins
     * @author Aison
     * @date 2018/7/5 18:47
     */
    @Override
    public Coins getCoinsAccount(Long userId, AccountTypeEnum accountTypeEnum) {

        CoinsQuery query = new CoinsQuery();
        query.setUserId(userId);
        query.setUserType(accountTypeEnum.getCode());
        Coins coins = coinsMapper.selectOne(query);
        Declare.isFailed(coins == null, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);
        query = Biz.copyBean(coins, CoinsQuery.class);

        CoinsDetailQuery detailQuery = new CoinsDetailQuery();
        detailQuery.setCoinsId(coins.getId());
        List<CoinsDetail> details = coinsDetailMapper.selectList(detailQuery);

        Long allIn = 0L;
        Long allOut = 0L;
        for (CoinsDetail detail : details) {
            Integer status = detail.getStatus();
            // 已收入
            if(CoinsDetailStatusEnum.IN.isThis(status) || CoinsDetailStatusEnum.WAIT_IN.isThis(status)){
                allIn+=detail.getCount();
            }
            //实际已经出账了  已到期
            if(CoinsDetailStatusEnum.CASH_OUT.isThis(status) || CoinsDetailStatusEnum.EXPIRE.isThis(status) ||CoinsDetailStatusEnum.LOSE.isThis(status)) {
                allOut+=detail.getCount();
            }
        }
        query.setAllIn(allIn);
        query.setAllOut(allOut);
        return query;
    }

    /**
     * 待入账玖币数量
     *
     * @param userId 用户id
     * @param accountTypeEnum 用户类型
     * @author Aison
     * @date 2018/7/5 18:47
     * @return com.jiuy.rb.model.account.Coins
     */
    @Override
    public Long waitInCoins(Long userId, AccountTypeEnum accountTypeEnum) {

        CoinsQuery query = new CoinsQuery();
        query.setUserId(userId);
        query.setUserType(accountTypeEnum.getCode());
        Coins coins = coinsMapper.selectOne(query);
        if(coins==null) {
            return 0L;
        }
        CoinsDetailQuery detailQuery = new CoinsDetailQuery();
        detailQuery.setCoinsId(coins.getId());
        detailQuery.setStatus(CoinsDetailStatusEnum.WAIT_IN.getCode());

        Long count = coinsDetailMapper.sumCount(detailQuery);
        detailQuery.setStatus(CoinsDetailStatusEnum.LOSE.getCode());

        Long lostCount = coinsDetailMapper.sumCount(detailQuery);
        lostCount = lostCount == null ? 0 : lostCount;

        return count == null ? 0 : count - lostCount;
    }

    /**
     * 玖币分页，主要是后台管理用
     *
     * @param query query
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsQuery>
     * @author Aison
     * @date 2018/7/11 15:38
     */
    @Override
    public MyPage<CoinsQuery> coinsList(CoinsQuery query) {

        if (Biz.isEmpty(query.getNickNameLike())) {
            query.setNickNameLike(null);
        }
        if (Biz.isEmpty(query.getUserType())) {
            query.setUserType(null);
        }
        if (Biz.isEmpty(query.getCreateTimeBegin())) {
            query.setCreateTimeBegin(null);
        }
        if (Biz.isEmpty(query.getCreateTimeEnd())) {
            query.setCreateTimeEnd(null);
        }

        query.setQueryStore (true);
        List<CoinsQuery> coinsList = coinsMapper.selectCoinsWithMember(query);
        if (coinsList.size() == 0) {
            return new MyPage<>(new ArrayList<>());
        }
        Set<Long> coinsIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();

        coinsList.forEach(action -> {
            coinsIds.add(action.getId());
            userIds.add(action.getUserId());
        });
        Map<String, Object> param = new HashMap<>();
        param.put("coinsIds", coinsIds);
        Set<Integer> statuses = new HashSet<>();
        statuses.add(CoinsDetailStatusEnum.WAIT_IN.getCode());
        statuses.add(CoinsDetailStatusEnum.EXPIRE.getCode());
        statuses.add(CoinsDetailStatusEnum.LOSE.getCode());
        statuses.add(CoinsDetailStatusEnum.IN.getCode());
        param.put("statuses", statuses);
        Map<String, CoinsQuery> queryMap = coinsMapper.selectDetailSumGroup(param);


        // 获取邀请数
        Map<Long, Long> countMap = shareService.inviteCountMap(userIds);

        coinsList.forEach(action -> {
            //待入账的
            CoinsQuery wait = queryMap.get(action.getId() + "_" + CoinsDetailStatusEnum.WAIT_IN.getCode());
            //到期的
            CoinsQuery expire = queryMap.get(action.getId() + "_" + CoinsDetailStatusEnum.EXPIRE.getCode());
            //已失效的
            CoinsQuery lost = queryMap.get(action.getId() + "_" + CoinsDetailStatusEnum.LOSE.getCode());
            // 所有已入账的
            CoinsQuery allIn = queryMap.get(action.getId()+"_"+CoinsDetailStatusEnum.IN.getCode());

            Long count = countMap.get(action.getUserId());

            Long lostCount = lost == null ? 0L : lost.getCount();

            action.setInviteCount(count == null ? 0 : count);
            action.setWaitInCount(wait == null ? 0 : wait.getCount() - lostCount );
            action.setExpireCount(expire == null ? 0 : expire.getCount());
            action.setLostCount(lost == null ? 0 : lost.getCount());
            action.setAllIn(allIn.getCount());

        });

        return new MyPage<>(coinsList);
    }


    /**
     * 进出帐明细 调用的时候需要传递 userId
     *
     * @param query    query
     * @param userId   用户id
     * @param userType 用户类型
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsDetail>
     * @author Aison
     * @date 2018/7/12 9:07
     */
    @Override
    public MyPage<CoinsDetailQuery> coinsDetailList(CoinsDetailQuery query, Long userId, Integer userType) {

        Declare.notNull(GlobalsEnums.ACCOUNT_USER_ID_IS_NULL, userId, userType);
        CoinsQuery coinsQuery = new CoinsQuery(userId, userType);
        Coins coins = coinsMapper.selectOne(coinsQuery);
        Declare.isFailed(coins == null, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);

        query.setCoinsId(coins.getId());
        query.setOrderBy("create_time desc");
        List<CoinsDetail> coinsDetails = coinsDetailMapper.selectList (query);
        return MyPage.copy2Child (coinsDetails, CoinsDetailQuery.class,
                (source, target) -> {
                    String statusName = CoinsDetailStatusEnum.getStatusName(source.getStatus());
                    target.setStatusName (statusName);
                    target.setDetail (source.getDescription ());
                    target.setCreateTimeStr (Biz.formatDate (new Date (source.getCreateTime ()), "yyyy-MM-dd HH:mm:ss"));
                }
        );
    }


    /**
     * 初始化玖币账户
     *
     * @param coinsVo coinsVo
     * @author Aison
     * @date 2018/7/5 16:21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Coins initCoinsCount(CoinsVo coinsVo) {

        Long userId = coinsVo.getUserId();
        Integer userType = coinsVo.getAccountType().getCode();
        CoinsQuery query = new CoinsQuery(userId, userType);
        Coins coins = coinsMapper.selectOne(query);
        if (coins == null) {
            coins = new Coins();
            coins.setUserId(userId);
            coins.setUserType(userType);
            coins.setCreateTime(new Date());
            coins.setTotalCoins(coinsVo.getInitCount());
            coins.setAliveCoins(coinsVo.getInitCount());
            coins.setCashOutCoins(0L);
            coins.setCashOutRmb(BigDecimal.ZERO);
            int rs = coinsMapper.insertSelective(coins);
            Declare.isFailed(rs == 0, GlobalsEnums.COINS_ACCOUNT_INIT_FAILED);

            // 操作日志
            CoinsLog log = new CoinsLog();
            log.setBeforeAvliedCoins(0L);
            log.setBeforeTotalCoins(0L);
            log.setBeforeUnavliedCoins(0L);
            log.setAfterAvliedCoins(0L);
            log.setAfterTotalCoins(0L);
            log.setAfterUnavliedCoins(0L);
            log.setCoinsId(coins.getId());
            log.setCoinsDetailId(-1L);
            log.setOptUserId(coinsVo.getOptUser() == null ? -1L : coinsVo.getOptUser().getId().longValue());
            log.setCount(0L);
            log.setCreateTime(System.currentTimeMillis());
            log.setDetail("创建玖币账户");
            log.setStatus(-1);
            log.setType(-1);
            log.setInOut(-1);
            int logRs = coinsLogMapper.insertSelective(log);
            Declare.isFailed(logRs == 0, GlobalsEnums.COINS_ACCOUNT_INIT_FAILED);
        }
        return coins;
    }

    /**
     * 资金加减总入口 根据操作明细来 没有的操作明细将会报错
     *
     * @param coinsVo coinsVo
     * @author Aison
     * @date 2018/7/5 15:14
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void acceptCoins(CoinsVo coinsVo) {


        // 获取操作类型
        CoinsDetail coinsDetail = coinsVo.getCoinsDetail();
        Long userId = coinsVo.getUserId();
        Integer userType = coinsVo.getAccountType().getCode();
        Declare.notNull(GlobalsEnums.ACCOUNT_DETAIL_IS_NULL, coinsDetail, userId, userType);
        // 只有有玖币且玖币数量大于0的时候才能做此操作
        Declare.isFailed(Biz.isEmpty(coinsDetail.getCount()) || coinsDetail.getCount()==0 ||
                coinsDetail.getCount()<0 ,GlobalsEnums.COINS_COUNT_ERROR);

        // 查询玖币
        CoinsQuery query = new CoinsQuery(userId, userType);
        Coins coins = coinsMapper.selectOne(query);
        // 是否要创建一个账户
        if (coinsVo.isCreateAccount()) {
            if (coins == null) {
                initCoinsCount(coinsVo);
            }
        } else {
            Declare.isFailed(coins == null, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);
        }
        // 锁定账户信息
        int lock = coinsMapper.updateVersion(userId, userType);
        Declare.isFailed(lock == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
        // 锁定后再查询出此条记录
        coins = coinsMapper.selectOne(query);

        // 类型: 1 分享进账,2 邀请者购买进账, 3 被邀请者购买进账 , 50 兑换出账, 51 提现出账
        Integer detailType = coinsDetail.getType();
        // 记录操作之前的值
        CoinsLog log = CoinsVo.coinsLog(coins, true, null);

        // 分享入账
        if (CoinsDetailTypeEnum.SHARE_IN.isThis(detailType)) {
            shareIn(coinsVo, coins);
        } else if (CoinsDetailTypeEnum.INVITE_BUY_IN.isThis(detailType)) {
            inviteBuyIn(coinsVo, coins);
        } else if (CoinsDetailTypeEnum.INVITEE_BUY_IN.isThis(detailType)) {
            inviteBuyIn(coinsVo, coins);
        } else if (CoinsDetailTypeEnum.EXCHANGE_OUT.isThis(detailType)) {
            //exchange(coinsVo, coins);
        } else if (CoinsDetailTypeEnum.CASH_OUT.isThis(detailType)) {
            exchange(coinsVo, coins);
        } else {
            // 错误的入账类型 抛出异常
            throw BizException.instance(GlobalsEnums.WRONG_DETAIL_TYPE);
        }
        // 添加明细
        coinsDetail = addDetail(coinsVo, coins);
        // 记录操作之后的值
        // 查询出操作后的值
        coins = coinsMapper.selectOne(query);
        log = CoinsVo.coinsLog(coins, false, log);
        log.setCoinsId(coins.getId());
        log.setCoinsDetailId(coinsDetail.getId());
        log.setOptUserId(coinsVo.getOptUser() == null ? -1L : coinsVo.getOptUser().getId().longValue());
        log.setCount(coinsDetail.getCount());
        log.setDetail(coinsDetail.getDescription());
        log.setStatus(coinsDetail.getStatus());
        log.setType(coinsDetail.getType());
        log.setInOut(coinsDetail.getInOut());
        log.setCreateTime(System.currentTimeMillis());
        int logRs = coinsLogMapper.insertSelective(log);
        Declare.isFailed(logRs == 0, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);
    }




    /**
     * 添加明细
     *
     * @param coinsVo coinsVo
     * @param coins   coins
     * @return com.jiuy.rb.model.account.CoinsDetail
     * @author Aison
     * @date 2018/7/5 15:07
     */
    private CoinsDetail addDetail(CoinsVo coinsVo, Coins coins) {
        CoinsDetail detail = coinsVo.getCoinsDetail();
        detail.setCoinsId(coins.getId());
        detail.setCreateTime(System.currentTimeMillis());
        if(detail.getId()!=null) {
           coinsDetailMapper.updateByPrimaryKeySelective(detail);
        } else {
            int detailRs = coinsDetailMapper.insertSelective(detail);
            Declare.isFailed(detailRs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
        }
        return detail;
    }


    /**
     * 分享入账直接可用
     *
     * @param coinsVo coinsVo
     * @author Aison
     * @date 2018/7/5 14:16
     */
    private void shareIn(CoinsVo coinsVo, Coins coins) {
        CoinsUpVo upVo = upVo(coinsVo, true);
        coinsVo.getCoinsDetail().setStatus(CoinsDetailStatusEnum.IN.getCode());
        // 添加可用玖币
        int rs = coinsMapper.modifyAliveCoins(upVo);
        Declare.isFailed(rs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
    }

    /**
     * 被邀请者购买的时候 邀请者入账/被邀请者入账
     * 此时入账是入账到不可用账户里面
     *
     * @param coinsVo coinsVo
     * @param coins   coins
     * @author Aison
     * @date 2018/7/5 14:45
     */
    private void inviteBuyIn(CoinsVo coinsVo, Coins coins) {

        CoinsUpVo upVo = upVo(coinsVo, true);
        coinsVo.getCoinsDetail().setStatus(CoinsDetailStatusEnum.WAIT_IN.getCode());
        // 添加可用玖币
        int rs = coinsMapper.modifyUnAliveCoins(upVo);
        Declare.isFailed(rs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
    }


    /**
     * 提现操作
     *
     * @param coinsVo coinsVo
     * @param coins   coins
     * @author Aison
     * @date 2018/7/5 14:50
     */
    private void exchange(CoinsVo coinsVo, Coins coins) {

        CoinsDetail detail = coinsVo.getCoinsDetail();

        CoinsUpVo upVo = upVo(coinsVo, false);
        coinsVo.getCoinsDetail().setStatus(CoinsDetailStatusEnum.CASH_WAIT.getCode());
        // 从可用转到不可用
        int rs = coinsMapper.moveUnAlive2Live(upVo);
        Declare.isFailed(rs == 0, GlobalsEnums.COINS_NOT_ENOUGH);

        Long count = detail.getCount();
        // 判断最小提现
        DataDictionaryRb min = dataDictionaryService.getByCode(ConstMy.MIN_CASH_COINS_CODE, ConstMy.MIN_CASH_COINS_GROUP);
        Long minCoins = Long.valueOf(min.getVal());
        Declare.isFailed(minCoins>count,GlobalsEnums.MIN_CASH_COINS_COUNT,":"+minCoins);

        //查询提现比例
        DataDictionaryRb dd = dataDictionaryService.getByCode(ConstMy.CASH_OUT_CODE, ConstMy.CASH_OUT_GROUP);
        // 设置note
        detail.setNote("玖币提现，当前玖币与人名币的转换率为"+dd.getVal()+":1");
        //计算数量
        BigDecimal rmb = new BigDecimal(count).divide(new BigDecimal(dd.getVal()),2,BigDecimal.ROUND_HALF_UP);
        // 判断是否超出了提现rmb的总数
        BigDecimal left = leftCashOutRmb(coins.getUserId(),AccountTypeEnum.WXA_USER);
        Declare.isFailed(rmb.compareTo(left)>0,GlobalsEnums.CASH_RMB_IS_OUT);


        detail = addDetail(coinsVo,coins);
        //添加提现记录
        CoinsCashOut cashOut = new CoinsCashOut();
        cashOut.setCoinsId(coins.getId());
        cashOut.setCreateTime(new Date());
        cashOut.setCashCount(count);
        cashOut.setCashRmb(rmb);
        cashOut.setMemberId(coins.getUserId());
        cashOut.setCoinsDetailId(detail.getId());
        cashOut.setStatus(CashOutStatusEnum.WAI_OUT.getCode());
        cashOut.setLeftCashOutRmb(left);
        int cashRs = coinsCashOutMapper.insertSelective(cashOut);
        Declare.isFailed(cashRs == 0, GlobalsEnums.CASH_OUT_FAILED);

        // 添加累计提现 因为第一步锁住了 可以使用此种方式
        Coins coinsUp = new Coins();
        coinsUp.setCashOutCoins(coins.getCashOutCoins()+count);
        coinsUp.setCashOutRmb(rmb.add(coins.getCashOutRmb()));
        coinsUp.setId(coins.getId());
        int cashOutRs = coinsMapper.updateByPrimaryKeySelective(coinsUp);
        Declare.isFailed(cashOutRs == 0, GlobalsEnums.CASH_OUT_FAILED);
    }

    /**
     * 剩余可提现金额
     *
     * @param userId userId
     * @param accountTypeEnum accountTypeEnum
     * @author Aison
     * @date 2018/7/18 17:01
     * @return java.math.BigDecimal
     */
    @Override
    public BigDecimal leftCashOutRmb(Long userId,AccountTypeEnum accountTypeEnum) {

        Integer moth = Calendar.getInstance().get(Calendar.MONTH)+1;
        // 查询玖币账户
        CoinsQuery query = new CoinsQuery();
        query.setUserType(accountTypeEnum.getCode());
        query.setUserId(userId);
        Coins coins = coinsMapper.selectOne(query);
        if(coins==null) {
            return BigDecimal.ZERO;
        }
        DataDictionaryRb max = dataDictionaryService.getByCode(ConstMy.MAX_CASH_RMB_CODE, ConstMy.MAX_CASH_RMB_GROUP);
        // 如果不是本月的 则初始化为0
        if(!coins.getInitMoth().equals(moth)) {
            coins.setCashOutRmb(BigDecimal.ZERO);
            coins.setCashOutCoins(0L);
            coins.setInitMoth(moth);
            coinsMapper.updateByPrimaryKeySelective(coins);
            return new BigDecimal(max.getVal());
        } else {
            return new BigDecimal(max.getVal()).subtract(coins.getCashOutRmb());
        }
    }



    /**
     * 组装更新对象
     *
     * @param coinsVo coinsVo
     * @param isAdd   true是添加 false是扣减
     * @return com.jiuy.rb.model.account.CoinsUpVo
     * @author Aison
     * @date 2018/7/5 14:59
     */
    private CoinsUpVo upVo(CoinsVo coinsVo, boolean isAdd) {

        CoinsDetail coinsDetail = coinsVo.getCoinsDetail();
        Long count = coinsDetail.getCount();
        Long userId = coinsVo.getUserId();
        Integer userType = coinsVo.getAccountType().getCode();
        CoinsUpVo upVo = new CoinsUpVo();
        if (isAdd) {
            coinsDetail.setInOut(InOutEnum.IN.getCode());
        } else {
            coinsDetail.setInOut(InOutEnum.OUT.getCode());
        }
        upVo.setCount(isAdd ? count : 0 - count);
        upVo.setUserId(userId);
        upVo.setUserType(userType);
        return upVo;
    }


    /**
     * 将待入账的玖币转到已入账玖币
     *
     * @author Aison
     * @date 2018/7/18 11:04
     */
    @Override
    public void wait2in() {
        // 查询所有的
        Date data = Biz.dateStr2Date(Biz.formatDate(new Date(), "yyyy-MM-dd ") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        List<CoinsDetail> coinsDetails = coinsDetailMapper.selectReleaseOrderCoins(data.getTime());

        // 将对应的记录修改成已经入账  可用玖币增加 不可用玖币减少
        coinsDetails.forEach(action -> {
            try{
                releaseCoinsService.releaseOrder(action);
            }catch (Exception e) {
                logger.info(action.getId()+" 此条资金释放失败...请排查"+Biz.getFullException(e));
            }
        });
    }


    /**
     * 提现记录
     *
     * @param query query
     * @author Aison
     * @date 2018/7/19 9:37
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsCashOut>
     */
    @Override
    public MyPage<CoinsCashOutQuery> cashOutMyPage(CoinsCashOutQuery query) {

         List<CoinsCashOutQuery> list = coinsCashOutMapper.selectCashOutList(query);
         list.forEach(action-> action.setStatusName(CashOutStatusEnum.getStatusName(action.getStatus())));
         return  new MyPage<>(list);
    }





    private String transferUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 提现 逻辑是先调用微信的接口  因为微信的接口是幂等的..
     * 只要商户号不变和金额不变就可以
     *
     * 排队确认提现吧..临时方案 以后要改使用version
     *
     * @param cashOutId cashOutId
     * @param  agree true 同意提现  false 不同意提现
     * @author Aison
     * @date 2018/7/19 15:38
     *
     */
    @Override
    public synchronized void dealCashOut(Long cashOutId, boolean agree, UserSession userSession) {

        Declare.isFailed(cashOutId==null || cashOutId == 0 ,GlobalsEnums.PARAM_ERROR);
        CoinsCashOut cashOut = coinsCashOutMapper.selectByPrimaryKey(cashOutId);
        Declare.isFailed(cashOut==null || !CashOutStatusEnum.WAI_OUT.isThis(cashOut.getStatus()) ,GlobalsEnums.PARAM_ERROR);

        String wxaNo = cashOut.getWxaNo();
        wxaNo = Biz.isEmpty(wxaNo) ? Biz.getUuid() : wxaNo;

        // 调用之前先更新微信no 需要先更新 然后再调用微信的接口
        CoinsCashOut cashUp = new CoinsCashOut();
        cashUp.setId(cashOut.getId());
        cashUp.setWxaNo(wxaNo);
        cashUp.setUpdateTime(new Date());
        int rs = coinsCashOutMapper.updateByPrimaryKeySelective(cashUp);
        Declare.isFailed(rs==0,GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

        // 调用微信的接口
        Map<String,String> param = new HashMap<>(10);

        // 获取微信openid
        ShopMemberRbQuery query = new ShopMemberRbQuery();
        query.setId(cashOut.getMemberId());
        ShopMemberRb shopMember = shopMemberService.getShopMember(query);
        Declare.isFailed(shopMember==null,GlobalsEnums.CASH_OUT_FAILED);
        Long amount = cashOut.getCashRmb().multiply(new BigDecimal(100)).longValue();
        // 查询member
        StoreBusinessRb storeBusiness = storeBusinessRbMapper.selectByPrimaryKey(shopMember.getStoreId());
        // 查询微信支付配置信息
        WxaPayConfigQuery wcq = new WxaPayConfigQuery();
        wcq.setAppId(storeBusiness.getWxaAppId());
        WxaPayConfig wxaPayConfig = wxaPayConfigMapper.selectOne(wcq);
        // 商户账号 mch_appid 是
        param.put("mch_appid", wxaPayConfig.getAppId());
        // 商户号 mchid 是
        param.put("mchid",wxaPayConfig.getMchId());
        // 随机字符串 	nonce_str 是
        param.put("nonce_str",UUID.randomUUID().toString().replace("-",""));
        // 校验用户姓名选项	check_name 是
        param.put("check_name","NO_CHECK");
        // 收款用户姓名
        // 企业付款描述信息	desc	是
        param.put("desc","玖币提现");
        // 用户openid	openid 是
        param.put("openid",shopMember.getBindWeixin());
        // 金额	amount	是 单位分
        param.put("amount",amount.toString());
        // Ip地址	spbill_create_ip	是
        param.put("spbill_create_ip",userSession.getRequestIp());
        // 商户订单号	partner_trade_no 是
        param.put("partner_trade_no",wxaNo);

        String sign = PaymentBiz.createSign(param, wxaPayConfig.getPaternerKey());
        param.put("sign",sign);
        // （apiclient_cert.p12）证书存放路径
        String cerPath = wxaPayConfig.getCertPath();

        String xml = PaymentBiz.toXml(param);
        System.out.println(xml);
        String xmlResult = HttpUtils.postSSL(transferUrl, xml, cerPath, wxaPayConfig.getMchId());
        Map<String,String> resMap = PaymentBiz.xmlToMap(xmlResult);

        // 处理本地的逻辑.. 此方法有事务..
        releaseCoinsService.doCashOut(cashOut,userSession,resMap);
    }

}
