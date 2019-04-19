package com.jiuyuan.service.common;

import com.jiuyuan.constant.MemberPackageType;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.dao.mapper.store.MemberLogDeprecatedDao;
import com.jiuyuan.dao.mapper.store.YjjMemberDeprecatedDao;
import com.jiuyuan.util.BizException;
import com.yujj.entity.product.MemberLog;
import com.yujj.entity.product.ValidTimeQueueHelper;
import com.yujj.entity.product.YjjMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 17:33
 * @Copyright 玖远网络
 */
@Service("yjjMemberServiceImplDeprecated")
public class YjjMemberServiceImplDeprecated implements IYjjMemberServiceDeprecated{

    static Logger logger = LoggerFactory.getLogger(YjjMemberServiceImplDeprecated.class);
    @Autowired
    private YjjMemberDeprecatedDao yjjMemberMapper;
    @Autowired
    private MemberLogDeprecatedDao memberLogMapper;


    /**
     * 开通会员
     *
     * @param storeId 门店用户id
     * @author Charlie
     * @date 2018/8/14 17:47
     */
    @Override
    public void openMemberShipAccount(Long storeId) {
        YjjMember yjjMember = yjjMemberMapper.selectOne (storeId, SystemPlatform.STORE.getCode ());
        if (null == yjjMember) {
            //创建
            yjjMember = new YjjMember ();
            Calendar calendar = Calendar.getInstance ();
            calendar.add (Calendar.YEAR, MemberPackageType.USER_YEAR);
            long endTime = calendar.getTimeInMillis ();
            yjjMember.setPlatformType (SystemPlatform.STORE.getCode ());
            yjjMember.setUserId (storeId);
            yjjMember.setMemberLevel (1);
            yjjMember.setEndTime (endTime);
            yjjMember.setMoneyTotal (new BigDecimal ("1800"));
            yjjMember.setDelState (0);
//            yjjMember.setType ();默认
            yjjMember.setValidTimeQueue ("["+endTime+":"+yjjMember.getMemberLevel ()+"]");
            int rec = yjjMemberMapper.insert (yjjMember);
            if (rec != 1) {
                throw BizException.defulat ().msg ("创建失败");
            }
        }
        else {
            if (!ObjectUtils.nullSafeEquals (yjjMember.getDelState (), 0)) {
                int rec = yjjMemberMapper.switchToDelState (yjjMember.getId (), 0);
                if (rec != 1) {
                    throw BizException.defulat ().msg ("更新失败");
                }
            }
        }
    }



    /**
     * 关闭会员
     *
     * @param storeId 门店用户id
     * @author Charlie
     * @date 2018/8/14 17:47
     */
    @Override
    public void closeMemberShipAccount(Long storeId) {
        YjjMember yjjMember = yjjMemberMapper.selectOne (storeId, SystemPlatform.STORE.getCode ());
        if (null == yjjMember || ObjectUtils.nullSafeEquals (yjjMember.getDelState (), 1)) {
            return;
        }
        else {
            int rec = yjjMemberMapper.switchToDelState (yjjMember.getId (), 1);
            if (rec != 1) {
                throw BizException.defulat ().msg ("更新失败");
            }
        }
    }



    /**
     * 根据用户id查询 会员信息(统一入口)
     *
     * @param systemPlatform systemPlatform
     * @param userId         userId
     * @return com.yujj.entity.product.YjjMember
     * @author Charlie
     * @date 2018/8/16 10:45
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public YjjMember findMemberByUserId(SystemPlatform systemPlatform, Long userId) {
        logger.info ("查询用户={} 会员信息", userId);
        YjjMember member = yjjMemberMapper.selectOne (userId, systemPlatform.getCode ());
        //查询会员是否过期,更新过期信息
        return member;
    }



    /**
     * 刷新会员过期时间
     *
     * @param member member
     * @return 是否更新了 true 更新
     * @author Charlie
     * @date 2018/8/16 7:53
     */
    @Override
    public void refreshExpirationTime(YjjMember member) {
        if (member == null) {
            return;
        }
        ValidTimeQueueHelper helper = ValidTimeQueueHelper.instance (member);
        boolean hasDirtyEndTime = helper.isHasDirtyEndTime ();
        if (hasDirtyEndTime) {
            final MemberLog log = MemberLog.createLog (member);
            helper.clearExpirationTime ();
            Long endTime = helper.getEndTime ();
            Integer type = helper.getMemberPackageType ();
            String validTimeQueue = helper.getValidTimeQueue ();
            int rec = yjjMemberMapper.updateEndTime (member.getId (), type, endTime, 0D, validTimeQueue, member.getValidTimeQueue ());
            if (rec != 1) {
                throw new RuntimeException ("过期会员更新账户过期时间失败");
            }
            logger.info ("过期会员更新账户过期时间成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", type, endTime, 0, validTimeQueue, member.getValidTimeQueue ());

//            member = yjjMemberMapper.selectOne (member.getUserId (), member.getPlatformType ());
//            MemberLog log1 = MemberLog.recAfterData (member, log);
            MemberLog log1 = log;
            ((Runnable) () -> {
                log1.setSource ("重设用户过期时间");
                memberLogMapper.insertSelective (log1);
            }).run ();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearHistoryDirtyData() {
        List<YjjMember> members = yjjMemberMapper.selectDirtyEndTime ();
        if (members != null && members.size () > 0) {
            for (YjjMember member : members) {
                refreshExpirationTime (member);
            }
        }
    }


}
