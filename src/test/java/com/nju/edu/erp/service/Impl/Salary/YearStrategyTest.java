package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.model.vo.user.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class YearStrategyTest {
    @Autowired
    YearSalaryStrategy yearSalaryStrategy;

    @Test
    @Transactional
    @Rollback(value = true)
    void test(){
        BigDecimal rawSalary = yearSalaryStrategy.calculateRawSalary(UserVO.builder().name("67").build());
        assertEquals(new BigDecimal("120000.00"),rawSalary);
    }
}
