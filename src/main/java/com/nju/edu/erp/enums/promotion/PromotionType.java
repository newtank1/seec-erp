package com.nju.edu.erp.enums.promotion;

import com.nju.edu.erp.enums.BaseEnum;

public enum PromotionType implements BaseEnum<PromotionType,String> {
    PRESENT("赠品"),VOUCHER("代金券"),REDUCE("降价");

    private final String value;

    PromotionType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
