package com.goldplusgold.td.sltp.core.dao.impl;

import com.goldplusgold.td.sltp.core.dao.UserSltpDao;
import com.goldplusgold.td.sltp.core.mapper.UserSltpMapper;
import com.goldplusgold.td.sltp.core.operate.component.RedisHashOperateComponent;
import com.goldplusgold.td.sltp.core.operate.enums.ClickTypeEnum;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户止盈止损的dao 实现层
 * Created by Ness on 2017/5/10.
 */
@Repository
public class UserSltpDaoImpl implements UserSltpDao {

    /**
     * 开始页数
     */
    private final int PAGE_COUNT = 3;
    /**
     * 每页数目
     */
    private final int START_PAGE = 1;

    private Logger logger = LoggerFactory.getLogger(UserSltpDaoImpl.class);
    @Autowired
    private RedisHashOperateComponent redisHashOperateComponent;

    @Autowired
    private UserSltpMapper userSltpMapper;

    /**
     * 根据用户的止盈止损的id查询单个详细信息
     *
     * @param uuid 止盈止损的id
     * @return
     */
    @Override
    public UserSltpRecord findUserSltpOneByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {

            logger.warn("用户查询止盈止损单个 所传参数为空");
            return null;
        }
        logger.info("查询 uuid为{}数据成功", uuid);
        return userSltpMapper.findSltpOneByUuid(uuid);
    }


    private Map<String, Integer> userTotalPages = new ConcurrentHashMap<>();
    private Map<String, Integer> userMysqlTotalPages = new ConcurrentHashMap<>();

    /**
     * 根据用户Id查找所有用户的数据
     *
     * @param userId       用户的id
     * @param clickType    用户点击类型
     * @param contractName 合约的名称
     * @param bearBull     空头或者多头 0 空 1多
     * @param page         开始的页数 默认是第一页
     * @param isOperateDb  是否是删除mysql还是redis true是mysql
     * @return
     */
    @Override
    public List<UserSltpRecord> findAllUserSltpInByUserId(String userId, Integer clickType, String contractName, Integer bearBull, Integer page, Boolean isOperateDb) {

        if (userId == null || isOperateDb == null) {
            logger.warn("用户所传的userId为空");
            return null;
        }
        logger.info("根据 用户Id为{}查询止盈止损记录成功", userId);
        //查询当前的redis的记录
        if (!isOperateDb)
            return redisHashOperateComponent.get(contractName, userId, bearBull);

        //根据clickType 判断是已经触发(已经触发包含了触发成功 和触发失败)还是未触发(包含了redis中还未触发和已经失效了)


        if (clickType == null || clickType > ClickTypeEnum.MAX_LEN.toCode()) {
            logger.warn("用户所传的clickType不符合规范");
            return null;
        }

        if (page == null || page < 0) {
            page = START_PAGE;
        }


        if (clickType.equals(ClickTypeEnum.ALREADY_TRIGGER.toCode())) {
            return userSltpMapper.findSltpAllInfoByUserId(userId, null, (page - 1) * PAGE_COUNT, PAGE_COUNT);
        } else {

            //先从redis查询
            List<UserSltpRecord> list = redisHashOperateComponent.getAll(userId, (page - 1) * PAGE_COUNT, PAGE_COUNT);


            //这里插入默认redis的为空页数
            if (page != START_PAGE && list.size() < PAGE_COUNT) {
                userTotalPages.putIfAbsent(userId, page);
            } else {
                userTotalPages.remove(userId);
            }


            if (list.isEmpty() || list.size() < PAGE_COUNT) {


                //这里把mysql的页数进行整理
                Integer _redisPage = userTotalPages.get(userId);

                if (_redisPage != null) {
                    page = Math.abs(page - _redisPage) + START_PAGE;
                }

                Integer pageCount = PAGE_COUNT;
                Integer _divideStart = 0;
                //将首页的数据进行减掉
                if (!list.isEmpty() && list.size() < PAGE_COUNT) {
                    pageCount = PAGE_COUNT - list.size();
                    userMysqlTotalPages.put(userId, list.size());
                } else {
                    //将差的数据补充出来
                    _divideStart = userMysqlTotalPages.get(userId);
                    if (_divideStart == null) {
                        _divideStart = 0;
                    }
                }

                page = page - 1;
                int start = page * pageCount - _divideStart;


                //不满足条件去db查
                List<UserSltpRecord> _tempList = userSltpMapper.findSltpAllInfoByUserId(userId, UserSltpRecord.SltpType.COMMISS_INVAILD.toType(), start, pageCount);

                if (_tempList != null && !_tempList.isEmpty()) {

                    //该条件是不满足分页查询
                    if (list != null && !list.isEmpty()) {

                        list.addAll(_tempList);


                    } else {
                        list = _tempList;
                    }


                }


            }


            return list;

        }


    }

    /**
     * 根据用户的止盈止损的id删除
     *
     * @param uuid 用户的止盈止损的id
     */
    @Override
    public boolean removeUserSltpRecordByUuid(String uuid) {

        if (StringUtils.isEmpty(uuid)) {


            logger.warn("用户删除止盈止损单个  所传id为空");
            return false;
        }

        // UserSltpRecord removeUserSltp = new UserSltpRecord();
        // removeUserSltp.setDeleted(DeletedMarkEnum.DELETED.getMark());
        // removeUserSltp.setUuid(uuid);
        //TODO:暂时不操作mysql
        //userSltpMapper.updateUserSltpOneByUuid(removeUserSltp);
        List<UserSltpRecord> delete = redisHashOperateComponent.delete(uuid);


        logger.info("根据 止盈止损记录Id为{} 删除 止盈止损记录成功", uuid);

        return (delete == null || delete.isEmpty()) ? false : true;

    }

    /**
     * 根据止盈止损的id 更新止盈止损的详细信息
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    @Override
    public boolean updateUserSltpRecordInfoByUuid(UserSltpRecord userSltpRecord, Boolean isOperateDb) {

        boolean flag = false;

        if (userSltpRecord == null || StringUtils.isEmpty(userSltpRecord.getUuid()) || isOperateDb == null) {
            logger.warn("更新止盈止损记录 字段为空");
            return flag;
        }

        logger.info("根据 止盈止损记录Id为{} 更新 止盈止损记录成功", userSltpRecord.getUuid());

        if (isOperateDb) {
            flag = userSltpMapper.updateUserSltpOneByUuid(userSltpRecord);
        } else {
            flag = redisHashOperateComponent.update(userSltpRecord);
        }

        return flag;
    }


    /**
     * 保存用户的止盈止损的数据
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    @Override
    public boolean saveUserSltpRecordInfo(UserSltpRecord userSltpRecord, Boolean isOperateDb) {
        boolean flag = false;
        if (userSltpRecord == null || isOperateDb == null) {
            logger.warn("插入止盈止损记录 字段为空");
            return flag;
        }
        if (isOperateDb) {
            flag = userSltpMapper.saveUserSltpOne(userSltpRecord);
        } else {

            flag = redisHashOperateComponent.add(userSltpRecord);
        }

        return flag;

    }


}
