package com.e_commerce.miscroservice.activity.service.impl;

import com.e_commerce.miscroservice.activity.PO.LotteryDrawActivityPo;
import com.e_commerce.miscroservice.activity.dao.ActivityDao;
import com.e_commerce.miscroservice.activity.dao.LotteryDrawActivityDao;
import com.e_commerce.miscroservice.activity.entity.ActivityUser;
import com.e_commerce.miscroservice.commons.enums.activity.ActivityEnums;
import com.e_commerce.miscroservice.activity.entity.PicturesCollection;
import com.e_commerce.miscroservice.activity.service.LotteryDrawActivityService;
import com.e_commerce.miscroservice.commons.enums.activity.PictureTypeEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
@Service
public class LotteryDrawActivityServiceImpl implements LotteryDrawActivityService {
    private Log logger = Log.getInstance(LotteryDrawActivityServiceImpl.class);

    @Resource
    private LotteryDrawActivityDao lotteryDrawActivityDao;

    @Resource
    private ActivityDao activityDao;


    /**
     * 参加活动
     * @param phone
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response joinActivity(String phone) {
        logger.info("phone ={}参加活动",phone);
        ActivityUser activityUser = activityDao.findActivityUser(phone, ActivityEnums.LOTTERY_DRAW.getCode());
        if (activityUser!=null){
            logger.warn("该用户已参加");
            return Response.errorMsg("该用户已参加");
        }
        ActivityUser user = new ActivityUser();
        user.setApplyTime(System.currentTimeMillis());
        user.setPhone(phone);
        user.setStatus(ActivityEnums.LOTTERY_DRAW.getCode());
        user.setStatusName(ActivityEnums.LOTTERY_DRAW.getValue());
        activityDao.addActivityUser(user);
        return Response.success();
    }

    @Override
    public Response findAllProductList(Integer code) {
        logger.info("查询所有活动内容code={}",code);
        ActivityEnums[] activityEnums = ActivityEnums.values();
        Integer save = -1;
        for (ActivityEnums a:activityEnums){
            if (code.equals(a.getCode())){
                save=code;
                break;
            }
        }
        if (save==-1){
            logger.warn("活动不存在");
            return Response.errorMsg("活动不存在");
        }
        List<LotteryDrawActivityPo> lotteryDrawActivity = lotteryDrawActivityDao.findAllProductList(code);
        lotteryDrawActivity.forEach(
                lotteryDrawActivityPo -> {
                    List<PicturesCollection> list = lotteryDrawActivityDao.findPictureCollectionByIdType(lotteryDrawActivityPo.getId(), PictureTypeEnums.LOTTERY_DRAW_ACTIVITY_PIC.getCode());
                    lotteryDrawActivityPo.setList(list);
                }
        );
        return Response.success(lotteryDrawActivity);
    }


}
