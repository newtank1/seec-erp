package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;

// 制定进货退货单 + 销售经理审批/总经理二级审批 + 更新客户表 + 更新库存
public interface PurchaseReturnsService extends SheetService<PurchaseReturnsSheetVO,PurchaseReturnsSheetState>{

}