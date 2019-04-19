package com.yujj.entity.account;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.account.UserCoinOperationDetail;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.OrderItem;

public class UserCoinLog implements Serializable {

    private static final long serialVersionUID = -6718137924763381737L;

    private long id;

    private long userId;

    private UserCoinOperation operation;

    private int oldAvalCoins;

    private int newAvalCoins;

    private int oldUnavalCoins;

    private int newUnavalCoins;

    private String relatedId;
    
    private String monthBegin;

    private long createTime;
    
    private int readStatus;
    
    private Coupon coupon;
    
    private List<OrderItem> orderItemList;
    
    private String unitMonth;
    
    private int count;
    
    private String content;

    public long getId() {
        return id;
    }

	public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserCoinOperation getOperation() {
        return operation;
    }

    public void setOperation(UserCoinOperation operation) {
        this.operation = operation;
    }

    public int getOldAvalCoins() {
        return oldAvalCoins;
    }

    public void setOldAvalCoins(int oldAvalCoins) {
        this.oldAvalCoins = oldAvalCoins;
    }

    public int getNewAvalCoins() {
        return newAvalCoins;
    }

    public void setNewAvalCoins(int newAvalCoins) {
        this.newAvalCoins = newAvalCoins;
    }

    public int getOldUnavalCoins() {
        return oldUnavalCoins;
    }

    public void setOldUnavalCoins(int oldUnavalCoins) {
        this.oldUnavalCoins = oldUnavalCoins;
    }

    public int getNewUnavalCoins() {
        return newUnavalCoins;
    }

    public void setNewUnavalCoins(int newUnavalCoins) {
        this.newUnavalCoins = newUnavalCoins;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

	public String getCreateTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(this.getCreateTime()));
	}
	
	public String getOperationName() {
		if(operation == null){
			return "";
		}
		if( operation.getIntValue() == 50 && relatedId != null && relatedId.length() > 0){		
				return relatedId;
		}
		UserCoinOperationDetail detail = UserCoinOperationDetail.getNameByValue(this.getOperation().getIntValue());
		if (detail == null) {
			return "";
		}
		return detail.getName();
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public String getMonthBegin() {
		return monthBegin;
	}

	public void setMonthBegin(String monthBegin) {
		this.monthBegin = monthBegin;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getUnitMonth() {
		return unitMonth;
	}

	public void setUnitMonth(String unitMonth) {
		this.unitMonth = unitMonth;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
