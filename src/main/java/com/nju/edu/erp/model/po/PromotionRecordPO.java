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
public class PromotionRecordPO{
    /**
     * 促销记录id
     */
    private Integer id;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 促销方式id
     */
    private Integer promotionId;


    /**
     * 促销触发时间
     */
    private Date time;

}
