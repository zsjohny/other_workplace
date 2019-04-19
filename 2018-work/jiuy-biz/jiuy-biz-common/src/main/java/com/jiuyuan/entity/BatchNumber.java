package com.jiuyuan.entity;

import java.io.Serializable;

public class BatchNumber implements Serializable {
    private static final long serialVersionUID = 3677024405796211754L;

    private String batch_no;

    private int inner_code;

    private String supplier_code;

    private String title;

    private String purchase_site;

    private String warehouse_site;

    private int token_ratio;

    private int status;

    private long load_time;

    private long create_time;

    private long update_time;

    private int total_count;

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public int getInner_code() {
        return inner_code;
    }

    public void setInner_code(int inner_code) {
        this.inner_code = inner_code;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurchase_site() {
        return purchase_site;
    }

    public void setPurchase_site(String purchase_site) {
        this.purchase_site = purchase_site;
    }

    public String getWarehouse_site() {
        return warehouse_site;
    }

    public void setWarehouse_site(String warehouse_site) {
        this.warehouse_site = warehouse_site;
    }

    public int getToken_ratio() {
        return token_ratio;
    }

    public void setToken_ratio(int token_ratio) {
        this.token_ratio = token_ratio;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLoad_time() {
        return load_time;
    }

    public void setLoad_time(long load_time) {
        this.load_time = load_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

}