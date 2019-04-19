
package com.jiuy.supplier.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.supplier.common.constant.factory.SupplierConstantFactory;

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
        map.put("sexName", SupplierConstantFactory.me().getSexName((Integer) map.get("sex")));
        map.put("roleName", SupplierConstantFactory.me().getRoleName((String) map.get("roleid")));
        map.put("deptName", SupplierConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("statusName", SupplierConstantFactory.me().getStatusName((Integer) map.get("status")));
    }

}
