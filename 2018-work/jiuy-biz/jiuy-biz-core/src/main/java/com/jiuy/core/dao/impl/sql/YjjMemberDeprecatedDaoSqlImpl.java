package com.jiuy.core.dao.impl.sql;

import com.jiuyuan.dao.mapper.store.YjjMemberDeprecatedDao;
import com.yujj.entity.product.YjjMember;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 17:51
 * @Copyright 玖远网络
 */
@Repository
public class YjjMemberDeprecatedDaoSqlImpl implements YjjMemberDeprecatedDao{


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public YjjMember selectOne(Long storeId, int code) {
        HashMap<String, Object> params = new HashMap<> ();
        params.put("userId", storeId);
        params.put("platformType", code);
        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.YjjMemberDeprecatedDaoSqlImpl.selectOne",params);
    }

    @Override
    public int switchToDelState(Long id, Integer state) {
        HashMap<String, Object> params = new HashMap<> ();
        params.put("state", state);
        params.put ("id", id);
        return sqlSessionTemplate.update ("com.jiuy.core.dao.impl.sql.YjjMemberDeprecatedDaoSqlImpl.switchToDelState",params);
    }

    @Override
    public int insert(YjjMember yjjMember) {
        HashMap<String, Object> params = new HashMap<> ();
        params.put ("platformType", yjjMember.getPlatformType ());
        params.put ("memberLevel", yjjMember.getMemberLevel ());
        params.put ("endTime", yjjMember.getEndTime ());
        params.put ("delState", yjjMember.getDelState ());
        params.put ("moneyTotal", yjjMember.getMoneyTotal ());
        params.put ("userId", yjjMember.getUserId ());
        params.put ("validTimeQueue", yjjMember.getValidTimeQueue ());
        return sqlSessionTemplate.insert ("com.jiuy.core.dao.impl.sql.YjjMemberDeprecatedDaoSqlImpl.insertOne",params);
    }

    @Override
    public int updateEndTime(Long id, Integer memberPackageType, Long endTime, Double totalMoney, String validTimeQueue, String historyValidTimeQueue) {
        HashMap<String, Object> params = new HashMap<> ();
        params.put("id", id);
        params.put("memberPackageType", memberPackageType);
        params.put("endTime", endTime);
        params.put("totalMoney", totalMoney);
        params.put("validTimeQueue", validTimeQueue);
        params.put("historyValidTimeQueue", historyValidTimeQueue);
        return sqlSessionTemplate.update ("com.jiuy.core.dao.impl.sql.YjjMemberDeprecatedDaoSqlImpl.updateEndTime",params);
    }

    @Override
    public List<YjjMember> selectDirtyEndTime() {
        HashMap<String, Object> params = new HashMap<> ();
        params.put("current", System.currentTimeMillis ());
        return sqlSessionTemplate.selectList ("com.jiuy.core.dao.impl.sql.YjjMemberDeprecatedDaoSqlImpl.selectDirtyEndTime", params);
    }


}


