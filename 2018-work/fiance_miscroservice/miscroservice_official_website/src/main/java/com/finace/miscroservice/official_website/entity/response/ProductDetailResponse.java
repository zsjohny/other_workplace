package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.official_website.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * Created by hyf on 2018/3/6.
 */
@Data
@ToString
public class ProductDetailResponse extends BaseEntity {
        private String name;  //标的名称
        private String account;  //借贷总金额
        private String accountYes; //已购买金额
        private String surplusAccount;
        private String apr; //年利率
        private String lowestAccount;//最低投标金额
        private String mostAccount;//最多投标总额
        private String timeLimitDay;//投标期限
        private String scales; //已购比例
        private String use;//用途
        private String addApr;//加息年利率
        private String id;
        private String litpic; //缩略图
        private String status;  //状态 区分标的状态 0 待审 1 初审通过 2 初审不通过 3满标复审中 6还款中 8已还款
        private String remmoney; //
        private String trustLevel;
        private String validTime;
        private String financeCompany; //融资企业
        private String loanUsage;//借款用途
        private String payment;//还款来源
        private String viewType;//标的详情显示问题0-显示老的 名门望族 1-显示新的 天府名檀

        private String imgurl1;  //项目照片 1-10
        private String imgurl2;
        private String imgurl3;
        private String imgurl4;
        private String imgurl5;
        private String imgurl6;
        private String imgurl7;
        private String imgurl8;
        private String imgurl9;
        private String imgurl10;

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

        public String getSurplusAccount() {
                return surplusAccount;
        }

        public void setSurplusAccount(String surplusAccount) {
                this.surplusAccount = Double.parseDouble(surplusAccount)<0?"0":surplusAccount;
        }
}
