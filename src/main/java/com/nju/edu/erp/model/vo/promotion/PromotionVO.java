package com.nju.edu.erp.model.vo.promotion;

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
public class PromotionVO{
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
     * 关于json的格式：
     * 分为判断数据和执行数据。即为{
     *     "check":......,
     *     "exec":......
     * }
     *在check中，如果是客户等级促销,则check为一个数字，表示针对的客户等级；
     * 如果为商品组合促销，则check为一个数组，表示组合的商品id，每个元素为一个字符串；
     * 如果是总价促销，则check为一个数字，表示促销需达到的总价
     *
     * 在exec中，如果是赠品，则exec也为json对象，包含pid和num两个属性，分别对应赠品id和赠品数量，类型均为String
     * 如果是代金券，则exec为一个数字，表示代金券金额（代金券发放后即刻使用，不保存）
     * 如果是组合商品降价，则exec为一个数字，表示降价金额（组合降价在一个促销策略中只触发一次）
     *
     * 例如以下的metadata:
     * {
     *     "check":[”1",“2”],
     *     "exec":{
     *         "pid":"3“,
     *         "num":"4"
     *     }
     * }
     * 表示同时购买商品1和2时，赠送4个商品3.
     */
    private String metaData;

}
