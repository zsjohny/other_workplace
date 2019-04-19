package com.yujj.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.MemberStoreRelation;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:38:37
 * 
 */
@DBMaster
public interface MemberStoreRelationMapper {

	int add(MemberStoreRelation memberStoreRelation);

}
