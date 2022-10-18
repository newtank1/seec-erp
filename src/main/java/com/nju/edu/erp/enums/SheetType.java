package com.nju.edu.erp.enums;

public enum SheetType implements BaseEnum<SheetType,String>{
    SALE("销售单"),
    SALE_RETURN("销售退货单"),
    PURCHASE("进货单"),
    PURCHASE_RETURN("进货退货单"),
    RECEIVE("收款单"),
    PAYMENT("付款单"),
    SALARY("工资单"),
    WAREHOUSE_INPUT("入库单"),
    WAREHOUSE_OUTPUT("出库单"),
    WAREHOUSE_PRESENT("赠送单");

    private final String value;

    SheetType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
