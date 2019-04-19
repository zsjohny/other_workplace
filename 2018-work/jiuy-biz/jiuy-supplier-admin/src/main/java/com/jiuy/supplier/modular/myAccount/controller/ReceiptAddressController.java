package com.jiuy.supplier.modular.myAccount.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;
import com.jiuyuan.service.common.ISupplierDeliveryAddress;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 收货地址控制器
 *
 * @author fengshuonan
 * @Date 2017-12-15 11:51:30
 */
@Controller
@Login
@RequestMapping("/receiptAddress")
public class ReceiptAddressController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ReceiptAddressController.class);

	private String PREFIX = "/myAccount/receiptAddress/";


	@Autowired
	private ISupplierDeliveryAddress deliveryAddressService;

	/**
	 * 跳转到收货地址首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "receiptAddress.html";
	}

	/**
	 * 跳转到添加收货地址
	 */
	@RequestMapping("/receiptAddress_add")
	public String receiptAddressAdd() {
		return PREFIX + "receiptAddress_add.html";
	}


	@RequestMapping("/receiptAddress_update")
	  public String receiptAddressUpdate() {
	    return PREFIX + "receiptAddress_edit.html";
	  }
	/**
	 * 获取收货地址列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list() {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			List<Map<String,Object>> selectList = deliveryAddressService.selectList(supplierId,page);
			page.setRecords(selectList);
			return super.packForBT(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 新增收货地址
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam("receiptInfoName") String receiptInfoName,
			@RequestParam("receiverName") String receiverName, 
			@RequestParam("address") String address,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam(value = "isDefault", required = false, defaultValue = "0") int isDefault) {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		
		try {
			SupplierDeliveryAddress supplierDeliveryAddress = new SupplierDeliveryAddress();
			supplierDeliveryAddress.setAddress(address);
			supplierDeliveryAddress.setSupplierId(supplierId);
			supplierDeliveryAddress.setCreateTime(System.currentTimeMillis());
			supplierDeliveryAddress.setDefaultAddress(isDefault);
			supplierDeliveryAddress.setPhoneNumber(phoneNumber);
			supplierDeliveryAddress.setReceiptInfoName(receiptInfoName);
			supplierDeliveryAddress.setReceiverName(receiverName);
			supplierDeliveryAddress.setUpdateTime(System.currentTimeMillis());
			if (isDefault == 1 ){
				SupplierDeliveryAddress defaultAddress = deliveryAddressService.getDefaultAddress(supplierId);
				if (defaultAddress != null) {
					deliveryAddressService.cancelDefault(defaultAddress);
				}
				supplierDeliveryAddress.setDefaultAddress(1);
			}else{
				boolean result = deliveryAddressService.checkDefault(supplierId);
				if (!result) {
					supplierDeliveryAddress.setDefaultAddress(1);
				}else{
					supplierDeliveryAddress.setDefaultAddress(0);
				}
			}
			deliveryAddressService.add(supplierDeliveryAddress);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}

	}

	/**
	 * 判断是否有默认收货地址
	 */
	@RequestMapping(value = "/checkDefault")
	@ResponseBody
	public JsonResponse checkDefault() {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			boolean result = deliveryAddressService.checkDefault(supplierId);
			if (result) {
				return jsonResponse.setSuccessful().setData(1);// 1:显示默认设置选项
			} else {
				return jsonResponse.setSuccessful().setData(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}


	/**
	 * 修改收货地址
	 */
	@RequestMapping(value = "/toUpdate")
	@ResponseBody
	public JsonResponse update(@RequestParam("addressId") long addressId) {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			SupplierDeliveryAddress deliveryAddress = deliveryAddressService.selectById(addressId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("address", deliveryAddress.getAddress());
			map.put("defaultAddress", deliveryAddress.getDefaultAddress());
			map.put("phoneNumber", deliveryAddress.getPhoneNumber());
			map.put("addressId", deliveryAddress.getId());
			map.put("receiptInfoName", deliveryAddress.getReceiptInfoName());
			map.put("receiverName", deliveryAddress.getReceiverName());
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("跳转到修改收货地址失败!");
		}
		
		
	}
	/**
	 * 修改收货地址
	 */
	@RequestMapping(value = "/saveUpdate")
	@ResponseBody
	public JsonResponse saveUpdate(@RequestParam("addressId") long addressId,	
			@RequestParam("receiptInfoName") String receiptInfoName,
			@RequestParam("receiverName") String receiverName, 
			@RequestParam("address") String address,
			@RequestParam("phoneNumber") String phoneNumber) {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		
		try {
			checkAddress(supplierId,addressId);
			
			SupplierDeliveryAddress supplierDeliveryAddress = new SupplierDeliveryAddress();
			supplierDeliveryAddress.setId(addressId);
			supplierDeliveryAddress.setAddress(address);
			supplierDeliveryAddress.setPhoneNumber(phoneNumber);
			supplierDeliveryAddress.setReceiptInfoName(receiptInfoName);
			supplierDeliveryAddress.setReceiverName(receiverName);
			supplierDeliveryAddress.setUpdateTime(System.currentTimeMillis());
			deliveryAddressService.update(supplierDeliveryAddress);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("保存收货地址失败!");
		}
		
		
	}



	/**
	 * 设置为默认收货地址
	 * @param addressId
	 * @return
	 */
	@RequestMapping(value = "/setDefault")
	@ResponseBody
	public JsonResponse setDefault(@RequestParam("addressId") long addressId) {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			checkAddress(supplierId,addressId);
			
			SupplierDeliveryAddress supplierDeliveryAddress = new SupplierDeliveryAddress();
			supplierDeliveryAddress.setId(addressId);
			supplierDeliveryAddress.setDefaultAddress(1);
			supplierDeliveryAddress.setUpdateTime(System.currentTimeMillis());
			SupplierDeliveryAddress defaultAddress = deliveryAddressService.getDefaultAddress(supplierId);
			if (defaultAddress != null) {
				deliveryAddressService.cancelDefault(defaultAddress);
			}
			deliveryAddressService.update(supplierDeliveryAddress);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("设置为默认收货地址失败!");
		}
		
	}
	
	/**
	 * 删除收货地址
	 * @param addressId
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public JsonResponse delete(@RequestParam("addressId") long addressId) {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			checkAddress(supplierId,addressId);
			
			SupplierDeliveryAddress supplierDeliveryAddress = new SupplierDeliveryAddress();
			supplierDeliveryAddress.setId(addressId);
			supplierDeliveryAddress.setStatus(-1);//删除
			deliveryAddressService.update(supplierDeliveryAddress);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("删除收货地址失败！");
		}
		
	}
	/**
	 * 判断地址是否属于该供应商
	 * @param supplierId
	 * @param addressId
	 */
	private void checkAddress(long supplierId, long addressId) {
		SupplierDeliveryAddress deliveryAddress = deliveryAddressService.selectById(addressId);
		if (deliveryAddress.getSupplierId()!= supplierId) {
			logger.info("此地址不属于该供应商不能操作！addressId："+addressId+","+"supplierId:"+supplierId);
			throw new RuntimeException("此收货地址不能进行操作！");
		}
		
	}
	
}
