package com.onway.baib.core.cache.code;

/**
 * 商品编码生成器
 * @author weina
 *
 */
public class InsuranceCodeBuilder extends CodeBuilder {

	@Override
	protected String getSuffix() {
		
		return "";
	}

	@Override
	protected String getPrefix() {
		
		return CodeGenerateConfig.INSURANCE_PRE;
	}

}
