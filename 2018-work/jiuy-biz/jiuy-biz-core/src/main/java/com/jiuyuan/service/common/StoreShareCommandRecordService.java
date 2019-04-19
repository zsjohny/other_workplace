package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreShareCommandLogMapper;
import com.jiuyuan.dao.mapper.supplier.StoreShareCommandRecordMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.StoreShareCommandLog;
import com.jiuyuan.entity.newentity.StoreShareCommandRecord;
import com.jiuyuan.util.NumberUtil;
import com.jiuyuan.util.ResultCodeException;

@Service
public class StoreShareCommandRecordService implements IStoreShareCommandRecordService {
	private static final Logger logger = LoggerFactory.getLogger(StoreShareCommandRecordService.class);
	
	@Autowired
	private StoreShareCommandRecordMapper storeShareCommandRecordMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;

	@Autowired
	private IProductMonitorService productMonitorService;
	
	@Autowired
	private StoreShareCommandLogMapper storeShareCommandLogMapper;

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreShareCommandRecordService#getValidKey(java.lang.String)
	 */
	@Override
	public List<StoreShareCommandRecord> getValidKey(String key) {
		Wrapper<StoreShareCommandRecord> wrapper = new EntityWrapper<StoreShareCommandRecord>();
		wrapper.eq("share_command", key);
		wrapper.ge("share_deadline", System.currentTimeMillis());
		List<StoreShareCommandRecord> list = storeShareCommandRecordMapper.selectList(wrapper);
		return list;
	}
    /* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreShareCommandRecordService#insertShareCommandRecord(com.jiuyuan.entity.newentity.StoreShareCommandRecord)
	 */
	@Override
	public void insertShareCommandRecord(StoreShareCommandRecord shareCommandRecord) {
		storeShareCommandRecordMapper.insert(shareCommandRecord);
	}
    
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreShareCommandRecordService#openShareCommandURL(java.lang.String)
	 */
	@Override
	public Map<String,Object> parseShareCommand(String key) {
		Map<String,Object> map = new HashMap<String,Object>();
		//获取该口令下的有效分享记录
		List<StoreShareCommandRecord> list= getValidKey(key);
		if(!key.matches("[a-zA-Z0-9]{10}")){
			logger.info("该分享口令无效！key:"+key);
			throw new ResultCodeException(ResultCode.INVALID_SHARE_COMMAND);
		}
		if(null==list || list.size()==0){
			logger.info("该分享口令无效！key:"+key);
			throw new ResultCodeException(ResultCode.INVALID_SHARE_COMMAND);
		}
		StoreShareCommandRecord shareCommandRecord = list.get(0);
		//获取商品Id
		long productId = shareCommandRecord.getProductId();
		ProductNew productNew = productNewMapper.selectById(productId);
		long id = shareCommandRecord.getId();
		StoreShareCommandRecord newShareCommandRecord = new StoreShareCommandRecord();
		//添加解析分享的次数
		newShareCommandRecord.setId(id);
		long parseTimes = shareCommandRecord.getParseTimes();
		newShareCommandRecord.setParseTimes(++parseTimes);
		//更新数据
		updateShareCommandRecord(newShareCommandRecord);
		//商品Id
		map.put("productId", productId);
		//商品标题
		map.put("productName", productNew.getName());
		Double price = productNew.getMinLadderPrice();
		if (productNew.getMemberLevel()>0){
			price=productNew.getMemberLadderPriceMin().doubleValue();
		}
		//商品价格
		map.put("productPrice", price);
		//商品主图
		map.put("productMainImg", productNew.getMainImg());
		//分享口令ID
		map.put("shareCommandId", id);
		map.put("saleTotalCount",productNew.getSaleTotalCount());
		productMonitorService.fillProductMonitorProductMap(map,"productId");
		return map;
		
	}
	private void updateShareCommandRecord(StoreShareCommandRecord newShareCommandRecord) {
		storeShareCommandRecordMapper.updateById(newShareCommandRecord);
	}
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IStoreShareCommandRecordService#getShareCommandByStoreIdAndProductId(long, long)
	 */
	@Override
	public StoreShareCommandRecord getShareCommandByStoreIdAndProductId(long storeId, long productId) {
		Wrapper<StoreShareCommandRecord> wrapper = new EntityWrapper<StoreShareCommandRecord>();
		wrapper.ge("share_deadline", System.currentTimeMillis())
		       .eq("share_man", storeId)
		       .eq("product_id", productId);
		List<StoreShareCommandRecord> list = storeShareCommandRecordMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public String getShareCommand(long storeId, long productId, long validTime) {
		//先查找是否有该分享者与商品ID相同的分享口令
		StoreShareCommandRecord storeShareCommandRecord = getShareCommandByStoreIdAndProductId(storeId,productId);
		if(null != storeShareCommandRecord){
			return storeShareCommandRecord.getShareCommandContent();
		}
		//获取分享口令
		String key = "";
		while(true){
			key = NumberUtil.genRandomNum(10, true);
			List<StoreShareCommandRecord> shareCommandRecordList =  getValidKey(key);
			if(null == shareCommandRecordList || shareCommandRecordList.size() == 0){
				break;
			}
		}
		logger.info("分享口令:"+key+"商品Id:"+productId);
		//获取该商品信息
		ProductNew productNew = productNewMapper.selectById(productId);
		if(null == productNew){
			logger.info("没有该商品ID，无法生成分享口令");
			throw new ResultCodeException(ResultCode.CANT_GEN_SHARE_COMMAND);
		}
		//获取商品标题，品牌名称
		String productName = productNew.getName();
		productName = productName==null?"":productName;
		String brandName = productNew.getBrandName();
		StringBuffer stringBuffer = new StringBuffer();
		if(null ==brandName || brandName.equals("")||brandName.contains("自营")){
			//如果没有品牌名
			stringBuffer.append("【")
			            .append(productName)
			            .append("】")
			            .append("复制这条信息￥")
			            .append(key)
			            .append("￥后打开俞姐姐门店宝");
		}else{
			//获取分享口令内容
			stringBuffer.append("【")
			.append(brandName)
			.append("(")
			.append(productName)
			.append(")】")
			.append("复制这条信息￥")
			.append(key)
			.append("￥后打开俞姐姐门店宝");
		}
		String content = stringBuffer.toString();//分享口令内容
		logger.info("分享口令内容："+content);
		//插入分享口令
		StoreShareCommandRecord shareCommandRecord = new StoreShareCommandRecord();
		shareCommandRecord.setProductId(productId);//商品ID
		shareCommandRecord.setShareCommand(key);//口令
		shareCommandRecord.setShareCommandContent(content);//口令复制内容
		long currentTime = System.currentTimeMillis();
		shareCommandRecord.setShareDeadline(currentTime+validTime);//分享截止时间
		shareCommandRecord.setShareMan(String.valueOf(storeId));//分享者ID
		shareCommandRecord.setShareTime(System.currentTimeMillis());//分享时间
		insertShareCommandRecord(shareCommandRecord);
		return content;
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void insertShareCommandLog(long shareCommandId ,long storeId) {
		StoreShareCommandRecord storeShareCommandRecord = storeShareCommandRecordMapper.selectById(shareCommandId);
		//更新打开次数
		StoreShareCommandRecord shareCommandRecord = new StoreShareCommandRecord();
		shareCommandRecord.setId(shareCommandId);
		long openTimes = storeShareCommandRecord.getOpenTimes();
		shareCommandRecord.setOpenTimes(++openTimes);//打开次数
		updateShareCommandRecord(shareCommandRecord);
		//插入日志
		StoreShareCommandLog storeShareCommandLog = new StoreShareCommandLog();
		storeShareCommandLog.setOpenMan(String.valueOf(storeId));
		storeShareCommandLog.setOpenTime(System.currentTimeMillis());
		storeShareCommandLog.setShareCommandId(shareCommandId);
		storeShareCommandLogMapper.insert(storeShareCommandLog);
		
	}


	
	

}
