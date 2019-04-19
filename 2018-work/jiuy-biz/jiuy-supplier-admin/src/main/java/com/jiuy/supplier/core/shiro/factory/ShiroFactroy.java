package com.jiuy.supplier.core.shiro.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.common.constant.state.ManagerStatus;
import com.admin.core.util.Convert;
import com.admin.core.util.SpringContextHolder;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.supplier.common.constant.factory.SupplierConstantFactory;
import com.jiuy.supplier.common.system.dao.MenuDao;
import com.jiuy.supplier.common.system.dao.UserMgrDao;
import com.jiuy.supplier.common.system.persistence.dao.BaseBrandMapper;
import com.jiuy.supplier.common.system.persistence.dao.UserMapper;
import com.jiuy.supplier.common.system.persistence.model.BaseBrand;
import com.jiuy.supplier.common.system.persistence.model.User;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.service.common.IUserNewService;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {
    
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
	private UserMgrDao userMgrDao;
	
    @Autowired
    private BaseBrandMapper brandMapper;
    
    @Autowired
	private IUserNewService supplierUserService;

	@Autowired
	private MenuDao menuDao;

	public static IShiro me() {
		return SpringContextHolder.getBean(IShiro.class);
	}

	@Override
	public User user(String account) {
		Wrapper<User> wrapper = new EntityWrapper<User>();
		if(account.equals("")){
			throw new CredentialsException();
		}
		wrapper.eq("account", account).or().eq("phone", account);
		List<User> list = userMapper.selectList(wrapper);
		if(list == null||list.size() == 0){
			throw new CredentialsException();
		}
        User user = list.get(0);
		// 账号不存在
		if (null == user) {
			throw new CredentialsException();
		}
		// 账号被冻结
		if (user.getStatus() != ManagerStatus.OK.getCode()) {
			throw new LockedAccountException();
		}
		return user;
	}

	@Override
	public ShiroUser shiroUser(User user) {
		ShiroUser shiroUser = new ShiroUser();
        
		shiroUser.setId(user.getId()); // 账号id
		shiroUser.setAccount(user.getAccount());// 账号
		shiroUser.setDeptId(user.getDeptid()); // 部门id
		shiroUser.setDeptName(SupplierConstantFactory.me().getDeptName(user.getDeptid()));// 部门名称
		shiroUser.setName(user.getName()); // 用户名称
		shiroUser.setBusinessName(user.getBusinessName());//商家名称
		shiroUser.setStatus(user.getStatus()==1?"可用":"禁用");//账户状态1正常，2 禁用
		shiroUser.setCompanyName(user.getCompanyName());//公司名称
		shiroUser.setBusinessAddress(user.getBusinessAddress());//商家地址
		shiroUser.setLicenseNumber(user.getLicenseNumber());//营业执照号或者统一社会信用代码
		shiroUser.setTaxId(user.getTaxid());//税务登记号
		shiroUser.setLegalPerson(user.getLegalPerson());//法定代表人
		shiroUser.setIdCardNumber(user.getIdCardNumber());//身份证号
		shiroUser.setProvince(user.getProvince());//所在省份
		shiroUser.setCity(user.getCity());//所在城市
		shiroUser.setBankCardFlag(user.getBankCardFlag());//银行卡选择标记 0 未选中， 1选中
		shiroUser.setBankAccountName(user.getBankAccountName());//银行开户名称
		shiroUser.setBankName(user.getBankName());//开户银行名称
		shiroUser.setBankAccountNo(user.getBankAccountNo());//银行账户号
		shiroUser.setAlipayFlag(user.getAlipayFlag());//支付宝选择标记 0 未选中， 1选中
		shiroUser.setAlipayAccount(user.getAlipayAccount());//支付宝账号
		shiroUser.setAlipayName(user.getAlipayName());//支付宝关联人姓名
		shiroUser.setPhoneNumber(user.getPhone());//手机号码
		shiroUser.setAvailableBalance(user.getAvailableBalance().doubleValue());//可提现金额
		shiroUser.setMinWithdrawal(user.getMinWithdrawal().doubleValue());//最低提现额
		long brandId = user.getBrandId();
		shiroUser.setBrandId(brandId);//品牌ID
		long lowarehouseId = user.getLowarehouseId();
		shiroUser.setLowarehouseId(lowarehouseId);//仓库ID
		shiroUser.setLowarehouseName(supplierUserService.getSupplierLowarehouse(lowarehouseId).getName());//仓库名称
		
		
		
		Wrapper<BaseBrand> wrapper = new EntityWrapper<BaseBrand>().eq("BrandId", brandId);
		List<BaseBrand> list = brandMapper.selectList(wrapper);
		if(list.size()>0){
			shiroUser.setBrandName(list.get(0).getBrandName());//品牌名称
			shiroUser.setClothNumberPrefix(list.get(0).getClothNumberPrefix());//品牌款号前缀
		}
		

		Integer[] roleArray = Convert.toIntArray(user.getRoleid());// 角色集合
		List<Integer> roleList = new ArrayList<Integer>();
		List<String> roleNameList = new ArrayList<String>();
		for (int roleId : roleArray) {
			roleList.add(roleId);
			roleNameList.add(SupplierConstantFactory.me().getSingleRoleName(roleId));
		}
		shiroUser.setRoleList(roleList);
		shiroUser.setRoleNames(roleNameList);

		return shiroUser;
	}

	@Override
	public List<String> findPermissionsByRoleId(Integer roleId) {
		List<String> resUrls = menuDao.getResUrlsByRoleId(roleId);
		return resUrls;
	}

	@Override
	public String findRoleNameByRoleId(Integer roleId) {
		return SupplierConstantFactory.me().getSingleRoleTip(roleId);
	}

	@Override
	public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
		String credentials = user.getPassword();
		// 密码加盐处理
		String source = user.getSalt();
		ByteSource credentialsSalt = new Md5Hash(source);
		return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
	}

}
