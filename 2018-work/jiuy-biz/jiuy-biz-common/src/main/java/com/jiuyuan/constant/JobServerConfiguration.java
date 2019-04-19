package com.jiuyuan.constant;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 调用job服务的jobName
 * @package jiuy-biz
 * @description
 * @date 2018/6/7 16:07
 * @copyright 玖远网络
 */
public enum JobServerConfiguration{

    // 商品定时上架
    PRODUCT_PUTAWAY("Product", "timming.product.putaway", "supplier_job_timingProductPutaway"),
    // sku更新库存
    SKU_SET_REMAIN_COUNT("Sku", "sku.set.remain.count", "supplier_job_setRemainCount")
    ;

    /** job服务上注册的jobGroupName */
    private String jobGroupName;
    /** job服务上注册的jobName */
    private String jobName;
    /** job服务回调服务地址, 对应的字典表code值 */
    private String callBackUrlCode;

    JobServerConfiguration(String jobGroupName, String jobName, String callBackUrlCode) {
        this.jobGroupName = jobGroupName;
        this.jobName = jobName;
        this.callBackUrlCode = callBackUrlCode;
    }


    public String getJobName() {
        return jobName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getCallBackUrlCode() {
        return callBackUrlCode;
    }

    public void setCallBackUrlCode(String callBackUrlCode) {
        this.callBackUrlCode = callBackUrlCode;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


}
