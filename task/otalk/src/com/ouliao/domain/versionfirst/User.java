package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by p on 2016/2/18.
 */

@Entity
@Table(name = "user")
public class User implements Serializable {
    private Integer userId;
    private String userName;
    private String userNickName;
    private String userNum;
    private String userPhone;
    private String userPass;
    private String userKey;
    private String userPhoneCode;
    private String userHeadPic;
    private String currentTime;
    private String tempKey;
    private String userSign;
    private String userAuth;
    private String userContract;
    private String userAlipayAccount;
    private String userAlipayName;
    private String userOwnerOrder;

    private String userRecord;
    private String userCallTime;
    private String userCallTimeWeek;
    private Double userCallCost;// 主播接听电话的费用
    private Double userCallEarn;
    private Double userCallConsume;
    private Long userCallScore;
    private Double userCallTotal;
    private String userLabel;
    private String isCheckPass;

    private Double userMoney;

    private String isDeleted;

    private String isRegister;
    private Date userCreateTime;
    private Date userModifyTime;


    private String thridId;
    private String isFirstConcern;
    private Integer firstConcernId;

    //version second
    private String emailCode;
    private String eamil;
    private String backPicUrl;
    private String userGreet;

    @Column(length = 64)
    public String getUserGreet() {
        return userGreet;
    }

    public void setUserGreet(String userGreet) {
        this.userGreet = userGreet;
    }

    @Column(length = 1024)
    public String getBackPicUrl() {
        return backPicUrl;
    }

    public void setBackPicUrl(String backPicUrl) {
        this.backPicUrl = backPicUrl;
    }

    @Column(length = 6)
    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    @Column(length = 54)
    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }
    //-----------------------------------------------------

    @Column(length = 512)
    public String getThridId() {
        return thridId;
    }

    public void setThridId(String thridId) {
        this.thridId = thridId;
    }

    public Integer getFirstConcernId() {
        return firstConcernId;
    }

    public void setFirstConcernId(Integer firstConcernId) {
        this.firstConcernId = firstConcernId;
    }

    @Column(length = 4)
    public String getIsFirstConcern() {
        return isFirstConcern;
    }

    public void setIsFirstConcern(String isFirstConcern) {
        this.isFirstConcern = isFirstConcern;
    }

    @Id
    @GeneratedValue
    public Integer getUserId() {
        return userId;
    }

    @Column(length = 8)
    /**
     * @return the isRegister
     */
    public String getIsRegister() {
        return isRegister;
    }

    /**
     * @param isRegister the isRegister to set
     */
    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    @Column(length = 6)
    /**
     * @return the isCheckPass
     */
    public String getIsCheckPass() {
        return isCheckPass;
    }

    /**
     * @param isCheckPass the isCheckPass to set
     */
    public void setIsCheckPass(String isCheckPass) {
        this.isCheckPass = isCheckPass;
    }

    /**
     * @return the userCallConsume
     */
    public Double getUserCallConsume() {
        return userCallConsume;
    }

    /**
     * @param userCallConsume the userCallConsume to set
     */
    public void setUserCallConsume(Double userCallConsume) {
        this.userCallConsume = userCallConsume;
    }

    /**
     * @return the userCallEarn
     */
    public Double getUserCallEarn() {
        return userCallEarn;
    }

    /**
     * @param userCallEarn the userCallEarn to set
     */
    public void setUserCallEarn(Double userCallEarn) {
        this.userCallEarn = userCallEarn;
    }

    /**
     * @return the userCallScore
     */
    public Long getUserCallScore() {
        return userCallScore;
    }

    /**
     * @param userCallScore the userCallScore to set
     */
    public void setUserCallScore(Long userCallScore) {
        this.userCallScore = userCallScore;
    }

    /**
     * @return the userMoney
     */
    public Double getUserMoney() {
        return userMoney;
    }

    /**
     * @return the userCallTotal
     */
    public Double getUserCallTotal() {
        return userCallTotal;
    }

    /**
     * @param userCallTotal the userCallTotal to set
     */
    public void setUserCallTotal(Double userCallTotal) {
        this.userCallTotal = userCallTotal;
    }

    /**
     * @param userMoney the userMoney to set
     */
    public void setUserMoney(Double userMoney) {
        this.userMoney = userMoney;
    }

    @Column(length = 6)
    /**
     * @return the userCallCost
     */
    public Double getUserCallCost() {
        return userCallCost;
    }

    /**
     * @param userCallCost the userCallCost to set
     */
    public void setUserCallCost(Double userCallCost) {
        this.userCallCost = userCallCost;
    }

    @Column(length = 8)
    /**
     * @return the userOwnerOrder
     */
    public String getUserOwnerOrder() {
        return userOwnerOrder;
    }

    /**
     * @param userOwnerOrder the userOwnerOrder to set
     */
    public void setUserOwnerOrder(String userOwnerOrder) {
        this.userOwnerOrder = userOwnerOrder;
    }

    @Column(length = 512)
    /**
     * @return the userCallTimeWeek
     */
    public String getUserCallTimeWeek() {
        return userCallTimeWeek;
    }

    /**
     * @param userCallTimeWeek the userCallTimeWeek to set
     */
    public void setUserCallTimeWeek(String userCallTimeWeek) {
        this.userCallTimeWeek = userCallTimeWeek;
    }

    @Column(length = 512)
    /**
     * @return the userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * @param userLabel the userLabel to set
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    @Column(length = 216)
    /**
     * @return the userRecord
     */
    public String getUserRecord() {
        return userRecord;
    }

    @Column(length = 128)
    /**
     * @return the userCallTime
     */
    public String getUserCallTime() {
        return userCallTime;
    }

    /**
     * @param userRecord the userRecord to set
     */
    public void setUserRecord(String userRecord) {
        this.userRecord = userRecord;
    }

    /**
     * @param userCallTime the userCallTime to set
     */
    public void setUserCallTime(String userCallTime) {
        this.userCallTime = userCallTime;
    }

    @Column(length = 128)
    /**
     * @return the userAlipayAccount
     */
    public String getUserAlipayAccount() {
        return userAlipayAccount;
    }

    @Column(length = 128)
    /**
     * @return the userAlipayName
     */
    public String getUserAlipayName() {
        return userAlipayName;
    }

    /**
     * @param userAlipayAccount the userAlipayAccount to set
     */
    public void setUserAlipayAccount(String userAlipayAccount) {
        this.userAlipayAccount = userAlipayAccount;
    }

    /**
     * @param userAlipayName the userAlipayName to set
     */
    public void setUserAlipayName(String userAlipayName) {
        this.userAlipayName = userAlipayName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 6)
    /**
     * @return the userContract
     */
    public String getUserContract() {
        return userContract;
    }

    /**
     * @param userContract the userContract to set
     */
    public void setUserContract(String userContract) {
        this.userContract = userContract;
    }

    @Column(length = 512)
    /**
     * @return the userNum
     */
    public String getUserNum() {
        return userNum;
    }

    @Column(length = 52)
    /**
     * @return the userNickName
     */
    public String getUserNickName() {
        return userNickName;
    }

    /**
     * @param userNickName the userNickName to set
     */
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @Column(length = 512)
    /**
     * @return the userKey
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * @param userKey the userKey to set
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /**
     * @param userNum the userNum to set
     */
    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    @Column(length = 52)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(length = 11)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Column(length = 512)
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Column(length = 6)
    public String getUserPhoneCode() {
        return userPhoneCode;
    }

    public void setUserPhoneCode(String userPhoneCode) {
        this.userPhoneCode = userPhoneCode;
    }

    @Column(length = 4)
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public Date getUserModifyTime() {
        return userModifyTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public void setUserModifyTime(Date userModifyTime) {
        this.userModifyTime = userModifyTime;
    }

    @Column(length = 512)
    /**
     * @return the userHeadPic
     */
    public String getUserHeadPic() {
        return userHeadPic;
    }

    /**
     * @param userHeadPic the userHeadPic to set
     */
    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    @Column(length = 128)
    /**
     * @return the currentTime
     */
    public String getCurrentTime() {
        return currentTime;
    }

    @Column(length = 512)
    /**
     * @return the tempKey
     */
    public String getTempKey() {
        return tempKey;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @param tempKey the tempKey to set
     */
    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }

    @Column(length = 215)
    /**
     * @return the userSign
     */
    public String getUserSign() {
        return userSign;
    }

    /**
     * @param userSign the userSign to set
     */
    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    @Column(length = 215)
    /**
     * @return the userAuth
     */
    public String getUserAuth() {
        return userAuth;
    }

    /**
     * @param userAuth the userAuth to set
     */
    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

}
