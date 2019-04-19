package com.jiuy.supplier.common.system.warpper;

import java.util.Map;

import com.admin.core.base.warpper.BaseControllerWarpper;
import com.jiuy.supplier.common.constant.factory.SupplierConstantFactory;

/**
 * 部门列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class NoticeWrapper extends BaseControllerWarpper {

    public NoticeWrapper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        Integer creater = (Integer) map.get("creater");
        map.put("createrName", SupplierConstantFactory.me().getUserNameById(creater));
    }

}
