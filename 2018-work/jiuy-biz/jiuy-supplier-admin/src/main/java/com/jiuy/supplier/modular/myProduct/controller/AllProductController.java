package com.jiuy.supplier.modular.myProduct.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jiuyuan.service.common.*;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyProduct;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.VideoSignatureUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.alibaba.fastjson.JSONObject;

import static com.jiuyuan.service.common.ProductSkuNewService.calculateCountDown;

/**
 * 所有商品控制器
 */
@Controller
@RequestMapping( "/allProduct" )
public class AllProductController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(AllProductController.class);
    private String PREFIX = "/myProduct/allProduct/";

    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private IProductSupplierFacade productSupplierFacade;
    @Autowired
    private IUserNewService supplierUserService;
    @Autowired
    private IProductSkuNewService productSkuNewService;
    @Autowired
    private IDynamicPropertyCategoryService dynamicPropertyCategoryService;
    @Autowired
    private IDynamicPropertyService dynamicPropertyService;

    @Autowired
    private IDynamicPropertyValueService dynamicPropertyValueService;
    @Autowired
    private IDynamicPropertyProductService dynamicPropertyProductService;

    @Autowired
    private ICategoryNewService categoryNewService;


    /**
     * 编辑商品SKU信息（编辑第二步）
     *
     * @return
     * @RequestParam productId  商品ID，新建时为0
     * @RequestParam oneCategoryId  所属一级分类ID
     * @RequestParam oneCategoryName 所属一级分类名称
     * @RequestParam twoCategoryId 所属二级分类ID
     * @RequestParam twoCategoryName 所属二级分类名称
     * @RequestParam threeCategoryId 所属三级分类ID
     * @RequestParam threeCategoryName 所属三级分类名称
     * @RequestParam name 商品名称
     * @RequestParam mainImg 商品主图
     * @RequestParam showcaseImgs 商品橱窗图
     * @RequestParam clothesNumber 商品款号 拼接上改品牌的款号前缀
     * @RequestParam ladderPriceJson 阶梯价JSON
     * @RequestParam maxLadderPrice 最大阶梯价格
     * @RequestParam minLadderPrice 最小阶梯价格
     */
    @RequestMapping( value = "/updProductSkuInfo" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updProductSkuInfo(
            // 商品ID
            @RequestParam() long productId,
            //编辑商品SKU列表JSON（skuId、库存数量）
            @RequestParam( required = false, defaultValue = "" ) String updSkuListJson,
            //添加商品SKU列表JSON（库存数量、颜色值、颜色组Id、尺码值、尺码组Id），说明：如果颜色值不存在则会新增
            @RequestParam( required = false, defaultValue = "" ) String addSkuListJson
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //当前登录用户供应商ID
            long supplierId = ShiroKit.getUser().getId();
            //仓库ID
            long lowarehouseId = ShiroKit.getUser().getLowarehouseId();

            if (productId != 0) {
                ProductNew product = productNewService.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("没找到商品");
                }
                else {
                    if (product.getSupplierId() != supplierId) {
                        throw new RuntimeException("不是该供应商的商品不能编辑");
                    }
                }
            }
//			编辑商品SKU信息（编辑第二步）
            ProductNew productNew = productSupplierFacade.updProductSkuInfo(productId, supplierId, lowarehouseId, updSkuListJson, addSkuListJson);
            return jsonResponse.setSuccessful().setData(productNew);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取颜色选项和尺码选项（编辑第二步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getColorListAndSizeList" )
    @ResponseBody
    public JsonResponse getColorListAndSizeList() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            Map<String, Object> data = productSupplierFacade.getColorListAndSizeList(supplierId);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 修改SKU库存数量（编辑商品第二步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/updSkuRemainCount" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updSkuRemainCount(
            @RequestParam( required = true ) long productId,// 商品ID
            @RequestParam( required = true ) long skuId,// 商品SKUID
            @RequestParam( required = true ) int remainCount,// 商品SKU库存数量
            @RequestParam( required = true ) Double weight// sku的重量
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID

            if (productId != 0) {
                ProductNew product = productNewService.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("没找到商品");
                }
                else {
                    if (product.getSupplierId() != supplierId) {
                        throw new RuntimeException("不是该供应商的商品不能编辑");
                    }
                }
            }

            if (remainCount < 0) {
                throw new RuntimeException("库存数量不能小于0");
            }
            // 更新商品SKU的库存数量
            productSkuNewService.updSkuRemainCount(productId, skuId, remainCount, weight);

            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取编辑商品SKU信息页面数据（编辑第二步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getUpdProductSkuInfoPageData" )
    @ResponseBody
    public JsonResponse getUpdProductSkuInfoPageData(
            // 商品ID
            @RequestParam( required = true ) long productId
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("编辑或新建基本信息商品（编辑第一步）开始，productId：" + productId);
        try {
            //当前登录用户供应商ID
            long supplierId = ShiroKit.getUser().getId();

            ProductNew product = null;
            if (productId != 0) {
                product = productNewService.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("没找到商品");
                }
                else {
                    if (product.getSupplierId() != supplierId) {
                        throw new RuntimeException("不是该供应商的商品不能编辑");
                    }
                }
            }
            // 获取有效商品SKU列表(包括上架和下架的sku)
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            List<ProductSkuNew> skuList = productSkuNewService.getValidSkuListByProductId(productId);
            for (ProductSkuNew sku : skuList) {
                Map<String, Object> map = new HashMap<>(5);
                map.put("skuId", sku.getId());
                map.put("colorName", sku.getColorName());
                map.put("sizeName", sku.getSizeName());
                map.put("remainCount", sku.getRemainCount());
                map.put("weight", sku.getWeight());
                // 定时修改库存的信息
                Integer type = sku.getTimingSetType();
                map.put("timingSetType", type);
                Integer count = sku.getTimingSetCount();
                map.put("timingSetCount", count);
                Long timingTime = sku.getTimingSetRemainCountTime();
                String time = type == 1 ? DateUtil.parseLongTime2Str(timingTime) : String.valueOf(timingTime);
                map.put("timingSetRemainCountTime", time);
                // 定时任务状态
                // 0:无,
                // 1:已定时 商品未上架过,还未在job上注册任务
                // 2:定时中 job上已注册任务,等待执行
                Long lastPutonTime = product.getLastPutonTime();
                map.put("timingSetStatus", ProductSkuNewService.calculateTimingSetRemainCountStatus(type, timingTime, lastPutonTime));
                // 定时任务剩余时间
                // 商品设置首次上架时间时 ---> 1: 商品没有首次上架时间, 按照指定日期; 2:商品有首次上架时间, 计算日期
                String countDown = "";
                if (type == 2) {
                    if (lastPutonTime == null || lastPutonTime == 0) {
                        countDown = time;
                    }
                    else {
                        // 计算倒计时
                        countDown = calculateCountDown(timingTime, lastPutonTime);
                    }
                }
                map.put("countDown", countDown);
                data.add(map);
            }
            logger.info("获取sku列表data：" + data);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }






    /**
     * 校验商品款号可用状态，用于编辑商品时校验使用
     *
     * @return
     */
    @RequestMapping( value = "/checkClothesNumberUsable" )
    @ResponseBody
    public JsonResponse checkClothesNumberUsable(
            @RequestParam( required = true ) long productId,// 商品ID
            @RequestParam( required = true ) String clothesNumber//商品款号
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            boolean usable = productNewService.checkClothesNumberUsable(supplierId, productId, clothesNumber);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("productId", productId);//商品ID
            data.put("clothesNumber", clothesNumber);//商品款号
            data.put("usable", usable ? 1 : 0);//商品款号可用状态：0不可用，1可用
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取新建编辑商品基本信息页面数据（编辑商品第一步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getUpdProductBasicInfoPageData" )
    @ResponseBody
    public JsonResponse getUpdProductBasicInfoPageData(
            @RequestParam( required = false ) long productId// 商品ID，新建时为0
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info(" 获取新建编辑商品基本信息页面数据开始（测试是否发布成功），productId：" + productId);
        try {

            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            BrandNew supplierBrand = supplierUserService.getSupplierBrandInfoBySupplierId(supplierId);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("productId", product.getId());//// 商品ID，新建时为0
            data.put("oneCategoryId", product.getOneCategoryId());// 所属一级分类ID
            data.put("oneCategoryName", product.getOneCategoryName());//所属一级分类名称
            data.put("twoCategoryId", product.getTwoCategoryId());//所属二级分类ID
            data.put("twoCategoryName", product.getTwoCategoryName());//所属二级分类名称
            data.put("threeCategoryId", product.getThreeCategoryId());////所属三级分类ID
            data.put("threeCategoryName", product.getThreeCategoryName());//所属三级分类名称
            data.put("name", product.getName());//商品名称
            // 商品橱窗视频
            data.put("vedioMain", product.getVedioMain());
            data.put("memberLevel", product.getMemberLevel ());
            data.put("memberLadderPriceJson", product.getMemberLadderPriceJson ());

            data.put("clothesNumber", product.getClothesNumber());////商品款号
            String clothesNumberPrefix = supplierBrand.getClothNumberPrefix();
            data.put("clothesNumberEditValue", product.buildClothesNumberEditValue(clothesNumberPrefix));////商品款号编辑值
            data.put("clothesNumberPrefix", clothesNumberPrefix);////商品款号前缀
            data.put("ladderPriceJson", product.getLadderPriceJson());////阶梯价JSON
            data.put("maxLadderPrice", product.getMaxLadderPrice());////最大阶梯价格
            data.put("minLadderPrice", product.getMinLadderPrice());////最小阶梯价格
            //商品主图
            data.put("mainImg", product.getMainImg());
            // 是否免邮
            data.put("expressFree", product.getExpressFree());

            ProductDetail oldProductDetail = productNewService.getProductDatailByProductId(productId);
            if (oldProductDetail != null) {
                data.put("showcaseImgs", oldProductDetail.getShowcaseImgs());////商品橱窗图
            }
            else {
                data.put("showcaseImgs", product.getDetailImages());////商品橱窗图
            }
            logger.info("获取新建编辑商品基本信息页面数据（编辑商品第一步）,data:" + JSON.toJSONString(data));
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 编辑或新建基本信息商品（编辑第一步）
     * productId 商品ID
     *
     * @return
     * @RequestParam(required = true)int auditState,
     * @RequestParam(required = false, defaultValue = "0")long productId,
     * @RequestParam(required = false, defaultValue = "")String productName,
     * @RequestParam(required = false, defaultValue = "")String priceBegin,
     * @RequestParam(required = false, defaultValue = "")String priceEnd,
     * @RequestParam(required = false, defaultValue = "")String clothesNumber,
     * @RequestParam(required = false, defaultValue = "0")long auditTimeBegin,
     * @RequestParam(required = false, defaultValue = "0")long auditTimeEnd,
     * @RequestParam(required = false, defaultValue = "")String brandName,
     * @RequestParam(required = false, defaultValue = "1")int current,
     * @RequestParam(required = false, defaultValue = "10")int size ,
     */
    @RequestMapping( value = "/updProductBasicInfo" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updProductBasicInfo(
            @RequestParam( required = true ) long productId,// 商品ID，新建时为0
            @RequestParam( required = true, defaultValue = "0" ) long oneCategoryId,// 所属一级分类ID
            @RequestParam( required = true ) String oneCategoryName,//所属一级分类名称
            @RequestParam( required = false, defaultValue = "0" ) long twoCategoryId,//所属二级分类ID
            @RequestParam( required = false ) String twoCategoryName,//所属二级分类名称
            @RequestParam( required = false, defaultValue = "0" ) long threeCategoryId,//所属三级分类ID
            @RequestParam( required = false ) String threeCategoryName,//所属三级分类名称
            @RequestParam( required = true ) String name,//商品名称
            @RequestParam( required = true ) String mainImg,//商品主图
            @RequestParam( required = true ) String showcaseImgs,//商品橱窗图
            @RequestParam( required = true ) String clothesNumber,//商品款号 拼接上改品牌的款号前缀
            @RequestParam( required = true ) String ladderPriceJson,//阶梯价JSON
            String vedioMain, Integer expressFree
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("编辑或新建基本信息商品（编辑第一步）开始，productId：" + productId);
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            long lowarehouseId = ShiroKit.getUser().getLowarehouseId();//主仓库ID
            if (productId != 0) {
                ProductNew product = productNewService.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("没找到商品");
                }
                else {
                    if (product.getSupplierId() != supplierId) {
                        throw new RuntimeException("不是该供应商的商品不能编辑");
                    }
                }
            }
            long returnProductId = productSupplierFacade.updProductBasicInfo(productId, supplierId, oneCategoryId, oneCategoryName,
                    twoCategoryId, twoCategoryName, threeCategoryId, threeCategoryName, name,
                    mainImg, showcaseImgs, clothesNumber, ladderPriceJson, lowarehouseId, vedioMain, expressFree);
            logger.info("编辑或新建基本信息商品（编辑第一步）完成，productId：" + productId + ",returnProductId:" + returnProductId);
            Map<String, String> data = new HashMap<String, String>();
            data.put("productId", String.valueOf(returnProductId));
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取商品分类列表，新建编辑商品时用于下拉选择分类
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getProductCategoryList" )
    @ResponseBody
    public JsonResponse getProductCategoryList() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
//	    	获取商品分类列表，用于编辑商品选择商品分类
            List<CategoryNew> list = categoryNewService.getProductCategoryList();
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (CategoryNew categoryNew : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("categoryId", categoryNew.getId());//分类ID
                map.put("parentId", categoryNew.getParentId());//分类父id，0表示顶级分类
                map.put("categoryName", categoryNew.getCategoryName());//分类名称
                data.add(map);

            }
//	    	logger.info("获取商品分类列表，新建编辑商品时用于下拉选择分类,data:"+JSON.toJSONString(data));
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 查询商品列表
     * 说明：供应商产品管理查询列表
     * 接口/product/getSearchProductList
     * 示例：http://dev.yujiejie.com:34080/product/getSearchProductList?keyword=&limit=10&offset=10
     *
     * @param clothesNumber   款号
     *                        //@param skuId           SKUID
     * @param state           状态  商品状态： -1（全部）、0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待审核，审核中）、3（审核不通过）、 4（待上架，审核通过、待上架）、 5（上架，审核通过、已上架）、 6（下架，审核通过、已下架）
     * @param upSoldTimeBegin 上架时间开始
     * @param upSoldTimeEnd   上架时间结束
     * @param priceBegin      价格开始
     * @param priceEnd        价格结束
     *                        //@param buyCountBegin   销量开始
     *                        //@param buyCountEnd     销量结束
     * @return
     */
    @RequestMapping( value = "/list" )
    @ResponseBody
    public Object getSearchProductList(@RequestParam( required = false, defaultValue = "" ) String clothesNumber,
                                       @RequestParam( required = false, defaultValue = "" ) String productName,
                                       @RequestParam( required = false, defaultValue = "-1" ) long state,
                                       @RequestParam( required = false, defaultValue = "0" ) long upSoldTimeBegin,
                                       @RequestParam( required = false, defaultValue = "0" ) long upSoldTimeEnd,
                                       @RequestParam( required = false, defaultValue = "0" ) double priceBegin,
                                       @RequestParam( required = false, defaultValue = "0" ) double priceEnd,
                                       @RequestParam( required = false, defaultValue = "0" ) int salesCountBegin,
                                       @RequestParam( required = false, defaultValue = "0" ) int salesCountEnd,
                                       @RequestParam( required = false, defaultValue = "0" ) int totalSkuCountBegin,
                                       @RequestParam( required = false, defaultValue = "0" ) int totalSkuCountEnd) {
        logger.info("clothesNumber:" + clothesNumber + ",productName:" + productName + ",state:" + state + ",upSoldTimeBegin：" + upSoldTimeBegin + ",upSoldTimeEnd:" + upSoldTimeEnd +
                ",priceBegin:" + priceBegin + ",priceEnd:" + priceEnd + ",salesCountBegin:" + salesCountBegin + ",salesCountEnd:" + salesCountEnd);


        //    	Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            logger.info("getSearchProductList,supplierId:" + supplierId);

            List<ProductNew> productList = productNewService.getSearchProductList(supplierId,
                    clothesNumber, productName, state, upSoldTimeBegin, upSoldTimeEnd, priceBegin, priceEnd, salesCountBegin, salesCountEnd, page);

            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
            for (ProductNew product : productList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("productId", product.getId());//商品ID
                map.put("clothesNumber", product.getClothesNumber());//款号
                List<ProductSkuNew> skuList = productSkuNewService.getValidSkuListByProductId(product.getId());
                int totalSkuCount = 0;
                for (ProductSkuNew productSkuNew : skuList) {
                    totalSkuCount += productSkuNew.getRemainCount();
                }
                map.put("totalSkuCount", totalSkuCount);//总库存
                map.put("skuCount", skuList.size());//SKU数量
                map.put("mainImg", product.getMainImg());//主图
                map.put("name", product.getName());//名称
                map.put("salesCount", product.getSaleTotalCount());//销量
                map.put("ladderPriceJson", product.getLadderPriceJson());//阶梯价格JSON
                int State = product.getState();
                ProductNewStateEnum productStateEnum = ProductNewStateEnum.getEnum(State);
                map.put("stateValue", productStateEnum.getIntValue());//状态值
                map.put("stateName", productStateEnum.getDesc());//状态名称
                map.put("upSoldTime", DateUtil.parseLongTime2Str(product.getUpSoldTime()));//上架时间
                map.put("auditTime", DateUtil.parseLongTime2Str(product.getAuditTime()));//审核时间
                map.put("newTime", DateUtil.parseLongTime2Str(product.getNewTime()));//新建时间
                //是否需要审核，是否需要审核： 0（不需要审核）、 1（需要审核），
//	        	说明：用来判断下架状态商品是否需要提交审核后才能上架，商品编辑后会需要重新提交审核才能上架，上架时会关闭此开关
//	        	map.put("needAudit", product.getNeedAudit());
                records.add(map);
            }
            page.setRecords(records);
//	        logger.info("供应商获取所有商品列表page:"+JSON.toJSONString(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("获取商品列表接口返回数据page:" + page);
        return super.packForBT(page);
    }


    /**
     * 根据款号获取商品信息，用于商品详情编辑时使用（编辑商品第三步）
     *
     * @param clothesNumber 商品款号
     * @return
     */
    @RequestMapping( value = "/getProductByClothesNumber" )
    @ResponseBody
    public JsonResponse getProductByClothesNumber(@RequestParam( required = false ) String clothesNumber) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ProductNew product = productNewService.getProductByClothesNumber(clothesNumber);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }
            else if (product.getState() != 6) {
                throw new RuntimeException("商品未上架");
            }

            Map<String, Object> data = new HashMap<String, Object>();
            // 商品ID，新建时为0
            data.put("productId", product.getId());
            // 商品款号
            data.put("clothesNumber", product.getClothesNumber());
            // 商品名称
            data.put("name", product.getName());
            // 商品主图
            data.put("mainImg", product.getMainImg());
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取新建编辑商品详细信息页面数据（编辑商品第三步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getUpdProductDetailInfoPageData" )
    @ResponseBody
    public JsonResponse getUpdProductDetailInfoPageData(
            @RequestParam( required = false ) long productId// 商品ID，新建时为0
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("获取新建编辑商品详细信息页面数据开始，productId：" + productId);
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("productId", product.getId());//// 商品ID


            ProductDetail oldProductDetail = productNewService.getProductDatailByProductId(productId);
            String detailImgs = "";
            String videoUrl = "";
            String videoName = "";
            long videoFileId = 0;
            if (oldProductDetail != null) {
                detailImgs = oldProductDetail.getDetailImgs();
                videoUrl = oldProductDetail.getVideoUrl();
                videoName = oldProductDetail.getVideoName();
                videoFileId = oldProductDetail.getVideoFileId();
            }
            //商品详情图集合JSON，格式例：["https://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14663027018061466302701806.jpg"]
            data.put("detailImgs", detailImgs);
            //商品视频URL
            data.put("videoUrl", videoUrl);
            //商品视频名称
            data.put("videoName", videoName);
            //商品视频fileId
            data.put("videoFileId", videoFileId);

            // 上架商品编辑不显示状态
            int onShelvesState = 6;
            if (product.getState().equals(onShelvesState)) {
                data.put("putawayType", "");
                data.put("timingPutwayTime", "");
                // 是否显示定时上架前端控件
                data.put("isTimingShow", false);
            }
            else {
                // 下架商品, 有未执行的任务显示
                // 下架商品, 定时状态是立刻上架,是前次操作遗留数据,应该初始化重选,不显示
                // 其他, 都显示
                if (product.getState().equals(7)) {
                    // 有未执行的任务,或者设定是暂不上架,回显
                    if (ProductSupplierFacade.isHasUnDoTimingOnShelvesTask(oldProductDetail)) {
                        data.put("putawayType", 2);
                        data.put("timingPutwayTime", DateUtil.parseLongTime2Str(oldProductDetail.getTimingPutwayTime()));
                        data.put("isTimingShow", true);
                    }
                    else if (oldProductDetail.getPutawayType().equals(3)){
                        data.put("putawayType", oldProductDetail.getPutawayType());
                        data.put("timingPutwayTime", oldProductDetail.getTimingPutwayTime());
                        data.put("isTimingShow", true);
                    }
                    else {
                        data.put("putawayType", oldProductDetail.getPutawayType());
                        data.put("timingPutwayTime", "");
                        data.put("isTimingShow", false);
                    }
                }
                else {
                    data.put("putawayType", oldProductDetail.getPutawayType());
                    // 设置定时项目的格式化日期,否则""
                    data.put("timingPutwayTime", oldProductDetail.getPutawayType().equals(2) ? DateUtil.parseLongTime2Str(oldProductDetail.getTimingPutwayTime()) : 0);
                    data.put("isTimingShow", true);
                }
            }
            // 商品历史是否已执行
            data.put("state", product.getState());

            //搭配商品
            List<Map<String, String>> togetherProductList = new ArrayList<>();
            String together = product.getTogether();//搭配推荐商品ID集合，英文逗号分隔
            if (StringUtils.isNotEmpty(together)) {
                String[] productIdArr = together.split(",");
                for (String togetherProductId : productIdArr) {
                    ProductNew togetherProduct = productNewService.getProductById(Long.parseLong(togetherProductId));
                    Map<String, String> togetherProductMap = new HashMap<>();
                    togetherProductMap.put("productId", String.valueOf(togetherProduct.getId()));//商品ID
                    togetherProductMap.put("clothesNumber", togetherProduct.getClothesNumber());//商品款号
                    togetherProductMap.put("name", togetherProduct.getName());//商品名称
                    togetherProductMap.put("mainImg", togetherProduct.getMainImg());//商品主图
                    togetherProductList.add(togetherProductMap);
                }
            }

            data.put("togetherProductList", togetherProductList);//搭配推荐商品列表

            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 编辑或新建基本详细信息商品（编辑商品第三步）
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/updProductDetailInfo" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updProductDetailInfo(
            @RequestParam( required = true ) long productId,// 商品ID
            @RequestParam( required = false ) String togetherProducts,//搭配推荐商品Id集合，英文逗号分隔
            @RequestParam( required = false ) String detailImgs,//商品详情图集合JSON
            @RequestParam( required = false ) String videoUrl,//商品视频
            @RequestParam( required = false ) String videoName,//商品视频名称
            @RequestParam( required = false, defaultValue = "0" ) long videoFileId,//商品视频fileId
            @RequestParam( required = true ) int putawayType,//上架类型 1:审核通过后立即上架, 2:定时上架, 3:暂不上架
            @RequestParam( required = false, defaultValue = "0" ) Long timingPutwayTime,//定时上架时间
            @RequestParam( required = false, defaultValue = "" ) String jsonStr) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("编辑或新建基本详细信息商品（编辑商品第三步）开始，productId：" + productId);
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            //当前登录用户供应商ID
            long supplierId = ShiroKit.getUser().getId();
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            productSupplierFacade.updProductDetailInfo(
                    productId,
                    togetherProducts,
                    detailImgs,
                    videoUrl,
                    videoName,
                    videoFileId,
                    putawayType,
                    timingPutwayTime
            );
            //商品绑定属性
            bindProductAndProperty(jsonStr, productId);
            logger.info("编辑或新建基本详细信息商品（编辑商品第三步）完成，productId：" + productId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 商品提交审核
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/productSubmitAudit" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse productSubmitAudit(@RequestParam( required = true ) long productId) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("商品提交审核开始，productId：" + productId);
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            productNewService.productSubmitAudit(productId);
            logger.info("商品提交审核完成，productId：" + productId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 上架商品
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/upSoldProduct" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse upSoldProduct(@RequestParam( required = true ) long productId) {
        logger.info("上架商品开始，productId：" + productId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                logger.info("没找到商品，productId：" + productId);
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                logger.info("不是该供应商的商品不能编辑,productId:" + productId + ",supplierId：" + supplierId);
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }
            productSupplierFacade.manualOnShelvesV375(productId, ShiroKit.getUser().getId().longValue());

//            productNewService.upSoldProduct(productId);
            logger.info("上架商品完成，productId：" + productId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 上架商品 新版本
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/upSoldProductV375" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse upSoldProductV375(@RequestParam( required = true ) Long productId) {
        logger.info("上架商品开始，productId：" + productId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            productSupplierFacade.manualOnShelvesV375(productId, ShiroKit.getUser().getId().longValue());
            logger.info("上架商品完成，productId：" + productId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 下架商品
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/downSoldProduct" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse downSoldProduct(@RequestParam( required = true ) long productId) {
        JsonResponse jsonResponse = new JsonResponse();
        logger.info("下架商品开始，productId：" + productId);
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            productNewService.downSoldProduct(productId);
            logger.info("下架商品完成，productId：" + productId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 获取商品审核不通过理由
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getProductAuditNoPassReason" )
    @ResponseBody
    public JsonResponse getProductAuditNoPassReason(@RequestParam( required = true ) long productId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("没找到商品");
            }

            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (product.getSupplierId() != supplierId) {
                throw new RuntimeException("不是该供应商的商品不能编辑");
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("productId", product.getId());//商品ID
            data.put("auditNoPassReason", product.getAuditNoPassReason());//款号
            //TODO V3.0 待根据情况填充数据
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 获取预览商品信息
     * productId 商品ID
     *
     * @return
     */
    @RequestMapping( value = "/getPreviewProductInfo" )
    @ResponseBody
    public JsonResponse getPreviewProductInfo(@RequestParam( required = true ) long productId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ProductNew product = productNewService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("商品不存在:");
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("productId", product.getId());//商品ID
            data.put("clothesNumber", product.getClothesNumber());//款号
            data.put("skuCount", 0);//SKU数量
            data.put("mainImg", product.getMainImg());//主图
            data.put("name", product.getName());//名称
            data.put("ladderPriceJson", product.getLadderPriceJson());//阶梯价格JSON
            data.put("detailImages", product.getDetailImages());// 橱窗图片:JSON 格式数组
            data.put("summaryImages", product.getSummaryImages());// 详情图 json数组
            data.put("brandLogo", product.getBrandLogo());// 品牌Logo
            data.put("brandName", product.getBrandName());// 品牌名称

            //搭配商品
            List<Map<String, String>> matchProductList = new ArrayList<Map<String, String>>();
            String matchProductIds = product.getTogether();//搭配推荐商品ID集合，英文逗号分隔
            if (StringUtils.isNotEmpty(matchProductIds)) {
                String[] productIdArr = matchProductIds.split(",");
                for (String togetherProductId : productIdArr) {
                    ProductNew togetherProduct = productNewService.getProductById(Long.parseLong(togetherProductId));
                    Map<String, String> togetherProductMap = new HashMap<String, String>();
                    togetherProductMap.put("productId", String.valueOf(togetherProduct.getId()));//商品ID
                    togetherProductMap.put("clothesNumber", togetherProduct.getClothesNumber());//商品款号
                    togetherProductMap.put("name", togetherProduct.getName());//商品名称
                    togetherProductMap.put("mainImg", togetherProduct.getMainImg());//商品主图
                    togetherProductMap.put("ladderPriceJson", togetherProduct.getLadderPriceJson());//阶梯价格JSON
                    togetherProductMap.put("maxLadderPrice", String.valueOf(togetherProduct.getMaxLadderPrice()));//最大阶梯价格
                    togetherProductMap.put("minLadderPrice", String.valueOf(togetherProduct.getMinLadderPrice()));//最小阶梯价格
                    matchProductList.add(togetherProductMap);
                }
            }
            data.put("matchProductList", matchProductList);//搭配推荐商品列表
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 跳转到所有商品首页
     */
    @RequestMapping( "" )
    public String index() {
        return PREFIX + "allProduct.html";
    }

    /**
     * 跳转到添加所有商品
     */
    @RequestMapping( "/allProduct_add" )
    public String allProductAdd() {
        return PREFIX + "allProduct_add.html";
    }

    /**
     * 跳转预览
     */
    @RequestMapping( "/preview" )
    public String preview() {
        return PREFIX + "preview.html";
    }

    /**
     * 跳转预览
     */
    @RequestMapping( "/preview_detail" )
    public String previewDetail() {
        return PREFIX + "preview_detail.html";
    }

    /**
     * 跳转到修改所有商品
     */
    @RequestMapping( "/allProduct_update/{allProductId}" )
    public String allProductUpdate(@PathVariable Integer allProductId, Model model) {
        return PREFIX + "allProduct_edit.html";
    }

    /**
     *获取所有商品列表
     */
    @RequestMapping( value = "/getSearchProductList" )
    @ResponseBody
    public Object list(String condition) {
        //logger.info("productNew:"+"aaa");
        return null;
    }

    /**
     * 新增所有商品
     */
    @RequestMapping( value = "/add" )
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除所有商品
     */
    @RequestMapping( value = "/delete" )
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改所有商品
     */
    @RequestMapping( value = "/update" )
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 所有商品详情
     */
    @RequestMapping( value = "/detail" )
    @ResponseBody
    public Object detail() {
        return null;
    }


    /**
     * 获取上传视频签名
     *
     * @return
     */
    @RequestMapping( value = "/getsign" )
    @ResponseBody
    public JsonResponse getsign() {
        JsonResponse jsonResponse = new JsonResponse();

        VideoSignatureUtil sign = new VideoSignatureUtil();
        sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
        sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";
        sign.m_qwNowTime = System.currentTimeMillis() / 1000;
        sign.m_iRandom = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.m_iSignValidDuration = 3600 * 24 * 2;

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("sign", sign.getUploadSignature());
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 删除视频签名接口
     *
     * @param fileId
     * @param priority
     * @return
     */
    @RequestMapping( value = "/deletevideo" )
    @ResponseBody
    public JsonResponse getDeleteVideoSign(@RequestParam( "fileId" ) long fileId, @RequestParam( value = "priority", required = false, defaultValue = "1" ) int priority) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            VideoSignatureUtil sign = new VideoSignatureUtil();
            sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
            sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";

            Map<String, Object> data = new HashMap<String, Object>();
            String url = sign.getDeleteSignatureG(fileId, priority);
            data.put("url", url);
            String rs = com.jiuyuan.util.HttpRequest.sendGet(url, "", "UTF-8");
            System.out.println(rs);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError("获取删除视频签名失败" + e.getMessage());
        }
    }

    /**
     * 根据分类ID获取该分类下的动态属性
     *
     * @param cateId
     * @return
     */
    @RequestMapping( "/getDynamicProperty" )
    @ResponseBody
    public JsonResponse getDynamicPropertyByCateId(@RequestParam( value = "cateId", required = true ) long cateId,
                                                   @RequestParam( value = "productId", required = true ) long productId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper = new EntityWrapper<DynamicPropertyProduct>().eq("product_id", productId);
            List<DynamicPropertyProduct> dynamicPropertyProductList = dynamicPropertyProductService.selectList(dynamicPropertyProductWrapper);
            Wrapper<DynamicPropertyCategory> wrapper = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", cateId).eq("status", 1);//1:启用 0：禁用
            List<DynamicPropertyCategory> dynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (DynamicPropertyCategory dynamicPropertyCategory : dynamicPropertyCategoryList) {
                Map<String, Object> map = new HashMap<>();
                Wrapper<DynamicProperty> dynamicPropertywrapper = new EntityWrapper<DynamicProperty>().eq("id", dynamicPropertyCategory.getDynaPropId()).eq("status", DynamicProperty.DYNA_PROP_STATUS_ON);
                List<DynamicProperty> dynamicPropertyList = dynamicPropertyService.getDynamicProperty(dynamicPropertywrapper);
                if (dynamicPropertyList.size() > 0) {
                    DynamicProperty dynamicProperty = dynamicPropertyList.get(0);
                    Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_id", dynamicProperty.getId()).eq("status", 1);
                    List<DynamicPropertyValue> dynamicPropertyValueList = dynamicPropertyValueService.selectByDynaPropId(dynamicPropertyValueWrapper);

                    List<Map<String, Object>> dynamicPropertyValueMapList = new ArrayList<>();
                    if (dynamicPropertyValueList.size() > 0) {
                        for (DynamicPropertyValue dynamicPropertyValue : dynamicPropertyValueList) {
                            Map<String, Object> dynamicPropertyValueMap = new HashMap<>();

                            dynamicPropertyValueMap.put("choosed", 0);//未选中
                            for (DynamicPropertyProduct dynamicPropertyProduct : dynamicPropertyProductList) {
                                if (dynamicPropertyProduct.getDynaPropValueId().equals(dynamicPropertyValue.getId())) {
                                    dynamicPropertyValueMap.put("choosed", 1);//已选中
                                }
                            }
                            dynamicPropertyValueMap.put("dynamicPropertyValueId", dynamicPropertyValue.getId());
                            if (dynamicProperty.getFormType() == DynamicProperty.FORM_TYPE_TEXT || dynamicProperty.getFormType() == DynamicProperty.FORM_TYPE_TEXTAREA) {
                                Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper1 = new EntityWrapper<DynamicPropertyProduct>()
                                        .eq("product_id", productId)
                                        .eq("dyna_prop_id", dynamicProperty.getId())
                                        .eq("dyna_prop_value_id", dynamicPropertyValue.getId());
                                List<DynamicPropertyProduct> selectList = dynamicPropertyProductService.selectList(dynamicPropertyProductWrapper1);
                                if (selectList.size() > 0) {
                                    dynamicPropertyValueMap.put("dynamicPropertyValue", selectList.get(0).getDynaPropValue());
                                    dynamicPropertyValueMap.put("tipValue", dynamicPropertyValue.getDynaPropValue());
                                }
                                else {
                                    dynamicPropertyValueMap.put("dynamicPropertyValue", null);
                                    dynamicPropertyValueMap.put("tipValue", dynamicPropertyValue.getDynaPropValue());
                                }
                            }
                            else {
                                dynamicPropertyValueMap.put("dynamicPropertyValue", dynamicPropertyValue.getDynaPropValue());
                            }
                            dynamicPropertyValueMapList.add(dynamicPropertyValueMap);
                        }
                        map.put("dynaPropId", dynamicProperty.getId());
                        map.put("dynaPropName", dynamicProperty.getName());
                        map.put("formType", dynamicProperty.getFormType());
                        map.put("isFill", dynamicProperty.getIsFill());
                        map.put("isDisplay", dynamicProperty.getIsDisplay());
                        map.put("dynamicPropertyValueList", dynamicPropertyValueMapList);
                        list.add(map);
                    }
                }
            }
            return jsonResponse.setSuccessful().setData(list);
        } catch (Exception e) {
            return jsonResponse.setError("商品编辑动态属性e:" + e.getMessage());
        }
    }


    /**
     * 商品属性绑定
     *
     * @param jsonStr
     * @param productId
     * @return
     */
    private void bindProductAndProperty(String jsonStr, long productId) {
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        DynamicPropertyProduct dynamicPropertyProduct = new DynamicPropertyProduct();
        Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper = new EntityWrapper<DynamicPropertyProduct>().eq("product_id", productId);
        //删除商品绑定的动态属性
        dynamicPropertyProductService.deleteByWrapper(dynamicPropertyProductWrapper);
        dynamicPropertyProduct.setProductId(productId);
        //解析json
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            long dynaPropId = json.getLong("dynaPropId");
            dynamicPropertyProduct.setDynaPropId(dynaPropId);
            JSONArray json1 = json.getJSONArray("dynamicPropertyValueList");
            for (int j = 0; j < json1.size(); j++) {
                JSONObject json2 = json1.getJSONObject(j);
                long dynaPropValueId = json2.getLong("dynamicPropertyValueId");
                String string = json2.getString("dynamicPropertyValue");
                dynamicPropertyProduct.setCreateTime(System.currentTimeMillis());
                dynamicPropertyProduct.setUpdateTime(System.currentTimeMillis());
                dynamicPropertyProduct.setDynaPropValue(string);
                dynamicPropertyProduct.setDynaPropValueId(dynaPropValueId);
                dynamicPropertyProductService.insert(dynamicPropertyProduct);
            }
        }
    }


    /**
     * 查询商品列表
     * 说明：供应商产品管理查询列表
     * 接口/product/getSearchProductList
     * 示例：http://dev.yujiejie.com:34080/product/getSearchProductList?keyword=&limit=10&offset=10
     *
     * @param clothesNumber          款号
     *                               skuId              SKUID
     * @param state                  状态  商品状态： -1（全部）、0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待审核，审核中）、3（审核不通过）、 4（待上架，审核通过、待上架）、 5（上架，审核通过、已上架）、 6（下架，审核通过、已下架）
     * @param upSoldTimeBegin        上架时间开始
     * @param upSoldTimeEnd          上架时间结束
     * @param priceBegin             价格开始
     * @param priceEnd               价格结束
     * @param timingPutawayTimeFloor 定时上架最小值(查询条件)
     * @param timingPutawayTimeCeil  定时上架最大值(查询条件)
     *                               buyCountBegin      销量开始
     *                               buyCountEnd        销量结束
     * @param totalSkuCountBegin     库存量开始
     * @param totalSkuCountEnd       销量结束
     * @return
     */
    @RequestMapping( value = "/listNew" )
    @ResponseBody
    public Object getSearchProductListNew(@RequestParam( required = false, defaultValue = "" ) String clothesNumber,
                                          @RequestParam( required = false, defaultValue = "" ) String productName,
                                          @RequestParam( required = false, defaultValue = "-1" ) long state,
                                          @RequestParam( required = false, defaultValue = "0" ) long upSoldTimeBegin,
                                          @RequestParam( required = false, defaultValue = "0" ) long upSoldTimeEnd,
                                          @RequestParam( required = false, defaultValue = "0" ) double priceBegin,
                                          @RequestParam( required = false, defaultValue = "0" ) double priceEnd,
                                          @RequestParam( required = false, defaultValue = "0" ) int salesCountBegin,
                                          @RequestParam( required = false, defaultValue = "0" ) int salesCountEnd,
                                          @RequestParam( required = false, defaultValue = "0" ) int totalSkuCountBegin,
                                          @RequestParam( required = false, defaultValue = "0" ) int totalSkuCountEnd,
                                          @RequestParam( required = false, defaultValue = "0" ) int orderType,
                                          @RequestParam( required = false ) Long timingPutawayTimeFloor,
                                          @RequestParam( required = false ) Long timingPutawayTimeCeil
    ) {
        logger.info("clothesNumber:" + clothesNumber + ",productName:" + productName
                + ",state:" + state + ",upSoldTimeBegin：" + upSoldTimeBegin
                + ",upSoldTimeEnd:" + upSoldTimeEnd + ",priceBegin:" + priceBegin
                + ",priceEnd:" + priceEnd + ",salesCountBegin:" + salesCountBegin
                + ",salesCountEnd:" + salesCountEnd + ",totalSkuCountBegin:"
                + totalSkuCountBegin + ",totalSkuCountEnd:" + totalSkuCountEnd
                + ",timingPutawayTimeFloor:" + timingPutawayTimeFloor + ",timingPutawayTimeCeil:" + timingPutawayTimeCeil
        );

        //    	Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            logger.info("getSearchProductList,supplierId:" + supplierId);
            List<Map<String, Object>> productList = productNewService.getSearchProductListNew(supplierId, timingPutawayTimeFloor, timingPutawayTimeCeil,
                    clothesNumber, productName, state, upSoldTimeBegin, upSoldTimeEnd, priceBegin, priceEnd, salesCountBegin, salesCountEnd, page, totalSkuCountBegin, totalSkuCountEnd, orderType);
            for (Map<String, Object> map : productList) {
                map.put("auditTime", DateUtil.parseLongTime2Str((Long) map.get("auditTime")));
                map.put("upSoldTime", DateUtil.parseLongTime2Str((Long) map.get("upSoldTime")));
                map.put("newTime", DateUtil.parseLongTime2Str((Long) map.get("newTime")));
                int State = (int) map.get("state");
                ProductNewStateEnum productStateEnum = ProductNewStateEnum.getEnum(State);
                // 状态值
                map.put("stateValue", productStateEnum.getIntValue());
                // 状态名称
                map.put("stateName", productStateEnum.getDesc());
            }
            page.setRecords(productList);
//	        logger.info("供应商获取所有商品列表page:"+JSON.toJSONString(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("获取商品列表接口返回数据page:" + page);
        return super.packForBT(page);
    }

    /**
     * 设置排名
     *
     * @param productId
     * @param rank
     * @return
     */
    @RequestMapping( value = "/setRank" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse setRank(@RequestParam( value = "productId", required = true ) long productId,
                                @RequestParam( value = "rank", required = true ) int rank) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            long supplierId = ShiroKit.getUser().getId();//当前登录用户供应商ID
            if (productId != 0) {
                ProductNew product = productNewService.getProductById(productId);
                if (product == null) {
                    return jsonResponse.setError("没找到商品");
                }
                else {
                    if (product.getSupplierId() != supplierId) {
                        return jsonResponse.setError("不是该供应商的商品不能编辑");
                    }
                }
            }
            //判断排名是否重复
            boolean result = productNewService.validateRank(supplierId, rank);
            if (result) {
                return jsonResponse.setError("排名重复，请重新输入");
            }
            ProductNew newProduct = new ProductNew();
            newProduct.setRank(rank);
            newProduct.setId(productId);
            //设置排名
            productNewService.setRank(newProduct);
            return jsonResponse.setSuccessful().setData("ok");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return jsonResponse.setError("设置排名失败！");
        }
    }

    /**
     * 重置排名
     * @return
     */
    @RequestMapping("reset/rank")
    @ResponseBody
    public JsonResponse resetRank(){
        return productNewService.resetRank();
    }
}

