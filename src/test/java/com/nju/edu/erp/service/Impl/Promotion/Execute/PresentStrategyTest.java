package com.nju.edu.erp.service.Impl.Promotion.Execute;

import com.nju.edu.erp.dao.WarehousePresentDao;
import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.enums.promotion.PromotionType;
import com.nju.edu.erp.enums.sheetState.WarehousePresentSheetState;
import com.nju.edu.erp.model.po.WarehousePresentSheetPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Rollback(value = true)
class PresentStrategyTest {
    @Autowired
    PresentStrategy presentStrategy;
    @Autowired
    WarehousePresentDao warehousePresentDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void execute() {

        PromotionVO promotionVO = PromotionVO.builder()
                .beginTime(new Date(new Date().getTime()-100000))
                .triggerType(PromotionTriggerType.PRICE)
                .promotionType(PromotionType.PRESENT)
                .interval(1000000L)
                .metaData("{\"exec\":{\n" +
                        "\"pid\":\"3\",\n" +
                        "\"num\":\"4\"\n" +
                        "}}").build();
        assertNotNull(presentStrategy.execute(new SaleSheetVO(), promotionVO, UserVO.builder().name("123").build()));
        WarehousePresentSheetPO latest = warehousePresentDao.getLatest();
        assertEquals("3",latest.getPid());
        assertEquals(4,latest.getQuantity());
        assertEquals(WarehousePresentSheetState.PENDING,latest.getState());
    }
}