package com.jiuy.supplier.common.system.warpper;

import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.admin.core.util.ToolUtil;
import com.jiuy.supplier.common.constant.factory.SupplierConstantFactory;

/**
 * 部门列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {

        Integer pid = (Integer) map.get("pid");

        if (ToolUtil.isEmpty(pid) || pid.equals(0)) {
            map.put("pName", "--");
        } else {
            map.put("pName", SupplierConstantFactory.me().getDeptName(pid));
        }
    }

}
