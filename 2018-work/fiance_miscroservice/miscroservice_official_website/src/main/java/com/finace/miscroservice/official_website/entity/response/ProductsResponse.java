package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.official_website.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductsResponse extends BaseEntity{
//    scales,A.lowest_account as lowestAccount,A.most_account as mostAccount,
//        (A.account-A.account_yes)/A.flow_money as fen,A.add_apr as addApr
    private Integer id;  //订单号
    private String name;//标的名称
    private String timeLimitDay;// 期限
    private String use;//用途
    private Double apr;//年利率
    private  String litpic;//缩略图
    private Integer status;//状态 区分标的状态 0 待审 1 初审通过 2 初审不通过 3满标复审中 6还款中 8已还款
    private String scales;//标的剩余百分比
    private Double account;//借贷总金额
    private Double accountYes;//已购金额
    private String surplusAccount;
    private Double residueAccount;//剩余金额
    private Double lowestAccount;//最低投标金额
    private Double mostAccount;//最多投标总额
    private Double addApr;//加息年利率

    public String getScales() {
        return scales;
    }

    //格式转换
    public void setScales(String scales) {
        Double sca = Double.parseDouble(scales)>1d?1d:Double.parseDouble(scales);
        String sacStr = String.valueOf(NumberUtil.multiply(2,sca,100));
        String scaStr2 = NumberUtil.strFormat2(sacStr);
        this.scales = scaStr2;
    }

    public Double getResidueAccount() {
        return residueAccount;
    }

    public void setResidueAccount(Double residueAccount) {
        if (residueAccount<0){
            residueAccount=0d;
        }
        this.residueAccount = residueAccount;
    }

    public static void main(String[] args) {
        Long l = 1522158473l;
        System.out.println(        DateUtils.dateStr2(String.valueOf(l))
        );
   }

}
