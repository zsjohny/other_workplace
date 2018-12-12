/**
 * onway.com Inc.
 * Copyright (c) 2013-2015 All Rights Reserved.
 */
package com.onway.baib.core.cache.code;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.onway.baib.common.dal.daointerface.SequenceDAO;
import com.onway.baib.common.dal.dataobject.SequenceDO;
import com.onway.baib.common.service.enums.BaibCoreResultCodeEnum;
import com.onway.baib.core.exception.BaseRuntimeException;

/**
 * 编码生成组件实现
 *
 * @author li.hong
 * @version $Id: CodeGenerateComponentImpl.java, v 0.1 2016年9月9日 上午11:58:24 li.hong Exp $
 */
public class CodeGenerateComponentImpl implements CodeGenerateComponent {

    /** logger */
    protected static final Logger logger = Logger.getLogger(CodeGenerateComponentImpl.class);

    private SequenceDAO sequenceDAO;

    /** 事物模板 */
    private TransactionTemplate transactionTemplate;

    /**
     * @see com.onway.baib.core.cache.code.paycore.core.service.code.CodeGenerateComponent#nextCodeByType(com.onway.baib.core.cache.code.paycore.core.model.enums.CodeGenerateTypeEnum)
     */
    @Override public String nextCodeByType(final CodeGenerateTypeEnum codeType) {

        int sequenceNo = transactionTemplate.execute(new TransactionCallback<Integer>() {

            /**
             * 模拟sequence生成
             * @see org.springframework.transaction.support.TransactionCallback#doInTransaction(org.springframework.transaction.TransactionStatus)
             */
            @Override public Integer doInTransaction(TransactionStatus status) {
                int count = sequenceDAO.nextSequence(codeType.name());
                if (count == 0) { //第一次,则数据初始化
                    SequenceDO sequenceDO = new SequenceDO();
                    sequenceDO.setBizName(codeType.name());
                    sequenceDO.setCurrentValue(CodeGenerateConfig.INIT_VALUE);
                    sequenceDO.setIncrementValue(CodeGenerateConfig.INCREMENT_STEP);
                    sequenceDAO.insert(sequenceDO);
                } else if (count > 1) {
                    logger.warn("业务序列号数据异常，存在重复记录,bizKey:" + codeType.name());
                    throw new BaseRuntimeException(BaibCoreResultCodeEnum.DATA_ERROR.getMessage());
                }
                SequenceDO sequence = sequenceDAO.currentSequence(codeType.name());
                return sequence.getCurrentValue();
            }
        });

        if (sequenceNo <= 0) {
            throw new BaseRuntimeException("编码序列不存在:" + codeType.name());
        }
        return CodeBuilder.getCodeBuilder(codeType).getCode(sequenceNo);
    }

    public void setSequenceDAO(SequenceDAO sequenceDAO) {
        this.sequenceDAO = sequenceDAO;
    }

    /**
     * Setter method for property <tt>transactionTemplate</tt>.
     *
     * @param transactionTemplate value to be assigned to property transactionTemplate
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}
