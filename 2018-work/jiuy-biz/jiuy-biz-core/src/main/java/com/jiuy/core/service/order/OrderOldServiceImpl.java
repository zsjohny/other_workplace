package com.jiuy.core.service.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class OrderOldServiceImpl implements OrderOldService {

    @Autowired
    private OrderNewDao orderNewDao;

    @Override
    public List<OrderNew> childOfOrderNew(long startTime, long endTime) {
        return orderNewDao.childOfOrderNew(startTime, endTime);
    }

    @Override
    public int updateMegerdSelf(Collection<Long> singleMerge) {
        if (singleMerge.size() < 1) {
            return 0;
        }
        return orderNewDao.updateMegerdSelf(singleMerge);
    }

    @Override
    public OrderNew addMergerdOrderNew(OrderNew orderNew) {
        return orderNewDao.addMergerdOrderNew(orderNew);
    }

    @Override
    public int updateMegerdChild(Collection<Long> orderNos, long orderNo) {
        if (orderNos.size() < 1) {
            return 0;
        }

        return orderNewDao.updateMegerdChild(orderNos, orderNo);
    }

    @Override
    public List<OrderNew> selfMergedOrderNew(long startTime, long endTime, int orderType) {
        return orderNewDao.selfMergedOrderNew(startTime, endTime, orderType);
    }

    @Override
    public List<OrderNew> getParentMergedOrderNews(long startTime, long endTime, int orderType) {
        return orderNewDao.getParentMergedOrderNews(startTime, endTime, orderType);
    }

	@Override
	public Map<Long, List<OrderNew>> splitMapOfOrderNos(Collection<Long> splitOrderNos) {
		if(splitOrderNos.size() < 1) {
			return new HashMap<Long, List<OrderNew>>();
		}
		List<OrderNew> orderNews = orderNewDao.orderNewsOfSplitOrderNos(splitOrderNos);
		
		if (orderNews.size() < 1) {
            return new HashMap<Long, List<OrderNew>>();
        }
		
		long lastParentId = 0;
        List<OrderNew> list = null;
        Map<Long, List<OrderNew>> map = new HashMap<Long, List<OrderNew>>();
        for (OrderNew orderNew : orderNews) {
            long parentId = orderNew.getParentId();
            if (parentId != lastParentId) {
                list = new ArrayList<OrderNew>();
                lastParentId = parentId;
                map.put(lastParentId, list);
            }
            
            list.add(orderNew);
        }
		
		return map;
	}

    @Override
    public Map<Long, List<OrderNew>> parentMergedMap(Collection<Long> parentMergedOrderNos) {
		if(parentMergedOrderNos.size() < 1) {
			return new HashMap<Long, List<OrderNew>>();
		}

    	List<OrderNew> orderNews = orderNewDao.orderNewsOfParentMergedOrderNos(parentMergedOrderNos);

        if (orderNews.size() < 1) {
            return new HashMap<Long, List<OrderNew>>();
        }
        
        long lastMergedId = 0;
        List<OrderNew> list = null;
        Map<Long, List<OrderNew>> map = new HashMap<Long, List<OrderNew>>();
        for (OrderNew orderNew : orderNews) {
            long mergedId = orderNew.getMergedId();
            if (mergedId != lastMergedId) {
                list = new ArrayList<OrderNew>();
                lastMergedId = mergedId;
                map.put(lastMergedId, list);
            }
            
            list.add(orderNew);
        }
        
        return map;
    }

    @Override
    public Map<Long, OrderNew> orderNewMapOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new HashMap<Long, OrderNew>();
        }
        List<OrderNew> orderNews = orderNewDao.orderNewsOfOrderNos(orderNos);

        Map<Long, OrderNew> map = new HashMap<Long, OrderNew>();
        for (OrderNew orderNew : orderNews) {
            map.put(orderNew.getOrderNo(), orderNew);
        }

        return map;
    }
    
    @Override
    public OrderNew orderNewOfOrderNo(long orderNo) {
        return orderNewDao.orderNewOfOrderNo(orderNo);
    }

	@Override
    public int updateOrderStatus(Collection<Long> orderNos, int orderStatus) {
        return orderNewDao.updateOrderStatus(orderNos, orderStatus);
	}

	@Override
	public List<Long> getOrderNosByOrderStatus(int orderStatus) {
		return orderNewDao.getOrderNosByOrderStatus(orderStatus, null);
	}
	
	@Override
	public List<OrderNew> allUnpaidFacepayOrderNew() {
		return orderNewDao.allUnpaidFacepayOrderNew();
	}

	@Override
    public List<OrderNew> searchOrderNews(PageQuery pageQuery, String orderNo, int orderType, long userId,
            String receiver, String phone, long startTime, long endTime,int orderStatus, Collection<Long> orderNos,int sendType) {
		if(orderNos.size() < 1) {
			orderNos = null;
		}
        return orderNewDao.searchOrderNews(pageQuery, orderNo, orderType, userId, receiver, phone, startTime, endTime,
            orderStatus, orderNos,sendType);
	}
	
	@Override
	public int searchOrderNewsCount(String orderNo, int orderType, long userId, String receiver, String phone, long startTime,
    		long endTime, int orderStatus, Collection<Long> orderNos,int sendType) {
		if(orderNos.size() < 1) {
			orderNos = null;
		}
		return orderNewDao.searchOrderNewsCount(orderNo, orderType, userId, receiver, phone, startTime, endTime, orderStatus, orderNos,sendType);
	}
	
	@Override
	public List<OrderNew> searchUndelivered(PageQuery pageQuery, String orderNo, long userId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos, int orderType) {
		
		if(orderNos.size() < 1) {
			orderNos = null;
		}
        return orderNewDao.searchUndelivered(pageQuery, orderNo, userId, receiver, phone, startTime, endTime, orderNos,orderType);
	}

	@Override
	public int searchUndeliveredCount(String orderNo, long userId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos,int orderType) {
		if(orderNos.size() < 1) {
			orderNos = null;
		}
		return orderNewDao.searchUndeliveredCount(orderNo, userId, receiver, phone, startTime, endTime, orderNos,orderType);
	}


	@Override
	public List<OrderNew> childOfCombinationOrderNos(Collection<Long> combinationOrderNos) {
		if(combinationOrderNos.size() < 1) {
			return new ArrayList<OrderNew>();
		}
		return orderNewDao.childOfCombinationOrderNos(combinationOrderNos);
	}

    @Override
    public int addPushTime(Collection<Long> orderNos, long erpTime) {
        if (orderNos.size() < 1) {
            return 0;
        }

        return orderNewDao.addPushTime(orderNos, erpTime);
    }

	@Override
	public List<OrderNew> childOfSplitOrderNos(Collection<Long> splitOrderNos) {
		if(splitOrderNos.size() < 1) {
			return new ArrayList<OrderNew>();
		}
		return orderNewDao.childOfSplitOrderNos(splitOrderNos);
	}

    @Override
    public List<OrderNew> orderNewsOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new ArrayList<OrderNew>();
        }

        return orderNewDao.orderNewsOfOrderNos(orderNos);
    }

	@Override
	public OrderNew orderNewOfServiceId(long serviceId) {
		return orderNewDao.orderNewOfServiceId(serviceId);
	}

	@Override
	public int updateCommission(OrderNew orderNew) {
		return orderNewDao.updateCommission(orderNew);
	}

	@Override
	public OrderNew orderNewOfReturnNo(long parentId) {
		return orderNewDao.orderNewOfReturnNo(parentId);
	}

	@Override
	public List<Long>  searchOfParentId(long orderNo) {
		return orderNewDao.searchOfParentId(orderNo);
	}

	@Override
	public List<OrderNew> getOrderNewOfTime(long startTime, long endTime) {
		return orderNewDao.getOrderNewOfTime(startTime,endTime);
	}
	
	@Override
	public List<OrderNew> getByUserIdStatus(long userId, OrderStatus orderStatus) {
		return orderNewDao.getByUserIdStatus(userId, orderStatus);
	}

	@Override
	public Map<Long, OrderNew> searchUndeliveredMap(OrderNewSO so, PageQuery pageQuery, Set<Long> orderNos) {
		return orderNewDao.searchUndeliveredMap(so, pageQuery, orderNos);
	}

	@Override
	public int searchUndeliveredNewCount(OrderNewSO so, Set<Long> orderNos) {
		return orderNewDao.searchUndeliveredNewCount(so, orderNos);
	}

	@Override
	public Map<Long, List<OrderNew>> getMergedChildren(Collection<Long> combineOrderNos) {
		if (combineOrderNos.size() < 1) {
			return new HashMap<>();
		}
		
		List<OrderNew> storeOrders = orderNewDao.getByMergedNos(combineOrderNos);
		Map<Long, List<OrderNew>> result_map = new HashMap<>();
		List<OrderNew> list = null;
		long last_key = -1;
		for (OrderNew storeOrder : storeOrders) {
			if (storeOrder.getMergedId() != last_key) {
				last_key = storeOrder.getMergedId();
				list = new ArrayList<>();
				result_map.put(last_key, list);
			}
			list.add(storeOrder);
		}
		return result_map;
	}
	

}
