package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Salary.SalaryStrategy;
import com.nju.edu.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MonthSalaryStrategy implements SalaryStrategy {
    private final UserService userService;

    @Autowired
    public MonthSalaryStrategy(UserService userService) {
        this.userService = userService;
    }


    @Override
    public BigDecimal calculateRawSalary(UserVO userVO) {
        UserInfoVO userInfo = userService.getUserInfo(userVO.getName());
        BigDecimal basicSalary = userInfo.getBasicSalary();

        //缺勤扣钱
        int absentDay=userService.getAbsentDay(userVO);
        basicSalary=basicSalary.multiply(new BigDecimal(Math.max(30-absentDay,0))).divide(new BigDecimal(30), RoundingMode.HALF_EVEN);


        BigDecimal roleSalary = userInfo.getRoleSalary();
        BigDecimal rawSalary = basicSalary.add(roleSalary);
        return rawSalary;
    }

}
