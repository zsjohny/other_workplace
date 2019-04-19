package com.store.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.dao.mapper.supplier.WxaMemberKeywordMapper;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.PriceUtil;
import com.jiuyuan.util.SmallPage;
import com.store.dao.mapper.CategoryMapper;
import com.store.dao.mapper.ShopTagMapper;
import com.store.entity.ShopCategory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Administrator
 */
@Service
public class WxaHomePageShopProductNewFacade {
	private static final Logger logger = LoggerFactory.getLogger(WxaHomePageShopProductNewFacade.class);
	@Autowired
	private MemcachedService memcachedService;
	@Autowired
	private ShopProductService shopProductService;

	@Autowired
	private ShopProductMapper shopProductMapper;

    @Autowired
	private WxaMemberKeywordMapper wxaMemberKeywordMapper;
	@Autowired
	private CategoryMapper categoryMapper;



	@Autowired
	private ProductNewMapper productNewMapper;

	@Autowired
	private ShopTagMapper shopTagMapper;

	@Autowired
	private IMyStoreActivityService myStoreActivityService;

	/**
	 * //1、根据标签获取商家商品列表
	 *
	 */
	private List<ShopProduct> getShopProductListByCategoryId(Integer categoryId, Page<ShopProduct> page,
			int storeBusinessVip,long storeId) {
		Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("sold_out",ShopProduct.sold_out_up).eq("store_id", storeId).orderBy("ground_time", false);

		//1、获取所有上架商家商品列表
		List<ShopProduct> onShelfShopProductList = shopProductMapper.selectList(wrapper);

		//1、获取平台商品ID Map集合，key：商品Id，value:原始位置角标
		Map<Long,Integer> productIdIndexMap = new HashMap<Long,Integer>();
		for(int i=0;i<onShelfShopProductList.size();i++){
			ShopProduct shopProduct = onShelfShopProductList.get(i);
			if(shopProduct.getOwn()==0){
				long productId = shopProduct.getProductId();
				productIdIndexMap.put(productId, i);
			}
		}

		//2、获取平台商品列表列表
		long c = System.currentTimeMillis();
		List<Long> productIdList = new ArrayList<Long>(productIdIndexMap.keySet());
//		List<ProductNew> platformProductNewList = productNewMapper.selectBatchIds(productIdList);
        List<ProductNew> platformProductNewList = new ArrayList<ProductNew>();
        if (productIdList.size() > 0) {
            platformProductNewList = productNewMapper.selectBatchIds(productIdList);
        }


        long d = System.currentTimeMillis();
        System.out.println((d - c));

        onShelfShopProductList = fillPlatformShopProductInfo(onShelfShopProductList, platformProductNewList, productIdIndexMap);

        // 2、获取分类ID及其子集分类ID集合
//		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
//		if (categoryId != 0) {
//			shopCategoryList = getShopCategoryList(categoryId);
//		}

        // 3、剔除商品
//		List<Product> shopProductPlatformProductList = getShopProductPlatformProductListCache(storeId);
//		Map<String, List<Long>> shopProductPlatformProductCategoryIdMap = getShopProductPlatformProductCategoryIdMapCache(storeId);
//		logger.info("=============shopProductPlatformProductCategoryIdMap"+shopProductPlatformProductCategoryIdMap.toString());
        for (int i = onShelfShopProductList.size() - 1; i >= 0; i--) {

            ShopProduct shopProduct = onShelfShopProductList.get(i);
            // 是否是自有商品：1是自有商品，0平台商品
            int own = shopProduct.getOwn();
            if (own == ShopProduct.platform_product) {// 平台商品
                long productId = shopProduct.getProductId();
//				Product product = productService.getProductFromCache(productId);
                ProductNew product = getProductFromPlatformProductList(productId, storeId, platformProductNewList);
                int productVip = product.getVip();
                // //2、根据商家VIP身份剔除VIP商品
                if (storeBusinessVip == 0 && productVip == 1) {// 非VIP身份
                    onShelfShopProductList.remove(i);
                } else {
//					if (categoryId != 0) {
//						List<Long> categoryIds = getShopProductPlatformProductCategoryId(productId,storeId,shopProductPlatformProductCategoryIdMap);
////						logger.info("=============productId:"+productId+",storeId:"+storeId+",categoryIds"+categoryIds.toString());
//						// 不符合该分类则移除
//						if (!checkCategoryId(shopCategoryList, categoryIds)) {
//							onShelfShopProductList.remove(i);
//						}
//					}
                }
            } else {// 自有商品
//				if (categoryId != 0) {
//					List<Long> categoryIds = new ArrayList<Long>();
//					categoryIds.add(shopProduct.getCategoryId());
//					// 不符合该分类则移除
//					if (!checkCategoryId(shopCategoryList, categoryIds)) {
//						onShelfShopProductList.remove(i);
//					}
//				}
            }
        }

        // 3、分页
        // 每页开始数量
        int fromIndex = page.getOffset();
//		logger.info("获取小程序首页商品列表，fromIndex："+fromIndex);
        // 每页结束数量
        int toIndex = fromIndex + page.getLimit();
//		logger.info("获取小程序首页商品列表，toIndex："+toIndex);
        // 剩余最大数量
        int maxIndex = onShelfShopProductList.size();
//		logger.info("获取小程序首页商品列表，maxIndex："+maxIndex);
        if (maxIndex <= toIndex) {
            toIndex = maxIndex;
        }
//		logger.info("获取小程序首页商品列表，toIndex："+toIndex);

        // list截取含头不含尾
        List<ShopProduct> pageList = onShelfShopProductList.subList(fromIndex, toIndex);
        return pageList;
    }


    /**
     * 组装平台商品列表
     *
     * @param onShelfShopProductList
     * @param platformProductNewList
     * @param productIdIndexMap
     * @return
     */
    private List<ShopProduct> fillPlatformShopProductInfo(List<ShopProduct> onShelfShopProductList, List<ProductNew> platformProductNewList, Map<Long, Integer> productIdIndexMap) {
//		logger.info("productIdIndexMap:"+JSON.toJSONString(productIdIndexMap));
        Set<Long> productIds = productIdIndexMap.keySet();
        for (long productId : productIds) {
            //1、商家商品
            int index = productIdIndexMap.get(productId);
            ShopProduct shopProduct = onShelfShopProductList.get(index);

            //2、平台商品
            ProductNew productNew = null;
            for (ProductNew product : platformProductNewList) {
                long id = product.getId();
                if (id == productId) {
                    productNew = product;
                }
            }

            //3、填充数据
            shopProduct.setName(productNew.getName());
            shopProduct.setXprice(productNew.getWholeSaleCash());
//			shopProduct.setMarketPrice(productNew.getMarketPrice().doubleValue());
            shopProduct.setClothesNumber(productNew.getClothesNumber());
            shopProduct.setVideoUrl(productNew.getVideoUrl());
            shopProduct.setCategoryId(productNew.getCategoryId());
//			List<ProductSKU> productSKUList = productSKUMapper.getAllProductSKUsOfProduct(productId);
//			List<ProductSkuNew> productSkuNewList = productSkuNewMap.get(productId);
//			int stock = 0;
            String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
            int state = productNew.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            if (state == ProductNewStateEnum.up_sold.getIntValue()) {
                platformProductState = "0";
            }

//			if(productSkuNewList!=null){
//				for (ProductSkuNew productSkuNew : productSkuNewList) {
//					if (productSkuNew == null) {
//						continue;
//					}
//					if (stock == 0) {
//						stock = productSkuNew.getRemainCount();
//					} else if (productSkuNew.getRemainCount() < stock) {
//						stock = productSkuNew.getRemainCount();
//					}
//					if(productSkuNew.getOnSaling()){
//						platformProductState = "0";
//					}
//				}
//			}
//			shopProduct.setPlatformProductState(productSKUService.getPlatformProductState(productId));
            shopProduct.setPlatformProductState(platformProductState);
            shopProduct.setStock(Long.parseLong("0"));//库存首页获取数据用不到直接填0
            shopProduct.setSummaryImages(productNew.getDetailImages());

            String sizeTableImage = productNew.getSizeTableImage();
            String detailImages = productNew.getSummaryImages();
            if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
                detailImages = productNew.getDetailImages();
            }
            String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
            shopProduct.setRemark(remark);
            if (shopProduct.getStock() > 0) {
                shopProduct.setStockTime(System.currentTimeMillis());
            }

        }


        return onShelfShopProductList;
    }


    /**
     * 根据商品ID获取商品分类ID集合
     *
     * @param productId
     * @return
     */
    private List<Long> getCategoryIdsByProductIdCache(long productId) {
        List<Long> categoryIds = new ArrayList<Long>();
        String groupKey = MemcachedKey.GROUP_KEY_categoryIdsByProductId;
        String key = String.valueOf(productId);
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
//			logger.info("从缓存获取商家商家商品的分类列表obj："+obj);
            categoryIds = (List<Long>) obj;
//			logger.info("----------------------------------------从缓存获取根据商品ID获取商品分类ID集合categoryIds.size："+categoryIds.size()+",categoryIds.size():"+categoryIds.size());
        } else {
            categoryIds = shopProductMapper.getCategoryIdsByProductId(productId);
            int time = DateConstants.SECONDS_PER_DAY;
            memcachedService.set(groupKey, key, time, categoryIds);
//			logger.info("-------------------------------------------从数据库获取根据商品ID获取商品分类ID集合完成,缓存时间time:"+time+",categoryIds.size()："+categoryIds.size());
        }

        return categoryIds;
    }

    /**
     * 从列表中找商品
     *
     * @param productId
     * @param storeId
     * @return
     */
    private ProductNew getProductFromPlatformProductList(long productId, long storeId, List<ProductNew> platformProductNewList) {
        for (ProductNew product : platformProductNewList) {
            if (product.getId() == productId) {
                return product;
            }
        }
        logger.info("没有从缓存的平台商品列表中找到该商品，请排查问题，productId：" + productId);
        return null;
//		return productService.getProductFromCache(productId);
    }

//	/**
//	 * 获取分类IDMap缓存
//	 * @param storeId
//	 * @return
//	 */
//	private Map<String, List<Long>> getShopProductPlatformProductCategoryIdMapCache(long storeId) {
//		//1、获取在架的商家商品列表
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeId);
//		Map<String, List<Long>> categoryIdsMap = new HashMap<String,List<Long>>();
//		String groupKey = MemcachedKey.GROUP_KEY_categoryIdsMapByProductId;
//		String key = String.valueOf(storeId);
//		Object obj = memcachedService.get(groupKey, key);
//		if (obj != null) {
////			logger.info("从缓存获取商家商家商品的分类列表obj："+obj);
//			categoryIdsMap = (Map<String, List<Long>>) obj;
////			logger.info("----------------------------------------从缓存获取分类Id集合categoryIdsMap："+categoryIdsMap);
//		}else{
//			for (ShopProduct shopProduct : onShelfShopProductList) {
//				// 是否是自有商品：1是自有商品，0平台商品
//				int own = shopProduct.getOwn();
//				if (own == ShopProduct.platform_product) {// 平台商品
//					long productId = shopProduct.getProductId();
//					categoryIdsMap.put(storeId+"_"+productId,getCategoryIdsByProductIdCache(productId));
//				}
//			}
//			int time = DateConstants.SECONDS_PER_DAY;
//			memcachedService.set(groupKey, key,time , categoryIdsMap);
////			logger.info("-------------------------------------------从数据库获取分类Id集合categoryIdsMap：:"+categoryIdsMap.toString());
//		}
//		return categoryIdsMap;
//	}


//	/**
//	 * 从缓存一次性获取商家商品中平台商品集合
//	 * @param storeId
//	 * @return
//	 */
//	private List<Product> getShopProductPlatformProductListCache(long storeId) {
//		//1、获取在架的商家商品列表
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeId);
//
//		//2、填充商品
//		List<Product> platformProductList = new ArrayList<Product>();
//		String groupKey = MemcachedKey.GROUP_KEY_platformProductList;
//		String key = String.valueOf(storeId);
//		Object obj = memcachedService.get(groupKey, key);
//		if (obj != null) {
//			platformProductList = (List<Product>) obj;
////			logger.info("********************************从缓存一次性获取商家商品中平台商品集合完成，platformProductList.size():"+platformProductList.size());
//		}else{
//			for (ShopProduct shopProduct : onShelfShopProductList) {
//				// 是否是自有商品：1是自有商品，0平台商品
//				int own = shopProduct.getOwn();
//				if (own == ShopProduct.platform_product) {// 平台商品
//					long productId = shopProduct.getProductId();
//					platformProductList.add(productService.getProductFromCache(productId));
//				}
//			}
//			int time = DateConstants.SECONDS_PER_DAY;
//			memcachedService.set(groupKey, key,time , platformProductList);
//			logger.info("************************************从数据库获取商家商品中平台商品集合完成并存入缓存，platformProductList.size():"+platformProductList.size());
//		}
//		return platformProductList;
//	}

    /**
     * 获取分类ID及其子集分类ID集合
     *
     * @param categoryId
     * @return
     */
    private List<ShopCategory> getShopCategoryList(Integer categoryId) {
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        String groupKey = MemcachedKey.GROUP_KEY_ShopCategoryList;
        String key = String.valueOf(categoryId);
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            logger.info("从缓存中获取获取分类ID及其子集分类ID集合obj：" + obj);
            shopCategoryList = (List<ShopCategory>) obj;
        } else {
            List<Long> categoryIdListFirst = new ArrayList<Long>();
            categoryIdListFirst.add((long) categoryId);
            shopCategoryList = categoryMapper.getCategoryByIds(categoryIdListFirst);
            int time = DateConstants.SECONDS_TEN_MINUTES;
            memcachedService.set(groupKey, key, time, shopCategoryList);
            logger.info("从数据库中获取分类ID及其子集分类ID集合完成并存入缓存完成,缓存时间为time：" + time + ",shopCategoryList.size()：" + shopCategoryList.size());
        }
        return shopCategoryList;
    }

    /**
     * 判断商品分类是否符合条件 true 符合、false不符合
     *
     * @param shopCategoryList
     * @param categoryIds
     * @return
     */
    private boolean checkCategoryId(List<ShopCategory> shopCategoryList, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.size() <= 0) {
            return false;
        }
        if (shopCategoryList == null || shopCategoryList.size() <= 0) {
            return false;
        }
        for (Long shopProductCategoryId : categoryIds) {
            if (shopProductCategoryId == null) {
                continue;
            }
            for (ShopCategory shopCategory : shopCategoryList) {
                if (shopCategory.getId() == shopProductCategoryId) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getKeywordList(String keyword) {
        List<String> keywordList = new ArrayList<String>();
        String[] keywordArr = keyword.split(" ");
        for (String word : keywordArr) {
            if (StringUtils.isNotEmpty(word)) {
                keywordList.add(word);
            }
        }
        return keywordList;
    }

    private void addMemberKeyword(String memberKeyword, long storeId, long memberId) {
        Wrapper<WxaMemberKeyword> wrapper = new EntityWrapper<WxaMemberKeyword>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("store_id", storeId);
        wrapper.eq("keyword", memberKeyword);
        wrapper.orderBy("create_time", false);
        List<WxaMemberKeyword> wxaMemberKeywordList = wxaMemberKeywordMapper.selectList(wrapper);
        if (wxaMemberKeywordList.size() == 0) {
            WxaMemberKeyword wxaMemberKeyword = new WxaMemberKeyword();
            wxaMemberKeyword.setStoreId(storeId);
            wxaMemberKeyword.setMemberId(memberId);
            wxaMemberKeyword.setKeyword(memberKeyword);
            wxaMemberKeyword.setCreateTime(System.currentTimeMillis());
            wxaMemberKeywordMapper.insert(wxaMemberKeyword);
            logger.info("添加搜索词成功，memberKeyword：" + memberKeyword);
        } else {
            logger.info("搜索词已经存在，memberKeyword：" + memberKeyword);
        }


    }


    /**
     * 搜索商品
     */
    public SmallPage searchProductList(String keyword, Page<ShopProduct> page, long storeId,long memberId) {
        List<String> keywordList = getKeywordList(keyword);
        if(memberId > 0){
            for(String word : keywordList){
                addMemberKeyword(word,storeId,memberId);
            }
        }

        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>()
                .eq("status", 0)
                .eq("sold_out",ShopProduct.sold_out_up)
                .eq("store_id", storeId);
//		String nameSql = "";
        if(keywordList.size() == 1){
            String word = keywordList.get(0);
            wrapper.like("name", word);
        }else{
//			String word = keywordArr[0];
//			wrapper.andNew("").like("name", word);
//			for(int i = 1;i < keywordArr.length; i++ ){
//				wrapper.or("").like("name", keywordArr[i]);
//			}
            String word = keywordList.get(0);
            String sql = " name like '%"+ word +"%' ";
            for(int i = 1;i < keywordList.size(); i++ ){
                sql = sql + " or name like '%"+ keywordList.get(i) +"%' ";
            }
            logger.info("搜索条件sql："+sql);
            wrapper.and("("+sql+")");
        }
        logger.info("wrapper.toString()"+wrapper.toString());
        List<ShopProduct> shopProductList = shopProductMapper.selectPage(page, wrapper);
        page.setRecords(shopProductList);
        List<Map<String,String>> searchProductList = new ArrayList<>();

        //新的
        //填充供应商平台商品信息
        List<Long> supplierProductIds = new ArrayList<>();
        shopProductList.forEach(shopProduct -> {
            if (shopProduct != null && shopProduct.getOwn() == 0) {
                supplierProductIds.add(shopProduct.getProductId());
            }
        });

        if (! supplierProductIds.isEmpty()) {
            List<ProductNew> productNews = productNewMapper.selectBatchIds(supplierProductIds);
            if (! productNews.isEmpty()) {
                for (ShopProduct shopProduct : shopProductList) {

                    if (shopProduct.getOwn() != 0) {
                        continue;
                    }
                    //重构--填充供应商商品信息
                    fillSupplierProductInfo(productNews, shopProduct);
                }
            }
        }

        for(ShopProduct wxaShopProduct : shopProductList){
            Map<String,String> map = new HashMap<>(6);
            //商家商品ID
            map.put("shopProductId", String.valueOf(wxaShopProduct.getId()));
            //商品标题
            map.put("name", wxaShopProduct.getName());
            //商品主图
            map.put("image", wxaShopProduct.getFirstImage());
            Double price = wxaShopProduct.getPrice();
            //零售价
            if(price == null){
                map.put("price", "");
            }else{
                map.put("price", String.valueOf(price));
            }
            //是否现货：0无现货、1有现货
            if(wxaShopProduct.getStockTime()==0 || wxaShopProduct.getTabType()==1){
                map.put("stock", "0");
            }else{
                map.put("stock", "1");
            }
            searchProductList.add(map);
        }
        SmallPage smallPage = new SmallPage(page);
        smallPage.setRecords(searchProductList);
        return smallPage;
    }






    /**
     * 获取所有商品
     * watch = StopWatch '查询': running time (millis) = 286
     * -----------------------------------------
     * ms     %     Task name
     * -----------------------------------------
     * 00259  091%  老的
     * 00027  009%  新的
     *
     */
    public SmallPage allProductList( Page<ShopProduct> page, long storeId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("sold_out",ShopProduct.sold_out_up).eq("store_id", storeId);
        wrapper.orderBy("ground_time",false);
        List<ShopProduct> shopProductList = shopProductMapper.selectPage(page, wrapper);
        page.setRecords(shopProductList);
        List<Map<String,String>> searchProductList = new ArrayList<>(15);

        //老的
//		for(ShopProduct shopProduct : shopProductList){
//			ShopProduct wxaShopProduct = shopProductService.getShopProductById(shopProduct.getId());
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("shopProductId", String.valueOf(wxaShopProduct.getId()));//商家商品ID
//			map.put("name", wxaShopProduct.getName());//商品标题
//			map.put("image", wxaShopProduct.getFirstImage());//商品主图
//			Double price = wxaShopProduct.getPrice();
//			if(price == null){
//				map.put("price", "");//零售价
//			}else{
//				map.put("price", String.valueOf(price));//零售价
//			}
//			if(wxaShopProduct.getStockTime()==0/* || wxaShopProduct.getTabType()==1*/){
//				map.put("stock", "0");
//			}else{
//				map.put("stock", "1");
//			}
//			searchProductList.add(map);
//		}


        //新的
        //填充供应商平台商品信息
        List<Long> supplierProductIds = new ArrayList<>();
        shopProductList.forEach(shopProduct -> {
            if (shopProduct != null && shopProduct.getOwn() == 0) {
                supplierProductIds.add(shopProduct.getProductId());
            }
        });

        List<ProductNew> productNews;
        if (supplierProductIds.isEmpty()) {
            productNews = new ArrayList<>(0);
        }
        else {
            productNews = productNewMapper.selectBatchIds(supplierProductIds);
        }

        if (! productNews.isEmpty()) {
            for (ShopProduct shopProduct : shopProductList) {

                if (shopProduct.getOwn() != 0) {
                    continue;
                }
                //重构--填充供应商商品信息
                fillSupplierProductInfo(productNews, shopProduct);
            }
        }

        for(ShopProduct shopProduct : shopProductList){
            Map<String,String> map = new HashMap<String,String>();
            //商家商品ID
            map.put("shopProductId", String.valueOf(shopProduct.getId()));
            //商品标题
            map.put("name", shopProduct.getName());
            //商品主图
            map.put("image", shopProduct.getFirstImage());
            Double price = shopProduct.getPrice();
            //零售价
            if(price == null){
                map.put("price", "");
            }else{
                map.put("price", String.valueOf(price));
            }
            map.put("stock", shopProduct.getStockTime() == 0?"0":"1");
            searchProductList.add(map);
        }

        SmallPage smallPage = new SmallPage(page);
        smallPage.setRecords(searchProductList);
        return smallPage;
    }

    /**
     * 重构--填充供应商商品信息
     *
     * @param productNews productNews
     * @param shopProduct shopProduct
     * @author Charlie
     * @date 2019/1/2 21:45
     */
    private void fillSupplierProductInfo(List<ProductNew> productNews, ShopProduct shopProduct) {
        Iterator<ProductNew> pIt = productNews.iterator();
        while (pIt.hasNext()) {
            ProductNew product = pIt.next();
            if (product.getId().equals(shopProduct.getProductId())) {
                shopProduct.setXprice (product.getWholeSaleCash ());
                shopProduct.setClothesNumber (product.getClothesNumber ());
                shopProduct.setVideoUrl (product.getVideoUrl ());

                //获取库存需要查询sku信息非常慢，所有不进行填充，如果发小其他问题请单独获取
                shopProduct.setSummaryImages (product.getDetailImages ());
                String sizeTableImage = product.getSizeTableImage ();
                String detailImages = product.getSummaryImages ();
                if (! StringUtils.isEmpty (detailImages) && JSON.parseArray (detailImages).size () <= 0) {
                    detailImages = product.getDetailImages ();
                }
                String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
                shopProduct.setRemark (remark);

                // 商品状态:0已上架、1已下架、2已删除
                String platformProductState = "1";
                //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
                int state = product.getState ();
                if (state == ProductNewStateEnum.up_sold.getIntValue ()) {
                    platformProductState = "0";
                }
                shopProduct.setPlatformProductState (platformProductState);

                pIt.remove();
            }
        }
    }



    private int getStarNum(Integer start, Integer count) {
        return ((start <= 1 ? 1 : start) - 1) * count;
    }


    /**
     * 小程序首页
     *
     * @param type：类型：0店长推荐、1热销推荐
     * @param page
     * @param
     * @return
     */
    public SmallPage getHomeProductList(int type, Page<ShopProduct> page, Long storeId) {


        List<Long> allIds = shopProductMapper.findAllIdsByStoreIdAndTypes(storeId, type
                , getStarNum(page.getCurrent(), page.getSize()), page.getSize());

        List<Map<String, String>> HomeProductList = new ArrayList<Map<String, String>>();
        for (Long id : allIds) {

            ShopProduct wxaShopProduct = shopProductService.getShopProductById(id);

            Map<String, String> map = new HashMap<String, String>();
            map.put("shopProductId", String.valueOf(wxaShopProduct.getId()));//商家商品ID
            map.put("name", wxaShopProduct.getName());//商品标题
            map.put("image", wxaShopProduct.getFirstImage());//商品主图
            Double price = wxaShopProduct.getPrice();
            if (price == null) {
                map.put("price", "");//零售价
            } else {
                map.put("price", String.valueOf(price));//零售价
            }

            if (wxaShopProduct.getStockTime() == 0) {
                map.put("stock", "0");
            } else {
                map.put("stock", "1");
            }


            if (StringUtils.isNotEmpty(wxaShopProduct.getWxaqrcodeUrl())) {
                map.put("isShare", "1");
            } else {
                map.put("isShare", "0");
            }


            //0未参与活动、1表示参与团购活动、2表示参与秒杀活动
            int intoActivity = myStoreActivityService.getShopProductActivityState2(id, storeId);
            map.put("intoActivity", intoActivity + "");

            if (intoActivity == 1) {
                map.put("activityId", myStoreActivityService.existTeamBuyActivityActivity(wxaShopProduct.getId(), storeId) + "");
            } else if (intoActivity == 2) {
                map.put("activityId", myStoreActivityService.existCurrentSecondBuyActivity(wxaShopProduct.getId(), storeId) + "");
            } else {
                map.put("activityId", "0");
            }

			HomeProductList.add(map);
		}

		SmallPage smallPage = new SmallPage(page);
		smallPage.setRecords(HomeProductList);
		return smallPage;
	}

	/**
	 * 获取小程序首页商品
	 */
	public Map<String, Object> getHomePageProductList(Integer categoryId,Page<ShopProduct> page,
			StoreBusiness storeBusiness) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 3、根据标签获取商家商品列表
		List<ShopProduct> shopProductlist = getShopProductListByCategoryId(categoryId, page, storeBusiness.getVip(),storeBusiness.getId());
		 for(ShopProduct shopProduct:shopProductlist){
			 String name = shopProduct.getName();
			 if(StringUtils.isEmpty(name)){
				 logger.info("商家商品名称为空请排查问题，shopProduct："+JSON.toJSONString(shopProduct));
			 }
		 }
		 List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();
		data.put("categoryList", categoryList);
		if (shopProductlist.size() == 0) {
			data.put("isNoProduct", 0);
			data.put("isMore", page.hasNext());
			data.put("text", "没有任何商品");
		} else {
			List<Map<String, Object>> productList = sortShopProductByTop(categoryId, shopProductlist);
			data.put("productList", productList);
			data.put("isNoProduct", 1);
			data.put("isMore", page.hasNext());
		}
		return data;
	}
	/**
	 * 从缓存获取商家商家商品的分类列表
	 * @param storeId
	 * @param onShelfShopProductList
	 * @return
	 */
	private List<ShopCategory> getSoldOutShopProductCategoryListByCache(long storeId,List<ShopProduct> onShelfShopProductList) {
		List<ShopCategory> soldOutShopProductCategoryList = new ArrayList<ShopCategory>();
		String groupKey = MemcachedKey.GROUP_KEY_shopSoldOutShopProductCategoryList;
		String key = String.valueOf(storeId);
		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
//			logger.info("从缓存获取商家商家商品的分类列表obj："+obj);
			soldOutShopProductCategoryList = (List<ShopCategory>) obj;
			logger.info("----从缓存获取商家商家商品的分类列表soldOutShopProductCategoryList.size："+soldOutShopProductCategoryList.size()+",onShelfShopProductList.size():"+onShelfShopProductList.size());
		}else{
			soldOutShopProductCategoryList = getSoldOutShopProductCategoryList(storeId,onShelfShopProductList);
			int time = DateConstants.SECONDS_PER_DAY;
			memcachedService.set(groupKey, key, time , soldOutShopProductCategoryList);
			logger.info("----从数据库获取商家商家商品的分类列表完成,缓存时间time:"+time+",soldOutShopProductCategoryList.size()："+soldOutShopProductCategoryList.size()+",onShelfShopProductList.size():"+onShelfShopProductList.size());
		}
		return soldOutShopProductCategoryList;
	}

	/**
	 * 获取上架商家商品的分类列表
	 */
	private List<ShopCategory> getSoldOutShopProductCategoryList(long storeId,List<ShopProduct> onShelfShopProductList) {
		List<ShopCategory> soldOutShopProductCategoryList = new ArrayList<ShopCategory>();
		//2、
		for (ShopProduct shopProduct : onShelfShopProductList) {
			List<ShopCategory> categoriesOne = new ArrayList<ShopCategory>();
			// 获取指定商品分类列表
			List<Long> categoryIds = categoryMapper.getCategoriesByProductId(shopProduct.getProductId());
			if (categoryIds.size() > 0) {// 有分类则获取分类
				//递归获取分类下的子分类
				List<ShopCategory> shopCategoryList = categoryMapper.getCategoryByIds(categoryIds);
				soldOutShopProductCategoryList.addAll(getCategories(shopCategoryList, categoriesOne));
			}
		}

        //3、按权重排序
        soldOutShopProductCategoryList.sort(new Comparator<ShopCategory>() {
            @Override
            public int compare(ShopCategory s1, ShopCategory s2) {
                int w1 = s1.getWeight();
                int w2 = s2.getWeight();
                if (w1 > w2) {
                    return -1;
                }
                if (w2 > w1) {
                    return 1;
                }
                return 0;
            }
        });
        // logger.info("小程序首页soldOutShopProductCategoryList:" +
        // soldOutShopProductCategoryList);
        return soldOutShopProductCategoryList;
    }

    private List<Map<String, Object>> sortShopProductByTop(Integer status, List<ShopProduct> shopProductlist) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> topProductList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> unTopProductList = new ArrayList<Map<String, Object>>();
        for (ShopProduct shopProduct : shopProductlist) {
            // logger.error("小程序首页shopProductId:" + shopProduct.getId());
            Map<String, Object> product = new HashMap<String, Object>();
            product.put("id", shopProduct.getId() + "");
            product.put("name", shopProduct.getName());
            product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()));

            // 现货时间为0表示没有现货
            if (shopProduct.getStockTime() == 0 || shopProduct.getTabType() == 1 || status == -998) {
                product.put("isStock", "0");
            } else {
                product.put("isStock", "1");
            }

            product.put("firstImage", shopProduct.getFirstImage());

            // logger.error("wxaHomePage:firstImage:" +
            // shopProduct.getFirstImage());
            product.put("sortTimeMilliSecond", shopProduct.getGroundTime());
            // 上架时间
            product.put("groundTime", shopProduct.getGroundTime());

            if (shopProduct.getTopTime() == 0) {
                topProductList.add(product);
            } else {
                unTopProductList.add(product);
            }
            // productList.add(product);
        }

        // topProductList = sortList(topProductList, "sortTimeMilliSecond");
        // unTopProductList = sortList(unTopProductList, "sortTimeMilliSecond");

        productList.addAll(unTopProductList);
        productList.addAll(topProductList);
        return productList;
    }


    /**
     * 获取父级子级所有的标签Id
     *
     * @param shopCategoryList
     * @param categoryIdListAll
     * @return
     */
    private List<Long> getAllCategoryIds(List<ShopCategory> shopCategoryList, List<Long> categoryIdListAll) {
        if (shopCategoryList.size() > 0) {
            for (ShopCategory shopCategory : shopCategoryList) {
                if (shopCategory == null || categoryIdListAll.contains(shopCategory.getId())) {
                    continue;
                }
                categoryIdListAll.add(shopCategory.getId());
                List<ShopCategory> childCategories = categoryMapper.getCategoriesByParentId(shopCategory.getId());
                categoryIdListAll = getAllCategoryIds(childCategories, categoryIdListAll);
            }
        }
        return categoryIdListAll;
    }

    private List<Map<String, Object>> sortList(List<Map<String, Object>> list, String sortStr) {
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (long) o1.get(sortStr) > (long) o2.get(sortStr) ? -1 : 1;
            }
        });
        return list;
    }


    /**
     * 获取分类ID集合
     *
     * @param productId
     * @param storeId
     * @param shopProductPlatformProductCategoryIdMap
     * @return
     */
    private List<Long> getShopProductPlatformProductCategoryId(long productId, long storeId, Map<String, List<Long>> shopProductPlatformProductCategoryIdMap) {
//		logger.info("=============productId:"+productId+",storeId:"+storeId+",shopProductPlatformProductCategoryIdMap:"+shopProductPlatformProductCategoryIdMap.toString());
        List<Long> categoryIds = shopProductPlatformProductCategoryIdMap.get(storeId + "_" + productId);
//		logger.info("=============productId:"+productId+",storeId:"+storeId+",categoryIds:"+categoryIds.toString());

        if (categoryIds != null) {
            return categoryIds;
        }
        logger.info("没有从缓存的获取分类ID集合，请排查问题，productId：" + productId);
        return getCategoryIdsByProductIdCache(productId);
    }


    private List<Map<String, Object>> getShopCategoryList(List<ShopCategory> soldOutShopProductCategoryList) {
        List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();

        List<Long> categoryIdList = new ArrayList<Long>();
        for (ShopCategory shopCategory : soldOutShopProductCategoryList) {
            Map<String, Object> categoryMap = new HashMap<String, Object>();
            if (!categoryIdList.contains(shopCategory.getId())) {
                categoryMap.put("categoryId", shopCategory.getId());
                categoryMap.put("categoryName", shopCategory.getCategoryName());
                categoryIdList.add(shopCategory.getId());
                categoryList.add(categoryMap);
            }
        }
        // logger.error("小程序首页categoryList:" + categoryList);
        return categoryList;
    }

    /**
     * 获取首页所有类别（递归）
     *
     * @param shopCategoryList
     * @param categoriesNew
     * @return
     */
    private List<ShopCategory> getCategories(List<ShopCategory> shopCategoryList, List<ShopCategory> categoriesNew) {
        for (ShopCategory shopCategory : shopCategoryList) {
            if (shopCategory == null || categoriesNew.contains(shopCategory)) {
                continue;
            } else if (shopCategory.getParentId() == 0) {
                categoriesNew.add(shopCategory);
            } else {
                // List<ShopCategory> childCategories =
                // categoryMapper.getCategoriesByParentId(shopCategory.getId());
                List<Long> parentId = new ArrayList<Long>();
                parentId.add(shopCategory.getParentId());
                if (parentId.size() <= 0) {
                    continue;
                }
                List<ShopCategory> childCategories = categoryMapper.getCategoryByIds(parentId);
                categoriesNew = getCategories(childCategories, categoriesNew);
            }
        }
        return categoriesNew;
    }

    /**
     * 根据导航获取商品列表
     *
     * @param tagId
     * @param page
     * @return
     */
    public Map<String, Object> getNavigationProductList(long tagId, Page<Map<String, Object>> page, Long storeId) {
        List<ShopProduct> shopProductList = shopProductMapper.getNavigationProductList(tagId, storeId, page);

        Map<String, Object> data = new HashMap();
        if (shopProductList.size() > 0) {
            List<Map<String, String>> productList = new ArrayList<Map<String, String>>();
            for (ShopProduct shopProducts : shopProductList) {
                long shopProductId = shopProducts.getId();
                ShopProduct shopProduct = shopProductService.getShopProductInfoNoStock(shopProductId);
                if (shopProduct == null || shopProduct.getStatus() == -1 || shopProduct.getSoldOut() != 1) {
                    continue;
                }
                Map<String, String> product = new HashMap<String, String>();
                product.put("productId", shopProduct.getId() + "");
                product.put("name", shopProduct.getName());
                product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()) + "");

                product.put("image", shopProduct.getFirstImage());
                if (shopProduct.getStockTime() == 0 || shopProduct.getTabType() == 1) {
                    product.put("isStock", "0");
                } else {
                    product.put("isStock", "1");
                }
                product.put("shopProductState", shopProductService.getShopProductState(shopProductId));
                productList.add(product);
            }
            data.put("productList", productList);
            data.put("isNoProduct", 1);
        } else {
            data.put("isNoProduct", 0);
            data.put("text", "没有任何内容");
        }
        ShopTag shopTag = shopTagMapper.selectById(tagId);
        data.put("tagName", shopTag.getTagName());
        return data;
    }


}