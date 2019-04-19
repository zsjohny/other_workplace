package com.e_commerce.miscroservice.product.dao.impl;

import com.beust.jcommander.internal.Lists;
import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.product.ShopProductOwnEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.dao.ProductSkuDao;
import com.e_commerce.miscroservice.product.dao.ShopProductDao;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.product.mapper.ProductSkuMapper;
import com.e_commerce.miscroservice.product.mapper.ShopProductMapper;
import com.e_commerce.miscroservice.product.vo.ShopProductVO;
import com.e_commerce.miscroservice.product.vo.SkuOfProductDTO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 20:56
 * @Copyright 玖远网络
 */
@Component
public class ShopProductDaoImpl implements ShopProductDao {

    private Log logger = Log.getInstance(ShopProductDaoImpl.class);

    @Autowired
    private ProductSkuDao productSkuDao;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ShopProductMapper shopProductMapper;
    @Autowired
    private ProductDao productDao;

    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/11/26 21:01
     */
    @Override
    public int updateById(ShopProduct updInfo) {
        return MybatisOperaterUtil.getInstance ().update (
                updInfo, new MybatisSqlWhereBuild (ShopProduct.class).eq (ShopProduct::getId, updInfo.getId ()));
    }



    /**
     * 根据id批量查询
     *
     * @param ids     ids
     * @param storeId 门店用户id
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ShopProduct>
     * @author Charlie
     * @date 2018/11/27 11:30
     */
    @Override
    public List<ShopProduct> findByIds(List<Long> ids, Long storeId) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new ShopProduct (), new MybatisSqlWhereBuild (ShopProduct.class)
                        .in (ShopProduct::getId, ids.toArray ())
                        .eq (ShopProduct::getStoreId, storeId)
                        .eq (ShopProduct::getStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 批量推荐
     *
     * @param request ids
     * @param isCancelRecommend 是否取消推荐
     * @author Charlie
     * @date 2018/11/27 13:48
     */
    @Override
    public void batchRecommend(ShopProductQuery request, boolean isCancelRecommend) {
        Long storeId = request.getStoreId ();
        ErrorHelper.declareNull (storeId, "用户未登录");
        if (request.getIds () == null || request.getIds ().isEmpty ()) {
            logger.info ("更新商品推荐状态, 没有商品id");
            return;
        }

        List<Long> ids = request.getIds ();
        logger.info ("批量更新商品推荐状态 ids={}, 是取消推荐?={}", ids, isCancelRecommend);
        Long current = System.currentTimeMillis ();
        List<ShopProduct> shopProducts = findByIds (ids, storeId);

        //如果是设置推荐,只有上架商品才可设置为推荐
        if (! isCancelRecommend) {
            shopProducts.forEach (shopProduct -> ErrorHelper.declare (shopProduct.getSoldOut () == 1, "商品(" + shopProduct.getName () + ")未上架,不能设为推荐"));
        }

        //设置推荐/取消推荐
        for (ShopProduct shopProduct : shopProducts) {
            ShopProduct updInfo = new ShopProduct ();
            updInfo.setId (shopProduct.getId ());
            updInfo.setTopTime (isCancelRecommend ? 0L : current);
            updInfo.setUpdateTime (current);
            int rec = updateById (updInfo);
            ErrorHelper.declare (rec == 1, "设置商品推荐状态失败");
        }
    }


    /**
     * 查询商品(未删除)
     *
     * @param shopProductId shopProductId
     * @param userId        userId
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2018/11/27 20:03
     */
    @Override
    public ShopProduct findById(Long shopProductId, Long userId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopProduct (),
                new MybatisSqlWhereBuild (ShopProduct.class)
                        .eq (ShopProduct::getId, shopProductId)
                        .eq (ShopProduct::getStoreId, userId)
                        .eq (ShopProduct::getStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 查找门店用户的sku
     *
     * @param storeId storeId
     * @param skuId skuId
     * @return com.e_commerce.miscroservice.product.entity.ProductSku
     * @author Charlie
     * @date 2018/12/7 14:14
     */
    @Override
    public ProductSku findSkuBySkuId(Long storeId, Long skuId) {
        ProductSku sku = productSkuDao.findById (skuId);
        if (sku != null) {
            ShopProduct shopProduct = findById (sku.getWxaProductId (), storeId);
            return shopProduct == null ? null : sku;
        }
        return null;
    }


    /**
     * 直播商品选择列表
     *
     * @param query query
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:25
     */
    @Override
    public List<Map<String, Object>> listWxaLiveSelectProducts(ShopProductQuery query) {
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<Map<String, Object>> res = shopProductMapper.listLiveSelectProducts(query);
        if (! res.isEmpty()) {
            //格式化数据
            res.stream().forEach(product -> {
                //如果是一键上传的,填充供应商的商品信息
                Integer own = (Integer) product.get("own");
                if (ShopProductOwnEnum.isNoSelfSupport(own)) {
                    product.put("summaryImgJsonArr", product.get("supplierProductSummaryImgJsonArr"));
                    product.put("styleNo", product.get("supplierProductStyleNo"));
                }
            });

            /* 填充库存 */
            List<ShopProductVO> shopProductVOList = Lists.newArrayList(res.size());
            res.stream().forEach(product -> {
                ShopProductVO vo = new ShopProductVO();
                vo.setId((Long) product.get("id"));
                vo.setProductId((Long) product.get("productId"));
                vo.setOwn((Integer) product.get("own"));
                shopProductVOList.add(vo);
            });
            //查询sku sql
            Map<Long, SkuOfProductDTO> skuOfProductDTOMap = listOnSaleWxaSkuByShopProductIds(shopProductVOList);
            //填充库存
            res.stream().forEach(product -> {
                Long id = Long.valueOf(String.valueOf(product.get("id")));
                SkuOfProductDTO sku = skuOfProductDTOMap.get(id);
                int inventory = 0;
                if (sku == null) {
                    logger.warn("没有找到商品sku信息 id={}", id);
                } else {
                    inventory = sku.getInventory();
                }
                product.put("inventory", inventory);
            });
        }
        return res;
    }


    /**
     * 查询上架小程序商品的sku
     *
     * @param shopProductVOList shopProductVOList
     * @return key 小程序商品id, value sku信息
     * @author Charlie
     * @date 2019/1/15 17:22
     */
    @Override
    public Map<Long, SkuOfProductDTO> listOnSaleWxaSkuByShopProductIds(List<ShopProductVO> shopProductVOList) {
        if (ObjectUtils.isEmpty(shopProductVOList)) {
            return Collections.emptyMap();
        }
        int len = shopProductVOList.size();

        //一键上传商品的 productId
        List<Long> supplierProductIds = Lists.newArrayList(len);
        //自有商品的 id
        List<Long> selfProductIds = Lists.newArrayList(len);
        shopProductVOList.forEach(vo -> {
            Integer own = vo.getOwn();
            if (ShopProductOwnEnum.SUPPLIER_PRODUCT.isThis(own)) {
                //平台一键上传
                supplierProductIds.add(vo.getProductId());
            } else if (ShopProductOwnEnum.isSelfSupport(own)) {
                //自定义商品或供应商同款
                selfProductIds.add(vo.getId());
            } else {
                logger.warn("未知的平台类型");
            }
        });

        //查询自有商品sku
        Map<Long, SkuOfProductDTO> shopSkus = listSkuBySelfShopProductIds(selfProductIds);

        //查询一件上传商品sku(这里的key是平台商品的id)
        Map<Long, SkuOfProductDTO> supplierSkus = productDao.listSkuBySupplierProductIds(supplierProductIds);
        //将平台商品key转换为小程序商品id做为key
        Map<Long, SkuOfProductDTO> supplierSkusAdaptor = new HashMap<>(supplierProductIds.size());
        shopProductVOList.stream().forEach(vo -> {
            if (ShopProductOwnEnum.SUPPLIER_PRODUCT.isThis(vo.getOwn())) {
                supplierSkusAdaptor.put(vo.getId(), supplierSkus.get(vo.getProductId()));
            }
        });

        //merge
        if (! shopSkus.isEmpty()) {
            shopSkus.putAll(supplierSkusAdaptor);
            return shopSkus;
        } else if (! supplierSkusAdaptor.isEmpty()) {
            supplierSkusAdaptor.putAll(shopSkus);
            return supplierSkusAdaptor;
        } else {
            return Collections.emptyMap();
        }
    }


    /**
     * 查询自有商品的sku信息
     *
     * @param selfProductIds 小程序用户自有商品的商品id
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/15 16:37
     */
    private Map<Long, SkuOfProductDTO> listSkuBySelfShopProductIds(List<Long> selfProductIds) {
        if (ObjectUtils.isEmpty(selfProductIds)) {
            return Collections.emptyMap();
        }

        //查询sku
        List<ProductSku> productSkuList = productSkuMapper.listSkuByShopProductIds(selfProductIds);
        if (productSkuList.isEmpty()) {
            logger.warn("没有找到sku信息");
            return Collections.emptyMap();
        }

        return SkuOfProductDTO.groupByProductId(productSkuList, false);
    }


    /**
     * 获取初始化小程序直播商品的信息
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/15 10:58
     */
    @Override
    public List<ShopProduct> listByIds4InitLiveProduct(List<Long> shopProductIds) {
        return shopProductMapper.listByIds4InitLiveProduct(shopProductIds);
    }



    /**
     * 查询个别列
     * <p>名称,款号,价格,橱窗图</p>
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.ShopProduct>
     * @author Charlie
     * @date 2019/1/16 9:28
     */
    @Override
    public List<ShopProduct> findSimpleInfoByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return shopProductMapper.findSimpleInfoByIds(ids);
    }




    /**
     * 查询展示直播商品的信息
     *
     * @param id id
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2019/1/16 16:37
     */
    @Override
    public ShopProduct findLiveProductIntroById(Long id) {
        return shopProductMapper.findLiveProductIntroById(id);
    }




    /**
     * 查找图片
     *
     * @param shopProductId shopProductId
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 10:09
     */
    @Override
    public ShopProduct findImg(Long shopProductId) {
        return shopProductMapper.findImg(shopProductId);
    }

    @Override
    public ShopProduct findSimpleInfoById(Long shopProductId) {
        return shopProductMapper.findSimpleInfoById(shopProductId);
    }


}
