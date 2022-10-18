package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TotalPriceStrategyTest {
    @Autowired
    TotalPriceStrategy totalPriceStrategy;
    @Test
    void checkPromotion() {
        PromotionVO promotionVO=PromotionVO.builder().triggerType(PromotionTriggerType.PRICE)
                .beginTime(new Date(new Date().getTime()-10000000)).interval(1000000000L).metaData("{\"check\":5000}").build();
        SaleSheetVO saleSheetVO=SaleSheetVO.builder().rawTotalAmount(new BigDecimal(10000)).build();
        assertTrue(totalPriceStrategy.checkPromotion(saleSheetVO,promotionVO));
        promotionVO.setMetaData("{\"check\":15000}");
        assertFalse(totalPriceStrategy.checkPromotion(saleSheetVO,promotionVO));

        promotionVO.setTriggerType(PromotionTriggerType.PRODUCT);
        try{
            totalPriceStrategy.checkPromotion(saleSheetVO,promotionVO);
            fail();
        }catch (RuntimeException ignored){

        }
    }
}