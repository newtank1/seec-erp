package com.nju.edu.erp.service.Impl.Financial;

import com.nju.edu.erp.model.vo.FinancialStatusVO;
import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SaleRecordVO;
import com.nju.edu.erp.service.Financial.FinancialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FinancialServiceImplTest {
    @Autowired
    FinancialService financialService;

    @Test
    void getBalance() {
        String start="2021-01-01 00:00:00";
        String end="2022-12-31 00:00:00";
        FinancialStatusVO expect=FinancialStatusVO.builder()
                .salesIncome(new BigDecimal("8840000.00"))//1800000进货退货+7040000销售
                .salesIncomeDiscount(new BigDecimal("1208600.00"))
                .productIncome(BigDecimal.ZERO)
                .salesExpense(new BigDecimal("9200000.00"))//8300000进货+900000销售退货
                .productExpense(new BigDecimal("120000.00"))
                .employeeExpense(new BigDecimal("250000.00"))
                .profit(new BigDecimal("-1938600.00"))
                .build();
        FinancialStatusVO balance = financialService.getBalance(start, end);
        assertEquals(expect,balance);
    }

    @Test
    void testGetSaleDetail() {
        SaleRecordFilterVO vo = SaleRecordFilterVO.builder()
                .productName("小米手机")
                .build();
        List<SaleRecordVO> saleRecordVOS = financialService.getSaleDetail(vo);
        System.out.println(saleRecordVOS.size());
    }
}