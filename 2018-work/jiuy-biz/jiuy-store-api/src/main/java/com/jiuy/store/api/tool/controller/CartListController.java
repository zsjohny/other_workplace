package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.dao.mapper.shop.StoreShoppingCartNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponTemplateNewMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.CartItemNewVO;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.IProductSkuNewService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.ShoppingCartNewService;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BrandCartItemNewVO;
import com.store.entity.CartProductItemVO;
import com.store.entity.SkuVO;
import com.store.service.ShoppingCartService;
import com.store.service.StoreBusinessServiceShop;
import com.store.service.StoreShoppingCartDelegator;
import com.store.service.StoreShoppingCartFacade;

/**
 * @author chen
 * @version 2017年7月10日 上午
 * 
 */

@Controller
@RequestMapping("/mobile/cart")
public class CartListController {

	private static final Logger logger = LoggerFactory.getLogger(CartListController.class);
	 @Autowired
	 private StoreBusinessServiceShop storeBusinessService;
    
    @Autowired
    private ShoppingCartNewService shoppingCartNewService;
    
    @Autowired
	private StoreCouponTemplateNewMapper storeCouponTemplateNewMapper;
    @Autowired
   	private IProductSkuNewService productSkuNewService;
	@Resource
    private StoreShoppingCartDelegator shoppingCartDelegator;
	
	@Autowired
	private IUserNewService supplierUserService;
	
	@Autowired
	private IBrandNewService brandNewService;
	


	/**
     * 新购物车列表接口（v3.6.3）
     * 
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/cartList/auth", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
	@Cacheable("cache")
    public JsonResponse cartList(UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
//    		data.put("list", shoppingCartDelegator.getCart(userDetail));
//	    	Map<String,Object> data = new HashMap<>();
	    	
	    	long time1 = System.currentTimeMillis();
	    	long storeId = userDetail.getId();
			if(storeId <= 0 ) {
				throw new RuntimeException("请登录后再试");
			}
			logger.info("获取购物车列表开始999,storeId:"+storeId);
			List<BrandCartItemNewVO> data =  getCartList(storeId);
			long time2 = System.currentTimeMillis();
			long time3 = time2 - time1;
    		logger.info("获取购物车列表结束，耗时time总3："+time3+",data"+JSON.toJSONString(data));
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	
   
    
    /**
     * 获取购物车信息
     * @return
     */
	private List<BrandCartItemNewVO> getCartList(long storeId) {
		StoreBusiness storeBusiness = storeBusinessService.getById(storeId);
		logger.info("没有找到门店信息，storeId："+storeId+",storeBusiness:"+JSON.toJSONString(storeBusiness));
		if(storeBusiness == null ){
			logger.info("没有找到门店信息，storeId："+storeId);
			throw new RuntimeException("没有找到门店信息，storeId："+storeId);
		}
		String phoneNumber = storeBusiness.getPhoneNumber();
		logger.info("没有找到门店信息，storeId："+storeId+",phoneNumber:"+phoneNumber);
		//1、获取购物车列表
//		List<StoreCartItemVO> cartItems = shoppingCartFacade.getCartItemVOs(userDetail);
//		 List<StoreCartItem> cartItems = shoppingCartService.getCartItems(storeId);
		long time1 = System.currentTimeMillis();
		List<CartItemNewVO> cartItemNewVOList = shoppingCartNewService.getCartItemMapList(storeId);
		if(cartItemNewVOList.size()==0){
			List<BrandCartItemNewVO> list=new ArrayList<>();
//			list.add(new BrandCartItemNewVO());
			return list;
		}
		long time2 = System.currentTimeMillis();
		logger.info("1、获取购物车列表，耗时："+(time2 - time1));
		//2、填充计算sku购物车转换商品购物车
		Map<String, CartProductItemVO> cartProductItemMap = buildSkuToProduct(cartItemNewVOList);
		long time3 = System.currentTimeMillis();
		logger.info("2、填充计算sku购物车转换商品购物车，耗时："+(time3 - time2));
		//3、按品牌进行分组
		Map<String, BrandCartItemNewVO> brandCartItemMap = buildProductToBrand(cartProductItemMap);
		long time4 = System.currentTimeMillis();
		logger.info("3、按品牌进行分组，耗时："+(time4 - time3));
		//4、填充品牌信息
		List<BrandCartItemNewVO> brandCartItemNewVOList = buildBrandInfo(phoneNumber, brandCartItemMap);
		long time5 = System.currentTimeMillis();
		logger.info("4、填充品牌信息，耗时："+(time5 - time4));
		return brandCartItemNewVOList;
	}

	/**
	 * 4、填充品牌信息
	 * @param phoneNumber
	 * @param brandCartItemMap
	 * @return
	 */
	private List<BrandCartItemNewVO> buildBrandInfo(String phoneNumber,
			Map<String, BrandCartItemNewVO> brandCartItemMap) {
		List<BrandCartItemNewVO> brandCartItemNewVOList = new ArrayList<BrandCartItemNewVO>();
		Set<String> brandIdSet = brandCartItemMap.keySet();
		for(String brandIdStr : brandIdSet){
			BrandCartItemNewVO brandCartItemNewVO = brandCartItemMap.get(brandIdStr);
			long brandId = Long.valueOf(brandIdStr);
			long time1 = System.currentTimeMillis();
			//品牌优惠券数量
			 int brandCouponCount = storeCouponTemplateNewMapper.getSupplierCouponTemplateCount(brandId);
			 brandCartItemNewVO.setBrandHasCouponCount(brandCouponCount);
			long time2 = System.currentTimeMillis();
			logger.info("4、填充品牌信息（品牌优惠券数量），耗时："+(time2 - time1));
//			批发限制提示
			UserNew supplierUser = supplierUserService.getSupplierByBrandId(brandId);
			brandCartItemNewVO.setWholesaleLimitTip(supplierUser.buildWholesaleLimitTip1() );//批发限制提示文本, 例子：10件、￥1000元 起订
			long time3 = System.currentTimeMillis();
			logger.info("4、填充品牌信息（批发限制提示），耗时："+(time3 - time2));
			//用户浏览商品权限
			int hasPermission = brandNewService.checkBrowsePermission(phoneNumber,brandId);
			brandCartItemNewVO.setHasPermission(hasPermission);
			brandCartItemNewVOList.add(brandCartItemNewVO);
			long time4= System.currentTimeMillis();
			logger.info("4、填充品牌信息（用户浏览商品权限），耗时："+(time4 - time3));
		}
		return brandCartItemNewVOList;
	}

	/**
	 * 按品牌进行分组
	 * @return
	 */
	private Map<String, BrandCartItemNewVO> buildProductToBrand(Map<String, CartProductItemVO> cartProductItemMap) {
		Map<String,BrandCartItemNewVO> brandCartItemMap = new HashMap<String,BrandCartItemNewVO>();
		Set<String> productIdSet = cartProductItemMap.keySet();
		for(String productIdStr : productIdSet){
			CartProductItemVO cartProductItemVO = cartProductItemMap.get(productIdStr);
			
			long brandId = cartProductItemVO.getBrandId();
			BrandCartItemNewVO brandCartItemNewVO = brandCartItemMap.get(String.valueOf(brandId));
			if(brandCartItemNewVO == null){
				BrandCartItemNewVO newBrandCartItemNewVO = new BrandCartItemNewVO();
				newBrandCartItemNewVO.setBrandId(brandId);
				newBrandCartItemNewVO.setBrandName(cartProductItemVO.getBrandName());
				List<CartProductItemVO> itemList = new ArrayList<CartProductItemVO>();
				itemList.add(cartProductItemVO);
				newBrandCartItemNewVO.setCartProductItemVOList(itemList);
				brandCartItemMap.put(String.valueOf(brandId), newBrandCartItemNewVO);
			}else{
				List<CartProductItemVO> itemList = brandCartItemNewVO.getCartProductItemVOList();
				itemList.add(cartProductItemVO);
			}
		}
		return brandCartItemMap;
	}
	
	/**
	 * 2、填充计算sku购物车转换商品购物车
	 * @param cartItemNewVOList
	 * @return
	 */
	private Map<String, CartProductItemVO> buildSkuToProduct(List<CartItemNewVO> cartItemNewVOList) {
		Map<String,CartProductItemVO> cartProductItemMap = new HashMap<String,CartProductItemVO>();
		//购物车商品ID列表
		List<String> productIdList = new ArrayList<String>(); 
		//1、组合购物车商品
		long time1 = System.currentTimeMillis();
		for(CartItemNewVO cartItemNewVO : cartItemNewVOList){
			long cartId = cartItemNewVO.getCartId();
			String productIdStr = String.valueOf(cartItemNewVO.getProductId());
			CartProductItemVO cartProductItem = cartProductItemMap.get(productIdStr);
			if(cartProductItem == null){
				CartProductItemVO cartProductItemVo = new CartProductItemVO(cartItemNewVO);//.buildCartItemNewVO(cartItemNewVO);
				cartProductItemVo.addCartId(cartId);
				cartProductItemMap.put(productIdStr, cartProductItemVo);
				productIdList.add(String.valueOf(cartProductItemVo.getProductId()));
			}else{
				int buyCount = cartItemNewVO.getBuyCount();
				int productBuyCount = cartProductItem.getProductBuyCount();
				cartProductItem.setProductBuyCount(productBuyCount + buyCount);
				cartProductItem.addCartId(cartId);
			}
		}
		long time2 = System.currentTimeMillis();
		logger.info("2、填充计算sku购物车转换商品购物车（组合购物车商品），耗时："+(time2 - time1));
		//2、根据商品ID获取sku列表
//		List<Map<String,String>> skuMapList = productSkuNewService.getSkuMapListByProductIds(productIdList);
		List<ProductSkuNew> skuList = productSkuNewService.getSkuListByProductIds(productIdList);
		
		long time3 = System.currentTimeMillis();
		logger.info("2、填充计算sku购物车转换商品购物车（根据商品ID获取sku列表），耗时："+(time3 - time2));
		//3、填充颜色列表和尺码列表和SKU信息
		Set<String> productIdSet = cartProductItemMap.keySet();
		for(String productIdStr : productIdSet){
			CartProductItemVO cartProductItemVO = cartProductItemMap.get(productIdStr);
			long productId = cartProductItemVO.getProductId();
			for(ProductSkuNew sku : skuList){
				long skuProductId = sku.getProductId();
				long skuId = sku.getId();
				if(skuProductId == productId){
					long colorId = sku.getColorId();
					String colorName =  sku.getColorName();
					cartProductItemVO.addColor(colorId,colorName);
					long sizeId = sku.getSizeId();
					String sizeName = sku.getSizeName();
					cartProductItemVO.addSize(sizeId,sizeName);
					int remainCount = sku.getRemainCount();
					String propertyIds = sku.getPropertyIds();
					cartProductItemVO.addSku(propertyIds,new SkuVO(skuId,colorId,colorName,sizeId,sizeName,remainCount,propertyIds));
				}
			}
		}
		long time4 = System.currentTimeMillis();
		logger.info("2、填充计算sku购物车转换商品购物车（填充颜色列表和尺码列表），耗时："+(time4 - time3));
		//4、填充被选择的颜色列表和尺码列表
		for(CartItemNewVO cartItemNewVO : cartItemNewVOList){
			long cartSkuId = cartItemNewVO.getSkuId();
			int buyCount = cartItemNewVO.getBuyCount();
			String productIdStr = String.valueOf(cartItemNewVO.getProductId());
//			CartProductItemVO cartProductItemVO = cartProductItemMap.get(productIdStr);
			
			for(ProductSkuNew sku : skuList){
//				long skuProductId = Long.parseLong(skuMap.get("prductId"));
				long skuId = sku.getId();
				if(cartSkuId == skuId){
					long colorId = sku.getColorId();
					String colorName =  sku.getColorName();
					long sizeId = sku.getSizeId();
					String sizeName = sku.getSizeName();
					String propertyIds = sku.getPropertyIds();
					CartProductItemVO cartProductItemVO = cartProductItemMap.get(productIdStr);
					if(cartProductItemVO == null){
						logger.info("productIdStr:"+productIdStr+"cartProductItemMap:"+JSON.toJSONString(cartProductItemMap)+",cartProductItemVO:"+JSON.toJSONString(cartProductItemVO));
					}else{
						cartProductItemVO.addSelectColorList(colorId,colorName);
						cartProductItemVO.addSelectSizeList(sizeId,sizeName);
						cartProductItemVO.addSelectSkuList(cartSkuId,buyCount,propertyIds);
					}
				}
			}
		}
		long time5 = System.currentTimeMillis();
		logger.info("2、填充计算sku购物车转换商品购物车（填充被选择的颜色列表和尺码列表），耗时："+(time5 - time4));
		return cartProductItemMap;
	}
   
	
}
