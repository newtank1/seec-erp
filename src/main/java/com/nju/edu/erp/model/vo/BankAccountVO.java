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
public class BankAccountVO {

    /**
     *账户id
     */
    private int accountId;

    /**
     *名称
     */
    private String name;

    /**
     * 余额
     */
    private BigDecimal surplus;
}
