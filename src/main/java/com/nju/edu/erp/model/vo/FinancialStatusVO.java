package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialStatusVO {

    /**
     * 销售收入（原始）
     */
    private BigDecimal salesIncome;

    /**
     * 销售折扣额
     */
    private BigDecimal salesIncomeDiscount;

    /**
     * 商品收入（原始）
     */
    private BigDecimal productIncome;

//    /**
//     * 商品折扣额
//     */
//    private BigDecimal productIncomeDiscount;

    /**
     * 销售成本
     */
    private BigDecimal salesExpense;

    /**
     * 商品类支出
     */
    private BigDecimal productExpense;

    /**
     * 人力成本
     */
    private BigDecimal employeeExpense;

    /**
     * 利润
     */
    private BigDecimal profit;
}
