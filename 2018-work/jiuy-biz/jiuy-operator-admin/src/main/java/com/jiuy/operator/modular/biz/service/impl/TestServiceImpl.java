package com.jiuy.operator.modular.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.common.constant.DSEnum;
import com.admin.core.mutidatasource.annotion.DataSource;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.operator.common.system.persistence.dao.TestMapper;
import com.jiuy.operator.common.system.persistence.model.Test;
import com.jiuy.operator.modular.biz.service.ITestService;
import com.jiuyuan.service.common.YunXinSmsService;

/**
 * 测试服务
 *
 * @author fengshuonan
 * @date 2017-06-23 23:02
 */
@Service
public class TestServiceImpl implements ITestService {

	@Autowired
	YunXinSmsService yunXinSmsService;

	@Autowired
	TestMapper testMapper;

	@Override
	@DataSource(name = DSEnum.DATA_SOURCE_BIZ)
	public void testBiz() {
		Test test = testMapper.selectById(1);
		test.setId(22);
		test.insert();
	}

	@Override
	@DataSource(name = DSEnum.DATA_SOURCE_GUNS)
	public void testGuns() {
		Test test = testMapper.selectById(1);
		test.setId(33);
		test.insert();
	}

	@Override
	public void testSms() {
		JSONArray params = new JSONArray();
		params.add("111111");// 初始密码
		// yunXinSmsService.sendNotice("15824400571", params, 3120104);
	}

	@Override
	@Transactional
	public void testAll() {
		testBiz();
		testGuns();
		// int i = 1 / 0;
	}

}
