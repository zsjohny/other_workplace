package com.jiuy.supplier.modular.myProduct.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.shop.PropertyValueNewMapper;
import com.jiuyuan.dao.mapper.supplier.CategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductCategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductIdCategoryIdMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.newentity.ProductCategoryNew;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductIdCategoryId;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.PropertyValueNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.ICategoryNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IProductSupplierFacade;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.web.help.JsonResponse;

/**
 * V3.0数据修订
 */
@Controller
@RequestMapping("/sql")
public class SqlController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SqlController.class);
    private String PREFIX = "/myProduct/allProduct/";

	@Autowired
	private IProductNewService productNewService;
	@Autowired
    IProductSupplierFacade productSupplierFacade;
    @Autowired
	private IUserNewService supplierUserService;
    @Autowired
	private ICategoryNewService categoryNewService;
	@Autowired
	private ProductDetailMapper productDetailMapper;
	@Autowired
	private ProductNewMapper productNewMapper;
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private PropertyValueNewMapper propertyValueNewMapper;
	
	@Autowired
	private CategoryNewMapper categoryNewMapper;
	@Autowired
	private ProductIdCategoryIdMapper productIdCategoryIdMapper;
	@Autowired
	private ProductCategoryNewMapper productCategoryNewMapper;
	
	/**
	 *  根据SKU修复商品品牌
	 * @return
	 */
//	  @RequestMapping(value = "/updProductSkuBrandId")
//	  @ResponseBody
//	  public JsonResponse updProductSkuBrandId(){
//		  JsonResponse jsonResponse = new JsonResponse();
//			try {
//				//为商品建立商品详情记录
//				Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
//				List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
//				for(ProductNew product : allProdictList){
//					long productId = product.getId();
//					long brandId = product.getBrandId();
//					//获得
//					Wrapper<ProductSkuNew> skuwrapper = new EntityWrapper<ProductSkuNew>();
//		        	wrapper.eq("ProductId", productId);
//		        	List<ProductSkuNew> skulist = productSkuNewMapper.selectList(skuwrapper);
//		        	for(ProductSkuNew sku : skulist){
//		        		ProductSkuNew newSku = new ProductSkuNew();
//		        		newSku.setId(sku.getId());
//		        		newSku.setBrandId(brandId);
//		        		productSkuNewMapper.updateById(newSku);
//		        		logger.info("根据SKU修复商品SKU品牌，productId："+productId+",brandId:"+brandId+",newSku.getId()："+newSku.getId());
//		        	}
//		        	logger.info("根据SKU修复商品品牌成功，productId："+productId+",brandId:"+brandId);
//				}
//				logger.info("根据SKU修复商品品牌完成，allProdictList.size()："+allProdictList.size());
//				return jsonResponse.setSuccessful();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return jsonResponse.setError(e.getMessage());
//			}
//	  }
//	
	
	/**
	 *  为库中所有商品SKU修复颜色和尺码数据（可多次执行）
	 * @return
	 */
//	  @RequestMapping(value = "/updProductSkuColorAndSize")
//	  @ResponseBody
//	  public JsonResponse updProductSkuColorAndSize(){
//		JsonResponse jsonResponse = new JsonResponse();
//		try {
//			//为库中所有商品SKU修复颜色和尺码数据
//			Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
//			List<ProductSkuNew> list = productSkuNewMapper.selectList(wrapper);
//			for(ProductSkuNew productSku : list){
//				long productId = productSku.getId();
//				logger.info("为库中所有商品SKU修复颜色和尺码数据，productId："+productId);
//			}
//			return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
//	  }
//	

	
 
      
      
    
   
    	
        /**
         *  修复线上上架商品分类信息数据
         * @return
         */
          @RequestMapping(value = "/updProductCategoryInfo")
          @ResponseBody
          public JsonResponse updProductCategoryInfo(){
      	  	JsonResponse jsonResponse = new JsonResponse();
      	  	try {
      	  		//1、线上商品分类关联列表
      	  	 List<ProductIdCategoryId> list = getProductIdCategoryId();
      	  	 logger.info("1、线上商品分类关联列表获取完成，list.size():"+list.size());
	      	  	//
      	  		//2、校验商品是否存在和校验分类ID是否存在
	      	  	for(ProductIdCategoryId item : list){
	      	  		long productId = item.getProductId();
	      	  		long categoryId = item.getCategoryId();
	      	  		ProductNew  product = productNewMapper.selectById(productId);
	      	  		if(product == null){
	      	  			throw new RuntimeException("根据商品ID获取商品失败，请排查原因，productId："+productId);
	      	  		}
	      	  		CategoryNew category = categoryNewMapper.selectById(categoryId);
		      	  	if(category == null){
	      	  			throw new RuntimeException("根据分类ID获取分类失败，productId："+productId+",categoryId:"+categoryId);
	      	  		}
		      	  	logger.info("校验productId:"+productId+",categoryId:"+categoryId+"完成");
	      	  	}
	      	  logger.info("2、校验商品是否存在和校验分类ID是否存在完成，list.size():"+list.size());
	      	  	//3、修改商品分类ID
	      	  	for(ProductIdCategoryId item : list){
	      	  		long productId = item.getProductId();
	      	  		long categoryId = item.getCategoryId();
	      	  		ProductNew newProduct = new ProductNew();
	      	  		newProduct.setId(productId);
	      	  		newProduct.setCategoryId(categoryId);
	      	  		productNewMapper.updateById(newProduct);
	      	  	}		
	      	  	logger.info("修复商品categoryId完成");
	      	  	//4、修复商品分类信息
	      	  	for(ProductIdCategoryId item : list){
	      	  		long productId = item.getProductId();
	      	  		updCategoryInfo(productId);
	      	  	}	
	      	  	
	      	  logger.info("修复商品分类信息完成");
	      	  	//5.修复商品和分类的中间表数据
	      	  	for(ProductIdCategoryId item : list){
	      	  		long productId = item.getProductId();
	      	  		long categoryId = item.getCategoryId();
	      	  		eidtProductCategory(productId,categoryId);
	      	  	}	
	      	  logger.info("修复商品和分类的中间表数据完成");
      	  		return jsonResponse.setSuccessful();
      	  	} catch (Exception e) {
      	  		e.printStackTrace();
      	  		return jsonResponse.setError(e.getMessage());
      	  	}
          }
          
          /**
           * 编辑商品分类中间表
           * @param productId
           * @param categoryId
           */
          private void eidtProductCategory(long productId,long categoryId){
        		long time =  System.currentTimeMillis();
        		logger.info("编辑商品分类中间表开始执行，productId："+productId);
        		
      		Wrapper<ProductCategoryNew> wrapper = new EntityWrapper<ProductCategoryNew>();
      		wrapper.eq("ProductId",productId);
      		wrapper.eq("CategoryId",categoryId);
      		wrapper.eq("Status",0);
      		List<ProductCategoryNew> productCategoryList = productCategoryNewMapper.selectList(wrapper);
      		if(productCategoryList.size()>0){//老分类存在则修改成新分类
//      			ProductCategoryNew productCategory = productCategoryList.get(0);
//      			ProductCategoryNew productCategoryNew = new ProductCategoryNew();
//      			productCategoryNew.setId(productCategory.getId());
//      			productCategoryNew.setCategoryId(categoryId);
//      			productCategoryNew.setUpdateTime(time);
//      			productCategoryNewMapper.updateById(productCategoryNew);
//      			logger.info("商品分类关联记录已经存在进行修改完成");
      		}else{//老分类不存在则新建新分类
      			//新建分类
      			ProductCategoryNew productCategoryNew = new ProductCategoryNew();
      			productCategoryNew.setProductId(productId);//产品ID
      			productCategoryNew.setCategoryId(categoryId);//分类ID
      			productCategoryNew.setStatus(0);//状态
      			productCategoryNew.setCreateTime(time);//创建时间
      			productCategoryNew.setUpdateTime(time);//修改时间
      			productCategoryNewMapper.insert(productCategoryNew);
      			logger.info("商品分类关联记录不存在新建完成");
      		}
          }

	private List<ProductIdCategoryId> getProductIdCategoryId() {
		Wrapper<ProductIdCategoryId> wrapper = new EntityWrapper<ProductIdCategoryId>();
		return productIdCategoryIdMapper.selectList(wrapper);
	}

	//修复商品分类数据
    private void updCategoryInfo( long productId) {
    	logger.info("修复商品分类数据开始执行，productId："+productId);
	  	ProductNew  product = productNewMapper.selectById(productId);
	  	
	  	String categoryIds = "";
    	long oneCategoryId = 0;
    	String oneCategoryName = "";
    	long twoCategoryId = 0;
    	String twoCategoryName = "";
    	long threeCategoryId = 0;
    	String threeCategoryName = "";
    	
    	//获取当前商品所属分类
    	long categoryId = product.getCategoryId();
    	CategoryNew category = categoryNewMapper.selectById(categoryId);
    	if(category == null){
    		logger.info("根据商品分类ID获取分类为空，categoryId："+categoryId);
    		return;
    	}
    	long parentId = category.getParentId();
    	if(parentId == 0){//挂在一级分类上
    		oneCategoryId =  category.getId();
    		oneCategoryName = category.getCategoryName();
    		categoryIds = String.valueOf(oneCategoryId);
    	}else{
    		CategoryNew parentCategory = categoryNewMapper.selectById(parentId);
    		long parentCategoryParentId = parentCategory.getParentId();
    		if(parentCategoryParentId == 0){
    			oneCategoryId =  parentCategory.getId();
        		oneCategoryName = parentCategory.getCategoryName();
        		twoCategoryId =  category.getId();
        		twoCategoryName = category.getCategoryName();
        		categoryIds = oneCategoryId+","+twoCategoryId;
    		}else{
    			CategoryNew parentParentCategory = categoryNewMapper.selectById(parentCategoryParentId);
        		oneCategoryId =  parentParentCategory.getId();
        		oneCategoryName = parentParentCategory.getCategoryName();
        		twoCategoryId =  parentCategory.getId();
        		twoCategoryName = parentCategory.getCategoryName();
        		threeCategoryId =  category.getId();
        		threeCategoryName = category.getCategoryName();
        		categoryIds = oneCategoryId+","+twoCategoryId+","+threeCategoryId;
    		}
    	}
    	
    	//修改商品分类
    	ProductNew newProduct = new ProductNew(); 
    	newProduct.setId(product.getId());
    	newProduct.setOneCategoryId(oneCategoryId);
    	newProduct.setOneCategoryName(oneCategoryName);
    	newProduct.setTwoCategoryId(twoCategoryId);
    	newProduct.setTwoCategoryName(twoCategoryName);
    	newProduct.setThreeCategoryId(threeCategoryId);
    	newProduct.setThreeCategoryName(threeCategoryName);
    	newProduct.setCategoryIds(categoryIds);
    	productNewMapper.updateById(newProduct);
    	
    	logger.info("修复商品多个分类相关字段完成,categoryIds:"+categoryIds);
	}
    
    
	
	
	
	 /**
	 *  为库中所有商品建立商品详情记录（可多次执行）（测试完成可用）
	 * @return
	 */
	  @RequestMapping(value = "/addProductDetail")
	  @ResponseBody
	  public JsonResponse addProductDetail(){
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//为商品建立商品详情记录
			Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
			List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
			for(ProductNew product : allProdictList){
				long productId = product.getId();
				ProductDetail productDetail = productNewService.getProductDetail(productId);
				if(productDetail != null){//不存在则添加
					logger.info("无需新建商品信息，productId："+productId);
				}else{
					long time = System.currentTimeMillis();
					ProductDetail  newProductDetail = new ProductDetail();
					newProductDetail.setProductId(productId);
					newProductDetail.setCreateTime(time);
					newProductDetail.setUpdateTime(time);
					newProductDetail.setShowcaseImgs(product.getDetailImages());
					newProductDetail.setDetailImgs(product.getSummaryImages());
					newProductDetail.setVideoUrl(product.getVideoUrl());
					newProductDetail.setVideoName(product.getVideoName());
					newProductDetail.setVideoFileId(product.getVideoFileId());
					productDetailMapper.insert(newProductDetail);
					logger.info("新建商品信息，productId："+productId);
				}
			}
			logger.info("为库中所有商品建立商品详情记录完成，allProdictList.size()："+allProdictList.size());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	  }
	  
	  
	  /**
	   *  为库中所有商品修复阶梯价格（测试完成）
	   */
	    @RequestMapping(value = "/updProductLadderPrice")
	    @ResponseBody
	    public JsonResponse updProductLadderPrice(){
		  	JsonResponse jsonResponse = new JsonResponse();
		  	try {
		  		//为商品建立商品详情记录
		  		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
		  		List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
		  		for(ProductNew product : allProdictList){
		  			long productId = product.getId();
		  			logger.info("productId:"+productId+",product："+JSON.toJSONString(product));
		  			Double wholeSaleCash = product.getWholeSaleCash();
		  			if(wholeSaleCash != null){
		  				ProductNew newProductNew = new ProductNew();
			  			newProductNew.setId(productId);
			  			newProductNew.setMaxLadderPrice(wholeSaleCash);//最大阶梯价
			  			newProductNew.setMinLadderPrice(wholeSaleCash);//最小阶梯价
			  			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			  			Map<String,Object> ladderPriceMap = new HashMap<String,Object>();
			  			ladderPriceMap.put("count", 1);
			  			ladderPriceMap.put("price", wholeSaleCash);
			  			list.add(ladderPriceMap);
			  			String ladderPriceJson = JSON.toJSONString(list);//阶梯价JSON
			  			newProductNew.setLadderPriceJson(ladderPriceJson);
			  			productNewMapper.updateById(newProductNew);
			  			logger.info("为库中所有商品修复阶梯价格成功，productId："+productId+",WholeSaleCash:"+wholeSaleCash);
		  			}else{
		  				logger.info("为库中所有商品修复阶梯价格批发价为空无法修复，productId："+productId+",WholeSaleCash:"+wholeSaleCash);
		  			}
		  			
		  		}
		  		logger.info("为库中所有商品修复商品主图完成，allProdictList.size()："+allProdictList.size());
		  		return jsonResponse.setSuccessful();
		  	} catch (Exception e) {
		  		e.printStackTrace();
		  		return jsonResponse.setError(e.getMessage());
		  	}
	    }
	  
	  
	    /**
	     *  为库中所有商品修复主图（可执行多次）（测试完成）
	     *  解析出橱窗图的第一种放到主图字段中
	     * @return
	     */
	      @RequestMapping(value = "/updMainImg")
	      @ResponseBody
	      public JsonResponse updMainImg(){
	  	  	JsonResponse jsonResponse = new JsonResponse();
	  	  	try {
	  	  		//为商品建立商品详情记录
	  	  		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
	  	  		List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
	  	  		for(ProductNew product : allProdictList){
	  	  			long productId = product.getId();
	  	  			String maiImg = product.getFirstImage();
	  	  			if(StringUtils.isNotEmpty(maiImg)){
	  	  				ProductNew newProductNew = new ProductNew();
	  	  				newProductNew.setId(productId);
	  	  				newProductNew.setMainImg(maiImg);
	  	  				productNewMapper.updateById(newProductNew);
	  	  				logger.info("为库中所有商品修复商品主图成功，productId："+productId+"，maiImg："+maiImg);
	  	  			}
	  	  		}
	  	  		logger.info("为库中所有商品修复商品主图完成，allProdictList.size()："+allProdictList.size());
	  	  		return jsonResponse.setSuccessful();
	  	  	} catch (Exception e) {
	  	  		e.printStackTrace();
	  	  		return jsonResponse.setError(e.getMessage());
	  	  	}
	      }
	  
	  
	  
	  
	  
	  
	      /**
	       *  为库中所有商品修复上下架状态（可执行多次）（已经测试完成）
	       * @return
	       */
	        @RequestMapping(value = "/updProductUpDownSold")
	        @ResponseBody
	        public JsonResponse updProductUpDownSold(){
	    	  	JsonResponse jsonResponse = new JsonResponse();
	    	  	try {
	    	  		//1、获取所有商品
//	    	  		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
//	    	  		List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
	    	  		 List<ProductIdCategoryId> list = getProductIdCategoryId();
		      	  	 logger.info("1、线上商品分类关联列表获取完成，list.size():"+list.size());
			      	  	
			      	for(ProductIdCategoryId item : list){
//	    	  		for(ProductNew product : allProdictList){
	    	  			//2、根据SKU状态修改商品上下架记录
	    	  			long productId = item.getProductId();
		  	  	  		ProductNew newProductNew = new ProductNew();
		  	  	  		newProductNew.setId(productId);
		  	  	  		int state = getUpDownSold(productId);
		  	  	  		newProductNew.setState(state);
		  	  	  		productNewMapper.updateById(newProductNew);
		  	  	  		logger.info("为库中所有商品修复上下架成功，productId："+productId+",state:"+state+",newProductNew.getState()："+newProductNew.getState());
	    	  		}
	    	  		logger.info("为库中所有商品修复上下架完成，list.size()："+list.size());
	    	  		return jsonResponse.setSuccessful();
	    	  	} catch (Exception e) {
	    	  		e.printStackTrace();
	    	  		return jsonResponse.setError(e.getMessage());
	    	  	}
	        }
	    
	        /**
	         * 获得商品上下架状态
	         * @param productId
	         * @return
	         */
	        private Integer getUpDownSold(long productId) {
//	        	int upDownSold = ProductNewStateEnum.down_sold.getIntValue();
//	        	
//	        	//1、获得商品上架状态的SKU列表
	        	Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
//	        	wrapper.eq("ProductId", productId);
//	        	wrapper.eq("Status", ProductSkuNew.up_sold);
//	        	List<ProductSkuNew> list = productSkuNewMapper.selectList(wrapper);
//	        	//2、如果list为不0则该商品上架状态
//	        	if(list.size() > 0){
//	        		upDownSold = ProductNewStateEnum.up_sold.getIntValue();
//	        	}
//	    		return upDownSold;
	        	
	        	int upDownSold = ProductNewStateEnum.down_sold.getIntValue();
	    		Wrapper<ProductSkuNew> skuWrapper = new EntityWrapper<ProductSkuNew>();
	    		skuWrapper.eq("ProductId", productId);
    			List<ProductSkuNew> skuList = productSkuNewMapper.selectList(skuWrapper);
    			for(ProductSkuNew sku : skuList){
    				boolean onSaling = sku.getOnSaling();
    				if(onSaling){
    					upDownSold = ProductNewStateEnum.up_sold.getIntValue();
    				}
    			}
	    		return upDownSold;
	    	}
	        
	        
	        
	     
	  
	  
	        /**
	         *  为库中指定上线商品修复供应商数据、品牌数据、仓库数据（重点必须在所有品牌关联供应商和仓库后才可进行）（测试完成）
	         * @return
	         */
	          @RequestMapping(value = "/updProductBrandInfo")
	          @ResponseBody
	          public JsonResponse updProductBrandInfo(){
	      	  	JsonResponse jsonResponse = new JsonResponse();
	      	  	try {
	      	  		//为商品建立商品详情记录
//	      	  		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
//	      	  		List<ProductNew> allProdictList = productNewMapper.selectList(wrapper);
	      	  		
	      	 	 List<ProductIdCategoryId> list = getProductIdCategoryId();
	      	  	 logger.info("1、线上商品分类关联列表获取完成，list.size():"+list.size());
		      	  	//
	      	  		//2、校验商品是否存在和校验分类ID是否存在
		      	  	for(ProductIdCategoryId item : list){
	    		  			long productId = item.getProductId();
	    		  			ProductNew product = productNewMapper.selectById(productId);
	    		  			long brandId = product.getBrandId();
	    		  			if(brandId == 0){
	    		  				throw new RuntimeException("获取商品对应品牌ID为空，请排查问题，productId："+productId+",brandId："+brandId); 
	    		  			}
	    		  			BrandNew brand = supplierUserService.getSupplierBrandInfo(brandId);
	    		  			if(brand == null){
	    		  				throw new RuntimeException("获取商品对应品牌为空，请排查问题，productId："+productId+",brandId："+brandId); 
	    		  			}
	    		  			UserNew supplier = supplierUserService.getSupplierByBrandId(brandId);
	    		  			if(supplier == null){
	    		  				throw new RuntimeException("获取商品对应品牌对应的供应商为空，请排查问题，productId："+productId+",brandId："+brandId); 
	    		  			}
	    		  			long lowarehouseId = supplier.getLowarehouseId();//仓库ID
	    		  			if(lowarehouseId == 0){
	    		  				throw new RuntimeException("获取商品对应品牌对应的供应商仓库为空，请排查问题，productId："+productId+",brandId："+brandId+",lowarehouseId:"+lowarehouseId); 
	    		  			}
	    	  	  	}
	    	  	  	logger.info("为库中所有商品校验品牌、供应商、从仓库完成，list.size()："+list.size());
	    	  		for(ProductIdCategoryId item : list){
	    	  			long productId = item.getProductId();
	    	  			ProductNew product = productNewMapper.selectById(productId);
	      	  			long brandId = product.getBrandId();
	      	  			BrandNew brand = supplierUserService.getSupplierBrandInfo(brandId);
	      	  			UserNew supplier = supplierUserService.getSupplierByBrandId(brandId);
	      	  			long lowarehouseId = supplier.getLowarehouseId();//仓库ID
	      	  			
	    	  			ProductNew newProductNew = new ProductNew();
	    	  			newProductNew.setId(productId);
	    	  			long supplierId = Long.parseLong(String.valueOf(supplier.getId()));
	    	  			newProductNew.setSupplierId(supplierId);
	    	  			newProductNew.setLOWarehouseId(lowarehouseId);
	    	  			newProductNew.setBrandId(brand.getBrandId());
	    	  			newProductNew.setBrandName(brand.getBrandName());
	    	  			newProductNew.setBrandLogo(brand.getBrandIdentity());
	    	  			productNewMapper.updateById(newProductNew);
	    	  			logger.info("修复商品供应商信息成功，productId："+productId+",supplierId："+supplierId+",lowarehouseId:"+lowarehouseId+",brand.getBrandId():"+brand.getBrandId());
	      	  		}
	      	  		logger.info("为库中指定上线商品修复供应商数据、品牌数据、仓库数据，list.size()："+list.size());
	      	  		return jsonResponse.setSuccessful();
	      	  	} catch (Exception e) {
	      	  		e.printStackTrace();
	      	  		return jsonResponse.setError(e.getMessage());
	      	  	}
	          }
	          
	          
	  

	      	/**
	           *  修复商品SKU的颜色值和尺码值
	           * @return
	           */
	            @RequestMapping(value = "/updateColorAndSize")
	            @ResponseBody
	            public JsonResponse updateColorAndSize(){
	        	  	JsonResponse jsonResponse = new JsonResponse();
	        	  	try {
	        	  		//获取所有的属性
	        			Wrapper<PropertyValueNew> wrapper = new EntityWrapper<PropertyValueNew>();
	        			List<PropertyValueNew> valueList = propertyValueNewMapper.selectList(wrapper);
	        			
	        			
	        			//获取所有的SKU
	        			Wrapper<ProductSkuNew> skuWrapper = new EntityWrapper<ProductSkuNew>();
	        			List<ProductSkuNew> allProdictSkuList = productSkuNewMapper.selectList(skuWrapper);
	        			for(ProductSkuNew sku : allProdictSkuList){
	        				//解析出颜色ID和尺码ID
	        				String propertyIds = sku.getPropertyIds();//7:404;8:424
	        				long colorId = 0;
	        				long sizeId = 0;
	        				if(StringUtils.isNotEmpty(propertyIds)){
	        					String[] arr = propertyIds.split(";");
	        					String colorIdStr = arr[0].split(":")[1];//颜色ID
	        					String sizeIdStr = arr[1].split(":")[1];//尺码ID
	        					colorId = Long.parseLong(colorIdStr);
	        					sizeId = Long.parseLong(sizeIdStr);
	        				}else{
	        					logger.info("propertyIds为空，sku.getId():"+sku.getId());
	        				}
	        				String colorName = getName(valueList,colorId);
	        				String sizeName = getName(valueList,sizeId);
	        				ProductSkuNew newProductSkuNew = new ProductSkuNew();
	        				newProductSkuNew.setId(sku.getId());
	        				newProductSkuNew.setColorName(colorName);
	        				newProductSkuNew.setSizeName(sizeName);
	        				newProductSkuNew.setColorId(colorId);
	        				newProductSkuNew.setSizeId(sizeId);
	        				logger.info("colorId:"+colorId+",colorName:"+colorName+",sizeId:"+sizeId+",sizeName:"+sizeName);
	        				productSkuNewMapper.updateById(newProductSkuNew);
	        			}
	        			logger.info("处理完成，allProdictSkuList.size():"+allProdictSkuList.size());
	        			logger.info("处理完成，valueList.size():"+valueList.size());
	            	  	
	        	  		return jsonResponse.setSuccessful();
	        	  	} catch (Exception e) {
	        	  		e.printStackTrace();
	        	  		return jsonResponse.setError(e.getMessage());
	        	  	}
	            }
	          
	      	private String getName(List<PropertyValueNew> valueList,long id) {
	      		String name = "";
	      		if(id == 0){
	      			logger.info("根据属性ID获取name完成name为空，id:"+id+",name:"+name);
	      			return name;
	      		}
	      		for(PropertyValueNew value : valueList){
	      			if(value.getId() == id){
	      				name = value.getPropertyValue();
	      			}
	      		}
	      		if(StringUtils.isEmpty(name)){
	      			logger.info("根据属性ID获取name完成name为空，id:"+id+",name:"+name);
	      		}
	      		
	      		return name;
	      	}
	  
	  
	  
	  
}
