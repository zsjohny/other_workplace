package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.official_website.entity.BaseEntity;

/**
 * Created by hyf on 2018/3/6.
 */
public class ProductRecordUsersResponse extends BaseEntity {
    private Integer borrowId;
    private Double account;
    private String addtime;
    private String phone;

    public ProductRecordUsersResponse() {
    }

    public ProductRecordUsersResponse(Integer borrowId, Double account, String addtime, String phone) {
        this.borrowId = borrowId;
        this.account = account;
        this.addtime = addtime;
        this.phone = phone;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = TextUtil.hideUsernameChar(phone);
    }

    public static void main(String[] args) {
        ProductRecordUsersResponse p = new ProductRecordUsersResponse();
        p.setPhone("13216513249");
        System.out.println(p.getPhone());
    }
}
