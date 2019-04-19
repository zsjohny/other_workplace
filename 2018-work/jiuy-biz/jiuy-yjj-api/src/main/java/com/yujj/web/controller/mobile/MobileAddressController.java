/**
 * 
 */
package com.yujj.web.controller.mobile;

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
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.AddressDelegator;

/**
 * @author LWS
 *
 */
@Login
@Controller
@RequestMapping("/mobile/address")
public class MobileAddressController {
    
    @Autowired
    private AddressDelegator addressDelegator;
    
    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonResponse list(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String,Object> data = addressDelegator.list(userDetail);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse add(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.add(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse update(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.update(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        addressDelegator.remove(addressId, userDetail);
        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public JsonResponse getAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = addressDelegator.getAddress(addressId, userDetail);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/setdefault", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse setDefaultAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        addressDelegator.setDefaultAddress(addressId, userDetail);
        return jsonResponse.setSuccessful();
    }
}
