package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.SalaryDao;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Sheet.SalaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SalaryServiceImplTest {
    @Autowired
    SalaryService salaryService;
    @Autowired
    SalaryDao salaryDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void makeSheet() {
        UserVO userVO=UserVO.builder().name("newtank").build();
        SalarySheetVO salarySheetVO=SalarySheetVO.builder()
                        .staffName("seecoder").build();
        salaryService.makeSheet(userVO,salarySheetVO);
        SalarySheetPO latest = salaryDao.getLatest();
        assertEquals(new BigDecimal("43333.33"),latest.getRawSalary());
        assertEquals(new BigDecimal("7090.00"),latest.getIncomeTax());
        assertEquals(new BigDecimal("3750.00"),latest.getHousingFund());
        assertEquals(new BigDecimal("866.67"),latest.getInsurance());
        assertEquals(new BigDecimal("31626.66"),latest.getFinalSalary());
    }
}