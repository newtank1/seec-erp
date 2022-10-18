package com.nju.edu.erp.model.vo.startBill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartBillVO{
    /**
     * id
     */
    private Integer id;

    /**
     * 商品列表
     */
    private List<StartProductVO> products;

    /**
     * 客户列表
     */
    private List<StartCustomerVO> customers;

    /**
     * 银行账户列表
     */
    private List<StartBankAccountVO> bankAccounts;


    /**
     * 创建时间
     */
    private Date createTime;

}
