package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.BankAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountDao {

    int insert(BankAccountPO accountPO);

    BankAccountPO findByName(String name);

    List<BankAccountPO> findByKeyword(String keyword);

    int deleteById(Integer id);

    int update(BankAccountPO accountPO);

    BankAccountPO findById(Integer id);

    List<BankAccountPO> findAll();
}
