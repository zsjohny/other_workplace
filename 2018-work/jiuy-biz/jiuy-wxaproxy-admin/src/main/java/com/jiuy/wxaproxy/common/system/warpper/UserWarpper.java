
package com.jiuy.wxaproxy.common.system.warpper;

import java.util.List;
import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;

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
        map.put("sexName", WxaproxyConstantFactory.me().getSexName((Integer) map.get("sex")));
        map.put("roleName", WxaproxyConstantFactory.me().getRoleName((String) map.get("roleid")));
        map.put("deptName", WxaproxyConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("statusName", WxaproxyConstantFactory.me().getStatusName((Integer) map.get("status")));
    }

}
