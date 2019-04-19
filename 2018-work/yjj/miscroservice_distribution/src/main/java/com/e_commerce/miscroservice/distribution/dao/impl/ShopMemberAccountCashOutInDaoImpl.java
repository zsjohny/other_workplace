package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusDetailEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.distribution.mapper.ShopMemberAccountCashOutInMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum.NEW_USER_INVITEE;
import static com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum.SHARE;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 20:02
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberAccountCashOutInDaoImpl implements ShopMemberAccountCashOutInDao{

    @Autowired
    private ShopMemberAccountCashOutInMapper shopMemberAccountCashOutInMapper;

    @Override
    public int insertSelective(ShopMemberAccountCashOutIn cashOutIn) {
//        return MybatisOperaterUtil.getInstance().save(cashOutIn);
        return shopMemberAccountCashOutInMapper.insertSelective (cashOutIn);
    }

    /**
     * 今日总进账金额
     *
     * @param userId
     * @return
     */
    @Override
    public Double findTodayCountMoney(Long userId) {
        return shopMemberAccountCashOutInMapper.findTodayCountMoney(userId);
    }

    /**
     * 今日佣金进账
     *
     * @param userId
     * @return
     */
    @Override
    public Double findCommissionTodayCountMoney(Long userId) {
        return shopMemberAccountCashOutInMapper.findCommissionTodayCountMoney(userId);
    }

    /**
     * 今日管理奖 进账
     *
     * @param userId
     * @return
     */
    @Override
    public Double findManageTodayCountMoney(Long userId) {
        return shopMemberAccountCashOutInMapper.findManageTodayCountMoney(userId);
    }

    /**
     * 账单信息
     *
     *
     * @param choose
     * @param type 0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账," +
     *                     "10.分销商的团队收益入账,11.合伙人的团队收益入账," +
     *                     "20.签到,21.签到阶段奖励," +
     *                     "50.提现
     * @param inOutType 1 进账 2出账
     * @param status 状态 0:失效,1:待结算,2:已结算,3：已冻结
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<ShopMemberAccountCashOutIn> findBillDetails(Integer choose, Integer type, Integer inOutType, Integer status, Long userId, Integer page) {
//      type  0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,10.分销商的团队收益入账,
// 11.合伙人的团队收益入账,20.签到,21.签到阶段奖励,30.订单取消,31.订单抵扣,"50.提现-总额,51.提现-佣金,52提现-管理金
//        "in_out_type", commit = "1:进账,2:出账",
//        "status", commit = "1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态)"
//        0订单佣金,1管理奖金,2提现,3 签到 4,订单抵扣,5订单取消,
        Integer[] types = null;
        //现金所有数组
        Integer[] moneyTypes = {0,1,2,10,11,50,30,40,41};
        //金币所有数组
        Integer[] coinTypes = {0,1,2,10,11,30,31,20,21,40,41};
        //管理奖类型
        Integer[] manageTypes = {10,11};
        //订单佣金类型
        Integer[] orderTypes = {0,1,2};
        //提现
        Integer[] cashMoneyTypes = {50};
        //6签到
        Integer[] signTypes = {20,21};
        if (choose==0){
            types=moneyTypes;
        }else {
            types=coinTypes;
        }
        if (type!=null&&type==0){
            types = orderTypes;
        }else if (type!=null&&type==1){
            types = manageTypes;
        }else if (type!=null&&type==2){
            types = cashMoneyTypes;
        }else if (type!=null&&type==3){
            types=signTypes;
        }
        else if (NEW_USER_INVITEE.isThis (type)) {
            //邀新,分享,归为一类
            types= new Integer[]{NEW_USER_INVITEE.getCode (), SHARE.getCode ()};
        }
        MybatisSqlWhereBuild sqlWhereBuild = new MybatisSqlWhereBuild(ShopMemberAccountCashOutIn.class).in(ShopMemberAccountCashOutIn::getType,types)
                .eq(ShopMemberAccountCashOutIn::getUserId,userId);
        if (inOutType!=null){
            sqlWhereBuild .eq(ShopMemberAccountCashOutIn::getInOutType,inOutType);
        } if (status!=null){
            sqlWhereBuild.eq(ShopMemberAccountCashOutIn::getStatus,status);
        }


        // 0，现金1，金币
        if (choose == 0) {
            sqlWhereBuild.gt (ShopMemberAccountCashOutIn::getOperCash, 0);
        }
        else if (choose == 1) {
            sqlWhereBuild.gt (ShopMemberAccountCashOutIn::getOperGoldCoin, 0);
        }

        sqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopMemberAccountCashOutIn::getCreateTime).page(page,10);
        List<ShopMemberAccountCashOutIn> list = MybatisOperaterUtil.getInstance().finAll(
                new ShopMemberAccountCashOutIn(), sqlWhereBuild);


        list.forEach (cashOutIn->{
            if (NEW_USER_INVITEE.isThis (cashOutIn.getType ())) {
                //邀新算到分享
                cashOutIn.setType (CashOutInTypeEnum.SHARE.getCode ());
            }
        });

        return list;
    }

    /**
     * 今日金币进账
     *
     * @param userId
     * @return
     */
    @Override
    public Double findTodayGoldCountMoney(Long userId) {
        return shopMemberAccountCashOutInMapper.findTodayGoldCountMoney(userId);
    }

    @Override
    public List<ShopMemberAccountCashOutIn> findLogList(Integer page, Long userId) {
        List<ShopMemberAccountCashOutIn> list = MybatisOperaterUtil.getInstance().finAll(new ShopMemberAccountCashOutIn(),
                new MybatisSqlWhereBuild(ShopMemberAccountCashOutIn.class)
                        .eq(ShopMemberAccountCashOutIn::getUserId,userId)
                        .gt(ShopMemberAccountCashOutIn::getOperGoldCoin,0)
                        .orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopMemberAccountCashOutIn::getOperTime)
                        .page(page,10));
        return list;
    }

    @Override
    public List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(Long userId) {
        return shopMemberAccountCashOutInMapper.findPeriodicalPrizeMonthLog(userId);
    }






    /**
     * 将结算条件设为成功
     *
     * @param outInId outInId
     * @return int
     * @author Charlie
     * @date 2018/10/20 14:00
     */
    @Override
    public int conditionIsOk(Long outInId) {
        if (outInId == null) {
            return 0;
        }
        ShopMemberAccountCashOutIn upd = new ShopMemberAccountCashOutIn ();
        upd.setDetailStatus (CashOutInStatusDetailEnum.WAIT_GRANT.getCode ());
        return MybatisOperaterUtil.getInstance ().update (
                upd,
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getId, outInId)
                        .eq (ShopMemberAccountCashOutIn::getDetailStatus, CashOutInStatusDetailEnum.WAIT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
        );
    }




    /**
     *
     * @param orderNo orderNo
     * @param code code
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/27 9:53
     */
    @Override
    public List<ShopMemberAccountCashOutIn> findByOrderNo(String orderNo, int code) {
        return MybatisOperaterUtil.getInstance().finAll(new ShopMemberAccountCashOutIn(),new MybatisSqlWhereBuild(ShopMemberAccountCashOutIn.class).eq(ShopMemberAccountCashOutIn::getOrderNo,orderNo).eq(ShopMemberAccountCashOutIn::getDelStatus,StateEnum.NORMAL));
    }

    @Override
    public int lock(Long cashOutId) {
        ShopMemberAccountCashOutIn updInfo = new ShopMemberAccountCashOutIn ();
        updInfo.setId (cashOutId);
        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getId, cashOutId)
        );
    }





    /**
     * 查找提现露水
     *
     * @param cashOutId cashOutId
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/11/19 10:33
     */

    @Override
    public ShopMemberAccountCashOutIn findCashOutById(Long cashOutId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getId, cashOutId)
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
                        //50.提现-总额,51.提现-佣金,52提现-管理金
                        .eq (ShopMemberAccountCashOutIn::getType, 50)
        );
    }


    /**
     * 历史分享收益
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 18:08
     */
    @Override
    public Map<String, BigDecimal> findHistoryShareEarnings(Long userId) {
        return shopMemberAccountCashOutInMapper.findHistoryShareEarnings (userId);
    }



    /**
     * 平台最近五条分享
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/15 11:50
     */
    @Override
    public List<Map<String, Object>> recentlyPlatformShares() {
        return shopMemberAccountCashOutInMapper.recentlyPlatformShares ();
    }
}
