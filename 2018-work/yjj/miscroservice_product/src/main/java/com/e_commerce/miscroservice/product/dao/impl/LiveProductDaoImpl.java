package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.LiveProductDao;
import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.mapper.LiveProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.commons.enums.product.LiveProductTypeEnums.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 11:58
 * @Copyright 玖远网络
 */
@Component
public class LiveProductDaoImpl implements LiveProductDao {


    @Autowired
    private LiveProductMapper liveProductMapper;

    /**
     * 平台直播商品
     *
     * @param anchorId 主播id
     * @param productIds 平台商品
     * @return java.util.List<java.lang.Long>
     * @author Charlie
     * @date 2019/1/14 19:06
     */
    @Override
    public List<Long> listPlatformBySupplierProductIds(Long anchorId, List<Long> productIds) {
        List<LiveProduct> liveProducts = MybatisOperaterUtil.getInstance().finAll(
                new LiveProduct(),
                new MybatisSqlWhereBuild(LiveProduct.class)
                        .eq(LiveProduct::getAnchorId, anchorId)
                        .eq(LiveProduct::getType, PLATFORM.getCode())
                        .in(LiveProduct::getSupplierProductId, productIds.toArray())
                        .eq(LiveProduct::getDelStatus, StateEnum.NORMAL)
                        .eq(LiveProduct::getLiveStatus, 0)
        );
        if (liveProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return liveProducts.stream().mapToLong(p -> p.getSupplierProductId()).boxed().collect(Collectors.toList());
    }



    /**
     * 安全的批量插入
     *
     * @param liveProducts liveProducts
     * @return int
     * @author Charlie
     * @date 2019/1/14 19:24
     */
    @Override
    public int batchInsertSafe(List<LiveProduct> liveProducts) {
        int rec = 0;
        for (LiveProduct liveProduct : liveProducts) {
//            int save = liveProductMapper.insertSafe(liveProduct);
            int save = MybatisOperaterUtil.getInstance().save(liveProduct);
            if (save == 0) {
                return rec;
            }
            rec ++;
        }
        return rec;
    }

    /**
     * 查找自己直播商品
     *
     * @param anchorId anchorId
     * @param productIds productIds
     * @return java.util.List<java.lang.Long>
     * @author Charlie
     * @date 2019/1/15 13:59
     */
    @Override
    public List<Long> listSelfLiveByShopProductIds(Long anchorId, List<Long> productIds) {
        List<LiveProduct> liveProducts = MybatisOperaterUtil.getInstance().finAll(
                new LiveProduct(),
                new MybatisSqlWhereBuild(LiveProduct.class)
                        .eq(LiveProduct::getAnchorId, anchorId)
                        .in(LiveProduct::getType, SUPPLIER_PRODUCT.getCode(), SELF_CUSTOM.getCode(), AS_SAME_AS_SUPPLIER.getCode())
                        .in(LiveProduct::getShopProductId, productIds.toArray())
                        .eq(LiveProduct::getDelStatus, StateEnum.NORMAL)
                        .eq(LiveProduct::getLiveStatus, 0)
        );
        if (liveProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return liveProducts.stream().mapToLong(p -> p.getShopProductId()).boxed().collect(Collectors.toList());
    }


    @Override
    public LiveProduct findById(Long id, Long anchorId) {
        MybatisSqlWhereBuild where = new MybatisSqlWhereBuild(LiveProduct.class).eq(LiveProduct::getId, id);
        if (anchorId != null) {
            where.eq(LiveProduct::getAnchorId, anchorId);
        }
        return MybatisOperaterUtil.getInstance().findOne(new LiveProduct(), where);
    }

    @Override
    public LiveProduct findById(Long id) {
        return findById(id, null);
    }

    @Override
    public int updateById(LiveProduct updVO) {
        Long id = updVO.getId();
        updVO.setId(null);
        return MybatisOperaterUtil.getInstance().update(
                updVO,
                new MybatisSqlWhereBuild(LiveProduct.class)
                        .eq(LiveProduct::getId, id)
        );
    }




    /**
     * 查询主播商品个数
     *
     * @param anchorIdList anchorIdList
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/17 18:29
     */
    @Override
    public List<Map<String, Long>> countAnchorProduct(List<Long> anchorIdList) {
        if (ObjectUtils.isEmpty(anchorIdList)) {
            return Collections.emptyList();
        }
        return liveProductMapper.countAnchorProduct(anchorIdList);
    }



    @Override
    public Long countAnchorProduct(Long anchorId) {
        List<Map<String, Long>> anchorProductCount = liveProductMapper.countAnchorProduct(Arrays.asList(anchorId));
        long res = 0L;
        if (! ObjectUtils.isEmpty(anchorProductCount)) {
            Map<String, Long> countMap = anchorProductCount.get(0);
            res = countMap.get("pCount");
        }
        return res;
    }


    /**
     * 查询直播商品
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.LiveProduct>
     * @author Charlie
     * @date 2019/1/21 9:59
     */
    @Override
    public List<LiveProduct> listByIds(List<Long> ids) {
        return ObjectUtils.isEmpty(ids) ? Collections.emptyList()
                : MybatisOperaterUtil.getInstance()
                .finAll(
                        new LiveProduct(),
                        new MybatisSqlWhereBuild(LiveProduct.class)
                                .in(LiveProduct::getId, ids.toArray())
                                .eq(LiveProduct::getDelStatus, StateEnum.NORMAL)
                );

    }

    @Override
    public List<LiveProduct> findByRoomId(Long roomId) {
        return MybatisOperaterUtil.getInstance().finAll(new LiveProduct(), new MybatisSqlWhereBuild(LiveProduct.class).eq(LiveProduct::getRoomNum,roomId).eq(LiveProduct::getDelStatus,StateEnum.NORMAL));

    }

    @Override
    public Integer findOrderCountByProductIds(List<Long> list, Long create, Long current) {
        Integer cou = liveProductMapper.findOrderCountByProductIds(list,create,current);
        return cou;
    }

}
