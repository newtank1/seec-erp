package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnsSheetVO;
import org.springframework.stereotype.Service;

@Service

public interface SaleReturnsService extends SaleSheetService,SheetService<SaleReturnsSheetVO,SaleReturnsSheetState>{

    /**
     * 根据销售退货单Id搜索销售退货单信息
     * @param saleReturnsSheetId 销售退货单Id
     * @return 销售退货单全部信息
     */
    SaleReturnsSheetVO getSaleReturnsSheetById(String saleReturnsSheetId);
}
