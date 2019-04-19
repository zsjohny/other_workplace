package com.yujj.test.util;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "dataSourceTransactionManager", defaultRollback = false)
@WebAppConfiguration
@ContextHierarchy({ @ContextConfiguration(locations = {
		"classpath:spring/spring-yujj_servlet.xml",
		"classpath:spring/spring-yujj-*.xml" }) })
public class YjjAbstractUnitilsTest extends
		AbstractTransactionalJUnit4SpringContextTests {
}
