package com.jiuyuan.service.common;

import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.JobTaskType;
import com.jiuyuan.service.common.job.SkuJobService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;

import net.sf.json.JSONObject;
import org.springframework.util.ObjectUtils;

import static com.jiuyuan.constant.JobTaskType.*;

/**
 * 新商品SKU服务
 */

@Service
public class ProductSkuNewService implements IProductSkuNewService{
    private static final Logger logger = LoggerFactory.getLogger(ProductSkuNewService.class);
    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;
    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private ProductNewMapper productNewMapper;
    @Autowired
    private SkuJobService skuJobService;


    /**
     * 根据首次上架时间设置定时更新库存时间的最小天数
     *
     * @param lastPutonTime 首次上架时间
     * @return int
     * @auther Charlie(唐静)
     * @date 2018/6/14 19:30
     */
    public static int autoSetRemainCountMinDayStrategy(long lastPutonTime) {
        return (int) ((System.currentTimeMillis() - lastPutonTime) / (24 * 3600 * 1000) + 1);
    }


    /**
     * 修改SKU库存数量
     */
    @Override
    public void updSkuRemainCount(long productId, long skuId, int remainCount, Double weight) {
        ProductSkuNew sku = productSkuNewMapper.selectById(skuId);
        if (sku == null) {
            throw new RuntimeException("商品SKU不存在");
        }
        //更改SKU库存
        ProductSkuNew newSku = new ProductSkuNew();
        newSku.setId(skuId);
        newSku.setWeight(weight);
        newSku.setRemainCount(remainCount);
        productSkuNewMapper.updateById(newSku);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductSkuNewService#getValidSkuListByProductId(long)
     */
    @Override
    /**
     * 获取有效SKU列表
     */
    public List<ProductSkuNew> getValidSkuListByProductId(long productId) {
//		 从sku表中获得该商品sku数量
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);//
        wrapper.ge("Status", ProductSkuNew.down_sold);//大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
        return skuList;
    }

    /**
     * 添加商品SKU（如果颜色和尺码不存在则创建）
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void insertProductSku(long productId, ProductNew product, long sizeId, long colorId, String sizeName,
                                 String colorName, int remainCount, long supplierId, long lowarehouseId, Double weight) {
        //检测sku不存在则进行添加SKU，已存在直接忽略
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);
        wrapper.eq("sizeId", sizeId);
        wrapper.eq("colorId", colorId);
        wrapper.eq("own_type", 1);
        wrapper.in ("Status", Arrays.asList (-2, - 1, 0, 1));
        List<ProductSkuNew> list = productSkuNewMapper.selectList(wrapper);
        if (list.size() == 0) {
            //添加sku
            ProductSkuNew sku = buildProductSku(productId, product, sizeId, colorId, sizeName, colorName, remainCount, supplierId, lowarehouseId);
//			logger.info("将要添加的SKU信息sku："+JSON.toJSONString(sku));
            productSkuNewMapper.insert(sku);

            //修改sku编码
            ProductSkuNew newSku = new ProductSkuNew();
            newSku.setId(sku.getId());
            newSku.setSkuNo(sku.getId() + 2000000);
            newSku.setWeight(weight);
            productSkuNewMapper.updateById(newSku);
        }
        else {
            logger.info("该商品该SKU已经存在，无需添加");
        }
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductSkuNewService#buildSkuListMap(long)
     */
    @Override
    /**
     * 编译封装SKU列表Map
     */
    public List<Map<String, Object>> buildSkuListMap(long productId) {

        //获取有效sku信息
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);//
        wrapper.ge("Status", - 1);//大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);

        //封装可用信息
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ProductSkuNew sku : skuList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("skuId", sku.getId());//SKUID
            map.put("productId", sku.getProductId());//商品ID
            map.put("colorName", sku.getColorName());//颜色名称
            map.put("sizeName", sku.getSizeName());//尺码名称
            map.put("colorId", sku.getColorId());//颜色ID
            map.put("sizeId", sku.getSizeId());//尺码ID
            map.put("remainCount", sku.getRemainCount());//库存数量
            list.add(map);
        }
        return list;
    }

    public List<ProductSkuNew> getSkuListByProductIds(List<String> productIdList) {
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.in("ProductId", productIdList);//
        wrapper.ge("Status", - 1);//大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
        return skuList;
    }
//	public List<Map<String,String>> getSkuMapListByProductIds(Collection<String> productIdList){
//		return productSkuNewMapper.getSkuMapListByProductIds(productIdList);
//	}

    @Override
    public List<Map<String, String>> getSkuMapListByProductIds(List<String> productIdList) {
        return productSkuNewMapper.getSkuMapListByProductIds(productIdList);
    }


    /* (non-Javadoc)
     */
    @Override
    /**
     *
     */
    public ProductSkuNew buildProductSku(long productId, ProductNew product, long sizeId, long colorId, String sizeName, String colorName,
                                         int remainCount, long supplierId, long lowarehouseId) {
        long time = System.currentTimeMillis();
        ProductSkuNew sku = new ProductSkuNew();
        ProductNew productNew = productNewService.getProductById(productId);
        //有用字段

        if (productNew.getState() == 6) {
            sku.setStatus(0);//状态:-3废弃，-2停用，-1下架，0正常，1定时上架
//			sku.setSaleStartTime(System.currentTimeMillis());
        }
        else {
            sku.setStatus(- 1);//状态:-3废弃，-2停用，-1下架，0正常，1定时上架
//			sku.setSaleEndTime(System.currentTimeMillis());
        }
//		sku.setStatus(-1);//状态:-3废弃，-2停用，-1下架，0正常，1定时上架

        sku.setProductId(productId);//商品id
        sku.setColorId(colorId);//颜色ID
        sku.setColorName(colorName);//颜色名称
        sku.setSizeId(sizeId);//尺码ID
        sku.setSizeName(sizeName);//尺码名称
        sku.setPropertyIds("7:" + colorId + ";8:" + sizeId);//7:416;8:426 商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开
        sku.setRemainCount(remainCount);//库存量
        sku.setName(product.getName());//货品名称
        sku.setLOWarehouseId(lowarehouseId);//主仓库      供应商仓库
        sku.setBrandId(product.getBrandId());//品牌id
        sku.setClothesNumber(product.getClothesNumber());//sku款号
        sku.setCreateTime(time);// 创建时间
        sku.setUpdateTime(time);//更新时间

        //无用字段，是默认值无需添加
//		sku.setPosition("--");//货架位置格式  1--2（表示1号2排）  0
//		sku.setSaleStartTime(0L);//上架时间 0
//		sku.setSaleEndTime(0L);//下架时间 0
//		sku.setPrice(0);//价格，人民币以分为单位，玖币以1为单位   
//		sku.setSpecificImage("");//对应SKU的图片信息   
//		sku.setCash(Double.parseDouble("0"));//   
//		sku.setWeight(Double.parseDouble("0"));//重量   0  
//		sku.setMarketPrice(Double.parseDouble("0"));//市场价（吊牌价）   0
//		sku.setCostPrice(Double.parseDouble("0"));//成本价     0
//		sku.setSort(0);// 
//		sku.setRemainCountLock(0);//库存锁定量  0  
//	    sku.setRemainCountStartTime(0L);//库存锁定开始时间  0
//	    sku.setRemainCountEndTime(0L);//库存锁定结束时间    0
//		sku.setRemainKeepTime(0);//'库存保留时间' 天   0
//	    sku.setIsRemainCountLock(0);//是否锁库存      否
//		sku.setPushTime(0L);//推送erp时间    0
//	    sku.setPromotionSaleCount(0);//推广销量  0
//	    sku.setPromotionVisitCount(0);//推广访问量   0
//	    sku.setRemainCount2(0);//副仓库库存
//	    sku.setLOWarehouseId2(0L);//副仓库
//	    sku.setSetLOWarehouseId2(0);//

        return sku;


    }

    /**
     * 获取对应的SKU列表
     *
     * @param skuIds
     * @return
     */
    public List<ProductSkuNew> getProductSKUs(Collection<Long> skuIds) {
        if (skuIds.size() < 1) {
            return new ArrayList<ProductSkuNew>();
        }
        Wrapper<ProductSkuNew> wrapper =
                new EntityWrapper<ProductSkuNew>().ge("Status", ProductSkuNew.down_sold).in("Id", skuIds);
        return productSkuNewMapper.selectList(wrapper);
    }


    @Override
    public void editProductSkuClothesNumber(long productId, String clothesNumber) {
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);//
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
        for (ProductSkuNew sku : skuList) {
            long skuId = sku.getId();
            ProductSkuNew productSkuNewUpd = new ProductSkuNew();
            productSkuNewUpd.setId(skuId);
            productSkuNewUpd.setClothesNumber(clothesNumber);
            productSkuNewMapper.updateById(productSkuNewUpd);
        }
    }

    @Override
    public Map<String, Object> getSkuList(Long productId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int remainCounts = 0;
        //颜色和尺码集合
        List<Map<String, Object>> colorList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> sizeList = new ArrayList<Map<String, Object>>();
        Set<Long> colorSet = new LinkedHashSet<Long>();
        Set<Long> sizeSet = new LinkedHashSet<Long>();
        //获取商品信息
        ProductNew productNew = productNewMapper.selectById(productId);
//		Map<String,Object> productMap = new HashMap<String,Object>();
//		productMap.put("name", productNew.getName());//商品标题
//		productMap.put("ladderPriceJson", productNew.getLadderPriceJson());//商品阶梯价
//		productMap.put("clothesNumber", productNew.getClothesNumber());//商品款号
//		productMap.put("mainImg", productNew.getMainImg());//商品主图
//		resultMap.put("productInfo", productMap);//商品信息
        //获取sku相关信息
        List<ProductSkuNew> list = getValidSkuListByProductId(productId);
        //json
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        for (ProductSkuNew productSkuNew : list) {

            Map<String, Object> skuPropMap = new HashMap<String, Object>();
            skuPropMap.put("id", productSkuNew.getId());//skuId
            skuPropMap.put("color", productSkuNew.getColorName());//颜色名称
            skuPropMap.put("colorId", productSkuNew.getColorId());//颜色ID
            skuPropMap.put("sizeId", productSkuNew.getSizeId());//尺寸ID
            skuPropMap.put("size", productSkuNew.getSizeName());//尺寸名称
            int remainCount = productSkuNew.getRemainCount();
            skuPropMap.put("remainCount", remainCount);//库存
            skuPropMap.put("onSaling", productSkuNew.getOnSaling());//在售
            skuPropMap.put("remainCountLock", productSkuNew.getRemainCountLock());//库存锁
            skuPropMap.put("isRemainCountLock", productSkuNew.getIsRemainCountLock());//是否库存锁

            jsonMap.put(productSkuNew.getPropertyIds(), skuPropMap);

            //添加商品库存
            remainCounts += remainCount;

            //添加颜色集合
            int colorBeforeAdd = colorSet.size();
            colorSet.add(productSkuNew.getColorId());
            int colorSizeAfterAdd = colorSet.size();
            if (colorBeforeAdd < colorSizeAfterAdd) {
                Map<String, Object> colorMap = new HashMap<String, Object>();
                colorMap.put("id", productSkuNew.getColorId());
                colorMap.put("propertyValue", productSkuNew.getColorName());
                colorList.add(colorMap);
            }

            //添加尺寸集合
            int sizeBeforeAdd = sizeSet.size();
            sizeSet.add(productSkuNew.getSizeId());
            int sizeAfterAdd = sizeSet.size();
            if (sizeBeforeAdd < sizeAfterAdd) {
                Map<String, Object> sizeMap = new HashMap<>();
                sizeMap.put("id", productSkuNew.getSizeId());
                sizeMap.put("propertyValue", productSkuNew.getSizeName());
                sizeList.add(sizeMap);
            }

        }

        //获取颜色
        resultMap.put("colors", colorList);//颜色集合
        //获取尺码
        resultMap.put("sizes", sizeList);//尺寸集合
        //获取商品上架状态
        resultMap.put("platformProductState", Integer.parseInt(getPlatformProductState(productId)));//商品上架状态
        //获取总库存
        resultMap.put("remainCounts", remainCounts);//总库存
        //获取sku
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        resultMap.put("skuMap", jsonObject);//sku列表
        return resultMap;
    }


    /**
     * 获取平台商品状态
     *
     * @param
     * @return 平台商品状态:0已上架、1已下架、2已删除
     */
    public String getPlatformProductState(long productId) {
//		logger.info("开始获取平台商品状态productId："+productId);
        String platformProductState = "1";//默认为已下架
        List<ProductSkuNew> productSKUList = getAllProductSKUsOfProduct(productId);
        //如果有一个sku上架则产品状态为上架
        for (ProductSkuNew productSKU : productSKUList) {
            if (productSKU.getOnSaling()) {
                platformProductState = "0";
            }
        }
//		logger.info("结束获取平台商品状态productId："+productId+",platformProductState:"+platformProductState);
        return platformProductState;
    }

    public List<ProductSkuNew> getAllProductSKUsOfProduct(long productId) {
        return productSkuNewMapper.getAllProductSKUsOfProduct(productId);
    }


    /**
     * 获取sku信息统计列表
     *
     * @param page      分页参数
     * @param startTime 查询条件, 查询上架时间在startTime之后
     * @param endTime   查询条z件, 查询上架时间在endTime之前
     * @return java.lang.Object
     * @auther Charlie(唐静)
     * @date 2018/6/5 17:09
     */
    @Override
    public List<Map<String, Object>> findSkuHistory(Page<Map<String, Object>> page, Long startTime, Long endTime, Long auditStartTime, Long auditEndTime) {

        List<Map<String, Object>> result = productSkuNewMapper.findSkuHistory(page, startTime, endTime, auditStartTime, auditEndTime);
        if (null == result || result.isEmpty()) {
            logger.info("查询sku, 查询记录数为0 startTime:" + startTime + ",endTime:" + endTime);
            return new ArrayList<>();
        }


        //日期格式化
        String keyAuditTime = "auditTime";
        String keyLastPTime = "lastPutonTime";
        for (Map<String, Object> map : result) {
            //审核时间
            Long auditTime = (Long) map.get(keyAuditTime);
            String temp;
            if (auditTime > 0) {
                temp = DateUtil.parseLongTime2Str(auditTime);
            }
            else {
                temp = "";
            }
            map.put(keyAuditTime, temp);

            //上架时间
            Long lastPutonTime = Long.parseLong((String) map.get(keyLastPTime));
            if (lastPutonTime > 0) {
                temp = DateUtil.parseLongTime2Str(lastPutonTime);
            }
            else {
                temp = "";
            }
            map.put(keyLastPTime, temp);
        }

        return result;
    }


    /**
     * 编辑 定时修改库存任务
     *
     * @param supplierId 供应商id
     * @param query      ProductSkuNew#Id    skuId
     *                   ProductSkuNew#timingSetRemainCountTime 定时更新库存时间
     *                   ProductSkuNew#timingSetType 定时更新库存类型, 0关闭(不更新), 1指定日期更新 ,2上架后N天更新
     *                   ProductSkuNew#timingSetCount 定时更新库存数
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/8 9:33
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public List<Map<String, Object>> timingSetRemainCountUpd(List<ProductSkuNew> query, Long supplierId) {

        if (query != null && ! query.isEmpty()) {
            /* 请求参数校验 */
            Set<Long> skuIdSet = skuIdSetTimingNoNull(query);
            if (skuIdSet.size() != query.size()) {
                throw BizException.defulat().msg("请求参数不合法, 有重复的skuId");
            }
            // 用户历史sku
            List<ProductSkuNew> historySkuList = findSkuBySupplierAndSkuIds(supplierId, skuIdSet);
            if (historySkuList == null || historySkuList.size() != skuIdSet.size()) {
                throw BizException.defulat().msg("请求参数不合法, 访问sku数据非供应商自有的sku");
            }
            // 获取sku对应的商品信息
            Set<Long> productIds = new HashSet<>(2);
            for (ProductSkuNew hisSku : historySkuList) {
                productIds.add(hisSku.getProductId());
                // 传入的参数没有productId信息, 组装进去
                for (ProductSkuNew querySku : query) {
                    if (querySku.getId().equals(hisSku.getId())) {
                        querySku.setProductId(hisSku.getProductId());
                    }
                }
            }
            // 商品信息 key:productId,value:Object
            Map<Long, ProductNew> productMap = productNewService.productByIds(productIds);
            if (productMap.isEmpty()) {
                throw BizException.defulat().msg("未找到商品信息");
            }

            // 校验请求参数合法
            verifyParams(query, productMap);

            // 申请定时服务
            applyTimingSetRemainCountTask(historySkuList, query, supplierId, productMap);

            // 更新表信息
            long curr = System.currentTimeMillis();
            for (ProductSkuNew sku : query) {
                ProductSkuNew entity = new ProductSkuNew();
                entity.setId(sku.getId());
                entity.setTimingSetType(sku.getTimingSetType());
                entity.setUpdateTime(curr);
                // 关闭时候, 不更新时间和数量
                if (sku.getTimingSetType() > 0) {
                    entity.setTimingSetRemainCountTime(sku.getTimingSetRemainCountTime());
                    entity.setTimingSetCount(sku.getTimingSetCount());
                }
                productSkuNewMapper.updateById(entity);
            }

            // 返回参数
            return buildResultMap(query, productMap);

        }
        return new ArrayList<>();
    }



    /**
     * 组装返回参数
     *
     * @param query
     * @param productMap
     * @return java.util.List
     * @auther Charlie(唐静)
     * @date 2018/6/19 19:32
     */
    public List<Map<String, Object>> buildResultMap(List<ProductSkuNew> query, Map<Long, ProductNew> productMap) {
        List<Map<String, Object>> result = new ArrayList<>(query.size());
        for (ProductSkuNew sku : query) {
            Map<String, Object> map = new HashMap<>(4);
            Integer type = sku.getTimingSetType();
            Long lastPutonTime = productMap.get(sku.getProductId()).getLastPutonTime();
            // 倒计时
            String countDown = "";
            /* 定时修改库存返回参数 */
            map.put("timingSetType", type);
            if (type == 0) {
                map.put("timingSetCount", "");
                map.put("timingSetRemainCountTime", "");
                map.put("timingSetStatus", 0);
            }
            else {
                map.put("timingSetCount", sku.getTimingSetCount());
                Long timingTime = sku.getTimingSetRemainCountTime();
                String time = type == 2 ? timingTime.toString() : DateUtil.parseLongTime2Str(timingTime);
                map.put("timingSetRemainCountTime", time);
                // 定时任务状态
                // 0:无,
                // 1:已定时 商品未上架过,还未在job上注册任务
                // 2:定时中 job上已注册任务,等待执行
                map.put("timingSetStatus", ProductSkuNewService.calculateTimingSetRemainCountStatus(type, timingTime, lastPutonTime));

                // 组装倒计时
                if (type == 2) {
                    if (lastPutonTime == null || lastPutonTime == 0) {
                        countDown = timingTime.toString();
                    }
                    else {
                        // 计算倒计时
                        countDown = calculateCountDown(timingTime, lastPutonTime);
                    }
                }
            }
            map.put("countDown", countDown);
            result.add(map);
        }
        return result;
    }


    /**
     * 根据首次上架时间, 计算相对上架时间的倒计时
     *
     * @param timingTime
     * @param lastPutonTime
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/6/19 19:22
     */
    public static String calculateCountDown(Long timingTime, Long lastPutonTime) {
        String countDown;
        long span = lastPutonTime + ( 24 * 3600 * 1000)*timingTime - System.currentTimeMillis();
        long day = span /( 24 * 3600 * 1000);
        long temp = span % (24 * 3600 * 1000);
        long hour = temp / (3600 * 1000);
        temp = temp % (3600 * 1000);
        long minute =temp % (60 * 1000)==0? temp / (60 * 1000) : temp / (60 * 1000) +1;
        countDown = day + "" + "天" + hour + "时" + minute + "分";
        return countDown;
    }




    /**
     * 验证定时修改sku库存, 请求参数是否合法
     *
     * @param productMap key:productId, value:obj NoNull
     * @param query      需要新添加/修改/删除的任务  NoNull
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/10 11:52
     */
    private void verifyParams(List<ProductSkuNew> query, Map<Long, ProductNew> productMap) {
        long curr = System.currentTimeMillis();
        // 如果是指定日期, 判断日期是否大于今天
        // 如果指定上架后时间, 判断上架后时间是否有效
        for (ProductSkuNew newSetting : query) {
            Integer type = newSetting.getTimingSetType();
            Long setTime = newSetting.getTimingSetRemainCountTime();
            Integer count = newSetting.getTimingSetCount();

            // 关闭定时任务, 不做数量校验
            if (! type.equals(0)) {
                if (count == null || count < 0) {
                    throw BizException.defulat().msg("无法保存，库存数量必须为大于或等于零的整数");
                }
            }

            if (type == 0) {
                // ignore
            }
            else if (type == 1) {
                if (newSetting.getTimingSetRemainCountTime() - 1000 * 60 * 10 <= curr) {
                    throw BizException.defulat().msg("无法保存，时间必须大于当前时间10分钟");
                }
            }
            else if (type == 2) {
                // product不为空
                ProductNew product = productMap.get(newSetting.getProductId());
                // 首次上架时间设置的日期, 应该大于等于今天日期+1day
                long lpt = product.getLastPutonTime();
                if (lpt > 0) {
                    int miniDays = autoSetRemainCountMinDayStrategy(lpt);
                    if (setTime < miniDays) {
                        throw BizException.defulat().msg("无法保存，输入值必须大于等于" + miniDays + "天");
                    }
                }
            }
            else {
                throw new IllegalArgumentException("无法保存, 未知的类型 timingSetType:" + type);
            }
        }

    }


    /**
     * 首次上架后, 启动上架后定时修改库存任务
     *
     * @param product
     * @param skus    product的sku列表,从数据库取出的sku
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/10 8:14
     */
    @Override
    public void startTimingSetRemainCountAfterOnShelves(ProductNew product, Collection<ProductSkuNew> skus, Long supplierId) {

        logger.info(
                new StringBuilder("启动上架后定时修改库存任务")
                        .append("supplierId:").append(supplierId)
                        .append(", skus:").append(BizUtil.bean2json(skus))
                        .toString()
        );

        Map<Long, ProductNew> map = new HashMap<>(1);
        map.put(product.getId(), product);
        skuJobService.timingSetRemainCountAdd(skus, map, supplierId);
    }


    /**
     * 更新sku库存
     *
     * @param skuId      skuId
     * @param supplierId 供应商id
     * @param count      更新的数量
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/10 15:24
     */
    @Override
    public void updateRemainCount(Long skuId, Long supplierId, Integer count, String token) {

        //todo... 服务调用者认证 token

        logger.info("job回调,定时修改库存 skuId:" + skuId + ",supplierId:" + supplierId + ",count:" + count);
        List<ProductSkuNew> skuList = selectBySupplierAndSkuIds(supplierId, Arrays.asList(skuId));
        if (ObjectUtils.isEmpty(skuList)) {
            logger.error("job回调,定时修改库存,未找到sku {skuId:" + skuId + ",supplierId:" + supplierId + ",count:" + count + "}");
            throw BizException.defulat().msg("未找到该供应商的sku信息");
        }

        try {
            ProductSkuNew entity = new ProductSkuNew();
            entity.setId(skuId);
            entity.setRemainCount(count);
            productSkuNewMapper.updateById(entity);
        } catch (Exception e) {
            String msg = "设置定时更新库存失败 sku:" + skuId + ",supplierId:" + supplierId + ",count:" + count;
            logger.error(msg);
            throw BizException.defulat().msg(msg);
        }
    }



    /**
     * 根据商品查询sku
     *
     * @param productId productId
     * @param ownType ownType
     * @param status
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
     * @author Charlie
     * @date 2018/9/7 17:44
     */
    @Override
    public List<ProductSkuNew> listSkuByProduct(Long productId, Integer ownType, String status) {
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<> ();
        if (StringUtils.isNotBlank (status)) {
            wrapper.and (" Status IN(" + status + ")");
        }
        wrapper.eq ("own_type", ownType);
        if (ownType == 1) {
            wrapper.eq ("own_type", ownType);
            wrapper.eq ("ProductId", productId);
        }
        else if (ownType == 2) {
            wrapper.eq ("own_type", ownType);
            wrapper.eq ("wxa_product_id", productId);
        }
        return productSkuNewMapper.selectList (wrapper);
    }


    /**
     * 查询供应商的skuIds
     *
     * @param supplierId
     * @param skuIds
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
     * @auther Charlie(唐静)
     * @date 2018/6/10 16:02
     */
    private List<ProductSkuNew> selectBySupplierAndSkuIds(Long supplierId, List<Long> skuIds) throws SecurityException {
        List<ProductSkuNew> list = productSkuNewMapper.selectBatchIds(skuIds);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>(0);
        }

        Set<Long> productIdSet = new HashSet<>(1);
        for (ProductSkuNew sku : list) {
            productIdSet.add(sku.getProductId());
        }

        List<ProductNew> productList = productNewService.selectByIds(new ArrayList<>(productIdSet));
        if (ObjectUtils.isEmpty(productList)) {
            throw BizException.defulat().msg("未找到sku的商品信息 skuIds:" + skuIds.toString());
        }

        for (ProductNew product : productList) {
            if (! product.getSupplierId().equals(supplierId)) {
                logger.warn("sku对应的商品信息非供应商自有商品, 无法访问 {productId:" + product.getId() + ",supplierId:" + supplierId + ",skuIds:" + skuIds.toString() + "}");
                throw BizException.defulat().msg("sku对应的商品信息非供应商自有商品, 无法访问");
            }
        }

        return list;
    }


    /**
     * 申请更新定时修改sku库存的job任务
     *
     * @param history 历史sku信息(Nullable)
     * @param newData 新设置的sku定时任务信息封装(Nullable)
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/8 10:37
     */
    public void applyTimingSetRemainCountTask(List<ProductSkuNew> history, List<ProductSkuNew> newData, Long supplierId, Map<Long, ProductNew> productMap) {
        if (newData != null && ! newData.isEmpty()) {

            Map<Long, ProductSkuNew> historyMap = createMap(history);
            Map<Long, ProductSkuNew> newDataMap = createMap(newData);
            // 需添加的job任务
            List<ProductSkuNew> addTask = new ArrayList<>();
            // 需修改的job任务
            List<ProductSkuNew> updTask = new ArrayList<>();
            // 需删除的job任务
            List<ProductSkuNew> delTask = new ArrayList<>();

            // 是新增还是修改, 没有历史sku就是新增, 有就是修改
            for (Map.Entry<Long, ProductSkuNew> newEntry : newDataMap.entrySet()) {
                ProductSkuNew newSku = newEntry.getValue();
                ProductSkuNew hisSku = historyMap.get(newEntry.getKey());

                boolean isAddJobTask = hisSku == null;
                if (isAddJobTask && newSku.getTimingSetType() != 0) {
                    // 没有添加过job任务,并且新job任务不是无效的
                    addTask.add(newSku);
                }
                else {
                    // update job task (与历史数据一样,则不更新)
                    boolean isTaskChange = ! timingSetRemainCountEquals(hisSku, newSku);
                    if (isTaskChange) {
                        // 判断job服务具体类型
                        JobTaskType taskType = judgeJobTaskType(hisSku, newSku, productMap.get(newSku.getProductId()));
                        if (taskType == ADD) {
                            addTask.add(newSku);
                        }
                        else if (taskType == UPDATE) {
                            updTask.add(newSku);
                        }
                        else if (taskType == DELETE) {
                            delTask.add(newSku);
                        }
                    }
                }
            }

            // 申请job server
            if (addTask.isEmpty() && updTask.isEmpty() && delTask.isEmpty()) {
                return;
            }
            skuJobService.timingSetRemainCountMulti(addTask, delTask, updTask, productMap, supplierId);
        }

    }


    /**
     * 判断申请job服务的类型
     *
     * @param hisSku  历史sku
     * @param newSku  新的sku服务设定的封装
     * @param product sku商品信息
     * @return com.jiuyuan.constant.JobTaskType
     * @auther Charlie(唐静)
     * @date 2018/6/9 9:58
     */
    private JobTaskType judgeJobTaskType(ProductSkuNew hisSku, ProductSkuNew newSku, ProductNew product) {
        // 是有有历史任务且还未执行
        boolean hasUnExecutedTask = isHasOnDoSetRemainCountTask(hisSku, product);

        Integer type = newSku.getTimingSetType();
        if (type == 0 && hasUnExecutedTask) {
            // 关闭历史job任务
            return JobTaskType.DELETE;
        }
        else if (type == 1) {
            /*
             * 指定时间更新:
             * 有未执行任务, 更新
             * 没有未执行任务, 新增
             */
            return hasUnExecutedTask ? UPDATE : ADD;
        }
        else if (type == 2) {
            // 商品是否已上过架(至少上过一次架)
            boolean isOnShelves = product.getLastPutonTime() > 0;
            if (isOnShelves) {
                // 上架后N天更新库存, 商品上过架
                // 有未执行任务, 更新
                // 没有未执行任务, 新增
                return hasUnExecutedTask ? UPDATE : ADD;
            }
            else {
                if (hasUnExecutedTask) {
                    // 上架后N天更新库存, 商品未上过架, 删除未执行的job任务
                    return JobTaskType.DELETE;
                }
            }
        }
        else {
            // ignore
        }

        return null;
    }


    /**
     * sku 是否存在还未指定的定时更新库存的任务
     *
     * @param hisSku
     * @param product
     * @return boolean
     * @auther Charlie(唐静)
     * @date 2018/6/10 14:34
     */
    private boolean isHasOnDoSetRemainCountTask(ProductSkuNew hisSku, ProductNew product) {
        long curr = System.currentTimeMillis();
        int oneDay = 24 * 3600 * 1000;
        // 有未执行的历史任务?
        boolean hasUnDoTask = false;
        if (hisSku.getTimingSetType() == 1 && hisSku.getTimingSetRemainCountTime() > curr) {
            // 历史设定 按日期更新+时间未到
            hasUnDoTask = true;
        }
        // 首次上架时间(标准时间) + 设定日期*24*3600*1000 > 当前时间
        if (hisSku.getTimingSetType() == 2 && hisSku.getTimingSetRemainCountTime() * oneDay + product.getLastPutonTime() > curr) {
            hasUnDoTask = true;
        }
        return hasUnDoTask;
    }

    /**
     * 组装成map格式
     * <p>
     * key : skuId
     * value: sku 实体类
     *
     * @param list
     * @return java.util.Map
     * @auther Charlie(唐静)
     * @date 2018/6/8 11:42
     */
    private Map<Long, ProductSkuNew> createMap(Collection<ProductSkuNew> list) {
        if (list == null) {
            return new HashMap<>(0);
        }

        Map<Long, ProductSkuNew> map = new HashMap<>(list.size());
        for (ProductSkuNew ele : list) {
            map.put(ele.getId(), ele);
        }
        return map;
    }


    /**
     * @param hisSku 历史sku
     * @param newSku 新设置的定时更新库存 sku参数封装
     * @return boolean
     * @auther Charlie(唐静)
     * @date 2018/6/8 11:34
     */
    public static boolean timingSetRemainCountEquals(ProductSkuNew hisSku, ProductSkuNew newSku) {
        if (ObjectUtils.nullSafeEquals(hisSku, newSku)) {
            return true;
        }

        boolean isTypeEqual = hisSku.getTimingSetType().equals(newSku.getTimingSetType());
        boolean isCountEqual = hisSku.getTimingSetCount().equals(newSku.getTimingSetCount());
        boolean isTimeEqual = hisSku.getTimingSetRemainCountTime().equals(newSku.getTimingSetRemainCountTime());

        if (isCountEqual && isTypeEqual && isTimeEqual) {
            return true;
        }
        return false;
    }


    /**
     * 根据供应商id, skuId 查询供应商的sku
     * <p>
     * 可以达到校验功能, 只查询供应商自己的sku
     *
     * @param supplierId
     * @param skuIds
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
     * @auther Charlie(唐静)
     * @date 2018/6/8 10:05
     */
    public List<ProductSkuNew> findSkuBySupplierAndSkuIds(Long supplierId, Collection<Long> skuIds) {
        return productSkuNewMapper.findSkuBySupplierAndSkuIds(supplierId, skuIds);
    }


    /**
     * 提取skuId
     * <p>
     * 并对 timingSetType, TimingSetRemainCount, timingSetCount做非空校验
     *
     * @param sources
     * @return java.util.List<java.lang.Long>
     * @auther Charlie(唐静)
     * @date 2018/6/8 9:47
     */
    public Set<Long> skuIdSetTimingNoNull(Collection<ProductSkuNew> sources) {
        HashSet<Long> set = new HashSet<>(sources.size());
        for (ProductSkuNew sku : sources) {
            // 非空校验
            if (BizUtil.isNotEmpty(sku.getTimingSetType())) {
                if (! sku.getTimingSetType().equals(0)) {
                    boolean notNull = BizUtil.isNotEmpty(sku.getId(), sku.getTimingSetType(), sku.getTimingSetRemainCountTime(), sku.getTimingSetCount());
                    if (! notNull) {
                        throw BizException.defulat().msg("请求参数不合法, 有参数为空");
                    }
                }
            }
            else {
                throw BizException.defulat().msg("请求参数不合法, TimingSetType为空");
            }

            set.add(sku.getId());
        }
        return set;
    }


    /**
     * sku定时更新库存任务状态
     *
     * @param timingSetType            定时任务类型
     * @param timingSetRemainCountTime 定时时间
     * @param lastPutonTime            商品首次上架时间
     * @return int
     * 0:无
     * 1:已定时 商品未上架过,还未在job上注册任务
     * 2:定时中 job上已注册任务,等待执行
     * @auther Charlie(唐静)
     * @date 2018/6/11 14:02
     */
    public static int calculateTimingSetRemainCountStatus(Integer timingSetType, Long timingSetRemainCountTime, Long lastPutonTime) {
        long curr = System.currentTimeMillis();
        // 没有任务
        int noTask = 0;
        // 有任务,但还未在job server上注册
        int existNoRegister = 1;
        // 已在job上注册任务, 等待执行
        int hasRegister = 2;

        if (timingSetType == 0) {
            return noTask;
        }
        else if (timingSetType == 1) {
            return timingSetRemainCountTime - curr > 0 ? hasRegister : noTask;
        }
        else if (timingSetType == 2) {
            if (lastPutonTime != null && lastPutonTime > 0) {
                return (lastPutonTime + timingSetRemainCountTime * 24 * 3600 * 1000) - curr > 0 ? hasRegister : noTask;
            }
            else {
                return existNoRegister;
            }
        }
        else {
            throw new IllegalArgumentException("位置的timingSetType" + timingSetType);
        }

    }

}
