package com.ground.web.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 将平台商品的name
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/RepairWxaShopProductName")
public class RepairWxaShopProductNameController {
    private static final Logger logger = LoggerFactory.getLogger(RepairWxaShopProductNameController.class);
    Log log = LogFactory.get();
    
    @Autowired
	private ShopProductMapper shopProductMapper;
    @Autowired
	private ProductNewMapper productNewMapper;

	@RequestMapping("/repairName")
    @ResponseBody
    public JsonResponse repairName() {
		JsonResponse jsonResponse = new JsonResponse();
		long id = 0;
    	try {
    		logger.info("平台商品的name修改:开始");
    		Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("own", 0);
    		List<ShopProduct> selectList = shopProductMapper.selectList(wrapper);
    		for (ShopProduct shopProduct : selectList) {
    			id = shopProduct.getId();
    			String name = shopProduct.getName();
//    			if(StringUtils.isEmpty(name)){
//    				name = "123";
//    			}else{
//    				name = "123" + name;
//    			}
    			long productId = shopProduct.getProductId();
    			if(productId > 0){
    				ProductNew productNew = productNewMapper.selectById(productId);
        			if(StringUtils.isEmpty(name)){
        				name = productNew.getName();
	    			}
    			}
    			shopProduct = new ShopProduct();
    			shopProduct.setId(id);
    			shopProduct.setName(name);
    			shopProductMapper.updateById(shopProduct);
    			logger.info("平台商品的name修改，id"+id+",name:"+name);
    		}
    		logger.info("平台商品的name修改:结束，selectList.size():"+selectList.size());
        	return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("将平台商品的name前面加上123有误:id:"+id);
			logger.error("将平台商品的name前面加上123有误:"+e.getMessage());
    		e.printStackTrace();
			return jsonResponse.setError("将平台商品的name前面加上123有误");
		}
	}

}