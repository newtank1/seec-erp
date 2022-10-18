package com.nju.edu.erp.service.Salary;


import com.nju.edu.erp.service.Impl.Salary.TaxTable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TaxTableTest {

    @Test
    void calculateTax() {
        TaxTable taxTable=new TaxTable();

        BigDecimal[] cases={
                new BigDecimal(0),
                new BigDecimal(5000),
                new BigDecimal(5100),
                new BigDecimal(8000),

                new BigDecimal(10000),
                new BigDecimal(17000),
                new BigDecimal(25000),
                new BigDecimal(30000),

                new BigDecimal(35000),
                new BigDecimal(40000),
                new BigDecimal(50000),
                new BigDecimal(60000),

                new BigDecimal(80000),
                new BigDecimal(85000),
                new BigDecimal(200000)
        };

        BigDecimal[] expects={
                new BigDecimal(0),
                new BigDecimal(0),
                new BigDecimal(3),
                new BigDecimal(90),

                new BigDecimal(290),
                new BigDecimal(990),
                new BigDecimal(2590),
                new BigDecimal(3590),

                new BigDecimal(4840),
                new BigDecimal(6090),
                new BigDecimal(9090),
                new BigDecimal(12090),

                new BigDecimal(19090),
                new BigDecimal(20840),
                new BigDecimal(72590),
        };

        for (int i = 0; i < cases.length; i++) {
            assertEquals(expects[i],taxTable.calculateTax(cases[i]));
        }

        try {
            taxTable.calculateTax(new BigDecimal(-1));
            fail();
        }catch (RuntimeException ignored){

        }
    }
}