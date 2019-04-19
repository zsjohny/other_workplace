package com.jiuyuan.service.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.PlacardApplyMapper;
import com.jiuyuan.dao.mapper.supplier.PlacardReadMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierPlacardMapper;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.SupplierPlacard;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.UrlUtil;

/**
 * 供应商公告
 */
@Service
public class SupplierPlacardService implements ISupplierPlacardService  {
	private static final Logger logger = LoggerFactory.getLogger(SupplierPlacardService.class);
	
	
	

	@Autowired
	private PlacardApplyMapper placardApplyMapper;
	@Autowired
	private IPlacardReadService placardReadService;
	@Autowired
	private IPlacardApplyService placardApplyService;
	
	@Autowired
	private SupplierPlacardMapper supplierPlacardMapper;
	
	
	
	public SupplierPlacard getSupplierPlacardInfo(long supplierPlacardId){
		return supplierPlacardMapper.selectById(supplierPlacardId);
	}
	
	/**
	 * 公告总数
	 * @return
	 */
	public int getTotalCount(){
		Wrapper<SupplierPlacard> wrapper = new EntityWrapper<SupplierPlacard>();
		return supplierPlacardMapper.selectCount(wrapper);
	}
	
	/**
	 * 获取未读公告数量
	 * @return
	 */
	public int getNoReadCount(long supplierId){
		return supplierPlacardMapper.getNoReadCount(supplierId);
	}
	
	
	/**
	 * 获取未读公告列表
	 * @param supplierId
	 * @return
	 */
	public List<SupplierPlacard> getHomePageNoReadPlacardListTop5(long supplierId){
		return supplierPlacardMapper.getNoReadPlacardTop5(supplierId);
	}
	
	/**
	 * 
	 * @param supplierId
	 * @return
	 */
	public void increaseReadCount(long placardId){
		 supplierPlacardMapper.increaseReadCount(placardId);
	}
	
	/**
	 * 增加公告通知数
	 * @param placardId
	 */
	public void increaseNotifyCount(long placardId){
		 supplierPlacardMapper.increaseNotifyCount(placardId);
	}
	
	@Override
	public Map<String,String> getNoReadPlacard(long supplierId) {
		//1、获取前五条通知公告中未读公告列表
		List<SupplierPlacard> noReadlist = getAdvicePlacardTop5(supplierId);
		
		//获取最新一条公告信息
		long placardId = 0;
		String placardTitle = "";
		String placardContent = "";
		long publishTime = 0;
		int isHaveNext = 0;
		if(noReadlist.size() > 0){
			SupplierPlacard supplierPlacard = noReadlist.get(0);
			placardId = supplierPlacard.getId();
			placardTitle = supplierPlacard.getTitle();
			placardContent = supplierPlacard.getContent();
			publishTime = supplierPlacard.getPublishTime();
			if(noReadlist.size() > 1){
				isHaveNext = 1;//有下一条公告
			}
		}
		
		//3、组装数据
		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
		supplierPlacardMap.put("placardId", String.valueOf(placardId));//公告ID
		supplierPlacardMap.put("placardTitle", placardTitle);//公告标题
		supplierPlacardMap.put("placardContent", placardContent);//公告内容
		supplierPlacardMap.put("publishTime", DateUtil.parseLongTime2Str3(publishTime));//发布时间
		supplierPlacardMap.put("isHaveNext", String.valueOf(isHaveNext));//是否有下一条公告：0无下一条、1有下一条
		
		//4、添加阅读记录
		placardReadService.setPlacardIsRead(placardId,supplierId);
		
		//3、增加公告阅读记录数
		increaseReadCount(placardId);
		
		//4、增加公告通知数
		increaseNotifyCount(placardId);
		
		return supplierPlacardMap;
	}

	private List<SupplierPlacard> getAdvicePlacardTop5(long supplierId) {
		//获取5条发布的且通知的公告
		List<SupplierPlacard> list = supplierPlacardMapper.getAdvicePlacardTop5(supplierId);
		List<SupplierPlacard> noReadlist = new ArrayList<SupplierPlacard>();
		//去除已读公告
		for(SupplierPlacard supplierPlacard : list){
			long placardId = supplierPlacard.getId();
			int readState = placardReadService.getReadState(placardId,supplierId);//获取公告读取状态（阅读状态：0未读、1已读）
			if(readState == 0){
				noReadlist.add(supplierPlacard);
			}
		}
		return noReadlist;
	}
	
	

	/**
	 * 通知中公告总数
	 * @return
	 */
	public int getNotifyCount(){
		Wrapper<SupplierPlacard> wrapper = new EntityWrapper<SupplierPlacard>();
		wrapper.eq("state", SupplierPlacard.state_notify);
		return supplierPlacardMapper.selectCount(wrapper);
	}
	
	
	public void stopSupplierPlacard(long supplierPlacardId){
		SupplierPlacard supplierPlacard = new SupplierPlacard();
		supplierPlacard.setId(supplierPlacardId);
		supplierPlacard.setState(SupplierPlacard.state_stop);
		supplierPlacardMapper.updateById(supplierPlacard);
	}
	
	public void updateSupplierPlacard(long placardId, String title, String content, int type,int isSendAdvice, int publishType,long publishTime,long applyEndTime){
		SupplierPlacard supplierPlacardOld = supplierPlacardMapper.selectById(placardId);
		int supplierPlacardType = supplierPlacardOld.getType();
		//报名公告改为普通公告校验是否有报名申请单
		if(supplierPlacardType == SupplierPlacard.type_apply && type == SupplierPlacard.type_general){
			Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
			wrapper.eq("placard_id", placardId);
			int count= placardApplyMapper.selectCount(wrapper);
			if(count > 0){
				throw new RuntimeException("更新失败！本公告已有相关报名单，不允许关闭报名功能");
			}
		}
		
		SupplierPlacard supplierPlacard = new SupplierPlacard();
		supplierPlacard.setId(placardId);
		supplierPlacard.setTitle(title);
		supplierPlacard.setContent(UrlUtil.getURLDecoderString(content));
		supplierPlacard.setType(type);
		supplierPlacard.setIsSendAdvice(isSendAdvice);//是否发送站内通知：0不通知、1通知
		supplierPlacard.setPublishType(isSendAdvice);
		if(isSendAdvice == 1){
			supplierPlacard.setState(SupplierPlacard.state_notify);//公告状态:0已发布、1通知中、2已停止
		}else{
			supplierPlacard.setState(SupplierPlacard.state_publish);//公告状态:0已发布、1通知中、2已停止
		}
		supplierPlacard.setPublishType(publishType);
		if(publishType == 0){//发布类型：0立即、1定时
			supplierPlacard.setPublishTime(System.currentTimeMillis());
		}else{
			supplierPlacard.setPublishTime(publishTime);
		}
		supplierPlacard.setApplyEndTime(applyEndTime);
		supplierPlacardMapper.updateById(supplierPlacard);
		
		
//		修改公告申请中的公告信息（公告标题placardTitle、报名结束时间applyEndTime） 
		List<PlacardApply> placardApplyList = placardApplyService.getPlacardApplyList(placardId);
		for(PlacardApply placardApply : placardApplyList){
			PlacardApply placardApplyNew = new PlacardApply(); 
			placardApplyNew.setId(placardApply.getId());
			placardApplyNew.setPlacardTitle(title);
			placardApplyNew.setApplyEndTime(applyEndTime);
			placardApplyMapper.updateById(placardApplyNew);
		}
		
		
	}
	
	
	public void addSupplierPlacard(long adminId, String title, String content,int type,int isSendAdvice, int publishType,long publishTime,long applyEndTime){
		
		SupplierPlacard supplierPlacard = new SupplierPlacard();
		supplierPlacard.setTitle(title);//公告标题
		supplierPlacard.setContent(UrlUtil.getURLDecoderString(content));//公告内容
		supplierPlacard.setType(type);//公告类型:0普通公告(报名状态关闭)、1报名公告（报名状态开启）
		supplierPlacard.setIsSendAdvice(isSendAdvice);//是否发送站内通知：0不通知、1通知
		supplierPlacard.setPublishType(publishType);//发布类型：0立即、1定时
		supplierPlacard.setApplyEndTime(applyEndTime);//报名结束时间
		supplierPlacard.setCreateTime(System.currentTimeMillis());//创建时间
		
		if(isSendAdvice == 1){
			supplierPlacard.setState(SupplierPlacard.state_notify);//公告状态:0已发布、1通知中、2已停止
		}else{
			supplierPlacard.setState(SupplierPlacard.state_publish);//公告状态:0已发布、1通知中、2已停止
		}
		
		
		if(publishType == 0){//发布类型：0立即、1定时
			supplierPlacard.setPublishTime(System.currentTimeMillis());
		}else{
			supplierPlacard.setPublishTime(publishTime);
		}
		
		supplierPlacard.setAdminId(adminId);//创建人ID
		supplierPlacardMapper.insert(supplierPlacard);
	}
	
	
	
	public List<SupplierPlacard> supplierGetPlacardList(Page<Map<String,String>> page, 
			String title, int type, long publishTimeBegin, long publishTimeEnd, int applyState,int readState,long supplierId){
		Wrapper<SupplierPlacard> wrapper = new EntityWrapper<SupplierPlacard>();
		wrapper.in("state", SupplierPlacard.state_publish+","+SupplierPlacard.state_notify);//0已发布、1通知中
		wrapper.lt("publish_time", System.currentTimeMillis());
		if(StringUtils.isNotEmpty(title)){
			wrapper.like("title", title);
		}
		
		if(publishTimeBegin != 0){
			wrapper.gt("publish_time", publishTimeBegin);
		}
		if(publishTimeEnd != 0){
			wrapper.lt("publish_time", publishTimeEnd);
		}
		
		
		if(type != -1){
			wrapper.eq("type", type);//公告类型:-1全部0普通公告、1报名公告
		}
		
		
		if(readState != -1){
			if(readState == 1){
				wrapper.and(" FIND_IN_SET("+supplierId+",is_read_supplier_ids) ");//已读
			}else{
				wrapper.and(" id not in( select id from store_supplier_placard WHERE FIND_IN_SET("+supplierId+",is_read_supplier_ids)) ");//未读
			}
		}
		
		if(applyState != -1){//报名活动状态:-1全部、0报名进行中、1报名已经结束
			long time =  System.currentTimeMillis();
			if(applyState == 0){//0报名进行中
				wrapper.eq("type", 1).gt("apply_end_time", time);
			}else if(applyState == 1){//1报名已经结束
				wrapper.eq("type", SupplierPlacard.type_apply).lt("apply_end_time", time);
			}
		}
		wrapper.orderBy("id", false);
		List<SupplierPlacard> supplierPlacardList = supplierPlacardMapper.selectPage(page,wrapper);
		return supplierPlacardList;
	}
	
	public List<SupplierPlacard> getSupplierPlacardList(Page<Map<String,String>> page, long supplierPlacardId, String title,String content,
				long publishTimeBegin, long publishTimeEnd, int state, int type){
		logger.info("获取供应商列表：getSupplierPlacardList____supplierPlacardId:"+supplierPlacardId+",title:"+title+
				",content:"+content+
				",publishTimeBegin:"+publishTimeBegin+",publishTimeEnd:"+publishTimeEnd
				+",state:"+state+",type:"+type);
		Wrapper<SupplierPlacard> wrapper = new EntityWrapper<SupplierPlacard>();
		if(supplierPlacardId != 0){
			wrapper.eq("id", supplierPlacardId);
		}
		if(StringUtils.isNotEmpty(title)){
			wrapper.like("title", title);
		}
		if(StringUtils.isNotEmpty(content)){
			wrapper.like("content", content);
		}
		if(publishTimeBegin > 0){
			wrapper.gt("publish_time", publishTimeBegin);
		}
		if(publishTimeEnd > 0){
			wrapper.lt("publish_time", publishTimeEnd);
		}
		if(state != -1){
			wrapper.eq("state", state);
		}
		if(type != -1){
			wrapper.eq("type", type);
		}
		wrapper.orderBy("id", false);
		List<SupplierPlacard> supplierPlacardList = supplierPlacardMapper.selectPage(page,wrapper);
		return supplierPlacardList;
	}

	

	

	
}