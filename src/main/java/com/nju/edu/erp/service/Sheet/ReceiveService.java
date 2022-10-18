package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.vo.ReceiveSheetVO;
import org.springframework.stereotype.Service;

@Service
public interface ReceiveService extends SheetService<ReceiveSheetVO,FinancialSheetState>{

}
