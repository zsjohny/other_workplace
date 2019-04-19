package com.yujj.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.shopping.CartItem;
import com.jiuyuan.entity.shopping.CartItemVO;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ShoppingCartFacade;
import com.yujj.business.service.ShoppingCartService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

@Controller
@Login
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartFacade shoppingCartFacade;

    @RequestMapping
    public String getCart(UserDetail userDetail, Map<String, Object> model) {
        long userId = userDetail.getUserId();

        List<CartItemVO> cartItems = shoppingCartFacade.getCartItemVOs(userId);
        model.put("cartItems", cartItems);

        return "cart/index";
    }

    @RequestMapping("/query")
    @ResponseBody
    public JsonResponse queryCart(UserDetail userDetail) {
        long userId = userDetail.getUserId();

        List<CartItemVO> cartItems = shoppingCartFacade.getCartItemVOs(userId);

        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        data.put("count", cartItems.size());
        data.put("items", itemList);
        for (CartItemVO cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Map<String, Object> productMap = new HashMap<String, Object>();
            productMap.put("productId", product.getId());
            productMap.put("productName", product.getName());
            productMap.put("productImage", product.getDetailImageArray()[0]);

            ProductSKU sku = cartItem.getSku();
            List<ProductPropVO> skuProps = cartItem.getSkuProps();
            Map<String, Object> skuMap = new HashMap<String, Object>();
            skuMap.put("skuId", sku.getId());
            skuMap.put("price", sku.getPrice());
            skuMap.put("remainCount", sku.getRemainCount());
            skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));

            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("product", productMap);
            itemMap.put("sku", skuMap);
            itemMap.put("buyCount", cartItem.getBuyCount());
            itemList.add(itemMap);
        }

        return jsonResponse.setSuccessful().setData(data);
    }

    private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
        StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProps) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addCart(@RequestParam("product_id") long productId, @RequestParam("sku_id") long skuId,
                                @RequestParam("count") int count, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        if (count <= 0) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        long userId = userDetail.getUserId();
        long time = System.currentTimeMillis();
        CartItem cartItem = shoppingCartService.getCartItem(userId, productId, skuId);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setSkuId(skuId);
            cartItem.setBuyCount(count);
            cartItem.setStatus(0);
            cartItem.setCreateTime(time);
            cartItem.setUpdateTime(time);
            shoppingCartService.addCartItem(cartItem);
        } else if (cartItem != null) {
            shoppingCartService.addCount(userId, productId, skuId, count, time);
        }

        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse removeCart(@RequestParam("product_id") long productId, @RequestParam("sku_id") long skuId,
                                   @RequestParam("count") int count, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        long userId = userDetail.getUserId();
        CartItem cartItem = shoppingCartService.getCartItem(userId, productId, skuId);
        if (cartItem == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (count >= cartItem.getBuyCount()) {
            shoppingCartService.removeCartItem(userId, productId, skuId);
        } else {
            long time = System.currentTimeMillis();
            shoppingCartService.addCount(userId, productId, skuId, -count, time);
        }

        return jsonResponse.setSuccessful();
    }
}
