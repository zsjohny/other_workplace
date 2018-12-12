package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.user.dao.AccountCashDao;
import com.finace.miscroservice.user.dao.AccountDao;
import com.finace.miscroservice.user.dao.AccountLogDao;
import com.finace.miscroservice.user.po.AccountCashPO;
import com.finace.miscroservice.user.po.AccountLogPO;
import com.finace.miscroservice.user.po.AccountPO;
import com.finace.miscroservice.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 账户service层实现类
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountLogDao accountLogDao;
    @Autowired
    private AccountCashDao accountCashDao;


    @Override
    public AccountPO getAccountByUserId(String userId) {
        return accountDao.getAccountByUserId(userId);
    }


    @Override
    public List<AccountLogPO> getZjmxByUserId(Map<String, Object> map, int page) {
        return accountLogDao.getZjmxByUserId(map, page);
    }

    @Override
    public AccountCashPO getAccountCashById(int id) {
        return accountCashDao.getAccountCashById(id);
    }

    @Override
    public int addAccountCash(AccountCashPO accountCashPO) {
        return accountCashDao.addAccountCash(accountCashPO);
    }

    /**
     * 提现
     * @param ac
     * @param alog
     * @param type
     * @return
     */
    public int cashing(AccountCashPO ac, AccountLogPO alog, int type){

        //取现返回成功
        if(type==1){
            /**
             * 更新提现记录表
             **rjg0510 提现时做同步处理
             */
            synchronized (this) {
                int count = accountCashDao.updateAccountCash(ac);
                if(count>0){

                    ac = accountCashDao.getAccountCashById(ac.getId());
                    /**
                     * 更新用户资金表
                     */
                    AccountPO account = accountDao.getAccountByUserId(String.valueOf(ac.getUserId()));
                    account.setTotal(account.getTotal()-Double.valueOf(ac.getTotal()));
                    account.setUseMoney(account.getUseMoney()-Double.valueOf(ac.getTotal()));
                    accountDao.updateAccount(account);
                    account = accountDao.getAccountByUserId(ac.getUserId()+"");
                    /**
                     * 新增用户资金记录表
                     */
                    alog.setUser_id(ac.getUserId());
                    alog.setType("cash_success");
                    alog.setToUser(1);
                    alog.setTotal(account.getTotal());
                    alog.setMoney(Double.valueOf(ac.getTotal()));
                    alog.setUseMoney(account.getUseMoney());
                    alog.setNoUseMoney(account.getNoUseMoney());
                    alog.setCollection(account.getCollection());
                    alog.setRemark("通过第三方托管提现"+ac.getTotal()+"成功, 其中手续费扣除"+ ac.getFee());
                    int acid = accountLogDao.addAccountLog(alog);
                    return acid;
                }else{



                    return 0;
                }
            }
            //取现失败，解冻
        }else{
            int count = accountCashDao.updateAccountCash(ac);
            if(count>0){
                ac = accountCashDao.getAccountCashById(ac.getId());
                ac.setStatus(9);  //提现失败
                /**
                 * 更新提现记录表
                 */
                AccountPO account = accountDao.getAccountByUserId(ac.getUserId()+"");
                account.setTotal(account.getTotal()+Double.valueOf(ac.getTotal()));
                account.setUseMoney(account.getUseMoney()+Double.valueOf(ac.getTotal()));
                accountDao.updateAccount(account);
                account = accountDao.getAccountByUserId(ac.getUserId()+"");

                /**
                 * 新增用户资金记录表
                 */
                alog.setUser_id(ac.getUserId());
                alog.setType("cash_fail");
                alog.setToUser(1);
                alog.setTotal(account.getTotal());
                alog.setMoney(Double.valueOf(ac.getTotal()));
                alog.setUseMoney(account.getUseMoney());
                alog.setNoUseMoney(account.getNoUseMoney());
                alog.setCollection(account.getCollection());
                alog.setRemark("通过第三方托管提现"+ac.getTotal()+"失败，解冻资金");
                int acid = accountLogDao.addAccountLog(alog);
                return acid;
            }
        }
        return 0;
    }


    @Override
    public int addAccountLog(AccountLogPO accountLogPO) {
        return accountLogDao.addAccountLog(accountLogPO);
    }
}
