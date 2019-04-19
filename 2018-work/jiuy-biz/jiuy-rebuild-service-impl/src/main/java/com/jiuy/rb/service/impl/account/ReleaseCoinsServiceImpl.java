package com.jiuy.rb.service.impl.account;

import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.account.CoinsCashOutMapper;
import com.jiuy.rb.mapper.account.CoinsDetailMapper;
import com.jiuy.rb.mapper.account.CoinsLogMapper;
import com.jiuy.rb.mapper.account.CoinsMapper;
import com.jiuy.rb.model.account.*;
import com.jiuy.rb.model.order.ShopMemberOrderRb;
import com.jiuy.rb.service.account.IReleaseCoinsService;
import com.jiuy.rb.service.order.IMemberOrderService;
import com.jiuy.rb.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/18 14:04
 * @Copyright 玖远网络
 */
@Service("releaseCoinsService")
public class ReleaseCoinsServiceImpl implements IReleaseCoinsService {


    @Autowired
    private CoinsMapper coinsMapper;

    @Autowired
    private CoinsDetailMapper coinsDetailMapper;

    @Autowired
    private CoinsLogMapper coinsLogMapper;

    @Resource(name = "memberOrderService")
    private IMemberOrderService memberOrderService;

    @Resource(name = "coinsCashOutMapper")
    private CoinsCashOutMapper coinsCashOutMapper;

    /**
     * 将购买玖币从待入账到已入账
     *
     * @param coinsDetail coinsDetail
     * @author Aison
     * @date 2018/7/18 14:12
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void releaseOrder(CoinsDetail coinsDetail) {


        //判断订单状态
        ShopMemberOrderRb order = memberOrderService.getOrderByOrderNo(coinsDetail.getOrderNo());
        Declare.isFailed(order == null, GlobalsEnums.ORDER_NOT_FOUND);
        Integer orderStatus = order.getOrderStatus();

        // 查询玖币账户
        Coins coins = coinsMapper.selectByPrimaryKey(coinsDetail.getCoinsId());
        Declare.isFailed(coins == null, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);
        int rs = coinsMapper.updateVersion(coins.getUserId(), coins.getUserType());

        CoinsLog log =  CoinsVo.coinsLog(coins,true,null);
        Declare.isFailed(rs == 0, GlobalsEnums.ACCOUNT_LOCK_FAILED);

        CoinsDetailStatusEnum statusEnum = CoinsDetailStatusEnum.IN;
        // 切割出详情
        String memo = coinsDetail.getDescription();
        memo = memo == null ? "" : memo;
        memo = memo.substring(memo.indexOf(":")+1);
        String detail = "订单完成:"+ memo;
        Integer inOut = 1;
        // 如果是退款中 或者订单关闭则 修改当前资金明细状态到已失效
        long curr = System.currentTimeMillis ();
        if(MemberOrderStatusEnum.REFUNDING.isThis(orderStatus) || MemberOrderStatusEnum.CLOSED.isThis(orderStatus) ){

            // 将此条数据修改为失效的状态
            CoinsDetail detailUp = new CoinsDetail();
            detailUp.setId(coinsDetail.getId());
            detailUp.setIsLost(1);
            int lostRs = coinsDetailMapper.updateByPrimaryKeySelective(detailUp);
            Declare.isFailed(lostRs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

            //新增一个失效记录
            coinsDetail.setCoinsDetailId (coinsDetail.getId ());
            coinsDetail.setId (null);
            coinsDetail.setInOut (InOutEnum.OUT.getCode ());
            coinsDetail.setStatus (CoinsDetailStatusEnum.LOSE.getCode ());
            coinsDetail.setCreateTime (curr);
            coinsDetail.setUpdateTime (curr);
            String description = MemberOrderStatusEnum.REFUNDING.isThis (orderStatus) ? "订单退款中 ": "订单关闭 ";
            description += memo;
            coinsDetail.setDescription (description);
            coinsDetail.setNote (description);
            int iRc = coinsDetailMapper.insertSelective (coinsDetail);
            Declare.state (iRc==1, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);


            // 从账户中扣除玖币
            CoinsUpVo upVo = new CoinsUpVo();
            upVo.setUserType(coins.getUserType());
            upVo.setUserId(coins.getUserId());
            upVo.setCount(0 - coinsDetail.getCount());
            int subCoins = coinsMapper.modifyUnAliveCoins(upVo);
            Declare.isFailed(subCoins == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
            detail = "订单关闭:"+memo;
            statusEnum = CoinsDetailStatusEnum.LOSE;
            inOut = 0;
        } else {
            // 从不可用转移到可用
            CoinsUpVo upVo = new CoinsUpVo();
            upVo.setUserType(coins.getUserType());
            upVo.setUserId(coins.getUserId());
            upVo.setCount(coinsDetail.getCount());
            int moveRs = coinsMapper.moveUnAlive2Live(upVo);
            Declare.isFailed(moveRs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
            int statusRs = coinsDetailMapper.updateCoinsDetail(coinsDetail.getId(), CoinsDetailStatusEnum.WAIT_IN.getCode(), CoinsDetailStatusEnum.IN.getCode());
            Declare.isFailed(statusRs == 0, GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);
        }
        coins = coinsMapper.selectByPrimaryKey(coinsDetail.getCoinsId());
        log =  CoinsVo.coinsLog(coins,false,log);
        log.setCoinsId(coins.getId());
        log.setCoinsDetailId(coinsDetail.getId());
        log.setOptUserId(-1L);
        log.setCount(coinsDetail.getCount());
        log.setDetail(detail);
        log.setStatus(statusEnum.getCode());
        log.setType(coinsDetail.getType());
        log.setInOut(inOut);
        log.setCreateTime(curr);
        int logRs = coinsLogMapper.insertSelective(log);
        Declare.isFailed(logRs == 0, GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);
    }



    /**
     * 提现本地逻辑
     *
     * @param cashOut cashOut
     * @param userSession  userSession
     * @param rsMap rsMap
     * @author Aison
     * @date 2018/7/23 11:32
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> doCashOut(CoinsCashOut cashOut,  UserSession userSession,Map<String,String> rsMap) {



        // 锁定资金账户
        Coins coins = coinsMapper.selectByIdForUpdate(cashOut.getCoinsId());
        Declare.isFailed(coins==null,GlobalsEnums.ACCOUNT_LOCK_FAILED);
        CoinsLog log =  CoinsVo.coinsLog(coins,true,null);

        String detail ;
        CoinsCashOut cashOutUp = new CoinsCashOut();

        String resultCode = rsMap.get("result_code");
        boolean isSuccess = "SUCCESS".equalsIgnoreCase(resultCode);
        // 失败不操作
        if(!isSuccess) {
            //修改提现记录状态
            cashOutUp.setStatus(CashOutStatusEnum.FAILED.getCode());

            // 修改账户金额
            CoinsUpVo coinsUpVo = new CoinsUpVo();
            coinsUpVo.setCount(cashOut.getCashCount());
            coinsUpVo.setUserId(coins.getUserId());
            coinsUpVo.setUserType(coins.getUserType());
            int failedRs = coinsMapper.moveUnAlive2Live(coinsUpVo);
            Declare.isFailed(failedRs==0,GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

            // 将累计的提现金额扣减掉
            Coins coinsUp = new Coins();
            coinsUp.setId(coins.getId());
            coinsUp.setCashOutRmb(coins.getCashOutRmb().subtract(cashOut.getCashRmb()));
            coinsUp.setCashOutCoins(coins.getCashOutCoins()-cashOut.getCashCount());
            int cashRs = coinsMapper.updateByPrimaryKeySelective(coinsUp);
            Declare.isFailed(cashRs==0,GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

            // 删除此条资金明细记录
            int detailRs = coinsDetailMapper.updateCoinsDetail(cashOut.getCoinsDetailId(),CoinsDetailStatusEnum.CASH_WAIT.getCode(),CoinsDetailStatusEnum.CASH_FAILED.getCode());
            Declare.isFailed(detailRs==0,GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

            detail = "提现操作失败，修改资金账户";
        } else {
            // 提现成功

            //修改提现记录状态
            cashOutUp.setStatus(CashOutStatusEnum.SUCCESS.getCode());

            // 扣减不可用库存  扣减总库存
            CoinsUpVo coinsUpVo = new CoinsUpVo();
            coinsUpVo.setCount(0-cashOut.getCashCount());
            coinsUpVo.setUserId(coins.getUserId());
            coinsUpVo.setUserType(coins.getUserType());
            int coinsRs = coinsMapper.modifyUnAliveCoins(coinsUpVo);
            Declare.isFailed(coinsRs==0,GlobalsEnums.COINS_ACCOUNT_NOT_FOUND);

            // 修改资金明细的状态
            int detailRs = coinsDetailMapper.updateCoinsDetail(cashOut.getCoinsDetailId(),CoinsDetailStatusEnum.CASH_WAIT.getCode(),CoinsDetailStatusEnum.CASH_OUT.getCode());
            Declare.isFailed(detailRs==0,GlobalsEnums.CASH_OUT_FAILED);

            detail = "提现操作成功";
        }

        // 重新查询一下防止被修改了
        // 修改资金明细
        CoinsDetail coinsDetail = coinsDetailMapper.selectByPrimaryKey(cashOut.getCoinsDetailId());

        coins = coinsMapper.selectByIdForUpdate(cashOut.getCoinsId());
        // 添加日志
        log = CoinsVo.coinsLog(coins,false,log);
        log.setCoinsId(coins.getId());
        log.setCoinsDetailId(coinsDetail.getId());
        log.setOptUserId(userSession.getId());
        log.setCount(coinsDetail.getCount());
        log.setDetail(detail);
        log.setStatus(coinsDetail.getStatus());
        log.setType(coinsDetail.getType());
        log.setInOut(coinsDetail.getInOut());
        log.setCreateTime(System.currentTimeMillis());
        int logRs = coinsLogMapper.insertSelective(log);
        Declare.isFailed(logRs==0,GlobalsEnums.CASH_OUT_FAILED);


        String result = rsMap.get("err_code_des");
        String payNo = rsMap.get("payment_no");
        // 修改当前的提现记录到失败
        cashOutUp.setResult(result);
        cashOutUp.setUpdateTime(new Date());
        cashOutUp.setId(cashOut.getId());
        cashOutUp.setPaymentNo(payNo);
        int outRS = coinsCashOutMapper.updateByPrimaryKeySelective(cashOutUp);
        Declare.isFailed(outRS==0,GlobalsEnums.UPDATE_COINS_ACCOUNT_FAILED);

        return null;
    }
    
}
