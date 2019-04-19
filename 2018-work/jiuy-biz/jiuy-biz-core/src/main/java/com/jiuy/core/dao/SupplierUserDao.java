package com.jiuy.core.dao;

import java.math.BigDecimal;

public interface SupplierUserDao {

	int handleSupplierAvailableBalance(long supplierId, BigDecimal availableBalance);

	BigDecimal getAvailableBalanceById(long supplierId);

}
