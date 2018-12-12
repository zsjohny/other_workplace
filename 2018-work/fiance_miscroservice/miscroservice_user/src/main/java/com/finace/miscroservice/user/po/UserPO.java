package com.finace.miscroservice.user.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 用户数据实体类
 */
public class UserPO extends BasePage implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 5059667926063885817L;

    /**
     *
     */
    private int user_id;
    /**
     * user.type_id
     *
     */
    private int typeId;

    /**
     * user.order
     *
     */
    private int order;

    /**
     * user.purview
     *
     */
    private String purview;

    /**
     * user.username
     *
     */
    private String username;

    /**
     * user.password
     *
     */
    private String password;

    /**
     * user.paypassword
     *
     */
    private String paypassword;

    /**
     * user.islock
     *
     */
    private int islock;

    /**
     * user.invite_userid
     * 邀请好友
     */
    private int inviteUserid;

    /**
     * user.invite_money
     * 邀请注册提成
     */
    private String inviteMoney;

    /**
     * user.real_status
     *
     */
    private String realStatus;

    /**
     * user.card_type
     *
     */
    private String cardType;

    /**
     * user.card_id
     *
     */
    private String cardId;

    /**
     * user.card_pic1
     *
     */
    private String cardPic1;

    /**
     * user.card_pic2
     *
     */
    private String cardPic2;

    /**
     * user.nation
     *
     */
    private String nation;

    /**
     * user.realname数据库里面的realname
     *
     */
    private String realname;

    /**
     * user.integral
     *
     */
    private String integral;  //代替籍贯

    /**
     * user.status
     *
     */
    private int status;

    /**
     * user.avatar_status
     *
     */
    private int avatarStatus;

    /**
     * user.email_status
     *
     */
    private String emailStatus;

    /**
     * user.phone_status
     *
     */
    private String phoneStatus;

    /**
     * user.video_status
     * 视频认证
     */
    private int videoStatus;

    /**
     * user.scene_status
     * 现场认证
     */
    private int sceneStatus;

    /**
     * user.email
     *
     */
    private String email;

    /**
     * user.sex
     *
     */
    private String sex;

    /**
     * user.litpic
     *
     */
    private String litpic;

    /**
     * user.tel
     *
     */
    private String tel;

    /**
     * user.phone
     *
     */
    private String phone;

    /**
     * user.qq
     *
     */
    private String qq;

    /**
     * user.wangwang
     *
     */
    private String wangwang;

    /**
     * user.question
     *
     */
    private String question;

    /**
     * user.answer
     *
     */
    private String answer;

    /**
     * user.birthday
     *
     */
    private String birthday;

    /**
     * user.province
     *
     */
    private String province;

    /**
     * user.city
     *
     */
    private String city;

    /**
     * user.area
     *
     */
    private String area;

    /**
     * user.address
     *
     */
    private String address;

    /**
     * user.logintime
     *
     */
    private int logintime;

    /**
     * user.addtime
     *
     */
    private String addtime;

    /**
     * user.addip
     *
     */
    private String addip;

    /**
     * user.uptime
     *
     */
    private String uptime;

    /**
     * user.upip
     *
     */
    private String upip;

    /**
     * user.lasttime
     *
     */
    private String lasttime;

    /**
     * user.lastip
     *
     */
    private String lastip;

    /**
     * user.is_phone
     *
     */
    private int isPhone;

    /**
     * user.memberLevel
     *
     */
    private int memberlevel;

    /**
     * user.serial_id
     *
     */
    private String serialId;

    /**
     * user.serial_status
     *
     */
    private String serialStatus;

    /**
     * user.hongbao
     *
     */
    private String hongbao;

    private String remind;

    private String privacy;

    private String bigPic;
    private String onPic;
    private String smallPic;
    private int user_level;//新增用户星级
    private String auditStat;//保存汇付企业用户开户的状态
    private String sftb;//查看推荐的好友是否投标

    private String starTime;//抽奖开始时间
    private String endTime;//抽奖结束时间

    private Integer age; //年龄
    private String province_name; //省份名称
    private String sx; //生肖
    private String xz;//星座
    private Integer isDevice;//设备型号

    private String payChannel;//支付公司  null || "" || chinapnr : 汇付  ，fuiou : 富友
    private String regChannel;//注册渠道 1
    private String regChannel2;//注册渠道 2

    private int credit_jifen;
    private double use_money;
    private String  credit_pic;
    private int vip_status;
    private int  vip_verify_time;
    private int kefu_addtime;

    private String provincetext;
    private String citytext;
    private String areatext;
    private String typename;
    private String kefu_username;


    /** 新增第三方托管方信息*/
    private String trustUsrId;   //托管方客户用户名
    private String trustUsrCustId;   //托管方客户名
    private String trustTrxId;       //托管方客户号

    /**
     * 设备标识
     */
    @JsonIgnore
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //隐藏用户重要信息
    public void hideChar(){
        if(StringUtils.isNotEmpty(realStatus) && realStatus.equals("1")){
            if(cardId!=null){
                setCardId(TextUtil.hideLastChar(cardId, 4));
            }
            if(realname!=null){
                int len=2;
                if(realname.length()<3) len=1;
                setRealname(TextUtil.hideFirstChar(realname, len));
            }
        }

        if(addip != null){
            setAddip(TextUtil.hideLastChar(addip, 4));
        }
        if(addtime != null){
            setAddtime(TextUtil.hideLastChar(addtime, 4));
        }
        if(password != null){
            setPassword(TextUtil.hideLastChar(password, 6));
        }
        if(paypassword != null){
            setPaypassword(TextUtil.hideLastChar(paypassword, 6));
        }

        if(StringUtils.isNotEmpty(phoneStatus) && phoneStatus.equals("1")){
            if(phone!=null){
                setPhone(TextUtil.hideLastChar(phone, 4));
            }
        }
        if(StringUtils.isNotEmpty(emailStatus)&& emailStatus.equals("1")){
            if(email!=null){
                String[] temp=email.split("@");
                if(temp.length>1){
                    String newEmail=TextUtil.hideChar(temp[0],temp.length)+"@"+temp[1];
                    setEmail(newEmail);
                }
            }
        }
    }


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPurview() {
        return purview;
    }

    public void setPurview(String purview) {
        this.purview = purview;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public int getIslock() {
        return islock;
    }

    public void setIslock(int islock) {
        this.islock = islock;
    }

    public int getInviteUserid() {
        return inviteUserid;
    }

    public void setInviteUserid(int inviteUserid) {
        this.inviteUserid = inviteUserid;
    }

    public String getInviteMoney() {
        return inviteMoney;
    }

    public void setInviteMoney(String inviteMoney) {
        this.inviteMoney = inviteMoney;
    }

    public String getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(String realStatus) {
        this.realStatus = realStatus;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardPic1() {
        return cardPic1;
    }

    public void setCardPic1(String cardPic1) {
        this.cardPic1 = cardPic1;
    }

    public String getCardPic2() {
        return cardPic2;
    }

    public void setCardPic2(String cardPic2) {
        this.cardPic2 = cardPic2;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(int avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(String phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }

    public int getSceneStatus() {
        return sceneStatus;
    }

    public void setSceneStatus(int sceneStatus) {
        this.sceneStatus = sceneStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWangwang() {
        return wangwang;
    }

    public void setWangwang(String wangwang) {
        this.wangwang = wangwang;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLogintime() {
        return logintime;
    }

    public void setLogintime(int logintime) {
        this.logintime = logintime;
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

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getUpip() {
        return upip;
    }

    public void setUpip(String upip) {
        this.upip = upip;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getLastip() {
        return lastip;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip;
    }

    public int getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(int isPhone) {
        this.isPhone = isPhone;
    }

    public int getMemberlevel() {
        return memberlevel;
    }

    public void setMemberlevel(int memberlevel) {
        this.memberlevel = memberlevel;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getSerialStatus() {
        return serialStatus;
    }

    public void setSerialStatus(String serialStatus) {
        this.serialStatus = serialStatus;
    }

    public String getHongbao() {
        return hongbao;
    }

    public void setHongbao(String hongbao) {
        this.hongbao = hongbao;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getOnPic() {
        return onPic;
    }

    public void setOnPic(String onPic) {
        this.onPic = onPic;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getAuditStat() {
        return auditStat;
    }

    public void setAuditStat(String auditStat) {
        this.auditStat = auditStat;
    }

    public String getSftb() {
        return sftb;
    }

    public void setSftb(String sftb) {
        this.sftb = sftb;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    public Integer getIsDevice() {
        return isDevice;
    }

    public void setIsDevice(Integer isDevice) {
        this.isDevice = isDevice;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getRegChannel() {
        return regChannel;
    }

    public void setRegChannel(String regChannel) {
        this.regChannel = regChannel;
    }

    public String getRegChannel2() {
        return regChannel2;
    }

    public void setRegChannel2(String regChannel2) {
        this.regChannel2 = regChannel2;
    }

    public int getCredit_jifen() {
        return credit_jifen;
    }

    public void setCredit_jifen(int credit_jifen) {
        this.credit_jifen = credit_jifen;
    }

    public double getUse_money() {
        return use_money;
    }

    public void setUse_money(double use_money) {
        this.use_money = use_money;
    }

    public String getCredit_pic() {
        return credit_pic;
    }

    public void setCredit_pic(String credit_pic) {
        this.credit_pic = credit_pic;
    }

    public int getVip_status() {
        return vip_status;
    }

    public void setVip_status(int vip_status) {
        this.vip_status = vip_status;
    }

    public int getVip_verify_time() {
        return vip_verify_time;
    }

    public void setVip_verify_time(int vip_verify_time) {
        this.vip_verify_time = vip_verify_time;
    }

    public int getKefu_addtime() {
        return kefu_addtime;
    }

    public void setKefu_addtime(int kefu_addtime) {
        this.kefu_addtime = kefu_addtime;
    }

    public String getProvincetext() {
        return provincetext;
    }

    public void setProvincetext(String provincetext) {
        this.provincetext = provincetext;
    }

    public String getCitytext() {
        return citytext;
    }

    public void setCitytext(String citytext) {
        this.citytext = citytext;
    }

    public String getAreatext() {
        return areatext;
    }

    public void setAreatext(String areatext) {
        this.areatext = areatext;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getKefu_username() {
        return kefu_username;
    }

    public void setKefu_username(String kefu_username) {
        this.kefu_username = kefu_username;
    }

    public String getTrustUsrId() {
        return trustUsrId;
    }

    public void setTrustUsrId(String trustUsrId) {
        this.trustUsrId = trustUsrId;
    }

    public String getTrustUsrCustId() {
        return trustUsrCustId;
    }

    public void setTrustUsrCustId(String trustUsrCustId) {
        this.trustUsrCustId = trustUsrCustId;
    }

    public String getTrustTrxId() {
        return trustTrxId;
    }

    public void setTrustTrxId(String trustTrxId) {
        this.trustTrxId = trustTrxId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isEmpty() {
        if(StringUtils.isEmpty(phone)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
