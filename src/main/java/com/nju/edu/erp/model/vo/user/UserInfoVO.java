package com.nju.edu.erp.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO{
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 手机
     */
    private String phone;

    /**
     * 基本工资
     */
    private BigDecimal basicSalary;

    /**
     * 岗位工资
     */
    private BigDecimal roleSalary;

    /**
     * 岗位级别
     */
    private Integer level;


    /**
     * 银行账户
     */
    private String account;

}
