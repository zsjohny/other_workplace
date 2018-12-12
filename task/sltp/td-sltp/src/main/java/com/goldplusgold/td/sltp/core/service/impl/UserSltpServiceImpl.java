package com.goldplusgold.td.sltp.core.service.impl;


import com.goldplusgold.td.sltp.core.dao.UserSltpDao;
import com.goldplusgold.td.sltp.core.operate.enums.AccordLimitAndLotsEnum;
import com.goldplusgold.td.sltp.core.operate.enums.DbOperateEnum;
import com.goldplusgold.td.sltp.core.operate.util.UserUtil;
import com.goldplusgold.td.sltp.core.service.UserSltpService;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpAllVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOneVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOperVM;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.cache.QuotaPriceLimit;
import com.goldplusgold.td.sltp.monitor.dbservice.AvailableLotsOperations;
import com.goldplusgold.td.sltp.monitor.model.AvailableLots;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.goldplusgold.td.sltp.core.operate.enums.ResponseTypeEnum.*;
import static com.goldplusgold.td.sltp.core.vo.UserSltpRecord._BEAR;
import static com.goldplusgold.td.sltp.core.vo.UserSltpRecord._BULL;

/**
 * 用户的止盈止损的service 实现层
 * Created by Ness on 2017/5/10.
 */
@Service
@Transactional
public class UserSltpServiceImpl implements UserSltpService {

    private Logger logger = LoggerFactory.getLogger(UserSltpServiceImpl.class);

    @Autowired
    private UserSltpDao userSltpDao;

    @Autowired
    private AvailableLotsOperations availableLotsOperations;

    /**
     * 根据用户的止盈止损的id查询单个详细信息
     *
     * @param uuid 止盈止损的id
     * @return
     */
    @Override
    public UserSltpOneVM findUserSltpOneByUuid(String uuid) {
        UserSltpOneVM vm = new UserSltpOneVM();
        if (StringUtils.isEmpty(uuid)) {

            logger.warn("用户查询止盈止损单个 所传参数为空");
            vm.setCode(EMPTY_PARAM.toCode());
            return vm;
        }
        logger.info("查询 uuid为{}数据 start query...", uuid);
        UserSltpRecord record = userSltpDao.findUserSltpOneByUuid(uuid);
        logger.info("查询 uuid为{}数据 end query...", uuid);

        vm.setCode(SUCCESS_PARAM.toCode());

        vm.setData(record);

        return vm;
    }


    /**
     * 根据用户Id查找所有用户的数据
     *
     * @param userId       用户的id
     * @param clickType    用户点击类型
     * @param contractName 合约的名称 (非必须字段)
     * @param bearBull     空头或者多头 0 空 1多(非必须字段)
     * @param page         开始的页数 默认是第一页
     * @return
     */
    @Override
    public UserSltpAllVM findAllUserSltpInByUserId(String userId, Integer clickType, String contractName, Integer bearBull, Integer page) {
        UserSltpAllVM vm = new UserSltpAllVM();
        if (userId == null) {
            logger.warn("用户所传的userId为空");
            vm.setCode(EMPTY_PARAM.toCode());
            return vm;
        }
        logger.info("查询 用户Id为为{}数据 start query...", userId);
        List<UserSltpRecord> lists;
        //混合查询类似于 db查询+redis查询
        Boolean flag = DbOperateEnum.MIXED.getFlag();

        //查特定合约的--在redis中查询
        if (StringUtils.isNotEmpty(contractName)) {
            flag = DbOperateEnum.REDIS.getFlag();
        }

        lists = userSltpDao.findAllUserSltpInByUserId(userId, clickType, contractName, bearBull, page, flag);


        logger.info("查询 用户Id为为{}数据 end query...", userId);

        vm.setCode(SUCCESS_PARAM.toCode());

        vm.setData(lists == null ? new ArrayList<>() : lists);

        return vm;
    }

    /**
     * 根据用户的止盈止损的id删除
     *
     * @param uuid 用户的止盈止损的id
     */
    @Override
    public UserSltpOperVM removeUserSltpRecordByUuid(String uuid) {

        UserSltpOperVM vm = new UserSltpOperVM();

        if (StringUtils.isEmpty(uuid)) {

            logger.warn("用户删除止盈止损单个  所传id为空");
            vm.setCode(EMPTY_PARAM.toCode());
            return vm;
        }
        logger.info("根据 止盈止损记录Id为{} 删除 止盈止损记录 start remove...", uuid);
        boolean flag = userSltpDao.removeUserSltpRecordByUuid(uuid);
        logger.info("根据 止盈止损记录Id为{} 删除 止盈止损记录 end remove...", uuid);

        vm.setCode(flag ? SUCCESS_PARAM.toCode() : ERROR_PARAM.toCode());

        return vm;
    }

    /**
     * 根据止盈止损的id 更新止盈止损的详细信息
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    @Override
    public UserSltpOperVM updateUserSltpRecordInfoByUuid(UserSltpRecord userSltpRecord, Boolean isOperateDb) {
        UserSltpOperVM vm = new UserSltpOperVM();
        if (userSltpRecord == null || StringUtils.isEmpty(userSltpRecord.getUuid())) {
            logger.warn("更新止盈止损记录 字段为空");
            vm.setCode(EMPTY_PARAM.toCode());
            return vm;
        }
        if (!userSltpRecord.checkUpdateArgument()) {
            logger.warn("更新止盈止损记录不符合规范");
            vm.setCode(ILLEGAL_PARAM.toCode());
            return vm;
        }

        //是否超过当日涨跌幅和可用手数
        AccordLimitAndLotsEnum accordLimitAndLots = isAccordLimitAndLots(userSltpRecord);
        switch (accordLimitAndLots) {
            case AUTO_TRIGGER:
                break;
            case SUCCESS_CODE:
                break;
            default:
                logger.warn("订单{}保存设置的止盈止损不符合当日涨停板或者可用手数", userSltpRecord.getUuid());
                vm.setCode(accordLimitAndLots.toCode());
                return vm;
        }

        //TODO:test
        //http://localhost:8078/sltp/update?slPrice=12&lots=20&commissionPrice=2&floatPrice=4&uuid=da781a52611245979e5d0a1be42ffeaf

        logger.info("根据 止盈止损记录Id为{} 更新 止盈止损记录 start update", userSltpRecord.getUuid());
        boolean flag = userSltpDao.updateUserSltpRecordInfoByUuid(userSltpRecord, isOperateDb);
        logger.info("根据 止盈止损记录Id为{} 更新 止盈止损记录 end update", userSltpRecord.getUuid());

        vm.setCode(flag ? SUCCESS_PARAM.toCode() : ERROR_PARAM.toCode());

        return vm;
    }

    @Autowired
    private QuotaPriceLimit quotaPriceLimit;

    /**
     * 保存用户的止盈止损的数据
     *
     * @param userSltpRecord 止盈止损上传实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    @Override
    public UserSltpOperVM saveUserSltpRecordInfo(UserSltpRecord userSltpRecord, Boolean isOperateDb) {

        UserSltpOperVM vm = new UserSltpOperVM();

        if (userSltpRecord == null) {
            logger.warn("插入止盈止损记录 字段为空");
            vm.setCode(EMPTY_PARAM.toCode());
            return vm;

        }

        if (!userSltpRecord.checkSaveArgument()) {
            logger.warn("插入止盈止损记录不符合规范");
            vm.setCode(ILLEGAL_PARAM.toCode());
            return vm;

        }

        //默认值
        userSltpRecord.setUuid(UserUtil.createUuid());


        //是否超过当日涨跌幅和可用手数
        AccordLimitAndLotsEnum accordLimitAndLots = isAccordLimitAndLots(userSltpRecord);
        switch (accordLimitAndLots) {
            case AUTO_TRIGGER:
                break;
            case SUCCESS_CODE:
                break;
            default:
                logger.warn("订单{}保存设置的止盈止损不符合当日涨停板或者可用手数", userSltpRecord.getUuid());
                vm.setCode(accordLimitAndLots.toCode());
                return vm;
        }


        userSltpRecord.setCommissionStartDate(System.currentTimeMillis());
        userSltpRecord.setCommissionEndDate(userSltpRecord.getCommissionStartDate() + UserSltpRecord.NOWDAY_EXPIRE_TIME);

        //test
        //http://localhost:8078/sltp/save?slPrice=2&tpPrice=2&lots=3&contract=22&bearBull=1&sltpType=0&commissionResult=1&commissionPrice=2&floatPrice=4


        logger.info("插入 止盈止损记录 start save");
        boolean flag = userSltpDao.saveUserSltpRecordInfo(userSltpRecord, isOperateDb);
        logger.info("插入 止盈止损记录 end save");

        vm.setCode(flag ? SUCCESS_PARAM.toCode() : ERROR_PARAM.toCode());

        return vm;
    }

    /**
     * 判断是否符合当日的涨停板限制和可用手数
     *
     * @param userSltpRecord 止盈止损的记录
     * @return
     */
    private AccordLimitAndLotsEnum isAccordLimitAndLots(UserSltpRecord userSltpRecord) {


        /**
         * 交易回调
         */
        if (userSltpRecord.getAutoTriggle() != null && userSltpRecord.getAutoTriggle()) {
            return AccordLimitAndLotsEnum.AUTO_TRIGGER;
        }

        //判断可用手数
        AvailableLots availableLots = availableLotsOperations.getAvailableLots(userSltpRecord.getContract(), userSltpRecord.getUserId());
        if (_BEAR.equals(userSltpRecord.getBearBull())) {
            Integer bearLots = availableLots.getBearLots();
            if (userSltpRecord.getLots() > bearLots) {
                logger.warn("用户{}的空头的设置手数{}大于可用手数", userSltpRecord.getUuid(), userSltpRecord.getLots());
                return AccordLimitAndLotsEnum.LIMIT_LOTS;

            }


        } else if (_BULL.equals(userSltpRecord.getBearBull())) {
            Integer bullLots = availableLots.getBullLots();
            if (userSltpRecord.getLots() > bullLots) {
                logger.warn("用户{}的多头的设置手数{}大于可用手数", userSltpRecord.getUuid(), userSltpRecord.getLots());
                return AccordLimitAndLotsEnum.LIMIT_LOTS;
            }
        } else {
            logger.warn("用户{}的空头多头设置参数不对", userSltpRecord.getUuid());
            return AccordLimitAndLotsEnum.SETTING_LOTS_ERROR;
        }


        if (userSltpRecord == null || StringUtils.isEmpty(userSltpRecord.getUuid()) || StringUtils.isEmpty(userSltpRecord.getContract())
                || userSltpRecord.getBearBull() == null || (userSltpRecord.getSlPrice() == null && userSltpRecord.getTpPrice() == null)) {
            logger.warn("进行匹配用户设置的止盈止损 参数为空");
            return AccordLimitAndLotsEnum.EMPTY_PARAM;
        }

        String highStr = quotaPriceLimit.getHigh(userSltpRecord.getContract());

        if (StringUtils.isEmpty(highStr)) {
            logger.warn("当日合约{}涨停板缓存为空", userSltpRecord.getContract());
            return AccordLimitAndLotsEnum.EMPTY_RECORD;
        }

        String lowStr = quotaPriceLimit.getLow(userSltpRecord.getContract());

        if (StringUtils.isEmpty(lowStr)) {
            logger.warn("当日合约{}跌停板缓存为空", userSltpRecord.getContract());
            return AccordLimitAndLotsEnum.EMPTY_RECORD;
        }


        double highPrice = Double.parseDouble(highStr);
        double lowPrice = Double.parseDouble(lowStr);

        /**
         * 空头
         *....止损<-------------
         * -----------------涨停板
         *    .....
         *     ......
         *      ......
         * -----------------跌停板
         *         .....止盈<---------
         *
         */
        switch (userSltpRecord.getBearBull().toString()) {
            //空头
            case _BEAR:
                //止盈价大于当前跌停板
                if (userSltpRecord.getTpPrice() != null) {
                    if (userSltpRecord.getTpPrice() < lowPrice) {
                        logger.warn("订单{}设置的空头的止盈价格{}小于跌停板{}", userSltpRecord.getUuid(), userSltpRecord.getTpPrice(), lowPrice);
                        return AccordLimitAndLotsEnum.BEAR_LOW_TP;
                    }
                    //止损的价格小于当前的涨停板
                } else if (userSltpRecord.getSlPrice() != null) {
                    if (userSltpRecord.getSlPrice() > highPrice) {
                        logger.warn("订单{}设置的空头的止损价格{}大于涨停板{}", userSltpRecord.getUuid(), userSltpRecord.getSlPrice(), highPrice);
                        return AccordLimitAndLotsEnum.BEAR_HIGH_SL;
                    }
                } else {
                    logger.warn("空头订单{}设置的没有符合记录", userSltpRecord.getUuid());
                    return AccordLimitAndLotsEnum.EMPTY_RECORD;
                }
                break;

            /**
             * 多头
             *
             * -----------------涨停板
             *            .....止盈<-----
             *          ......
             *     止损.......<----------
             * -----------------跌停板
             *
             *
             */
            //多头
            case _BULL:
                //止盈价小于当前涨停板
                if (userSltpRecord.getTpPrice() != null) {
                    if (userSltpRecord.getTpPrice() > highPrice) {
                        logger.warn("订单{}设置的多头的止盈价格{}大于涨停板{}", userSltpRecord.getUuid(), userSltpRecord.getTpPrice(), highPrice);
                        return AccordLimitAndLotsEnum.BULL_HIGH_TP;
                    }
                    //止损的价格大于当前的跌停板
                } else if (userSltpRecord.getSlPrice() != null) {
                    if (userSltpRecord.getSlPrice() < lowPrice) {
                        logger.warn("订单{}设置的多头的止损价格{}小于涨停板{}", userSltpRecord.getUuid(), userSltpRecord.getSlPrice(), lowPrice);
                        return AccordLimitAndLotsEnum.BULL_LOW_SL;
                    }
                } else {
                    logger.warn("多头订单{}设置的没有符合记录", userSltpRecord.getUuid());
                    return AccordLimitAndLotsEnum.EMPTY_RECORD;
                }
                break;
            default:
                logger.warn("订单{}所传参数没有多头和空头", userSltpRecord.getUuid());
                return AccordLimitAndLotsEnum.EMPTY_RECORD;

        }


        return AccordLimitAndLotsEnum.SUCCESS_CODE;

    }

}
