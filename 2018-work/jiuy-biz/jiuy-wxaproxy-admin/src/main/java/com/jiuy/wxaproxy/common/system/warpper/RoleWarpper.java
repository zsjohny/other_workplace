package com.jiuy.wxaproxy.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;

/**
 * 角色列表的包装类
 *
 * @author fengshuonan
 * @date 2017年2月19日10:59:02
 */
public class RoleWarpper extends BaseControllerWarpper {

    public RoleWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("pName", WxaproxyConstantFactory.me().getSingleRoleName((Integer) map.get("pid")));
        map.put("deptName", WxaproxyConstantFactory.me().getDeptName((Integer) map.get("deptid")));
    }

}
