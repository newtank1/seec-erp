package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.UserClockInPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserClockInDao {
    int insert(UserClockInPO clockInPO);

    List<UserClockInPO> findByUserName(String name);
}
