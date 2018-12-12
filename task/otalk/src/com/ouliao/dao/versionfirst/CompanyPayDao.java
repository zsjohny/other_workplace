package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.CompanyPay;

/**
 * Created by nessary on 16-5-30.
 */
public interface CompanyPayDao {

    void saveComanyPay(CompanyPay companyPay);

    void updateComanyPayByConmanyConsume(Double companyConsume);
    void updateComanyPayByCompanyCost(Double companyCost);

    void deleteComanyPayByIsDelted();


    CompanyPay queryCompyPayByIsDelted();
}
