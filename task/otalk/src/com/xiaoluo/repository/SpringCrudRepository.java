package com.xiaoluo.repository;

import com.xiaoluo.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface SpringCrudRepository extends CrudRepository<Customer, Integer> {

}
