package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.MemberDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.member.Member;
import com.jiuy.core.meta.member.MemberSearch;
import com.jiuy.core.meta.member.MemberVO;
import com.jiuyuan.entity.query.PageQuery;

public class MemberDaoSqlImpl extends DomainDaoSqlSupport<Member, Long> implements MemberDao {

	@Override
	public List<MemberVO> search(MemberSearch meSearch, PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("yjjNumber", meSearch.getYjjNumber());
		params.put("phone", meSearch.getBindPhone());
		params.put("parentDistribution", meSearch.getParentDistribution());
		params.put("status", meSearch.getStatus());
		params.put("distributionCountMin", meSearch.getPartnerCountMin());
		params.put("distributionCountMax", meSearch.getPartnerCountMax());
		params.put("distributionStatus", meSearch.getDistributionStatus());
		params.put("createTimeMin", meSearch.getCreateTimeMin());
		params.put("createTimeMax", meSearch.getCreateTimeMax());
		params.put("jiuCoinMin", meSearch.getJiuCoinMin());
		params.put("jiuCoinMax", meSearch.getJiuCoinMax());
		params.put("storeId", meSearch.getBelongStoreId());
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(MemberSearch meSearch) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("yjjNumber", meSearch.getYjjNumber());
		params.put("phone", meSearch.getBindPhone());
		params.put("parentDistribution", meSearch.getParentDistribution());
		params.put("status", meSearch.getStatus());
		params.put("distributionCountMin", meSearch.getPartnerCountMin());
		params.put("distributionCountMax", meSearch.getPartnerCountMax());
		params.put("distributionStatus", meSearch.getDistributionStatus());
		params.put("createTimeMin", meSearch.getCreateTimeMin());
		params.put("createTimeMax", meSearch.getCreateTimeMax());
		params.put("jiuCoinMin", meSearch.getJiuCoinMin());
		params.put("jiuCoinMax", meSearch.getJiuCoinMax());
		params.put("storeId", meSearch.getBelongStoreId());
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.searchCount", params);
	}

	@Override
	public int editStatus(MemberVO mVo) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.editStatus", mVo);
	}

	@Override
	public int resetpwd(long yjjCode, String password) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("yjjNumber", yjjCode);
		param.put("password", password);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.resetpwd", param);
	}

	@Override
	public int unbundlingStore(long userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("userId", userId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.unbundlingStore",param);
	}

	@Override
	public int addMemberStoreRelation(long userId,long businessNumber,int type,int status,long createTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("userId", userId);
		param.put("businessNumber", businessNumber);
		param.put("type", type);
		param.put("createTime", createTime);
		param.put("status", status);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.addMemberStoreRelation",param);
	}

	@Override
	public long userIdOfYjjNumber(long yjjNumber) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.userIdOfYjjNumber", yjjNumber)==null?0:getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.userIdOfYjjNumber", yjjNumber);
	}
	
	@Override
	public Map<Long, HashMap<String, Long>> getYJJNumberOfParentUserId(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.getYJJNumberOfParentUserId", params, "UserId");
	}

	@Override
	public long yjjNumberOfUserId(long userId) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.yjjNumberOfUserId", userId);
	}

	@Override
	public int addUserStatusLog(long relatedId, int type, int oldStatus, int newStatus, long createTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relatedId", relatedId);
		params.put("type", type);
		params.put("oldStatus", oldStatus);
		params.put("newStatus", newStatus);
		params.put("createTime", createTime);
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.addUserStatusLog", params);
	}

	@Override
	public int getDistributionStatusOfUserId(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		Integer distributionStatus = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.getDistributionStatusOfUserId", userId); 
		return distributionStatus==null ? 0 : distributionStatus;
	}

	@Override
	public int getStatusOfUserId(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.getStatusOfUserId", userId);
	}

	@Override
	public int editDistributionStatus(long id, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("status", status);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.editDistributionStatus", params);
	}

	@Override
	public long idOfUserId(long userId) {
		Long id = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.MemberDaoSqlImpl.idOfUserId", userId);
		return id==null?0:id;
	}
}
