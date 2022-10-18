package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum WarehousePresentSheetState implements BaseEnum<WarehousePresentSheetState,String> {

    PENDING("待审批"), // 待总经理审批
    SUCCESS("审批完成"),
    FAILURE("审批失败");

    private final String value;

    WarehousePresentSheetState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
