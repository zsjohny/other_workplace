/**
 *
 */
package com.jiuyuan.service.common;

import java.util.*;

import com.jiuyuan.service.common.job.ProductJobService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.ProductCategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.ProductCategoryNew;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.PropertyValueGroup;
import com.jiuyuan.entity.newentity.PropertyValueNew;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyProduct;
import org.springframework.util.ObjectUtils;

/**
 * 供应商平台专用商品服务
 */

@Service
public class ProductSupplierFacade implements IProductSupplierFacade{
    private static final Logger logger = LoggerFactory.getLogger(ProductSupplierFacade.class);
    @Autowired
    private ProductNewMapper productNewMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private ProductCategoryNewMapper productCategoryNewMapper;
    @Autowired
    private ProductJobService productJobService;
    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private IProductSkuNewService productSkuNewService;
    @Autowired
    private IUserNewService supplierUserService;
    @Autowired
    private IPropertySupplierService propertySupplierService;
    @Autowired
    private IDynamicPropertyCategoryService dynamicPropertyCategoryService;
    @Autowired
    private IDynamicPropertyProductService dynamicPropertyProductService;

    /**
     *
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public ProductNew updProductSkuInfo(long productId, long supplierId, long lowarehouseId, String updSkuListJson, String addSkuListJson) {
        //1、编辑sku库存数量
        updSkuList(productId, updSkuListJson);

        //2、新增SKU信息
        addSkuList(productId, addSkuListJson, supplierId, lowarehouseId);

        return productNewService.getProductById(productId);
    }


    /**
     * 新增SKU信息
     *
     * @param productId
     * @param addSkuListJson
     */
    @Transactional( rollbackFor = Exception.class )
    public void addSkuList(long productId, String addSkuListJson, long supplierId, long lowarehouseId) {
        ProductNew product = productNewService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException ("没有找到商品信息");
        }

        //防止表单重复提交 update by charlie >==================================================
        ProductNew upd = new ProductNew ();
        long updTime = System.currentTimeMillis ();
        if (ObjectUtils.nullSafeEquals (updTime, product.getUpdateTime ())) {
            updTime++;
        }
        upd.setUpdateTime (updTime);
        upd.setId (product.getId ());
        Integer rec = productNewMapper.updateById (upd);
        logger.info ("新增商品sku productId={}, rec={}", productId, rec);
        if (rec != 1) {
            throw new RuntimeException ("网络延迟,请稍后再试");
        }
        //防止表单重复提交 update by charlie <==================================================


        //批量添加SKU
        if (StringUtils.isNotEmpty(addSkuListJson)) {
            JSONArray addSkuListJsonArr = JSON.parseArray(addSkuListJson);
            for (int i = 0; i < addSkuListJsonArr.size(); i++) {
                JSONObject skuObject = addSkuListJsonArr.getJSONObject(i);
                //库存数量
                int remainCount = Integer.parseInt(skuObject.getString("remainCount"));
                //颜色值
                String colorName = skuObject.getString("colorName");
                //去空格
                colorName = colorName.replaceAll(" ", "");
                //颜色组Id
                long colorGroupId = Long.parseLong(skuObject.getString("colorGroupId"));
                //尺码值
                String sizeName = skuObject.getString("sizeName");
                //去空格
                sizeName = sizeName.replaceAll(" ", "");
                //尺码组Id
                long sizeGroupId = Long.parseLong(skuObject.getString("sizeGroupId"));
                long colorId = buildColor(colorName, colorGroupId, supplierId);
                Double weight = skuObject.getDouble("weight");
                long sizeId = buildSize(sizeName, sizeGroupId, supplierId);
                productSkuNewService.insertProductSku(productId, product, sizeId, colorId, sizeName, colorName, remainCount, supplierId, lowarehouseId, weight);
            }
        }

    }


    /**
     * 获得颜色Id（如果不存在则新建）
     *
     * @param colorName
     * @param colorGroupId
     * @param supplierId
     * @return
     */
    private long buildColor(String colorName, long colorGroupId, long supplierId) {
        //获取颜色值列表
        List<PropertyValueNew> colorList = propertySupplierService.getColorList(supplierId);

        long colorId = 0;
        //1、循环颜色列表获取colorId
        for (PropertyValueNew value : colorList) {
            String valueName = value.getPropertyValue();
            if (colorName.equals(valueName)) {
                colorId = value.getId();
            }
        }
        //2、如果颜色不存在则新建
        if (colorId == 0) {
            logger.info("颜色不存在进行新建，colorName：" + colorName);
            colorId = propertySupplierService.addColorPropertyValue(colorName, colorGroupId, supplierId);
        }
        return colorId;
    }

    /**
     * 获得尺码Id（如果不存在则新建）
     *
     * @param supplierId
     * @return
     */
    private long buildSize(String sizeName, long sizeGroupId, long supplierId) {
        //获取尺码值列表
        List<PropertyValueNew> sizeList = propertySupplierService.getSizeList(supplierId);

        long sizeId = 0;
        //1、循环尺码列表获取colorId
        for (PropertyValueNew value : sizeList) {
            String valueName = value.getPropertyValue();
            if (sizeName.equals(valueName)) {
                sizeId = value.getId();
            }
        }
        //2、如果尺码不存在则新建
        if (sizeId == 0) {
            sizeId = propertySupplierService.addSizePropertyValue(sizeName, sizeGroupId, supplierId);
        }
        return sizeId;
    }


    /**
     * 编辑sku库存数量
     *
     * @param productId
     * @param updSkuListJson
     */
    @Transactional( rollbackFor = Exception.class )
    public void updSkuList(long productId, String updSkuListJson) {
        JSONArray updSkuListJsonArr = JSON.parseArray(updSkuListJson);
//		for(Object sku : updSkuListJsonArr){
        for (int i = 0; i < updSkuListJsonArr.size(); i++) {
            JSONObject skuObject = updSkuListJsonArr.getJSONObject(i);
            String skuId = skuObject.getString("skuId");
            String remainCount = skuObject.getString("remainCount");
            Double weight = skuObject.getDouble("weight");
            productSkuNewService.updSkuRemainCount(productId, Long.parseLong(skuId), Integer.parseInt(remainCount), weight);
        }
    }


    /* (non-Javadoc)
     * @see com.supplier.service.facade.IProductSupplierFacade#updProductBasicInfo(long, long, long, java.lang.String, long, java.lang.String, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public long updProductBasicInfo(long productId, long supplierId, long oneCategoryId, String oneCategoryName, long twoCategoryId, String twoCategoryName
            , long threeCategoryId, String threeCategoryName, String name, String mainImg, String showcaseImgs, String clothesNumber, String ladderPriceJson,
                                    long lOWarehouseId, String vedioMain, Integer expressFreee) {// 阶梯价json


        //要返回的商品ID
        long returnProductId = 0;
        //校验款号是否可用
        boolean usable = productNewService.checkClothesNumberUsable(supplierId, productId, clothesNumber);
        if (! usable) {//不可用则弹出提示
            throw new RuntimeException("款号不可用");
        }
        //获取当前供应商品牌
        BrandNew supplierBrand = supplierUserService.getSupplierBrandInfoBySupplierId(supplierId);
        if (supplierBrand == null) {
            throw new RuntimeException("当前供应商没有关联品牌");
        }
        else {
            String logo = supplierBrand.getBrandIdentity();
            if (StringUtils.isEmpty(logo)) {
                throw new RuntimeException("当前供应商关联品牌没有填写品牌Logo");
            }

            int brandType = supplierBrand.getBrandType();//品牌类型：1：高档，2：中档
            if (brandType == 0) {
                throw new RuntimeException("当前供应商关联品牌没进行高档中档设置，请联系客服设置！");
            }

        }

        //组装商品数据
        ProductNew product = new ProductNew();
        product.setOneCategoryId(oneCategoryId);
        product.setOneCategoryName(oneCategoryName);
        product.setTwoCategoryId(twoCategoryId);
        product.setTwoCategoryName(twoCategoryName);
        product.setThreeCategoryId(threeCategoryId);
        product.setThreeCategoryName(threeCategoryName);
        product.setVedioMain(vedioMain);
        product.setExpressFree(expressFreee);
        //最后一级分类ID，会只有一级或只有一级和二级
        String categoryIds = null;
        long categoryId = 0;
        if (threeCategoryId == 0) {
            //判断是否选择第三级类目
            throw new RuntimeException("请选择商品分类");
        }

        if (threeCategoryId != 0) {
            categoryId = threeCategoryId;
            categoryIds = oneCategoryId + "," + twoCategoryId + "," + threeCategoryId;
        }
        else if (twoCategoryId != 0) {
            categoryId = twoCategoryId;
            categoryIds = oneCategoryId + "," + twoCategoryId;
        }
        else if (oneCategoryId != 0) {
            categoryId = oneCategoryId;
            categoryIds = String.valueOf(oneCategoryId);
        }
        else {
            throw new RuntimeException("没有选择没类");
        }

        product.setCategoryIds(categoryIds);
        product.setCategoryId(categoryId);
        product.setName(name);
        product.setMainImg(mainImg);
        product.setDetailImages(showcaseImgs);
        String clothesNumberPrefix = supplierBrand.getClothNumberPrefix();//品牌款号前缀
        clothesNumber = clothesNumberPrefix + clothesNumber;
        product.setClothesNumber(clothesNumber);
        product.setLadderPriceJson(ladderPriceJson);
        product.setMaxLadderPrice(ProductNew.buildMaxLadderPrice(ladderPriceJson));
        product.setMinLadderPrice(ProductNew.buildMinLadderPrice(ladderPriceJson));
        product.setWholeSaleCash(ProductNew.buildMaxLadderPrice(ladderPriceJson));

        if (productId == 0) {//新建商品
            returnProductId = addProduct(product, supplierId, lOWarehouseId, supplierBrand, categoryId, showcaseImgs);
        }
        else {//编辑商品
            returnProductId = editProduct(productId, product, categoryId, showcaseImgs);
            //修改sku款号
            productSkuNewService.editProductSkuClothesNumber(productId, clothesNumber);
        }

//UPDATE yjj_ProductDetail SET showcaseImgs=?, updateTime=?, putawayType=?, timingPutwayTime=? WHERE id=?
        return returnProductId;
    }

    /**
     * 添加商品第一步
     *
     * @param product
     * @param supplierId
     * @param lOWarehouseId
     * @param supplierBrand
     * @param categoryId
     * @param showcaseImgs
     * @return
     */
    private long addProduct(ProductNew product, long supplierId, long lOWarehouseId, BrandNew supplierBrand,
                            long categoryId, String showcaseImgs) {
        long time = System.currentTimeMillis();
        //1、新建商品
        product.setSupplierId(supplierId);
        product.setLOWarehouseId(lOWarehouseId);
        product.setBrandId(supplierBrand.getBrandId());
        product.setBrandName(supplierBrand.getBrandName());
        product.setBrandLogo(supplierBrand.getBrandIdentity());
        product.setBrandType(supplierBrand.getBrandType());
        product.setCreateTime(time);
        product.setUpdateTime(time);
        product.setSaleStartTime(0L);
        product.setSaleEndTime(0L);
        product.setState(ProductNewStateEnum.edit.getIntValue());
        productNewMapper.insert(product);
        long productId = product.getId();

        //2、新建商品详情
        ProductDetail productDetail = new ProductDetail();
        productDetail.setShowcaseImgs(showcaseImgs);
        productDetail.setProductId(productId);
        productDetail.setCreateTime(time);
        productDetail.setUpdateTime(time);
        productDetailMapper.insert(productDetail);

        //3、新建分类
        ProductCategoryNew productCategoryNew = new ProductCategoryNew();
        productCategoryNew.setProductId(productId);//产品ID
        productCategoryNew.setCategoryId(categoryId);//分类ID
        productCategoryNew.setStatus(0);//状态
        productCategoryNew.setCreateTime(time);//创建时间
        productCategoryNew.setUpdateTime(time);//修改时间
        productCategoryNewMapper.insert(productCategoryNew);
        return productId;
    }


    /**
     * 编辑商品
     *
     * @param productId
     * @param product
     * @param categoryId
     */
    private long editProduct(long productId, ProductNew product, long categoryId, String showcaseImgs) {
        long time = System.currentTimeMillis();
        //编辑商品详情
        ProductNew oldProduct = productNewService.getProductById(productId);
        ProductDetail oldProductDetail = productNewService.getProductDatailByProductId(productId);
        //1、编辑商品
        product.setId(productId);
        product.setUpdateTime(time);
        //如果下架的商品编辑后状态改待提审，

        //审核不通过或待上架或下架或上架编辑后则直接改为待提审
        int state = oldProduct.getState();
        if (state == ProductNewStateEnum.audit_no_pass.getIntValue() ||
                state == ProductNewStateEnum.wait_up_sold.getIntValue() ||
                state == ProductNewStateEnum.down_sold.getIntValue() ||
                state == ProductNewStateEnum.up_sold.getIntValue()) {
            //验证商品是否需要提交审核
            boolean needSubmitAudit = checkNeedAudit(product.getLadderPriceJson(), oldProduct.getLadderPriceJson(), showcaseImgs, oldProductDetail.getShowcaseImgs(),
                    oldProduct.getVedioMain(), product.getVedioMain()
            );
            if (needSubmitAudit) {
                logger.info("第二步改变了需要审核项商品状态改为待提审**********************************");
                productNewService.waitProduct(productId);
            }
        }
        //注入价格
        injectProductMemberPrice (product, oldProduct, product.getLadderPriceJson());
        productNewMapper.updateById(product);

        //2、编辑商品详情数据

        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(oldProductDetail.getId());
        productDetail.setShowcaseImgs(showcaseImgs);
        productDetail.setUpdateTime(time);
        productDetailMapper.updateById(productDetail);

        //3、编辑商品
        long oldCategoryId = oldProduct.getCategoryId();//旧分类ID
        Wrapper<ProductCategoryNew> wrapper = new EntityWrapper<ProductCategoryNew>();
        wrapper.eq("ProductId", productId);
        wrapper.eq("CategoryId", oldCategoryId);
        wrapper.eq("Status", 0);
        List<ProductCategoryNew> productCategoryList = productCategoryNewMapper.selectList(wrapper);
        if (productCategoryList.size() > 0) {//老分类存在则修改成新分类
            ProductCategoryNew productCategory = productCategoryList.get(0);
            ProductCategoryNew productCategoryNew = new ProductCategoryNew();
            productCategoryNew.setId(productCategory.getId());
            productCategoryNew.setCategoryId(categoryId);
            productCategoryNew.setUpdateTime(time);
            productCategoryNewMapper.updateById(productCategoryNew);
        }
        else {//老分类不存在则新建新分类
            //新建分类
            ProductCategoryNew productCategoryNew = new ProductCategoryNew();
            productCategoryNew.setProductId(productId);//产品ID
            productCategoryNew.setCategoryId(categoryId);//分类ID
            productCategoryNew.setStatus(0);//状态
            productCategoryNew.setCreateTime(time);//创建时间
            productCategoryNew.setUpdateTime(time);//修改时间
            productCategoryNewMapper.insert(productCategoryNew);
        }
        //切换分类
        switchDynaProp(productId, categoryId);
        logger.info("切换分类结束**********************************");
        return productId;
    }




    /**
     * 更新商品的会员阶梯价
     *
     * @param product 需要组装的新的商品信息
     * @param history 记录记录
     * @param newLadderPriceJson 新更改的信息
     * @author Charlie
     * @date 2018/8/14 12:40
     */
    private void injectProductMemberPrice(ProductNew product, ProductNew history, String newLadderPriceJson) {
        if (history.getMemberLevel ().equals (0)) {
            //非会员
            return;
        }
        if (ObjectUtils.nullSafeEquals (history.getLadderPriceJson (), newLadderPriceJson)) {
            //没有更改
            return;
        }

        //历史会员阶梯价
        List<ProductNewService.LadderPriceVo> hisMembPrices = ProductNewService.LadderPriceVo.build2List (history.getMemberLadderPriceJson ());
        //新的商品阶梯价
        List<ProductNewService.LadderPriceVo> newOriginalPrices = ProductNewService.LadderPriceVo.build2List (newLadderPriceJson);
        //将要更新的新的会员阶梯价
        List<ProductNewService.LadderPriceVo> newMembPrices = new ArrayList<> (newOriginalPrices.size ());
        for (ProductNewService.LadderPriceVo nVo : newOriginalPrices) {
            //count相等,则会员阶梯价不变
            boolean isEquals = false;
            if (hisMembPrices != null) {
                for (ProductNewService.LadderPriceVo membPrice : hisMembPrices) {
                    if (nVo.getCount ().equals (membPrice.getCount ())) {
                        isEquals = true;
                        newMembPrices.add (membPrice);
                        break;
                    }
                }
            }
            //count不相等,去商品阶梯价原价做会员阶梯价
            if (! isEquals) {
                newMembPrices.add (nVo);
            }
        }

        //注入会员阶梯json
        String membPriceJson = BizUtil.bean2json (newMembPrices);
        product.setMemberLadderPriceJson (membPriceJson);
        //注入会员阶梯最大最小价
        Map<Integer, ProductNewService.LadderPriceVo> newMembPrice = ProductNewService.LadderPriceVo.build (membPriceJson);
        product.setMemberLadderPriceMax (ProductNewService.LadderPriceVo.acquireMaxPrice (newMembPrice));
        product.setMemberLadderPriceMin (ProductNewService.LadderPriceVo.acquireMinPrice (newMembPrice));
    }


    /*
     * @see com.supplier.service.facade.IProductSupplierFacade#updProductDetailInfo(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long)
     */

    /**
     * @param productId        商品ID
     * @param togetherProducts 搭配推荐商品Id集合，英文逗号分隔
     * @param detailImgs       商品详情图集合JSON
     * @param videoUrl         商品视频
     * @param putawayType      上架类型 1:审核通过后立即上架, 2:定时上架, 3:暂不上架
     * @param timingPutwayTime 定时上架时间
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void updProductDetailInfo(long productId, String togetherProducts, String detailImgs, String videoUrl, String videoName, long videoFileId, int putawayType, long timingPutwayTime) {

        //校验putawayType, 定时上架时间不做校验
        switch (putawayType) {
            case 1:
            case 3:
                timingPutwayTime = 0L;
            case 2:
                break;
            default:
                throw BizException.defulat().msg("请求参数不合法, putawayType:" + putawayType);
        }

        // 历史商品信息
        ProductNew historyProduct = productNewService.getProductById(productId);
        // 历史商品详情信息
        ProductDetail historyProductDetail = productNewService.getProductDatailByProductId(productId);
        // 更新商品状态, 如果修改了必审项, 则下架商品和sku, 将状态设为待提审状态
        boolean needSubmitAudit = updateProductInfoAndState(productId, togetherProducts, detailImgs, videoUrl, videoName, videoFileId, historyProduct, historyProductDetail);

        // 修改详情表
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(historyProductDetail.getId());
        productDetail.setVideoUrl(videoUrl);
        productDetail.setVideoName(videoName);
        productDetail.setVideoFileId(videoFileId);
        productDetail.setDetailImgs(detailImgs);
        productDetail.setUpdateTime(System.currentTimeMillis());
        if (! isOnShelvesNoChangeKeyProp(historyProduct, needSubmitAudit)) {
            productDetail.setPutawayType(putawayType);
            productDetail.setTimingPutwayTime(timingPutwayTime);
        }
        productDetailMapper.updateById(productDetail);

        // 定时上架,或直接上架
        timingOrDirectOnShelves(historyProductDetail, productDetail, historyProduct, needSubmitAudit);
    }


    /**
     * 商品手动上架
     *
     * @param productId 待上架商品id
     * @param supplierId 供应商id
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/14 15:57
     */
    @Override
    public void manualOnShelvesV375(Long productId, Long supplierId) {

        // 历史商品信息
        ProductNew historyProduct = productNewService.getProductById(productId);
        if (historyProduct == null) {
            throw BizException.defulat().msg("未找到商品");
        }
        else if (! historyProduct.getSupplierId().equals(supplierId)) {
            throw BizException.defulat().msg("商品并不属于供应商");
        }
        // 历史商品详情信息
        ProductDetail historyProductDetail = productNewService.getProductDatailByProductId(productId);

        // 修改详情信息
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(historyProductDetail.getId());
        productDetail.setUpdateTime(System.currentTimeMillis());
        productDetail.setPutawayType(1);
        productDetail.setTimingPutwayTime(0L);
        productDetailMapper.updateById(productDetail);

        // 定时上架
        timingOrDirectOnShelves(historyProductDetail, productDetail, historyProduct, false);
    }



    /**
     * 更新商品信息, 以及商品和sku上下架
     *
     * @param productId
     * @param togetherProducts
     * @param detailImgs
     * @param videoUrl
     * @param videoName
     * @param videoFileId
     * @param oldProduct
     * @param oldProductDetail
     * @return boolean
     * @auther Charlie(唐静)
     * @date 2018/6/14 15:37
     */
    public boolean updateProductInfoAndState(long productId, String togetherProducts, String detailImgs, String videoUrl, String videoName, long videoFileId, ProductNew oldProduct, ProductDetail oldProductDetail) {
        long curr = System.currentTimeMillis();
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setSummaryImages(detailImgs);
        product.setVideoUrl(videoUrl);
        product.setVideoName(videoName);
        product.setVideoFileId(videoFileId);
        product.setTogether(togetherProducts);
        product.setUpdateTime(curr);
        product.setNewTime(curr);
        // 是否修改了必审项,是则商品还需要进行审核
        boolean needSubmitAudit = false;
        //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待审核，审核中）、3（审核不通过）、 4（待上架，审核通过、待上架）、 5（上架，审核通过、已上架）、 6（下架，审核通过、已下架）
        int state = oldProduct.getState();
        //如果商品状态为编辑中则修改为新建状态
        if (state == ProductNewStateEnum.edit.getIntValue()) {
            product.setState(ProductNewStateEnum.new_finish.getIntValue());
        }
        else if (state == ProductNewStateEnum.audit_no_pass.getIntValue() ||
                state == ProductNewStateEnum.wait_up_sold.getIntValue() ||
                state == ProductNewStateEnum.down_sold.getIntValue() ||
                state == ProductNewStateEnum.up_sold.getIntValue()) {//审核不通过或待上架或下架或上架编辑后则直接改为待提审
            //验证商品是否需要提交审核
            needSubmitAudit = detailImagesAndVideoUrlChanged(oldProductDetail, detailImgs, videoUrl);
            if (needSubmitAudit) {
                product.setState(ProductNewStateEnum.wait_submit_audit.getIntValue());
                logger.info("第三步改变了需要审核项商品状态改为待提审**********************************");
                // 下架商品和sku
                productNewService.waitProduct(productId);
            }
        }
        //编辑时审核开关打开，打开后需要提交审核后才能上架
//		product.setNeedAudit(ProductNew.needAudit_true);
        productNewMapper.updateById(product);
        return needSubmitAudit;
    }


    /**
     * 修改商品定时上架,直接上架
     *
     * @param history         历史信息
     * @param current         当前信息
     * @param product         商品状态：
     * @param passState2Audit 商品是否修改了必省项, 商品从待上架,上架,下架状态修改了必省项时,可能商品的瞬时状态还是原来状态
     *                        但是商品不允许上架,这是判断商品是否执行立即上架的过滤条件
     *                        passState2Audit=true,关闭上架条件
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/12 10:25
     */
    @Transactional( rollbackFor = Exception.class )
    public void timingOrDirectOnShelves(ProductDetail history, ProductDetail current, ProductNew product, boolean passState2Audit) {

        logger.info("修改商品定时上架,直接上架. 是否修改必申项:"+passState2Audit
                +",历史上架状态:"+history.getPutawayType()+",请求上架状态:"+current.getPutawayType()
                +",历史上架时间:"+history.getTimingPutwayTime()+",请求上架时间:"+current.getTimingPutwayTime()
                +",商品id:"+product.getId()+",商品首次上架时间:"+product.getLastPutonTime()
        );

        // 上架商品并没有修改必申项, 是不需要设置定时操作的
        boolean isOnShelvesNoChangeKeyProp = isOnShelvesNoChangeKeyProp(product, passState2Audit);
        if (isOnShelvesNoChangeKeyProp) {
            return;
        }

        current.setProductId(history.getProductId());
        // 历史定时信息和请求提交的是否一致, 一致则return, 不一致则申请job服务
        boolean isChange = false;
        boolean isTimeChange = ! history.getTimingPutwayTime().equals(current.getTimingPutwayTime());
        boolean isTypeChange = ! history.getPutawayType().equals(current.getPutawayType());
        // 是否有未执行的定时任务
        boolean hasUnDoTask = isHasUnDoTimingOnShelvesTask(history);
        if (isTimeChange || isTypeChange) {
            isChange = true;
        }

        if (! isChange && product.getState() != 7) {
            // 没有修改则不操作, 但是下架商品有例外,手动下架后保留的记录的上次执行后的"立刻上架",这个时候要重新判断
            return;
        }


        // 需添加的job任务
        List<ProductDetail> addTask = new ArrayList<>();
        // 需修改的job任务
        List<ProductDetail> updTask = new ArrayList<>();
        // 需删除的job任务
        List<ProductDetail> delTask = new ArrayList<>();

        // 商品上架状态是没有修改过必审项,且是待上架或下架
        // 用于判断商品是否可以立即上架,true可以立即上架
        boolean isCanOnShelves = ! passState2Audit && (product.getState().equals(5) || product.getState().equals(7));


        int currTaskType = current.getPutawayType();
        // 定时上架
        if (currTaskType == 2) {
            /*
             * 新设置时间是否有效
             *      有未执行任务,时间有效,修改定时任务  任务未完成
             *      有未执行任务,时间无效,删除定时任务(待上架和下架商品立刻上架)    任务已完成
             *      无未执行任务,时间有效,新增  任务未完成
             *      无未执行任务,时间无效,(待上架和下架商品立刻上架)  任务已完成
             */
            boolean isNewTimeValid = System.currentTimeMillis() < current.getTimingPutwayTime().longValue();
            if (hasUnDoTask && isNewTimeValid) {
                updTask.add(current);
            }
            else if (hasUnDoTask) {
                delTask.add(history);
                if (isCanOnShelves) {
                    productNewService.upSoldProduct(product.getId());
                }
            }
            else if (isNewTimeValid) {
                addTask.add(current);
            }
            else {
                if (isCanOnShelves) {
                    productNewService.upSoldProduct(product.getId());
                }
            }
        }

        //新任务是不设置
        else if (currTaskType == 3) {
            /* 删除未执行历史任务 */
            if (hasUnDoTask) {
                delTask.add(history);
            }
        }

        // 新任务是审核通过立即上架
        else {
            // 有未执行的任务,状态是待上架,删除历史任务
            if (hasUnDoTask) {
                delTask.add(history);
            }
            // 待上架和下架商品立刻上架
            if (isCanOnShelves) {
                productNewService.upSoldProduct(product.getId());
            }
        }

        // 调用job服务
        if (! addTask.isEmpty() || ! updTask.isEmpty() || ! delTask.isEmpty()) {
            productJobService.timingProductPutawayMulti(addTask, delTask, updTask, product.getSupplierId());
        }
    }


    /**
     * 上架商品没有修改必省项
     *
     * @param product
     * @param passState2Audit 修改了
     * @return boolean true 没有改
     * @auther Charlie(唐静)
     * @date 2018/6/13 20:31
     */
    public static boolean isOnShelvesNoChangeKeyProp(ProductNew product, boolean passState2Audit) {
        // 是上架商品,且修改了必申项
        return product.getState().equals(6) && ! passState2Audit;
    }


    /**
     * 当前商品是否还有未执行的定时上架任务
     *
     * @param history
     * @return true 有未执行任务
     * @auther Charlie(唐静)
     * @date 2018/6/12 14:06
     */
    public static boolean isHasUnDoTimingOnShelvesTask(ProductDetail history) {
        boolean hasUnDoTask = false;
        if (history.getPutawayType().equals(2) && System.currentTimeMillis() < history.getTimingPutwayTime()) {
            hasUnDoTask = true;
        }
        return hasUnDoTask;
    }


    /* (non-Javadoc)
     * @see com.supplier.service.facade.IProductSupplierFacade#getColorListAndSizeList(long)
     */
    @Override
    public Map<String, Object> getColorListAndSizeList(long supplierId) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取尺码组列表（如：通用、欧码）
        List<PropertyValueGroup> sizeGroupList = propertySupplierService.getSizeGroupList();
        //获取颜色组列表（如：红色系）
        List<PropertyValueGroup> colorGroupList = propertySupplierService.getColorGroupList();
        //获取尺码值列表
        List<PropertyValueNew> sizeList = propertySupplierService.getSizeList(supplierId);
        //获取颜色值列表
        List<PropertyValueNew> colorList = propertySupplierService.getColorList(supplierId);
        map.put("colorGroupList", colorGroupList);
        map.put("sizeGroupList", sizeGroupList);
        map.put("colorList", colorList);
        map.put("sizeList", sizeList);
        return map;
    }

    /**
     * 验证商品是否需要提交审核
     * 说明：需要提交审核条件
     * 1、橱窗图发生变更
     * 2、价格是否上调
     * 3、购买数量更改为1
     *
     * @param newLadderPriceJson
     * @param oldLadderPriceJson
     * @param newShowcaseImgs
     * @param oldShowcaseImgs
     * @return
     */
    private boolean checkNeedAudit(String newLadderPriceJson, String oldLadderPriceJson, String newShowcaseImgs,
                                   String oldShowcaseImgs, String oldVedioMian, String newVedioMain) {
        //1、判断橱窗图发生变更
        if (! newShowcaseImgs.equals(oldShowcaseImgs)) {
            return true;
        }
//        //2.将阶梯价JSON解析成Map,key(购买数量)、value(价格)
//        Map<Integer, Double> newLadderPriceMap = ProductNew.buildLadderPriceMap(newLadderPriceJson);
//        Map<Integer, Double> oldLadderPriceMap = ProductNew.buildLadderPriceMap(oldLadderPriceJson);
//        //3、判断价格是否上调
//        if (chekPrice(newLadderPriceMap, oldLadderPriceMap)) {
//            return true;
//        }
//        //4.购买数量更改为1
//        if (chekBuyCount(newLadderPriceMap, oldLadderPriceMap)) {
//            return true;
//        }
        if (!ObjectUtils.nullSafeEquals (newLadderPriceJson, oldLadderPriceJson)) {
            return true;
        }

        if (newVedioMain == null && oldVedioMian == null) {
            return true;
        }
        // 视频地址是否发生了变化
        if (! newVedioMain.equals(oldVedioMian)) {
            return true;
        }

        return false;
    }

    /**
     * 判断价格是否上调
     *
     * @param newLadderPriceMap
     * @param oldLadderPriceMap
     * @return
     */
    private boolean chekPrice(Map<Integer, Double> newLadderPriceMap, Map<Integer, Double> oldLadderPriceMap) {

//		1、价格定义初始值
        double newPrice0 = 0;
        double newPrice1 = 0;
        double newPrice2 = 0;
        double oldPrice0 = 0;
        double oldPrice1 = 0;
        double oldPrice2 = 0;
        //2、解析map为新价格赋值
        List<Integer> newCountList = new ArrayList<Integer>(newLadderPriceMap.keySet());
        for (int i = 0; i < newCountList.size(); i++) {
            double price = newLadderPriceMap.get(newCountList.get(i));
            if (i == 0) {
                newPrice0 = price;
            }
            else if (i == 1) {
                newPrice1 = price;
            }
            else if (i == 2) {
                newPrice2 = price;
            }
        }
        //3、解析map为老价格赋值
        List<Integer> oldCountList = new ArrayList<Integer>(oldLadderPriceMap.keySet());
        for (int i = 0; i < oldCountList.size(); i++) {
            double price = oldLadderPriceMap.get(oldCountList.get(i));
            if (i == 0) {
                oldPrice0 = price;
            }
            else if (i == 1) {
                oldPrice1 = price;
            }
            else if (i == 2) {
                oldPrice2 = price;
            }
        }
        //4、比对第一阶梯价格
        if (newPrice0 > 0 && oldPrice0 > 0) {
            if (newPrice0 > oldPrice0) {
                return true;
            }
        }
        //5、比对第2阶梯价格
        if (newPrice1 > 0 && oldPrice1 > 0) {
            if (newPrice1 > oldPrice1) {
                return true;
            }
        }
        //6、比对第3阶梯价格
        if (newPrice2 > 0 && oldPrice2 > 0) {
            if (newPrice2 > oldPrice2) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断购买数量是否变更为1
     *
     * @param newLadderPriceMap
     * @param oldLadderPriceMap
     * @return
     */
    private boolean chekBuyCount(Map<Integer, Double> newLadderPriceMap, Map<Integer, Double> oldLadderPriceMap) {
        //1、解析map获取新购买量
        List<Integer> newCountList = new ArrayList<Integer>(newLadderPriceMap.keySet());
        //2、解析map获取老购买量
        List<Integer> oldCountList = new ArrayList<Integer>(oldLadderPriceMap.keySet());
        int newCount1 = newCountList.get(0);
        int oldCount1 = oldCountList.get(0);
        //3、判断购买数量是否变更为1
        if (oldCount1 != 1 && newCount1 == 1) {
            return true;
        }
        return false;
    }

    /**
     * 验证商品是否需要提交审核
     * 说明：需要提交审核的条件
     * 1.商品详情图发生变更
     * 2.商品视频发生变更
     *
     * @param oldProductDetail
     * @param detailImgs
     * @param videoUrl
     * @return
     */
    private boolean detailImagesAndVideoUrlChanged(ProductDetail oldProductDetail, String detailImgs, String videoUrl) {
        String oldDetailImgs = oldProductDetail.getDetailImgs();
        String oldVideoUrl = oldProductDetail.getVideoUrl();
        //1.判断详情图是否发生变更
        if (! oldDetailImgs.equals(detailImgs)) {
            return true;
        }
        //2.判断商品视频是否发生变更
        if (StringUtils.isEmpty(oldVideoUrl) && ! StringUtils.isEmpty(videoUrl)) {
            return true;
        }
        else if (! StringUtils.isEmpty(oldVideoUrl) && ! StringUtils.isEmpty(videoUrl) && ! oldVideoUrl.equals(videoUrl)) {
            return true;
        }
        return false;
    }

    //切换商品分类更改动态属性
    public void switchDynaProp(long productId, long newThreeCategoryId) {
        logger.info("切换分类开始**********************************************************");
//		Wrapper<DynamicPropertyCategory> wrapper =new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", oldThreeCategoryId);
//		List<DynamicPropertyCategory> oldDynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(wrapper );
        Wrapper<DynamicPropertyCategory> wrapper = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", newThreeCategoryId);
        List<DynamicPropertyCategory> newDynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(wrapper);

        Wrapper<DynamicPropertyProduct> dynamicPropertywrapper = new EntityWrapper<DynamicPropertyProduct>().eq("product_id", productId);
        List<DynamicPropertyProduct> dynamicPropertyProductList = dynamicPropertyProductService.selectList(dynamicPropertywrapper);
        boolean flag = true;
        for (DynamicPropertyProduct dynamicPropertyProduct : dynamicPropertyProductList) {
            for (DynamicPropertyCategory dynamicPropertyCategory : newDynamicPropertyCategoryList) {
                if (dynamicPropertyProduct.getDynaPropId() == dynamicPropertyCategory.getDynaPropId()) {
                    logger.info("新分类绑定了该属性**********************************************************");
                    flag = false;//新分类绑定了该属性
                }
            }
            if (flag) {//新分类未绑定该属性
                logger.info("删除新分类未绑定的属性**********************************************************");
                dynamicPropertyProductService.delete(dynamicPropertyProduct);
            }
        }
    }



/*

    */
/**
     * @param productId        商品ID
     * @param togetherProducts 搭配推荐商品Id集合，英文逗号分隔
     * @param detailImgs       商品详情图集合JSON
     * @param videoUrl         商品视频
     * @param putawayType      上架类型 1:审核通过后立即上架, 2:定时上架, 3:暂不上架
     * @param timingPutwayTime 定时上架时间
     *//*

    @Override
    @Transactional( rollbackFor = Exception.class )
    public void updProductDetailInfo(long productId, String togetherProducts, String detailImgs, String videoUrl, String videoName, long videoFileId, int putawayType, long timingPutwayTime) {
        long curr = System.currentTimeMillis();
        System.err.println("=================================================");
        System.err.println("productId : " + productId);
        System.err.println("=================================================");
        //校验putawayType, 定时上架时间不做校验
        switch (putawayType) {
            case 1:
            case 3:
                timingPutwayTime = 0L;
            case 2:
                break;
            default:
                throw new RuntimeException("请求参数不合法, putawayType:" + putawayType);
        }


        // 修改商品信息 "yjj_Product"
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setSummaryImages(detailImgs);
        product.setVideoUrl(videoUrl);
        product.setVideoName(videoName);
        product.setVideoFileId(videoFileId);
        product.setTogether(togetherProducts);
        product.setUpdateTime(curr);
        product.setNewTime(curr);
        ProductNew oldProduct = productNewService.getProductById(productId);

        ProductDetail oldProductDetail = productNewService.getProductDatailByProductId(productId);

        // 是否修改了必审项,是则商品还需要进行审核
        boolean needSubmitAudit = false;
        //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待审核，审核中）、3（审核不通过）、 4（待上架，审核通过、待上架）、 5（上架，审核通过、已上架）、 6（下架，审核通过、已下架）
        int state = oldProduct.getState();
        //如果商品状态为编辑中则修改为新建状态
        if (state == ProductNewStateEnum.edit.getIntValue()) {
            product.setState(ProductNewStateEnum.new_finish.getIntValue());
        }
        else if (state == ProductNewStateEnum.audit_no_pass.getIntValue() ||
                state == ProductNewStateEnum.wait_up_sold.getIntValue() ||
                state == ProductNewStateEnum.down_sold.getIntValue() ||
                state == ProductNewStateEnum.up_sold.getIntValue()) {//审核不通过或待上架或下架或上架编辑后则直接改为待提审
            //验证商品是否需要提交审核
            needSubmitAudit = detailImagesAndVideoUrlChanged(oldProductDetail, detailImgs, videoUrl);
            if (needSubmitAudit) {
                product.setState(ProductNewStateEnum.wait_submit_audit.getIntValue());
                logger.info("第三步改变了需要审核项商品状态改为待提审**********************************");
                productNewService.waitProduct(productId);
            }
        }
        //编辑时审核开关打开，打开后需要提交审核后才能上架
//		product.setNeedAudit(ProductNew.needAudit_true);
        productNewMapper.updateById(product);

        // 修改详情表
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(oldProductDetail.getId());
        productDetail.setVideoUrl(videoUrl);
        productDetail.setVideoName(videoName);
        productDetail.setVideoFileId(videoFileId);
        productDetail.setDetailImgs(detailImgs);
        productDetail.setUpdateTime(curr);
        if (! isOnShelvesNoChangeKeyProp(oldProduct, needSubmitAudit)) {
            productDetail.setPutawayType(putawayType);
            productDetail.setTimingPutwayTime(timingPutwayTime);
        }
        productDetailMapper.updateById(productDetail);

        // 定时上架
        updOnShelves(oldProductDetail, productDetail, oldProduct, needSubmitAudit);
    }
*/

}
