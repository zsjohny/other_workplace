package com.jiuy.core.dao.impl.sql;

import com.jiuyuan.dao.mapper.store.MemberLogDeprecatedDao;
import com.yujj.entity.product.MemberLog;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/17 7:11
 * @Copyright 玖远网络
 */
@Repository
public class MemberLogDeprecateDaoSqlImp implements MemberLogDeprecatedDao{


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    @Override
    public int insertSelective(MemberLog log) {
        HashMap<String, Object> params = new HashMap<> ();
        params.put ("afterDelState", log.getAfterDelState ());
        params.put ("afterEndTime", log.getAfterEndTime ());
        params.put ("afterTimeQueue", log.getAfterTimeQueue ());
        params.put ("afterTotalMoney", log.getAfterTotalMoney ());
        params.put ("afterWxClosedTime", log.getAfterWxClosedTime ());
        params.put ("beforeDelState", log.getBeforeDelState ());
        params.put ("beforeEndTime", log.getBeforeEndTime ());
        params.put ("beforeTimeQueue", log.getBeforeTimeQueue ());
        params.put ("beforeTotalMoney", log.getBeforeTotalMoney ());
        params.put ("beforeWxClosedTime", log.getBeforeWxClosedTime ());
        params.put ("memberId", log.getMemberId ());
        params.put ("source", log.getSource ());
        params.put ("orderNo", log.getOrderNo ());
        return sqlSessionTemplate.insert ("com.jiuy.core.dao.impl.sql.MemberLogDeprecateDaoSqlImp.insertSelective",params);
    }
}
