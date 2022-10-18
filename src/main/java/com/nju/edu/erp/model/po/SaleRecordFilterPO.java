package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleRecordFilterPO{
    /**
     * 起始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 业务员
     */
    private String salesman;


    /**
     * 仓库id
     */
    private Integer warehouseId;

}
