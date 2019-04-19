package com.jiuyuan.entity;

import java.io.Serializable;

public class QrCode implements Serializable {
    
    private static final long serialVersionUID = 619557118088502920L;

    private String qrcode_id;
    
    private String qrcode_url;
    
    private String batch_no;

    private int status;

    public String getQrcode_id() {
        return qrcode_id;
    }

    public void setQrcode_id(String qrcode_id) {
        this.qrcode_id = qrcode_id;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}