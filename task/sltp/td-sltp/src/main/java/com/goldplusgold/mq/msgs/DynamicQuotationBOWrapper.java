package com.goldplusgold.mq.msgs;

/**
 * 动态行情消息包装器
 * Created by Administrator on 2017/5/19.
 */
public class DynamicQuotationBOWrapper {

    private DynamicQuotationBO bo;
    private String instType;

    public DynamicQuotationBO getBo() {
        return bo;
    }

    public void setBo(DynamicQuotationBO bo) {
        this.bo = bo;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    @Override
    public String toString() {
        return "DynamicQuotationBOWrapper{" +
                "bo=" + bo +
                ", instType='" + instType + '\'' +
                '}';
    }

}