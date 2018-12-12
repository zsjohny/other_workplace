package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.CompanyPayDao;
import com.ouliao.domain.versionfirst.CompanyPay;
import com.ouliao.service.versionfirst.CompanyPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nessary on 16-5-30.
 */
@Service
@Transactional
public class ComanyServicempl implements CompanyPayService {

    @Autowired
    private CompanyPayDao companyPayDao;

    @Override
    public void saveComanyPay(CompanyPay companyPay) {

        companyPayDao.saveComanyPay(companyPay);
    }

    @Override
    public void updateComanyPayByConmanyConsume(Double companyConsume) {
        companyPayDao.updateComanyPayByConmanyConsume(companyConsume);
    }

    @Override
    public void updateComanyPayByCompanyCost(Double companyCost) {
        companyPayDao.updateComanyPayByCompanyCost(companyCost);
    }

    @Override
    public void deleteComanyPayByIsDelted() {
        companyPayDao.deleteComanyPayByIsDelted();
    }

    @Override
    public CompanyPay queryCompyPayByIsDelted() {
        return companyPayDao.queryCompyPayByIsDelted();
    }
}
