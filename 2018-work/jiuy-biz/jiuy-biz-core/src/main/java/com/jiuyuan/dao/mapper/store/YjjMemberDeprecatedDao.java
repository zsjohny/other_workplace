package com.jiuyuan.dao.mapper.store;

import com.yujj.entity.product.YjjMember;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 17:46
 * @Copyright 玖远网络
 */
public interface YjjMemberDeprecatedDao{

    /*
     * 这里的code都是简单的操作, 不做注释, 较复杂的update, 参见{com.store.dao.mapper.YjjMemberMapper}都是那边搬过来的
     */



    YjjMember selectOne(Long storeId, int code);


    int switchToDelState(Long id, Integer state);

    int insert(YjjMember yjjMember);

    int updateEndTime(Long id, Integer memberPackageType, Long endTime, Double totalMoney,
                      String validTimeQueue, String historyValidTimeQueue);

    List<YjjMember> selectDirtyEndTime();
}
