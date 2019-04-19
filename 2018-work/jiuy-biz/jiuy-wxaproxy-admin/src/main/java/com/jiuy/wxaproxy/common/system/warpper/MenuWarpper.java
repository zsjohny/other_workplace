package com.jiuy.wxaproxy.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.common.constant.state.IsMenu;
import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;

/**
 * 菜单列表的包装类
 *
 * @author fengshuonan
 * @date 2017年2月19日15:07:29
 */
public class MenuWarpper extends BaseControllerWarpper {

    public MenuWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("statusName", WxaproxyConstantFactory.me().getMenuStatusName((Integer) map.get("status")));
        map.put("isMenuName", IsMenu.valueOf((Integer) map.get("ismenu")));
    }

}
