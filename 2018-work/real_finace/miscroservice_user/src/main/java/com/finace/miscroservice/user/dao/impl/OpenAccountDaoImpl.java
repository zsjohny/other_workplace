package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.OpenAccountDao;
import com.finace.miscroservice.user.entity.po.Account;
import com.finace.miscroservice.user.mapper.OpenAccountMapper;
import com.finace.miscroservice.user.po.CreditGoAccountPO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OpenAccountDaoImpl implements OpenAccountDao {

    @Resource
    private OpenAccountMapper openAccountMapper;

    @Override
    @Transactional
    public void openAccount(String txDate,String txTime,String seqNo,String userId, String idType, String idNo, String name, String gender, String mobile, String acctUse, String smsFlag, String identity, String coinstName) {
        openAccountMapper.openAccount( txDate, txTime, seqNo,userId, idType, idNo,  name, gender, mobile, acctUse, smsFlag, identity, coinstName);
    }

    @Override
    public CreditGoAccountPO findOpenAccountByUserId(String userId) {
        return openAccountMapper.findOpenAccountByUserId(userId);
    }

    @Override
    @Transactional
    public void upOpenAccount(String txDate,String txTime,String seqNo,String userId, String idType, String idNo, String name, String gender, String mobile, String acctUse, String smsFlag, String identity, String coinstName) {
        openAccountMapper.upOpenAccount(txDate,txTime,seqNo,userId, idType, idNo,  name, gender, mobile, acctUse, smsFlag, identity, coinstName);
    }

    @Override
    @Transactional
    public void upCardNoByAccountId(String accountId, String cardNo, String seqNo, String txTime, String txDate) {
        openAccountMapper.upCardNoByAccountId(accountId,cardNo,seqNo,txTime,txDate);
    }

    @Override
    public CreditGoAccountPO findOpenAccountByAccountId(String accountId) {
        return openAccountMapper.findOpenAccountByAccountId(accountId);
    }

    @Override
    @Transactional
    public void upSetPass(String accountId) {
        openAccountMapper.upSetPass(accountId);
    }

    @Override
    @Transactional
    public void upCardNo(String accountId, String cardNo) {
        openAccountMapper.upCardNo(accountId,cardNo);
    }

    @Override
    @Transactional
    public void delCardNo(String reAccountId) {
        openAccountMapper.delCardNo(reAccountId);
    }

    @Override
    @Transactional
    public void upMoney(String reAccountId, Double availBal, Double currBal, Double iceMoney) {
        openAccountMapper.upMoney(reAccountId,availBal,currBal,iceMoney);
    }

    @Override
    @Transactional
    public void upOpenAccountCutPayment(String accountId, String txAmount) {
        openAccountMapper.upOpenAccountCutPayment(accountId,txAmount);
    }

    @Override
    @Transactional
    public void upPaymentAuth(String accountId) {
        openAccountMapper.upPaymentAuth(accountId);
    }

    @Override
    @Transactional
    public void upRepayAuth(String accountId) {
        openAccountMapper.upRepayAuth(accountId);

    }

    @Override
    @Transactional
    public void updateCreditAccount(CreditGoAccountPO creditGoAccountPO) {
        openAccountMapper.updateCreditAccount(creditGoAccountPO);
    }

    @Override
    public List<String> findAccountByIdNo(String idNo) {
        return openAccountMapper.findAccountByIdNo(idNo);
    }
}
