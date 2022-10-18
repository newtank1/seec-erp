package com.nju.edu.erp.service.Salary;

import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.service.Impl.Sheet.SalaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest

public class BonusTest {
    @Autowired
    SalaryServiceImpl salaryService;

    @Test
    @Transactional
    @Rollback(value = true)
    void testBonus() {
        SalarySheetVO salarySheetVO = SalarySheetVO.builder()
                .id("GZD-20220707-00005")
                .rawSalary(new BigDecimal(4000))
                .build();
        salaryService.makeBonus("GZD-20220707-00005", new BigDecimal(4000));
    }

}
