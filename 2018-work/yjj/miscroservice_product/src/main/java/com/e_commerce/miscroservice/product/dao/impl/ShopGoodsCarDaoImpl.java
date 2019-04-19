package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ShopGoodsCarDao;
import com.e_commerce.miscroservice.product.entity.ShopGoodsCar;
import com.thoughtworks.xstream.converters.ErrorReporter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 18:04
 * @Copyright 玖远网络
 */
@Component
public class ShopGoodsCarDaoImpl implements ShopGoodsCarDao{


    /**
     * 批量更新购物车sku状态
     *
     * @param carIds 购物车id
     * @param status 修改的状态
     * @return int
     * @author Charlie
     * @date 2018/11/27 18:08
     */
    @Override
    public void batchSetStatusByIds(List<Long> carIds, int status) {
        ShopGoodsCar upd = new ShopGoodsCar ();
        upd.setCarSukStatus (status);
        upd.setLastUpdateTime (System.currentTimeMillis ());
        int rec = MybatisOperaterUtil.getInstance ().update (
                upd,
                new MybatisSqlWhereBuild (ShopGoodsCar.class)
                        .in (ShopGoodsCar::getId, carIds)
        );
        ErrorHelper.declare (rec == carIds.size (), "更新购物车状态失败");
    }
}
