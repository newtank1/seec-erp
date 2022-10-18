package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.service.Salary.SalaryStrategy;
import com.nju.edu.erp.service.Salary.SalaryStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalaryStrategyFactoryImpl implements SalaryStrategyFactory {
    private final BasicSalaryStrategy basicSalaryStrategy;
    private final MonthSalaryStrategy monthSalaryStrategy;
    private final YearSalaryStrategy yearSalaryStrategy;

    @Autowired
    public SalaryStrategyFactoryImpl(BasicSalaryStrategy basicSalaryStrategy, MonthSalaryStrategy monthSalaryStrategy, YearSalaryStrategy yearSalaryStrategy) {
        this.basicSalaryStrategy = basicSalaryStrategy;
        this.monthSalaryStrategy = monthSalaryStrategy;
        this.yearSalaryStrategy = yearSalaryStrategy;
    }

    @Override
    public SalaryStrategy getSalaryStrategy(Role role) {
        switch (role){
            case INVENTORY_MANAGER:
            case FINANCIAL_STAFF:
            case HR:return monthSalaryStrategy;
            case SALE_MANAGER:
            case SALE_STAFF:return basicSalaryStrategy;
            case GM:return yearSalaryStrategy;

        }
        throw new RuntimeException("该角色不存在工资计算方式");
    }
}
