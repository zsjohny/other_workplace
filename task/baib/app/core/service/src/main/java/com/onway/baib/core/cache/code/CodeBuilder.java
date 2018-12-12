/**
 * onway.com Inc.
 * Copyright (c) 2013-2014 All Rights Reserved.
 */
package com.onway.baib.core.cache.code;

import com.onway.baib.common.service.exception.BaibCoreException;
import com.onway.common.lang.DateUtils;
import com.onway.common.lang.StringUtils;

/**
 *  Code编码器,封装了code生成的业务规则
 * 
 * @author li.hong
 * @version $Id: CodeBuilder.java, v 0.1 2016年9月9日 下午12:03:44 li.hong Exp $
 */
public abstract class CodeBuilder {

    private static OrderCodeBuilder orderCodeBuilder = new OrderCodeBuilder();

    private static SubscribeCodeBuilder subscribeCodeBuilder = new SubscribeCodeBuilder();
    
    private static EnterpriseCodeBuilder enterpriseCodeBuilder = new EnterpriseCodeBuilder();
    
    private static GoodCodeBuilder goodCodeBuilder = new GoodCodeBuilder();
    
    private static InsuranceCodeBuilder insuranceCodeBuilder = new InsuranceCodeBuilder();

    /**
     * 根据业务类型得到builder实例
     * @param type
     * @return
     */
    public static CodeBuilder getCodeBuilder(CodeGenerateTypeEnum type) {
        switch (type) {
            case ENTERPRISE:
            	return enterpriseCodeBuilder;
            case ORDER:
                return orderCodeBuilder;
            case SUBSCRIBE:
                return subscribeCodeBuilder;
            case GOODCODE:
                return goodCodeBuilder;
            case PURCHASE:
                return goodCodeBuilder;
            case PURCHASE_QUOTE:
                return goodCodeBuilder;
            case INSURANCE:
                return insuranceCodeBuilder;
            default:
                throw new BaibCoreException("编码器类型不对 type:" + type);
        }
    }

    public String getCode(int seq) {
        StringBuilder sb = new StringBuilder(getPrefix());
        sb.append(DateUtils.getTodayString());
        sb.append(StringUtils.fillPrefix(Long.toString(seq % CodeGenerateConfig.NO_CIRCLE), "0",
            CodeGenerateConfig.NO_CIRCLE_LENGTH));
        sb.append(getSuffix());
        return sb.toString();
    }

    /** 获得后缀 */
    protected abstract String getSuffix();

    /** 获得前缀 */
    protected abstract String getPrefix();

}
