package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.entity.response.EyeDataResponse;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.EyeInfoService;
import com.finace.miscroservice.borrow.utils.EyeResponse;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Rc4Utils;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EyeInfoServiceImpl implements EyeInfoService {

    private Log logger = Log.getInstance(EyeInfoServiceImpl.class);

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private FinanceBidDao financeBidDao;

    @Autowired
    private UserRpcService userRpcService;


    @Override
    public EyeResponse getEyeInfo(String start_time, String end_time, String mobile, String order_id) {

        Map<String, Object> map = new HashMap<>();
        if (!"".equals(mobile) && null != mobile) {
            User user = userRpcService.getUserByPhone(mobile);
            if (null != user) {
                map.put("userId", String.valueOf(user.getUser_id()));
            } else {
                return EyeResponse.error(EyeResponse.CODE_4005);
            }
        }

        if (!"".equals(order_id) && null != order_id) {
            String toStr = "";
            for (String str : order_id.split(",")) {
                toStr += Rc4Utils.toString(str, Constant.EYEKEY) != "" ? "'" + Rc4Utils.toString(str, Constant.EYEKEY) + "'" + "," : "";
            }
            if (toStr.length() > 0) {
                toStr = toStr.substring(0, toStr.length() - 1);
                map.put("orders", toStr);
            }
        }

        if (!"".equals(start_time) && null != start_time) {
            map.put("startTime", start_time);
        }

        if (!"".equals(end_time) && null != end_time) {
            map.put("endTime", end_time);
        }

        //时间前后判断
        if (null != start_time && null != end_time && !"".equals(start_time) && !"".equals(end_time) && DateUtils.compareDate(start_time, end_time)) {

            return EyeResponse.error(EyeResponse.CODE_4008);
        }

        logger.info("天眼查询数据,条件{}", map.toString());

        List<EyeDataResponse> list = financeBidDao.getEyeInfoByData(map);
        if (null == list || list.size() <= 0) {
            return EyeResponse.error(EyeResponse.CODE_4006);
        }

        logger.info("天眼查询数据条数{}", list.size());

        for (EyeDataResponse eyeDataResponse : list) {
            eyeDataResponse.setOrder_id(Rc4Utils.toHexString(eyeDataResponse.getOrder_id(), Constant.EYEKEY));
            eyeDataResponse.setBid_id(Rc4Utils.toHexString(String.valueOf(eyeDataResponse.getBid_id()), Constant.EYEKEY));
            eyeDataResponse.setBid_name(Rc4Utils.toHexString(String.valueOf(eyeDataResponse.getBid_name()), Constant.EYEKEY));

            String reward_type = "2";
            String reward = "0";
            if (eyeDataResponse.getCouponAmt() != null) {
                reward_type = "2";
                reward = eyeDataResponse.getCouponAmt();
            } else if (eyeDataResponse.getCouponRate() != null) {
                reward_type = "1";
                reward = eyeDataResponse.getCouponRate();
            }
            eyeDataResponse.setReward(reward);
            eyeDataResponse.setReward_type(reward_type);
            eyeDataResponse.setCost("0");
            eyeDataResponse.setIs_bill("1");
            eyeDataResponse.setCouponAmt(null);
            eyeDataResponse.setCouponRate(null);
            eyeDataResponse.setUserId(null);
        }

        return EyeResponse.success(list);
    }

}
