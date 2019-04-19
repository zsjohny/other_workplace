package com.admin.core.util;

import java.util.ArrayList;
import java.util.List;

import com.admin.common.constant.SysConst;
import com.admin.core.config.GunsProperties;
import com.admin.core.node.MenuNode;

/**
 * api接口文档显示过滤
 *
 * @author fengshuonan
 * @date 2017-08-17 16:55
 */
public class ApiMenuFilter extends MenuNode {

	public static List<MenuNode> build(List<MenuNode> nodes) {

		// 如果关闭了接口文档,则不显示接口文档菜单
		GunsProperties gunsProperties = SpringContextHolder.getBean(GunsProperties.class);
		if (!gunsProperties.getSwaggerOpen()) {
			List<MenuNode> menuNodesCopy = new ArrayList<>();
			for (MenuNode menuNode : nodes) {
				if (SysConst.API_MENU_NAME.equals(menuNode.getName())) {
					continue;
				} else {
					menuNodesCopy.add(menuNode);
				}
			}
			nodes = menuNodesCopy;
		}

		return nodes;
	}
}
