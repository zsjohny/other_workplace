package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.util.DateUtilJiuYuan;
//import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.MemberMapper;
import com.store.dao.mapper.MemberMemoMapper;
import com.store.entity.member.MemberMemo;
import com.store.entity.member.ShopMember;
import com.store.entity.message.Message;
import com.store.enumerate.Member48MarkEnum;
import com.store.enumerate.MessageSendTypeeEnum;
import com.store.enumerate.MessageTypeEnum;
import com.store.service.coupon.ShopCouponTemplateService;
import com.store.service.coupon.ShopMemberCouponService;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 *  extends ServiceImpl<MessageMapper); Message>
 */
@Service
public class MemberService {
	 private static final Log logger = LogFactory.get();
	    @Autowired
	    private WxaMemberFavoriteService shopMemberFavoriteService;
		 
		 @Autowired
		 private ShopCouponTemplateService shopCouponTemplateService;
		 
	@Autowired
	MessageReceiveService  messageReceiveService;
	  
	@Autowired
	private StoreUserService userService;

	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	MemberMemoMapper memberMemoMapper;
	
	@Autowired
	MessageService messageService;
//	
//	@Autowired
//	private WxaMemberFavoriteMapper shopMemberFavoriteMapper;
//	
//	@Autowired
//	private ShopProductMapper shopProductMapper;
	
	 
	 @Autowired
	 private ShopMemberCouponService shopMemberCouponService;
	 
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;

	public int updateMemberById(ShopMember member) {
		return memberMapper.updateById(member);
	}
	
	/**
	 * 获取门店全部未读消息数量
	 */
	public int getAllNoReadCount(long storeId) {
		Wrapper<ShopMember> wrapper = new EntityWrapper<ShopMember>();
		wrapper.eq("store_id", storeId);
		wrapper.ge("not_read_message_count", 1);//大于等于1
		wrapper.eq("status", 0);
		
		int allNoReadCount = 0;
		List<ShopMember> list = memberMapper.selectList(wrapper);
		for(ShopMember Member : list){
			allNoReadCount = allNoReadCount + Member.getNotReadMessageCount();
		}
		return allNoReadCount;
	}
	
	/**
	 * 小程序登陆
     * @param unionId
     * @param appId
     */
	public Map<String,Object> login(String unionId, String appId, long storeId,
									HttpServletResponse response, String from) {
		logger.info("用户登录 storeId={},unionId={},appId={},loginType={}", storeId, unionId, appId);
        Map<String, Object> retMap = addMember(unionId, null, null, appId, storeId, from, null);

        ShopMember member = (ShopMember) retMap.get("member");
		
		//2、发送消息给客服关注提醒
		String userNickname = member.getUserNickname();
		String content = "关注了您的小程序";
		if(StringUtils.isNotEmpty(userNickname)){
			content = "["+ userNickname + "]" + content;
		}
		logger.info("授权后添加消息提醒，content："+content);

		StoreBusiness storeBusiness = userService.getStoreBusinessById(storeId);
		logger.info ("storeInfo: ", storeBusiness);
		messageReceiveService.processInTextMsg(
				content,
				"-1",
				storeBusiness.getWeiXinNum(),
				unionId,
				MessageTypeEnum.text.getCode()
		);
		
        return retMap;
	}





	/**
	 * 授权后会员信息添加
     * @param unionId
     * @param nickName
     * @param headImg
     * @param sex
     */
	public Map<String,Object> addMember(String unionId, String nickName, String headImg, String appId, long storeId, String from, Integer sex) {
		logger.info("授权后会员信息添加,unionId:{},nickName{},headImg{},appId{},appId{},sex{}",unionId,nickName,headImg,appId,storeId,sex);
		long currentTime = DateUtil.current(false);
		ShopMember member = getMemberByBindWeixin(unionId,storeId);

		Map<String,Object> retMap = new HashMap<>();
		if(member != null ){
			member.setStoreId(storeId);
			member.setStatus(0);
			if(StringUtils.isNotEmpty(nickName)){
				member.setUserNickname(nickName);
			}
			if(StringUtils.isNotEmpty(headImg)){
				member.setUserIcon(headImg);
			}
			if(member.getSource()==null || "".equals(member.getSource())) {
				member.setSource( from==null || "null".equals(from) || "".equals(from) ? "0" : from);
			}
			if (sex != null) {
				member.setSex (sex);
			}
			memberMapper.updateById(member);
			retMap.put("member",member);
			return retMap;
		}else{
			member = new ShopMember();
			member.setStoreId(storeId);
			member.setBindWeixin(unionId);
			//主要手机手机号不能为空串
			member.setBindPhone(null);
			member.setUserIcon(headImg);
			member.setStatus(0);;
			member.setCreateTime(currentTime);
			member.setUpdateTime(currentTime);
			member.setSource(from==null || "null".equals(from) || "".equals(from) ? "0" : from);
			member.setSex (sex);
			memberMapper.insert(member);
			retMap.put("coupon",true);
			
		}
		member = memberMapper.selectById(member.getId());
		retMap.put("member",member);
		return retMap;
	}
	
	/**
	 * 修改会员最后发送消息内容
	 * 
	 * 新消息触发修改会员信息表中消息相关字段
	 */
	public void updateMemberLastMessage(Message message , Long memberId) {
//		Member memberOld = memberMapper.selectById(memberId);
		ShopMember member = new ShopMember();
		member.setId(memberId);
		
		if(MessageTypeEnum.getEnum(message.getMessageType()).getIntCode() == MessageTypeEnum.text.getIntCode()){
			member.setLastMessageContent(message.getTextContent());
		}else{
			member.setLastMessageContent(MessageTypeEnum.getEnum(message.getMessageType()).getName());
		}
		member.setLastMessageType(message.getMessageType());
		
		//会员发送客服的消息进行更新最后发送消息时间和未读消息加1
		if(message.getSendType() == MessageSendTypeeEnum.memberToServer.getIntValue()){
			//如果是会员发送客服消息类型则更改最后消息时间，该时间用于判断48小时之前或之后
			member.setLastMessageTime(message.getCreateTime());
//			member.setNotReadMessageCount(memberOld.getNotReadMessageCount() + 1);//未读消息加1
			ShopMember memberObj = memberMapper.selectById(memberId);
			int notReadMessageCount = memberObj.getNotReadMessageCount();
			//增加会员客服未读消息数量
			memberMapper.increaseNotReadMessageCount(memberId);
				
		}
		
		memberMapper.updateById(member);
	}

	
	
	/**
	 * 记录会员登陆日志
	 * @param member
	 */
	private void addMemberLoginLog(ShopMember member,String appId,long storeId) {
		logger.info("用户登陆成功，member："+member);
		//  TODO记录会员登陆日志待开发
	}


	/**
	 * 获取商家的总会员数量
	 * @param storeId
	 * @return
	 */
	public int getMemberTotalCount(long storeId) {
		Wrapper<ShopMember> wrapper = new EntityWrapper<ShopMember>();
		wrapper.eq("store_id", storeId);
		wrapper.eq("status", 0);
		return memberMapper.selectCount(wrapper);
	}
	
	
	/**
	 * 组装条件构造器
	 * @param member48Mark 筛选条件：0（全部）、1（48小时内）、2（48小时前）
	 * @param searchWord 搜索词  无搜索此请填null
	 * @return
	 */
	private Wrapper<ShopMember> buildListWrapper(Member48MarkEnum member48Mark,String searchWord,long storeId) {
		Wrapper<ShopMember> wrapper = new EntityWrapper<ShopMember>();
		//1、获得48小时前的时间点
		long before48Hour = DateUtilJiuYuan.getBefore48Hour();//48小时前减1分钟的时间点的的时间戳
		
		//2、根据标记组装48小时条件
		if(member48Mark.getIntValue() == 1){//1（48小时内）
//			大于这个时间点则为48小时内
			wrapper.gt("last_message_time",before48Hour);
		}else if(member48Mark.getIntValue() == 2){//2（48小时前） 
			//小于或等于这个时间点则为48小时前
			wrapper.le("last_message_time",before48Hour);
		}
		
		wrapper.eq("store_id", storeId);
		wrapper.eq("status", 0);
		//3、组装搜索词条件
		if(StringUtils.isNotEmpty(searchWord)){
//			wrapper.like("user_nickname", searchWord);
			wrapper.andNew("").like("user_nickname", searchWord);
	    	wrapper.or("").like("memo_name", searchWord);
		}
		
		wrapper.orderBy("last_message_time",false);
		
		return wrapper;
	}
	
	
	/**
	 * @param member48Mark 筛选条件：0（全部）、1（48小时内）、2（48小时前）
	 * @param searchWord 搜索词
	 */
	public SmallPage list(Member48MarkEnum member48Mark,String searchWord,StoreBusiness storeBusiness,Page<ShopMember> page,long storeId) {
		//组装条件构造器
		Wrapper<ShopMember> wrapper = buildListWrapper(member48Mark,searchWord,storeId);
		page.setRecords(memberMapper.selectPage(page,wrapper));
		
		//1、获得48小时前的时间点
		long before48Hour = DateUtilJiuYuan.getBefore48Hour();//48小时前减1分钟的时间点的的时间戳
				
		//组装数据
   	 	SmallPage smallPage = new SmallPage(page);
   	 	List<ShopMember> list = smallPage.getRecords();
   	 	List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
   	 	
   	 	Map<String,Object> selectMap = new HashMap<String,Object>();
		selectMap.put("store_id", storeId);
		List<MemberMemo> memberMemoList = memberMemoMapper.selectByMap(selectMap);
   	 	for(ShopMember member : list){
   	 		Map<String,String> messageMap = new HashMap<String,String>();
   	 		messageMap.put("id", String.valueOf(member.getId()));
   	 		messageMap.put("bindPhone", member.getBindPhone());
   	 		messageMap.put("memoName", member.getMemoName());
   	 		messageMap.put("userNickname", member.getUserNickname());
   	 		messageMap.put("userIcon", member.getUserIcon());
   	 		messageMap.put("lastMessageTime", String.valueOf(member.getLastMessageTime()));
   	 		messageMap.put("lastMessageType", String.valueOf(member.getLastMessageType()));
   	 		messageMap.put("lastMessageContent", member.getLastMessageContent());
   	 		messageMap.put("notReadMessageCount", String.valueOf(member.getNotReadMessageCount()));
   	 		long lastMessageTime = member.getLastMessageTime();
   	 		boolean is48HourBefore = lastMessageTime <= before48Hour;
   	 		if(is48HourBefore){//小于48小时前时间点则为48小时前
   	 			messageMap.put("is48HourBefore", String.valueOf(1));
   	 		}else{
   	 			messageMap.put("is48HourBefore", String.valueOf(0));
   	 		}
   	 		//如果有备注名则将昵称改为备注名
//   	 		if(memberMemoList != null && memberMemoList.size() > 0){
//   	 			for(MemberMemo memberMemo : memberMemoList){
//   	 				if(member.getId().intValue() == memberMemo.getMemberId().intValue() && memberMemo.getMemoName() != null && memberMemo.getMemoName().trim().length() > 0){
//   	 					messageMap.put("userNickname", String.valueOf(memberMemo.getMemoName()));
//   	 				}
//   	 			}
//   	 		}
    	   	mapList.add(messageMap);
   	 	}
   	 	smallPage.setRecords(mapList);
		return smallPage;
	}
	
	/**
	 * 根据条件获取会员列表（不翻页）
     * @param member48Mark 筛选条件：0（全部）、1（48小时内）、2（48小时前）
	 * @return
	 */
	public List<ShopMember> getMemberList(Member48MarkEnum member48Mark,long storeId) {
		Wrapper<ShopMember> wrapper = buildListWrapper(member48Mark,null,storeId);
		return memberMapper.selectList(wrapper);
	}
	
	
	
	/**
	 * 根据会员ID获取会员信息
	 * @param memberId
	 * @return
	 */
	public ShopMember getMemberById(long memberId) {
		return memberMapper.selectById(memberId);
	}
	
	/**
	 * 根据微信会员openId获取会员信息
	 */
	public ShopMember getMemberByBindWeixin(String bindWeixin,long storeId) {
		Wrapper<ShopMember> wrapper = new EntityWrapper<ShopMember>().eq("bind_weixin", bindWeixin).eq("store_id", storeId);
		
		List<ShopMember> list = memberMapper.selectList(wrapper);
		ShopMember member = null;
		if(list.size() > 0){
			member = list.get(0);
		}
		return member;
	}
	
	/**
	 * 获取我的信息
	 * @param memberId 会员Id
	 * @param storeId 
	 * @return
	 */
	public Map<String,Object> getMineMemberInfo(long memberId, long storeId) {

			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,String> memberInfo = new HashMap<String,String>();
			ShopMember mineMember = memberMapper.findMyInformationById(memberId);
//			ShopMember mineMember = memberMapper.selectById(memberId);

			memberInfo.put("id", mineMember.getId() + "");
			memberInfo.put("bindWeixin", mineMember.getBindWeixin());
			memberInfo.put("userNickname", mineMember.getUserNickname());
			memberInfo.put("userIcon", mineMember.getUserIcon());
			memberInfo.put("minicodeUrl", mineMember.getMinicodeUrl());
			memberInfo.put ("sex", mineMember.getSex ()+"");
			
			//会员收藏数量
			int myFavoriteCount = shopMemberFavoriteService.getMyFavoriteCount(memberId, storeId);
			memberInfo.put("myFavoriteCount", String.valueOf(myFavoriteCount));

			//可领取优惠券数量
			 //List<ShopCouponTemplate> waitGetShopCouponTemplateList = shopCouponTemplateService.getWaitGetShopCouponTemplateList(storeId,memberId);
			//  memberInfo.put("waitGetShopCouponTemplateCount", String.valueOf(waitGetShopCouponTemplateList.size()));


			//我的订单数量
			Map<String,String> orderCount = shopMemberOrderService.getOrderCount(memberId,storeId);
			data.put("orderCount", orderCount);


			data.put("memberInfo", memberInfo);
//			logger.info("===============获取我的消息data："+JSONObject.toJSONString(data));
			return data;
	}
	
	/**
	 * 获取会员信息
	 * @param memberId 会员Id
	 * @return
	 */
	public JsonResponse getMemberMemo(Long memberId, UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> selectMap = new HashMap<String,Object>();
			selectMap.put("member_id", memberId);
			selectMap.put("store_id", userDetail.getId());
			List<MemberMemo> memberMemoList = memberMemoMapper.selectByMap(selectMap);
			Map<String,String> memberMemoMap = new HashMap<String,String>();
			
			ShopMember mineMember = memberMapper.selectById(memberId);
			
//			long currentTime = DateUtil.current(true);
//			long nowTime = System.currentTimeMillis();
//			System.out.println(currentTime);
//			System.out.println(nowTime);
			if(mineMember != null){
				memberMemoMap.put("bindWeixin", mineMember.getBindWeixin());
				memberMemoMap.put("userNickname", mineMember.getUserNickname());
				memberMemoMap.put("userIcon", mineMember.getUserIcon());
				memberMemoMap.put("minicodeUrl", mineMember.getMinicodeUrl());
				
				memberMemoMap.put("regTime", UtilDate.getDateStrFromMillis(mineMember.getCreateTime(),UtilDate.yearMonthDay));
				
				//1、获得48小时前的时间点
				long before48Hour = DateUtilJiuYuan.getBefore48Hour();//48小时前减1分钟的时间点的的时间戳
				long lastMessageTime = mineMember.getLastMessageTime();
	   	 		boolean is48HourBefore = lastMessageTime <= before48Hour;
	   	 		if(is48HourBefore){//小于48小时前时间点则为48小时前
	   	 			memberMemoMap.put("is48HourBefore", String.valueOf(1));
	   	 		}else{
	   	 			memberMemoMap.put("is48HourBefore", String.valueOf(0));
	   	 		}
				
			}else{
				memberMemoMap.put("userNickname", "");
				memberMemoMap.put("userIcon", "");
				memberMemoMap.put("minicodeUrl", "");
				memberMemoMap.put("regTime", "");
				memberMemoMap.put("is48HourBefore", "");
			}
			
			
			if(memberMemoList != null && memberMemoList.size() > 0){
				MemberMemo memberMemo = memberMemoList.get(0);
				
				memberMemoMap.put("id", memberMemo.getId() + "");
				memberMemoMap.put("memo_name", memberMemo.getMemoName());
				memberMemoMap.put("memo_phone", memberMemo.getMemoPhone());
				memberMemoMap.put("memo_gender", memberMemo.getMemoGender());
				memberMemoMap.put("memo_career", memberMemo.getMemoCareer());
				memberMemoMap.put("memo_earning", memberMemo.getMemoEarning());
				memberMemoMap.put("memo_size", memberMemo.getMemoSize());
				memberMemoMap.put("memo_character", memberMemo.getMemoCharacter());
				memberMemoMap.put("memo_province_name", memberMemo.getMemoProvinceName());
				memberMemoMap.put("memo_city_name", memberMemo.getMemoCityName());
				memberMemoMap.put("district_name", memberMemo.getMemoDistrictName());
				memberMemoMap.put("memo_address_detail", memberMemo.getMemoAddressDetail());
				memberMemoMap.put("memo_content", memberMemo.getMemoContent());
				memberMemoMap.put("birthday_year", memberMemo.getBirthdayYear());
				memberMemoMap.put("birthday_month", memberMemo.getBirthdayMonth());
				memberMemoMap.put("birthday_day", memberMemo.getBirthdayDay());
				memberMemoMap.put("bust_size", memberMemo.getBustSize());
				memberMemoMap.put("waist_size", memberMemo.getWaistSize());
				memberMemoMap.put("hip_size", memberMemo.getHipSize());
				memberMemoMap.put("height", memberMemo.getHeight());
				memberMemoMap.put("weight", memberMemo.getWeight());
				
				
			}else{
				memberMemoMap.put("id", memberId + "");
				memberMemoMap.put("memo_name", "");
				memberMemoMap.put("memo_phone", "");
				memberMemoMap.put("memo_gender", "");
				memberMemoMap.put("memo_career", "{\"selected_ids\":\"\",\"other\":\"\"}");
				memberMemoMap.put("memo_earning", "");
				memberMemoMap.put("memo_size", "");
				memberMemoMap.put("memo_character", "{\"selected_ids\":\"\",\"other\":\"\"}");
				memberMemoMap.put("memo_province_name", "");
				memberMemoMap.put("memo_city_name", "");
				memberMemoMap.put("district_name", "");
				memberMemoMap.put("memo_address_detail", "");
				memberMemoMap.put("memo_content", "");
				memberMemoMap.put("birthday_year", "");
				memberMemoMap.put("birthday_month", "");
				memberMemoMap.put("birthday_day","");
				memberMemoMap.put("bust_size", "");
				memberMemoMap.put("waist_size", "");
				memberMemoMap.put("hip_size", "");
				memberMemoMap.put("height", "");
				memberMemoMap.put("weight", "");
			}
			

	
			memberMemoMap.put("start_year", "1950");
			//			memberMemoMap.put("end_year", "2017");
			memberMemoMap.put("start_bust", "80");
			memberMemoMap.put("end_bust", "100");
			
			memberMemoMap.put("start_waist", "61");
			memberMemoMap.put("end_waist", "95");
			
			memberMemoMap.put("start_hip", "85");
			memberMemoMap.put("end_hip", "105");
			
			memberMemoMap.put("name_tip", "请输入备注名，限10个字符");
			memberMemoMap.put("phone_tip", "请输入手机号码");
			memberMemoMap.put("height_tip", "请输入身高数据");
			memberMemoMap.put("weight_tip", "请输入体重数据");
			memberMemoMap.put("other_input_tip", "请在这里填写");
			memberMemoMap.put("address_tip", "请输入详细地址");
			memberMemoMap.put("other_input_tip", "请在这里填写其它备注内容，最多400个字。");
			
			memberMemoMap.put("earning_option", "{\"field\":\"earning\",\"type\":\"radio\",\"options\":[{\"id\":1,\"text\":\"2000元以下\"},{\"id\":2,\"text\":\"2000-5000元\"},{\"id\":3,\"text\":\"5000-10000元\"},{\"id\":4,\"text\":\"1万元以上\"}]}");
			
			memberMemoMap.put("character_option", "{\"field\":\"character\",\"type\":\"checkbox\",\"options\":[{\"id\":1,\"text\":\"自命不凡、自以为是\"},{\"id\":2,\"text\":\"脾气暴躁、爱唱反调\"},{\"id\":3,\"text\":\"犹豫不决\"},{\"id\":4,\"text\":\"小心谨慎\"},{\"id\":5,\"text\":\"来去匆匆、做事爽快\"},{\"id\":6,\"text\":\"爱贪小便宜\"},{\"id\":7,\"text\":\"经济不足\"},{\"id\":-1,\"text\":\"其他性格\"}]}");
			memberMemoMap.put("career_option", "{\"field\":\"career\",\"type\":\"radio\",\"options\":[{\"id\":1,\"text\":\"教师/幼教\"},{\"id\":2,\"text\":\"企业主\"},{\"id\":3,\"text\":\"IT互联网\"},{\"id\":4,\"text\":\"律师\"},{\"id\":5,\"text\":\"医生/护士\"},{\"id\":6,\"text\":\"微商\"},{\"id\":7,\"text\":\"金融行业\"},{\"id\":8,\"text\":\"自由职业\"},{\"id\":9,\"text\":\"公务员\"},{\"id\":10,\"text\":\"学生\"},{\"id\":11,\"text\":\"销售客服\"},{\"id\":12,\"text\":\"财务/人力/行政\"},{\"id\":-1,\"text\":\"其它职业\"}]}");
			data.put("memberMemo", memberMemoMap);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			return jsonResponse.setError("获取我的信息失败");
		}
	}
	
	
//	/**
//	 * 修改会员的未读消息数量
//	 * @param subtractNoStateCount 减少未读数量
//	 * @param memberId
//	 */
//	public void updateNoReadCount(long memberId,int subtractNoStateCount) {
//		Member member = memberMapper.selectById(memberId);
//		logger.info("============未读消息数量减"+subtractNoStateCount+"开始===================member："+JSONObject.toJSONString(member));
//		int count = member.getNotReadMessageCount() - subtractNoStateCount;
//		logger.info("============未读消息数量减1===================count："+count);
//		if(count < 0){
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("============未读消息数量错误，不应该为负数，请尽快排查问题==================="
//					+ "count:"+count+",memberId:"+memberId+"notReadMessageCount:"+member.getNotReadMessageCount()+",subtractNoStateCount:"+subtractNoStateCount);
//			logger.info("=======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//			logger.info("======================================================================");
//		}
//		Member memberNew = new Member();
//		memberNew.setId(memberId);
//		memberNew.setNotReadMessageCount(count);
//		memberMapper.updateById(member);
//		logger.info("============未读消息数量减1完成===================member："+JSONObject.toJSONString(member));
//	}
	
	/**
     * 修改我的头像/昵称
     * @param id	用户Id
     * @param key	想要修改的字段 userNickname:用户昵称 userIcon:用户头像
     * @param value	想要修改的字段对应的值
     * @return
     */
	public void updateFieldValue(Long id, String key, String value) {
			if(id==null || id<1){
				throw new RuntimeException("会员信息有误,请确认");
			}
			ShopMember entity = new ShopMember();
			entity.setId(id);
			if("userNickname".equals(key)){
				entity.setUserNickname(value);
			}else if("userIcon".equals(key)){
				entity.setUserIcon(value);
			}
			Integer record = memberMapper.updateById(entity);
			if(record!=1){
				throw new RuntimeException("修改失败");
			}
	}
	/**
	 * 修改我的头像/昵称
	 * @param memberId	用户Id
	 * @return
	 */
	public JsonResponse updateMemberMemo(Long memberId,
			String memo_name,
			String memo_phone,
			String memo_gender,
			String memo_career,
			String memo_earning,
			String memo_character,
			String memo_province_name,
			String memo_city_name,
			String district_name,
			String memo_address_detail,
			String memo_content,
			String birthday_year,
			String birthday_month,
			String birthday_day,
			String bust_size,
			String waist_size,
			String hip_size,
			String height,
			String weight, UserDetail userDetail ) {
		
		long nowTime = System.currentTimeMillis();
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Integer record = 0;
			Map<String,Object> selectMap = new HashMap<String,Object>();
			selectMap.put("member_id", memberId);
			selectMap.put("store_id", userDetail.getId());
			List<MemberMemo> memberMemoList = memberMemoMapper.selectByMap(selectMap);
			if(memberMemoList != null && memberMemoList.size() > 0){
				MemberMemo memberMemo = memberMemoList.get(0);
				memberMemo.setMemoName(memo_name);
				memberMemo.setMemoPhone(memo_phone);
				memberMemo.setMemoGender(memo_gender);
				memberMemo.setMemoCareer(memo_career);
				memberMemo.setMemoEarning(memo_earning);
				memberMemo.setMemoCharacter(memo_character);
				memberMemo.setMemoProvinceName(memo_province_name);
				memberMemo.setMemoCityName(memo_city_name);
				memberMemo.setMemoDistrictName(district_name);
				memberMemo.setMemoAddressDetail(memo_address_detail);
				memberMemo.setMemoContent(memo_content);
				memberMemo.setBirthdayYear(birthday_year);
				memberMemo.setBirthdayMonth(birthday_month);
				memberMemo.setBirthdayDay(birthday_day);
				memberMemo.setBustSize(bust_size);
				memberMemo.setWaistSize(waist_size);
				memberMemo.setHipSize(hip_size);
				memberMemo.setHeight(height);
				memberMemo.setWeight(weight);
				memberMemo.setStoreId(userDetail.getId());
				memberMemo.setUpdateTime(nowTime);
				
				record = memberMemoMapper.updateById(memberMemo);
				
			}else{
				MemberMemo memberMemo = new MemberMemo();
				memberMemo.setMemoName(memo_name);
				memberMemo.setMemoPhone(memo_phone);
				memberMemo.setMemoGender(memo_gender);
				memberMemo.setMemoCareer(memo_career);
				memberMemo.setMemoEarning(memo_earning);
				memberMemo.setMemoCharacter(memo_character);
				memberMemo.setMemoProvinceName(memo_province_name);
				memberMemo.setMemoCityName(memo_city_name);
				memberMemo.setMemoDistrictName(district_name);
				memberMemo.setMemoAddressDetail(memo_address_detail);
				memberMemo.setMemoContent(memo_content);
				memberMemo.setBirthdayYear(birthday_year);
				memberMemo.setBirthdayMonth(birthday_month);
				memberMemo.setBirthdayDay(birthday_day);
				memberMemo.setBustSize(bust_size);
				memberMemo.setWaistSize(waist_size);
				memberMemo.setHipSize(hip_size);
				memberMemo.setHeight(height);
				memberMemo.setWeight(weight);
				memberMemo.setMemberId(memberId);
				memberMemo.setStoreId(userDetail.getId());
				memberMemo.setCreateTime(nowTime);
				memberMemo.setUpdateTime(nowTime);
				record = memberMemoMapper.insert(memberMemo);
			}
			
			
			if(record != 1){
				return jsonResponse.setError("修改或新增失败");
			}else{
				//同时更新member表备注名
				ShopMember member = new ShopMember();
				member.setId(memberId);
				member.setMemoName(memo_name);
				memberMapper.updateById(member);
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("修改或新增失败");
		}
	}

	/**
	 * 查询会员信息
	 * @param memberId
	 * @return
	 */
    public ShopMember findMemberById(Long memberId) {
    	return memberMapper.findMemberById(memberId);
    }
}