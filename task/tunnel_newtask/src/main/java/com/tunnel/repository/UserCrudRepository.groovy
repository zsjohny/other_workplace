package com.tunnel.repository

import com.tunnel.domain.User
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface UserCrudRepository extends CrudRepository<User, Integer> {
    User findByName(String name)

}
