package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface SalaryService extends SheetService<SalarySheetVO, FinancialSheetState> {

    List<SalarySheetVO> getAll();

    /**
     * 制定年终奖
     * @param sheetId 工资单id
     * @param newSalary 新的12月工资，会替换原工资单中的工资，随后计税
     */
    void makeBonus(String sheetId, BigDecimal newSalary);
}
