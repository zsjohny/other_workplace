package com.reliable.repository

import com.reliable.domain.FindPhoneRecord
import com.reliable.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

import java.lang.reflect.Field

/**
 * Created by nessary on 16-5-7.
 */
@Component
class UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveUser(User user) {
        mongoTemplate.save(user, "user")

    }

    public void saveUserFind(FindPhoneRecord findPhoneRecord) {
        mongoTemplate.save(findPhoneRecord, "findPhoneRecord")

    }

    public User findOne(User user) {

        Query query = new Query(Criteria.where("phone").is(user.getPhone()))
        return mongoTemplate.findOne(query, User.class)

    }

    public void updateUser(User user) {
        Query query = new Query(Criteria.where("phone").is(user.getPhone()))
        User us = mongoTemplate.findOne(query, User.class)

        if (us != null) {

            Update update = new Update()
            for (Field field : User.class.getDeclaredFields()) {
                if (field.getName() ==~ /[a-zA-Z]+/ && !"metaClass".equals(field.getName())) {
                    if (user.getProperty(field.getName()) != null) {
                        update.set(field.getName(), String.valueOf(user.getProperty(field.getName())))
                    } else {

                        update.set(field.getName(), String.valueOf(us.getProperty(field.getName()) == null ? "" : String.valueOf(us.getProperty(field.getName()))))
                    }
                }

            }

            mongoTemplate.updateFirst(new Query(Criteria.where("phone").is(user.getPhone())), update, "user")
        }

    }

    public void deleteUser(User user) {
        mongoTemplate.remove(Criteria.where("phone").is(user.getPhone()), "user")
    }

    public long queryCount() {
        return mongoTemplate.count(new Query(Criteria.where("isSupport").is("true")), "user")

    }

    public long queryAllCount() {
        return mongoTemplate.count(new Query(), "user")

    }

    public static void main(String[] args) {
        User user1 = new User()

        for (Field field : User.class.getDeclaredFields()) {
            if (field.getName() ==~ /[a-zA-Z]+/ && !"metaClass".equals(field.getName())) {

                user1.setProperty(field.getName(), "2222");
                println user1
            }

        }
    }
}



