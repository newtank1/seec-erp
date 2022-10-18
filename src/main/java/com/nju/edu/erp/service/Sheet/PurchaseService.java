package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;

// 制定进货单 + 销售经理审批/总经理二级审批 + 更新客户表/制定入库单草稿 + 库存管理人员确认入库单/总经理审批 + 更新库存
public interface PurchaseService extends SheetService<PurchaseSheetVO,PurchaseSheetState>{

    /**
     * 根据进货单Id搜索进货单信息
     * @param purchaseSheetId 进货单Id
     * @return 进货单全部信息
     */
    PurchaseSheetVO getPurchaseSheetById(String purchaseSheetId);




}
