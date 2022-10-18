package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Salary.SalaryStrategy;
import com.nju.edu.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class YearSalaryStrategy implements SalaryStrategy {
    private final UserService userService;

    @Autowired
    public YearSalaryStrategy(UserService userService) {
        this.userService = userService;
    }


    @Override
    public BigDecimal calculateRawSalary(UserVO userVO) {
        UserInfoVO userInfo = userService.getUserInfo(userVO.getName());
        BigDecimal basicSalary = userInfo.getBasicSalary();
        BigDecimal roleSalary = userInfo.getRoleSalary();
        BigDecimal rawSalary = basicSalary.add(roleSalary);
        return rawSalary;
    }
}
