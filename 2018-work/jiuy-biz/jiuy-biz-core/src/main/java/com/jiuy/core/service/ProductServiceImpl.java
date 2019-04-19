
/**
 * 
 */
package com.jiuy.core.service;
/**
 * @author LWS
 */
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.ClassificationDefinitionDao;
import com.jiuy.core.dao.DictionaryDao;
import com.jiuy.core.dao.ProductDao;
import com.jiuy.core.dao.TreeDictionaryDao;
import com.jiuy.web.controller.util.JSONStringUtil;
import com.jiuyuan.entity.ClassificationDefinition;
import com.jiuyuan.entity.Dictionary;
import com.jiuyuan.entity.ProductV1;
import com.jiuyuan.entity.TreeDictionary;
import com.jiuyuan.entity.wrap.ExpressDetail;
import com.jiuyuan.entity.wrap.Specification;
import com.jiuyuan.util.CommonConstant;


@Service("prodService")
public class ProductServiceImpl implements ProductServiceV1 {


	/**
	 */
	@Resource
    private ProductDao productDao;
    
    @Resource
    private ClassificationDefinitionDao classificationDefinitionDao;
    
    @Resource
    private DictionaryDao dictionaryDao;
    
    @Resource
    private TreeDictionaryDao treeDctionaryDao;

    /*******************************************************************/
    /*
     *                      商品逻辑操作，使用旧的产品模型
     *                      创建时间： 2015.6.2
     *                      创建人：     LiuWeisheng
     *
    /******************************************************************/
    @Override
    @Transactional
    public long addProduct(ProductV1 product, String adminAccountEmail) {
        /*int valid = checkProductValid(product);
        AdminAccount adminAccount = adminAccountDao.getAdminByEmail(adminAccountEmail);
        if(valid <= 0)
            return valid;*/
        /*if(adminAccount == null) {
            return -101;
        }*/
        
        product.setElement("");
        product.setFormat("");
        product.setLocationArea("");
        product.setLocationCity("");
        product.setLocationProvince("");
        product.setMaterial("");
        product.setStyle("");
        ProductV1 newProduct = productDao.add(product);

       /* if(newProduct != null && newProduct.getId() > 0) {
            adminAccountDao.incProductCount(adminAccount.getId());
        }*/
        return newProduct.getId();
    }


    /*
    * TODO:仅实现了初步数据较验，上线前请进行完善。
    * */
    @SuppressWarnings("unused")
	private int checkProductValid(ProductV1 product) {
        CommonConstant.SaleState state = CommonConstant.SaleState.parse(product.getSaleState());
        if(state == CommonConstant.SaleState.Unknown) {
            return -1;
        }
        CommonConstant.SaleCurrency currency = CommonConstant.SaleCurrency.parse(product.getSaleCurrencyType());
        if(currency == CommonConstant.SaleCurrency.Unknown) {
            return -2;
        }
        if(product.getSaleEndTime() - product.getSaleStartTime() <= 0) {
            return -3;
        }
        long currentTime = System.currentTimeMillis();
        if(product.getSaleStartTime() < currentTime && product.getSaleEndTime() > currentTime) { //上架开始时间已到，强制上架
            product.setSaleState(CommonConstant.SaleState.Saling.value);
        }

        if(StringUtils.isBlank(product.getTitle())
                || StringUtils.isBlank(product.getDescription())
                || StringUtils.isBlank(product.getClassification())
                || StringUtils.isBlank(product.getLocationProvince())
                || StringUtils.isBlank(product.getLocationCity())
        ) {
            return -4;
        }

        //图片检查
        List<String> images = JSONStringUtil.string2ObjectList(product.getDetailImages(), String.class);
        if(CollectionUtils.isEmpty(images)) {
            return -5;
        }

        //快递检查
        if(product.getExpressFree() == 0) {
            product.setExpressDetails(null);
        } else {
            List<ExpressDetail> details = JSONStringUtil.string2ObjectList(product.getExpressDetails(), ExpressDetail.class);
            if(CollectionUtils.isEmpty(details)) {
                product.setExpressFree(0);
            }
            boolean hasExpressPrice = false;
            for(ExpressDetail detail : details) {
                if(detail.getBasePrice() > 0) {
                    hasExpressPrice = true;
                    break;
                }
            }
            if(!hasExpressPrice) {
                product.setExpressFree(1);
            }
        }

        //规格检查
        List<Specification> specifications = JSONStringUtil.string2ObjectList(product.getSpecifications(), Specification.class);
        if(CollectionUtils.isEmpty(specifications)) {
            return -6;
        }
        for(Specification specification : specifications) {

        }

        return 1;
    }

    @Override
    public List<ProductV1> getAllProducts() {
        return productDao.loadAll();
    }


	@Override
	public ProductV1 getProductByProperty(String property) {
		long id = -1;
		ProductV1 prod = null;
		try {
			id = Long.parseLong(property);
			// 通过产品ID获取产品
			prod = productDao.getById(id);
			return prod;
		} catch (NumberFormatException e) {
			//通过其他条件查询
			prod = productDao.getProductByProperty(property);
			return prod;
		}
	}


	@Override
	public boolean increaseFavorite(String productid) {
		ProductV1 prod = null;
		prod = getProductByProperty(productid);
		if(null != prod){
			prod.setFavorite(prod.getFavorite() + 1);
			return productDao.update(prod);
		}
		return false;
	}


	@Override
	public List<ProductV1> getProductList(String startPage, String pageSize,
			String productType) {
		return productDao.getProductList(startPage,pageSize,productType);
	}

	@Override
	public List<ClassificationDefinition> getClassificationDefs() {
		return classificationDefinitionDao.loadAll();
	}


	@Override
	public List<Dictionary> getDictionaries(String groupId) {
		return dictionaryDao.loadDictionaryByGroup(groupId,null);
	}


	@Override
	public List<TreeDictionary> getTreeDictionaries(String groupId,
			String parentId) {
		return treeDctionaryDao.loadTreeDictionaryByParentId(groupId, parentId);
	}

}
