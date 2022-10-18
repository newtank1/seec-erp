package com.nju.edu.erp.enums.promotion;

import com.nju.edu.erp.enums.BaseEnum;

public enum PromotionTriggerType implements BaseEnum<PromotionTriggerType,String> {
    CUSTOMER_LEVEL("客户级别"),PRODUCT("商品组合"),PRICE("总价");

    private final String value;

    PromotionTriggerType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
