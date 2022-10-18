package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCheckStrategyTest {
    @Autowired
    ProductCheckStrategy productCheckStrategy;

    @Test
    void checkPromotion() {
        PromotionVO promotionVO=PromotionVO.builder().triggerType(PromotionTriggerType.PRODUCT)
                .beginTime(new Date(new Date().getTime()-10000000)).interval(1000000000L).metaData("{\"check\":[\"1\",\"2\"]}").build();
        SaleSheetContentVO contentVO1=SaleSheetContentVO.builder().pid("1").build();
        SaleSheetContentVO contentVO2=SaleSheetContentVO.builder().pid("2").build();
        SaleSheetContentVO contentVO3=SaleSheetContentVO.builder().pid("3").build();
        SaleSheetVO saleSheetVO=SaleSheetVO.builder().saleSheetContent(new ArrayList<>()).build();
        saleSheetVO.getSaleSheetContent().add(contentVO1);
        saleSheetVO.getSaleSheetContent().add(contentVO2);
        assertTrue(productCheckStrategy.checkPromotion(saleSheetVO,promotionVO));
        saleSheetVO.getSaleSheetContent().add(contentVO3);
        assertTrue(productCheckStrategy.checkPromotion(saleSheetVO,promotionVO));
        saleSheetVO.getSaleSheetContent().remove(contentVO1);
        assertFalse(productCheckStrategy.checkPromotion(saleSheetVO,promotionVO));

        promotionVO.setTriggerType(PromotionTriggerType.PRICE);
        try{
            productCheckStrategy.checkPromotion(saleSheetVO,promotionVO);
            fail();
        }catch (RuntimeException ignored){

        }
    }
}