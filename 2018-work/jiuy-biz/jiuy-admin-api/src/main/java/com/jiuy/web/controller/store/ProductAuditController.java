/**
 * 
 */
package com.jiuy.web.controller.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.dao.BrandBusinessDao;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.ProductAudit;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.service.common.IProductAuditService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/productAudit")
@Controller
@Login
public class ProductAuditController{

	private static final Logger logger = LoggerFactory.getLogger(ProductAuditController.class);
	@Autowired
	private IProductAuditService productAuditService;
	@Autowired
	private IProductNewService productNewService;
	
	
	
	@Autowired
	private BrandBusinessDao brandBusinessDao;
	
	@Autowired
	private BrandLogoServiceImpl brandLogoService;
	
	@Autowired
	private LOWarehouseService lOWarehouseService;
	
	/**
     * 商品审核不通过接口
     * @param productAuditId 商品审核ID
     * @param noPassReason 审核不通过理由
     * 
     * @return
     */
    @RequestMapping("/productAuditNoPass")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse productAuditPass(
    		@RequestParam(required = true)long productAuditId,
    		@RequestParam(required = true)String noPassReason,
    		HttpServletRequest request) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		productAuditService.productAuditNoPass(productAuditId,noPassReason);
            return new JsonResponse().setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
	
	/**
     * 商品审核通过接口
     * @param productAuditId 商品审核ID
     * @return
     */
    @RequestMapping("/productAuditPass")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse productAuditPass(
    		@RequestParam(required = true)long productAuditId,
    		HttpServletRequest request) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		
    		productAuditService.productAuditPass(productAuditId);
            return new JsonResponse().setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
	
	
	/**
     * 商品审核详情接口
     * @param productAuditId 商品审核ID
     * @return
     */
    @RequestMapping("/getProductAuditInfo")
    @ResponseBody
    public JsonResponse getProductAuditInfo(
    		@RequestParam(required = false, defaultValue = "0")long productAuditId,
    		HttpServletRequest request) {
    	ProductAudit audit = productAuditService.getProductAuditById(productAuditId);
    	Map<String,Object> map =  new HashMap<String,Object>();
    	map.put("productAuditId", audit.getId());//商品审核ID
    	map.put("productId", audit.getProductId());//商品ID
    	map.put("productName", audit.getProductName());//商品名称
    	map.put("clothesNumber", audit.getClothesNumber());//商品款号
    	map.put("ladderPriceJson", audit.getLadderPriceJson());//阶梯价JSON
    	map.put("showcaseImgs", audit.getShowcaseImgs());//橱窗图片集合，英文逗号分隔
    	map.put("detailImgs", audit.getDetailImgs());//详情图片集合，英文逗号分隔
    	map.put("brandName", audit.getBrandName());//品牌名称
    	map.put("brandLogo", audit.getBrandLogo());//品牌Logo
    	map.put("videoUrl", audit.getVideoUrl());//商品视频url
    	map.put("skuJSON", audit.getSkuJSON());//商品SKUJSON

		ProductNew product = productNewService.getProductById(audit.getProductId());
		map.put("vedioMain",product.getVedioMain());
		map.put ("memberLadderPriceJson", product.getMemberLadderPriceJson ());
		map.put ("memberLevel", product.getMemberLevel ());

    	//搭配商品
		List<Map<String,String>> matchProductList = new ArrayList<Map<String,String>>();
		String matchProductIds = audit.getMatchProductIds();//搭配推荐商品ID集合，英文逗号分隔
		if(StringUtils.isNotEmpty(matchProductIds)){
			String[] productIdArr = matchProductIds.split(",");
			for(String togetherProductId : productIdArr){
				ProductNew togetherProduct = productNewService.getProductById(Long.parseLong(togetherProductId));
				Map<String,String> togetherProductMap = new HashMap<String,String>();
				togetherProductMap.put("productId", String.valueOf(togetherProduct.getId()));//商品ID
				togetherProductMap.put("clothesNumber", togetherProduct.getClothesNumber());//商品款号
				togetherProductMap.put("name", togetherProduct.getName());//商品名称
				togetherProductMap.put("mainImg", togetherProduct.getMainImg());//商品主图
				togetherProductMap.put("ladderPriceJson", togetherProduct.getLadderPriceJson());//阶梯价格JSON
				togetherProductMap.put("maxLadderPrice", String.valueOf(togetherProduct.getMaxLadderPrice()));//最大阶梯价格
				togetherProductMap.put("minLadderPrice", String.valueOf(togetherProduct.getMinLadderPrice()));//最小阶梯价格
				togetherProductMap.put("vedioMain",togetherProduct.getVedioMain());
				matchProductList.add(togetherProductMap);
			}
		}
		map.put("matchProductList", matchProductList);//搭配推荐商品列表
        return new JsonResponse().setSuccessful().setData(map);
    }
    
    /**
     * 供应商详情接口
     */
    @RequestMapping("/getSupplierInfo")
    @ResponseBody
    public JsonResponse getSupplierInfo(
    		@RequestParam(required = true)long supplierId,
    		HttpServletRequest request) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
//    	ProductAudit audit = productAuditService.getProductAuditById(supplierId);
    	Map<String,Object> map =  new HashMap<String,Object>();
    	BrandBusiness brandBusiness = brandBusinessDao.getById(supplierId);
    	if(brandBusiness == null){
    		return jsonResponse.setError("找不到供应商信息！");
    	}
    	map.put("businessName", brandBusiness.getBusinessName());//商家名称
    	map.put("status", brandBusiness.getStatus()==0?"可用":"禁用");//账户状态  0正常	1禁用
    	
    	BrandLogo brandLogo = brandLogoService.getByBrandId(brandBusiness.getBrandId());
    	if(brandLogo !=null){
    		map.put("brandLogo", brandLogo.getLogo());//品牌LOGO
    		map.put("brandName", brandLogo.getBrandName());//关联品牌
    	}
    	map.put("companyName", brandBusiness.getCompanyName());//公司名称
    	map.put("businessAddress", brandBusiness.getBusinessAddress());//商家地址
    	map.put("licenseNumber", brandBusiness.getLicenseNumber());//营业执照号或者统一社会信用代码
    	map.put("taxId", brandBusiness.getTaxId());//税务登记号
    	map.put("legalPerson", brandBusiness.getLegalPerson());//法定代表人
    	map.put("idCardNumber", brandBusiness.getIdCardNumber());//身份证号
    	LOWarehouse loWarehouse = lOWarehouseService.loadById(brandBusiness.getlOWarehouseId());
    	if(loWarehouse !=null){
    		map.put("lOWarehouseName", loWarehouse.getName());// 仓库name
    	}
    	map.put("settlementDate", brandBusiness.getSettlementDate());//结算日期
    	
    	map.put("bankCardFlag", brandBusiness.getBankCardFlag());//是否开通银行汇款 0：未开通 1：已开通
    	map.put("bankAccountName", brandBusiness.getBankAccountName());//银行开户名称
    	map.put("bankName", brandBusiness.getBankName());//开户银行名称
   		map.put("bankAccountNo", brandBusiness.getBankAccountNo());//银行账户号
    	map.put("alipayFlag", brandBusiness.getAlipayFlag());//是否开通支付宝汇款 0：未开通 1：已开通
    	map.put("alipayAccount", brandBusiness.getAlipayAccount());//支付宝账户
    	map.put("alipayName", brandBusiness.getAlipayName());//支付宝关联人
    
    	map.put("phoneNumber", brandBusiness.getPhoneNumber());//手机号码
//    	map.put("productAuditId", audit.getId());//商品审核ID
//    	map.put("productId", audit.getProductId());//商品ID
//    	map.put("productName", audit.getProductName());//商品名称
//    	map.put("clothesNumber", audit.getClothesNumber());//商品款号
//    	map.put("ladderPriceJson", audit.getLadderPriceJson());//阶梯价JSON
//    	map.put("showcaseImgs", audit.getShowcaseImgs());//橱窗图片集合，英文逗号分隔
//    	map.put("detailImgs", audit.getDetailImgs());//详情图片集合，英文逗号分隔
//    	map.put("brandName", audit.getBrandName());//品牌名称
//    	map.put("brandLogo", audit.getBrandLogo());//品牌Logo
//    	map.put("videoUrl", audit.getVideoUrl());//商品视频url
//    	map.put("skuJSON", audit.getSkuJSON());//商品SKUJSON
        return new JsonResponse().setSuccessful().setData(map);
    }
	
	
	 
	 /**
     * 商品审核列表接口
     *  审核类型：0买手审核、1客服审核
     * @param auditState 审核状态：-2客服全部、-1买手全部、0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
     * @param productId 商品ID
     * @param productName 商品名称
     * @param priceBegin 价格开始
     * @param priceEnd 价格结束
     * @param clothesNumber 款号
     * @param brandName 品牌名称
     * @param current 当前页数
     * @param size 每页数量
     * @return
     */
    @RequestMapping("/getSearchProductAuditList")
    @ResponseBody
    public JsonResponse getSearchProductAuditList(
    		@RequestParam(required = true)int auditState,
    		@RequestParam(required = false, defaultValue = "0")long productId,
    		@RequestParam(required = false, defaultValue = "")String productName,
    		@RequestParam(required = false, defaultValue = "")String priceBegin,
    		@RequestParam(required = false, defaultValue = "")String priceEnd,
    		@RequestParam(required = false, defaultValue = "")String clothesNumber,
    		@RequestParam(required = false, defaultValue = "0")long submitAuditTimeBegin,
    		@RequestParam(required = false, defaultValue = "0")long submitAuditTimeEnd,
    		@RequestParam(required = false, defaultValue = "")String brandName,
    		@RequestParam(required = false, defaultValue = "1")int current, 
    		@RequestParam(required = false, defaultValue = "10")int size ) {
    	logger.info("auditState:"+auditState+",productId:"+productId+",productName:"+productName+",priceEnd:"+priceEnd+",clothesNumber:"+clothesNumber+",submitAuditTimeBegin"
    		+submitAuditTimeBegin+",submitAuditTimeEnd:"+submitAuditTimeEnd+",brandName:"+brandName+",current:"+current+",size:"+size);
    	Page page = new Page(current,size);
    	//审核信息
        List<ProductAudit> list = productAuditService.getSearchProductAuditList(page ,auditState,productId, productName, priceBegin, priceEnd, clothesNumber,submitAuditTimeBegin,submitAuditTimeEnd,brandName);

		//商品信息
		List<Long> productIds = new ArrayList<> (list.size ());
		list.forEach (productAudit -> {
        	productIds.add (productAudit.getProductId ());
		});
		List<ProductNew> productList = productNewService.selectByIds (productIds);

		List<Map<String,Object>> records = new ArrayList<> (list.size ());
        for(ProductAudit audit : list){
        	Map<String,Object> map =  new HashMap<String,Object>();
        	map.put("productAuditId", audit.getId());//商品审核ID
        	map.put("productId", audit.getProductId());//商品ID
        	map.put("productName", audit.getProductName());//商品名称
        	map.put("clothesNumber", audit.getClothesNumber());//商品款号
        	map.put("ladderPriceJson", audit.getLadderPriceJson());//阶梯价JSON
        	map.put("categoryName", audit.getCategoryName());//品类
        	map.put("brandName", audit.getBrandName());//品牌名称
        	map.put("auditState", audit.getAuditState());//审核状态值：0待审核、1已通过、2已拒绝
        	map.put("noPassReason", audit.getNoPassReason());//审核不通过理由
        	map.put("auditTime", DateUtil.parseLongTime2Str(audit.getAuditTime()));//审核时间
        	map.put("submitAuditTime", DateUtil.parseLongTime2Str(audit.getSubmitAuditTime()));//提交审核时间
        	map.put("supplierId", audit.getSupplierId());//供应商ID

			productList.forEach (product -> {
				if (product.getId ().equals (audit.getProductId ())) {
					String json = product.getMemberLadderPriceJson ();
					map.put ("memberLadderPriceJson", json == null ? "" : json);
					map.put ("memberLevel", product.getMemberLevel ());
				}
			});

        	records.add(map);
        }
        page.setRecords(records);
        return new JsonResponse().setSuccessful().setData(new SmallPage(page));
    }
    
	
}
