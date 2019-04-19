/**
 * 
 */
package com.jiuy.web.controller.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;

import com.jiuy.core.meta.product.ProductVO;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.PropertyService;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductV1;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.wrap.Specification;
import com.jiuyuan.util.anno.VersionControl;
import com.store.entity.ProductCategoryVO;
import com.store.entity.ProductPropVO;

/**
 * @author LWS
 */
public class ProductVersionTransformUtil<T, K> {

    private static Logger logger = Logger.getLogger(ProductVersionTransformUtil.class);
    

    private static PropertyService propertyService;
    private static CategoryService categoryService;
    
    private static List<String> _supportedSource;
    static {
        _supportedSource = new ArrayList<String>();
        _supportedSource.add("1.0.0.0");
    }

    private static List<String> _supportedDestination;
    static {
        _supportedDestination = new ArrayList<String>();
        _supportedDestination.add("2.0.0.0");
    }
    
    public static void  init(PropertyService propertySer,
    CategoryService categoryServ){
        propertyService = propertySer;
        categoryService = categoryServ;
    }
    
    private static final String LINE_SEP = "\\|";
    
    public static <T, K> Object transformProperties(T source, K dest) {
        if (_supportedSource.contains(getProductVersion(source.getClass())) &&
            _supportedDestination.contains(getProductVersion(dest.getClass()))) {
            // 执行转换
            if ("1.0.0.0".equals(getProductVersion(source.getClass())) &&
                "2.0.0.0".equals(getProductVersion(dest.getClass()))) {
                ProductVO destPVO = new ProductVO();
                Product destP = (Product) dest;
                List<ProductSKUVO> productSKUs = null;
                List<ProductPropVO> propList = null;
                List<ProductCategoryVO> productCates = null;
                List<ProductPropVO> propListFromSKU = new ArrayList<ProductPropVO>();
                ProductV1 sourceP = (ProductV1) source;
                if (null != sourceP && null != destP) {
                    // 转换产品基本属性
                    destP.setAssessmentCount(0);
                    /****delete on 2015/08/91 by LiuWeisheng ****************/
                    /*destP.setCategoryId(ProductV2Util.getProdcutCategoryId(StringUtils.split(
                        sourceP.getClassification(), "\\|")[1]));*/
                    destP.setCreateTime(new Date().getTime());
                    destP.setDetailImages(sourceP.getDetailImages());
                    destP.setExpressDetails(sourceP.getExpressDetails());
                    destP.setExpressFree(sourceP.getExpressFree());
                    destP.setFavorite(0);
                    destP.setName(sourceP.getTitle());
                    destP.setPrice(sourceP.getPrice());
                    destP.setSaleCurrencyType(0);
                    destP.setSaleEndTime(sourceP.getSaleEndTime());
                    destP.setSaleMonthlyMaxCount(0);
                    destP.setSaleStartTime(sourceP.getSaleStartTime());
                    destP.setSaleTotalCount(0);
                    destP.setStatus(sourceP.getStatus());
                    destP.setUpdateTime(new Date().getTime());
                    destP.setSummaryImages(sourceP.getDescription());
                    destP.setDetailImages(sourceP.getDetailImages());
                    destP.setRemark(sourceP.getAttributeComment());
                    destP.setMarketPrice(sourceP.marketPrice);
                    destP.setSizeTableImage(sourceP.getSizeTableImage());
                    destP.setClothesNumber(sourceP.getClothesNumber());
                    destP.setPromotionImage(sourceP.getPromotionImage());
                    destP.setWeight(sourceP.getWeight());
                    destP.setBrandId(sourceP.getBrandId());
                    destP.setShowStatus(sourceP.getShowStatus());
                    destP.setBottomPrice(sourceP.getBottomPrice());
                    destP.setMarketPriceMin(sourceP.getMarketPriceMin());
                    destP.setMarketPriceMax(sourceP.getMarketPriceMax());
                    /*app 1.7*/
                    destP.setCash(sourceP.getCash());
                    destP.setJiuCoin(sourceP.getJiuCoin());
                    destP.setRestrictHistoryBuy(sourceP.getRestrictHistoryBuy());
                    destP.setRestrictDayBuy(sourceP.getRestrictDayBuy());
                    destP.setPromotionSetting(sourceP.getPromotionSetting());
                    destP.setPromotionCash(sourceP.getPromotionCash());
                    destP.setPromotionJiuCoin(sourceP.getPromotionJiuCoin());
                    destP.setPromotionStartTime(sourceP.getPromotionStartTime());
                    destP.setPromotionEndTime(sourceP.getPromotionEndTime());
                    destP.setRestrictCycle(sourceP.getRestrictCycle());
                    destP.setRestrictDayBuyTime(sourceP.getRestrictDayBuyTime());
                    destP.setRestrictHistoryBuyTime(sourceP.getRestrictHistoryBuyTime());
                    destP.setlOWarehouseId(sourceP.getlOWarehouseId());
                    
                    destPVO.setProduct(destP);
                    
                    // 转换产品SKU属性
                    String specificationStr = sourceP.getSpecifications();
                    if(validateSpecification(specificationStr)){
                        List<Specification> specifications = doTransform(specificationStr);
                        productSKUs = makeSKUFromSpecification(specifications,sourceP,propListFromSKU);
                    }
                    
                    // 转换产品基本属性
                    propList = makeBasicPropertiesFromSource(sourceP);
                
                    // 转换产品分类
                    productCates = makeProductCategory(sourceP.getClassification());
                }
                destPVO.setProduct(destP);
                destPVO.setProductSKUs(productSKUs);
                destPVO.setBaseProperties(propList);
                destPVO.setProductCategories(productCates);
                return destPVO;
            }else{
                return null;
            }
        }else{
            return null;
        }
        
    }
    
    /**
     * 构建产品分类列表
     * 
     * @param classification
     * @return
     */
    private static List<ProductCategoryVO> makeProductCategory(String classification){
        if(StringUtils.isBlank(classification)){
            return new ArrayList<ProductCategoryVO>(0);
        }
        List<ProductCategoryVO> cates = new ArrayList<ProductCategoryVO>();
        String[] categories = StringUtils.split(classification, LINE_SEP);
        for(String category : categories){
            Category categoryObj =  categoryService.getCategoryById(Long.parseLong(category));
            ProductCategoryVO pcVO = new ProductCategoryVO();
            pcVO.setCategory(categoryObj);
            pcVO.setCategoryId(categoryObj.getId());
            pcVO.setCreateTime(new Date().getTime());
            pcVO.setStatus((short)0);
            pcVO.setUpdateTime(new Date().getTime());
            cates.add(pcVO);
        }
        return cates;
    }

    /**
     * 构建产品基础属性
     * 
     * @param sourceP
     * @return
     */
    private static List<ProductPropVO> makeBasicPropertiesFromSource(ProductV1 sourceP) {
        // brand 
        Class<? extends ProductV1> productV1Class = sourceP.getClass();
        Field[] fields = productV1Class.getDeclaredFields();
        logger.debug(fields.length);
        List<ProductPropVO> basicProperties = new ArrayList<ProductPropVO>();
        int orderIndex = 0;
        for(Field f : fields){
            logger.debug("field name:" + f.getName());
            if(f.getName().equals("style") || f.getName().equals("element") || f.getName().equals("brand") ||  f.getName().equals("material") || f.getName().equals("format")){
                String value = null;
                try {
                    Object _objValue = f.get(sourceP);
                    if(null == _objValue){
                        continue;
                    }
                    value = _objValue.toString();
                    logger.debug(value);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                String[] valueList = StringUtils.split(value, LINE_SEP);
                for(String properpValueStr : valueList){
                    ProductPropValue property = propertyService.getPropertyValue(properpValueStr);
                    if(null != property){
                        orderIndex += 1;
                        ProductPropVO prodPropVO = new ProductPropVO();
                        prodPropVO.setPropertyNameId(property.getPropertyNameId());
                        prodPropVO.setPropertyValueId(property.getId());
                        prodPropVO.setIsSKU(0);
                        prodPropVO.setOrderIndex(orderIndex);
                        basicProperties.add(prodPropVO);
                    }
                }
            }
            /****delete on 2015/08/91 by LiuWeisheng ****************/
            /*else if(f.getName().equals("classification") ){
                orderIndex += 1;
                String value = null;
                try {
                    Object _objValue = f.get(sourceP);
                    value = _objValue.toString();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                ProductPropValue property = propertyService.getPropertyValue(StringUtils.split(value, "\\|")[2]);
                if(null != property){
                    ProductPropVO prodPropVO = new ProductPropVO();
                    prodPropVO.setPropertyNameId(property.getPropertyNameId());
                    prodPropVO.setPropertyValueId(property.getId());
                    prodPropVO.setIsSKU(0);
                    prodPropVO.setOrderIndex(orderIndex);
                    basicProperties.add(prodPropVO);
                }
            }*/else if(f.getName().equals("season") ){
                String value = null;
                try {
                    Object _objValue = f.get(sourceP);
                    value = _objValue.toString();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                String[] valuesYearAndSeason = StringUtils.split(value, "\\|");
                String year = valuesYearAndSeason[0];
                ProductPropValue propertyYear = propertyService.getPropertyValue(year);
                if(null != propertyYear){
                    ProductPropVO prodPropVO = new ProductPropVO();
                    prodPropVO.setPropertyNameId(propertyYear.getPropertyNameId());
                    prodPropVO.setPropertyValueId(propertyYear.getId());
                    prodPropVO.setIsSKU(0);
                    prodPropVO.setOrderIndex(orderIndex);
                    basicProperties.add(prodPropVO);
                }
                orderIndex += 1;
                String seasonStr = valuesYearAndSeason[1];
                ProductPropValue propertySeason = propertyService.getPropertyValue(seasonStr);
                if(null != propertySeason){
                    ProductPropVO prodPropVO = new ProductPropVO();
                    prodPropVO.setPropertyNameId(propertySeason.getPropertyNameId());
                    prodPropVO.setPropertyValueId(propertySeason.getId());
                    prodPropVO.setIsSKU(0);
                    prodPropVO.setOrderIndex(orderIndex);
                    basicProperties.add(prodPropVO);
                }
                orderIndex += 1;
            }
        }
        return basicProperties;
    }

    /**
     * 构建产品SKU属性
     * 
     * @param specifications
     * @param source
     * @param propListFromSKU
     * @return
     */
    private static List<ProductSKUVO> makeSKUFromSpecification(List<? extends Specification> specifications,ProductV1 source, List<ProductPropVO> propListFromSKU) {
        if (null == specifications) {
            return new ArrayList<ProductSKUVO>(0);
        }
        List<ProductSKUVO> skuList = new ArrayList<ProductSKUVO>();
        for (Specification specification : specifications) {
            ProductSKUVO prodSKU = new ProductSKUVO();
            Class<? extends Specification> specificationClass = specification.getClass();
            Field[] fields = specificationClass.getDeclaredFields();
            logger.debug(fields.length);
            List<ProductPropVO> skuProperties = new ArrayList<ProductPropVO>();
            String propertyIds = "";
            int orderIndex = 0;
            for(Field f : fields){
                if(f.getName().equals("size") || f.getName().equals("color")){
                    String value = null;
                    try {
                        Object _objValue = f.get(specification);
                        value = _objValue.toString();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ProductPropValue property = propertyService.getPropertyValue(value);
                    if(null != property){
                        ++ orderIndex;
                        ProductPropVO prodPropVO = new ProductPropVO();
                        prodPropVO.setPropertyNameId(property.getPropertyNameId());
                        prodPropVO.setPropertyValueId(property.getId());
                        if(1 == orderIndex){
                            propertyIds += (property.getPropertyNameId() + ":" + property.getId());
                        }else{
                            propertyIds += (";" + property.getPropertyNameId() + ":" + property.getId());
                        }
                        prodPropVO.setIsSKU(1);
                        prodPropVO.setOrderIndex(orderIndex);
                        skuProperties.add(prodPropVO);
                        /*if(null != propListFromSKU && 0 == count){
                            propListFromSKU.add(prodPropVO);
                        }*/
                    }
                }
            }
            prodSKU.setStatus(0);
            prodSKU.setRemainCount(specification.getAmount());
            long time = new Date().getTime();
            prodSKU.setCreateTime(time);
            prodSKU.setUpdateTime(time);
            prodSKU.setSkuProperties(skuProperties);
            prodSKU.setPropertyIds(propertyIds);
            prodSKU.setPrice(source.getPrice());
            prodSKU.setCash(source.getCash());
            prodSKU.setSkuNo(Long.parseLong(specification.getSkuNo()));
            skuList.add(prodSKU);
        }
        return skuList;
    }

    private static <T> String getProductVersion(Class<? extends Object> productCls) {
        if (null == productCls) {
            return "";
        }
        if (productCls.isAnnotationPresent(VersionControl.class)) {
            return AnnotationUtils.findAnnotation(productCls, VersionControl.class).value();
        } else {
            return "";
        }
    }
    
    private static boolean validateSpecification(String strSpecifications) {
        if (null == strSpecifications) {
            return false;
        }
        List<Specification> specifications = doTransform(strSpecifications);
        if(null != specifications && specifications.size() != 0){
            return true;
        }else{
            return false;
        }
    }
    
    private static List<Specification> doTransform(String strSpecifications){
        List<Specification> specifications = JSONStringUtil.string2ObjectList(strSpecifications, Specification.class);
        return specifications;
    }
}
