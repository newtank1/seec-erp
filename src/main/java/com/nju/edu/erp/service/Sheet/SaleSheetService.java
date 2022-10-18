package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SaleRecordVO;

import java.util.List;

public interface SaleSheetService {
    List<SaleRecordVO> findRecords(SaleRecordFilterVO filterVO);
}
