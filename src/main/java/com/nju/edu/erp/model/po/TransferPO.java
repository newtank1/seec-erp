package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferPO {
    /**
     * id
     */
    private Integer id;

    /**
     * 收款单id
     */
    private String sheetId;

    /**
     * 银行账户
     */
    private Integer accountId;

    /**
     * 转账金额
     */
    private BigDecimal transferMoney;


    /**
     * 备注
     */
    private String remark;
}
