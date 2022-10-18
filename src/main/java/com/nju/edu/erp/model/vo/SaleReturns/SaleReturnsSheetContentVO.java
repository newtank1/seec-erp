package com.nju.edu.erp.model.vo.SaleReturns;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnsSheetContentVO {
    /**
     * 商品id
     */
    private String pid;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 优惠后单价
     */
    private BigDecimal unitDiscountPrice;
    /**
     * 实际退还总金额
     */
    private BigDecimal totalPrice;
    /**
     * 备注
     */
    private String remark;
}
