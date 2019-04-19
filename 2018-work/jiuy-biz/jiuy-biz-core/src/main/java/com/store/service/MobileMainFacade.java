/**
 *
 */
package com.store.service;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.service.common.ICategoryNewService;
import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.util.BizUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductTagNewMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.SmallPage;
import com.store.entity.ProductVOShop;

/**
 * @author LWS
 */
@Component
public class MobileMainFacade{

    private static final Logger logger = Logger.getLogger(MobileMainFacade.class);


    @Autowired
    private ProductTagNewMapper productTagNewMapper;

    @Autowired
    private ICategoryNewService categoryNewService;

    @Autowired
    private ProductNewMapper productNewMapper;

    @Autowired
    private IProductNewService productNewService;


    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private TagService tagService;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private IProductMonitorService productMonitorService;

    /**
     * 获取2.3版本开始使用的首页标签商品
     *
     * @param tagId
     * @param storeId
     * @param pageQuery
     * @return
     */
    public Map<String, Object> getMobileMainTagsAndProducts(long tagId, long storeId, PageQuery pageQuery) {
//    	logger.info("首页标签商品tagId:"+tagId+",storeId:"+storeId+",pageQuery"+pageQuery);
        Map<String, Object> data = new HashMap<String, Object>();
        List<Tag> tagList = tagService.getTopTagList();
        if (tagList.size() <= 0) {
            return data;
        }
        if (tagId == 0) {
            tagId = tagList.get(0).getId();
        }
        memcachedService.remove(MemcachedKey.GROUP_KEY_MOBILE_MAIN_TAGS_PRODUCTS, tagId + storeId + pageQuery.getPage() + pageQuery.getPageSize() + "");
        Object obj = memcachedService.get(MemcachedKey.GROUP_KEY_MOBILE_MAIN_TAGS_PRODUCTS, tagId + storeId + pageQuery.getPage() + pageQuery.getPageSize() + "");
        if (obj != null) {
            return (Map<String, Object>) obj;
        }
        List<ProductVOShop> productList = productService.getTagProducts(tagId, storeId, pageQuery);
        data.put("tagList", tagList);
        data.put("productList", productList);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, productService.getTagProductsCount(tagId, storeId));
        data.put("pageQuery", pageQueryResult);
        memcachedService.set(
                MemcachedKey.GROUP_KEY_MOBILE_MAIN_TAGS_PRODUCTS, tagId + storeId + pageQuery.getPage() + pageQuery.getPageSize() + "", DateConstants.SECONDS_FIVE_MINUTES, data);
        return data;
    }

    /**
     * 通过类目，标签，品牌id，及 (筛选上架商品)
     *
     * @param groupType     14 是通过标签查询,4是类目查询 8是品牌查询
     * @param targetId      是目标id  groupType是14 则表示是标签id 4标识是类目id 8表示是品牌id
     * @param current       当前页面
     * @param size          页面数量
     * @param brandIds      品牌ids
     * @param dynamicPotoId 动态属性id
     * @date: 2018/5/10 16:42
     * @author: Aison
     */
    public SmallPage getProductListByGroupId(int groupType, int targetId, int current, int size, String brandIds, String dynamicPotoId) {


        //14:标签
        if (groupType == 14) {
            SmallPage smallPage = new SmallPage(current, size);
            List<ProductNew> productList = productNewService.getProductNewListByTagId(smallPage.getLimit(), smallPage.getOffset(), targetId);
            int total = productNewService.getProductNewListByTagIdCount(targetId);
            smallPage.setDate(total, buildMap(productList));
            return smallPage;
        }

        Page page = new Page<ProductNew>(current, size);
        Wrapper<ProductNew> wrapper = new EntityWrapper<>();
        //未删除
        wrapper.eq("Status", 0);
        //未删除
        wrapper.eq("delState", 0);
        //上架状态，商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
        wrapper.eq("state", ProductNewStateEnum.up_sold.getIntValue());
        //4：类目
        if (groupType == 4) {
            CategoryNew category = categoryNewService.getById(targetId);
            if (category == null) {
                return new SmallPage(current, size);
            }

            // 如果动态属性为空
            if (BizUtil.hasEmpty(dynamicPotoId)) {

                /* 分类等级:0:未知;1:一级;2:二级;3:三级; */
                switch (category.getCategoryLevel()) {
                    case 1:
                        wrapper.eq("oneCategoryId", targetId);
                        break;
                    case 2:
                        wrapper.eq("twoCategoryId", targetId);
                        break;
                    case 3:
                        wrapper.eq("threeCategoryId", targetId);
                        break;
                    default:
                }

                //如果有品牌id
                if (BizUtil.isNotEmpty(brandIds)) {
                    String[] ids = brandIds.split(",");
                    wrapper.in("BrandId", ids);
                }
                //先按照排名排序，再上架时间排序
                wrapper.orderBy("upSoldTime", false);
                List<ProductNew> productList = productNewMapper.selectPage(page, wrapper);
                page.setRecords(productList);
                SmallPage smallPage = new SmallPage(page);

                List<Map<String, Object>> maps = buildMap(page.getRecords());
                productMonitorService.fillProductMonitor(maps,"productId");

                smallPage.setRecords(maps);
                return smallPage;
            } else {
                List<Map<String, Object>> listParam = new ArrayList<>(4);
                String[] dyIds = dynamicPotoId.split(",");
                List<String> brandIdArray = null;
                if (BizUtil.isNotEmpty(brandIds)) {
                    brandIdArray = Arrays.asList(brandIds.split(","));
                    brandIdArray = new ArrayList<>(brandIdArray);
                }

                for (String dyId : dyIds) {
                    Map<String, Object> param = new HashMap<>(3);
                    String[] dyname = dyId.split("_");
                    param.put("state", 6);
                    param.put("dynPropId", dyname[0]);
                    param.put("dynPropValId", dyname[1]);

                    /* 分类等级:0:未知;1:一级;2:二级;3:三级; */
                    switch (category.getCategoryLevel()) {
                        case 1:
                            param.put("oneCategoryId", targetId);
                            break;
                        case 2:
                            param.put("twoCategoryId", targetId);
                            break;
                        case 3:
                            param.put("threeCategoryId", targetId);
                            break;
                        default:
                    }

                    if (brandIdArray != null) {
                        param.put("brandIds", brandIdArray);
                    }
                    listParam.add(param);
                }


                page.setRecords(productNewMapper.selectByDynamics(page, listParam));

                // 如果动态属性不为空
                SmallPage smallPage = new SmallPage(page);
                List<Map<String, Object>> maps = buildMap(page.getRecords());

                productMonitorService.fillProductMonitor(maps,"productId");
                smallPage.setRecords(maps);
                return smallPage;
            }

        } else if (groupType == 8) {
            //8：品牌
            wrapper.eq("BrandId", targetId);
            //先按照排名排序，再上架时间排序
            wrapper.orderBy("rank", true).orderBy("upSoldTime", false);
            List<ProductNew> productList = productNewMapper.selectPage(page, wrapper);
            page.setRecords(productList);
            SmallPage smallPage = new SmallPage(page);
            List<Map<String, Object>> maps = buildMap(page.getRecords());
            productMonitorService.fillProductMonitor(maps,"productId");
            smallPage.setRecords(maps);
            return smallPage;
        } else {
            logger.info("未知类型，请排除问题！");
            return null;
        }
    }


    private List<Map<String, String>> buildMap(List<ProductNew> productList) {
        List<Map<String, String>> productMapList = new ArrayList<>();
        for (ProductNew productNew : productList) {
            Map<String, String> map = new HashMap<>(6);
            //商品ID
            map.put("productId", String.valueOf(productNew.getId()));
            //商品名称
            map.put("name", productNew.getName());
            //主图
            map.put("mainImg", productNew.getMainImg());
            //角标图片
            map.put("badgeImage", productNew.getBadgeImage());
            map.put("minLadderPrice", String.valueOf(productNew.getMinLadderPrice()));
            String vedioMian = productNew.getVedioMain();
            map.put("vedioMain", vedioMian);
            Integer saleCount = productNew.getSaleTotalCount();
            map.put("saleTotalCount",saleCount == null? "0":saleCount.toString() );
            productMapList.add(map);
        }
        return productMapList;
    }


}