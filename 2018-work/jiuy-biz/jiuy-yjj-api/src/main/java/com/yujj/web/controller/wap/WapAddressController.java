/**
 * 
 */
package com.yujj.web.controller.wap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
//import com.yujj.entity.account.Address;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.AddressDelegator;

/**
 * @author LWS
 *
 */
@Login
@Controller
@RequestMapping("/wap/address")
public class WapAddressController {
    
    @Autowired
    private AddressDelegator addressDelegator;
    
    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonResponse list(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String,Object> data = addressDelegator.list(userDetail);
        
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public JsonResponse add(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        ResultCode resultCode = checkAddress(address);
        if(resultCode.getCode()<0){
        	return jsonResponse.setResultCode(resultCode);
        }
        Map<String, Object> data = addressDelegator.add(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public JsonResponse update(UserDetail userDetail, Address address) {
        JsonResponse jsonResponse = new JsonResponse();
        ResultCode resultCode = checkAddress(address);
        if(resultCode.getCode()<0){
        	return jsonResponse.setResultCode(resultCode);
        }
        Map<String, Object> data = addressDelegator.update(userDetail, address);
        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/remove")
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

    @RequestMapping(value = "/setdefault")
    @ResponseBody
    public JsonResponse setDefaultAddress(@RequestParam("id") long addressId, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        addressDelegator.setDefaultAddress(addressId, userDetail);
        return jsonResponse.setSuccessful();
    }
    
    public ResultCode checkAddress(Address address){
    	
    	if(address.getReceiverName()==null || "".equals(address.getReceiverName().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        if(address.getProvinceName()==null || "".equals(address.getProvinceName().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        if(address.getCityName()==null || "".equals(address.getCityName().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        /*
        if(address.getDistrictName()==null || "".equals(address.getDistrictName().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        */
        if(address.getAddrDetail()==null || "".equals(address.getAddrDetail().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        if(address.getAddrDetail()==null || "".equals(address.getTelephone().trim())){
        	return ResultCode.USER_INFOMATION_INCOMPLETE;
        }
        if(!address.getTelephone().matches("^1[3|4|5|7|8]\\d{9}$")){
        	return ResultCode.USER_TELLEPHONE_WRONG;
        }
        return ResultCode.COMMON_SUCCESS;
    }
}
