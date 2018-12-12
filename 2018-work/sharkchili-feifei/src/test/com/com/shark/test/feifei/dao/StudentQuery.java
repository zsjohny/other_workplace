package com.com.shark.test.feifei.dao;

import com.shark.feifei.query.query.EntityQuery;
import com.shark.feifei.query.query.Query;
import com.shark.test.feifei.entity.Student;


/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/15 0015
 */
public class StudentQuery extends EntityQuery<Student> {

	public StudentQuery() {
	}

	public StudentQuery(String sql) {
		super(sql);
	}

	public static Query create(){
		return new StudentQuery();
	}

	public static Query create(String sql){
		return new StudentQuery(sql);
	}
}
