package com.jiuy.operator.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.operator.common.constant.factory.OperatorConstantFactory;

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
        map.put("pName", OperatorConstantFactory.me().getSingleRoleName((Integer) map.get("pid")));
        map.put("deptName", OperatorConstantFactory.me().getDeptName((Integer) map.get("deptid")));
    }

}
