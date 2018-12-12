package com.reliable.service.impl

import com.reliable.dao.UserDao
import com.reliable.domain.FindPhoneRecord
import com.reliable.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.reliable.service.UserService

/**
 * Created by nessary on 16-5-7.
 */
@Service
@Transactional
class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    public void saveUser(User user) {
        userDao.saveUser(user)
    }

    public void updateUser(User user) {
        userDao.updateUser(user)
    }

    public void deleteUser(User user) {
        userDao.deleteUser(user)
    }

    public User findOne(User user) {
        return userDao.findOne(user)
    }

    public void saveUserFind(FindPhoneRecord findPhoneRecord) {
        userDao.saveUserFind(findPhoneRecord)
    }

    public long queryCount() {
        return userDao.queryCount()
    }

    public long queryAllCount() {
        return userDao.queryAllCount()
    }


}
