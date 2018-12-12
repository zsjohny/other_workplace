package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.CompanyPayDao;
import com.ouliao.domain.versionfirst.CompanyPay;
import com.ouliao.repository.versionfirst.CompanyPayPageRepository;
import com.ouliao.repository.versionfirst.CompanyPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by nessary on 16-5-30.
 */
@Repository
public class ComanyDaoImpl implements CompanyPayDao {

    @Autowired
    private CompanyPayPageRepository companyPayPageRepository;

    @Autowired
    private CompanyPayRepository companyPayRepository;

    @Override
    public void saveComanyPay(CompanyPay companyPay) {
        companyPayPageRepository.saveAndFlush(companyPay);
    }

    @Override
    public void updateComanyPayByConmanyConsume(Double companyConsume) {
        companyPayRepository.updateComanyPayByConmanyConsume(companyConsume);
    }

    @Override
    public void updateComanyPayByCompanyCost(Double companyCost) {
        companyPayRepository.updateComanyPayByCompanyCost(companyCost);
    }

    @Override
    public void deleteComanyPayByIsDelted() {
        companyPayRepository.deleteComanyPayByIsDelted();
    }

    @Override
    public CompanyPay queryCompyPayByIsDelted() {
        return companyPayRepository.queryCompyPayByIsDelted();
    }
}
