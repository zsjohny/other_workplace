package com.finace.miscroservice.borrow.po;

public class BorrowTenderPO {

    private  int id;
    /**
     * borrow_tender.site_id
     * 所属站点
     */
    private long siteId;

    /**
     * borrow_tender.user_id
     * 用户名称
     */
    private long userId;

    /**
     * borrow_tender.status
     * 状态
     */
    private int status;

    /**
     * borrow_tender.borrow_id
     *
     */
    private long borrowId;

    /**
     * borrow_tender.money
     *
     */
    private String money;

    /**
     * borrow_tender.account
     *
     */
    private String account;

    /**
     * borrow_tender.repayment_account
     * 总额
     */
    private String repaymentAccount;

    /**
     * borrow_tender.interest
     *
     */
    private String interest;

    /**
     * borrow_tender.repayment_yesaccount
     * 已还总额
     */
    private String repaymentYesaccount;

    /**
     * borrow_tender.wait_account
     * 待还总额
     */
    private String waitAccount;

    /**
     * borrow_tender.wait_interest
     * 待还利息
     */
    private String waitInterest;

    /**
     * borrow_tender.repayment_yesinterest
     * 已还利息
     */
    private String repaymentYesinterest;

    /**
     * borrow_tender.addtime
     *
     */
    private String addtime;

    /**
     * borrow_tender.addip
     *
     */
    private String addip;

    /**
     * borrow_tender.auto_repurchase
     * 投标者是否自动续转
     */
    private Integer autoRepurchase;

    /**
     * borrow_tender.Is_auto_tender
     *
     */
    private Integer isAutoTender;

    /** 新增 */
    private double collectTotal;

    private double collectInterest;

    private double investTotal;

    private double investInterest;

    private String username;


    private Double inter;
    private String borrowName;
    private double borrowAccount;
    private Integer timeLimit;
    private Integer isDay;
    private Integer timeLimitDay;
    private Double apr;
    private Integer credit;

    private Integer isTransfer;  //是否转让0不1是
    private String tender_time;
    private String   anum;
    private String    borrow_name;
    private String time_limit;
    private String isday;
    private String time_limit_day;
    private String tender_account;
    private String tender_money;
    private String  borrow_userid;
    private String   op_username;
    private String  borrow_id;
    private String  borrow_account ;
    private String  borrow_account_yes;
    private String verify_time;
    private String  credit_jifen;
    private String credit_pic ;
    private String scales;

    private String realname;
    private String cardId;

    private String oneJiangli;

    private String twoJiangli;

    private String trustOrderNo;

    private int trustStatus;

    private String trustIsFreeze;

    private String trustFreezeOrdId;

    private String trustFreezeTrxId;

    private Double hongbaoMoney;

    private Double accountPast;//我的慈善
    private Double interestPast;//累计收益


    //rjg 20160104 yemian
    private int  st;//状态
    private String dqr;  //到期日
    private String qxr;  //起息日
    private int mjt;     //

    private String startTime;//抽奖活动开始时间
    private String endTime;//抽奖活动截止时间

    private int hbid; //福利卷id
    private Double jx_interst; //加息利息
    private Double jxnum; //加息数
    private String contract;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(long borrowId) {
        this.borrowId = borrowId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(String repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(String repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
    }

    public String getWaitAccount() {
        return waitAccount;
    }

    public void setWaitAccount(String waitAccount) {
        this.waitAccount = waitAccount;
    }

    public String getWaitInterest() {
        return waitInterest;
    }

    public void setWaitInterest(String waitInterest) {
        this.waitInterest = waitInterest;
    }

    public String getRepaymentYesinterest() {
        return repaymentYesinterest;
    }

    public void setRepaymentYesinterest(String repaymentYesinterest) {
        this.repaymentYesinterest = repaymentYesinterest;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip;
    }

    public Integer getAutoRepurchase() {
        return autoRepurchase;
    }

    public void setAutoRepurchase(Integer autoRepurchase) {
        this.autoRepurchase = autoRepurchase;
    }

    public Integer getIsAutoTender() {
        return isAutoTender;
    }

    public void setIsAutoTender(Integer isAutoTender) {
        this.isAutoTender = isAutoTender;
    }

    public double getCollectTotal() {
        return collectTotal;
    }

    public void setCollectTotal(double collectTotal) {
        this.collectTotal = collectTotal;
    }

    public double getCollectInterest() {
        return collectInterest;
    }

    public void setCollectInterest(double collectInterest) {
        this.collectInterest = collectInterest;
    }

    public double getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(double investTotal) {
        this.investTotal = investTotal;
    }

    public double getInvestInterest() {
        return investInterest;
    }

    public void setInvestInterest(double investInterest) {
        this.investInterest = investInterest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getInter() {
        return inter;
    }

    public void setInter(Double inter) {
        this.inter = inter;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public double getBorrowAccount() {
        return borrowAccount;
    }

    public void setBorrowAccount(double borrowAccount) {
        this.borrowAccount = borrowAccount;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getIsDay() {
        return isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public Integer getTimeLimitDay() {
        return timeLimitDay;
    }

    public void setTimeLimitDay(Integer timeLimitDay) {
        this.timeLimitDay = timeLimitDay;
    }

    public Double getApr() {
        return apr;
    }

    public void setApr(Double apr) {
        this.apr = apr;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(Integer isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getTender_time() {
        return tender_time;
    }

    public void setTender_time(String tender_time) {
        this.tender_time = tender_time;
    }

    public String getAnum() {
        return anum;
    }

    public void setAnum(String anum) {
        this.anum = anum;
    }

    public String getBorrow_name() {
        return borrow_name;
    }

    public void setBorrow_name(String borrow_name) {
        this.borrow_name = borrow_name;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getIsday() {
        return isday;
    }

    public void setIsday(String isday) {
        this.isday = isday;
    }

    public String getTime_limit_day() {
        return time_limit_day;
    }

    public void setTime_limit_day(String time_limit_day) {
        this.time_limit_day = time_limit_day;
    }

    public String getTender_account() {
        return tender_account;
    }

    public void setTender_account(String tender_account) {
        this.tender_account = tender_account;
    }

    public String getTender_money() {
        return tender_money;
    }

    public void setTender_money(String tender_money) {
        this.tender_money = tender_money;
    }

    public String getBorrow_userid() {
        return borrow_userid;
    }

    public void setBorrow_userid(String borrow_userid) {
        this.borrow_userid = borrow_userid;
    }

    public String getOp_username() {
        return op_username;
    }

    public void setOp_username(String op_username) {
        this.op_username = op_username;
    }

    public String getBorrow_id() {
        return borrow_id;
    }

    public void setBorrow_id(String borrow_id) {
        this.borrow_id = borrow_id;
    }

    public String getBorrow_account() {
        return borrow_account;
    }

    public void setBorrow_account(String borrow_account) {
        this.borrow_account = borrow_account;
    }

    public String getBorrow_account_yes() {
        return borrow_account_yes;
    }

    public void setBorrow_account_yes(String borrow_account_yes) {
        this.borrow_account_yes = borrow_account_yes;
    }

    public String getVerify_time() {
        return verify_time;
    }

    public void setVerify_time(String verify_time) {
        this.verify_time = verify_time;
    }

    public String getCredit_jifen() {
        return credit_jifen;
    }

    public void setCredit_jifen(String credit_jifen) {
        this.credit_jifen = credit_jifen;
    }

    public String getCredit_pic() {
        return credit_pic;
    }

    public void setCredit_pic(String credit_pic) {
        this.credit_pic = credit_pic;
    }

    public String getScales() {
        return scales;
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getOneJiangli() {
        return oneJiangli;
    }

    public void setOneJiangli(String oneJiangli) {
        this.oneJiangli = oneJiangli;
    }

    public String getTwoJiangli() {
        return twoJiangli;
    }

    public void setTwoJiangli(String twoJiangli) {
        this.twoJiangli = twoJiangli;
    }

    public String getTrustOrderNo() {
        return trustOrderNo;
    }

    public void setTrustOrderNo(String trustOrderNo) {
        this.trustOrderNo = trustOrderNo;
    }

    public int getTrustStatus() {
        return trustStatus;
    }

    public void setTrustStatus(int trustStatus) {
        this.trustStatus = trustStatus;
    }

    public String getTrustIsFreeze() {
        return trustIsFreeze;
    }

    public void setTrustIsFreeze(String trustIsFreeze) {
        this.trustIsFreeze = trustIsFreeze;
    }

    public String getTrustFreezeOrdId() {
        return trustFreezeOrdId;
    }

    public void setTrustFreezeOrdId(String trustFreezeOrdId) {
        this.trustFreezeOrdId = trustFreezeOrdId;
    }

    public String getTrustFreezeTrxId() {
        return trustFreezeTrxId;
    }

    public void setTrustFreezeTrxId(String trustFreezeTrxId) {
        this.trustFreezeTrxId = trustFreezeTrxId;
    }

    public Double getHongbaoMoney() {
        return hongbaoMoney;
    }

    public void setHongbaoMoney(Double hongbaoMoney) {
        this.hongbaoMoney = hongbaoMoney;
    }

    public Double getAccountPast() {
        return accountPast;
    }

    public void setAccountPast(Double accountPast) {
        this.accountPast = accountPast;
    }

    public Double getInterestPast() {
        return interestPast;
    }

    public void setInterestPast(Double interestPast) {
        this.interestPast = interestPast;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getDqr() {
        return dqr;
    }

    public void setDqr(String dqr) {
        this.dqr = dqr;
    }

    public String getQxr() {
        return qxr;
    }

    public void setQxr(String qxr) {
        this.qxr = qxr;
    }

    public int getMjt() {
        return mjt;
    }

    public void setMjt(int mjt) {
        this.mjt = mjt;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getHbid() {
        return hbid;
    }

    public void setHbid(int hbid) {
        this.hbid = hbid;
    }

    public Double getJx_interst() {
        return jx_interst;
    }

    public void setJx_interst(Double jx_interst) {
        this.jx_interst = jx_interst;
    }

    public Double getJxnum() {
        return jxnum;
    }

    public void setJxnum(Double jxnum) {
        this.jxnum = jxnum;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
