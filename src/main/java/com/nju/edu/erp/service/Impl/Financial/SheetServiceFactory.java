package com.nju.edu.erp.service.Impl.Financial;

import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.service.Sheet.*;
import com.nju.edu.erp.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SheetServiceFactory {
    private final PaymentService paymentService;
    private final PurchaseReturnsService purchaseReturnsService;
    private final PurchaseService purchaseService;
    private final ReceiveService receiveService;
    private final SalaryService salaryService;
    private final SaleReturnsService saleReturnsService;
    private final SaleService saleService;
    private final WarehousePresentService warehousePresentService;
    private final WarehouseInputService warehouseInputService;
    private final WarehouseOutputService warehouseOutputService;
    @Autowired
    public SheetServiceFactory(PaymentService paymentService, PurchaseReturnsService purchaseReturnsService, PurchaseService purchaseService, ReceiveService receiveService, SalaryService salaryService, SaleReturnsService saleReturnsService, SaleService saleService, WarehousePresentService warehousePresentService, WarehouseService warehouseService, WarehouseInputService warehouseInputService, WarehouseOutputService warehouseOutputService) {
        this.paymentService = paymentService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.purchaseService = purchaseService;
        this.receiveService = receiveService;
        this.salaryService = salaryService;
        this.saleReturnsService = saleReturnsService;
        this.saleService = saleService;
        this.warehousePresentService = warehousePresentService;
        this.warehouseInputService = warehouseInputService;
        this.warehouseOutputService = warehouseOutputService;
    }
    public SheetService getSheetServiceByType(SheetType type){
        switch (type){
            case SALE:return saleService;
            case SALE_RETURN:return saleReturnsService;
            case PURCHASE:return purchaseService;
            case PURCHASE_RETURN:return purchaseReturnsService;
            case RECEIVE:return receiveService;
            case PAYMENT:return paymentService;
            case SALARY:return salaryService;
            case WAREHOUSE_PRESENT:return warehousePresentService;
            case WAREHOUSE_INPUT:return warehouseInputService;
            case WAREHOUSE_OUTPUT:return warehouseOutputService;
        }
        return null;
    }
}
