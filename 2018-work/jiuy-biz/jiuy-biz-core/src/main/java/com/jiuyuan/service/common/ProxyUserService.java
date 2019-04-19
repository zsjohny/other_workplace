/**
 * 
 */
package com.jiuyuan.service.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.util.file.OSSFileUtil;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.dao.mapper.supplier.ProxyCustomerMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyProductMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyUserMapper;
import com.jiuyuan.dao.mapper.supplier.WxaproxyStockActionLogMapper;
import com.jiuyuan.entity.newentity.ProxyCustomer;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.entity.newentity.ProxyUser;
import com.jiuyuan.entity.newentity.WxaproxyStockActionLog;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.DoubleUtil;
import com.jiuyuan.util.JiuyMultipartFile;
import com.jiuyuan.util.QRCodeUtil;

/**
 * 代理商
 */
@Service
public class ProxyUserService implements IProxyUserService  {
	private static final Logger logger = LoggerFactory.getLogger(ProxyUserService.class);
	
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";
    
    public final static String DEFAULTPASSWORD = "12345678";
    
    public final static int DEFAULT_NUMBER_SIZE = 4;
    
    private static final String BUSINESS_NUMBER_PREFIX = "600";
    
    private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;
	
	@Autowired
	private ProxyUserMapper proxyUserMapper;
	
	@Autowired
	private ProxyProductMapper proxyProductMapper;
	@Autowired
	private WxaproxyStockActionLogMapper wxaproxyStockActionLogMapper;
	
	@Autowired
	private IProxyCustomerService proxyCustomerService;
	
	@Autowired
	private IProxyOrderService proxyOrderService;
	
	@Autowired
	private ProxyOrderMapper proxyOrderMapper;
	
	@Autowired
	private ProxyCustomerMapper proxyCustomerMapper;
	
	
	public ProxyUser getProxyUser(long proxyUserId){
		return proxyUserMapper.selectById(proxyUserId);
	}
	public void incrSellOutCount(long proxyUserId,int proxyProductCount){
		ProxyUser user = proxyUserMapper.selectById(proxyUserId);
		ProxyUser updUser = new ProxyUser();
		updUser.setId(proxyUserId);
		updUser.setSellOutCount(user.getSellOutCount()+proxyProductCount);
		proxyUserMapper.updateById(updUser);
	}
	
	/**
	 * 增加库存
	 */
	public void incrStockCount(long proxyUserId, int incrStockCount,long adminId){
		ProxyUser user = proxyUserMapper.selectById(proxyUserId);
		ProxyUser updUser = new ProxyUser();
		updUser.setId(proxyUserId);
		updUser.setStockCount(user.getStockCount()+incrStockCount);
		updUser.setHistoryTotalStockCount(user.getHistoryTotalStockCount()+incrStockCount);
		proxyUserMapper.updateById(updUser);
		
		//记录代理商增加库存记录
		WxaproxyStockActionLog log = new WxaproxyStockActionLog();
		log.setAdminId(adminId);//操作人ID
		log.setProxyUserId(proxyUserId);//代理商ID
		log.setProxyProductId(user.getProxyProductId());//代理产品ID
		log.setIncrStockCount(incrStockCount);//新增库存量
		log.setCreateTime(System.currentTimeMillis());//操作时间
		wxaproxyStockActionLogMapper.insert(log);
	}
	
	/**
	 * 退还库存
	 */
	public void returnStockCount(long proxyUserId, int incrStockCount){
		ProxyUser user = proxyUserMapper.selectById(proxyUserId);
		ProxyUser updUser = new ProxyUser();
		updUser.setId(proxyUserId);
		updUser.setStockCount(user.getStockCount()+incrStockCount);
		proxyUserMapper.updateById(updUser);
	}
	/**
	 * 减少库存
	 */
	public void reduceStockCount(long proxyUserId, int reduceStockCount){
		ProxyUser user = proxyUserMapper.selectById(proxyUserId);
		int stockCount = user.getStockCount();
		if(stockCount >= reduceStockCount){
			ProxyUser updUser = new ProxyUser();
			updUser.setId(proxyUserId);
			updUser.setStockCount(stockCount-reduceStockCount);
			proxyUserMapper.updateById(updUser);
		}else{
			throw new RuntimeException("库存不足！");
		}
	}

	public void updProxyUser(long proxyUserId, String proxyUserName, String proxyUserFullName, String proxyUserPhone,
			String province, String city, String county, String idCardNo, int proxyState){
		
		ProxyUser checkPhoneUser = getProxyUserByPhone(proxyUserPhone,proxyUserId);
		if(checkPhoneUser != null){
			throw new RuntimeException("手机号已经被使用");
		}
		ProxyUser checkNameUser = getProxyUserByName(proxyUserName,proxyUserId);
		if(checkNameUser != null){
			throw new RuntimeException("代理商名称已经被使用");
		}
		
		
		ProxyUser user = new ProxyUser();
		user.setId(proxyUserId);//代理商ID
		user.setProxyUserName(proxyUserName);//代理商名称
		user.setProxyUserFullName(proxyUserFullName);//代理商姓名
		user.setProxyUserPhone(proxyUserPhone);//代理商手机号
		user.setPhone(proxyUserPhone);//手机
		String provinceCityCounty = province+ city+county;
		user.setProvinceCityCounty(provinceCityCounty);//所在省份城市县区
		user.setProvince(province);//所在省份
		user.setCity(city);//所在城市
		user.setCounty(county);//所在县区
		user.setIdCardNo(idCardNo);//身份证号码
		user.setStatus(proxyState);//状态
		proxyUserMapper.updateById(user);
	}
	
	/**
	 * 根据手机号获取代理
	 * 代理商手机号
	 * 排查代理商ID 
	 */
	private ProxyUser getProxyUserByPhone( String proxyUserPhone,long proxyUserId) {
		logger.info("proxyUserPhone:"+proxyUserPhone+".proxyUserId:"+proxyUserId);
		
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.ge("roleid", "6");//代理商角色（系统预设角色）
		if(StringUtils.isNotEmpty(proxyUserPhone)){
			wrapper.like("proxy_user_phone",proxyUserPhone);//代理商手机号
		}
		List<ProxyUser> list = proxyUserMapper.selectList(wrapper);
		if(list.size() > 0){
			ProxyUser proxyUser = list.get(0);
			logger.info("proxyUserId:"+proxyUserId+".proxyUser:"+JSON.toJSONString(proxyUser));
			if(proxyUserId == 0){
				return proxyUser;
			} else if( proxyUser.getId() != proxyUserId){//排查指定ID
				return proxyUser;
			}
		}
		return null;
	}
	/**
	 * 根据手机号获取代理
	 * 代理商手机号
	 * 排查代理商ID 
	 */
	private ProxyUser getProxyUserByName( String proxyUserName,long proxyUserId) {
		logger.info("proxyUserName:"+proxyUserName+".proxyUserId:"+proxyUserId);
		
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.ge("roleid", "6");//代理商角色（系统预设角色）
		if(StringUtils.isNotEmpty(proxyUserName)){
			wrapper.like("proxy_user_name",proxyUserName);//代理商名称
		}
		List<ProxyUser> list = proxyUserMapper.selectList(wrapper);
		if(list.size() > 0){
			ProxyUser proxyUser = list.get(0);
		
			logger.info("proxyUserId:"+proxyUserId+".proxyUser:"+JSON.toJSONString(proxyUser));
			if(proxyUserId == 0){
				return proxyUser;
			} else if( proxyUser.getId() != proxyUserId){//排查指定ID
				return proxyUser;
			}
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addProxyUser(String proxyUserName, String proxyUserFullName, String proxyUserPhone, String province,
			String city, String county, String idCardNo, long proxyProductId, int proxyState) throws IOException{
		ProxyUser checkPhoneUser = getProxyUserByPhone(proxyUserPhone,0);
		if(checkPhoneUser != null){
			throw new RuntimeException("手机号已经被使用");
		}
		ProxyUser checkNameUser = getProxyUserByName(proxyUserName,0);
		if(checkNameUser != null){
			throw new RuntimeException("代理商名称已经被使用");
		}
		
		
		ProxyUser addUser = new ProxyUser();
		addUser.setRoleid("6");//代理商角色（系统预设角色）
		long time = System.currentTimeMillis();
		addUser.setName(proxyUserName);//名称
		addUser.setDeptid(26);//部门ID
		addUser.setProxyUserName(proxyUserName);//代理商名称
		String randomSalt = getRandomSalt(5);
		ByteSource salt = new Md5Hash(randomSalt);
		String passwordMD5 = new SimpleHash(hashAlgorithmName,DEFAULTPASSWORD,salt,hashIterations).toString(); 
		addUser.setPassword(passwordMD5);//设置默认密码
		addUser.setSalt(randomSalt);//设置盐值
		addUser.setStatus(1);//启用
		addUser.setProxyUserFullName(proxyUserFullName);//代理商姓名
		addUser.setProxyUserPhone(proxyUserPhone);//代理商手机号
		addUser.setPhone(proxyUserPhone);//代理商手机号
		String provinceCityCounty = province+ city+county;
		addUser.setProvinceCityCounty(provinceCityCounty);//所在省份城市县区
		addUser.setProvince(province);//所在省份
		addUser.setCity(city);//所在城市
		addUser.setCounty(county);//所在县区
		addUser.setIdCardNo(idCardNo);//身份证号码
		addUser.setProxyProductId(proxyProductId);//代理产品ID
		addUser.setStatus(proxyState);//状态(1：启用 2：冻结 3：删除）
		addUser.setCreateTime(time);//创建时间
		addUser.setProxyUserNum("");//代理商编号
		proxyUserMapper.insert(addUser);
		long proxyUserId = addUser.getId();
		String proxyUserNum = generateBusinessNumber(String.valueOf(proxyUserId)) ;
		ProxyUser proxyUser = new ProxyUser();
		proxyUser.setId(proxyUserId);
		proxyUser.setAccount(proxyUserNum);//登录账户
		proxyUser.setProxyUserNum(proxyUserNum);//代理商编号
		//生成二维码
		StringBuffer stringBuffer = new StringBuffer();
		String url = Constants.QRCODE_PROXY_URL;
		stringBuffer.append(url).append("?proxyUserId=").append(proxyUserId);
		BufferedImage bufferedImage = QRCodeUtil.getQR(stringBuffer.toString(), 1181, 1181);
        
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
        boolean flag = ImageIO.write(bufferedImage, "jpg", out);
        File file = new File("qrcodeProxyUser.jpg");
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        out.writeTo(fos);
        
		//上传到阿里云
		OSSFileUtil ossFileUtil = new OSSFileUtil();
		String qrcodeStr = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, new JiuyMultipartFile(file), "NO");
		
		//保存路径
		proxyUser.setQrcodeStr(qrcodeStr);
		proxyUserMapper.updateById(proxyUser);
		
	}
    
	//生成商家号
	private String generateBusinessNumber(String id){
		int count = DEFAULT_NUMBER_SIZE - id.length();
		StringBuffer stringBuffer = new StringBuffer();
		if(count>0){
			for(int i=0;i<count;i++){
				stringBuffer.append("0");
			}
		}
		
		return BUSINESS_NUMBER_PREFIX+stringBuffer.toString()+id;
	}
	
    //获取随机盐值
	public String getRandomSalt(int length){
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
	}
	
	public List<ProxyUser> getProxyUserList(String proxyUserNum, String proxyUserName, String proxyUserFullName,
			String proxyUserPhone, String idCardNo, int proxyState, Page<Map<String, String>> page){
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.ge("roleid", "6");//代理商角色（系统预设角色）
		if(StringUtils.isNotEmpty(proxyUserNum)){
			wrapper.like("proxy_user_num",proxyUserNum);//代理商编号
		}
		if(StringUtils.isNotEmpty(proxyUserName)){
			wrapper.like("proxy_user_name",proxyUserName);//代理商名称
		}
		if(StringUtils.isNotEmpty(proxyUserFullName)){
			wrapper.like("proxy_user_full_name",proxyUserFullName);//代理商姓名
		}
		if(StringUtils.isNotEmpty(proxyUserPhone)){
			wrapper.like("proxy_user_phone",proxyUserPhone);//代理商手机号
		}
		if(StringUtils.isNotEmpty(idCardNo)){
			wrapper.like("id_card_no",idCardNo);//身份证号码
		}
		if(proxyState != -1){
			wrapper.eq("status", proxyState);//状态
		}
		wrapper.orderBy("id", false);
		List<ProxyUser> list = proxyUserMapper.selectPage(page,wrapper);
		return list;
	}
	
	/**
	 * 获取可售总库存量(代理总库存量（个）)
	 */
	public int getProxyProductTotalCount(long proxyProductId){
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.eq("proxy_product_id", proxyProductId);
		List<ProxyUser> list = proxyUserMapper.selectList(wrapper);
		int totalStockCount = 0;//可售总库存量
		for(ProxyUser user : list){
			totalStockCount = totalStockCount + user.getStockCount();//代理产品库存量
		}
		return totalStockCount;
	}
	
	/**
	 * 接受代理用户数量
	 */
	public int getReceiveProxyUserCount(long proxyProductId){
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.eq("proxy_product_id", proxyProductId);
		return proxyUserMapper.selectCount(wrapper);
	}
    /**
     * 代理商个人信息
     */
	@Override
	public Map<String, Object> detail(long proxyUserId) {
		ProxyUser proxyUser = proxyUserMapper.selectById(proxyUserId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("proxyUserId", proxyUser.getId());//代理商Id
		map.put("proxyUserNum", proxyUser.getProxyUserNum());//代理商编号
		map.put("proxyUserName", proxyUser.getProxyUserName());//代理商名称
		map.put("proxyUserFullName", proxyUser.getProxyUserFullName());//代理商姓名
		map.put("proxyUserPhone", proxyUser.getProxyUserPhone());//代理商手机号
		map.put("provinceCityCounty", proxyUser.getProvinceCityCounty());//所在省份城市县区
		map.put("idCardNo", proxyUser.getIdCardNo());//代理商身份证
		map.put("status", proxyUser.getStatus());//状态
		long productId = proxyUser.getProxyProductId();
		ProxyProduct product =  proxyProductMapper.selectById(productId);
		map.put("proxyProductName", product.getName());//代理商商品名称
		//二维码
		map.put("qrcodeStr", proxyUser.getQrcodeStr());//二维码
		return map;
	}
	/**
	 * 首页
	 */
	@Override
	public Map<String, Object> home(long proxyUserId) {
		Map<String,Object> map = new HashMap<String,Object>();
		long currentTime = System.currentTimeMillis();
		//我的库存
		ProxyUser proxyUser = proxyUserMapper.selectById(proxyUserId);
		int stockCount = proxyUser.getStockCount();
		//我的账户总货值
		long proxyProductId = proxyUser.getProxyProductId();
		ProxyProduct proxyProduct = proxyProductMapper.selectById(proxyProductId);
		Double price = proxyProduct.getPrice();
		double totalStockValue = DoubleUtil.mul(stockCount, price);
		//我的客户
		//获取小程序续约保护期中的客户数
		int protectPeriodCustomerCount = proxyCustomerService.getProtectPeriodCustomerCount(proxyUserId, currentTime);
		//获取小程序使用中的客户数
		int usingPeriodCustomerCount = proxyCustomerService.getUsingPeriodCustomerCount(proxyUserId, currentTime);
		//我的订单
		//获取受理中的订单数
		int dealingOrderCount = proxyOrderService.getDealingOrderCount(proxyUserId);
		//获取已完成的订单数
		int finishedOrderCount = proxyOrderService.getfinishedOrderCount(proxyUserId);
		//最新动态
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<ProxyOrder> latestSituationList = proxyOrderService.selectLatestSituation(proxyUserId);
		for(ProxyOrder proxyOrder : latestSituationList){
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("applyFullName", proxyOrder.getApplyFullName());//申请人姓名
			map1.put("applyPhone", proxyOrder.getApplyPhone());//申请人手机号
			map1.put("status", proxyOrder.buildOrderStateName());//申请人状态
			map1.put("updateTime", DateUtil.parseLongTime2Str(proxyOrder.getUpdateTime()));//订单更新时间
			map1.put("proxyProductName", proxyOrder.getProxyProductName());//服务产品名称
			long productId = proxyOrder.getProxyProductId();
			ProxyProduct product = proxyProductMapper.selectById(productId);
			int proxyProductCount = proxyOrder.getProxyProductCount();//订单个数
			int singleUseLimitDay = product.getSingleUseLimitDay();//单位使用限制天数
			int totalUseDay = proxyProductCount*singleUseLimitDay;
			map1.put("totalUseDay", totalUseDay);//使用期限
			resultList.add(map1);
			
		}
		
		map.put("stockCount", stockCount);//我的库存
		map.put("totalStockValue", totalStockValue);//账户总货值
		map.put("proxyProductName", proxyProduct.getName());//产品名称
		map.put("protectPeriodCustomerCount", protectPeriodCustomerCount);//小程序续约保护期中客户数
		map.put("usingPeriodCustomerCount", usingPeriodCustomerCount);//小程序使用中的客户数
		map.put("dealingOrderCount", dealingOrderCount);//受理中订单数
		map.put("finishedOrderCount", finishedOrderCount);//已完成订单数
		map.put("latestSituationList", resultList);//最新动态列表
		
		return map;
	}
	@Override
	public Map<String, Object> getProxyUserStatistics(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState) {
		Map<String,Object> map = new HashMap<String,Object>();
		//代理商总数
		int proxyUserCount = getProxyUserCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
		//客户总数
		int proxyCustomerCount = getProxyCustomerCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
		//销售总量
		int saleTotalCount = getSaleTotalCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
		//库存总量
		int stockTotalCount = getStockTotalCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
		
		map.put("proxyUserCount", proxyUserCount);//代理商总数
		map.put("proxyCustomerCount", proxyCustomerCount);//客户总数
		map.put("saleTotalCount", saleTotalCount);//销售总量
		map.put("stockTotalCount", stockTotalCount);//库存总量
		
		return map;
	}
	/**
	 * 获取总库存数
	 * @param proxyState 
	 * @param idCardNo 
	 * @param proxyUserPhone 
	 * @param proxyUserFullName 
	 * @param proxyUserName 
	 * @param proxyUserNum 
	 */
	private int getStockTotalCount(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState) {
		return proxyUserMapper.getStockTotalCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
	}
	/**
	 * 获取总销售额
	 * @param proxyState 
	 * @param idCardNo 
	 * @param proxyUserPhone 
	 * @param proxyUserFullName 
	 * @param proxyUserName 
	 * @param proxyUserNum 
	 */
	private int getSaleTotalCount(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState) {
		return proxyOrderMapper.getSaleTotalCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
	}
	/**
	 * 获取总客户数
	 * @param proxyState 
	 * @param idCardNo 
	 * @param proxyUserPhone 
	 * @param proxyUserFullName 
	 * @param proxyUserName 
	 * @param proxyUserNum 
	 */
	private int getProxyCustomerCount(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState) {
		return proxyCustomerMapper.getProxyCustomerCount(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
	}
	/**
	 * 获取总代理商数
	 * @param proxyState 
	 * @param idCardNo 
	 * @param proxyUserPhone 
	 * @param proxyUserFullName 
	 * @param proxyUserName 
	 * @param proxyUserNum 
	 */
	private int getProxyUserCount(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState) {
		Wrapper<ProxyUser> wrapper = new EntityWrapper<ProxyUser>();
		wrapper.ge("roleid", "6");//代理商角色（系统预设角色）
		if(StringUtils.isNotEmpty(proxyUserNum)){
			wrapper.like("proxy_user_num",proxyUserNum);//代理商编号
		}
		if(StringUtils.isNotEmpty(proxyUserName)){
			wrapper.like("proxy_user_name",proxyUserName);//代理商名称
		}
		if(StringUtils.isNotEmpty(proxyUserFullName)){
			wrapper.like("proxy_user_full_name",proxyUserFullName);//代理商姓名
		}
		if(StringUtils.isNotEmpty(proxyUserPhone)){
			wrapper.like("proxy_user_phone",proxyUserPhone);//代理商手机号
		}
		if(StringUtils.isNotEmpty(idCardNo)){
			wrapper.like("id_card_no",idCardNo);//身份证号码
		}
		if(proxyState != -1){
			wrapper.eq("status", proxyState);//状态
		}
		return proxyUserMapper.selectCount(wrapper);
	}
}