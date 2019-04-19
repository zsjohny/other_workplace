package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.dao.DiscountInfoDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.DiscountInfoService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.DiscountType;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.brand.BrandLogoVO;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.shopping.DiscountInfo;

@Service
public class BrandFacade {
	private static final Logger logger = LoggerFactory.getLogger(BrandFacade.class);
	
	@Autowired
	private PropertyService propertyService;
	 
	@Autowired
	private BrandLogoServiceImpl brandLogoService;

    @Autowired
    private DiscountInfoService discountInfoService;
    
    @Autowired
    private DiscountInfoDao discountInfoDao;
    
    @Autowired
    private ProductNewMapper productNewMapper;

	@Transactional(rollbackFor = Exception.class)
	public void remove(Long[] ids) {
		Collection<Long> brandIds = Arrays.asList(ids);
        brandLogoService.remove(brandIds);
        propertyService.remove(brandIds);
	}
	
	public void updateStatus(int status, long brandId) {
		brandLogoService.updateStatus(status,brandId);		
	}

    public List<BrandLogoVO> search(String name, PageQuery pageQuery) {
        List<BrandLogo> brands = brandLogoService.search(name, pageQuery);
        Map<Long, List<DiscountInfo>> itemsByRelatedId =
            discountInfoService.discountOfRelatedIdMap(DiscountType.BRAND.getIntValue());

        List<BrandLogoVO> brandVOs = new ArrayList<BrandLogoVO>();
        for (BrandLogo brand : brands) {
            BrandLogoVO brandVO = new BrandLogoVO();
            BeanUtils.copyProperties(brand, brandVO);

            brandVO.setDiscountInfos(itemsByRelatedId.get(brand.getBrandId()));
            brandVOs.add(brandVO);
        }
        
        return brandVOs;
    }
    
	public List<BrandLogoVO> search(int status, String keywords, PageQuery pageQuery, int brandType) {
		List<BrandLogo> brands = brandLogoService.search(status,keywords, pageQuery, brandType);
        Map<Long, List<DiscountInfo>> itemsByRelatedId =
            discountInfoService.discountOfRelatedIdMap(DiscountType.BRAND.getIntValue());

        List<BrandLogoVO> brandVOs = new ArrayList<BrandLogoVO>();
        for (BrandLogo brand : brands) {
            BrandLogoVO brandVO = new BrandLogoVO();
            BeanUtils.copyProperties(brand, brandVO);

            brandVO.setDiscountInfos(itemsByRelatedId.get(brand.getBrandId()));
            brandVOs.add(brandVO);
        }
        
        return brandVOs;
		
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void addBrand(BrandLogo brandLogo, long brandProperty, String discountInfos) {
		ProductPropValue ppv = propertyService.addBrand(brandLogo.getBrandName(), brandProperty);
		brandLogo.setBrandId(ppv.getId());
		brandLogoService.addBrand(brandLogo);
		
        List<DiscountInfo> multipleDiscounts = JSON.parseArray(discountInfos, DiscountInfo.class);
    	for(DiscountInfo discountInfo : multipleDiscounts) {
            if (!isValidate(discountInfo)) {
                throw new ParameterErrorException("满减不符合规则");
            }
    		discountInfo.setRelatedId(brandLogo.getBrandId());
    	}
        discountInfoService.batchAdd(multipleDiscounts);
    	
        if (multipleDiscounts.size() > 0) {
            brandLogo.setIsDiscount(1);
            brandLogo.setExceedMoney(multipleDiscounts.get(0).getFull());
            brandLogo.setMinusMoney(multipleDiscounts.get(0).getMinus());
        }
        brandLogoService.updateBrand(brandLogo);

	}

    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(BrandLogo brandLogo, String discountInfos) {
    	discountInfoDao.delete(DiscountType.BRAND.getIntValue(), brandLogo.getBrandId());
        List<DiscountInfo> multipleDiscounts = JSON.parseArray(discountInfos, DiscountInfo.class);
        
        for (DiscountInfo discountInfo : multipleDiscounts) {
            if (!isValidate(discountInfo)) {
                throw new ParameterErrorException("满减不符合规则");
            }
        }
        discountInfoService.batchAdd(multipleDiscounts);

        if (multipleDiscounts.size() > 0) {
            brandLogo.setIsDiscount(1);
            brandLogo.setExceedMoney(multipleDiscounts.get(0).getFull());
            brandLogo.setMinusMoney(multipleDiscounts.get(0).getMinus());
        } else {
            brandLogo.setIsDiscount(0);
        }
        brandLogoService.updateBrand(brandLogo);
        //同步修改商品品牌名称,logo图,品牌类型
        updateProductBrandType(brandLogo);
	}
    /**
     * 更改该商品所属的品牌类型：1：高档 2：中档
     */
    private void updateProductBrandType(BrandLogo brandLogo) {
    	long brandId = brandLogo.getBrandId();
    	int offset = 0;
    	int limit = 1;
    	int brandType = 0;
    	String brandName = "";
    	String logo = "";
    	Page<ProductNew> page = new Page<ProductNew>(offset,limit);
    	Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
    	wrapper.eq("BrandId", brandId);
    	List<ProductNew> list = productNewMapper.selectPage(page, wrapper);
    	if(list.size() > 0){
    		brandType = list.get(0).getBrandType();
    		brandName = list.get(0).getBrandName();
    		logo = list.get(0).getBrandLogo();
    		if(brandLogo.getBrandType() != brandType ||
    		   brandLogo.getBrandName() != brandName ||
    		   brandLogo.getLogo() != logo
    				){
    			productNewMapper.batchUpdateRelativeInfo(brandId, brandLogo.getBrandType(), brandLogo.getBrandName(), brandLogo.getLogo());
    			
    		}
    		
    	}
    	
    	
    	
    	
    	
    	
    	
//    	List<Long> productIdList = new ArrayList<>();
//    	for(; ;offset++){
//    		if(offset == 0){
//    		}
//    		for(ProductNew productNew :list){
//    			productIdList.add(productNew.getId());
//    		}
//    		if(list.size() <= 0 ){
//    			break;
//    		}
//    		
//    	productNewMapper.batchUpdateBrandType(productIdList , brandLogo.getBrandType(), brandLogo.getBrandName(), brandLogo.getLogo());
//    	}
//    	if(productIdList.size() >0 ){
//    	}
    	
	}

	public boolean isValidate(DiscountInfo discountInfo) {

        double full = discountInfo.getFull();
        double minus = discountInfo.getMinus();

        if (full < minus) {
            return false;
        }

        if (full < 0 || minus < 0) {
            return false;
        }

        return true;
    }

	public BrandLogoVO getBrandVOById(long brandId) {
		BrandLogo brandLogo = brandLogoService.getByBrandId(brandId);
		
		if(brandLogo == null) {
			throw new ParameterErrorException("品牌id参数错误");
		}

		BrandLogoVO brandLogoVO = new BrandLogoVO();
		BeanUtils.copyProperties(brandLogo, brandLogoVO);
		
		List<DiscountInfo> discountInfos = discountInfoDao.itemsOfRelatedIdType(DiscountType.BRAND.getIntValue(), CollectionUtil.createList(brandId));
		brandLogoVO.setDiscountInfos(discountInfos);
		
		return brandLogoVO;
	}

}
