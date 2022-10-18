package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.SalarySheetPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class SalaryDaoTest {

    @Autowired
    SalaryDao salaryDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void getAll() {
        List<SalarySheetPO> all = salaryDao.findAll();
        System.out.println(all);
    }
}
