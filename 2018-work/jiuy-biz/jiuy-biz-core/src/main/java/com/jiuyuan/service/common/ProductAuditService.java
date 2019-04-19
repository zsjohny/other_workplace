/**
 *
 */
package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.service.common.job.ProductJobService;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProductAuditMapper;
import com.jiuyuan.entity.newentity.ProductAudit;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;


/**
 * 商品审核
 */

@Service
public class ProductAuditService implements IProductAuditService{

    private static final Logger logger = LoggerFactory.getLogger(ProductAuditService.class);
    @Autowired
    private ProductAuditMapper productAuditMapper;
    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private IProductSkuNewService productSkuNewService;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private ProductJobService productJobService;

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductAuditService#getSearchProductAuditList(com.baomidou.mybatisplus.plugins.Page, int, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, long, java.lang.String)
     */
    @Override
    public List<ProductAudit> getSearchProductAuditList(Page page, int auditState, long productId, String productName, String priceBegin,
                                                        String priceEnd, String clothesNumber, long submitAuditTimeBegin, long submitAuditTimeEnd, String brandName) {
        Wrapper<ProductAudit> wrapper = new EntityWrapper<ProductAudit>();

        //审核状态：-2客服全部、-1买手全部、0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
        if (auditState == - 2) {
            wrapper.in("auditState", "0,1,2,5");
        }
        else if (auditState == - 1) {
            wrapper.in("auditState", "3,4,5");
        }
        else {
            wrapper.eq("auditState", auditState);
        }
        //商品ID
        if (productId != 0) {
            wrapper.eq("productId", productId);
        }
        //商品名称
        if (StringUtils.isNotEmpty(productName)) {
            wrapper.like("productName", productName);
        }
        //价格开始
        if (StringUtils.isNotEmpty(priceBegin)) {
            wrapper.ge("maxLadderPrice", priceBegin);
        }
        //价格结束
        if (StringUtils.isNotEmpty(priceEnd)) {
            wrapper.le("maxLadderPrice", priceEnd);
        }
        //款号
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("clothesNumber", clothesNumber);
        }
        //审核时间开始
        if (submitAuditTimeEnd != 0) {
            wrapper.ge("submitAuditTime", submitAuditTimeBegin);
        }
        //审核时间结束
        if (submitAuditTimeEnd != 0) {
            wrapper.le("submitAuditTime", submitAuditTimeEnd);
        }
        //品牌名称
        if (StringUtils.isNotEmpty(brandName)) {
            wrapper.like("brandName", brandName);
        }
        wrapper.orderBy("update_time", false);
        List<ProductAudit> list = productAuditMapper.selectPage(page, wrapper);
        return list;

    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductAuditService#getProductAuditById(long)
     */
    @Override
    public ProductAudit getProductAuditById(long productAuditId) {
        return productAuditMapper.selectById(productAuditId);
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductAuditService#productAuditNoPass(long, java.lang.String)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void productAuditNoPass(long productAuditId, String noPassReason) {
        ProductAudit audit = getProductAuditById(productAuditId);
        int auditState = audit.getAuditState();// 审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、买手4已通过，买手已拒绝

        //1、修改本审核记录状态
        ProductAudit productAudit = new ProductAudit();
        productAudit.setId(productAuditId);
        productAudit.setAuditTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(noPassReason)) {
            noPassReason = "不通过";
        }
        productAudit.setNoPassReason(noPassReason);
        if (auditState == ProductAudit.auditState_server_wait) {//客服待审核
            productAudit.setAuditState(ProductAudit.auditState_server_no_pass);
        }
        else if (auditState == ProductAudit.auditState_buyer_wait) {//买手待审核
            productAudit.setAuditState(ProductAudit.auditState_buyer_no_pass);
        }
        else {
            throw new RuntimeException("审核状态已经不为待审核，不能进行审核不通过！");
        }
        productAuditMapper.updateById(productAudit);

        //2、修改商品状态为拒绝
        productNewService.productAuditNoPass(audit.getProductId(), noPassReason);
        logger.info("修改商品状态为拒绝完成");
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductAuditService#productAuditPass(long)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void productAuditPass(long productAuditId) {
        long time = System.currentTimeMillis();
        ProductAudit audit = getProductAuditById(productAuditId);
        // 审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、买手4已通过，买手已拒绝
        int auditState = audit.getAuditState();
        if (auditState == ProductAudit.auditState_server_wait) {
            /* 客服待审核 */
            //如果是客服审核则生成买手审核记录
            addBuyerProductAudit(audit);

            //修改本审核记录状态
            ProductAudit productAudit = new ProductAudit();
            productAudit.setId(productAuditId);
            productAudit.setAuditState(ProductAudit.auditState_server_pass);
            productAudit.setAuditTime(time);
            productAudit.setUpdateTime(time);
            productAuditMapper.updateById(productAudit);
        }
        else if (auditState == ProductAudit.auditState_buyer_wait) {
            /* 买手待审核 */
            //如果是买手审核则修改商品状态为待上架
            productNewService.productAuditPass(audit.getProductId());

            //修改本审核记录状态
            ProductAudit productAudit = new ProductAudit();
            productAudit.setId(productAuditId);
            productAudit.setAuditState(ProductAudit.auditState_buyer_pass);
            productAudit.setAuditTime(time);
            productAudit.setUpdateTime(time);
            productAuditMapper.updateById(productAudit);

            //自动上架
            autoPutaway(audit.getProductId(), audit.getSupplierId());
        }
        else {
            throw new RuntimeException("审核状态已经不为待审核，不能进行审核通过！");
        }
    }


    /**
     * 自动上架
     * 审核后自动上架, 上架
     * 定时上架, job上架
     * 暂不上架, 不处理
     *
     * @param productId
     * @param supplierId 商品所属 供应商id
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/5/30 13:45
     */
    private void autoPutaway(Long productId, Long supplierId) {

        ProductDetail detail = new ProductDetail();
        detail.setProductId(productId);
        ProductDetail productDetail = productDetailMapper.selectOne(detail);
        if (! BizUtil.isNotEmpty(productDetail)) {
            throw new RuntimeException("未找到产品详情信息 productId:" + productId);
        }

        Integer type = productDetail.getPutawayType();
        switch (type) {
            //暂不处理
            case 3:
                break;
            //定时上架
            case 2: {
                Long putwayTime = productDetail.getTimingPutwayTime();
                boolean isTimeout = putwayTime.compareTo(System.currentTimeMillis()) <= 0;
                if (isTimeout) {
                    //上架时间已经过了, 立即上架
                    productNewService.upSoldProduct(productId);
                }
                else {
                    // 当前逻辑是, 在供应商操作的上架
                    //定时时间 > 当前时间, 定时上架
//					productJobService.timingProductPutaway(putwayTime, productId, supplierId);
                }
                break;
            }

            //立即上架
            case 1: {
                productNewService.upSoldProduct(productId);
                break;
            }
            default:
                throw new RuntimeException("商品详情, 未知的上架类型, putawayType:" + type);
        }

    }

    /**
     * 生成买手审核记录
     *
     * @param productAudit
     */
    private void addBuyerProductAudit(ProductAudit productAudit) {
        ProductAudit newProductAudit = new ProductAudit();
        newProductAudit.setAuditState(ProductAudit.auditState_buyer_wait);//审核状态：0待审核、1已通过、2已拒绝
        newProductAudit.setSupplierId(productAudit.getSupplierId());//商家ID
        newProductAudit.setBrandId(productAudit.getBrandId());//品牌ID
        newProductAudit.setBrandName(productAudit.getBrandName());//品牌名称
        newProductAudit.setBrandLogo(productAudit.getBrandLogo());//品牌Logo
        newProductAudit.setProductId(productAudit.getProductId());//商品ID
        newProductAudit.setProductName(productAudit.getProductName());//商商品名称
        newProductAudit.setClothesNumber(productAudit.getClothesNumber());//商品款号
        newProductAudit.setShowcaseImgs(productAudit.getShowcaseImgs());//橱窗图片集合，英文逗号分隔
        newProductAudit.setDetailImgs(productAudit.getDetailImgs());//详情图片集合，英文逗号分隔
        newProductAudit.setVideoUrl(productAudit.getVideoUrl());//商品视频url
        newProductAudit.setMainImg(productAudit.getMainImg());//商品主图
        newProductAudit.setCategoryName(productAudit.getCategoryName());//商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
        newProductAudit.setSkuJSON(productAudit.getSkuJSON());//商品SKUJSON
        newProductAudit.setMatchProductIds(productAudit.getMatchProductIds());//搭配商品ID集合，英文逗号分隔
        newProductAudit.setLadderPriceJson(productAudit.getLadderPriceJson());//阶梯价格JSON
        newProductAudit.setMaxLadderPrice(productAudit.getMaxLadderPrice());// 最大阶梯价格
        newProductAudit.setMinLadderPrice(productAudit.getMinLadderPrice());//最小阶梯价格
        newProductAudit.setSubmitAuditTime(productAudit.getSubmitAuditTime());//提交审核时间

        long time = System.currentTimeMillis();
        newProductAudit.setCreateTime(time);//创建时间
        newProductAudit.setUpdateTime(time);//更新时间
        productAuditMapper.insert(newProductAudit);
        logger.info("生成买手审核记录完成");
    }


    /**
     * 生成客服审核记录
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void addServerProductAudit(long productId) {
        ProductNew product = productNewService.getProductById(productId);

        ProductAudit newProductAudit = new ProductAudit();
        newProductAudit.setAuditState(ProductAudit.auditState_server_wait);//审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
        newProductAudit.setSupplierId(product.getSupplierId());//商家ID
        newProductAudit.setBrandId(product.getBrandId());// 品牌ID
        newProductAudit.setBrandName(product.getBrandName());//品牌名称
        newProductAudit.setBrandLogo(product.getBrandLogo());//品牌Logo
        newProductAudit.setProductId(product.getId());//商品ID
        newProductAudit.setProductName(product.getName());//商品名称
        newProductAudit.setClothesNumber(product.getClothesNumber());//商品款号
        ProductDetail productDetail = productNewService.getProductDetail(productId);
        if (productDetail != null) {
            newProductAudit.setShowcaseImgs(productDetail.getShowcaseImgs());
            ;///橱窗图片集合，英文逗号分隔
            newProductAudit.setDetailImgs(productDetail.getDetailImgs());//详情图片集合，英文逗号分隔
            newProductAudit.setVideoUrl(productDetail.getVideoUrl());//商品视频url
        }
        newProductAudit.setMainImg(product.getMainImg());//商品主图
        newProductAudit.setCategoryName(product.getCategoryNames());//商品品类名称，只用于显示，格式例：裙装 > 连衣裙

        newProductAudit.setSkuJSON(JSON.toJSONString(productSkuNewService.buildSkuListMap(productId)));//商品SKUJSON
        newProductAudit.setMatchProductIds(product.getTogether());//搭配商品JSON
        newProductAudit.setLadderPriceJson(product.getLadderPriceJson());//阶梯价格JSON
        newProductAudit.setMaxLadderPrice(product.getMaxLadderPrice());
        ;//最大阶梯价格
        newProductAudit.setMinLadderPrice(product.getMinLadderPrice());
        ;//最大阶梯价格
        long time = System.currentTimeMillis();
        newProductAudit.setSubmitAuditTime(time);//创建时间
        newProductAudit.setCreateTime(time);//创建时间
        newProductAudit.setUpdateTime(time);//更新时间
        productAuditMapper.insert(newProductAudit);
        logger.info("生成客服审核记录完成");
    }


}
