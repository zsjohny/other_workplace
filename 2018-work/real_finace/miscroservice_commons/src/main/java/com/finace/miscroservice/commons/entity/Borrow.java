package com.finace.miscroservice.commons.entity;

import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;

/**
 * 标的实体类
 */
public class Borrow {
    /**
     *
     */
    private static final long serialVersionUID = -931397715769075199L;

    /**
     * 标的id
     */
    private int id;

    /**
     * borrow.site_id
     * 所属站点app/borrow/loadImage
     */
    private int siteId;

    /**
     * borrow.user_id
     * 用户名称
     */
    private int userId;

    /**
     * borrow.name
     * 标题
     */
    private String name;

    /**
     * borrow.status
     * 状态
     */
    private int status;

    /**
     * borrow.order
     * 排序
     */
    private int order;

    /**
     * borrow.hits
     * 点击次数
     */
    private int hits;

    /**
     * borrow.litpic
     * 缩略图
     */
    private String litpic;

    /**
     * borrow.flag
     * 属性(暂定为是否在首页显示) =1表示在首页显示
     */
    private String flag;

    /**
     * borrow.is_vouch
     *
     */
    private int isVouch;

    /**
     * borrow.type
     *
     */
    private String type;

    /**
     * borrow.view_type
     *
     */
    private int viewType;

    /**
     * borrow.vouch_award
     *
     */
    private String vouchAward;

    /**
     * borrow.vouch_user
     *
     */
    private String vouchUser;

    /**
     * borrow.vouch_account
     *
     */
    private String vouchAccount;

    /**
     * borrow.vouch_times
     *
     */
    private int vouchTimes;

    /**
     * borrow.source
     * 来源
     */
    private String source;

    /**
     * borrow.publish
     * 发布时间
     */
    private String publish;

    /**
     * borrow.customer
     * 客服
     */
    private String customer;

    /**
     * borrow.number_id
     *
     */
    private String numberId;

    /**
     * borrow.verify_user
     * 审核人
     */
    private String verifyUser;

    /**
     * borrow.verify_time
     * 客服
     */
    private String verifyTime;

    /**
     * borrow.verify_remark
     *
     */
    private String verifyRemark;

    /**
     * borrow.repayment_user
     *
     */
    private long repaymentUser;

    /**
     * borrow.forst_account
     *
     */
    private String forstAccount;

    /**
     * borrow.repayment_account
     *
     */
    private String repaymentAccount;

    /**
     * borrow.monthly_repayment
     * 每月还款额
     */
    private String monthlyRepayment;

    /**
     * borrow.repayment_yesaccount
     *
     */
    private String repaymentYesaccount;

    /**
     * borrow.repayment_yesinterest
     *
     */
    private int repaymentYesinterest;

    /**
     * borrow.repayment_time
     *
     */
    private String repaymentTime;

    /**
     * borrow.repayment_remark
     *
     */
    private String repaymentRemark;

    /**
     * borrow.success_time
     *
     */
    private String successTime;

    /**
     * borrow.end_time
     *
     */
    private String endTime;

    /**
     * borrow.payment_account
     *
     */
    private String paymentAccount;

    /**
     * borrow.each_time
     *
     */
    private String eachTime;

    /**
     * borrow.use
     * 用途
     */
    private String use;

    /**
     * borrow.time_limit
     * 借款期限
     */
    private String timeLimit;

    /**
     * borrow.style
     * 还款方式
     */
    private String style;

    /**
     * borrow.account
     * 借贷总金额
     */
    private String account;

    /**
     * borrow.account_yes
     *
     */
    private String accountYes;

    /**
     * borrow.tender_times
     *
     */
    private String tenderTimes;

    /**
     * borrow.apr
     * 总年利率
     */
    private double apr;

    /**
     * borrow.apr
     * 加息部分年利率
     */
    private double addApr;

    /**
     * borrow.lowest_account
     * 最低投标金额
     */
    private String lowestAccount;

    /**
     * borrow.most_account
     * 最多投标总额
     */
    private String mostAccount;

    /**
     * borrow.valid_time
     * 有效时间
     */
    private String validTime;

    /**
     * borrow.award
     * 投标奖励
     */
    private double award;

    /**
     * borrow.part_account
     * 分摊奖励金额
     */
    private double partAccount;

    /**
     * borrow.funds
     * 比例奖励的比例
     */
    private double funds;

    /**
     * borrow.is_false
     * 标的失败时也同样奖励
     */
    private String isFalse;

    /**
     * borrow.open_account
     * 公开我的帐户资金情况
     */
    private String openAccount;

    /**
     * borrow.open_borrow
     * 公开我的借款资金情况
     */
    private String openBorrow;

    /**
     * borrow.open_tender
     * 公开我的投标资金情况
     */
    private String openTender;

    /**
     * borrow.open_credit
     * 公开我的信用额度情况
     */
    private String openCredit;

    /**
     * borrow.addtime
     *
     */
    private String addtime;

    /**
     * borrow.addip
     *
     */
    private String addip;

    /**
     * borrow.is_mb
     *
     */
    private int isMb;

    /**
     * borrow.is_fast
     *
     */
    private int isFast;

    /**
     * borrow.is_jin
     *
     */
    private int isJin;

    /**
     * borrow.is_xin
     *
     */
    private int isXin;

    /**
     * borrow.pwd
     *
     */
    private String pwd;

    /**
     * borrow.isday
     *
     */
    private int isday;

    /**
     * borrow.time_limit_day
     *
     */
    private int timeLimitDay;

    /**
     * borrow.is_art
     *
     */
    private int isArt;

    /**
     * borrow.is_charity
     *
     */
    private int isCharity;

    /**
     * borrow.is_project
     *
     */
    private int isProject;

    /**
     * borrow.is_flow
     * 流转标的标识字段
     */
    private int isFlow;

    /**
     * borrow.flow_status
     * 流转标的状态
     */
    private int flowStatus;

    /**
     * borrow.flow_money
     * 流转标
     */
    private int flowMoney;

    /**
     * borrow.flow_count
     * 流转标的总份数
     */
    private int flowCount;

    /**
     * borrow.flow_yescount
     * 流转标已经购买的份数
     */
    private int flowYescount;

    /**
     * borrow.is_student
     * 学信标字段
     */
    private int isStudent;

    /**
     * borrow.is_offvouch
     *
     */
    private int isOffvouch;

    /**
     * borrow.hb_flag
     * 红包奖励(0没有投资红包奖励,1有红包奖励)
     */
    private int hbFlag;

    /**
     * borrow.content
     * 详细说明
     */
    private String content;
    private String content2;
    private String guarantee;//新增担保机构名

    private String username;
    private String scales;
    private String fen;
    private double borrowTotal;
    private double borrowInterest;
    private  String collectionLimit;
    private int borrowType;

    private String payTime;
    private String rePlayTime;
    private int count1;	 //是否是自己发的标
    private int count2;  //是否已经投过此标
    private int count3;  //是否标已经满

    private double remmoney;  //剩余金额

    private String BorrowerCustId;  //借款人客户号

    private Integer trustLevel;  //信用等级

    private String   expire;//到期
    private int   borrowTypes;//标的收益种类

    private String borrow_fee_apr;
    private String borrow_fee;

    private String tenderTime;//标的可投时间(投标页面的倒计时)
    private int hongbaoTrans;//新增:红包活动期间投标奖励红包

    private String startTime;
    private Integer jx_flag; //是否可以使用加息卷0--否  1--是
    private Integer jxj_flag;//是否可以使用红包 0--否  1--是
    private int releaseType;//标发布种类 10--汇付 20--富友
    private String lx;
    private String ficAccount;

    private String borrow_group;
    private String finance_company;
    private String loan_usage;
    private String payment;

    private String imgurl1;
    private String imgurl2;
    private String imgurl3;
    private String imgurl4;
    private String imgurl5;
    private String imgurl6;
    private String imgurl7;
    private String imgurl8;
    private String imgurl9;
    private String imgurl10;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getIsVouch() {
        return isVouch;
    }

    public void setIsVouch(int isVouch) {
        this.isVouch = isVouch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getVouchAward() {
        return vouchAward;
    }

    public void setVouchAward(String vouchAward) {
        this.vouchAward = vouchAward;
    }

    public String getVouchUser() {
        return vouchUser;
    }

    public void setVouchUser(String vouchUser) {
        this.vouchUser = vouchUser;
    }

    public String getVouchAccount() {
        return vouchAccount;
    }

    public void setVouchAccount(String vouchAccount) {
        this.vouchAccount = vouchAccount;
    }

    public int getVouchTimes() {
        return vouchTimes;
    }

    public void setVouchTimes(int vouchTimes) {
        this.vouchTimes = vouchTimes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public long getRepaymentUser() {
        return repaymentUser;
    }

    public void setRepaymentUser(long repaymentUser) {
        this.repaymentUser = repaymentUser;
    }

    public String getForstAccount() {
        return forstAccount;
    }

    public void setForstAccount(String forstAccount) {
        this.forstAccount = forstAccount;
    }

    public String getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(String repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public String getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(String monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public String getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(String repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
    }

    public int getRepaymentYesinterest() {
        return repaymentYesinterest;
    }

    public void setRepaymentYesinterest(int repaymentYesinterest) {
        this.repaymentYesinterest = repaymentYesinterest;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public String getRepaymentRemark() {
        return repaymentRemark;
    }

    public void setRepaymentRemark(String repaymentRemark) {
        this.repaymentRemark = repaymentRemark;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getEachTime() {
        return eachTime;
    }

    public void setEachTime(String eachTime) {
        this.eachTime = eachTime;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountYes() {
        return accountYes;
    }

    public void setAccountYes(String accountYes) {
        this.accountYes = accountYes;
    }

    public String getTenderTimes() {
        return tenderTimes;
    }

    public void setTenderTimes(String tenderTimes) {
        this.tenderTimes = tenderTimes;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public double getAddApr() {
        return addApr;
    }

    public void setAddApr(double addApr) {
        this.addApr = addApr;
    }

    public String getLowestAccount() {
        return lowestAccount;
    }

    public void setLowestAccount(String lowestAccount) {
        this.lowestAccount = lowestAccount;
    }

    public String getMostAccount() {
        return mostAccount;
    }

    public void setMostAccount(String mostAccount) {
        this.mostAccount = mostAccount;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public double getAward() {
        return award;
    }

    public void setAward(double award) {
        this.award = award;
    }

    public double getPartAccount() {
        return partAccount;
    }

    public void setPartAccount(double partAccount) {
        this.partAccount = partAccount;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public String getIsFalse() {
        return isFalse;
    }

    public void setIsFalse(String isFalse) {
        this.isFalse = isFalse;
    }

    public String getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }

    public String getOpenBorrow() {
        return openBorrow;
    }

    public void setOpenBorrow(String openBorrow) {
        this.openBorrow = openBorrow;
    }

    public String getOpenTender() {
        return openTender;
    }

    public void setOpenTender(String openTender) {
        this.openTender = openTender;
    }

    public String getOpenCredit() {
        return openCredit;
    }

    public void setOpenCredit(String openCredit) {
        this.openCredit = openCredit;
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

    public int getIsMb() {
        return isMb;
    }

    public void setIsMb(int isMb) {
        this.isMb = isMb;
    }

    public int getIsFast() {
        return isFast;
    }

    public void setIsFast(int isFast) {
        this.isFast = isFast;
    }

    public int getIsJin() {
        return isJin;
    }

    public void setIsJin(int isJin) {
        this.isJin = isJin;
    }

    public int getIsXin() {
        return isXin;
    }

    public void setIsXin(int isXin) {
        this.isXin = isXin;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getIsday() {
        return isday;
    }

    public void setIsday(int isday) {
        this.isday = isday;
    }

    public int getTimeLimitDay() {
        return timeLimitDay;
    }

    public void setTimeLimitDay(int timeLimitDay) {
        this.timeLimitDay = timeLimitDay;
    }

    public int getIsArt() {
        return isArt;
    }

    public void setIsArt(int isArt) {
        this.isArt = isArt;
    }

    public int getIsCharity() {
        return isCharity;
    }

    public void setIsCharity(int isCharity) {
        this.isCharity = isCharity;
    }

    public int getIsProject() {
        return isProject;
    }

    public void setIsProject(int isProject) {
        this.isProject = isProject;
    }

    public int getIsFlow() {
        return isFlow;
    }

    public void setIsFlow(int isFlow) {
        this.isFlow = isFlow;
    }

    public int getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(int flowStatus) {
        this.flowStatus = flowStatus;
    }

    public int getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(int flowMoney) {
        this.flowMoney = flowMoney;
    }

    public int getFlowCount() {
        return flowCount;
    }

    public void setFlowCount(int flowCount) {
        this.flowCount = flowCount;
    }

    public int getFlowYescount() {
        return flowYescount;
    }

    public void setFlowYescount(int flowYescount) {
        this.flowYescount = flowYescount;
    }

    public int getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(int isStudent) {
        this.isStudent = isStudent;
    }

    public int getIsOffvouch() {
        return isOffvouch;
    }

    public void setIsOffvouch(int isOffvouch) {
        this.isOffvouch = isOffvouch;
    }

    public int getHbFlag() {
        return hbFlag;
    }

    public void setHbFlag(int hbFlag) {
        this.hbFlag = hbFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScales() {
        return scales;
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public double getBorrowTotal() {
        return borrowTotal;
    }

    public void setBorrowTotal(double borrowTotal) {
        this.borrowTotal = borrowTotal;
    }

    public double getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(double borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public String getCollectionLimit() {
        return collectionLimit;
    }

    public void setCollectionLimit(String collectionLimit) {
        this.collectionLimit = collectionLimit;
    }

    public int getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(int borrowType) {
        this.borrowType = borrowType;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRePlayTime() {
        return rePlayTime;
    }

    public void setRePlayTime(String rePlayTime) {
        this.rePlayTime = rePlayTime;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public int getCount3() {
        return count3;
    }

    public void setCount3(int count3) {
        this.count3 = count3;
    }

    public String getBorrowerCustId() {
        return BorrowerCustId;
    }

    public void setBorrowerCustId(String borrowerCustId) {
        BorrowerCustId = borrowerCustId;
    }

    public Integer getTrustLevel() {
        return trustLevel;
    }

    public void setTrustLevel(Integer trustLevel) {
        this.trustLevel = trustLevel;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public int getBorrowTypes() {
        return borrowTypes;
    }

    public void setBorrowTypes(int borrowTypes) {
        this.borrowTypes = borrowTypes;
    }

    public String getBorrow_fee_apr() {
        return borrow_fee_apr;
    }

    public void setBorrow_fee_apr(String borrow_fee_apr) {
        this.borrow_fee_apr = borrow_fee_apr;
    }

    public String getBorrow_fee() {
        return borrow_fee;
    }

    public void setBorrow_fee(String borrow_fee) {
        this.borrow_fee = borrow_fee;
    }

    public String getTenderTime() {
        return tenderTime;
    }

    public void setTenderTime(String tenderTime) {
        this.tenderTime = tenderTime;
    }

    public int getHongbaoTrans() {
        return hongbaoTrans;
    }

    public void setHongbaoTrans(int hongbaoTrans) {
        this.hongbaoTrans = hongbaoTrans;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getJx_flag() {
        return jx_flag;
    }

    public void setJx_flag(Integer jx_flag) {
        this.jx_flag = jx_flag;
    }

    public Integer getJxj_flag() {
        return jxj_flag;
    }

    public void setJxj_flag(Integer jxj_flag) {
        this.jxj_flag = jxj_flag;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getFicAccount() {
        return ficAccount;
    }

    public void setFicAccount(String ficAccount) {
        this.ficAccount = ficAccount;
    }

    public String getBorrow_group() {
        return borrow_group;
    }

    public void setBorrow_group(String borrow_group) {
        this.borrow_group = borrow_group;
    }

    public String getFinance_company() {
        return finance_company;
    }

    public void setFinance_company(String finance_company) {
        this.finance_company = finance_company;
    }

    public String getLoan_usage() {
        return loan_usage;
    }

    public void setLoan_usage(String loan_usage) {
        this.loan_usage = loan_usage;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getImgurl1() {
        return imgurl1;
    }

    public void setImgurl1(String imgurl1) {
        this.imgurl1 = imgurl1;
    }

    public String getImgurl2() {
        return imgurl2;
    }

    public void setImgurl2(String imgurl2) {
        this.imgurl2 = imgurl2;
    }

    public String getImgurl3() {
        return imgurl3;
    }

    public void setImgurl3(String imgurl3) {
        this.imgurl3 = imgurl3;
    }

    public String getImgurl4() {
        return imgurl4;
    }

    public void setImgurl4(String imgurl4) {
        this.imgurl4 = imgurl4;
    }

    public String getImgurl5() {
        return imgurl5;
    }

    public void setImgurl5(String imgurl5) {
        this.imgurl5 = imgurl5;
    }

    public String getImgurl6() {
        return imgurl6;
    }

    public void setImgurl6(String imgurl6) {
        this.imgurl6 = imgurl6;
    }

    public String getImgurl7() {
        return imgurl7;
    }

    public void setImgurl7(String imgurl7) {
        this.imgurl7 = imgurl7;
    }

    public String getImgurl8() {
        return imgurl8;
    }

    public void setImgurl8(String imgurl8) {
        this.imgurl8 = imgurl8;
    }

    public String getImgurl9() {
        return imgurl9;
    }

    public void setImgurl9(String imgurl9) {
        this.imgurl9 = imgurl9;
    }

    public String getImgurl10() {
        return imgurl10;
    }

    public void setImgurl10(String imgurl10) {
        this.imgurl10 = imgurl10;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRemmoney() {
        if(StringUtils.isNotEmpty(account)&& StringUtils.isNotEmpty(accountYes)){
            double d = Double.valueOf(account)-Double.valueOf(accountYes);
            return NumberUtil.format(d,"####0.00");
        }else{
            return 0.00;
        }
    }

    public void setRemmoney(double remmoney) {
        this.remmoney = remmoney;
    }




}
