package com.nju.edu.erp.service.Financial;

import com.nju.edu.erp.model.vo.FinancialStatusVO;
import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SaleRecordVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FinancialService {

    List<SaleRecordVO> getSaleDetail(SaleRecordFilterVO filterVO);

    List<Object> getSheetRecord(SheetFilterVO sheetFilterVO);

    FinancialStatusVO getBalance(String beginDateStr, String endDateStr);
}
