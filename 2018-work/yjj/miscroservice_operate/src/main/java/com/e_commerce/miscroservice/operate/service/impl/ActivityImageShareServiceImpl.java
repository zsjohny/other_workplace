package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.activity.ActivityImageShare;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.ActivityImageShareDao;
import com.e_commerce.miscroservice.operate.entity.request.ActivityImageShareAddRequest;
import com.e_commerce.miscroservice.operate.service.activity.ActivityImageShareService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author hyf
 * @Date 2019/1/8 16:32
 */
@Service
public class ActivityImageShareServiceImpl implements ActivityImageShareService {
    private Log logger = Log.getInstance(ActivityImageShareServiceImpl.class);

    @Resource
    private ActivityImageShareDao activityImageShareDao;



    @Override
    public Response findAllActivityImageShare(Integer type, Integer shareType, Long timeStar, Long timeEnd, Integer pageNumber, Integer pageSize) {
        logger.info("查询分享图片type={},shareType={},timeStar={},timeEnd={},pageNumber={},pageSize={}", type,shareType,timeStar,timeEnd,pageNumber,pageSize);
        List<ActivityImageShare> list =  activityImageShareDao.findAllActivityImageShare( type,shareType,timeStar,timeEnd,pageNumber,pageSize);
        SimplePage<ActivityImageShare> shareSimplePage = new SimplePage<>(list);
        return Response.success(shareSimplePage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response shareImageChange(ActivityImageShareAddRequest activityImageShareAddRequest) {
        logger.info("添加或者修改 分享图片活动activityImageShareAddRequest={}",activityImageShareAddRequest);
        Map map = AnnotationUtils.validate(activityImageShareAddRequest);
        if (map.get("result")==Boolean.FALSE){
            logger.warn("错误信息={}",map.get("message"));
            return Response.error(map.get("message"));
        }
        ActivityImageShare activityImageShare = new ActivityImageShare();
        activityImageShare.setMainMap(activityImageShareAddRequest.getMainMap());
        activityImageShare.setType(activityImageShareAddRequest.getType());
        activityImageShare.setShareType(activityImageShareAddRequest.getShareType());
        activityImageShare.setWxImg(activityImageShareAddRequest.getWxImg());

        if (activityImageShareAddRequest!=null&&activityImageShareAddRequest.getId()==null){
            return shareImageInsert(activityImageShare);
        }else {
            activityImageShare.setId(activityImageShareAddRequest.getId());
            return shareImageUpd(activityImageShare);
        }
    }

    /**
     * 添加
      * @param request
     * @return
     */
    public Response shareImageInsert(ActivityImageShare request){

        Integer in = activityImageShareDao.addShareImage(request);
        if (in<=0){
            logger.warn("添加失败");
            return Response.errorMsg("添加失败");
        }
        return Response.success();
    }

    /**
     * 更新
     * @param request
     * @return
     */
    public Response shareImageUpd(ActivityImageShare request){
        ActivityImageShare activityImageShare =  activityImageShareDao.findById(request.getId());
        if (activityImageShare==null){
            return Response.errorMsg("活动不存在");
        }

        Integer in = activityImageShareDao.shareImageUpd(request);
        if (in<=0){
            logger.warn("更新失败");
            return Response.errorMsg("更新失败");
        }
        return Response.success();
    }
}
