package com.wuai.company.user.service.impl;

import com.wuai.company.entity.Response.GaoDeResponse;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.SnatchUser;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.TrystReceive;
import com.wuai.company.entity.User;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.user.service.GeTuiService;
import com.wuai.company.user.util.NoticeUtil;
import com.wuai.company.util.LocationUtils;
import org.apache.commons.lang3.StringUtils;
import com.wuai.company.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("geTuiServiceImpl")
@Transactional
public class GeTuiServiceImpl implements GeTuiService {

    private final String NEARBY_TOTAL_CITY="nearby:total:%s";
    private final String NEARBY_TOTAL_CITY_DISTRICT="nearby:total:%s:%s";
    private final String SNATCH_USER_TRYST = "snatch:user:%s"; //tryst 订单号
    private Logger logger = LoggerFactory.getLogger(GeTuiServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private NoticeUtil noticeUtil ;

    @Resource
    private HashOperations<String,String,SnatchUser> snatchUserTemplate;

    @Resource
    private HashOperations<String, String, NearBodyResponse> totalNearBodyTemplate;


    /*
    创建订单时，个推给附件用户
     */
    @Override
    public Response createSendGetuiAuth(Integer id, String trystId) {
        if(id == null || StringUtils.isEmpty(trystId)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = userDao.findTrystOrdersByUid(trystId);
        if (trystOrders == null){
            return Response.error("订单为空");
        }
        //此订单位置
        GaoDeResponse gaoDeResponse =  LocationUtils.GaoDeAddress(trystOrders.getLongitude(),trystOrders.getLatitude());
        String city = gaoDeResponse.getRegeocode().getAddressComponent().getCity();
        String district = gaoDeResponse.getRegeocode().getAddressComponent().getDistrict();
        if ("[]".equals(city)){
            logger.info("直辖市 添加");
            city = gaoDeResponse.getRegeocode().getAddressComponent().getProvince();
        }
        List<NearBodyResponse> nearbyBodyList = totalNearBodyTemplate.values(String.format(NEARBY_TOTAL_CITY_DISTRICT,city,district));
        if(nearbyBodyList.size() < 10){
            nearbyBodyList=totalNearBodyTemplate.values( String.format(NEARBY_TOTAL_CITY,city));
        }
        if (userDao.upTrystOrdersPay(trystId, PayTypeEnum.PARTY_IN_ADVANCE.toCode(),PayTypeEnum.PARTY_IN_ADVANCE.getValue()) == 0){
            return Response.error("更新订单失败");
        }
        noticeUtil.createSendGetuiAuth(nearbyBodyList,trystOrders);
        return Response.success(nearbyBodyList.size());
    }


    /*
    需求方取消订单时，个推给已抢单用户
     */
    @Override
    public Response cancelCreateGetuiAuth(Integer id, String trystId) {
        if(id == null || StringUtils.isEmpty(trystId)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<SnatchUser> snatchUserList = snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,trystId));
        noticeUtil.cancelCreateGetuiAuth(snatchUserList);
        return Response.success("已将取消通知发送给赴约方");
    }


    /*
    需求方付款后，通知用户等待约会开始
     */
    @Override
    public Response waitTrystGetuiAuth(Integer id, String trystId) {
        if(id == null || StringUtils.isEmpty(trystId)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<TrystReceive> trystReceiveList = userDao.selectReceiveByTrystId(trystId);
        noticeUtil.waitTrystGetuiAuth(trystReceiveList);
        return Response.success("已成功通知抢单用户，订单已生效");
    }

    /*
    赴约方取消订单，通知发单方
     */
    @Override
    public Response cancelUserGetuiAuth(Integer id, String trystId) {
        if(id == null || StringUtils.isEmpty(trystId)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = userDao.findTrystOrdersByUid(trystId);
        noticeUtil.cancelTrystUser(userDao.selectCidById(trystOrders.getUserId()),userDao.selectTypeById(trystOrders.getUserId()));
        return Response.success("已成功通知发单方，一位赴约方取消订单");
    }


}
