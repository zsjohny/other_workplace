package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaDataQuery;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.mapper.StoreWxaMapper;
import com.e_commerce.miscroservice.operate.service.user.StoreWxaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 15:40
 * @Copyright 玖远网络
 */
@Service
public class StoreWxaServiceImpl implements StoreWxaService{


    @Autowired
    private StoreWxaMapper storeWxaMapper;


    /**
     * 店铺资料列表
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/17 15:42
     * @param query
     */
    @Override
    public Map<String, Object> listAll(StoreWxaDataQuery query) {
        long current = System.currentTimeMillis ();
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<Map<String, Object>> allWxas = storeWxaMapper.listAll (query);
        if (! allWxas.isEmpty ()) {
            for (Map<String, Object> map : allWxas) {
                Long wxaOpenTimeStamp = (Long) map.get ("wxaOpenTime");
                map.put ("wxaOpenTimeStamp", wxaOpenTimeStamp);
                String wxaOpenTime = TimeUtils.longFormatString (wxaOpenTimeStamp);
                map.put ("wxaOpenTime", wxaOpenTime);

                //店铺有效期
                Long wxaCloseTimeLong = (Long) map.get ("wxaCloseTime");
                String wxaCloseTime;
                if (current < wxaCloseTimeLong) {
                    wxaCloseTime = TimeUtils.longFormatString (wxaCloseTimeLong);
                }
                else {
                    wxaCloseTime = "店铺过期";
                }
                map.put ("wxaCloseTime", wxaCloseTime);

                //会员有效期
                Long memberEndTimeLong = (Long) map.get ("memberEndTime");
                String memberEndTime;
                Integer isMember;
                if (memberEndTimeLong < 0L){
                    //没有购买购会员
                    memberEndTime = "--";
                    isMember = 0;
                }
                else if (current < memberEndTimeLong) {
                    memberEndTime = TimeUtils.longFormatString (memberEndTimeLong);
                    isMember = 1;
                }
                else {
                    memberEndTime = "会员过期";
                    isMember = 1;
                }
                map.put ("memberEndTime", memberEndTime);
                map.put ("isMember", isMember);
            }

        }

        Map<String, Object> res = new HashMap<> (2);
        res.put ("dataList", new SimplePage<> (allWxas));
        return res;
    }
}
