package com.com.shark.test.feifei.query;

import com.com.shark.test.feifei.base.BaseQueryTest;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.query.EntityQuery;
import com.shark.test.feifei.entity.Student;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class UpdateTest extends BaseQueryTest {

	@Test
	public void updateById(){
		query= EntityQuery.create().update(Student.create().setId(1).setName("MIKE"));
	}

	@Test
	public void updateByIdWhere(){
		query= EntityQuery.create().update(Student.create().setId(1).setName("mike"));
	}

	@Test
	public void updateWhere(){
		query= EntityQuery.create()
				.update(Student.create().setName("mary"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void updateReturnId(){
		query= EntityQuery.create().addOption(QueryOptions.RETURN_ID)
				.update(Student.create().setName("MIKE"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void updateReturnRecord(){
		query= EntityQuery.create()
				.addOption(QueryOptions.RETURN_RECORD)
				.update(Student.create().setName("MIKE"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

}
