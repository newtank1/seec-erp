package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSheetPO {
    /**
     * 付款单id，格式为FKD-yyyyMMdd-xxxxxx
     */
    private String id;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 状态
     */
    private FinancialSheetState state;

    /**
     * 转账列表
     */
    private List<TransferPO> transferList;

    /**
     * 转账总额
     */
    private BigDecimal totalAmount;

    /**
     * 创建时间
     */
    private Date createTime;
}
