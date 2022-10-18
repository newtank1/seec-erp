package com.nju.edu.erp.model.vo;

import com.nju.edu.erp.enums.SheetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheetFilterVO{
    /**
     * 起始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 类型
     */
    private SheetType type;

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


