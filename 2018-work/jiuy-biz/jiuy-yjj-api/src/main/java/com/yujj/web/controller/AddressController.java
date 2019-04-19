package com.yujj.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.account.Address;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.YJJUserAddressService;
import com.yujj.entity.account.UserDetail;

@Controller
@Login
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private YJJUserAddressService userAddressService;

    @RequestMapping
    public String list(UserDetail userDetail, Map<String, Object> model) {
        long userId = userDetail.getUserId();

        List<Address> addresses = userAddressService.getUserAddresses(userId);
        model.put("addresses", addresses);

        return "address/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse add(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();

        long userId = userDetail.getUserId();
        userAddressService.addUserAddress(userId, address);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);

        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse update(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();

        long userId = userDetail.getUserId();
        userAddressService.updateUserAddress(userId, address);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);

        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        userAddressService.removeAddress(userDetail.getUserId(), addressId);

        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public JsonResponse getAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        Address address = userAddressService.getUserAddress(userDetail.getUserId(), addressId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);

        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/setdefault", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse setDefaultAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        userAddressService.setDefaultAddress(userDetail.getUserId(), addressId);

        return jsonResponse.setSuccessful();
    }

}
