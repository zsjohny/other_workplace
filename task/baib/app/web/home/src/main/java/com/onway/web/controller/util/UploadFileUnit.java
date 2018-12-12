package com.onway.web.controller.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传单元
 * 
 * @author wwf
 * @version $Id: UploadFileUnit.java, v 0.1 2016年8月15日 下午3:26:37 Administrator Exp $
 */
public class UploadFileUnit {

    // 上传的文件内容
    private MultipartFile file;

    // 用户编号
    private String        userId;

    // 获取地址路径的点key值
    private String        appointKey;

    // 新文件的名字
    private String        newFileName;

    private String        strFile;

    public String getStrFile() {
        return strFile;
    }

    public void setStrFile(String strFile) {
        this.strFile = strFile;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointKey() {
        return appointKey;
    }

    public void setAppointKey(String appointKey) {
        this.appointKey = appointKey;
    }

}
