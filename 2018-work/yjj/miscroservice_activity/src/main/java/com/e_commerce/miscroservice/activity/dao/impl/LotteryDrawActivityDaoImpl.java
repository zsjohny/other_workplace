package com.e_commerce.miscroservice.activity.dao.impl;


import com.e_commerce.miscroservice.activity.PO.LotteryDrawActivityPo;
import com.e_commerce.miscroservice.activity.dao.LotteryDrawActivityDao;
import com.e_commerce.miscroservice.activity.entity.PicturesCollection;
import com.e_commerce.miscroservice.activity.mapper.LotteryDrawActivityMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by hyf on 2018/10/8
 */
@Repository
public class LotteryDrawActivityDaoImpl implements LotteryDrawActivityDao {

    @Resource
    private LotteryDrawActivityMapper lotteryDrawActivityMapper;
    /**
     * 查询所有 活动
     * @return
     * @param code
     */

    @Override
    public List<LotteryDrawActivityPo> findAllProductList(Integer code)
    {
        List<LotteryDrawActivityPo> list = lotteryDrawActivityMapper.findAllProductList(code);
        return list;
    }

    /**
     * 根据活动id 类型查找内容图片
     *
     * @param id
     * @param code
     * @return
     */
    @Override
    public List<PicturesCollection> findPictureCollectionByIdType(Long id, Integer code) {
        List<PicturesCollection> li = lotteryDrawActivityMapper.findPictureCollectionByIdType(id,code);
        return li;
    }
}
