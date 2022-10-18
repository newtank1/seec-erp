package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetPO {
    /**
     * 工资单id，格式为GZD-yyyyMMdd-xxxxxx
     */
    private String id;

    /**
     * 员工名称
     */
    private String staffName;

    /**
     * 状态
     */
    private FinancialSheetState state;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 应发工资
     */
    private BigDecimal rawSalary;

    /**
     * 个人所得税
     */
    private BigDecimal incomeTax;

    /**
     * 失业保险
     */
    private BigDecimal insurance;

    /**
     * 住房公积金
     */
    private BigDecimal housingFund;

    /**
     * 实发工资
     */
    private BigDecimal finalSalary;

    /**
     * 创建时间
     */
    private Date createTime;
}
