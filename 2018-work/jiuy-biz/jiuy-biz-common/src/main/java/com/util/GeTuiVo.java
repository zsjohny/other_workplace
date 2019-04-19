package com.util;

/**
 * @version V1.0
 * @Package com.util
 * @Description:
 * @author: Aison
 * @date: 2018/5/15 17:39
 * @Copyright: 玖远网络
 */
public class GeTuiVo {

    private static final String success = "success";

    public static GeTuiVo getIntance(String title, String abstracts, String linkUrl, String image, String pushTime, String type,String phone){
        return new  GeTuiVo(title,abstracts,linkUrl,image,pushTime,type,phone);
    }

    public GeTuiVo(){

    }

    public GeTuiVo(String title, String abstracts, String linkUrl, String image, String pushTime, String type,String phone) {
        this.title = title;
        this.abstracts = abstracts;
        this.linkUrl = linkUrl;
        this.image = image;
        this.pushTime = pushTime;
        this.type = type;
        this.phone = phone;
    }

    private String title;

    private String abstracts;

    private String  linkUrl;

    private String  image;

    private String  pushTime;

    private String  type;

    private String phone;


    public boolean isSuccess(String res){
        return success.equals(res);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
