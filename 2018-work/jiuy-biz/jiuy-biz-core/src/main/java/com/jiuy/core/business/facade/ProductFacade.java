package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.ProductWindow;
import com.jiuyuan.entity.query.PageQuery;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class ProductFacade {
	private static final Log logger = LogFactory.get(ProductFacade.class);
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	
	@Autowired
	private BrandLogoServiceImpl brandLogoServiceImpl;
	
	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private StoreBusinessDao storeBusinessDao;
	
	@Autowired
	private NotifacationService notificationService;

	public List<Map<String, Object>> searchOverview(String brandName, String clothesNo, PageQuery pageQuery, String orderSql, String saleStatusSql, int skuStatusIntValue) {
		long brandId = 0;
		if(!StringUtils.equals(brandName, "")) {
			brandId = brandLogoServiceImpl.getBrandIdByName(brandName);
		}
		
		List<Map<String, Object>> list = productService.searchOverview(brandId, clothesNo, pageQuery, orderSql, saleStatusSql, skuStatusIntValue);
		long productId = 0;
		String detailJson = "[]";
		
		for(Map<String, Object> map : list) {
			Set<Object> categories = new HashSet<Object>();

			detailJson = (String) map.get("SummaryImages");
			JSONArray jsArray = JSONArray.parseArray(detailJson);
			//详情图第一张作为封面图片
			map.put("coverImg", jsArray.size() > 0 ? jsArray.get(0) : "");
			map.remove("SummaryImages");
			
			productId = (long) map.get("Id");

			List<Map<String, Object>> datas = productCategoryMapper.getCatNameById(productId);
			for(Map<String, Object> data : datas) {
				categories.add(data.get("CategoryName"));
			}
			map.put("categories", categories);
			
			//true:正常(不缺货)
			boolean skuStatus = productSKUMapper.getSkuStatus(productId);
			map.put("skuStatus", skuStatus);

			//true:上架
			boolean onSaleStatus = productService.isOnSale(productId);
			map.put("onSaleStatus", onSaleStatus);
		}
		
		return list;
	}

	public int searchOverviewCount(String brandName, String clothesNo, String saleStatusSql, int skuStatus) {
		long brandId = 0;
		if(!StringUtils.equals(brandName, "")) {
			brandId = brandLogoServiceImpl.getBrandIdByName(brandName);
		}
		
		return productService.searchOverviewCount(brandId, clothesNo, saleStatusSql, skuStatus);
	}

	@Transactional(rollbackFor = Exception.class)
	public int batchUpdateRestrictId(long restrictId, String productIdsString) {
		//取消关联的所有商品
		int count = productService.batchRemoveRestrictId(restrictId);
		if(StringUtils.equals("", productIdsString)) {
			return count;
		}
		Set<Long> productIds = transClothesStrToList(productIdsString);
		
		return productService.batchUpdate(restrictId, null, null, productIds); 
	}

	private Set<Long> transClothesStrToList(String productIdsString) {
		String [] productIdsStr = productIdsString.split(",");
		
		Set<Long> productIds = new HashSet<Long>();
		for(String productId : productIdsStr) {
			productIds.add(Long.parseLong(productId));
		}
		
		return productIds;
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateProduct(Product product) {
		long productId = product.getId();
		long time =System.currentTimeMillis();
		Product oldProduct = productService.getProductById(productId);
		boolean isNeedSend = checkProductNewAndOld(product,oldProduct);
		if(product.getSummaryImages()==null){
			product.setSummaryImages(new ArrayList<String>().toString());
		}
		if(product.getDetailImages()==null){
			product.setDetailImages(new ArrayList<String>().toString());
		}
		//包含物理删除,顺序必须放在这里(之后重构)
		productService.updateProduct(product);
		
		ProductShare productShare = new ProductShare();
		productShare.setShareDesc(product.getShareDesc());
		productShare.setShareImg(product.getShareImg());
		productShare.setShareTitle(product.getShareTitle());
		productShare.setCreateTime(time);
		productShare.setUpdateTime(time);
		productShare.setProductId(productId);
		if(productShare.getShareTitle() == null || productShare.getShareTitle().trim().length() == 0){
			productShare.setShareTitle(oldProduct.getName());
		}
		if(productShare.getShareDesc() == null || productShare.getShareDesc().trim().length() == 0){
			productShare.setShareDesc(productShare.getShareTitle());
		}
		if(productShare.getShareImg() == null || productShare.getShareImg().trim().length() == 0){
			productShare.setShareImg(oldProduct.getFirstImage());
		}
		productService.updateProductShare(productShare);

		long categoryId = oldProduct.getvCategoryId();
		if(categoryId != 0) {
			productCategoryMapper.delete(productId, categoryId);
		}
		
		long vCategoryId = product.getvCategoryId();
		if (vCategoryId != 0) {
			productCategoryMapper.addVirtualProduct(vCategoryId, CollectionUtil.createList(productId));
		}
		
		//更新商品的玖币抵扣百分比
		productMapper.updateProductDeductPercent(CollectionUtil.createList(product.getId()), product.getDeductPercent());
		
		productSKUMapper.updateByProductId(CollectionUtil.createList(productId), product.getClothesNumber(), product.getlOWarehouseId(), product.getlOWarehouseId2(),
				product.getSetLOWarehouseId2(),
				product.getName(), product.getBrandId(), product.getCash(), product.getWeight(), product.getPrice(), product.getMarketPrice());
//		//如果有修改，则发送商品更新系统通知
//		if(isNeedSend){
////			获取上架指定商品的商家ID集合
//			List<Long> storeIdList = productSKUMapper.getStoreIdsByProductId(productId);
//			notificationService.updateProductSendNotification(product.getId(),storeIdList);
//		}
	}
	
	/**
	 * 判断新旧商品属性是否更改
	 * 比较修改前的商品和修改后的商品的主图、标题、批发价、商品详情图片、商品尺码图片、商品视频等6个属性是否有改变
	 * @param product
	 * @param oldProduct
	 * @return
	 */
	private boolean checkProductNewAndOld(Product product, Product oldProduct) {
		boolean flag = false;
//		if(!StringUtils.isEmpty(product.getName()) && !StringUtils.isEmpty(oldProduct.getName())){
			if(!product.getName().equals(oldProduct.getName())){
				flag = true;
			}
//		}
//		if(product.getWholeSaleCash()>=0 &&  oldProduct.getWholeSaleCash()>=0){
			if(product.getWholeSaleCash()!=oldProduct.getWholeSaleCash()){
				flag = false;
			}
//		}
//		if(!StringUtils.isEmpty(product.getDetailImages()) && !StringUtils.isEmpty(oldProduct.getDetailImages())){
			if(!product.getDetailImages().equals(oldProduct.getDetailImages())){
				flag = true;
			}
//		}
//		if(!StringUtils.isEmpty(product.getSummaryImages()) && !StringUtils.isEmpty(oldProduct.getSummaryImages())){
			if(!product.getSummaryImages().equals(oldProduct.getSummaryImages())){
				return true;
			}
//		}
//		if(!StringUtils.isEmpty(product.getSizeTableImage()) && !StringUtils.isEmpty(oldProduct.getSizeTableImage())){
			if(!product.getSizeTableImage().equals(oldProduct.getSizeTableImage())){
				flag = true;
			}
//		}
		if(StringUtils.isEmpty(oldProduct.getVideoUrl())){
			if(!StringUtils.isEmpty(product.getVideoUrl())){
				flag = true;
			}
		}else{
			if(!StringUtils.isEmpty(oldProduct.getVideoUrl())){
				if(!product.getVideoUrl().equals(oldProduct.getVideoUrl())){
					flag = true;
				}
			}else{
				flag = true;
			}
		}
			
//		}
		return flag;
	}

	
	
//	public void updateShopProduct(Product product){
//		ShopProduct shopProduct = new ShopProduct();
//		shopProduct.setProductId(product.getId());
//		shopProduct.setName(product.getName());
//		shopProduct.setXprice(product.getWholeSaleCash());
//		shopProduct.setMarketPrice((double) product.getMarketPrice());
//		shopProduct.setClothesNumber(product.getClothesNumber());
//		shopProduct.setVideoUrl(product.getVideoUrl());

//		shopProduct.setUpdateTime(System.currentTimeMillis());
//		productMapper.updateShopProduct(shopProduct);
//	}
	

	@Transactional(rollbackFor = Exception.class)
	public void updateWholeSaleProduct(Product product){
		
		productService.updateWholeSaleProduct(product);
		
	}

	public ProductWindow templateSearch(String clothesNo, Long brandId) {
		List<Product> products = productService.search(clothesNo, brandId);
		if(products.size() < 1) {
			return null;
		}
		
		Product product = products.get(0);
		ProductWindow productWindow = new ProductWindow();
		productWindow.setBrandId(product.getBrandId());
		
		return null;
	}


	
//	/**
//	 * 发送系统消息
//	 * @param storeId
//	 * @param title
//	 * @param content
//	 */
//	private void sendUpdProductMsg(Product product) {
//		Notification Notification = new Notification();
//		Notification.setTitle("商品上新提醒");
//		Notification.setAbstracts("本次上新商品:"+product.getName());
//		Notification.setPushStatus(1);
//		Notification.setImage("");
//		Notification.setType("3");
//		Notification.setStatus(0);
//		Notification.setLinkUrl(product.getId()+"");
//		notificationService.addNotification(Notification);
//	}

//	/**
//	 * 判断是否需要发送通知
//	 *  * 1、修改商品的主图、标题、批发价、商品描述图文等属性
//	 * @param oldProduct
//	 * @param product
//	 */
//	private boolean checkNeedSend(Product oldProduct, Product product) {
//		boolean flag = false;
//		if(!product.getName().equals(oldProduct.getName())){
//			flag = true;
//		}
//		if(product.getWholeSaleCash()!=oldProduct.getWholeSaleCash()){
//			flag = false;
//		}
//		if(!product.getVideoUrl().equals(oldProduct.getVideoUrl())){
//			flag = true;
//		}
//		if(!product.getDetailImages().equals(oldProduct.getDetailImages())){
//			flag = true;
//		}
//		if(!product.getSummaryImages().equals(oldProduct.getSummaryImages())){
//			return true;
//		}
//		if(!product.getSizeTableImage().equals(oldProduct.getSizeTableImage())){
//			flag = true;
//		}
//		return flag;
//	}
}
