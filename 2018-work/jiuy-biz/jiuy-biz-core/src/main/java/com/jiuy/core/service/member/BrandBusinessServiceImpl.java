package com.jiuy.core.service.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.dao.BrandBusinessDao;
import com.jiuy.core.dao.MemberDao;
import com.jiuy.core.meta.member.BrandBusinessSO;
import com.jiuy.core.meta.member.BrandBusinessSearch;
import com.jiuyuan.constant.UserStatusLogType;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerGroupMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.ISupplierCustomerGroupService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.UserNewService;
import com.jiuyuan.service.common.WithdrawApplyNewService;
import com.jiuyuan.service.common.YunXinSmsService;


/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午5:16:25
*/
@Service("brandBusinessService")
public class BrandBusinessServiceImpl implements BrandBusinessService{
	private static Logger logger = Logger.getLogger(BrandBusinessServiceImpl.class);
	
	private static final String BUSINESS_NUMBER_PREFIX = "900";
	
	private static final String DEFAULT_PASSWORD = "81234567";
	
	private static final int IS_ORIGINAL_PASSWORD = 1;
	private static final int NOT_ORIGINAL_PASSWORD = 0;
	
	private static final String SUPPLIER_ROLE ="6";
	
	private static final int OK = 1;
	private static final int FREEZED = 2;
	
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;
    
	
	private static final int DEFAULT_NUMBER_SIZE = 4;
	
	@Resource
	private BrandBusinessDao brandBusinessDao;
	
	@Resource
	private MemberDao memberDaoSqlImpl;
	
	@Resource
	private YunXinSmsService yunXinSmsService;
	
	@Resource
	private StoreBusinessService storeBusinessService;
	
	@Autowired
	private WithdrawApplyNewService withdrawApplyNewService;
	
	@Autowired
	private UserNewMapper UserNewMapper;
	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	
	@Autowired
	private StoreMapper storeMapper;
	@Autowired
	private ISupplierCustomerGroupService supplierCustomerGroupService;

	@Override
	public List<Map<String,Object>> search(BrandBusinessSearch brandBusinessSearch, PageQuery pageQuery) {
		List<Map<String,Object>> list = brandBusinessDao.search(brandBusinessSearch, pageQuery);
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		for(Map map:list){
			Map<String,Object> newMap = new HashMap<String,Object>();
			newMap.put("id", map.get("id"));
			newMap.put("businessName", map.get("business_name"));
			newMap.put("businessNumber", map.get("business_number"));//门店关联注册手机号
			newMap.put("phoneNumber", map.get("PhoneNumber")==null?"":map.get("PhoneNumber"));
			newMap.put("status", map.get("status"));
			newMap.put("minWithdrawal", map.get("min_withdrawal"));
			newMap.put("bond", map.get("bond"));
			if(map.get("phone")==null||((String)map.get("phone")).equals("")){
				newMap.put("phone", "");//供应商手机号
			}else{
				newMap.put("phone", map.get("phone"));//供应商手机号
			}
			newMap.put("availableBalance", map.get("available_balance"));
			newMap.put("createTime", map.get("add_createtime"));
			newMap.put("cashIncome", map.get("cash_income"));
			newMap.put("account", map.get("account"));
			newMap.put("alipayFlag", map.get("alipay_flag"));
			newMap.put("alipayName", map.get("alipay_name"));
			newMap.put("bankAccountName", map.get("bank_account_name"));
			newMap.put("bankAccountNo", map.get("bank_account_no"));
			newMap.put("bankCardFlag", map.get("bank_card_flag"));
			newMap.put("bankName", map.get("bank_name"));
			newMap.put("brandId", map.get("brand_id"));
			newMap.put("taxid", map.get("taxid"));
			newMap.put("lowarehouseId", map.get("lowarehouse_id"));
			newMap.put("legalPerson", map.get("legal_person"));
			newMap.put("licenseNumber", map.get("license_number"));
			newMap.put("companyName", map.get("company_name"));
			newMap.put("province", map.get("province"));
			newMap.put("city", map.get("city"));
			newMap.put("businessAddress", map.get("business_address"));
			newMap.put("idCardNumber", map.get("id_card_number"));
			newMap.put("alipayAccount", map.get("alipay_account"));
			newList.add(newMap);
		}
		return newList;
	}

	@Override
	public int searchCount(BrandBusinessSearch brandBusinessSearch) {
		return brandBusinessDao.searchCount(brandBusinessSearch);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(UserNew brandBusiness,String password,String storeBusinessPhone) {
		//手机号唯一性
		String phoneNumber = brandBusiness.getPhone();
		if(phoneNumber.length()!=11&&!phoneNumber.equals("")){
			throw new RuntimeException("手机号码错误！");
		}
		BrandBusinessSearch brandBusinessSearch = new BrandBusinessSearch();
		brandBusinessSearch.setPhoneNumber(phoneNumber);
		List<Map<String,Object>> phoneList =null; 
		try {
			if(!phoneNumber.equals("")){
				phoneList = brandBusinessDao.searchPhoneNumber(phoneNumber);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if(phoneList!=null&&phoneList.size()>0){
			throw new RuntimeException("手机号码已经注册过！");
		}
		//添加信息
		brandBusiness.setAvatar(brandBusiness.getBrandLogo());
		long currentTimeMillis = System.currentTimeMillis();
		if(phoneNumber.equals("")){
			brandBusiness.setIsOriginalpassword(NOT_ORIGINAL_PASSWORD);
		}
		brandBusiness.setCreatetime(new Date());
		brandBusiness.setUpdatetime(currentTimeMillis);
		brandBusiness.setAddCreatetime(currentTimeMillis);
		int h = UserNewMapper.insert(brandBusiness);
		if(h == -1){
			logger.error("供应商家号创建失败！");
			throw new RuntimeException("供应商家号创建失败！");
		}
		//生成商家号和默认密码
		long id = brandBusiness.getId();
		String businessNumber = generateBusinessNumber(String.valueOf(id));
		if(password.equals("")){
			password = DEFAULT_PASSWORD ;
		}
		//保存密码和盐值
		String randomSalt = getRandomSalt(5);
		ByteSource salt = new Md5Hash(randomSalt);
		String passwordMD5 = new SimpleHash(hashAlgorithmName,password,salt,hashIterations).toString(); 
		String roleId = SUPPLIER_ROLE;
		brandBusinessDao.updateBusinessNumberAndUserName(businessNumber,businessNumber, businessNumber, passwordMD5, id, randomSalt, roleId);
	    
		//校验手机号
		//门店信息绑定
		if(!storeBusinessPhone.equals("")){
			StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessByPhone(storeBusinessPhone);
			if(null == storeBusiness){
				logger.info("在创建供应商时，关联的门店手机号不存在！无法关联该门店手机号！");
				throw new RuntimeException("关联的门店手机号不存在！");
			}
			long supplierId = storeBusiness.getSupplierId();
			if(supplierId != 0L){
				logger.info("在创建供应商时，关联的门店手机号已经被关联！无法关联该门店手机号！");
				throw new RuntimeException("关联的门店手机号已经被关联！");
			}
			storeBusinessNewService.updateSupplierIdById(storeBusiness.getId(), id);
		}
		
		//添加客户默认分组 v3.6
		SupplierCustomerGroup customerGroup = new SupplierCustomerGroup();
		customerGroup.setCreateTime(System.currentTimeMillis());
		customerGroup.setUpdateTime(System.currentTimeMillis());
		customerGroup.setDefaultGroup(1);
		customerGroup.setGroupName("系统默认");
		customerGroup.setSupplierId(id);
		supplierCustomerGroupService.insert(customerGroup);
		
		
//		int i = storeBusinessService.updateSupplierIdByPhone(phoneNumber,id);
//		if(i==1){
//			logger.info("当前供应商已经有门店账号，并完成绑定！");
//		}
//		if(i>1){
//			logger.info("门店账号有多个相同手机号码，无法建立绑定！");
//			throw new RuntimeException("门店账号有多个相同手机号码，无法建立绑定！");
//		}
		//发送初始密码短信通知
		/*sendCode(brandBusiness.getPhoneNumber(),password);*/
		
		return true;
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
	

	//生成6位随机密码
	private  String generatePassword(){
		Random random = new Random();
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String password = "";
		for(int i=0;i<6;i++){
			int j = random.nextInt(base.length());
			password += base.charAt(j);
		}
		return password;
	}
	
	private void sendCode(String phone,String password){
		JSONArray params = new JSONArray();
		params.add(password);//初始密码
		params.add("4001180099");//400电话
    	yunXinSmsService.send(phone, params, 6110);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateBrandBusiness(UserNew brandBusiness,String password,String storeBusinessPhone) {
		String phoneNumber = brandBusiness.getPhone();
		long brandBusinessId = brandBusiness.getId();
		UserNew userNew = UserNewMapper.selectById(brandBusinessId);
		//检验手机号码是否合法
		if(phoneNumber.length()!=11&&!phoneNumber.equals("")){
			throw new RuntimeException("手机号码格式错误！");
		}
		List<Map<String,Object>> phoneList = null;
		//检验手机号码是否注册过
		if(!phoneNumber.equals("")){
			phoneList = brandBusinessDao.searchPhoneNumber(phoneNumber);
			for(Map map :phoneList){
				if(brandBusinessId !=(int)map.get("id")){
					throw new RuntimeException("手机号已经注册过！");
				}
			}
		}
		//设置密码是否必须修改
		if(phoneNumber.equals("")){
			brandBusiness.setIsOriginalpassword(NOT_ORIGINAL_PASSWORD);
		}else {
			String phone = userNew.getPhone();
			if(!phone.equals(phoneNumber)){
				brandBusiness.setIsOriginalpassword(IS_ORIGINAL_PASSWORD);
			}
		}
		//设置供应商密码
		if(password.equals("")&&!phoneNumber.equals(userNew.getPhone())){
			//如果无密码且手机号码有更改就设置为默认密码
			password=DEFAULT_PASSWORD;
		}
		if(!password.equals("")||!phoneNumber.equals(userNew.getPhone())){
			//如果有密码就设置为该密码
			String randomSalt = getRandomSalt(5);
			ByteSource salt = new Md5Hash(randomSalt);
			String passwordMD5 = new SimpleHash(hashAlgorithmName,password,salt,hashIterations).toString(); 
			brandBusiness.setSalt(randomSalt);
			brandBusiness.setPassword(passwordMD5);
		}
		
		long currentTime = System.currentTimeMillis();
		brandBusiness.setUpdatetime(currentTime);
		if(brandBusiness.getBrandLogo()==null){
			brandBusiness.setBrandLogo("");
		}
		long id = brandBusiness.getId();
		int oldStatus = brandBusinessDao.getStatusOfId(id);
		if(brandBusiness.getStatus() != oldStatus){
			memberDaoSqlImpl.addUserStatusLog(id, UserStatusLogType.BRANDSTATUS.getIntValue(), oldStatus, brandBusiness.getStatus(), currentTime);
		}
		int h = UserNewMapper.updateById(brandBusiness);
		if(brandBusiness.getStatus() == FREEZED){
			//禁用供应商，冻结供应商的未提现订单
			List<WithdrawApplyNew> list = withdrawApplyNewService.getNoDealWithdrawApplyInfoById(id);
			for(WithdrawApplyNew withdrawApplyNew : list){
				//更新提现状态为冻结
				withdrawApplyNewService.updateStatusById(WithdrawApplyNew.FREEZED,withdrawApplyNew.getId());
			}
			
		}
		if(brandBusiness.getStatus() == OK){
			//启动供应商，恢复供应商的已冻结提现订单
			List<WithdrawApplyNew> list = withdrawApplyNewService.getFreezedWithdrawApplyInfoById(id);
			for(WithdrawApplyNew withdrawApplyNew : list){
				withdrawApplyNewService.updateStatusById(WithdrawApplyNew.NO_DEAL,withdrawApplyNew.getId());
			}
		}
		
		//校验手机号
		//门店信息绑定
		StoreBusiness storeBusiness = new StoreBusiness();
		if(!storeBusinessPhone.equals("")){
			storeBusiness = storeBusinessNewService.getStoreBusinessByPhone(storeBusinessPhone);
			if(null == storeBusiness){
				logger.info("在创建供应商时，关联的门店手机号不存在！无法关联该门店手机号！");
				throw new RuntimeException("关联的门店手机号不存在！");
			}
			//获取有这个供应商ID的列表
			List<StoreBusiness> list = storeBusinessNewService.getStoreBusinessBySupplierId(id);
			//如果有列表里有两个门店有这个供应商ID，说明重复
			if(list.size()>1){
				logger.info("在编辑供应商时，关联的门店手机号已经被关联！无法关联该门店手机号！");
				throw new RuntimeException("关联的门店手机号已经被关联！");
			}
			//如果该门店绑定的供应商ID不为0，且不与当前供应商ID一致
			if(storeBusiness.getSupplierId() != 0L && id != storeBusiness.getSupplierId()){
				logger.info("在编辑供应商时，关联的门店手机号已经被关联！无法关联该门店手机号！");
				throw new RuntimeException("关联的门店手机号已经被关联！");
			}
			//如果该门店绑定的供应商ID为0，就要判断当前供应商ID是否被绑定过
			if(storeBusiness.getSupplierId() == 0L ){
				for(StoreBusiness storeBusiness2:list){
					storeBusinessNewService.updateSupplierIdById(storeBusiness2.getId(), 0L);
				}
				storeBusinessNewService.updateSupplierIdById(storeBusiness.getId(), id);
				
			}
		}else{
			//查询该供应商的ID在门店信息表中,清空该供应商
			List<StoreBusiness> list = storeBusinessNewService.getStoreBusinessBySupplierId(id);
			for(StoreBusiness storeBusiness2:list){
				StoreBusiness store = new StoreBusiness();
				store.setId(storeBusiness2.getId());
				store.setSupplierId(0L);
				storeMapper.updateById(store);
			}
		}
		return h;
	}

	@Override
	public List<Province> getProvinceList() {
		return brandBusinessDao.getProvinceList();
	}

	@Override
	public List<City> getCitysByProvinceId(long parentId) {
		return brandBusinessDao.getCitysByProvinceId(parentId);
	}

	@Override
	public List<BrandLogo> getBrandList() {
		return brandBusinessDao.getBrandList();
	}
    
	@Override
	public List<BrandLogo> getBrandListWithClothNumberPrefix() {
		return brandBusinessDao.getBrandListWithClothNumberPrefix();
	}
	
	@Override
	public BrandBusiness get(BrandBusinessSO brandBusinessSO) {
		return brandBusinessDao.get(brandBusinessSO);
	}

	@Override
	public BrandBusiness getById(long id) {
		return brandBusinessDao.getById(id);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean sendInitPassword(String phone, long id) {
		if(phone == null&&phone.length()!=11){
			logger.error("手机号码有误");
			return false;
		}
		//获取随机的6位密码
		String randonPassword = generatePassword();
		//存入密码并保存状态,并且更改了isOriginalPassword状态,用于登录时，让用户修改密码
		String randomSalt = getRandomSalt(5);
		ByteSource salt = new Md5Hash(randomSalt);
		String passwordMD5 = new SimpleHash(hashAlgorithmName,randonPassword,salt,hashIterations).toString();
		int isOriginalPassword = IS_ORIGINAL_PASSWORD; 
		brandBusinessDao.resetPassword(id,randomSalt,passwordMD5,isOriginalPassword);
		//发送短信
		JSONArray params = new JSONArray();
		params.add(randonPassword);//初始密码
    	yunXinSmsService.sendNotice(phone, params, 3120104);
    	
		return true;
	}



}
