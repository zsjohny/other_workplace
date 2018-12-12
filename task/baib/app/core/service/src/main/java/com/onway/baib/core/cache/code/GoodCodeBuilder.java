package com.onway.baib.core.cache.code;

/**
 * 商品编码生成器
 * @author weina
 *
 */
public class GoodCodeBuilder extends CodeBuilder {

	@Override
	protected String getSuffix() {
		
		return "";
	}

	@Override
	protected String getPrefix() {
		
		return CodeGenerateConfig.GOOD_PRE;
	}

}
