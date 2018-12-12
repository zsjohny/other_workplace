package com.tunnel.dao.impl

import com.tunnel.dao.UserDao
import com.tunnel.domain.User
import com.tunnel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by nessary on 16-10-9.
 */
@Repository
class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository


    void saveUser(User user) {
        org.hibernate.Session
        userRepository.save(user)
    }
}
