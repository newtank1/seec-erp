package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleRecordVO {
    private Date date;

    private String productName;

    private String type;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}
