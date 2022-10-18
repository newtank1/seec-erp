package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.enums.promotion.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionPO{
    /**
     * 促销id
     */
    private Integer id;

    /**
     * 触发方式（客户级别、商品组合、总价）
     */
    private PromotionTriggerType triggerType;

    /**
     * 促销方式（赠品、价格折让、赠送代金券）
     */
    private PromotionType promotionType;

    /**
     * 起始时间
     */
    private Date beginTime;

    /**
     * 间隔时间
     */
    private Long interval;


    /**
     * 具体信息（触发方式、促销方式）(JSON)
     * meta的格式介绍在VO部分，但数据层只需把它当成字符串就行了，不需进行特殊处理
     */
    private String metaData;

}
