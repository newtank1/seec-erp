package com.nju.edu.erp.service.Promotion;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.enums.promotion.PromotionType;

public interface PromotionStrategyDispatcher {
    /**
     * 根据触发方式分派确定策略
     * @param type 触发方式
     * @return 对应的确认策略
     */
    PromotionCheckStrategy dispatchCheckStrategy(PromotionTriggerType type);

    /**
     * 根据促销方式分派执行策略
     * @param type 促销执行方式
     * @return 对应的促销策略
     */
    PromotionExecuteStrategy dispatchExecStrategy(PromotionType type);
}
