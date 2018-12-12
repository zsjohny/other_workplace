package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.official_website.entity.BaseEntity;

import java.util.List;

/**
 * Created by hyf on 2018/3/6.
 */
public class ProductRecordsResponse extends BaseEntity{
    private List<ProductRecordUsersResponse> productRecordUsersResponse;
    private Integer totalSize;

    public ProductRecordsResponse() {
    }

    public ProductRecordsResponse(List<ProductRecordUsersResponse> productRecordUsersResponse, Integer totalSize) {
        this.productRecordUsersResponse = productRecordUsersResponse;
        this.totalSize = totalSize;
    }

    public List<ProductRecordUsersResponse> getProductRecordUsersResponse() {
        return productRecordUsersResponse;
    }

    public void setProductRecordUsersResponse(List<ProductRecordUsersResponse> productRecordUsersResponse) {
        this.productRecordUsersResponse = productRecordUsersResponse;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
