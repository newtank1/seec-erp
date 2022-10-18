package com.nju.edu.erp.service.Salary;

import com.nju.edu.erp.enums.Role;

public interface SalaryStrategyFactory {

    /**
     * 根据身份获取薪资策略
     * @param role 用户身份
     * @return 薪资策略
     */
    SalaryStrategy getSalaryStrategy(Role role);
}
