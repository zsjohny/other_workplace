
package com.jiuy.operator.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.operator.common.constant.factory.OperatorConstantFactory;

/**
 * 用户管理的包装类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:47:03
 */
public class UserWarpper extends BaseControllerWarpper {

    public UserWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("sexName", OperatorConstantFactory.me().getSexName((Integer) map.get("sex")));
        map.put("roleName", OperatorConstantFactory.me().getRoleName((String) map.get("roleid")));
        map.put("deptName", OperatorConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("statusName", OperatorConstantFactory.me().getStatusName((Integer) map.get("status")));
    }

}
