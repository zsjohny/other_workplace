package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.controller.AppActivityController;
import com.finace.miscroservice.activity.dao.AddressDao;
import com.finace.miscroservice.activity.po.AddressPO;
import com.finace.miscroservice.activity.service.AddressService;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class AddressServiceImpl implements AddressService {
    private Log logger = Log.getInstance(AddressServiceImpl.class);

    @Autowired
    private AddressDao addressDao;


    @Override
    @Transactional
    public void saveAddress(AddressPO addressPO) {
        this.addressDao.saveAddress(addressPO);
    }


    @Override
    public AddressPO getAddressByUserId(String userId) {
        return addressDao.getAddressByUserId(userId);
    }

    @Override
    @Transactional
    public void updateAddress(AddressPO addressPO) {

        addressDao.updateAddress(addressPO);
    }

    @Transactional
    @Override
    public Response saveAndUpdateAddress(String userId,String name,String phone,String province,String city,String county,String address, String status, String addressId) {

        try {
            AddressPO add = addressDao.getAddressById(addressId);
            if (null != add) {
                add.setUserId(Integer.valueOf(userId));
                add.setAddtime(new Date());
                add.setShengfen(province);
                add.setCity(city);
                add.setAdress(county);
                add.setAdressDeatil(address);
                add.setPhone(phone);
                add.setToUserName(name);

                if( null != status && !"".equals(status) && !"0".equals(status) ){
                    add.setStatus(status);
                    addressDao.updateAddressStatusByUser(userId, "0");
                }else{
                    add.setStatus("0");
                }
                addressDao.updateAddress(add);
                logger.info("用户{}修改收货地址,成功", userId);
                return Response.successMsg("修改地址成功");
            } else {
                AddressPO addressPO = new AddressPO();
                addressPO.setUserId(Integer.valueOf(userId));
                addressPO.setShengfen(province);
                addressPO.setCity(city);
                addressPO.setAdress(county);
                addressPO.setAdressDeatil(address);
                addressPO.setPhone(phone);
                addressPO.setToUserName(name);
                if( null != status && !"".equals(status) && !"0".equals(status)){
                    addressPO.setStatus(status);
                    addressDao.updateAddressStatusByUser(userId, "0");
                }else{
                    addressPO.setStatus("0");
                }

                addressDao.saveAddress(addressPO);
                logger.info("用户{}开始新增收货地址,成功", userId);
                return Response.successMsg("新增地址成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}新增或修改收货地址,失败", userId, e);
        }

        return Response.error();
    }

    @Override
    public Response getAddressListByUser(String userId, Integer page) {
        BasePage.setPage(page);
        List<AddressPO> list = addressDao.getAddressListByUser(userId);
        return Response.success(list);
    }


    @Transactional
    @Override
    public Response delAddressById(String userId, String addressId) {
        try {
            int del = addressDao.delAddressById(userId, addressId);
            if (del >= 0) {
                logger.info("用户{}删除地址{}成功", userId, addressId);
                return Response.successMsg("删除地址成功");
            } else {
                logger.warn("用户{}删除地址{}失败", userId, addressId);
                return Response.errorMsg("删除地址失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}删除地址{}，异常{}", userId, addressId, e);
            return Response.errorMsg("删除失败,系统异常");
        }
    }








}
