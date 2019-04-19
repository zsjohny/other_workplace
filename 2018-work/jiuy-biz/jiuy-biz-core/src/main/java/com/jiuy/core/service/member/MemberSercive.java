package com.jiuy.core.service.member;

import java.util.List;

import com.jiuy.core.meta.member.MemberSearch;
import com.jiuy.core.meta.member.MemberVO;
import com.jiuyuan.entity.query.PageQuery;

public interface MemberSercive {

	List<MemberVO> search(MemberSearch meVo, PageQuery pageQuery);

	int searchCount(MemberSearch meVo);

	int edit(MemberVO mVo);

	int resetpwd(long yjjCode);
	
	long userIdOfYjjNumber(long yjjNumber);
	
	int unbundlingStore(long userId, long businessNumber);
	
	int addMemberStoreRelation(long userId,long businessNumber,int type,int status,long createTime);

}
