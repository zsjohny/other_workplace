/**
 * onway.com Inc.
 * Copyright (c) 2013-2015 All Rights Reserved.
 */
package com.onway.baib.core.cache.code;

/**
 * 编码生成组件
 * 
 * @author li.hong
 * @version $Id: CodeGenerateComponent.java, v 0.1 2016年9月9日 下午1:52:12 li.hong Exp $
 */
public interface CodeGenerateComponent {

    /**
     * 根据类型获得编码 
     * @param codeType  编码类型枚举
     * @return
     */
    public String nextCodeByType(CodeGenerateTypeEnum codeType);

}
