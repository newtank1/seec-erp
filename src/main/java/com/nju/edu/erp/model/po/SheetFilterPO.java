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
/**
 * 单据过滤的类。属性为过滤条件，如果为null则这个方面没有过滤条件。
 *
 */
public class SheetFilterPO{
    /**
     * 起始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

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
