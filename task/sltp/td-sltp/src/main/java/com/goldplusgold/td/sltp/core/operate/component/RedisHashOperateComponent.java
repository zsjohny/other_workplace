package com.goldplusgold.td.sltp.core.operate.component;

import com.alibaba.fastjson.JSONObject;
import com.goldplusgold.td.sltp.core.dao.UserSltpDao;
import com.goldplusgold.td.sltp.core.operate.enums.DbOperateEnum;
import com.goldplusgold.td.sltp.core.operate.enums.InstTypeEnum;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.cache.QuotaPriceLimit;
import com.goldplusgold.td.sltp.monitor.dbservice.KeyRecordsOperations;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.goldplusgold.td.sltp.core.vo.UserSltpRecord._BEAR;
import static com.goldplusgold.td.sltp.core.vo.UserSltpRecord._BULL;

/**
 * redis的操作类
 * <p/>
 * \---合约名称-----订单的Id-------订单的详细信息(表1结构)
 * \
 * \--------------------------------------------------
 * \                               ( 空头/多头 ------所有订单的内容)--JSONObject
 * \---合约名称-----用户的id----[key<bear/bull>:value<linkedHashMap(uuid:实体类){v}>](表2结构)
 * \
 * Created by Ness on 2017/5/10.
 */
@Component
public class RedisHashOperateComponent implements DisposableBean {

    /**
     * redis的详细订单的key
     */
    private final String SLTP_DETAIL_CONTRACT = "TD_SLTP_DETAIL_CONTRACT_";
    /**
     * redis的组合订单的key
     */
    private final String SLTP_COMBIN_CONTRACT = "TD_SLTP_COMBIN_CONTRACT_";


    private final int PAGE_COUNT = 20;
    private final int START_PAGE = 1;

    private Logger logger = LoggerFactory.getLogger(RedisHashOperateComponent.class);


    @Resource
    private HashOperations<String, String, UserSltpRecord> __detailHashOperate;
    @Resource
    private HashOperations<String, String, JSONObject> _combinHashOperate;
    @Autowired
    @Lazy
    private UserSltpDao userSltpDao;
    @Autowired
    private KeyRecordsOperations keyRecordsOperations;

    private ExecutorService expireExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);


    /**
     * 添加订单
     *
     * @param userSltpRecord
     */
    public boolean add(UserSltpRecord userSltpRecord) {

        boolean flag = false;
        //从这里进行验证 方便后面delete无需进行再次验证处理
        if (userSltpRecord == null || userSltpRecord.getBearBull() == null
                || userSltpRecord.getSltpType() == null || (userSltpRecord.getSlPrice() == null
                && userSltpRecord.getTpPrice() == null)
                || StringUtils.isEmpty(userSltpRecord.getContract()) || userSltpRecord.getCommissionStartDate() == null
                || userSltpRecord.getCommissionEndDate() == null || userSltpRecord.getUserId() == null || StringUtils.isEmpty(userSltpRecord.getUuid())) {
            logger.warn("用户插入时候所传参数为空");

            return flag;
        }
        logger.info("用户{}开始插入数据", userSltpRecord.getUserId());

        LinkedHashMap<String, UserSltpRecord> serRecordsMap;
        String key = (SLTP_COMBIN_CONTRACT + userSltpRecord.getContract()).intern();

        __detailHashOperate.putIfAbsent(SLTP_DETAIL_CONTRACT, userSltpRecord.getUuid(), userSltpRecord);
        JSONObject userSltpReocrds =
                _combinHashOperate.get(key, userSltpRecord.getUserId());
        if (userSltpReocrds == null) {
            serRecordsMap = new LinkedHashMap<>();
            userSltpReocrds = new JSONObject();
        } else {
            serRecordsMap = (LinkedHashMap<String, UserSltpRecord>) userSltpReocrds.get(userSltpRecord.getBearBull().toString());
        }

        serRecordsMap.put(userSltpRecord.getUuid(), userSltpRecord);

        userSltpReocrds.put(userSltpRecord.getBearBull().toString(), serRecordsMap);

        _combinHashOperate.put(key, userSltpRecord.getUserId(), userSltpReocrds);

        //添加zSet
        keyRecordsOperations.add(userSltpRecord);

        logger.info("用户{}结束插入数据", userSltpRecord.getUserId());

        flag = true;

        return flag;
    }


    /**
     * 更新订单的数据
     *
     * @param userSltpRecord 更新的实体类
     * @return
     */
    public boolean update(UserSltpRecord userSltpRecord) {


        if (userSltpRecord == null || StringUtils.isEmpty(userSltpRecord.getUuid())) {
            logger.warn("更新订单数据传入参数为空");
            return false;
        }


        UserSltpRecord _originalUsr = __detailHashOperate.get(SLTP_DETAIL_CONTRACT, userSltpRecord.getUuid());

        if (_originalUsr == null) {
            logger.warn("订单记录{},已经不存在", userSltpRecord.getUuid());
            return false;
        }
        //价格不同同时更新zSet
        if (!_originalUsr.judgeEqualPriceAndtransfer(userSltpRecord)) {
            //更新zSset
            keyRecordsOperations.update(userSltpRecord);

        }

        logger.info("开始更新合约为{}的订单为{}的信息", _originalUsr.getContract(), _originalUsr.getUuid());
        __detailHashOperate.put(SLTP_DETAIL_CONTRACT, _originalUsr.getUuid(), _originalUsr);
        logger.info("结束更新合约为{}的订单为{}的信息", _originalUsr.getContract(), _originalUsr.getUuid());

        JSONObject result = _combinHashOperate.get((SLTP_COMBIN_CONTRACT + _originalUsr.getContract()).intern(), _originalUsr.getUserId());


        if (result == null) {
            logger.warn("合约为{} 订单所有详情记录{},已经不存在", _originalUsr.getContract(), _originalUsr.getUuid());
            return false;
        }

        logger.info("开始更新合约为{}的订单为{}的详细信息", _originalUsr.getContract(), _originalUsr.getUuid());


        Object r = result.get(_originalUsr.getBearBull().toString());

        if (r == null) {
            logger.warn("合约为{} 订单{}的详情记录类型为{},已经不存在", _originalUsr.getContract(), _originalUsr.getUuid(), _originalUsr.getBearBull());
            return false;

        }

        LinkedHashMap<String, UserSltpRecord> _bearBullMap = (LinkedHashMap<String, UserSltpRecord>) r;


        _bearBullMap.put(_originalUsr.getUuid(), _originalUsr);


        r = result.get(_originalUsr.getOppositeBearBull());

        result = new JSONObject();
        result.put(_originalUsr.getBearBull().toString(), _bearBullMap);
        if (r != null) {
            result.put(_originalUsr.getOppositeBearBull(), r);
        }

        reAdd(_originalUsr.getContract(), _originalUsr.getUserId(), result);

        logger.info("结束更新合约为{}的订单为{}的详细信息", _originalUsr.getContract(), _originalUsr.getUuid());

        return true;

    }


    /**
     * 根据(合约-用户Id-空头或者多头)获取详情数据
     *
     * @param contract 合约的名称
     * @param userId   用户Id
     * @param bearBull 空头还是多头
     */
    public LinkedList<UserSltpRecord> get(String contract, String userId, Integer bearBull) {

        LinkedList<UserSltpRecord> result = new LinkedList<>();

        if (userId == null || StringUtils.isEmpty(contract) || bearBull == null) {


            logger.warn("用户 查询所传参数为空");

            return result;
        }


        logger.info("用户{}开始查询合约{}的类型{}数据", userId, contract, bearBull);

        JSONObject userSltpReocrds =
                _combinHashOperate.get((SLTP_COMBIN_CONTRACT + contract).intern(), userId);


        if (userSltpReocrds == null) {

            return result;
        }


        logger.info("用户{}结合查询合约{}的类型{}数据", userId, contract, bearBull);

        Object r = userSltpReocrds.get(bearBull.toString());
        if (r == null) {
            return result;
        }

        LinkedHashMap<String, UserSltpRecord> _combineUsers = (LinkedHashMap<String, UserSltpRecord>) r;

        List<String> _expireUuids = new ArrayList<>();

        for (UserSltpRecord usr : _combineUsers.values()) {
            if (System.currentTimeMillis() >= usr.getCommissionEndDate()) {
                _expireUuids.add(usr.getUuid());
                continue;
            }
            result.add(usr);
        }

        if (!_expireUuids.isEmpty()) {
            expireExecutor.execute(() -> deleteExpire(bearBull, _expireUuids));
        }


        return result;

    }


    /**
     * 根据用户Id获取所有行情信息
     *
     * @param userId    用户的id
     * @param page      开始的页数
     * @param pageCount 每页的数量
     */
    public LinkedList<UserSltpRecord> getAll(String userId, Integer page, Integer pageCount) {
        LinkedList<UserSltpRecord> result = new LinkedList<>();

        if (StringUtils.isEmpty(userId)) {
            logger.warn("获取所以的行情信息 用户Id参数为空");
            return result;
        }

        if (pageCount == null || pageCount <= 0) {
            pageCount = PAGE_COUNT;
        }

        if (page == null || page <= 0) {
            page = 0;
        }


        EnumSet<InstTypeEnum> _contractS = EnumSet.allOf(InstTypeEnum.class);
        InstTypeEnum ise;
        JSONObject userSltpReocrds;
        Object r;

        for (Iterator<InstTypeEnum> iterator = _contractS.iterator(); iterator.hasNext(); ) {
            ise = iterator.next();
            if (ise == null) {
                continue;
            }
            userSltpReocrds = _combinHashOperate.get((SLTP_COMBIN_CONTRACT + ise.toName()).intern(), userId);


            if (userSltpReocrds == null) {
                continue;
            }

            r = userSltpReocrds.get(_BEAR);

            if (r != null) {
                result = getAllData(page, pageCount, result, r);
            }


            r = userSltpReocrds.get(_BULL);

            if (r != null) {
                result = getAllData(page, pageCount, result, r);
            }


        }


        return result;


    }


    /**
     * 获取用户数据
     *
     * @param start           开始的数量
     * @param pageCount       每页的数量
     * @param userSltpRecords 总的用户数量
     * @param r               需要遍历的数据
     * @return
     */
    private LinkedList<UserSltpRecord> getAllData(int start, int pageCount,
                                                  LinkedList<UserSltpRecord> userSltpRecords,
                                                  Object r) {

        LinkedHashMap<String, UserSltpRecord> _tempMap = (LinkedHashMap<String, UserSltpRecord>) r;

        boolean flag = false;
        if (userSltpRecords.isEmpty()) {
            flag = true;
        }


        int _seq = 0;
        UserSltpRecord usr;

        for (Iterator<UserSltpRecord> iterator = _tempMap.values().iterator(); iterator.hasNext(); ) {
            usr = iterator.next();
            if (usr == null) {
                continue;
            }
            //如果不是首次不需要进行判断
            if (flag) {

                if (start >= ++_seq) {
                    continue;
                }
            }

            if (userSltpRecords.size() >= pageCount) {
                break;
            }

            userSltpRecords.add(usr);


        }


        return userSltpRecords;
    }

    /**
     * 根据订单详情数据 删除具体详细记录
     *
     * @param uuids 需要删除的订单详情数组
     * @return
     */
    public boolean delete(Set<String> uuids) {
        if (uuids == null || uuids.isEmpty()) {
            return false;
        }
        List<UserSltpRecord> lists = delete(uuids.toArray(new String[uuids.size()]));
        return lists.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 根据订单详情数据 删除具体详细记录
     *
     * @param uuids 需要删除的订单详情数组
     * @return
     */
    public List<UserSltpRecord> delete(String... uuids) {


        List<UserSltpRecord> resultList = new ArrayList<>();

        if (uuids == null || uuids.length == 0) {
            logger.warn("用户删除详情所传数据为空");

            return resultList;
        }

        UserSltpRecord userSltpRecord = null;


        int len = uuids.length;

        JSONObject result;

        LinkedHashMap<String, UserSltpRecord> _bearMap = null;
        LinkedHashMap<String, UserSltpRecord> _bullMap = null;

        Map<String, JSONObject> _allUsersSltps = new HashMap<>();
        JSONObject _subAddIds;

        synchronized (__detailHashOperate) {


            for (int i = 0; i < len; i++) {
                //加载用户的数据Id--也是为了验证数据是否正确
                userSltpRecord = __detailHashOperate.get(SLTP_DETAIL_CONTRACT, uuids[i]);


                if (userSltpRecord == null) {
                    logger.warn("id为{}用户数据为空", uuids[i]);
                    continue;
                }
                logger.info("id为{}用户开始删除合约{}的 数据", userSltpRecord.getUserId(), userSltpRecord.getContract());


                _subAddIds = _allUsersSltps.get(userSltpRecord.getUserId());

                if (_subAddIds == null) {

                    result = _combinHashOperate.get((SLTP_COMBIN_CONTRACT + userSltpRecord.getContract()).intern(), userSltpRecord.getUserId());

                    if (result == null) {
                        logger.warn("id为{}用户数据组合{}详情数据为空", userSltpRecord.getContract(), userSltpRecord.getUserId());
                        continue;
                    }

                    _subAddIds = new JSONObject();

                    Object r = result.get(_BEAR);

                    if (r != null) {

                        _bearMap = (LinkedHashMap<String, UserSltpRecord>) r;

                    }
                    _subAddIds.put(_BEAR, _bearMap);

                    r = result.get(_BULL);
                    if (r != null) {

                        _bullMap = (LinkedHashMap<String, UserSltpRecord>) r;

                    }

                    _subAddIds.put(_BULL, _bullMap);

                }


                boolean _removeFlag = removeHaveExistUUids(uuids[i], _subAddIds, _BEAR);
                //如果已经删除了无需再次删除
                if (!_removeFlag) {
                    removeHaveExistUUids(uuids[i], _subAddIds, _BULL);
                }

                _allUsersSltps.put(userSltpRecord.getUserId(), _subAddIds);

            }

            if (_allUsersSltps.isEmpty()) {
                logger.warn("Id为{}所需要删除的合约数据为空", userSltpRecord == null ? "" : userSltpRecord.getUserId());
                return resultList;
            }

            for (Map.Entry<String, JSONObject> entry : _allUsersSltps.entrySet()) {
                reAdd(userSltpRecord.getContract(), entry.getKey(), entry.getValue());
            }


            deleteDetail(uuids);

            logger.info("id为{}用户结束删除合约{}的 数据", userSltpRecord.getUserId(), userSltpRecord.getContract());

        }


        return resultList;
    }


    /**
     * 根据jsonObject删除已经存在的uuid
     *
     * @param uuid       订单的id
     * @param _subAddIds 总的jsonObject
     * @param bearBull   空头或者多头的类型
     * @return
     */
    private boolean removeHaveExistUUids(String uuid, JSONObject _subAddIds, String bearBull) {
        boolean flag = false;
        LinkedHashMap<String, UserSltpRecord> _tempMap = (LinkedHashMap<String, UserSltpRecord>) _subAddIds.get(bearBull);
        if (_tempMap != null) {
            for (String str : _tempMap.keySet().toArray(new String[_tempMap.keySet().size()])) {
                //将需要删除的id进行过滤
                if (str.equals(uuid)) {
                    _tempMap.remove(str);
                    flag = true;
                }

            }
        }

        _subAddIds.put(bearBull, _tempMap);

        return flag;
    }

    @Autowired
    private QuotaPriceLimit quotaPriceLimit;

    /**
     * 清理所有当天交易
     */

    public void clear() {

        //zSet的所有记录
        keyRecordsOperations.clearAll();

        //清除24小时map
        quotaPriceLimit.clearCache();

        synchronized (__detailHashOperate) {
            List<UserSltpRecord> values = __detailHashOperate.values(SLTP_DETAIL_CONTRACT);

            if (values == null || values.isEmpty()) {
                logger.warn("没有需要清理的交易记录");
                return;
            }

            logger.info("开始清理详细记录...");
            UserSltpRecord temp;
            List<String> _clearIds = new ArrayList<>(values.size());
            for (Iterator<UserSltpRecord> iterator = values.iterator(); iterator.hasNext(); ) {
                temp = iterator.next();
                if (temp == null) {
                    continue;
                }
                temp.setCommissionResult(UserSltpRecord.SltpType.COMMISS_INVAILD.toType());
                userSltpDao.saveUserSltpRecordInfo(temp, DbOperateEnum.MYSQL.getFlag());
                _clearIds.add(temp.getUuid());
            }

            if (!_clearIds.isEmpty()) {
                logger.info("清除了ids为{}的详情数据", _clearIds);
                __detailHashOperate.delete(SLTP_DETAIL_CONTRACT, _clearIds.toArray(new String[_clearIds.size()]));
            }
            logger.info("结束清理详细记录...");

        }


        synchronized (_combinHashOperate) {
            logger.info("开始清理所有详情记录...");
            EnumSet<InstTypeEnum> _instType = EnumSet.allOf(InstTypeEnum.class);

            String key;
            Set<String> keys;
            for (Iterator<InstTypeEnum> iterator = _instType.iterator(); iterator.hasNext(); ) {
                key = iterator.next().toName();

                keys = _combinHashOperate.keys((SLTP_COMBIN_CONTRACT + key).intern());

                if (keys == null || keys.isEmpty()) {
                    logger.warn("清理所有详情记录为空");
                    continue;
                }
                logger.info("清除了Ids为{}的所有的订单信息", keys);
                _combinHashOperate.delete((SLTP_COMBIN_CONTRACT + key).intern(), keys.toArray(new String[keys.size()]));

            }
            logger.info("结束清理所有详情记录...");

        }


    }


    /**
     * 自定清理失效的数据
     *
     * @param bearBull 空头或者多头
     * @param uuids    订单Id数组
     */
    private void deleteExpire(Integer bearBull, List<String> uuids) {

        if (bearBull == null || uuids == null || uuids.isEmpty()) {
            logger.warn("没有需要清理的任务");
            return;
        }

        logger.info("任务{}已经失效,正在清除....", uuids);


        int len = uuids.size();
        UserSltpRecord temp;
        //保存失效
        for (int i = 0; i < len; i++) {
            //捞取所有详细数据
            temp = __detailHashOperate.get(SLTP_DETAIL_CONTRACT, uuids.get(i));
            if (temp == null) {
                logger.warn("清理过期任务{} 详细数据为空", uuids.get(i));
                continue;
            }
            temp.setCommissionResult(UserSltpRecord.SltpType.COMMISS_INVAILD.toType());

            userSltpDao.saveUserSltpRecordInfo(temp, DbOperateEnum.MYSQL.getFlag());
        }


        //清除map和清除zSet
        keyRecordsOperations.batchDelete(delete(uuids.toArray(new String[uuids.size()])));

        logger.info("任务{}已经失效,清除成功....", uuids);


    }

    /**
     * 删除详细的订单信息
     *
     * @param uuids 详细的订单信息
     */
    private void deleteDetail(String... uuids) {
        if (uuids == null || uuids.length == 0) {
            logger.warn("清除详细订单信息参数为空");
            return;
        }
        logger.info("开始清除{}详细订单信息", uuids);
        __detailHashOperate.delete(SLTP_DETAIL_CONTRACT, uuids);
        logger.info("结束清除{}详细订单信息", uuids);

    }


    /**
     * 重新添加
     *
     * @param contract     需要添加的合约
     * @param userId       需要添加的userId
     * @param saveUserSltp 需要添加的jsonObject
     */
    private void reAdd(String contract, String userId, JSONObject saveUserSltp) {
        if (saveUserSltp == null || saveUserSltp.isEmpty() || StringUtils.isEmpty(contract) || userId == null) {
            logger.warn("没有需要添加的任务");
            return;
        }

        _combinHashOperate.put((SLTP_COMBIN_CONTRACT + contract).intern(), userId, saveUserSltp);


    }


    @Override
    public void destroy() throws Exception {
        if (!expireExecutor.isShutdown()) {
            expireExecutor.shutdown();
        }

    }


}
