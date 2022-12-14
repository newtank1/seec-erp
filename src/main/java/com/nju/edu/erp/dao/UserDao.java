package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.po.UserInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {

    User findByUsernameAndPassword(String username, String password);

    int createUser(User user);

    User findByUsername(String username);

    UserInfoPO getUserInfo(String username);

    List<User> getAll();

    int insertInfo(UserInfoPO userInfoPO);

    List<UserInfoPO> getAllInfo();
}
