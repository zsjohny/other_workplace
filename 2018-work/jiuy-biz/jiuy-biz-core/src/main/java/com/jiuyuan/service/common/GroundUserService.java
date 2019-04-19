package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.A;
import com.jiuyuan.constant.GroundUserType;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.util.IdsToStringUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class GroundUserService implements IGroundUserService {
	private static final Log logger = LogFactory.get("GroundUserService");
	
	private static final String DEFAULT_PASSWORD = "123456";
	
	private static final int FIVE_BIT = 5;
	
	private static final int ALL_USER_TYPE = -1;
	
	
    @Autowired
	private GroundUserMapper groundUserMapper;
    
    
	

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IGroundUserService#list(com.baomidou.mybatisplus.plugins.Page, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Double, java.lang.Double)
	 */
	@Override
	public List<Map<String,Object>> listPage(Page<Map<String,Object>> page, String name,long id ,String phone, int userType, String province, String city, String district, String pname,
			String pphone, Integer minClientCount, Integer maxClientCount, Double minTotalSales, Double maxTotalSales, String managerId) {
		Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>();
		//查询id
		if(id != 0){
			wrapper.eq("id", id);
		}
		//查询名字
		if(name != null &&!name.isEmpty()){
			wrapper.like("name", name);
		}
		//查询电话
		if(phone != null &&!phone.isEmpty()){
			wrapper.eq("phone", phone);
		}
		//职位
		if(userType != ALL_USER_TYPE){
			wrapper.eq("user_type", userType);
		}
		//索检所在地区
		if(province != null && !province.isEmpty()){
			wrapper.eq("province", province);
		}
		if(city != null && !city.isEmpty()){
			wrapper.eq("city", city);
		}
		if(district != null && !district.isEmpty()){
			wrapper.eq("district", district);
		}
		//索检上级姓名
		if(pname != null && !pname.isEmpty()){
			wrapper.like("pname", pname);
		}
		//索检上级电话
		if(pphone != null && !pphone.isEmpty()){
			wrapper.eq("pphone", pphone);
		}
		//索检客户数
		if(minClientCount != null ){
			wrapper.and(" individual_client_count + team_client_count >= "+minClientCount);
		}
		if(maxClientCount != null){
			wrapper.and(" individual_client_count + team_client_count <= "+maxClientCount);
		}
		//销售额
		if(minTotalSales != null){
			wrapper.and(" individual_total_sale_amount + team_total_sale_amount >= "+minTotalSales);
		}
		if(maxTotalSales != null){
			wrapper.and(" individual_total_sale_amount + team_total_sale_amount <= "+maxTotalSales);
		}
		//查询该管理员下的所有团队成员
		if(!managerId.isEmpty()){
			wrapper.like("super_ids", ","+managerId+",");
		}
		wrapper.orderBy("create_time", false);
		List<Map<String,Object>> list = groundUserMapper.selectMapsPage(page, wrapper);
		for(Map<String,Object> map :list){
			map.remove("password");
			map.remove("salt");
			map.remove("createTime");
			BigDecimal iTotalOrderAmount = (BigDecimal)map.get("individualTotalSaleAmount");
			BigDecimal tTotalOrderAmount = (BigDecimal)map.get("teamTotalSaleAmount");
			BigDecimal totalAmount = iTotalOrderAmount.add(tTotalOrderAmount);
			map.put("TotalOrderAmount", totalAmount.doubleValue());
			int individualClientCount = (Integer)map.get("individualClientCount");
			int teamClientCount = (Integer)map.get("teamClientCount");
			int TotalClientCount = individualClientCount + teamClientCount;
			map.put("TotalClientCount", TotalClientCount);
			
		}
		return list;
	}

    /* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IGroundUserService#add(int, java.lang.String, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
	@Transactional(rollbackFor = Exception.class)
	public void add(int status, String name, String phone, int userType, long pid, String pphone, String ppname, int puserType,
			String province, String city, String district, String bankAccountNo, String bankAccountName,
			String bankName) {
    	GroundUser groundUser = new GroundUser();
    	groundUser.setStatus(status);
    	groundUser.setName(name);
    	groundUser.setPhone(phone);
    	groundUser.setUserType(userType);
    	groundUser.setProvince(province);
    	groundUser.setCity(city);
    	groundUser.setDistrict(district);
    	groundUser.setBankAccountNo(bankAccountNo);
    	groundUser.setBankAccountName(bankAccountName);
    	groundUser.setBankName(bankName);
    	groundUser.setCreateTime(System.currentTimeMillis());
    	groundUser.setPid(pid);
    	groundUser.setPphone(pphone);
    	groundUser.setPname(ppname);
    	groundUser.setPuserType(puserType);
    	//设置密码
    	String password = DEFAULT_PASSWORD;
    	String randomSalt = getRandomSalt(FIVE_BIT);
    	ByteSource salt = new Md5Hash(randomSalt);
		String passwordMD5 = new SimpleHash(hashAlgorithmName,password,salt,hashIterations).toString();
		groundUser.setPassword(passwordMD5);
		groundUser.setSalt(randomSalt);
		//搜索上级,并添加ids
		StringBuffer stringBuffer = new StringBuffer("");
		if(pid != 0){
			GroundUser groundUser2 = groundUserMapper.selectById(pid);
			String superIds = groundUser2.getSuperIds();
			//进行判断是否上级是城市经理
			if(groundUser2.getUserType() == GroundUserType.CITY_MANAGER.getIntValue()){
				List<String> ids = IdsToStringUtil.getIdsToListNoKomma(superIds);
				if(ids.size()>=4){
					Iterator<String> iterator = ids.iterator();
					int count = 0;
					while (iterator.hasNext()) {
						iterator.next();
						if(count >=3){
							iterator.remove();
						}
						count++;
					}
				}
				superIds = IdsToStringUtil.ListToString(ids);
			}
			if(superIds == null||superIds.equals("")){
				stringBuffer.append(",");
			}else{
				stringBuffer.append(superIds);
			}
			stringBuffer.append(pid).append(",");
		}
		groundUser.setSuperIds(stringBuffer.toString());
		
    	int i = groundUserMapper.insert(groundUser);
    	if(i == -1){
    		logger.info("添加地推人员失败！");
    		throw new RuntimeException("添加地推人员失败！");
    	}
	}
    
    //获取随机盐值
	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IGroundUserService#getRandomSalt(int)
	 */
	@Override
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

	@Override
	public Map<String, Object> getSupervisorInfo(int userType, String phone) {
		if(phone == null||phone.equals("")){
			logger.info("请输入上级手机号码！");
			throw new RuntimeException("请输入上级手机号码！");
		}
		//判断如果是城市经理
		Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>();
		if(userType == GroundUserType.CITY_MANAGER.getIntValue()){
			wrapper.and("user_type ="+GroundUserType.CITY_MANAGER.getIntValue())
			       .or("user_type = "+GroundUserType.AREA_MANAGER.getIntValue())
			       .andNew("phone = "+phone);
		}
		if(userType == GroundUserType.REGION_MANAGER.getIntValue()){
			return null;
		}
		if(userType != GroundUserType.CITY_MANAGER.getIntValue()){
			wrapper.eq("phone", phone).eq("user_type", --userType);
		}
		List<Map<String,Object>> list = groundUserMapper.selectMaps(wrapper);
		if(list == null || list.size() ==0){
			return null;
		}
		Map<String,Object> info = list.get(0);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pname", info.get("name"));
		map.put("pid", info.get("id"));
		map.put("pUserType", info.get("userType"));
		map.put("pphone", info.get("phone"));
		return map;
	}

	public  GroundUser getGroundUserByPhone(String phone) {
		Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>().eq("phone", phone).eq("status", 0);
		List<GroundUser> list = groundUserMapper.selectList(wrapper);
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}
	
	

	@Override
	public List<Map<String, Object>> count(String name, long id, String phone, int userType, String province, String city,
			String district, String pname, String pphone, Integer minClientCount, Integer maxClientCount,
			Double minTotalSales, Double maxTotalSales, String managerId) {
		Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>();
		//查询id
		if(id != 0){
			wrapper.eq("id", id);
		}
		//查询名字
		if(name != null &&!name.isEmpty()){
			wrapper.like("name", name);
		}
		//查询电话
		if(phone != null &&!phone.isEmpty()){
			wrapper.eq("phone", phone);
		}
		//职位
		if(userType != ALL_USER_TYPE){
			wrapper.eq("user_type", userType);
		}
		//索检所在地区
		if(province != null && !province.isEmpty()){
			wrapper.eq("province", province);
		}
		if(city != null && !city.isEmpty()){
			wrapper.eq("city", city);
		}
		if(district != null && !district.isEmpty()){
			wrapper.eq("district", district);
		}
		//索检上级姓名
		if(pname != null && !pname.isEmpty()){
			wrapper.like("pname", pname);
		}
		//索检上级电话
		if(pphone != null && !pphone.isEmpty()){
			wrapper.eq("pphone", pphone);
		}
		//索检客户数
		if(minClientCount != null ){
			wrapper.and(" individual_client_count + team_client_count >= "+minClientCount);
		}
		if(maxClientCount != null){
			wrapper.and(" individual_client_count + team_client_count <= "+minClientCount);
		}
		//销售额
		if(minTotalSales != null){
			wrapper.and(" individual_total_sale_amount + team_total_sale_amount >= "+minClientCount);
		}
		if(maxTotalSales != null){
			wrapper.and(" individual_total_sale_amount + team_total_sale_amount <= "+maxClientCount);
		}
		//查询该管理员下的所有团队成员
		if(!managerId.isEmpty()){
			wrapper.like("super_ids", ","+managerId+",");
		}
		List<Map<String,Object>> list = groundUserMapper.selectMaps(wrapper);
		int regionManagerCount = 0;
		int provinceManagerCount = 0;
		int areaManagerCount = 0;
		int cityManagerCount = 0;
		for(Map<String,Object> map :list){
			int individualUserType = (Integer)map.get("userType");
			switch (individualUserType) {
			case 1:
				regionManagerCount++;
				break;
			case 2:
				provinceManagerCount++;
				break;
			case 3:
				areaManagerCount++;
				break;
			case 4:
				cityManagerCount++;
				break;
			default:
				break;
			}
			
		}
		Map<String,Object> countMap = new HashMap<String,Object>();
		List<Map<String,Object>> countList = new ArrayList<Map<String,Object>>();
		countMap.put("regionManagerCount", regionManagerCount );
		countMap.put("provinceManagerCount", provinceManagerCount);
		countMap.put("areaManagerCount", areaManagerCount);
		countMap.put("cityManagerCount", cityManagerCount);
		countList.add(countMap);
		return countList;
	}

	@Override
	public GroundUser getGroundUserByPhoneWithAllStatus(String phone) {
		Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>().eq("phone", phone);
		List<GroundUser> list = groundUserMapper.selectList(wrapper);
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}

	
	

}
