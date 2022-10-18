package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerLevelStrategyTest {
    @Autowired
    CustomerLevelStrategy customerLevelStrategy;
    @Test
    void checkPromotion() {
        PromotionVO promotionVO=PromotionVO.builder().triggerType(PromotionTriggerType.CUSTOMER_LEVEL)
                .beginTime(new Date(new Date().getTime()-10000000)).interval(1000000000L).metaData("{\"check\":1}").build();
        SaleSheetVO saleSheetVO=SaleSheetVO.builder().supplier(1).build();
        assertTrue(customerLevelStrategy.checkPromotion(saleSheetVO,promotionVO));
        promotionVO.setMetaData("{\"check\":2}");
        assertFalse(customerLevelStrategy.checkPromotion(saleSheetVO,promotionVO));

        promotionVO.setTriggerType(PromotionTriggerType.PRICE);
        try{
            customerLevelStrategy.checkPromotion(saleSheetVO,promotionVO);
            fail();
        }catch (RuntimeException ignored){

        }
    }
}