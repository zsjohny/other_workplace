package com.jiuy.core.service.storeorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.BrandBusinessDao;
import com.jiuy.core.dao.StoreOrderMessageBoardDao;
import com.jiuy.core.dao.mapper.BrandOrderDao;
import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月11日 下午4:03:47
*/
@Service
public class StoreOrderMessageBoardServiceImpl implements StoreOrderMessageBoardService {

	@Resource
	private StoreOrderMessageBoardDao storeOrderMessageBoardDao;
	
	@Autowired
	private StoreOrderService storeOrderService;
	
	@Autowired
	private BrandOrderDao brandOrderDao;
	
	@Autowired
	private BrandBusinessDao brandBusinessDao;
	
	@Override
	public int add(StoreOrderMessageBoard storeOrderMessageBoard) {
		storeOrderMessageBoard.setCreateTime(System.currentTimeMillis());
		return storeOrderMessageBoardDao.add(storeOrderMessageBoard);
	}

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, long orderNo, List<Integer> types, long adminId, long startTimeL,
			long endTimeL) {
		List<Map<String, Object>> results = new ArrayList<>();
		
		List<StoreOrderMessageBoard> boards = storeOrderMessageBoardDao.search(pageQuery, orderNo, types, adminId, startTimeL, endTimeL);
		
		if (boards.size() > 0) {
			StoreOrder storeOrder = storeOrderService.orderOfOrderNo(boards.get(0).getOrderNo());
			BrandOrder brandOrder = brandOrderDao.getByOrderNo(storeOrder.getBrandOrder());
			BrandBusiness brandBusiness = brandBusinessDao.getById(brandOrder.getBrandBusinessId());
			String brandBusinessName = brandBusiness == null ? "" : brandBusiness.getUserName();
			
			for (StoreOrderMessageBoard item : boards) {
				Map<String, Object> result = new HashMap<>();
				result.put("operation", getOperation(item.getType()));
				result.put("name", getOperationOwner(item.getType(), item.getAdminName(), brandBusinessName));
				result.put("message", item.getMessage());
				result.put("create_time", DateUtil.convertMSEL(item.getCreateTime()));
				
				results.add(result);
			}
		}
		
		return results;
	}

	private String getOperationOwner(int type, String adminName, String brandBusinessName) {
		switch (type) {
		case 2:
		case 5:
		case 6:
			return brandBusinessName;
		case 3:
		case 4:
		case 7:
			return adminName;
		}
		return "";
	}

	private String getOperation(int type) {
		switch (type) {
		case 2:
			return "品牌发单";
		case 3:
			return "审核通过";
		case 4:
			return "发货审核不通过";
		case 5:
			return "品牌取消订单";
		case 6:
			return "修改物流信息";
		case 7:
			return "后台撤回发单";
		}
		
		return "";
	}

}
