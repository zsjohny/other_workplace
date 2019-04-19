package com.jiuy.core.service.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.store.service.StoreLoginDelegator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.impl.sql.RefuseReasonMapper;
import com.jiuy.core.dao.impl.sql.StoreAuditMapper;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.member.RefuseReason;
import com.jiuy.core.meta.notification.Notification;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.coupon.StoreCouponService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.dao.mapper.supplier.GroundCustomerStageChangeMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.newentity.ground.GroundConstant;
import com.jiuyuan.entity.newentity.ground.GroundCustomerStageChange;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.GroundBonusGrantFacade;
import com.jiuyuan.service.common.IGroundConditionRuleService;
import com.jiuyuan.service.common.IGroundCustomerStageChangeService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.DoubleUtil;
import com.jiuyuan.util.GetuiUtil;
import com.jiuyuan.util.IdsToStringUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * @author qiuyuefan
 */
@Service
public class StoreAuditService{

    private static final int NO_FIRST_LOGIN = 2;
    private static final int FIRST_LOGIN = 1;

    private static final long ONE_DAY = 24L * 60 * 60 * 1000;
    @Autowired
    private IGroundConditionRuleService groundConditionRuleService;
    @Autowired
    private RefuseReasonMapper refuseReasonMapper;

    @Autowired
    private StoreAuditMapper storeAuditMapper;

    @Autowired
    private StoreBusinessDao storeBusinessDao;

    @Autowired
    private YunXinSmsService yunXinSmsService;

    @Autowired
    private GlobalSettingService globalSettingService;

    @Autowired
    private NotifacationService notifacationService;

    @Autowired
    private StoreBusinessService storeBusinessService;

    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;

    @Autowired
    private StoreCouponService storeCouponService;

    @Autowired
    private GroundConditionRuleMapper groundConditionRuleMapper;

    @Autowired
    private GroundCustomerStageChangeMapper groundCustomerStageChangeMapper;

    @Autowired
    private IGroundCustomerStageChangeService groundCustomerStageChangeService;

    @Autowired
    private GroundBonusGrantFacade groundBonusGrantFacade;

	@Autowired
	private GroundUserMapper groundUserMapper;

	@Autowired
	private GroundBonusGrantMapper groundBonusGrantMapper;

	/**
	 * 获取新门店审核列表
	 *
	 * @param status
	 *            状态:’0：提交审核 1：审核通过 -1:审核不通过’
	 * @param pageQuery
	 * @param keyWord
	 * @param isVip
	 * @param storeBusinessAddress
	 * @param storeBusinessName
	 *            // * @param storeType2
	 * @param storeBusinessId
	 * @param userName
	 * @param phoneNumber
	 * @param registTimeEnd
	 * @param registTimeStart
	 * @param referenceNumber
	 * @return
	 */
	public JsonResponse getAuditList(Integer status, PageQuery pageQuery, Integer isVip, String keyWord,
			String registTimeStart, String registTimeEnd, String phoneNumber, String userName, long storeBusinessId,
			String storeBusinessName, String storeBusinessAddress, String referenceNumber) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long startTime = -1;
			if (!StringUtils.isEmpty(registTimeStart)) {
				startTime = getLongTime(registTimeStart);
			}
			long endTime = 0;
			if (!StringUtils.isEmpty(registTimeStart)) {
				endTime = getLongTime(registTimeEnd);
			}
			if (startTime >= endTime) {
				throw new RuntimeException("结束时间>开始时间");
			}
			if (endTime > System.currentTimeMillis()) {
				throw new RuntimeException("结束时间<=当前时间");
			}
			int count = storeAuditMapper.selectAuditCount(status, isVip, keyWord, pageQuery, startTime, endTime,
					phoneNumber, userName, storeBusinessId, storeBusinessName, storeBusinessAddress, referenceNumber);
			List<StoreBusiness> storeBusinessList = storeBusinessDao.getByStoreIdVipKeyWord(status, isVip, keyWord,
					pageQuery, startTime, endTime, phoneNumber, userName, storeBusinessId, storeBusinessName,
					storeBusinessAddress, referenceNumber);
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> auditStoreList = new ArrayList<Map<String, Object>>();
			for (StoreBusiness storeBusiness : storeBusinessList) {
				Map<String, Object> auditStore = new HashMap<String, Object>();
				List<StoreAudit> storeAuditList = storeAuditMapper.selectAuditByStoreId(storeBusiness.getId(), status);
				if (storeAuditList.size() <= 0) {
					throw new RuntimeException("该商家并没有审核记录");
				}
				auditStore.put("id", storeAuditList.get(0).getId() + "");
				auditStore.put("storeId", storeBusiness.getId() + "");
				auditStore.put("phoneNumber", storeBusiness.getPhoneNumber() + "");
				auditStore.put("businessName", storeBusiness.getBusinessName());
				auditStore.put("legalPerson", storeBusiness.getLegalPerson());
				auditStore.put("refuseReason", storeAuditList.get(0).getRefuseReason());
				// int type = storeBusiness.getStoreType();
				// switch (type) {
				// case 1:
				// auditStore.put("storeType", "实体店");
				// break;
				// case 2:
				// auditStore.put("storeType", "网店");
				// break;
				// case 3:
				// auditStore.put("storeType", "微商");
				// break;
				// }
				auditStore.put("registerTime", storeBusiness.getCreateTime() + "");
				auditStore.put("submitTime", storeAuditList.get(0).getCreateTime() + "");

                String qualificationProofImages = getTureQualificationProofImages(storeBusiness.getId(),
                        storeBusiness.getQualificationProofImages());
                if (! StringUtils.isEmpty(qualificationProofImages)) {
                    String[] images = qualificationProofImages.split(",");
                    auditStore.put("qualificationProofImages", images);
                } else {
                    auditStore.put("qualificationProofImages", new String[]{});
                }

                String address = "";
                String province = storeBusiness.getProvince();
                if (province != null) {
                    address += province;
                }
                String city = storeBusiness.getCity();
                if (city != null) {
                    address += city;
                }
                String county = storeBusiness.getCounty();
                if (county != null) {
                    address += county;
                }
                String businessAddress = storeBusiness.getBusinessAddress();
                if (businessAddress != null) {
                    address += businessAddress;
                }

                auditStore.put("address", address);
                auditStore.put("bindWeixinName", storeBusiness.getBindWeixinName());
                auditStore.put("vip", storeBusiness.getVip() + "");
                auditStore.put("IdCardNumber", storeBusiness.getLegalIdNumber() + "");
                auditStore.put("updateTime", storeAuditList.get(0).getUpdateTime() + "");
                auditStore.put("referenceNumber", storeBusiness.getGroundUserPhone());

                auditStore.put("storeStyle", parseStyle(storeBusiness.getStoreStyle()));// 女装风格
                auditStore.put("storeAge", parseStoreAge(storeBusiness.getStoreAge()));// 年龄范围
                auditStore.put("storeAreaScope", parseStoreAreaScope(storeBusiness.getStoreAreaScope()));// 店铺面积

                auditStoreList.add(auditStore);
            }

            data.put("auditStoreList", auditStoreList);

            Map<String, String> page = new HashMap<String, String>();
            page.put("current", pageQuery.getPage() + "");
            page.put("size", pageQuery.getPageSize() + "");
            data.put("page", page);
            data.put("count", count + "");

            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError("获取新门店审核列表失败" + e.getMessage());
        }
    }

    /**
     * 时间格式转换成毫秒值
     *
     * @param time
     * @return
     */
    private long getLongTime(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (Exception e) {
            throw new RuntimeException("时间格式转换成毫秒值出错");
        }
    }

    /**
     * 获取拒绝原因列表
     */

    public JsonResponse getRefuseReasonList() {
        JsonResponse jsonResponse = new JsonResponse();
        List<RefuseReason> data = refuseReasonMapper.getList();
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 删除拒绝原因
     */

    public JsonResponse delRefuseReasonById(long id) {
        JsonResponse jsonResponse = new JsonResponse();
        int i = refuseReasonMapper.deleteById(id);
        if (i == - 1) {
            return jsonResponse.setError("删除失败！");
        }
        return jsonResponse.setSuccessful();
    }

    /**
     * 添加拒绝原因
     */

    public JsonResponse addRefuseReason(String refuseReason) {
        JsonResponse jsonResponse = new JsonResponse();
        if (StringUtils.isEmpty(refuseReason)) {
            return jsonResponse.setError("拒绝原因不能为空");
        }
        int i = refuseReasonMapper.insertRefuseReason(refuseReason);
        if (i == - 1) {
            return jsonResponse.setError("添加拒绝原因失败！");
        }
        return jsonResponse.setSuccessful();
    }

    private static final Logger logger = Logger.getLogger(StoreAuditMapper.class);



    /**
     * 新门店审核通过/拒绝/返回
     * 3.7.2版本以前使用
     * @param id           审核Id
     * @param status       状态:’0：未审核 1：通过 -1:拒绝 -2禁用’
     * @param userinfo
     * @param refuseReason
     * @return
     *
     */
    public JsonResponse updateAuditStatus(Long id, Integer status, AdminUser userinfo, String refuseReason) {
        JsonResponse jsonResponse = new JsonResponse();
        /* 更改审核表状态 status, updateTime, auditPerson, auditTime, auditId, refuseReason, */
        Integer record = storeAuditMapper.updateAuditStatus(id, status, userinfo, refuseReason);
        if (record != 1) {
            logger.info("新门店审核通过/拒绝/返回失败，record：" + record);
            return jsonResponse.setError("新门店审核通过/拒绝/返回失败");
        }
        StoreAudit storeAudit = storeAuditMapper.selectAuditById(id);
        if (storeAudit == null) {
            return jsonResponse.setError("新门店审核通过失败");
        }
        long auditTime = - 1;
        if (status == 1) {
            auditTime = System.currentTimeMillis();
        }
        StoreBusiness storeBusiness = storeBusinessDao.searchBusinessById(storeAudit.getStoreId());
        /* 更新用户表认证状态 auditTime, status */
        storeBusinessDao.updateStoreBusinessAuditStatusAndTime(storeAudit.getStoreId(), status, auditTime);
        if (status == 1) {
            //认证通过 ActiveTime = current
            int count = storeBusinessDao.updateStoreActiveTime(storeAudit.getStoreId(), System.currentTimeMillis());
            if (count != 1) {
                return jsonResponse.setError("新门店审核通过设置门店激活时间失败");
            }
            if (storeBusiness == null) {
                return jsonResponse.setError("该门店信息为空");
            } else {
                long storeId = storeBusiness.getId();
                sendText(storeBusiness.getPhoneNumber(), storeBusiness.getBusinessName(), 3942352);
                // 审核通过后第一次
                int firstLoginStatus = storeBusiness.getFirstLoginStatus();
                if (firstLoginStatus != NO_FIRST_LOGIN) {
                    //firstLogin=0时,更新为firstLogin
                    storeBusinessDao.updateFirstLoginStatus(FIRST_LOGIN, storeId);
                }
                if (storeBusiness.getAuditTime() == 0) {
                    String userCID = storeBusinessService.getStoreBusinessUserCIDByStoreId(storeBusiness.getId());
                    // 给新注册并且第一次通过审核的用户发送优惠券和系统通知和推送
//                    int storeCouponCount = sendNewStoreBusinessStoreCouponAndNotificationAndGetui(storeBusiness);
//                    if (storeCouponCount > 0) {
//                        sendNotificationAndGetui(userCID, "欢迎您来到俞姐姐门店宝", "恭喜您获得 " + storeCouponCount + " 张优惠券！", "", "",
//                                "5", System.currentTimeMillis() + "", "," + storeBusiness.getId());
//                    }
                    // 注册时间是根据审核通过的时间
                    // 获取用户时间阶段规则
                    UserTimeRule userTimeRule = groundConditionRuleService.getUserTimeRule();
                    storeBusiness.setOneStageTime(DateUtil.addDay(userTimeRule.getStage1()));
                    storeBusiness.setTwoStageTime(DateUtil.addDay(userTimeRule.getStage2()));
                    storeBusiness.setThreeStageTime(DateUtil.addDay(userTimeRule.getStage3()));
                    // 更新门店信息的用户时间规则
                    int i = storeBusinessDao.updateStoreBusiness(storeBusiness);
                    if (i != 1) {
                        logger.info("添加门店用户阶段时间失败！");
                        return jsonResponse.setError("添加门店用户阶段时间失败！");
                    }
                    // 插入门店通过审核时对地推人员客户阶段变化7条记录
                    int insertCount = groundCustomerStageChangeService.insertGroundCustomerStageChange(storeId,
                            storeBusiness.getGroundUserId(), storeBusiness.getSuperIds(),
                            storeBusiness.getOneStageTime(), storeBusiness.getTwoStageTime(),
                            storeBusiness.getThreeStageTime(), userTimeRule);
                    if (insertCount != 7) {
                        logger.info("插入地推人员客户阶段变化7条记录失败！");
                        return jsonResponse.setError("插入地推人员客户阶段变化7条记录失败！");
                    }
                    // 获取地推人员信息
                    GroundUser groundUser = groundUserMapper.selectById(storeBusiness.getGroundUserId());
                    String superIds = "";
                    if (groundUser != null) {
                        superIds = groundUser.getSuperIds();
                    }
                    List<Long> list = IdsToStringUtil.getIdsToListNoKommaL(superIds);
                    list.add(storeBusiness.getGroundUserId());
                    // 更改发放注册奖金时间,个人以及团队
                    Wrapper<GroundBonusGrant> wrapper = new EntityWrapper<GroundBonusGrant>();
                    wrapper.in("ground_user_id", list).eq("store_id", storeId).eq("bonus_type",
                            GroundConstant.BONUS_TYPE_REGISTER);
                    List<GroundBonusGrant> groundBonusGrantList = groundBonusGrantMapper.selectList(wrapper);
                    // 获取注册X天后发放奖金天数
                    int afterDay = groundConditionRuleService.getUserRegAfterDayGrantBonus();
                    logger.info("获取注册X天后发放奖金天数，afterDay：" + afterDay);
                    for (GroundBonusGrant groundBonusGrant : groundBonusGrantList) {
                        GroundBonusGrant newGroundBonusGrant = new GroundBonusGrant();
                        newGroundBonusGrant.setId(groundBonusGrant.getId());
                        long intoTime = System.currentTimeMillis() + afterDay * ONE_DAY;// THREE_DAYS
                        long allowGetOutTime = System.currentTimeMillis() + afterDay * ONE_DAY;// THREE_DAYS;
                        newGroundBonusGrant.setIntoTime(intoTime);
                        newGroundBonusGrant.setIntoDate(DateUtil.getDateInt(intoTime));
                        newGroundBonusGrant.setAllowGetOutTime(allowGetOutTime);
                        newGroundBonusGrant.setAllowGetOutDate(DateUtil.getDateInt(allowGetOutTime));
                        groundBonusGrantMapper.updateById(newGroundBonusGrant);
                    }

                }

            }
        } else if (status == - 1 || status == - 2) {
            int count = storeBusinessDao.updateStoreActiveTime(storeAudit.getStoreId(), 0);
            if (count != 1) {
                return jsonResponse.setError("新门店审核拒绝/禁用设置门店激活时间为0失败");
            }
            if (status == - 1) {
                if (storeBusiness == null) {
                    return jsonResponse.setError("该门店信息为空");
                } else {
                    sendText(storeBusiness.getPhoneNumber(), storeBusiness.getBusinessName(), 3051621);
                }
            }
        }
        return jsonResponse.setSuccessful();
    }

    /**
     * 店审核通过/拒绝/返回 版本372
     * @param: id  审核Id
     * @param: status  状态:’0：未审核 1：通过 -1:拒绝 -2禁用’
     * @param: userInfo
     * @param: refuseReason
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/21 12:31
     */
    @Transactional( rollbackFor = Exception.class )
    public JsonResponse updateAuditStatusV372(Long id, Integer status, AdminUser userInfo, String refuseReason) {
        JsonResponse jsonResponse = new JsonResponse();

        // 更改审核表状态
        if (storeAuditMapper.updateAuditStatus(id, status, userInfo, refuseReason) != 1) {
            logger.error("新门店审核通过/拒绝/返回失败");
            return jsonResponse.setError("新门店审核通过/拒绝/返回失败");
        }

        /* 更新门店表审核状态 */
        StoreAudit storeAudit = storeAuditMapper.selectAuditById(id);
        if (storeAudit == null) {
            return jsonResponse.setError("新门店审核通过失败");
        }

        StoreBusiness storeBusiness = storeBusinessDao.searchBusinessById(storeAudit.getStoreId());
        if (storeBusiness == null) {
            return jsonResponse.setError("该门店信息为空");
        }

        long current = System.currentTimeMillis();
        long auditTime = status == 1 ? current : - 1;
        //更新门店表 AuditStatus ,AuditStatusTime, DataAuditStatus ,DataAuditStatusTime
        storeBusinessDao.updateStoreAuditStatusAndTimeV372(storeAudit.getStoreId(), status, auditTime);
        //通过
        if (status == 1) {
            //更新 ActiveTime
            int count = storeBusinessDao.updateStoreActiveTime(storeAudit.getStoreId(), current);
            if (count != 1) {
                return jsonResponse.setError("新门店审核通过设置门店激活时间失败");
            }
            long storeId = storeBusiness.getId();
            storeBusiness =storeBusinessDao.searchBusinessById(storeId);

            //发送短信
            sendMessage(storeBusiness.getPhoneNumber(), storeBusiness.getBusinessName(), 3942352);

            if (storeBusiness.getDataAuditTime() == 0) {
                // 注册时间是根据审核通过的时间
                // 获取用户时间阶段规则
                UserTimeRule userTimeRule = groundConditionRuleService.getUserTimeRule();
                storeBusiness.setOneStageTime(DateUtil.addDay(userTimeRule.getStage1()));
                storeBusiness.setTwoStageTime(DateUtil.addDay(userTimeRule.getStage2()));
                storeBusiness.setThreeStageTime(DateUtil.addDay(userTimeRule.getStage3()));
                // 更新门店信息的用户时间规则
                int i = storeBusinessDao.updateStoreBusiness(storeBusiness);
                if (i != 1) {
                    logger.info("添加门店用户阶段时间失败！");
                    return jsonResponse.setError("添加门店用户阶段时间失败！");
                }
                // 插入门店通过审核时对地推人员客户阶段变化7条记录
                int insertCount = groundCustomerStageChangeService.insertGroundCustomerStageChange(storeId,
                        storeBusiness.getGroundUserId(), storeBusiness.getSuperIds(),
                        storeBusiness.getOneStageTime(), storeBusiness.getTwoStageTime(),
                        storeBusiness.getThreeStageTime(), userTimeRule);
                if (insertCount != 7) {
                    logger.info("插入地推人员客户阶段变化7条记录失败！");
                    return jsonResponse.setError("插入地推人员客户阶段变化7条记录失败！");
                }
                // 获取地推人员信息
                GroundUser groundUser = groundUserMapper.selectById(storeBusiness.getGroundUserId());
                String superIds = "";
                if (groundUser != null) {
                    superIds = groundUser.getSuperIds();
                }
                List<Long> list = IdsToStringUtil.getIdsToListNoKommaL(superIds);
                list.add(storeBusiness.getGroundUserId());
                // 更改发放注册奖金时间,个人以及团队
                Wrapper<GroundBonusGrant> wrapper = new EntityWrapper<GroundBonusGrant>();
                wrapper.in("ground_user_id", list).eq("store_id", storeId).eq("bonus_type",
                        GroundConstant.BONUS_TYPE_REGISTER);
                List<GroundBonusGrant> groundBonusGrantList = groundBonusGrantMapper.selectList(wrapper);
                // 获取注册X天后发放奖金天数
                int afterDay = groundConditionRuleService.getUserRegAfterDayGrantBonus();
                logger.info("获取注册X天后发放奖金天数，afterDay：" + afterDay);
                for (GroundBonusGrant groundBonusGrant : groundBonusGrantList) {
                    GroundBonusGrant newGroundBonusGrant = new GroundBonusGrant();
                    newGroundBonusGrant.setId(groundBonusGrant.getId());
                    long intoTime = System.currentTimeMillis() + afterDay * ONE_DAY;// THREE_DAYS
                    long allowGetOutTime = System.currentTimeMillis() + afterDay * ONE_DAY;// THREE_DAYS;
                    newGroundBonusGrant.setIntoTime(intoTime);
                    newGroundBonusGrant.setIntoDate(DateUtil.getDateInt(intoTime));
                    newGroundBonusGrant.setAllowGetOutTime(allowGetOutTime);
                    newGroundBonusGrant.setAllowGetOutDate(DateUtil.getDateInt(allowGetOutTime));
                    groundBonusGrantMapper.updateById(newGroundBonusGrant);
                }

            }
        } else if (status == - 1 || status == - 2) {
            //更新 ActiveTime
            int count = storeBusinessDao.updateStoreActiveTime(storeAudit.getStoreId(), 0);
            if (count != 1) {
                return jsonResponse.setError("新门店审核拒绝/禁用设置门店激活时间为0失败");
            }
            if (status == - 1) {
                sendMessage(storeBusiness.getPhoneNumber(), storeBusiness.getBusinessName(), 3051621);
            }
        } else {
            throw new RuntimeException("审核状态不存在");
        }
        return jsonResponse.setSuccessful();
    }

    /**
     * 给新注册并且第一次通过审核的用户发送优惠券
     */
    private int sendNewStoreBusinessStoreCouponAndNotificationAndGetui(StoreBusiness storeBusiness) {
        int count = 0;
        String setting = globalSettingService.getSetting(GlobalSettingName.STORE_COUPON_SEND_1_SETTING);
        logger.info("给新注册并且第一次通过审核的用户发送优惠券，setting：" + setting);
        if (! StringUtils.isEmpty(setting)) {
            JSONObject jsonObject = JSONObject.parseObject(setting);
            logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject：" + jsonObject);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("setting");
                // JSONArray jsonArray = JSONArray.parseArray(object);
                logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonArray：" + jsonArray);
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (Object object2 : jsonArray) {
                        JSONObject jsonObject2 = JSONObject.parseObject((String) object2.toString());
                        logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject2：" + jsonObject2);
                        if (jsonObject2 != null) {
                            int couponCount = Integer.parseInt(jsonObject2.get("coupon_count").toString());
                            String jsonObject2String = jsonObject2.get("coupon_template_id").toString();
                            logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject2String：" + jsonObject2String);
                            if (! StringUtils.isEmpty(jsonObject2String)) {
                                long storeCouponTemplateId = Long.parseLong(jsonObject2String);
                                // 给新注册并且第一次通过审核的用户发送优惠券
                                boolean flag = storeCouponService.batchStoreCouponToNewStoreAudit(couponCount,
                                        storeCouponTemplateId, storeBusiness);
                                if (flag) {
                                    count = count + couponCount;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    // 发送系统通知和推送
    private void sendNotificationAndGetui(String cid, String title, String abstracts, String linkUrl, String image,
                                          String type, String pushTime, String storeIdArrays) {
        try {

            // UserCID是用户第一次登陆获取的，所以暂时获取不到，无法推送
            // boolean ret = GetuiUtil.pushGeTui(cid, title, abstracts, linkUrl,
            // image, type, pushTime);
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setAbstracts(abstracts);
            notification.setPushStatus(1);
            notification.setImage(image);
            notification.setType(type);
            notification.setStatus(0);
            notification.setLinkUrl(linkUrl);
            notification.setStoreIdArrays(storeIdArrays);
            notifacationService.addNotification(notification);
        } catch (Exception e) {
            logger.error("发送新注册用户优惠券通知和推送时发生异常:" + e.getMessage());
        }
    }


    /**
     * 发送短信
     *
     * @param: phone 短信接收人手机号
     * @param: username 用户姓名
     * @param: templateNum ??
     * @return: void
     * @auther: Charlie(唐静)
     * @date: 2018/5/21 9:46
     */
    private void sendMessage(String phone, String username, int templateNum) {
        if (org.apache.commons.lang3.StringUtils.isBlank(phone)) {
            throw new RuntimeException("门店手机号码为空");
        }
        JSONArray param = new JSONArray();
        if (username.length() > 6) {
            username = new StringBuffer().append(username, 0, 3).append("...").toString();
        }
        param.add(username);
        yunXinSmsService.sendNotice(username, param, templateNum);
    }


    private void sendText(String phoneNumber, String businessName, int templateNumber) {
        if (phoneNumber == null || "".equals(phoneNumber)) {
            throw new RuntimeException("该门店号码为空");
        } else {
            logger.info("发送短信！");
            JSONArray param = new JSONArray();
            if (businessName.length() > 6) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(businessName.substring(0, 3));
                stringBuffer.append("...");
                businessName = stringBuffer.toString();
            }
            param.add(businessName);
            yunXinSmsService.sendNotice(phoneNumber, param, templateNumber);
        }
    }

    // 临时方法:为了纠正app2.3版本门店qualificationProofImages字段默认值产生的bug
    public String getTureQualificationProofImages(long storeId, String qualificationProofImages) {
        String[] images = qualificationProofImages.split(",");
        String newImages = "";
        for (String string : images) {
            String[] split = string.split("http");
            for (String img : split) {
                if (StringUtils.isNotEmpty(img)) {
                    newImages += "http" + img + ",";
                }
            }
        }
        if (StringUtils.isNotEmpty(newImages)) {
            newImages = newImages.substring(0, newImages.length() - 1);
        }
        storeBusinessDao.updateBusinessQualificationProofImages(storeId, newImages);
        return newImages;
    }

    /**
     * 解析店铺面积范围为文本
     *
     * @param storeAreaScope
     * @return
     */
    private String parseStoreAreaScope(Integer storeAreaScope) {
        String storeArea = "";
        if (storeAreaScope == null) {
            return storeArea;
        }
        switch (storeAreaScope) {
            case 1:
                storeArea = "<=30";
                break;
            case 2:
                storeArea = "30-40";
                break;
            case 3:
                storeArea = "40-50";
                break;
            case 4:
                storeArea = "50-60";
                break;
            case 5:
                storeArea = "60以上";
                break;
            default:
                break;
        }
        return storeArea;
    }

    /**
     * 解析年龄范围返回文本
     *
     * @param storeAge
     * @return
     */
    private String parseStoreAge(String storeAge) {
        if (storeAge == null) {
            return null;
        }
        String[] split = storeAge.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : split) {
            if (string == "") {
                return null;
            }
            switch (Integer.valueOf(string)) {
                case 1:
                    stringBuffer.append("18-25 ");
                    break;
                case 2:
                    stringBuffer.append("25-35 ");
                    break;
                case 3:
                    stringBuffer.append("35-45 ");
                    break;
                case 4:
                    stringBuffer.append("45以上 ");
                    break;
                default:
                    break;
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 返回汉字女装风格
     *
     * @param storeStyle
     * @return
     */
    private String parseStyle(String storeStyle) {
        if (storeStyle == null) {
            return null;
        }
        String[] split = storeStyle.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : split) {
            if (string == "") {
                return null;
            }
            switch (Integer.valueOf(string)) {
                case 1:
                    stringBuffer.append("欧美 ");
                    break;
                case 2:
                    stringBuffer.append("日韩 ");
                    break;
                case 3:
                    stringBuffer.append("OL ");
                    break;
                case 4:
                    stringBuffer.append("淑女 ");
                    break;
                case 5:
                    stringBuffer.append("运动 ");
                    break;
                case 6:
                    stringBuffer.append("休闲 ");
                    break;
                case 7:
                    stringBuffer.append("街头 ");
                    break;
                case 8:
                    stringBuffer.append("民族 ");
                    break;
                case 9:
                    stringBuffer.append("简约 ");
                    break;
                case 10:
                    stringBuffer.append("学院 ");
                    break;
                case 11:
                    stringBuffer.append("森系 ");
                    break;
                case 12:
                    stringBuffer.append("百搭 ");
                    break;
                case 13:
                    stringBuffer.append("中性 ");
                    break;
                case 14:
                    stringBuffer.append("其他 ");
                    break;
                default:
                    break;
            }
        }
        return stringBuffer.toString();
    }




    /**
     * 根据不同的版本, 更新用户审核状态
     * @param: auditId
     * @param: status
     * @param: userinfo
     * @param: refuseReason
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 14:47
     */
    @Transactional( rollbackFor = Exception.class )
    public JsonResponse updateAuditStatusDispatcher(Long auditId, Integer status, AdminUser userInfo, String refuseReason) {
        String appVersion = storeBusinessDao.getAppIdByAuditId(auditId);

        /* 根据APP版本调用不同的接口 */
        boolean hasAppVersion = org.apache.commons.lang3.StringUtils.isNotBlank(appVersion);
        if (hasAppVersion && Integer.parseInt(appVersion) >= Integer.parseInt(StoreLoginDelegator.APP_VERSION_372)) {
            //372以及372以后的版本
            return updateAuditStatusV372(auditId, status, userInfo, refuseReason);
        }
        //372以前的版本
        return updateAuditStatus(auditId, status, userInfo, refuseReason);
    }

}