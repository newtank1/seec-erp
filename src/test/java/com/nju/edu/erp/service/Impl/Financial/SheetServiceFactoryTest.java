package com.nju.edu.erp.service.Impl.Financial;

import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.service.Sheet.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SheetServiceFactoryTest {
    @Autowired
    SheetServiceFactory factory;

    @Test
    void getSheetServiceByType() {
        assertTrue(factory.getSheetServiceByType(SheetType.PAYMENT) instanceof PaymentService);
        assertTrue(factory.getSheetServiceByType(SheetType.RECEIVE) instanceof ReceiveService);
        assertTrue(factory.getSheetServiceByType(SheetType.SALE) instanceof SaleService);
        assertTrue(factory.getSheetServiceByType(SheetType.SALE_RETURN) instanceof SaleReturnsService);
        assertTrue(factory.getSheetServiceByType(SheetType.PURCHASE) instanceof PurchaseService);
        assertTrue(factory.getSheetServiceByType(SheetType.PURCHASE_RETURN) instanceof PurchaseReturnsService);
    }
}