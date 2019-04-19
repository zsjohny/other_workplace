package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.HomeMenu;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.query.PageQuery;

public interface IHomeMenuService {
	/**
	 *  修改首页菜单
	 */
	public void updHomeMenu(long homeMenuId,String menuName,int menuType,long targetId,int weight) ;
	/**
	 *  添加首页菜单
	 */
	public void addHomeMenu(String menuName,int menuType,long targetId,int weight) ;
	/**
	 *  删除首页菜单
	 */
	public void delHomeMenu(long homeMenuId) ;
	/**
	 *  获取首页菜单列表
	 */
	public List<HomeMenu> getHomeMenuList(Page<HomeMenu> page);
	public List<HomeMenu> getHomeMenuList();
	public HomeMenu getHomeMenu(long homeMenuId);
	public List<HomeMenu> getHomeMenuListByMenuName(String menuName);
}