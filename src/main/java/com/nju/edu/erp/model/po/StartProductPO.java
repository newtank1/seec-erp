package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartProductPO{
    /**
     * 商品id
     */
    private String id;

    /**
     * 期初账单id
     */
    private Integer billId;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品分类
     */
    private Integer categoryId;

    /**
     * 型号
     */
    private String type;


    /**
     * 价格
     */
    private BigDecimal price;

}
