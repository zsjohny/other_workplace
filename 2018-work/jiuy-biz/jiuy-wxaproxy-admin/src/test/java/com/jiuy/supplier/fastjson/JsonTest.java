package com.jiuy.supplier.fastjson;

import org.junit.Test;

import com.admin.core.base.tips.SuccessTip;
import com.admin.core.base.tips.Tip;
import com.alibaba.fastjson.JSON;

public class JsonTest {

	@Test
	public void test() {
		Tip tip = new SuccessTip();
		System.out.println(JSON.toJSONString(tip));
	}
}
