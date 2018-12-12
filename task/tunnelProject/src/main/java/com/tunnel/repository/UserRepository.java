package com.tunnel.repository;

import com.tunnel.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Ness on 2016/10/8.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
}
