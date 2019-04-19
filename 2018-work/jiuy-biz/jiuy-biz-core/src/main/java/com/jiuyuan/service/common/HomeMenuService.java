/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.JsonArray;
import com.jiuyuan.dao.mapper.supplier.HomeMenuMapper;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.HomeMenu;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.xiaoleilu.hutool.json.JSONArray;

/**
 * app首页菜单服务
 */
@Service
public class HomeMenuService implements IHomeMenuService  {
	private static final Logger logger = LoggerFactory.getLogger(HomeMenuService.class);
	@Autowired
	private HomeMenuMapper homeMenuMapper;
	
	/**
	 *  修改首页菜单
	 */
	public void updHomeMenu(long homeMenuId,String menuName,int menuType,long targetId,int weight) {
		HomeMenu homeMenu = new HomeMenu();
		homeMenu.setId(homeMenuId);
		homeMenu.setMenuName(menuName);
		homeMenu.setMenuType(menuType);
		homeMenu.setTargetId(targetId);
		homeMenu.setWeight(weight);
		homeMenu.setUpdateTime(System.currentTimeMillis());
		homeMenuMapper.updateById(homeMenu);
	}
	
	/**
	 *  添加首页菜单
	 */
	public void addHomeMenu(String menuName,int menuType,long targetId,int weight) {
		
		
		
		long time = System.currentTimeMillis() ;
		HomeMenu homeMenu = new HomeMenu();
		homeMenu.setMenuName(menuName);
		homeMenu.setMenuType(menuType);
		homeMenu.setTargetId(targetId);
		homeMenu.setWeight(weight);
		homeMenu.setCreateTime(time);
		homeMenu.setUpdateTime(time);
		homeMenuMapper.insert(homeMenu);
	}
	
	/**
	 *  删除首页菜单
	 */
	public void delHomeMenu(long homeMenuId) {
		homeMenuMapper.deleteById(homeMenuId);
	}
	public HomeMenu getHomeMenu(long homeMenuId){
		return homeMenuMapper.selectById(homeMenuId);
	}
	/**
	 *  获取首页菜单列表
	 */
	public List<HomeMenu> getHomeMenuList(Page<HomeMenu> page) {
		Wrapper<HomeMenu> wrapper = new EntityWrapper<HomeMenu>();
		wrapper.orderBy("weight",false);
		List<HomeMenu> homeMenuList = homeMenuMapper.selectPage(page,wrapper);
		return homeMenuList;
	}
	/**
	 *  获取首页菜单列表
	 */
	public List<HomeMenu> getHomeMenuList() {
		Wrapper<HomeMenu> wrapper = new EntityWrapper<HomeMenu>();
		wrapper.orderBy("weight",false);
		System.out.println("===============测试缓存效果===========");
		List<HomeMenu> homeMenuList = homeMenuMapper.selectList(wrapper);
		return homeMenuList;
	}
	
	public List<HomeMenu> getHomeMenuListByMenuName(String menuName){
		Wrapper<HomeMenu> wrapper = new EntityWrapper<HomeMenu>();
		wrapper.eq("menu_name", menuName);
		List<HomeMenu> homeMenuList = homeMenuMapper.selectList(wrapper);
		return homeMenuList;
	}
	
}