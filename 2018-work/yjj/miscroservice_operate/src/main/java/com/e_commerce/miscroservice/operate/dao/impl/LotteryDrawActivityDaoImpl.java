package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityUser;
import com.e_commerce.miscroservice.commons.entity.activity.LotteryDrawActivity;
import com.e_commerce.miscroservice.commons.entity.activity.PicturesCollection;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.LotteryDrawActivityDao;
import com.e_commerce.miscroservice.operate.entity.response.LotteryDrawPo;
import com.e_commerce.miscroservice.operate.mapper.LotteryDrawActivityMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by hyf on 2018/12/18
 */
@Repository
public class LotteryDrawActivityDaoImpl implements LotteryDrawActivityDao {
    @Resource
    private LotteryDrawActivityMapper lotteryDrawActivityMapper;
    @Override
    public List<LotteryDrawPo> findAllProductList(Long id, String timeStart, String timeEnd, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<LotteryDrawPo> list = lotteryDrawActivityMapper.findAllProductList(id,timeStart,timeEnd);
        return list;
    }

    /**
     * 添加商品
     * @return
     */
    @Override
    public void insertProduct(LotteryDrawActivity lotteryDrawActivity) {

        MybatisOperaterUtil.getInstance().save(lotteryDrawActivity);
    }

    /**
     * 修改产品
     *
     * @param id
     * @return
     */
    @Override
    public void updateProduct(Long id, LotteryDrawActivity lotteryDrawActivity) {
        MybatisOperaterUtil.getInstance().update(lotteryDrawActivity,new MybatisSqlWhereBuild(LotteryDrawActivity.class).eq(LotteryDrawActivity::getId,id));
    }
    /**
     * 添加图片集合
     * @param id
     * @param pic
     * @param code
     * @param value
     * @param sort
     */
    @Override
    public void insertPicture(Long id, String pic, Integer code, String value, Integer sort) {
        PicturesCollection picturesCollection = new PicturesCollection();
        picturesCollection.setNeedPicId(id);
        picturesCollection.setTypeCode(code);
        picturesCollection.setTypeValue(value);
        picturesCollection.setSort(sort);
        picturesCollection.setPicture(pic);
        MybatisOperaterUtil.getInstance().save(picturesCollection);
    }

    /**
     * 删除原有
     * @param id
     */
    @Override
    public void delPicture(Long id) {
        lotteryDrawActivityMapper.delPicture(id);
    }

    @Override
    public List<PicturesCollection> findAllDetailPic(Long id) {

        List<PicturesCollection> list = MybatisOperaterUtil.getInstance().finAll(new PicturesCollection(), new MybatisSqlWhereBuild(PicturesCollection.class).eq(PicturesCollection::getNeedPicId,id));
        return list;
    }

    /**
     * 查询产品详情
     * @param id
     * @return
     */
    @Override
    public LotteryDrawPo findProductById(Long id) {
        return lotteryDrawActivityMapper.findProductById(id);
    }

    @Override
    public List<ActivityUser> findJoin(String phone, Integer pageNumber, Integer pageSize, Integer code) {
        PageHelper.startPage(pageNumber,pageSize);
        List<ActivityUser> list = lotteryDrawActivityMapper.findJoin(phone,code);
        return list;
    }

    /**
     * 根据活动类型查询
     * @param code
     * @return
     */
    @Override
    public LotteryDrawActivity findProductByType(Integer code) {

        return lotteryDrawActivityMapper.findProductByType(code);
    }


}
