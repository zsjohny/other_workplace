package com.reliable.dao.impl

import com.reliable.dao.UserDao
import com.reliable.domain.FindPhoneRecord
import com.reliable.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import com.reliable.repository.UserRepository

/**
 * Created by nessary on 16-5-7.
 */
@Repository
class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.saveUser(user)
    }

    public void updateUser(User user) {
        userRepository.updateUser(user)
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user)
    }

    public User findOne(User user) {
        userRepository.findOne(user)
    }

    public void saveUserFind(FindPhoneRecord findPhoneRecord) {
        userRepository.saveUserFind(findPhoneRecord)
    }

    public long queryCount() {
        userRepository.queryCount()
    }

    public long queryAllCount() {
        userRepository.queryAllCount()
    }


}
