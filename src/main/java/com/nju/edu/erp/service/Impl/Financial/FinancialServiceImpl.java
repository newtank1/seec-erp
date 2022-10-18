package com.nju.edu.erp.service.Impl.Financial;

import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehousePresentSheetVO;
import com.nju.edu.erp.service.Financial.FinancialService;
import com.nju.edu.erp.service.Sheet.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinancialServiceImpl implements FinancialService {
    private final SheetServiceFactory sheetServiceFactory;
    private final SaleService saleService;
    private final SaleReturnsService saleReturnsService;
    private final PurchaseService purchaseService;
    private final PurchaseReturnsService purchaseReturnsService;
    private final SalaryService salaryService;
    private final WarehousePresentService warehousePresentService;

    @Autowired
    public FinancialServiceImpl(SheetServiceFactory sheetServiceFactory, SaleService saleService, SaleReturnsService saleReturnsService, PurchaseService purchaseService, PurchaseReturnsService purchaseReturnsService, SalaryService salaryService, WarehousePresentService warehousePresentService) {
        this.sheetServiceFactory = sheetServiceFactory;
        this.saleService = saleService;
        this.saleReturnsService = saleReturnsService;
        this.purchaseService = purchaseService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.salaryService = salaryService;
        this.warehousePresentService = warehousePresentService;
    }


    @Override
    public List<SaleRecordVO> getSaleDetail(SaleRecordFilterVO filterVO) {
        if(filterVO.getProductName()!=null&&filterVO.getProductName().isEmpty()){
            filterVO.setProductName(null);
        }
        List<SaleRecordVO> saleRecords = saleService.findRecords(filterVO);
        List<SaleRecordVO> saleReturnRecords = saleReturnsService.findRecords(filterVO);
        List<SaleRecordVO> ret=new ArrayList<>();
        ret.addAll(saleRecords);
        ret.addAll(saleReturnRecords);
        return ret;
    }

    @Override
    public List getSheetRecord(SheetFilterVO filterVO) {
        SheetService sheetService = sheetServiceFactory.getSheetServiceByType(filterVO.getType());
        if (sheetService != null) {
            return sheetService.findByFilter(filterVO);
        }
        return new ArrayList();
    }

    @SneakyThrows
    @Override
    public FinancialStatusVO getBalance(String beginDateStr, String endDateStr) {
        //todo
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin=dateFormat.parse(beginDateStr);
        Date end=dateFormat.parse(endDateStr);

        SheetFilterVO sheetFilterVO=SheetFilterVO.builder().beginDate(begin).endDate(end).build();
        if(end.compareTo(begin)<0){
            throw new MyServiceException("F0001","日期次序错误");
        }

        FinancialStatusVO financialStatusVO=new FinancialStatusVO();

        BigDecimal rawSaleIncome=BigDecimal.ZERO;
        BigDecimal saleDiscount=BigDecimal.ZERO;
        List<SaleSheetVO> saleSheetVOS = saleService.findByFilter(sheetFilterVO);
        for (SaleSheetVO saleSheetVO : saleSheetVOS) {
            if(saleSheetVO.getState()== SaleSheetState.SUCCESS) {
                rawSaleIncome = rawSaleIncome.add(saleSheetVO.getRawTotalAmount());
                saleDiscount = saleDiscount.add(saleSheetVO.getRawTotalAmount()).subtract(saleSheetVO.getFinalAmount());
            }
        }
        List<PurchaseReturnsSheetVO> purchaseReturnsSheetVOS = purchaseReturnsService.findByFilter(sheetFilterVO);
        for (PurchaseReturnsSheetVO purchaseReturnsSheetVO : purchaseReturnsSheetVOS) {
            if(purchaseReturnsSheetVO.getState()== PurchaseReturnsSheetState.SUCCESS)
                rawSaleIncome=rawSaleIncome.add(purchaseReturnsSheetVO.getTotalAmount());
        }
        financialStatusVO.setSalesIncome(rawSaleIncome);//销售收入=所有销售的原始收入+所有退货的收入
        financialStatusVO.setSalesIncomeDiscount(saleDiscount);//销售折扣=所有销售中的原始收入-最终收入

        BigDecimal productIncome=BigDecimal.ZERO;
        financialStatusVO.setProductIncome(productIncome);

        BigDecimal saleExpense=BigDecimal.ZERO;
        List<PurchaseSheetVO> purchaseSheetVOS = purchaseService.findByFilter(sheetFilterVO);
        for (PurchaseSheetVO purchaseSheetVO : purchaseSheetVOS) {
            if(purchaseSheetVO.getState()== PurchaseSheetState.SUCCESS)
                saleExpense=saleExpense.add(purchaseSheetVO.getTotalAmount());
        }
        List<SaleReturnsSheetVO> saleReturnsSheetVOS = saleReturnsService.findByFilter(sheetFilterVO);
        for (SaleReturnsSheetVO saleReturnsSheetVO : saleReturnsSheetVOS) {
            if(saleReturnsSheetVO.getState()== SaleReturnsSheetState.SUCCESS)
                saleExpense=saleExpense.add(saleReturnsSheetVO.getFinalAmount());
        }
        financialStatusVO.setSalesExpense(saleExpense);//销售成本=所有进货支出+销售退还支出

        BigDecimal productExpense=BigDecimal.ZERO;
        List<WarehousePresentSheetVO> warehousePresentSheetVOS = warehousePresentService.findByFilter(sheetFilterVO);
        for (WarehousePresentSheetVO warehousePresentSheetVO : warehousePresentSheetVOS) {
            if(warehousePresentSheetVO.getState()== WarehousePresentSheetState.SUCCESS)
                productExpense=productExpense.add(warehousePresentSheetVO.getExpenses());
        }
        financialStatusVO.setProductExpense(productExpense);//商品类支出=赠送支出

        BigDecimal salaryExpense=BigDecimal.ZERO;
        List<SalarySheetVO> salarySheetVOS = salaryService.findByFilter(sheetFilterVO);
        for (SalarySheetVO salarySheetVO : salarySheetVOS) {
            if(salarySheetVO.getState()==FinancialSheetState.SUCCESS)
                salaryExpense=salaryExpense.add(salarySheetVO.getRawSalary());
        }
        financialStatusVO.setEmployeeExpense(salaryExpense);//人力成本=所有原始工资（因为要带税）

        BigDecimal profit=rawSaleIncome.subtract(saleDiscount).add(productIncome).subtract(saleExpense).subtract(productExpense).subtract(salaryExpense);
        financialStatusVO.setProfit(profit);
        return financialStatusVO;
    }
}
