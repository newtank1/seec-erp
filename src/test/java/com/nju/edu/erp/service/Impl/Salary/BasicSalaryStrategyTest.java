package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BasicSalaryStrategyTest {
    @Autowired
    BasicSalaryStrategy  basicSalaryStrategy;
    @Test
    @Transactional
    @Rollback(value = true)
    void calculateRawSalary() {
        UserVO userVO= UserVO.builder().name("xiaoshoujingli").role(Role.SALE_MANAGER).build();
        BigDecimal rawSalary = basicSalaryStrategy.calculateRawSalary(userVO);
        assertEquals(new BigDecimal("10000.00"),rawSalary);//基本工资被打卡扣光了+10000提成
    }
}