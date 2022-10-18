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
public class MonthStrategyTest {
    @Autowired
    MonthSalaryStrategy monthSalaryStrategy;

    @Test
    @Transactional
    @Rollback(value = true)
    void testNoClockIn(){
        BigDecimal rawSalary = monthSalaryStrategy.calculateRawSalary(UserVO.builder().name("newtank").build());
        assertEquals(new BigDecimal("2000.00"),rawSalary);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void testTwoDayAbsent(){//7月打卡两天，即缺勤29天
        BigDecimal rawSalary = monthSalaryStrategy.calculateRawSalary(UserVO.builder().name("seecoder").build());
        assertEquals(new BigDecimal("43333.33"),rawSalary);
    }
}
