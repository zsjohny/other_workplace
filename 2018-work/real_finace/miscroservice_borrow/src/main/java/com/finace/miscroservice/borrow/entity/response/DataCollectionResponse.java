package com.finace.miscroservice.borrow.entity.response;

import com.finace.miscroservice.borrow.entity.BaseEntity;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataCollectionResponse extends BaseEntity {
    private String ljcjje;//累计成交金额
    private Double ljyhsy;//累计用户收益(元)
    private Integer ljzcyh;//累计注册用户(人)
    private Integer ljcjbs;//累计成交笔数(笔)
    private Integer ljcjrzs;//累计出借人总数(人)
    private Double bncjje;//本年度成交金额(元)
    private String zddhcjyezb;//最大单户出借余额占比
    private String qydhcjyezb;//其余单户出借余额占比
    private String zdshtzcjzb;//最大10户投资出借占比
    private String qyyhcjyezb;//其余用户出借余额占比

    private String xbnan;//性别男 占比
    private String xbnv;//性别女 占比
    private Integer dhjebs;//待还金额笔数(笔)
    private Integer ljjkrzs;//累计借款人总数(人)
    private Double dhje;//待还金额(万元)

    private Double xmyql;//项目逾期率(OP)
    private Double yqje;//逾期金额(万元)
    private Double jeyql;//金额逾期率(OP)
    private Double jstysyql;//90天以上逾期率(OP)
    private Double jstysyqje;//90天以上逾期金额(万元)
    private Double jstysljyql;//90天以上累计逾期率(OP)
    private Integer dqjkrsl;//当前借款人数量
    private Integer dqcjrsl;//当前出借人数量

    public Double getXmyql() {
        return xmyql=0d;
    }

    public void setXmyql(Double xmyql) {
        this.xmyql = xmyql;
    }

    public Double getYqje() {
        return yqje=0d;
    }

    public void setYqje(Double yqje) {
        this.yqje = yqje;
    }

    public Double getJeyql() {
        return jeyql=0d;
    }

    public void setJeyql(Double jeyql) {
        this.jeyql = jeyql;
    }

    public Double getJstysyql() {
        return jstysyql=0d;
    }

    public void setJstysyql(Double jstysyql) {
        this.jstysyql = jstysyql;
    }

    public Double getJstysyqje() {
        return jstysyqje=0d;
    }

    public void setJstysyqje(Double jstysyqje) {
        this.jstysyqje = jstysyqje;
    }

    public Double getJstysljyql() {
        return jstysljyql=0d;
    }

    public void setJstysljyql(Double jstysljyql) {
        this.jstysljyql = jstysljyql;
    }
    public String getLjcjje() {
        return ljcjje;
    }

    public void setLjcjje(String ljcjje) {

        this.ljcjje = NumberUtil.strFormat2(ljcjje);
    }
}
