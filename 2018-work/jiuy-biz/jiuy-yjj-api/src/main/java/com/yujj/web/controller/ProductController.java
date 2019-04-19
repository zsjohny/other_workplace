package com.yujj.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.Constants;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.facade.ProductFacade;
import com.yujj.business.service.ProductPropertyService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductProp;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductSKU;
import com.yujj.entity.product.ProductPropNameValuesPair;
import com.yujj.entity.product.ProductPropVO;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private ProductPropAssembler productPropAssembler;

    @Autowired
    private ProductFacade productFacade;

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public String getProduct(@PathVariable("productId") long productId, Map<String, Object> model)
        throws JsonProcessingException {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }
        model.put("product", product);
        
        List<ProductProp> baseProps = productPropertyService.getOrderedProductProperties(productId);
        List<ProductPropVO> basePropVOs = productFacade.loadProductPropVOs(baseProps);
        model.put("baseProps", basePropVOs);

        List<ProductSKU> skus = productSKUService.getProductSKUsOfProduct(productId);
        Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        for (ProductSKU sku : skus) {
            skuMap.put(sku.getPropertyIds(), sku);
            productPropVOs.addAll(sku.getProductProps());
        }
        model.put("skuMap", objectMapper.writeValueAsString(skuMap));

        productPropAssembler.assemble(productPropVOs);

        List<ProductPropNameValuesPair> skuProps = new ArrayList<ProductPropNameValuesPair>();
        Map<Long, ProductPropNameValuesPair> skuPropMap = new HashMap<Long, ProductPropNameValuesPair>();
        for (ProductPropVO propVO : productPropVOs) {
            ProductPropName propName = propVO.getPropName();
            ProductPropNameValuesPair skuProp = skuPropMap.get(propName.getId());
            if (skuProp == null) {
                skuProp = new ProductPropNameValuesPair(propName);
                skuPropMap.put(propName.getId(), skuProp);
                skuProps.add(skuProp);
            }
            skuProp.add(propVO.getPropValue());
        }
        model.put("skuProps", skuProps);

        return "product/product_detail";
    }

}
