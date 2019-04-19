package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberOrderMapper;
import com.e_commerce.miscroservice.operate.dao.ShopMemberOrderDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberOrderDaoImpl implements ShopMemberOrderDao{

    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;


    /**
     * 小程序订单查询
     *
     * @param query query
     * @author Charlie
     * @date 2018/11/8 19:12
     */
    @Override
    public PageInfo<Map<String, Object>> listOrderPage(ShopMemberOrderQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        //sql查询
        List<Map<String, Object>> orderList = shopMemberOrderMapper.listOrder (query);

        //日期格式化
        if (! ObjectUtils.isEmpty (orderList)) {
            for (Map<String, Object> order : orderList) {
                Long payTime = Long.parseLong (order.get ("payTime").toString ());
                order.put ("payTime", TimeUtils.longFormatString (payTime));

                Long updateTime = Long.parseLong (order.get ("updateTime").toString ());
                order.put ("updateTime", TimeUtils.longFormatString (updateTime));

                Long createTime = Long.parseLong (order.get ("createTime").toString ());
                order.put ("createTime", TimeUtils.longFormatString (createTime));
            }
        }
        return new PageInfo<> (orderList);
    }
}
