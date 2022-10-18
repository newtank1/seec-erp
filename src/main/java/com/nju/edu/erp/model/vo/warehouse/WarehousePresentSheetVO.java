package com.nju.edu.erp.model.vo.warehouse;

import com.nju.edu.erp.enums.sheetState.WarehousePresentSheetState;
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
public class WarehousePresentSheetVO {
    /**
     * 赠送单id，格式为ZSD-yyyyMMdd-xxxxxx
     */
    private String id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private WarehousePresentSheetState state;

    /**
     * 赠送商品id
     */
    private String pid;


    /**
     * 赠送数量
     */
    private Integer quantity;

    /**
     * 赠送批次进价
     */
    private BigDecimal expenses;
}