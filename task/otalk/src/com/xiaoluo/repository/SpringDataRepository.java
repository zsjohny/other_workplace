package com.xiaoluo.repository;

import com.xiaoluo.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

//import org.springframework.data.repository.RepositoryDefinition;

//1.需要继承的第一个参数是实体类。第二个参数是实体类对应Id生成的类的类名
//2.也可以用注解的形式。第一个参数domainClass是对应的实体类， 
//第二个参数idClass是对应的实体类产生id的实体类
//@RepositoryDefinition(domainClass = Customer.class, idClass = Integer.class)
public interface SpringDataRepository extends Repository<Customer, Integer> {
	// 查询的方法有三种，第一种是固定的，利用规定的参数命令形式，进行查询
	/*
	 * 1. 不是随便声明的. 而需要符合一定的规范 2. 查询方法以 find | read | get 开头 3.
	 * 涉及条件查询时，条件的属性用条件关键字连接 4. 要注意的是：条件属性以首字母大写。 5. 支持属性的级联查询. 若当前类有符合条件的属性,
	 * 则优先使用, 而不使用级联属性. 若需要使用级联属性, 则属性之间使用 _ 进行连接.
	 */
	// 第二种，占位符的查询的方式
	@Query("from Customer where customerName like %1?%")
	List<Customer> queryAll(String customerName);
}
