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
public class StartBankAccountPO{
    /**
     * 账户ID
     */
    private Integer accountId;

    /**
     * 期初账单id
     */
    private Integer billId;

    /**
     * 名称
     */
    private String name;


    /**
     * 余额
     */
    private BigDecimal surplus;

}
