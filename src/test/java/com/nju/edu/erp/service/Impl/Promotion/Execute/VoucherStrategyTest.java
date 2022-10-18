package com.nju.edu.erp.service.Impl.Promotion.Execute;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.enums.promotion.PromotionType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VoucherStrategyTest {
    @Autowired
    VoucherStrategy voucherStrategy;
    @Test
    @Transactional
    @Rollback(value = true)
    void execute() {
        UserVO user=new UserVO();
        PromotionVO promotionVO=PromotionVO.builder()
                .beginTime(new Date(new Date().getTime()-100000))
                .triggerType(PromotionTriggerType.PRICE)
                .promotionType(PromotionType.VOUCHER)
                .interval(1000000L)
                .metaData("{\"exec\":200}").build();
        SaleSheetVO saleSheetVO=SaleSheetVO.builder()
                .voucherAmount(BigDecimal.ZERO)
                .discount(BigDecimal.ONE)
                .rawTotalAmount(new BigDecimal(1000))
                .finalAmount(new BigDecimal(1000)).build();
        voucherStrategy.execute(saleSheetVO,promotionVO,user);
        assertEquals(new BigDecimal(800),saleSheetVO.getFinalAmount());
        assertEquals(new BigDecimal(200),saleSheetVO.getVoucherAmount());

        saleSheetVO=SaleSheetVO.builder()
                .voucherAmount(BigDecimal.ZERO)
                .discount(BigDecimal.ONE)
                .rawTotalAmount(new BigDecimal(100))
                .finalAmount(new BigDecimal(100)).build();
        voucherStrategy.execute(saleSheetVO,promotionVO,user);
        assertEquals(BigDecimal.ZERO,saleSheetVO.getFinalAmount());
        assertEquals(new BigDecimal(100),saleSheetVO.getVoucherAmount());

        saleSheetVO=SaleSheetVO.builder()
                .voucherAmount(new BigDecimal(200))
                .discount(BigDecimal.ONE)
                .rawTotalAmount(new BigDecimal(1000))
                .finalAmount(new BigDecimal(800)).build();
        voucherStrategy.execute(saleSheetVO,promotionVO,user);
        assertEquals(new BigDecimal(600),saleSheetVO.getFinalAmount());
        assertEquals(new BigDecimal(400),saleSheetVO.getVoucherAmount());

        saleSheetVO=SaleSheetVO.builder()
                .voucherAmount(new BigDecimal(200))
                .discount(BigDecimal.ONE)
                .rawTotalAmount(new BigDecimal(300))
                .finalAmount(new BigDecimal(100)).build();
        voucherStrategy.execute(saleSheetVO,promotionVO,user);
        assertEquals(new BigDecimal(0),saleSheetVO.getFinalAmount());
        assertEquals(new BigDecimal(300),saleSheetVO.getVoucherAmount());

        saleSheetVO=SaleSheetVO.builder()
                .voucherAmount(new BigDecimal(200))
                .discount(new BigDecimal("0.8"))
                .rawTotalAmount(new BigDecimal(1000))
                .finalAmount(new BigDecimal(600)).build();
        voucherStrategy.execute(saleSheetVO,promotionVO,user);
        assertEquals(new BigDecimal(400),saleSheetVO.getFinalAmount());
        assertEquals(new BigDecimal(400),saleSheetVO.getVoucherAmount());
    }
}