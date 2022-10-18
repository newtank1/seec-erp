package com.nju.edu.erp.service.Promotion;

import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionCheckStrategyTest {

    @Test
    void isInTime() {
        PromotionCheckStrategy strategy= new DummyCheckStrategy();
        PromotionVO promotionVO=PromotionVO.builder().beginTime(new Date(100000)).interval(10000L).build();
        Date now=new Date(100000);
        assertTrue(strategy.isInTime(promotionVO,now));
        now=new Date(105000);
        assertTrue(strategy.isInTime(promotionVO,now));
        now=new Date(110000);
        assertFalse(strategy.isInTime(promotionVO,now));
        now=new Date(10000);
        assertFalse(strategy.isInTime(promotionVO,now));
        now=new Date(120000);
        assertFalse(strategy.isInTime(promotionVO,now));
    }

    private static class DummyCheckStrategy implements PromotionCheckStrategy{
        @Override
        public boolean checkPromotion(SaleSheetVO saleSheetVO, PromotionVO promotionVO) {
            return false;
        }
    }
}