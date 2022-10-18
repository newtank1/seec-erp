package com.nju.edu.erp.service.Salary;

import com.nju.edu.erp.model.vo.user.UserVO;

import java.math.BigDecimal;

public interface SalaryStrategy {

    /**
     * 计算原始薪资
     * @param userVO 用户
     * @return 原始薪资值
     */
    BigDecimal calculateRawSalary(UserVO userVO);

}
