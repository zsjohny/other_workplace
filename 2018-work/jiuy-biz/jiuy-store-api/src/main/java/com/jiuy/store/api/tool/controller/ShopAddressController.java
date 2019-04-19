/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.AddressDelegator;

/**
 * @author 
 * 地址Controller
 */
@Login
@Controller
@RequestMapping("/shop/address")
public class ShopAddressController {
    
    @Autowired
    private AddressDelegator addressDelegator;
    
    @RequestMapping(value = "/list/auth")
    @ResponseBody
    public JsonResponse list(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String,Object> data = addressDelegator.list(userDetail);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/add/auth")
    @ResponseBody
    public JsonResponse add(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.add(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/update/auth")
    @ResponseBody
    public JsonResponse update(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.update(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/remove/auth")
    @ResponseBody
    public JsonResponse remove(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        addressDelegator.remove(addressId, userDetail);
        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/get/auth")
    @ResponseBody
    public JsonResponse getAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.getAddress(addressId, userDetail);
        return jsonResponse.setSuccessful().setData(data);
    }
//    @RequestMapping(value = "/getCityList")
//    @ResponseBody
//    public JsonResponse getCityList(@RequestParam("provinceCode") long provinceCode, UserDetail userDetail) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = addressDelegator.getCityList(provinceCode);
//    	return jsonResponse.setSuccessful().setData(data);
//    }
//    @RequestMapping(value = "/getDistrictList")
//    @ResponseBody
//    public JsonResponse getDistrictList(@RequestParam("cityCode") long cityCode, UserDetail userDetail) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = addressDelegator.getDistrictList(cityCode);
//    	return jsonResponse.setSuccessful().setData(data);
//    }
//    
//    @RequestMapping(value = "/getProvinceList")
//    @ResponseBody
//    public JsonResponse getProvinceList(UserDetail userDetail) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = addressDelegator.getProvinceList();
//    	return jsonResponse.setSuccessful().setData(data);
//    }

    @RequestMapping(value = "/setdefault/auth", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse setDefaultAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        addressDelegator.setDefaultAddress(addressId, userDetail);
        return jsonResponse.setSuccessful();
    }
}
