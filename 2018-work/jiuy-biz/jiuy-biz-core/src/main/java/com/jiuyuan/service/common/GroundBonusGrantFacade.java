package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.newentity.ground.GroundConstant;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.DoubleUtil;
import com.jiuyuan.util.IdsToStringUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
/**
 * 奖金发放
 * @author Administrator
 *
 */
@Service
public class GroundBonusGrantFacade {
	private static final Log logger = LogFactory.get(GroundBonusGrantFacade.class);
	
	@Autowired
	private IGroundBonusRuleService groundBonusRuleService;
	
	@Autowired
	private GroundBonusGrantMapper groundBonusGrantMapper;
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	//该mapper使用的也是store_order表
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	
//	private static final long THREE_DAYS = 3L*24*60*60*1000;
	
	//修改个人门店订单交易奖金和团队订单交易奖金时间
		public int updateDealBonusAllowGetOutTime(long orderNo, long storeId) {
			List<String> bonusTypeList = new ArrayList<String>();
			bonusTypeList.add("3");
			bonusTypeList.add("4");
			bonusTypeList.add("5");
			
			Wrapper<GroundBonusGrant> groundBonusGrantWrapper = new EntityWrapper<GroundBonusGrant>().in("bonus_type", bonusTypeList).eq("related_id", orderNo)
					.eq("store_id", storeId);
			GroundBonusGrant groundBonusGrant = new GroundBonusGrant();
			groundBonusGrant.setAllowGetOutTime(System.currentTimeMillis());
			groundBonusGrant.setAllowGetOutDate(DateUtil.getDateInt(System.currentTimeMillis()));
			return groundBonusGrantMapper.update(groundBonusGrant, groundBonusGrantWrapper);
		}
	
	
	
	
	/**
	 * 预发放奖金，
	 * 说明：
	 * 1、个人和团队都发放
	 * 2、强调 发放阶段门店交易奖金，该方法在支付订单完成时调用
	 * 3、强调 发放激活奖金，该方法在确认收货完成时调用
	 * 4、强调 发放注册奖金，该方法在门店第一次审核通过时调用
	 * @param groundUser 地推人员信息 可以为null 
	 * @param bonusType 奖金类型：3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金） 可以调用bean中的常量
	 * @param storeId 门店商家ID
	 * @param relatedId 相关id，门店注册奖金时为门店Id、其他类型时为订单号
	 * @param remark 备注
	 */
	//@Transactional(rollbackFor = Exception.class)
	public void grantOrderBonus(long groundUserId, int bonusType, long storeId, long orderNo ){
		//阶段门店交易奖金
		if(GroundConstant.BONUS_TYPE_FIRST_STAGE == bonusType || 
				GroundConstant.BONUS_TYPE_SECOND_STAGE == bonusType ||
						GroundConstant.BONUS_TYPE_THIRD_STAGE == bonusType){
			stageBonusGrant(groundUserId, bonusType, storeId, "交易奖金", orderNo);
		}else{
			logger.info("发放阶段奖金时，奖金类型错误，请尽快排查问题，bonusType："+bonusType);
		}
	}


	
	
	
	/**
	 * 阶段门店交易奖金
	 * 强调 调用该方法在支付订单时已付款
	 * @param groundUser 地推人员信息 可以为null 
	 * @param bonusType 奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金） 可以调用bean中的常量
	 * @param storeId 门店商家ID
	 * @param relatedId 相关id，门店注册奖金时为门店Id、其他类型时为订单ID
	 * @param remark 备注
	 */
    private int stageBonusGrant(long groundUserId, int bonusType, long storeId, String remark, long relatedId) {
		
		GroundUser groundUser = groundUserMapper.selectById(groundUserId);
		//个人阶段
		//获取个人奖金规则
		Double bonusComission = groundBonusRuleService.getGroundBonusRule(groundUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_ONESELF);
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(relatedId);
		//获取相关订单金额（实付金额，不计邮费）
		double totalPay = storeOrderNew.getTotalPay();
		//获取个人奖金金额
		double bonus = DoubleUtil.mul(bonusComission, totalPay);
		//计算入账时间和允许提现时间
		long intoTime = System.currentTimeMillis();
		long allowGetOutTime = Long.valueOf(0);
		
		int i = bonusGrant(groundUser, groundUser.getId(), GroundConstant.BONUS_TYPE_ONESELF, bonusType, storeId, remark, relatedId, bonus, intoTime, allowGetOutTime, totalPay,0,bonusComission);
		if(i == -1){
			logger.error("地推人员发放奖金失败,ID:"+groundUser.getId()+",发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+",storeId:"+storeId+",relatedId:"+relatedId+",bonus:"+bonus);
			throw new RuntimeException("地推人员发放奖金失败，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}else{
			logger.info("地推人员发放奖金成功，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}
	    //团队奖金
		String superIds = groundUser.getSuperIds();
		List<String> list = IdsToStringUtil.getIdsToListNoKomma(superIds);
		int count = 0;
		List<Long> longList = IdsToStringUtil.getIdsToListNoKommaL(superIds);
		
		for(String id :list){
			GroundUser superUser = groundUserMapper.selectById(Long.parseLong(id));
			
			//被贡献人该奖金相关直接下级ID(0:奖金来源为个人)
			long directGroundUserId = 0;
			Wrapper<GroundUser> groundUserWrapper = new EntityWrapper<GroundUser>().in("id", longList);
			List<GroundUser> directGroundUserList = groundUserMapper.selectList(groundUserWrapper);
			//获取被贡献人该奖金相关直接下级ID
			boolean flag = true;
			for (GroundUser directGroundUser : directGroundUserList) {
				if(flag){
					if(superUser.getId()==directGroundUser.getPid()){// || (directGroundUser.getSuperIds().indexOf(superUser.getId()+""))!=-1){
						directGroundUserId = directGroundUser.getId();
						flag = false;
					}else if(groundUser.getPid()==superUser.getId() || (superUser.getUserType()==3 && groundUser.getUserType()==4)){
						directGroundUserId = groundUser.getId();
						flag = false;
					}
				}
			}
			logger.info(superUser.getId()+"获取被贡献人该奖金相关直接下级ID:"+directGroundUserId);
			
			//获取团队奖金规则
			Double superBonusComission = groundBonusRuleService.getGroundBonusRule(superUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_TEAM);
			//获取团队奖金金额
			double superBonus = DoubleUtil.mul(superBonusComission, totalPay);
			int h = bonusGrant(superUser, groundUser.getId(), GroundConstant.BONUS_TYPE_TEAM, bonusType, storeId, remark, relatedId, superBonus, intoTime, allowGetOutTime, storeOrderNew.getTotalPay(),directGroundUserId,superBonusComission);
			if(h == -1){
				logger.error("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
				throw new RuntimeException("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}else{
				logger.info("地推人员发放奖金成功，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}
			count++;
		}
		return i+count;
	}
    /**
	 * 预发放激活奖金，
	 * 说明：
	 * 1、个人和团队都发放
	 * 3、强调 发放激活奖金，该方法在确认收货完成时调用
	 * @param groundUser 地推人员信息 可以为null 
	 * @param storeId 门店商家ID
	 * @param relatedId 相关id，门店注册奖金时为门店Id、其他类型时为订单号
	 * @param remark 备注
	 */
    public int grantActivateBonus(long groundUserId, long storeId, long orderNo) {
    	int bonusType = GroundConstant.BONUS_TYPE_ACTIVE;
    	String remark = "激活奖金";
    	long relatedId = orderNo;
		GroundUser groundUser = groundUserMapper.selectById(groundUserId);
		//个人激活
		//获取个人奖金规则
		Double bonus = groundBonusRuleService.getGroundBonusRule(groundUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_ONESELF);
		//计算入账时间和允许提现时间
		long intoTime = System.currentTimeMillis();
		long allowGetOutTime = System.currentTimeMillis();
		
		//获取相关订单金额（实付金额，不计邮费）
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(relatedId);
		int i = bonusGrant(groundUser, groundUser.getId(), GroundConstant.BONUS_TYPE_ONESELF, bonusType, storeId, remark, relatedId, bonus, intoTime, allowGetOutTime, storeOrderNew.getTotalPay(),0,bonus);
		if(i == -1){
			logger.error("地推人员发放奖金失败，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
			throw new RuntimeException("地推人员发放奖金失败，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}else{
			logger.info("地推人员发放奖金成功，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}
	    //团队激活奖金
		String superIds = groundUser.getSuperIds();
		List<String> list = IdsToStringUtil.getIdsToListNoKomma(superIds);
		List<Long> longList = IdsToStringUtil.getIdsToListNoKommaL(superIds);
		int count = 0;
		for(String id :list){
			GroundUser superUser = groundUserMapper.selectById(Long.parseLong(id));
			
			//被贡献人该奖金相关直接下级ID(0:奖金来源为个人)
			long directGroundUserId = 0;
			Wrapper<GroundUser> groundUserWrapper = new EntityWrapper<GroundUser>().in("id", longList);
			List<GroundUser> directGroundUserList = groundUserMapper.selectList(groundUserWrapper);
			//获取被贡献人该奖金相关直接下级ID
			boolean flag = true;
			for (GroundUser directGroundUser : directGroundUserList) {
				if(flag){
					if(superUser.getId()==directGroundUser.getPid()){// || (directGroundUser.getSuperIds().indexOf(superUser.getId()+""))!=-1){
						directGroundUserId = directGroundUser.getId();
						flag = false;
					}else if(groundUser.getPid()==superUser.getId() || (superUser.getUserType()==3 && groundUser.getUserType()==4)){
						directGroundUserId = groundUser.getId();
						flag = false;
					}
				}
			}
			logger.info(superUser.getId()+"获取被贡献人该奖金相关直接下级ID:"+directGroundUserId);
			
			//获取团队激活奖金规则
			Double superBonus = groundBonusRuleService.getGroundBonusRule(superUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_TEAM);
			int h = bonusGrant(superUser, groundUser.getId(), GroundConstant.BONUS_TYPE_TEAM, bonusType, storeId, remark, relatedId, superBonus, intoTime, allowGetOutTime, storeOrderNew.getTotalPay(),directGroundUserId,superBonus);
			if(h == -1){
				logger.error("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
				throw new RuntimeException("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}else{
				logger.info("地推人员发放奖金成功，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}
			count++;
		}
		
		return i+count;
	}

    /**
	 * 预发放注册奖金，
	 * 说明：
	 * 1、个人和团队都发放
	 * 2、强调 发放注册奖金，该方法在门店第一次审核通过时调用
	 * @param groundUser 地推人员信息 可以为null 
	 * @param storeId 门店商家ID
	 * @param relatedId 相关id，门店注册奖金时为门店Id、其他类型时为订单号
	 * @param remark 备注
	 */
	
	//@Transactional(rollbackFor = Exception.class)
	public int grantRegBonus(long groundUserId , long storeId) {
		int bonusType = GroundConstant.BONUS_TYPE_REGISTER;
	    String remark = "注册奖金";
	    long relatedId = storeId;
		
		GroundUser groundUser = groundUserMapper.selectById(groundUserId);
		
		//个人注册奖金
		//获取个人奖金规则
		Double bonus = groundBonusRuleService.getGroundBonusRule(groundUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_ONESELF);
		long intoTime = 0L;
		long allowGetOutTime = 0L;
		
		int i = bonusGrant(groundUser, groundUser.getId(), GroundConstant.BONUS_TYPE_ONESELF, bonusType, storeId, remark, relatedId, bonus, intoTime, allowGetOutTime, null,0,bonus);
		if(i == -1){
			logger.error("地推人员发放奖金失败，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
			throw new RuntimeException("地推人员发放奖金失败，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}else{
			logger.info("地推人员发放奖金成功！，ID:"+groundUser.getId()+"发放sourceType："+GroundConstant.BONUS_TYPE_ONESELF+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+bonus);
		}
	    //团队注册奖金
		String superIds = groundUser.getSuperIds();
		List<String> list = IdsToStringUtil.getIdsToListNoKomma(superIds);
		List<Long> longList = IdsToStringUtil.getIdsToListNoKommaL(superIds);
		int count = 0;
		for(String id :list){
			GroundUser superUser = groundUserMapper.selectById(Long.parseLong(id));
			
			//被贡献人该奖金相关直接下级ID(0:奖金来源为个人)
			long directGroundUserId = 0;
			Wrapper<GroundUser> groundUserWrapper = new EntityWrapper<GroundUser>().in("id", longList);
			List<GroundUser> directGroundUserList = groundUserMapper.selectList(groundUserWrapper);
			//获取被贡献人该奖金相关直接下级ID
			boolean flag = true;
			for (GroundUser directGroundUser : directGroundUserList) {
				if(flag){
					if(superUser.getId()==directGroundUser.getPid()){// || (directGroundUser.getSuperIds().indexOf(superUser.getId()+""))!=-1){
						directGroundUserId = directGroundUser.getId();
						flag = false;
					}else if(groundUser.getPid()==superUser.getId() || (superUser.getUserType()==3 && groundUser.getUserType()==4)){
						directGroundUserId = groundUser.getId();
						flag = false;
					}
				}
			}
			logger.info(superUser.getId()+"获取被贡献人该奖金相关直接下级ID:"+directGroundUserId);
			
			Double superBonus = groundBonusRuleService.getGroundBonusRule(superUser.getUserType(), bonusType, GroundConstant.BONUS_TYPE_TEAM);
			int h = bonusGrant(superUser, groundUser.getId(), GroundConstant.BONUS_TYPE_TEAM, bonusType, storeId, remark, relatedId, superBonus, intoTime, allowGetOutTime, null,directGroundUserId,superBonus);
			if(h == -1){
				logger.error("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
				throw new RuntimeException("地推人员发放奖金失败，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}else{
				logger.info("地推人员发放奖金成功，ID:"+id+"发放sourceType："+GroundConstant.BONUS_TYPE_TEAM+",bonusType:"+bonusType+"storeId:"+storeId+"relatedId:"+relatedId+",bonus:"+superBonus);
			}
			count++;
		}
		return i+count;
	}

	/**
     * 添加发放奖金表记录
     * @param groundUser
     * @param sourceType
     * @param bonusType
     * @param storeId
     * @param remark
     * @param relatedId
     * @param bonus
     * @param intoTime
     * @param allowGetOutTime
     */
	private int bonusGrant(GroundUser groundUser, long SourceGroundUserId, int sourceType, int bonusType, long storeId, String remark,
			long relatedId , double bonus, long intoTime, long allowGetOutTime ,Double orderPrice,long directGroundUserId,double bonusRule) {
		GroundBonusGrant groundBonusGrant = new GroundBonusGrant();
		groundBonusGrant.setBonusRule(bonusRule);
		groundBonusGrant.setSuperIds(groundUser.getSuperIds());
		groundBonusGrant.setGroundUserId(groundUser.getId());
		groundBonusGrant.setSourceGroundUserId(SourceGroundUserId);
		groundBonusGrant.setSourceType(sourceType);
		groundBonusGrant.setBonusType(bonusType);
		groundBonusGrant.setStoreId(storeId);
		groundBonusGrant.setRemark(remark);
		groundBonusGrant.setCreateTime(System.currentTimeMillis());
		groundBonusGrant.setRelatedId(relatedId);
		groundBonusGrant.setOrderPrice(orderPrice);
		groundBonusGrant.setCash(bonus);
		groundBonusGrant.setIntoTime(intoTime);
		groundBonusGrant.setIntoDate(DateUtil.getDateInt(intoTime));
		groundBonusGrant.setAllowGetOutTime(allowGetOutTime);
		groundBonusGrant.setAllowGetOutDate(DateUtil.getDateInt(allowGetOutTime));
		groundBonusGrant.setDirectGroundUserId(directGroundUserId);
		return groundBonusGrantMapper.insert(groundBonusGrant);
	}
	
	
	
//	/**
//	 * 预发放奖金，
//	 * 说明：
//	 * 1、个人和团队都发放
//	 * 2、强调 发放阶段门店交易奖金，该方法在支付订单完成时调用
//	 * 3、强调 发放激活奖金，该方法在确认收货完成时调用
//	 * 4、强调 发放注册奖金，该方法在门店第一次审核通过时调用
//	 * @param groundUser 地推人员信息 可以为null 
//	 * @param sourceType 奖金来源类型 0个人、1团体  可以调用bean中的常量
//	 * @param bonusType 奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金） 可以调用bean中的常量
//	 * @param storeId 门店商家ID
//	 * @param relatedId 相关id，门店注册奖金时为门店Id、其他类型时为订单号
//	 * @param remark 备注
//	 */
//	//@Transactional(rollbackFor = Exception.class)
//	public int grantBonus(GroundUser groundUser, int bonusType, long storeId, long relatedId, String remark){
//		if(groundUser == null || groundUser.getId() == 0){
//			logger.info("该门店ID："+storeId+"，没有地推人员，无需发放奖金！");
//			return 0;
//		}
//		//获取对应的发放奖金金额，（注册和激活是奖金数，交易是奖金百分比）
//		//判断奖金类型
//		//注册奖金类型
//		if(GroundBonusGrant.BONUS_TYPE_REGISTER == bonusType){
//			int i = registerBonusGrant(groundUser, bonusType, storeId, remark, relatedId);
//			return i;
//		}
//		//激活奖金类型
//		if(GroundBonusGrant.BONUS_TYPE_ACTIVE == bonusType){
//			int i = activeBonusGrant(groundUser, bonusType, storeId, remark, relatedId);
//			return i;
//		}
//		//阶段门店交易奖金
//		if(GroundBonusGrant.BONUS_TYPE_FIRST_STAGE == bonusType || 
//		   GroundBonusGrant.BONUS_TYPE_SECOND_STAGE == bonusType ||
//		   GroundBonusGrant.BONUS_TYPE_THIRD_STAGE == bonusType){
//			int i = stageBonusGrant(groundUser, bonusType, storeId, remark, relatedId);
//			return i;
//			
//		}
//		return -1;
//	}
}
