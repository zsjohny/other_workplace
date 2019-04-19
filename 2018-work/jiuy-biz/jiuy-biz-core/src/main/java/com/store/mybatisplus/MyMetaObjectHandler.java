package com.store.mybatisplus;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

/**
 * 公共字段自动填充 特别说明： 需要填充的字段需要忽略验证，查看文档问题部分！
 * 
 * @author zhaoxinglin
 *
 */
public class MyMetaObjectHandler extends MetaObjectHandler {
	// private static final Logger logger =
	// LoggerFactory.getLogger(MyMetaObjectHandler.class);

	/**
	 * 测试 user 表 name 字段为空自动填充
	 */
	public void insertFill(MetaObject metaObject) {
		// logger.info("公共字段自动填充，insertFill");
		/*
		 * 自定义填充公共 name 字段 Object testType = metaObject.getValue("testType");
		 * System.err.println("testType==" + testType); if (null == testType) {
		 * metaObject.setValue("testType", 3); }
		 */
	}

	/**
	 * 更新填充
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		// logger.info("公共字段自动填充，updateFill");
	}
}
