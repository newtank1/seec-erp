package com.nju.edu.erp.service.Impl.Salary;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxTable {

    private final BigDecimal[] standards={
            new BigDecimal(5000),
            new BigDecimal(8000),
            new BigDecimal(17000),
            new BigDecimal(30000),
            new BigDecimal(40000),
            new BigDecimal(60000),
            new BigDecimal(85000)};

    private final BigDecimal[] rate={
            new BigDecimal(3),
            new BigDecimal(10),
            new BigDecimal(20),
            new BigDecimal(25),
            new BigDecimal(30),
            new BigDecimal(35),
            new BigDecimal(45)
    };

    public BigDecimal calculateTax(BigDecimal rawSalary){
        if(rawSalary.compareTo(BigDecimal.ZERO)<0)
            throw new RuntimeException("薪资不能为负");
        int tableSize=standards.length;
        BigDecimal total=BigDecimal.ZERO;
        for(int i=tableSize-1;i>=0;i--){
            if(rawSalary.compareTo(standards[i])>0){
                BigDecimal toCal=rawSalary.subtract(standards[i]);
                BigDecimal stageTax=toCal.multiply(rate[i]).divide(new BigDecimal(100), RoundingMode.HALF_EVEN);
                total=total.add(stageTax);
                rawSalary=standards[i];
            }
        }
        return total;
    }
}
