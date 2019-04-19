package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.DoubleUtil;

/**
 * <p>
 * 供应商客户表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@Service
public class SupplierCustomerService implements ISupplierCustomer {

	@Autowired
	private SupplierCustomerMapper supplierCustomerMapper;
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jiuyuan.service.common.ISupplierCustomer#insert(com.jiuyuan.entity.
	 * newentity.SupplierCustomer)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(SupplierCustomer supplierCustomer) {
		supplierCustomerMapper.insert(supplierCustomer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jiuyuan.service.common.ISupplierCustomer#update(com.jiuyuan.entity.
	 * newentity.SupplierCustomer)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SupplierCustomer supplierCustomer) {
		supplierCustomerMapper.updateById(supplierCustomer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jiuyuan.service.common.ISupplierCustomer#getCustomerByPhone(java.lang
	 * .String)
	 */
	@Override
	public Map<String, Object> getCustomerDetailById(long customerId) {
		SupplierCustomer customer = supplierCustomerMapper.selectById(customerId);
		Long storeId = customer.getStoreId();
		Map<String, Object> map = new HashMap<>();
		Long supplierId = customer.getSupplierId();
        
		//获取该客户对该客户下的订单，历史消费额+历史购买量
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>().eq("storeId", storeId)
				.eq("supplierId", supplierId).eq("status", 0).in("OrderStatus", "10,50,70,100").gt("PayTime", 0);// 获取所有已支付的订单
		List<StoreOrderNew> storeOrderNewList = supplierOrderMapper.selectList(wrapper);
		
		double totalPay = 0.00;//总实付金额
		double totalExpressMoney = 0.00;//总运费
		double platformTotalPreferential = 0.00;//总的平台优惠金额
		Integer totalBuyCount = 0;//总的购买数量
		for (StoreOrderNew storeOrderNew : storeOrderNewList) {
			platformTotalPreferential = DoubleUtil.add(platformTotalPreferential, storeOrderNew.getPlatformTotalPreferential());
			totalPay = DoubleUtil.add(totalPay, storeOrderNew.getTotalPay());
			totalExpressMoney = DoubleUtil.add(totalExpressMoney, storeOrderNew.getTotalExpressMoney());
			totalBuyCount += storeOrderNew.getTotalBuyCount();
		}
		
		//历史退款额+历史退货量,获取退货成功
		Wrapper<RefundOrder> refundOrderWrapper = new EntityWrapper<RefundOrder>();
		refundOrderWrapper.eq("store_id", storeId)
		                  .eq("supplier_id", supplierId)
		                  .eq("refund_status", RefundStatus.REFUND_SUCCESS.getIntValue());
		List<RefundOrder> refundOrders = refundOrderMapper.selectList(refundOrderWrapper);
		int totalBackCount = 0;//所有退货数量
		double  totalRefundCost = 0.00;//所有退货金额
        for(RefundOrder refundOrder :refundOrders){
        	totalBackCount += refundOrder.getReturnCount();
        	totalRefundCost = DoubleUtil.add(totalRefundCost, refundOrder.getRefundCost());
        }
        
		map.put("remarkName", customer.getRemarkName());
		map.put("customerGroup", customer.getGroupId());
		map.put("businessName", customer.getBusinessName());
		map.put("totalPay", DoubleUtil.round(DoubleUtil.add(totalPay, totalExpressMoney), 2));
		map.put("totalBuyCount", totalBuyCount);
		map.put("legalPerson", customer.getCustomerName());
		map.put("phoneNumber", customer.getPhoneNumber());
		StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(storeId);
		if(storeBusiness != null){
			map.put("status", storeBusiness.getStatus());
		}else{
			map.put("status", Status.NORMAL.getIntValue());
		}
		map.put("customerId", customer.getId());
		map.put("province", customer.getProvince());
		map.put("city", customer.getCity());
		map.put("businessAddress", customer.getBusinessAddress());
		map.put("totalBackCount", totalBackCount );
		map.put("totalBackCost", DoubleUtil.round(totalRefundCost , 2));
		return map;
	}

	@Override
	public SupplierCustomer getCustomerByPhone(String phoneNumber, long userId) {
		Wrapper<SupplierCustomer> wrapper = new EntityWrapper<SupplierCustomer>();
		wrapper.eq("phone_number", phoneNumber).eq("supplier_id", userId).eq("status", 0);
		List<SupplierCustomer> customers = supplierCustomerMapper.selectList(wrapper);
		SupplierCustomer customer = null;
		if (customers.size() > 0) {
			customer = customers.get(0);
		}
		return customer;
	}

	@Override
	public SupplierCustomer getCustomerById(long customerId) {

		return supplierCustomerMapper.selectById(customerId);
	}

	@Override
	public List<Map<String, Object>> getCustomerList(long userId, String customerName, String remarkName,
			String businessName, String phoneNumber, double moneyMin, double moneyMax, int countMin, int countMax,
			long groupId, String province, String city, int orderType, int customerType,
			Page<Map<String, Object>> page) {

		return supplierCustomerMapper.getCustomerList(userId, customerName, remarkName, businessName, phoneNumber,
				moneyMin, moneyMax, countMin, countMax, groupId, province, city, orderType, customerType, page);
	}

	/**
	 * 总客户数，新增客户数
	 */
	@Override
	public Map<String, Object> getNewAndOldCustomerCount(long userId) {
		Wrapper<SupplierCustomer> wrapper = new EntityWrapper<SupplierCustomer>();
		wrapper.eq("supplier_id", userId).eq("status", 0);
		Integer allCount = supplierCustomerMapper.selectCount(wrapper);
		wrapper.between("create_time", DateUtil.getTodayStart(), DateUtil.getTodayEnd());
		Integer newCount = supplierCustomerMapper.selectCount(wrapper);
		Map<String, Object> map = new HashMap<>();
		map.put("supplierCustomerAllCount", allCount);
		map.put("supplierCustomerTodayNewCount", newCount);
		return map;
	}

	@Override
	public SupplierCustomer getCustomerByStoreId(long storeId, long supplierId) {
		Wrapper<SupplierCustomer> wrapper = new EntityWrapper<>();
		wrapper.eq("store_id", storeId).eq("supplier_id", supplierId).eq("status", 0);
		List<SupplierCustomer> customers = supplierCustomerMapper.selectList(wrapper);
		return customers.size() > 0 ? customers.get(0) : null;
	}

	@Override
	public List<SupplierCustomer> selectListByStoreId(long storeId) {
		Wrapper<SupplierCustomer> wrapper = new EntityWrapper<>();
		wrapper.eq("store_id", storeId).ne("status", -1);
		List<SupplierCustomer> customers = supplierCustomerMapper.selectList(wrapper);
		return customers;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateCustomerStatus(long id, int status) {
		List<SupplierCustomer> list = selectListByStoreId(id);
		if (list.size() > 0) {
			SupplierCustomer newSupplierCustomer;
			for (SupplierCustomer supplierCustomer : list) {
				newSupplierCustomer = new SupplierCustomer();
				newSupplierCustomer.setId(supplierCustomer.getId());
				newSupplierCustomer.setStatus(status);
				update(newSupplierCustomer);
			}
		}
	}

	@Override
	public SupplierCustomer getCustomerByStoreIdOrPhoneNumber(String phoneNumber, Long storeId, Long supplierId) {
		List<SupplierCustomer> customers = supplierCustomerMapper.getCustomerByStoreIdOrPhoneNumber(phoneNumber,storeId,supplierId);
		return customers.size() > 0 ? customers.get(0) : null;
	}

}
