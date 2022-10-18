package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest



public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void testFindByUserNameAndPassword() {
        User user = userDao.findByUsernameAndPassword("67", "123456");
        System.out.println(user);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void testFindByUsername() {
        User user = userDao.findByUsername("67");
        System.out.println(user);
    }


}
