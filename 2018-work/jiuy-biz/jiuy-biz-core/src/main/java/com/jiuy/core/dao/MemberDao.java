package com.jiuy.core.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.member.Member;
import com.jiuy.core.meta.member.MemberSearch;
import com.jiuy.core.meta.member.MemberVO;
import com.jiuyuan.entity.query.PageQuery;

public interface MemberDao extends DomainDao<Member, Long>{

	List<MemberVO> search(MemberSearch meVo, PageQuery pageQuery);

	int searchCount(MemberSearch meVo);

	int editStatus(MemberVO mVo);
	
	int editDistributionStatus(long id, int status);

	int resetpwd(long yjjCode, String password);
	
	long userIdOfYjjNumber(long yjjNumber);
	
	long idOfUserId(long userId);
	
	long yjjNumberOfUserId(long userId);
	
	int unbundlingStore(long userId);
	
	int addMemberStoreRelation(long userId,long businessNumber,int type,int status,long createTime);
	
	public Map<Long, HashMap<String, Long>> getYJJNumberOfParentUserId(Collection<Long> ids);
	
	public int addUserStatusLog(long relatedId, int type, int oldStatus, int newStatus, long createTime);
	
	public int getDistributionStatusOfUserId(long userId);
	
	public int getStatusOfUserId(long userId);
}
