package com.jiuyuan.service.common;

import com.admin.common.constant.factory.PageFactory;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.dao.mapper.store.MemberLogMapper;
import com.jiuyuan.dao.mapper.store.YjjMemberMapper;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreLoginDelegator;
import com.yujj.entity.member.MemberOperatorRequest;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import com.yujj.entity.product.MemberLog;
import com.yujj.entity.product.MemberPackageVo;
import com.yujj.entity.product.ValidTimeQueueHelper;
import com.yujj.entity.product.YjjMember;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.jiuyuan.service.common.DataDictionaryService.MEMBER_PACKAGE_TYPE_GROUP_KEY;
import static com.yujj.entity.product.ValidTimeQueueHelper.buildValidTimeQueue;

/**
 * 会员表
 * Create by hyf on 2018/8/13
 */
@Service
public class YjjMemberService implements IYjjMemberService{

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 未删除
     */
    private static final int NO_DELETE = 0;
    private static final int FIRST_LOGIN = 1;
    private static final Logger logger = LoggerFactory.getLogger (YjjMemberService.class);
    @Autowired
    private YjjMemberMapper yjjMemberMapper;

    @Autowired
    private StoreBusinessNewService storeBusinessService;
    @Autowired
    private MemberLogMapper memberLogMapper;
    @Autowired
    private StoreBusinessNewService storeBusinessNewService;

    /**
     * 根据用户id查询 会员信息
     * <p>
     * 如果会员已过期,已删除,返回null
     * </p>
     */
    @Override
    public YjjMember findValidMemberByUserId(SystemPlatform systemPlatform, Long userId) {
        logger.info ("查询用户={} 会员信息", userId);
        YjjMember member = findMemberByUserId (systemPlatform, userId);
        if (member == null || member.getDelState () !=0 || member.getEndTime () < System.currentTimeMillis ()) {
            return null;
        }
        return member;
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
        logger.info ("查询用户={} 会员refreshExpirationTime信息", userId);
        YjjMember entity = new YjjMember ();
        entity.setPlatformType (systemPlatform.getCode ());
        entity.setUserId (userId);
        YjjMember member = yjjMemberMapper.selectOne (entity);
        //查询会员是否过期,更新过期信息
        return refreshExpirationTime (member, 0);
    }


    /**
     * 刷新会员过期时间
     *
     * @param member member
     * @return 是否更新了 true 更新
     * @author Charlie
     * @date 2018/8/16 7:53
     */
    private YjjMember refreshExpirationTime(YjjMember member, int max) {
        if (member == null) {
            return member;
        }
        if (max == 10) {
            //最大递归10次,绝对不能有无限循环
            return member;
        }
        ValidTimeQueueHelper helper = ValidTimeQueueHelper.instance (member);
        boolean hasDirtyEndTime = helper.isHasDirtyEndTime ();
        if (hasDirtyEndTime) {
//            final MemberLog log = MemberLog.createLog (member);
            helper.clearExpirationTime ();
            Long endTime = helper.getEndTime ();
            Integer type = helper.getMemberPackageType ();
            String validTimeQueue = helper.getValidTimeQueue ();
            int rec = yjjMemberMapper.updateEndTime (member.getId (), type, endTime, 0D, validTimeQueue, member.getValidTimeQueue ());
            if (rec != 1) {
                throw new RuntimeException ("过期会员更新账户过期时间失败");
            }
            logger.info ("过期会员更新账户过期时间成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", type, endTime, 0, validTimeQueue, member.getValidTimeQueue ());

            member.setValidTimeQueue(validTimeQueue);
            member.setType(type);
            member.setEndTime(endTime);

//            member = yjjMemberMapper.selectById (member.getId ());
//            MemberLog log1 = MemberLog.recAfterData (member, log);
//            ((Runnable) () -> {
//                log1.setSource ("重设用户过期时间");
//                memberLogMapper.insert (log1);
//            }).run ();
        }
        Long endTime = member.getEndTime();
        if (endTime < 1 || System.currentTimeMillis() < endTime) {
            return member;
        }
        else {
            //继续刷新
            return refreshExpirationTime(member, max+1);
        }
    }


    /**
     * 用户成功购买会员套餐
     *
     * @param systemPlatformCode  系统平台 暂时都只是1
     * @param userId            用户id
     * @param totalMoney        支付总额
     * @param memberPackageType 套餐类型
     * @param orderNo           订单编号
     * @author Charlie
     * @date 2018/8/16 7:37
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void buyMemberPackageOK(Integer systemPlatformCode, Long userId, Double totalMoney, Integer memberPackageType, String orderNo) {
        SystemPlatform sys = SystemPlatform.build (systemPlatformCode);
        logger.info ("购买会员服务成功开始更新,更新用户会员和小程序信息start ===> systemPlatformCode = [" + systemPlatformCode + "], userId = [" + userId + "], totalMoney = [" + totalMoney + "], memberPackageType = [" + memberPackageType + "], orderNo = [" + orderNo + "]");
        if (sys == null) {
            throw new RuntimeException ("目前不支持的其他系统购买会员");
        }

        //查找套餐的配置
        List<DataDictionary> dictList = dataDictionaryService.getByGroupAndLikeComment (MEMBER_PACKAGE_TYPE_GROUP_KEY, "\"memberType\":\"" + memberPackageType + "\"");
        if (dictList.isEmpty ()) {
            logger.error ("未找到会员套餐的信息 memberPackageType:{}", memberPackageType);
            throw new RuntimeException ("未找到会员套餐的信息");
        }
        if (dictList.size () > 1) {
            logger.error ("找到多个套餐类型 memberPackageType:{}", memberPackageType);
            throw new RuntimeException ("找到多个套餐类型");
        }
        DataDictionary dict = dictList.get (0);
        MemberPackageVo memberPackageVo = BizUtil.json2bean (dict.getComment (), MemberPackageVo.class);
        if (null == memberPackageVo) {
            logger.error ("没有会员套餐的配置 comment:{}", dict.getComment ());
            throw new RuntimeException ("没有会员套餐的配置");
        }

        YjjMember member = findMemberByUserId (SystemPlatform.STORE, userId);
        MemberLog log;
        if (member == null) {
            //没有就创建
            member = createMemberAccount (SystemPlatform.STORE, userId, totalMoney, memberPackageVo, 1);
            //记录日志
            log = MemberLog.createLog (member);
            log.setSource ("用户成功购买会员套餐,创建一个新的会员账户");
        }
        else {
            //用户会员续费
            accountRenew (member, totalMoney, memberPackageVo);
            //记录日志
            log = MemberLog.createLog (member);
            log.setSource ("用户成功购买会员套餐,用户会员账户续费");
//            YjjMember aftRec = yjjMemberMapper.selectById (member.getId ());
//            MemberLog.recAfterData (aftRec, log);
        }

        //更新小程序服务到期时间
        if (sys == SystemPlatform.STORE) {
            logger.info ("购买会员套餐,更新小程序关闭日期start....");
            StoreBusiness store = storeBusinessService.getById (userId);
            if (store == null) {
                throw new RuntimeException ("未找到用户信息");
            }
            Long wxaCloseTime = store.getWxaCloseTime ();
            wxaCloseTime = wxaCloseTime == null ? 0 :wxaCloseTime;
            Calendar calendar = Calendar.getInstance ();
            //是有已有小程序
            boolean firstBuyWeiChat = wxaCloseTime == 0 || wxaCloseTime < System.currentTimeMillis ();
            if (firstBuyWeiChat) {
                //ignore 首次购买, 客服设置时间
            }
            else {
                //延时
                calendar.setTimeInMillis (wxaCloseTime);
                calendar.add (Calendar.YEAR, 1);
                int rec = storeBusinessService.updateWxaCloseTime(store.getId (), calendar.getTimeInMillis (), wxaCloseTime);
                if (rec != 1) {
                    logger.error ("更新用户小程序关闭时间失败 storeId[{}].预计设置时间[{}]", store.getId (), calendar.getTimeInMillis ());
                    throw new RuntimeException ("更新用户小程序关闭时间失败");
                }
                logger.info ("更新用户小程序关闭时间成功 storeId[{}].预计设置时间[{}]", store.getId (), calendar.getTimeInMillis ());
                log.setBeforeWxClosedTime ( wxaCloseTime);
                log.setAfterWxClosedTime ( calendar.getTimeInMillis ());
            }
        }


        MemberLog finalLog = log;
        ((Runnable) () -> {
            finalLog.setOrderNo (orderNo);
            memberLogMapper.insert (finalLog);
        }).run ();
    }

    @Transactional
    @Override
    public JsonResponse addMember(MemberOperatorRequest memberOperatorRequest) {
        if (memberOperatorRequest.getMemberId()!=null){
            logger.info("修改会员 memberId={}",memberOperatorRequest.getMemberId());
            return upMember(memberOperatorRequest);
        }
        StoreBusiness user = storeBusinessNewService.getStoreBusinessByPhone(memberOperatorRequest.getPhone());
//        if (user!=null&&memberOperatorRequest.getMemberId()==null){
//            logger.warn("该会员已存在 请勿重复添加");
//            return JsonResponse.fail("该会员已存在 请勿重复添加");
//        }
        YjjMember yjjMember = new YjjMember();

        if (user!=null){
            yjjMember.setUserId(user.getId());
            YjjMember member = yjjMemberMapper.selectOne(yjjMember);
            if (member!=null){
//            StoreBusiness storeBusiness = storeBusinessNewService.getById(yjjMember.getUserId());
//            if (user!=null&&!storeBusiness.getPhoneNumber().equals(memberOperatorRequest.getPhone())){
//                logger.warn("手机号已存在");
//                return JsonResponse.fail("手机号已存在");
//            }
                logger.warn("该会员已存在");
                return JsonResponse.fail("该会员已存在");
            }
            Integer type = memberOperatorRequest.getType();
            buyMemberPackageOK(1,user.getId(),0d,type,"0");
        }
        if (user==null){
            logger.info("用户未注册 注册新用户");
            user = StoreLoginDelegator.buildDefaultStore();
//            user.setBusinessName(memberOperatorRequest.getName());
//            user.setProvince(memberOperatorRequest.getProvince());
//            user.setCity(memberOperatorRequest.getCity());
//            user.setCounty(memberOperatorRequest.getDistrict());
            user.setFirstLoginStatus(FIRST_LOGIN);
            user.setPhoneNumber(memberOperatorRequest.getPhone());
            storeBusinessNewService.add(user);
            user.setBusinessNumber(user.getId().longValue() + 800000000);
            storeBusinessNewService.update(user);

            logger.info("未注册用户 添加会员 phone={}",memberOperatorRequest.getPhone());
            Integer type = memberOperatorRequest.getType();
            buyMemberPackageOK(1,user.getId(),0d,type,"0");
        }
            StoreBusiness user2 = storeBusinessNewService.getStoreBusinessByPhone(memberOperatorRequest.getPhone());
            yjjMember.setUserId(user2.getId());
            YjjMember  isIn  = yjjMemberMapper.selectOne(yjjMember);
            isIn.setCanal("线下");
            isIn.setType(memberOperatorRequest.getType());
            isIn.setDelState(memberOperatorRequest.getDelState());
            isIn.setCity(memberOperatorRequest.getCity());
            isIn.setDistrict(memberOperatorRequest.getDistrict());
            isIn.setProvince(memberOperatorRequest.getProvince());
            isIn.setName(memberOperatorRequest.getName());
            yjjMemberMapper.updateById(isIn);


//        if (memberOperatorRequest.getMemberId()==null){
//            logger.info("添加会员 phone={}",memberOperatorRequest.getPhone());
//            Integer type = memberOperatorRequest.getType();
//            buyMemberPackageOK(1,user.getId(),0d,type,"0");
//            yjjMember.setCanal("线下");
//        }

/*

        logger.info("修改会员 phone={}，memberId={}",memberOperatorRequest.getPhone(),memberOperatorRequest.getMemberId());
        YjjMember  isIn  =  yjjMemberMapper.selectById(memberOperatorRequest.getMemberId());
        YjjMember  member  =  new YjjMember();
        if (isIn==null){
            StoreBusiness user2 = storeBusinessNewService.getStoreBusinessByPhone(memberOperatorRequest.getPhone());
            member.setUserId(user2.getId());
            isIn = yjjMemberMapper.selectOne(member);
        }
        StoreBusiness storeBusiness = storeBusinessNewService.getById(isIn.getUserId());
        if (user!=null&&!storeBusiness.getPhoneNumber().equals(memberOperatorRequest.getPhone())){
            logger.warn("手机号已存在");
            return JsonResponse.fail("手机号已存在");
        }
        Long memberId = null;
        if (memberOperatorRequest.getMemberId()!=null){
            memberId = memberOperatorRequest.getMemberId();
        }else {
            memberId=isIn.getId();
        }
        yjjMember.setId(memberId);
        yjjMember.setType(memberOperatorRequest.getType());
        yjjMember.setDelState(memberOperatorRequest.getDelState());
        yjjMember.setCity(memberOperatorRequest.getCity());
        yjjMember.setDistrict(memberOperatorRequest.getDistrict());
        yjjMember.setProvince(memberOperatorRequest.getProvince());
        yjjMember.setName(memberOperatorRequest.getName());
        yjjMemberMapper.updateById(yjjMember);
        //修改 省市县 手机号等
        memberOperatorRequest.setId(isIn.getUserId());
        storeBusinessNewService.updateInformation(memberOperatorRequest);
*/
        return JsonResponse.successful();
    }

    public JsonResponse upMember(MemberOperatorRequest memberOperatorRequest){
        StoreBusiness user = storeBusinessNewService.getStoreBusinessByPhone(memberOperatorRequest.getPhone());
        YjjMember yjjMember = new YjjMember();
        logger.info("修改会员 phone={}，memberId={}",memberOperatorRequest.getPhone(),memberOperatorRequest.getMemberId());
        YjjMember  isIn  =  yjjMemberMapper.selectById(memberOperatorRequest.getMemberId());
        YjjMember  member  =  new YjjMember();
        if (isIn==null){
            StoreBusiness user2 = storeBusinessNewService.getStoreBusinessByPhone(memberOperatorRequest.getPhone());
            member.setUserId(user2.getId());
            isIn = yjjMemberMapper.selectOne(member);
        }
        StoreBusiness storeBusiness = storeBusinessNewService.getById(isIn.getUserId());
        if (user!=null&&!storeBusiness.getPhoneNumber().equals(memberOperatorRequest.getPhone())){
            logger.warn("手机号已存在");
            return JsonResponse.fail("手机号已存在");
        }
        Long memberId = null;
        if (memberOperatorRequest.getMemberId()!=null){
            memberId = memberOperatorRequest.getMemberId();
        }else {
            memberId=isIn.getId();
        }
        Integer type = memberOperatorRequest.getType();
        yjjMember.setId(memberId);
        yjjMember.setType(type);
        yjjMember.setDelState(memberOperatorRequest.getDelState());
        yjjMember.setCity(memberOperatorRequest.getCity());
        yjjMember.setDistrict(memberOperatorRequest.getDistrict());
        yjjMember.setProvince(memberOperatorRequest.getProvince());
        yjjMember.setName(memberOperatorRequest.getName());

        String historyValidTimeQueue = isIn.getValidTimeQueue();
        if (StringUtils.isNotBlank(historyValidTimeQueue)) {
            historyValidTimeQueue = historyValidTimeQueue.trim();
            String[] queues = historyValidTimeQueue.split("]");
            if (queues.length > 1) {
                int offset = historyValidTimeQueue.indexOf("[", 1);
                historyValidTimeQueue = historyValidTimeQueue.substring(offset, historyValidTimeQueue.length());
            }
            else {
                historyValidTimeQueue = "";
            }
            logger.info("截取后的日期队列={}", historyValidTimeQueue);
        }

        Long historyEndTime = isIn.getEndTime();
        String validTime;
        if (historyEndTime == null || historyEndTime <= 0) {
            validTime = "";
            logger.info("过期时间是0");
        }
        else {
            validTime = String.format("[%s:%s]", historyEndTime, type) + historyValidTimeQueue ;
            logger.info("时间队列更新={}", validTime);
        }
        yjjMember.setValidTimeQueue(validTime);
        yjjMemberMapper.updateById(yjjMember);
        //修改 省市县 手机号等
        memberOperatorRequest.setId(isIn.getUserId());
        storeBusinessNewService.updateInformation(memberOperatorRequest);

        return JsonResponse.successful();
    }
    @Override
    public Page<MemberOperatorResponse> findAllMembers(MembersFindRequest membersFindRequest) {
//        Wrapper<MembersFindRequest> wrapper = new EntityWrapper<MembersFindRequest>();
        Page<MemberOperatorResponse> page = new PageFactory<MemberOperatorResponse>().defaultPage();
//        wrapper.like("a.PhoneNumber",membersFindRequest.getPhone());
//        wrapper.le("b.createTime",membersFindRequest.getEndTime());
//        wrapper.gt("b.createTime",membersFindRequest.getStartTime());
//        wrapper.eq("b.del_state",membersFindRequest.getDelState());

        List<MemberOperatorResponse> list = storeBusinessNewService.selectMyPageLists(page,membersFindRequest);
        page.setRecords(list);
        return page;
    }


    /**
     * 账号续费
     *
     * @param history           账号当前状态
     * @param totalMoney        续费金额
     * @param memberPackageVo 会员套餐详情
     * @author Charlie
     * @date 2018/8/16 8:14
     */
    private void accountRenew(YjjMember history, Double totalMoney, MemberPackageVo memberPackageVo) {
        /*一切以新系统为主, 老系统的业务是, 当前只有APP端免费的会员才能续费, 其他的都不会走这里的逻辑
         * 这里的逻辑只用了一部分
         */
        logger.info ("账号续费start...");
        boolean isUserIsMemberNow = history.getEndTime () > System.currentTimeMillis ();
        if (isUserIsMemberNow) {
            //当前会员期延长
            ValidTimeQueueHelper helper = ValidTimeQueueHelper.instance (history).add (memberPackageVo.getMemberType (), memberPackageVo.getTimeUnit (), memberPackageVo.getValue ());
            int rec = yjjMemberMapper.updateEndTime (history.getId (), helper.getMemberPackageType (), helper.getEndTime (), totalMoney, helper.getValidTimeQueue (), history.getValidTimeQueue ());
            if (rec != 1) {
                throw new RuntimeException ("过期会员回归, 更新会员账号有效期失败");
            }
            logger.info ("过期会员回归, 更新会员账号有效期成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", memberPackageVo.getMemberType (), helper.getEndTime (), totalMoney, helper.getValidTimeQueue (), history.getValidTimeQueue ());
        }
        else {
            //过期会员回归
            ValidTimeQueueHelper helper = ValidTimeQueueHelper.instance (history).add (memberPackageVo.getMemberType (), memberPackageVo.getTimeUnit (), memberPackageVo.getValue ());
            int rec = yjjMemberMapper.updateEndTime (history.getId (), memberPackageVo.getMemberType (), helper.getEndTime (), totalMoney, helper.getValidTimeQueue (), history.getValidTimeQueue ());
            if (rec != 1) {
                throw new RuntimeException ("过期会员回归, 更新会员账号有效期失败");
            }
            logger.info ("过期会员回归, 更新会员账号有效期成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", memberPackageVo.getMemberType (), helper.getEndTime (), totalMoney, helper.getValidTimeQueue (), history.getValidTimeQueue ());
        }
    }


    /**
     * 创建一个会员账号
     *
     * @param systemPlatform 系统平台
     * @param userId         用户id
     * @param totalMoney     支付(支付金额用实际支付金额)
     * @param memberPackageVo    套餐详情(用户等级会员的类型和会员的有效期)
     * @param level          用户等级
     * @author Charlie
     * @date 2018/8/16 8:05
     */
    private YjjMember createMemberAccount(SystemPlatform systemPlatform, Long userId, Double totalMoney, MemberPackageVo memberPackageVo, Integer level) {
        logger.info ("创建一个会员账号 ==> systemPlatform = [" + systemPlatform + "], userId = [" + userId + "], totalMoney = [" + totalMoney + "], memberPackageVo = [" + memberPackageVo.toString () + "], level = [" + level + "]");
        YjjMember yjjMember = new YjjMember ();
        yjjMember.setPlatformType (systemPlatform.getCode ());
        yjjMember.setUserId (userId);
        yjjMember.setMemberLevel (level);
        yjjMember.setMoneyTotal (new BigDecimal (String.valueOf (totalMoney)));
        yjjMember.setDelState (NO_DELETE);
        yjjMember.setType (memberPackageVo.getMemberType ());
        Calendar calendar = Calendar.getInstance ();
        calendar = ValidTimeQueueHelper.addTime (calendar, memberPackageVo.getTimeUnit (), memberPackageVo.getValue ());
        long endTime = calendar.getTimeInMillis ();
        yjjMember.setEndTime (endTime);
        yjjMember.setValidTimeQueue (buildValidTimeQueue(endTime, memberPackageVo.getMemberType ()));
        int rec = yjjMemberMapper.insert (yjjMember);
        if (rec != 1) {
            logger.error ("创建会员账号失败 systemPlatform = [" + systemPlatform + "], userId = [" + userId + "]" );
            throw new RuntimeException ("创建会员账号失败");
        }
        logger.info ("创建一个新的会员账号成功 systemPlatform = [" + systemPlatform + "], userId = [" + userId + "]" );
        return yjjMember;
    }


    public static void main(String[] args) {
        String s = "[1609567696870:8][321313131:2][1523543253:4]";
        int i = s.indexOf("[", 1);
        System.out.println("i = " + i);
        s = s.substring(i, s.length());
        System.out.println("s = " + s);
    }
}
