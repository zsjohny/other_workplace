package org.dream.utils.quota;

/**
 * 订阅行情的实体类
 * Created by nessary on 16-9-20.
 */
public class OrderQuotaInfoModel  {

    /**
     * 交易所编码
     */
    private String exchangeNo;

    /**
     * 合约编码
     */
    private String contractCode;

    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderQuotaInfoModel that = (OrderQuotaInfoModel) o;

        if (exchangeNo != null ? !exchangeNo.equals(that.exchangeNo) : that.exchangeNo != null) return false;
        return contractCode != null ? contractCode.equals(that.contractCode) : that.contractCode == null;

    }

    @Override
    public int hashCode() {
        int result = exchangeNo != null ? exchangeNo.hashCode() : 0;
        result = 31 * result + (contractCode != null ? contractCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderQuotaInfoModel{" +
                "exchangeNo='" + exchangeNo + '\'' +
                ", contractCode='" + contractCode + '\'' +
                '}';
    }
}
