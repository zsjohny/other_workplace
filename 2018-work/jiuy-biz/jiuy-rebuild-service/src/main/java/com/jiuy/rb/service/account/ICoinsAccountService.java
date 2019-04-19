package com.jiuy.rb.service.account;

import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.AccountTypeEnum;
import com.jiuy.rb.model.account.*;

import java.math.BigDecimal;

/**
 * 玖币账户接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 9:42
 * @Copyright 玖远网络
 */
public interface ICoinsAccountService {

    /**
     * 资金加减总入口 根据操作明细来 没有的操作明细将会报错
     *
     * @param coinsVo coinsVo
     * @author Aison
     * @date 2018/7/5 15:14
     *
     */
     void acceptCoins(CoinsVo coinsVo );

    /**
     * 剩余可提现金额
     *
     * @param userId userId
     * @param accountTypeEnum accountTypeEnum
     * @author Aison
     * @date 2018/7/18 17:01
     * @return java.math.BigDecimal
     */
     BigDecimal leftCashOutRmb(Long userId, AccountTypeEnum accountTypeEnum);

    /**
     * 初始化玖币账户
     *
     * @param coinsVo coinsVo
     * @author Aison
     * @date 2018/7/5 16:21
     * @return Coins
     */
    Coins initCoinsCount(CoinsVo coinsVo);

    /**
     * 查询某个用户的玖币账户
     *
     * @param userId 用户id
     * @param accountTypeEnum 用户类型
     * @author Aison
     * @date 2018/7/5 18:47
     * @return com.jiuy.rb.model.account.Coins
     */
    Coins getCoinsAccount(Long userId, AccountTypeEnum accountTypeEnum);


    /**
     * 待入账玖币数量
     *
     * @param userId 用户id
     * @param accountTypeEnum 用户类型
     * @author Aison
     * @date 2018/7/5 18:47
     * @return com.jiuy.rb.model.account.Coins
     */
    Long waitInCoins(Long userId, AccountTypeEnum accountTypeEnum);

    /**
     * 玖币分页，主要是后台管理用
     *
     * @param query query
     * @author Aison
     * @date 2018/7/11 15:38
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsQuery>
     */
    MyPage<CoinsQuery> coinsList(CoinsQuery query);

    /**
     * 进出帐明细 调用的时候需要传递 userId
     *
     * @param query query
     * @param userId 用户id
     * @param userType 用户类型
     * @author Aison
     * @date 2018/7/12 9:07
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsDetail>
     */
    MyPage<CoinsDetailQuery> coinsDetailList(CoinsDetailQuery query,Long userId,Integer userType) ;



    /**
     * 将待入账的玖币转到已入账玖币
     *
     * @author Aison
     * @date 2018/7/18 11:04
     */
    void wait2in();


    /**
     * 提现记录
     *
     * @param query query
     * @author Aison
     * @date 2018/7/19 9:37
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.account.CoinsCashOut>
     */
    MyPage<CoinsCashOutQuery> cashOutMyPage(CoinsCashOutQuery query);


    /**
     * 提现
     *
     * @param cashOutId cashOutId
     * @param  agree true 同意提现  false 不同意提现
     * @author Aison
     * @date 2018/7/19 15:38
     *
     */
    void dealCashOut(Long cashOutId, boolean agree, UserSession userSession);

}
