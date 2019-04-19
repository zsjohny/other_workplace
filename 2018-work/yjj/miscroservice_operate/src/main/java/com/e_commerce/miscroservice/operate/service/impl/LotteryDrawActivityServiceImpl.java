package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.activity.ActivityUser;
import com.e_commerce.miscroservice.commons.entity.activity.LotteryDrawActivity;
import com.e_commerce.miscroservice.commons.entity.activity.PicturesCollection;
import com.e_commerce.miscroservice.commons.enums.activity.ActivityEnums;
import com.e_commerce.miscroservice.commons.enums.activity.PictureTypeEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.LotteryDrawActivityDao;
import com.e_commerce.miscroservice.operate.entity.response.LotteryDrawPo;
import com.e_commerce.miscroservice.operate.service.activity.LotteryDrawActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by hyf on 2018/12/18
 */
@Service
public class LotteryDrawActivityServiceImpl implements LotteryDrawActivityService {

    private Log logger = Log.getInstance(LotteryDrawActivityServiceImpl.class);
    @Resource
    private LotteryDrawActivityDao lotteryDrawActivityDao;

    @Override
    public Response findAllProductList(Long id, String timeStart, String timeEnd, Integer pageNumber, Integer pageSize) {
        logger.info("查询产品列表,id={},timeStart={},timeEnd={},pageNumber={},pageSize={}",id,timeStart,timeEnd,pageNumber,pageSize);
        List<LotteryDrawPo> list = lotteryDrawActivityDao.findAllProductList( id,timeStart, timeEnd, pageNumber, pageSize);
        list.forEach(lotteryDrawPo -> {
           List<PicturesCollection> li = lotteryDrawActivityDao.findAllDetailPic(lotteryDrawPo.getId());
           if (li!=null&&li.size()>0){
               if (li.get(0)!=null){
                   lotteryDrawPo.setPic(li.get(0).getPicture());
               }
           }
        });
        SimplePage<LotteryDrawPo> simplePage = new SimplePage<>(list);
        return Response.success(simplePage);
    }
    /**
     * 添加商品
     * @param banner
     * @param pics
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response insertProduct(String banner, String[] pics, String button) {
        logger.info("添加商品banner={},pics={},button={}", banner,pics,button);
        if (StringUtils.isAnyEmpty(banner,button)||pics==null||pics.length==0){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        Integer code = ActivityEnums.LOTTERY_DRAW.getCode();
        LotteryDrawActivity lo = lotteryDrawActivityDao.findProductByType(code);
        if (lo!=null){
            logger.warn("该类型活动已存在",code);
            return Response.errorMsg("该类型活动已存在");
        }
        LotteryDrawActivity lotteryDrawActivity = new LotteryDrawActivity();
        lotteryDrawActivity.setBanner(banner);
        lotteryDrawActivity.setButtonPic(button);
//        默认设置
        lotteryDrawActivity.setType(ActivityEnums.LOTTERY_DRAW.getCode());
        lotteryDrawActivity.setTypeValue(ActivityEnums.LOTTERY_DRAW.getValue());
        lotteryDrawActivityDao.insertProduct(lotteryDrawActivity);

        for (int i=0;i<pics.length;i++){
            String pic = pics[i];
            lotteryDrawActivityDao.insertPicture(lotteryDrawActivity.getId(),pic, PictureTypeEnums.LOTTERY_DRAW_ACTIVITY_PIC.getCode(),PictureTypeEnums.LOTTERY_DRAW_ACTIVITY_PIC.getValue(),i);
        }
        return Response.success();
    }
    /**
     * 修改产品
     * @param id
     * @param banner
     * @param pics
     * @param button
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response updateProduct(Long id, String banner, String[] pics, String button) {
        if (StringUtils.isAnyEmpty(banner,button)||id==null||pics==null||pics.length==0){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }

        Integer code = ActivityEnums.LOTTERY_DRAW.getCode();
        LotteryDrawActivity lo = lotteryDrawActivityDao.findProductByType(code);
        if (lo!=null&&!lo.getId().equals(id)){
            logger.warn("该类型活动已存在",code);
            return Response.errorMsg("该类型活动已存在");
        }
        LotteryDrawActivity lotteryDrawActivity = new LotteryDrawActivity();
        lotteryDrawActivity.setBanner(banner);
        lotteryDrawActivity.setButtonPic(button);
//        默认设置
        lotteryDrawActivity.setType(ActivityEnums.LOTTERY_DRAW.getCode());
        lotteryDrawActivity.setTypeValue(ActivityEnums.LOTTERY_DRAW.getValue());
        lotteryDrawActivityDao.updateProduct(id,lotteryDrawActivity);

        lotteryDrawActivityDao.delPicture(id);
        for (int i=0;i<pics.length;i++){
            String pic = pics[i];
            lotteryDrawActivityDao.insertPicture(id,pic, PictureTypeEnums.LOTTERY_DRAW_ACTIVITY_PIC.getCode(),PictureTypeEnums.LOTTERY_DRAW_ACTIVITY_PIC.getValue(),i);
        }
        return Response.success();
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response deleteProduct(Long id) {
        if (id==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        LotteryDrawActivity lotteryDrawActivity = new LotteryDrawActivity();
        lotteryDrawActivity.setDelStatus(1);
        lotteryDrawActivityDao.updateProduct(id,lotteryDrawActivity);

        lotteryDrawActivityDao.delPicture(id);
        return Response.success();
    }

    /**
     * 查询所有详情内容图片
     * @param id
     * @return
     */
    @Override
    public Response findAllDetail(Long id) {
        LotteryDrawPo lotteryDrawPo =  lotteryDrawActivityDao.findProductById(id);
        List<PicturesCollection> list = lotteryDrawActivityDao.findAllDetailPic(id);
        if (list!=null&&list.size()>0){
            lotteryDrawPo.setList(list);
        }
        return Response.success(lotteryDrawPo);
    }

    @Override
    public Response findJoin(String phone, Integer pageNumber, Integer pageSize) {

        logger.info("查询参加用户 pageNumber={}，pageSize={}", pageNumber,pageSize);
        List<ActivityUser> list = lotteryDrawActivityDao.findJoin(phone,pageNumber,pageSize,ActivityEnums.LOTTERY_DRAW.getCode());
        SimplePage<ActivityUser> simplePage = new SimplePage<>(list);
        return Response.success(simplePage);
    }

}
