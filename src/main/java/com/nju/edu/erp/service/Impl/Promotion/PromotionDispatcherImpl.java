package com.nju.edu.erp.service.Impl.Promotion;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.enums.promotion.PromotionType;
import com.nju.edu.erp.service.Impl.Promotion.Check.CustomerLevelStrategy;
import com.nju.edu.erp.service.Impl.Promotion.Check.ProductCheckStrategy;
import com.nju.edu.erp.service.Impl.Promotion.Check.TotalPriceStrategy;
import com.nju.edu.erp.service.Promotion.PromotionCheckStrategy;
import com.nju.edu.erp.service.Promotion.PromotionExecuteStrategy;
import com.nju.edu.erp.service.Promotion.PromotionStrategyDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromotionDispatcherImpl implements PromotionStrategyDispatcher {
    CustomerLevelStrategy customerLevelStrategy;
    ProductCheckStrategy productCheckStrategy;
    TotalPriceStrategy totalPriceStrategy;

    @Autowired
    public PromotionDispatcherImpl(CustomerLevelStrategy customerLevelStrategy, ProductCheckStrategy productCheckStrategy, TotalPriceStrategy totalPriceStrategy) {
        this.customerLevelStrategy = customerLevelStrategy;
        this.productCheckStrategy = productCheckStrategy;
        this.totalPriceStrategy = totalPriceStrategy;
    }

    @Override
    public PromotionCheckStrategy dispatchCheckStrategy(PromotionTriggerType type) {
        switch (type){
            case PRICE:return totalPriceStrategy;
            case PRODUCT:return productCheckStrategy;
            case CUSTOMER_LEVEL:return customerLevelStrategy;
        }
        throw new RuntimeException("需传入判断类型");
    }

    @Override
    public PromotionExecuteStrategy dispatchExecStrategy(PromotionType type) {
        return null;
    }
}
