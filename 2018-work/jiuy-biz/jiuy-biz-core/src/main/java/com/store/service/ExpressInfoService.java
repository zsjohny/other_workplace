package com.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.ServiceTicket;
import com.store.dao.mapper.ExpressInfoMapper;
import com.store.dao.mapper.ServiceTicketMapper;

@Service
public class ExpressInfoService {

    @Autowired
    private ExpressInfoMapper expressInfoMapper;
    
    @Autowired
    private ServiceTicketMapper serviceTicketMapper;

    public ExpressInfo getUserExpressInfoByOrderNo(long userId, long orderNo) {
    	return expressInfoMapper.getUserExpressInfoByOrderNo(userId, orderNo);
    }

	public ExpressInfo getUserExpressInfoByServiceId(long id, long serviceId) {
		// TODO Auto-generated method stub
		ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(id, serviceId);
		ExpressInfo expressInfo = new ExpressInfo();
		expressInfo.setExpressSupplier(serviceTicket.getSellerExpressCom());
		expressInfo.setExpressOrderNo(serviceTicket.getSellerExpressNo());
		return expressInfo;
	}

	/**
	 * 根据快递公司英文名获取对应的中文名
	 * @param expressSupplier
	 * @return
	 */
	public String getExpressChineseNameByExpressSupplier(String expressSupplier) {
		return expressInfoMapper.getExpressChineseNameByExpressSupplier(expressSupplier);
	}

}