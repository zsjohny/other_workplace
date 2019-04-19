package com.jiuy.rb.model.account;

import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.AccountTypeEnum;
import com.jiuy.rb.enums.CoinsDetailTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 *
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 13:58
 * @Copyright 玖远网络
 */
@Data
public class CoinsVo {

    /**
     *
     * @param detailType 入账类型
     * @param orderNo 订单号 如果不是订单入账可不填
     * @param count 出入账多少 正数
     * @param deadline 到期时间
     * @param targetId 目标id
     * @author Aison
     * @date 2018/7/5 15:38
     * @return com.jiuy.rb.model.account.CoinsVo
     */
    public static CoinsVo instance(CoinsDetailTypeEnum detailType, String orderNo, Long count, Date deadline,Long targetId,String description,String note) {


        CoinsVo coinsVo = new CoinsVo();
        CoinsDetail coinsDetail = new CoinsDetail();
        coinsDetail.setType(detailType.getCode());
        coinsDetail.setOrderNo(orderNo);
        coinsDetail.setCount(count);
        coinsDetail.setDeadline(deadline);
        coinsDetail.setTargetId(targetId);
        coinsDetail.setNote(note);
        coinsDetail.setDescription(description);
        coinsVo.setCoinsDetail(coinsDetail);
        return coinsVo;
    }

    /**
     *
     * @param detailType 入账类型
     * @param orderNo 订单号 如果不是订单入账可不填
     * @param count 出入账多少 正数
     * @param deadline 到期时间
     * @param targetId 目标id
     * @param optUser 操作用户
     * @param userId 账户用户id
     * @param userType 用户类型
     * @author Aison
     * @date 2018/7/5 15:38
     * @return com.jiuy.rb.model.account.CoinsVo
     */
    public static CoinsVo instance(CoinsDetailTypeEnum detailType, String orderNo, Long count, Date deadline,Long targetId,String description,String note,UserSession optUser,Long userId,AccountTypeEnum userType) {


        CoinsVo coinsVo = new CoinsVo();
        CoinsDetail coinsDetail = new CoinsDetail();
        coinsDetail.setType(detailType.getCode());
        coinsDetail.setOrderNo(orderNo);
        coinsDetail.setCount(count);
        coinsDetail.setDeadline(deadline);
        coinsDetail.setTargetId(targetId);
        coinsDetail.setDescription(description);
        coinsDetail.setNote(note);
        coinsVo.setCoinsDetail(coinsDetail);
        coinsVo.setUserId(userId);
        coinsVo.setOptUser(optUser);
        coinsVo.setAccountType(userType);
        return coinsVo;
    }

    /**
     * 是否创建一个账户
     *
     * @param flag flag
     * @author Aison
     * @date 2018/7/5 16:30
     * @return com.jiuy.rb.model.account.CoinsVo
     */
    public CoinsVo create(boolean flag) {

        this.createAccount = flag;
        return this;
    }

    private CoinsDetail coinsDetail;

    private Long userId;

    private AccountTypeEnum accountType;

    private UserSession optUser;

    /**
     * 测试用的初始化账户的时候加多少钱
     */
    private Long initCount = 0L;

    private CoinsVo initCount(Long count) {

        this.initCount = count;
        return this;
    }

    /**
     * 如果没有账户是否创建一个 默认为false
     */
    private boolean createAccount = false;

    /**
     * 返回一个操作日志
     *
     * @param coins    coins
     * @param isBefore 是否是之前
     * @return com.jiuy.rb.model.account.CoinsLog
     * @author Aison
     * @date 2018/7/5 14:32
     */
    public static CoinsLog coinsLog(Coins coins, boolean isBefore, CoinsLog log) {
        log = log == null ? new CoinsLog() : log;
        if (isBefore) {
            log.setBeforeAvliedCoins(coins.getAliveCoins());
            log.setBeforeTotalCoins(coins.getTotalCoins());
            log.setBeforeUnavliedCoins(coins.getUnalivedCoins());
        } else {
            log.setAfterAvliedCoins(coins.getAliveCoins());
            log.setAfterTotalCoins(coins.getTotalCoins());
            log.setAfterUnavliedCoins(coins.getUnalivedCoins());
        }
        return log;
    }
}
