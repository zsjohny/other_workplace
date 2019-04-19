package com.jiuy.wxaproxy.core.shiro.factory;

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
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;
import com.jiuy.wxaproxy.common.system.dao.MenuDao;
import com.jiuy.wxaproxy.common.system.persistence.dao.UserMapper;
import com.jiuy.wxaproxy.common.system.persistence.model.User;
import com.jiuy.wxaproxy.core.shiro.ShiroUser;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {
    
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
	private MenuDao menuDao;

	public static IShiro me() {
		return SpringContextHolder.getBean(IShiro.class);
	}

	@Override
	public User user(String account) {
		Wrapper<User> wrapper = new EntityWrapper<User>();
		wrapper.eq("account", account)
		       .or("phone={0}",account);
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
		shiroUser.setDeptName(WxaproxyConstantFactory.me().getDeptName(user.getDeptid()));// 部门名称
		shiroUser.setName(user.getName()); // 用户名称
		shiroUser.setStatus(user.getStatus()==1?"可用":"禁用");//账户状态1正常，2 禁用
		shiroUser.setPhoneNumber(user.getPhone());//手机号码

		Integer[] roleArray = Convert.toIntArray(user.getRoleid());// 角色集合
		List<Integer> roleList = new ArrayList<Integer>();
		List<String> roleNameList = new ArrayList<String>();
		for (int roleId : roleArray) {
			roleList.add(roleId);
			roleNameList.add(WxaproxyConstantFactory.me().getSingleRoleName(roleId));
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
		return WxaproxyConstantFactory.me().getSingleRoleTip(roleId);
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
