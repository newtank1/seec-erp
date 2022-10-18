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
public class StartCustomerPO{
    /**
     * 商品id
     */
    private String id;

    /**
     * 期初账单id
     */
    private Integer billId;

    /**
     * 名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 类型
     */
    private String type;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String zipcode;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 信用额度
     */
    private BigDecimal lineOfCredit;

    /**
     * 应收
     */
    private BigDecimal receivable;

    /**
     * 应付
     */
    private BigDecimal payable;


    /**
     * 业务员名称
     */
    private String operator;

}

