package com.jiuyuan.constant.product;

import com.jiuyuan.util.enumeration.IntEnum;

public enum SortType implements IntEnum {
    DEFAULT(0, ""),
    
    CREATE_TIME_DESC(1, " order by Weight DESC, upSoldTime desc "),

    CREATE_TIME_ASC(2, " order by Weight DESC, upSoldTime asc"),

    PRICE_ASC(3, " order by minLadderPrice asc, Weight desc, upSoldTime asc"),
    
    PRICE_DESC(4, " order by minLadderPrice desc, Weight desc, upSoldTime desc"),

    WEIGHT_ASC(5, " order by Weight asc,upSoldTime desc "),
    
    WEIGHT_DESC(6, " order by Weight desc,upSoldTime desc "),
    
    CREATE_ASC(7, " order by upSoldTime asc , Weight desc"),

    CREATE_DESC(8, " order by upSoldTime desc , Weight desc"),
    
    VISIT_ASC(9, " order by visitCount asc , Weight desc"),
    
    VISIT_DESC(10, " order by visitCount desc , Weight desc"),

	SALES_ASC(11, " order by SaleTotalCount asc , Weight desc, upSoldTime asc"),
	
	SALES_DESC(12, " order by (SaleTotalCount+IFNULL(svp.sales_volume,0)) desc , Weight desc, upSoldTime desc"),
	
	SaleStartTime_ASC(13, " order by SaleStartTime asc , Weight desc"),

	SaleStartTime_DESC(14, " order by SaleStartTime desc , Weight desc");

	
//	jiuy 
//	  START(1, ""),
//	
//    CREATE_TIME_DESC(1, " order by CreateTime desc "),
//
//    CREATE_TIME_ASC(2, " order by CreateTime asc"),
//
//    PRICE_ASC(3, " order by Cash asc, Weight desc"),
//    
//    PRICE_DESC(4, " order by Cash desc, Weight desc"),
//
//    WEIGHT_ASC(5, " order by Weight asc "),
//
//    WEIGHT_DESC(6, " order by Weight desc "),
//    
//    REMAINCOUNT_DESC(7, " order by RemainCount desc, CreateTime desc"),
//
//    REMAINCOUNT_ASC(8, " order by RemainCount asc, CreateTime desc "),
//
//	END(8, "");
	
	
	
    private SortType(int intValue, String orderSql) {
        this.intValue = intValue;
        this.orderSql = orderSql;
    }

    private int intValue;

    private String orderSql;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getOrderSql() {
        return orderSql;
    }
    
    public static String getName(int intValue) {
		for (SortType sortType : SortType.values()) {
			if (sortType.getIntValue() == intValue) {
				return sortType.orderSql;
			}
		}
		return null;
	}

}
