package com.jiuy.core.service.member;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.MemberDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.meta.member.MemberSearch;
import com.jiuy.core.meta.member.MemberVO;
import com.jiuyuan.constant.UserStatusLogType;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.MD5Encoder;


@Service
public class MemberServiceImpl implements MemberSercive {

	@Resource
	private MemberDao memberDaoSqlImpl;
	
	@Resource
	private StoreBusinessDao storeBusinessDao;
	
	@Override
	public List<MemberVO> search(MemberSearch meSearch, PageQuery pageQuery) {
		return memberDaoSqlImpl.search(meSearch, pageQuery);
	}

	@Override
	public int searchCount(MemberSearch meSearch) {
		return memberDaoSqlImpl.searchCount(meSearch);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int edit(MemberVO mVo) {
		long userId = mVo.getUserId();
		long currentTime = System.currentTimeMillis();
		//增加UserStatusLog表
		int userOldStatus = memberDaoSqlImpl.getStatusOfUserId(userId);
		if(mVo.getStatus() != userOldStatus){
			memberDaoSqlImpl.addUserStatusLog(userId, UserStatusLogType.USERSTATUS.getIntValue(), userOldStatus, mVo.getStatus(), currentTime);
		}
		
		int oldDistributionStatus = memberDaoSqlImpl.getDistributionStatusOfUserId(userId);
		if(mVo.getDistributionStatus() != oldDistributionStatus){
			memberDaoSqlImpl.addUserStatusLog(userId, UserStatusLogType.USERDISTRIBUTIONSTATUS.getIntValue(), oldDistributionStatus, mVo.getDistributionStatus(), currentTime);
		}
		
		long id = memberDaoSqlImpl.idOfUserId(userId);
		if(id!=0){
			memberDaoSqlImpl.editDistributionStatus(id, mVo.getDistributionStatus());
		}
		
		return memberDaoSqlImpl.editStatus(mVo);
	}

	@Override
	public int resetpwd(long yjjCode) {
		Random random = new Random();
		int pwd = random.nextInt(100000);
		String password = MD5Encoder.encode(pwd+"");
		return memberDaoSqlImpl.resetpwd(yjjCode, password);
	}

	@Override
	public long userIdOfYjjNumber(long yjjNumber) {
		return memberDaoSqlImpl.userIdOfYjjNumber(yjjNumber);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int unbundlingStore(long userId,long businessNumber) {
		memberDaoSqlImpl.unbundlingStore(userId);
		//对应门店会员数-1
		long storeId = storeBusinessDao.getIdByBusinessNumber(businessNumber);
		storeBusinessDao.decreaseMemberNumberById(storeId);
		//增加解绑记录
    	Date date = new Date();
    	addMemberStoreRelation(userId,storeId,1,0,date.getTime());
		return 0;
	}

	@Override
	public int addMemberStoreRelation(long userId,long businessNumber,int type,int status,long createTime) {
		return memberDaoSqlImpl.addMemberStoreRelation(userId,businessNumber,type,status,createTime);
	}

}
