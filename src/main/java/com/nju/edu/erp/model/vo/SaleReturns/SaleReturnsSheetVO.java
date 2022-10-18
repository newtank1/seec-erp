package com.nju.edu.erp.model.vo.SaleReturns;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
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
public class SaleReturnsSheetVO {
    /**
     * 销售退货单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 关联的销售单id
     */
    private String saleSheetId;
    /**
     * 客户/销售商id
     */
    private Integer supplier;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;
    /**
     * 退货总额, 新建单据时前端传null
     */
    private BigDecimal finalAmount;
    /**
     * 单据状态, 新建单据时前端传null
     */
    private SaleReturnsSheetState state;
    /**
     * 销售退货单具体内容
     */
    List<SaleReturnsSheetContentVO> saleReturnsSheetContent;
    /**
     * 创建时间
     */
    private Date createTime;
}
