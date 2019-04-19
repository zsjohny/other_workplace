package com.e_commerce.miscroservice.distribution.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import com.e_commerce.miscroservice.commons.entity.distribution.UnderMyClassAResponse;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.DistributionGradeEnum;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.conver.NumberUtils;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.distribution.dao.DistributionSystemDao;
import com.e_commerce.miscroservice.distribution.rpc.activity.ShopShareRpcService;
import com.e_commerce.miscroservice.distribution.rpc.order.ShopMemberOrderRpcService;
import com.e_commerce.miscroservice.distribution.service.*;
import com.e_commerce.miscroservice.commons.utils.DistributionSystemUtil;
import com.e_commerce.miscroservice.distribution.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DistributionSystemImpl implements DistributionSystemService {

    private Log logger = Log.getInstance(DistributionSystemImpl.class);


    @Autowired
    private ShopShareRpcService shopShareRpcService;

    @Autowired
    @Qualifier("userJsonObjHashRedisTemplate")
    private ValueOperations<String,JSONObject> distributionRedis;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private DstbEarningsStrategyService dstbEarningsStrategyService;

    @Autowired
    private DistributionSystemDao distributionSystemDao;

    @Autowired
    private DistributionSystemUtil distributionSystemUtil;

    @Autowired
    private ShopMemberOrderRpcService shopMemberOrderRpcService;
    @Autowired
    private ShopMemberAccountService shopMemberAccountService;
    @Autowired
    private ShopMemberAccountCashOutInService shopMemberAccountCashOutInService;
    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 上级，上上级，合伙人
     */
    private static String DISTRIBUTION_GRADE_PARTNER_USERID="distribution:grade:%s";
    /**
     * 升级条件
     */
    private static String UPGRADE_CONDITION="distribution:upgrade:%s";
    private static final  String PARTNER = "partner";
    private static final  String DISTRIBUTOR = "distributor";
    private static final  String HIGHER = "higher";
    private static final  String TOP = "top";
    private static final  String GRADE = "grade";
    private static final  String SON_LIST = "sonList";
    private static final  String CLASS_A = "classA";
    private static final  String BUY_COUNT = "buyCount";
    private static final  String CLASS_A_B = "classAB";
    private static final  String COUNT_MONEY = "countMoney";


    /**
     * 查询 上级 上上级 合伙人 分销商
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject find(Long userId) {
        return distributionSystemUtil.find(userId);
    }

    /**
     * 添加上级
     *
     * @param userId
     * @param upUserId
     * @return
     */
    @Override
    public Response add(Long userId, Long upUserId) {
        return distributionSystemUtil.add(userId,upUserId);
    }

    /**
     * 添加自己
     *
     * @param userId
     * @param grade
     * @return
     */
    @Override
    public Response addSelf(Long userId, Integer grade) {
        return distributionSystemUtil.addSelf(userId, grade);
    }

    /**
     * 更新
     *
     * @param userId
     * @param grade
     * @return
     */
    @Override
    public Response update(Long userId, Integer grade) {
        return distributionSystemUtil.update(userId, grade);
    }

    @Override
    public Response redisChangeToMysql(Long userId) {
        JSONObject jsonObject = find(userId);
        if (jsonObject==null){
            return Response.errorMsg("错误");
        }
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info("----------------------------------------------------------------------------");
        logger.info("----------------------------------------------------------------------------");
        logger.info("----------------------------------------------------------------------------");
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Long partner = jsonObject.getLong(PARTNER);
        Long distributor = jsonObject.getLong(DISTRIBUTOR);
        Long higher = jsonObject.getLong(HIGHER);
        Long top = jsonObject.getLong(TOP);
        Integer grade = jsonObject.getInteger(GRADE);
        JSONArray sunArray = jsonObject.getJSONArray(SON_LIST);
        DistributionSystem system = new DistributionSystem();
        system.setDistributorId(distributor);
        system.setPartnerId(partner);
        system.setHigherUp(higher);
        system.setTopUp(top);
        system.setGrade(grade);
        system.setUserId(userId);
        distributionSystemDao.save(system);
        if(sunArray!=null&&sunArray.size()>0){
            addToAdd(sunArray);
        }

        return Response.success();
    }

    /**
     * 查找我的团队信息
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject findCountTeam(Long userId) {
        logger.info("userId={}查询团队人员统计信息",userId);
        JSONObject jasonObject = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        if (jasonObject==null){
            return null;
        }
        Integer grade = jasonObject.getInteger(GRADE);
        JSONObject jsonObject = new JSONObject();
        if (grade .equals(DistributionGradeEnum.DISTRIBUTOR.getCode()) ){
            Integer count = distributionSystemDao.underDistributorSize(userId);
            Integer countStore = distributionSystemDao.findUnderDistributorStore(userId);
            Integer countToday = distributionSystemDao.findUnderDistributorCountToday(userId);
            jsonObject.put("count",count);
            jsonObject.put("countStore",countStore);
            jsonObject.put("countToday",countToday);
            return jsonObject;
        }
        if (grade.equals( DistributionGradeEnum.PARTNER.getCode())){
            Integer count = distributionSystemDao.underPartnerSize(userId);
            Integer countDistributor = distributionSystemDao.findUnderPartnerDistributor(userId);
            Integer countStore = distributionSystemDao.findUnderPartnerStore(userId);
            Integer countToday = distributionSystemDao.findUnderPartnerCountToday(userId);
            jsonObject.put("count",count);
            jsonObject.put("countDistributor",countDistributor);
            jsonObject.put("countStore",countStore);
            jsonObject.put("countToday",countToday);
            return jsonObject;
        }

        return null;
    }

    /**
     * 我的粉丝信息
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject findCountFollower(Long userId) {
        logger.info("userId={}查询粉丝人员统计信息",userId);
        JSONObject jsonObject = new JSONObject();
        Integer countClassA = distributionSystemDao.findClassA(userId);
        Integer countClassB = distributionSystemDao.findClassB(userId);
        Integer todayIncrease = distributionSystemDao.todayIncrease(userId);
        Integer count = NumberUtils.adds(0,countClassA,countClassB).intValue();
        jsonObject.put("count",count);
        jsonObject.put("countClassA",countClassA);
        jsonObject.put("countClassB",countClassB);
        jsonObject.put("todayIncrease",todayIncrease);
        return jsonObject;
    }
    /**
     * 我的一级粉丝明细
     * @param userId
     * @param page
     * @return
     */
    @Override
    public Response findFollowerDetails(Long userId, Integer page) {
        PageHelper.startPage(page,10);
        List<UnderMyClassAResponse> list = distributionSystemDao.findFollowerDetails(userId);
        return Response.success(list);
    }

    @Override
    public JSONObject promoteCondition(Long userId) {

        JSONObject response = new JSONObject();

        //分销团队的收益策略
        Map<String, EarningsStrategyVo> earningsStrategy = dstbEarningsStrategyService.allStrategy ();
        logger.info ("分销收益 strategy.size={}", earningsStrategy == null ? -1: earningsStrategy.size ());
        response.put ("earningsStrategy", earningsStrategy);


        //升级条件, 用户已完成指数
        JSONObject userDstbInfo = distributionSystemUtil.find(userId);
        String gradeConditionStr = userStrHashRedisTemplate.get(String.format(UPGRADE_CONDITION,userId));
        Integer gradeCondition = -1;
        if (gradeConditionStr!=null){
            gradeCondition =Integer.parseInt(gradeConditionStr);
        }
        Integer grade = 0;
        if (userDstbInfo!=null) {
            grade = userDstbInfo.getInteger(GRADE);
        }


        Integer status = -1;
        ShopAuditManagement shopAuditManagement = distributionSystemDao.findShopAuditManage(userId);
        if (shopAuditManagement!=null&&shopAuditManagement.getBeforeRole().equals(grade)){
//            jsonObject.put("button")
            status = shopAuditManagement.getStatus();
            response.put("explain",shopAuditManagement.getAuditExplain());
        }
        response.put("status",status);
        Boolean isTrue = Boolean.FALSE;
        response.put("isTrue",isTrue);
        if (userDstbInfo!=null){
//            grade = jasonObject.getInteger(GRADE);
            response.put("grade",grade);
//            Double money = shopMemberOrderRpcService.montyTotalMoney(userId,grade);
            Integer classA = distributionSystemDao.findClassA(userId);
            Integer classB = distributionSystemDao.findClassB(userId);
            Integer classAB = NumberUtils.adds(0,classA,classB).intValue();
            //晋升店长
            if (grade.equals(DistributionGradeEnum.COMMON.getCode())){
                DataDictionary dataDictionary = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.UPGRADE_CONDITION_STORE.getCode(),DataDictionaryEnums.UPGRADE_CONDITION_STORE.getGroupCode());
                JSONObject conditionJson = JSONObject.parseObject(dataDictionary.getVal());
                response.put("condition",conditionJson);
//               if (gradeCondition.equals(DistributionGradeEnum.STORE.getCode())){
//                   isTrue=Boolean.TRUE;
//                   jsonObject.put("isTrue",isTrue);
////                   return Response.success(jsonObject);
//               }
                JSONObject doneJson = new JSONObject();
                List<ShopMemberOrder> list = shopMemberOrderRpcService.findOrderList(userId,1);
                Integer size =list==null?0:list.size();
                doneJson.put(CLASS_A,classA);
                doneJson.put(BUY_COUNT,size);
                response.put("done",doneJson);

                Integer conditionClassA = conditionJson.getInteger(CLASS_A);
                Integer conditionBuyCount = conditionJson.getInteger(BUY_COUNT);
                if (classA>=conditionClassA&&size>=conditionBuyCount){
                    isTrue=Boolean.TRUE;
                    response.put("isTrue",isTrue);
                    userStrHashRedisTemplate.set(String.format(UPGRADE_CONDITION,userId),String.valueOf(DistributionGradeEnum.STORE.getCode()));
                }

            }
            //晋升分销商
            if (grade.equals(DistributionGradeEnum.STORE.getCode())){
                DataDictionary dataDictionary = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.UPGRADE_CONDITION_DISTRIBUTOR.getCode(),DataDictionaryEnums.UPGRADE_CONDITION_DISTRIBUTOR.getGroupCode());
                JSONObject conditionJson = JSONObject.parseObject(dataDictionary.getVal());
                response.put("condition",conditionJson);
                if (gradeCondition.equals(DistributionGradeEnum.DISTRIBUTOR.getCode())){
                    logger.info("晋升分销商条件已满足无需重新查询");
                    isTrue=Boolean.TRUE;
                    response.put("isTrue",isTrue);
                    response.put("done",conditionJson);

                    return response;
                }
                JSONObject doneJson = new JSONObject();
                doneJson.put(CLASS_A,classA);
                doneJson.put(CLASS_A_B,classAB);
                Double countMoney = shopMemberOrderRpcService.montyTotalMoney(userId,DistributionGradeEnum.STORE.getCode());
                doneJson.put(COUNT_MONEY,countMoney);
                response.put("done",doneJson);

                Integer conditionClassA = conditionJson.getInteger(CLASS_A);
                Integer conditionClassAB = conditionJson.getInteger(CLASS_A_B);
                Integer conditionCountMoney = conditionJson.getInteger(COUNT_MONEY);
                if (classA>=conditionClassA&&classAB>=conditionClassAB&&countMoney>=conditionCountMoney){
                    isTrue=Boolean.TRUE;
                    response.put("isTrue",isTrue);
                    userStrHashRedisTemplate.set(String.format(UPGRADE_CONDITION,userId),String.valueOf(DistributionGradeEnum.DISTRIBUTOR.getCode()));
                }
            }
            //晋升合伙人
            if (grade.equals(DistributionGradeEnum.DISTRIBUTOR.getCode())){
                DataDictionary dataDictionary = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.UPGRADE_CONDITION_PARTNER.getCode(),DataDictionaryEnums.UPGRADE_CONDITION_PARTNER.getGroupCode());
                JSONObject conditionJson = JSONObject.parseObject(dataDictionary.getVal());
                response.put("condition",conditionJson);
                if (gradeCondition.equals(DistributionGradeEnum.PARTNER.getCode())){
                    logger.info("晋升合伙人条件已满足无需重新查询");
                    isTrue=Boolean.TRUE;
                    response.put("isTrue",isTrue);
                    response.put("done",conditionJson);

                    return response;
                }
                JSONObject doneJson = new JSONObject();
                Integer distributorSize = distributionSystemDao.findUnderDistributorDistributor(userId);
                doneJson.put(DISTRIBUTOR,distributorSize);
                Double countMoney = shopMemberOrderRpcService.montyTotalMoney(userId,DistributionGradeEnum.DISTRIBUTOR.getCode());
                doneJson.put(COUNT_MONEY,countMoney);
                response.put("done",doneJson);
                Integer conditionDistributor = conditionJson.getInteger(DISTRIBUTOR);
                Integer conditionCountMoney = conditionJson.getInteger(COUNT_MONEY);
                if (distributorSize>=conditionDistributor&&countMoney>=conditionCountMoney){
                    isTrue=Boolean.TRUE;
                    response.put("isTrue",isTrue);
                    userStrHashRedisTemplate.set(String.format(UPGRADE_CONDITION,userId),String.valueOf(DistributionGradeEnum.PARTNER.getCode()));
                }
            }

            response.put ("earningsStrategy", earningsStrategy);
            return response;

        }else {
            DataDictionary dataDictionary = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.UPGRADE_CONDITION_STORE.getCode(),DataDictionaryEnums.UPGRADE_CONDITION_STORE.getGroupCode());
            JSONObject conditionJson = JSONObject.parseObject(dataDictionary.getVal());
            response.put("condition",conditionJson);
            JSONObject doneJson = new JSONObject();
            List<ShopMemberOrder> list = shopMemberOrderRpcService.findOrderList(userId,1);
            Integer size =list==null?0:list.size();
            doneJson.put(CLASS_A,0);
            doneJson.put(BUY_COUNT,size);
            response.put("done",doneJson);
            response.put("grade",0);

            return response;

        }
//        shopMemberOrderRpcService.updateByOrderNoSelective(new ShopMemberOrder());
    }



    @Override
    public Response distributionProposer(Long userId, String realName, String wxNum, String phone, String idCard)
    {
        JSONObject jsonObject = distributionSystemUtil.find(userId);
        if (jsonObject==null){
            logger.warn("申请条件不满足");
            return Response.errorMsg("申请条件不满足");
        }
        Integer grade = jsonObject.getInteger(GRADE);
        String saveGrade = userStrHashRedisTemplate.get(String.format(UPGRADE_CONDITION,userId));
        if(saveGrade==null){
            logger.warn("申请条件不满足");
            return Response.errorMsg("申请条件不满足");
        }
        Integer gradeCondition = Integer.parseInt(saveGrade);

        if(grade.equals(gradeCondition)){
            logger.warn("申请条件不满足");
            return Response.errorMsg("申请条件不满足");
        }

        ShopAuditManagement auditManagement = new ShopAuditManagement();
        auditManagement.setApplicationRole(gradeCondition);
        auditManagement.setBeforeRole(grade);
        auditManagement.setCommitTime(String.valueOf(System.currentTimeMillis()));
        auditManagement.setIdCard(idCard);
        auditManagement.setPhone(phone);
        auditManagement.setWxNum(wxNum);
        auditManagement.setRealName(realName);
        auditManagement.setUserId(userId);

        distributionSystemDao.distributionProposer(auditManagement);
        //晋升店长
//        if (gradeCondition!=null&&gradeCondition.equals(DistributionGradeEnum.STORE.getCode())){
//        }


        return Response.success();
    }



    @Override
    public JSONObject myInformation(Long userId) {
        JSONObject countFollower = findCountFollower(userId);
        JSONObject countTeam = findCountTeam(userId);
        JSONObject userIncomeStatistics = shopMemberAccountService.getUserIncomeStatistics(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("countFollower",countFollower);
        jsonObject.put("countTeam",countTeam );
        jsonObject.put("userIncomeStatistics",userIncomeStatistics );
        JSONObject jasonObject = distributionSystemUtil.find(userId);
        UnderMyClassAResponse higherInformation = null;
        Integer grade = 0;
        if (jasonObject!=null){
            Long higher = jasonObject.getLong(HIGHER);
            higherInformation = distributionSystemDao.findHigherInformation(higher);
            grade = jasonObject.getInteger(GRADE);
        }
        jsonObject.put("grade",grade );
        jsonObject.put("higherInformation",higherInformation );
        return jsonObject;
    }
    /**
     * 绑定粉丝
     * @param userId
     * @param fans
     * @return
     */
    @Override
    public Response bindingFans(Long userId, Long fans) {
        logger.info("绑定粉丝userId={}，fans={}",userId,fans);
        DistributionSystem distribution = distributionSystemDao.findOne(fans);
        if (distribution!=null){
            logger.warn("fans={}已存在请勿重复添加上级",fans);
            return Response.errorMsg("已存在请勿重复添加上级");
        }
        DistributionSystem distributionSystem = distributionSystemDao.findOne(userId);
        if(distributionSystem==null){
            logger.info("userId={}绑定粉丝fans={}-新增",userId,fans);
            distributionSystemUtil.addSelf(userId,DistributionGradeEnum.COMMON.getCode());
            DistributionSystem system = new DistributionSystem();
            system.setUserId(userId);
            system.setGrade(DistributionGradeEnum.COMMON.getCode());
            //初始化 粉丝数量
            system.setFansUserCount(1L);
            distributionSystemDao.save(system);
        }
        JSONObject jsonObject = distributionSystemUtil.find(userId);
        Long distributor = jsonObject.getLong(DISTRIBUTOR);
        Long partner = jsonObject.getLong(PARTNER);
//        jsonObject.getLong(HIGHER);
        distributionSystemUtil.add(fans,userId);
        /***********************初始化粉丝***************************/
        DistributionSystem system = new DistributionSystem();
        system.setUserId(fans);
        system.setHigherUp(userId);
        system.setTopUp(jsonObject.getLong(HIGHER));
        system.setDistributorId(distributor);
        system.setPartnerId(partner);
        system.setGrade(DistributionGradeEnum.COMMON.getCode());
        /***********************初始化粉丝***************************/
        distributionSystemDao.save(system);
        this.toStore(userId, null);
        return Response.success();
    }


    /**
     * 自动晋升店长
     * @param memberId
     * @param successOrderNo 交易成功的订单号
     */
    @Override
    public void toStore(Long memberId, String successOrderNo){
        logger.info("memberId={}粉丝自动晋升店长--开始",memberId);
        /********************粉丝升级店长*********************/
        //        distributionSystemUtil.update()
        JSONObject jsonObject = distributionSystemUtil.find(memberId);
        logger.info ("memberId={}粉丝自动晋升店长--开始, userInfo={}",memberId, jsonObject);
        if (jsonObject!=null){
            Integer grade = jsonObject.getInteger(GRADE);
            //晋升店长
            if (grade.equals(DistributionGradeEnum.COMMON.getCode())){
                logger.info("user={}自动晋升店长",memberId);
                DataDictionary dataDictionary = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.UPGRADE_CONDITION_STORE.getCode(),DataDictionaryEnums.UPGRADE_CONDITION_STORE.getGroupCode());
                JSONObject conditionJson = JSONObject.parseObject(dataDictionary.getVal());
                jsonObject.put("condition",conditionJson);
                Integer classA = distributionSystemDao.findClassA(memberId);
                Integer size;
                if (StringUtils.isBlank (successOrderNo)) {
//                    List<ShopMemberOrder> list = shopMemberOrderRpcService.findOrderList (memberId, 2);
//                    size =list==null?0:list.size();

                    Long successSize = shopMemberOrderRpcService.countUserSuccessOrder(memberId, null);
                    logger.info ("用户已完成的订单数={},不排除订单号", successSize);
                    successSize = successSize == null ? 0 : successSize;
                    size = successSize.intValue ();
                }
                else {
                    //排除一个订单号,这个订单号因为异步的情况,可能在另一个系统,订单已完成但事务未提交
                    Long successSize = shopMemberOrderRpcService.countUserSuccessOrder(memberId, successOrderNo);
                    logger.info ("用户已完成的订单数={},排除订单号={} ", successSize, successOrderNo);
                    //加上一个已成功的
                    size = successSize == null ? 1 : successSize.intValue () + 1;
                }

                Integer conditionClassA = conditionJson.getInteger(CLASS_A);
                Integer conditionBuyCount = conditionJson.getInteger(BUY_COUNT);
                logger.info ("订单数 size={}", size);
                if (classA>=conditionClassA&&size>=conditionBuyCount){
                    logger.info("粉丝自动晋升店长 条件满足");
                    distributionSystemUtil.update(memberId,DistributionGradeEnum.STORE.getCode());
                    DistributionSystem distributionSystem = distributionSystemDao.findOne(memberId);
                    distributionSystem.setGrade(DistributionGradeEnum.STORE.getCode());
                    distributionSystemDao.update(distributionSystem);
                }
            }
        }
    }




    /**
     * 我的分销广告(分销收益+分享收益)
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 17:36
     */
    @Override
    public Map<String, Object> myInformationAd(Long userId) {


        Map<String, Object> res = new HashMap<> (2);
        //分销升级条件
        JSONObject conditionInfo = promoteCondition (userId);
        res.put ("conditionInfo", conditionInfo);

        //一级粉丝数量
        res.put ("countClassA", distributionSystemDao.findClassA(userId));

        //有效粉丝数量
        Long myEffectiveFans = shopShareRpcService.myEffectiveFans (userId);
        res.put ("myEffectiveFans", myEffectiveFans);

        //分享收益金额
        Map<String, BigDecimal> shareEarnings = shopMemberAccountCashOutInService.findHistoryShareEarnings(userId);
        BigDecimal shareEarningsCash = BigDecimal.ZERO;
        BigDecimal shareEarningsGoldCoin = BigDecimal.ZERO;
        if (shareEarnings != null) {
            shareEarningsCash = shareEarnings.get ("shareEarningsCash");
            shareEarningsGoldCoin = shareEarnings.get ("shareEarningsGoldCoin");
        }
        res.put ("shareEarningsCash", shareEarningsCash);
        res.put ("shareEarningsGoldCoin", shareEarningsGoldCoin);

        List<Map<String, Object>> recentlyPlatformShares = shopMemberAccountCashOutInService.recentlyPlatformShares();
        res.put ("recentlyPlatformShares", recentlyPlatformShares);
        return res;
    }




    public void addToAdd(JSONArray sunArray ){
        for (int i=0;i<sunArray.size();i++){
            logger.info("循环次数={}====TIME={}",System.currentTimeMillis(),i);

            JSONObject jsonObject = find(sunArray.getLong(i));
            Long partner = jsonObject.getLong(PARTNER);
            Long distributor = jsonObject.getLong(DISTRIBUTOR);
            Long higher = jsonObject.getLong(HIGHER);
            Long top = jsonObject.getLong(TOP);
            Integer grade = jsonObject.getInteger(GRADE);
            JSONArray array = jsonObject.getJSONArray(SON_LIST);
            DistributionSystem system = new DistributionSystem();
            if (distributor!=null){
                system.setDistributorId(distributor);
            }
            if (partner!=null){
                system.setPartnerId(partner);

            }
            if (higher!=null){
                system.setHigherUp(higher);

            }
            if (top!=null){
                system.setTopUp(top);

            }
            if (grade!=null){
                system.setGrade(grade);

            }
            system.setUserId(sunArray.getLong(i));
            distributionSystemDao.save(system);
            if (array!=null&&array.size()>0){
                addToAdd(array);
            }
        }
    }






}
