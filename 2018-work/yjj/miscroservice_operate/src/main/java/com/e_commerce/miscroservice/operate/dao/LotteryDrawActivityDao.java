package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityUser;
import com.e_commerce.miscroservice.commons.entity.activity.LotteryDrawActivity;
import com.e_commerce.miscroservice.commons.entity.activity.PicturesCollection;
import com.e_commerce.miscroservice.operate.entity.response.LotteryDrawPo;

import java.util.List;

/**
 * Create by hyf on 2018/12/18
 */
public interface LotteryDrawActivityDao {
    List<LotteryDrawPo> findAllProductList(Long id, String timeStart, String timeEnd, Integer pageNumber, Integer pageSize);

    /**
     * 添加商品
     *
     * @return
     */
    void insertProduct(LotteryDrawActivity lotteryDrawActivity);

    /**
     * 修改产品
     *
     * @param id
     * @return
     */
    void updateProduct(Long id, LotteryDrawActivity lotteryDrawActivity);

    /**
     * 添加图片集合
     *
     * @param id
     * @param pic
     * @param code
     * @param value
     * @param sort
     */
    void insertPicture(Long id, String pic, Integer code, String value, Integer sort);

    /**
     * 删除原有
     *
     * @param id
     */
    void delPicture(Long id);

    /**
     * 查询所有详情内容图片
     * @param id
     * @return
     */
    List<PicturesCollection> findAllDetailPic(Long id);

    /**
     * 查询产品详情
     * @param id
     * @return
     */
    LotteryDrawPo findProductById(Long id);

    /**
     * 查询参加用户
     * @param phone
     * @param pageNumber
     * @param pageSize
     * @param code
     * @return
     */
    List<ActivityUser> findJoin(String phone, Integer pageNumber, Integer pageSize, Integer code);

    /**
     * 根据活动类型查询
     * @param code
     * @return
     */
    LotteryDrawActivity findProductByType(Integer code);
}
