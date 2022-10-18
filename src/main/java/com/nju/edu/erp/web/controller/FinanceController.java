package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.PaymentSheetVO;
import com.nju.edu.erp.model.vo.ReceiveSheetVO;
import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.startBill.StartBillVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Financial.FinancialService;
import com.nju.edu.erp.service.Sheet.PaymentService;
import com.nju.edu.erp.service.Sheet.ReceiveService;
import com.nju.edu.erp.service.StartBillService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/finance")
public class FinanceController {

    private final FinancialService financialService;
    private final PaymentService paymentService;
    private final ReceiveService receiveService;
    private final StartBillService startBillService;


    @Autowired
    public FinanceController(FinancialService financialService, StartBillService startBillService,
                             PaymentService paymentService, ReceiveService receiveService){
        this.paymentService = paymentService;
        this.financialService = financialService;
        this.receiveService = receiveService;
        this.startBillService = startBillService;
    }

    /**
     * 查看销售明细表
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @PostMapping(value = "/saleDetail")
    public Response getSaleDetailTable(@RequestBody SaleRecordFilterVO vo){
        return Response.buildSuccess(financialService.getSaleDetail(vo));
    }

    /**
     * 查看经营历程表
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @PostMapping(value = "/runProcess")
    public Response getSaleRecordTable(@RequestBody SheetFilterVO vo){
        return Response.buildSuccess(financialService.getSheetRecord(vo));
    }

    /**
     * 查看经营状况表
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @GetMapping(value = "/runCondition")
    public Response getRunConditionTable(@RequestParam(value = "beginDate") String beginDate,
                                         @RequestParam(value = "endDate") String endDate){
        return Response.buildSuccess(financialService.getBalance(beginDate, endDate));
    }

    /**
     * 制定付款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/paymentSheet-make")
    public Response createPaymentSheet(UserVO userVO, @RequestBody PaymentSheetVO paymentSheetVO){
        paymentService.makeSheet(userVO, paymentSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 制定收款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/receiveSheet-make")
    public Response createReceiveSheet(UserVO userVO, @RequestBody ReceiveSheetVO receiveSheetVO){
        receiveService.makeSheet(userVO, receiveSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 查看期初建帐信息
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN, Role.GM})
    @GetMapping(value = "/start-information")
    public Response getStartBillInformation(){
        return Response.buildSuccess(startBillService.getBills());
    }

    /**
     * 期初建帐
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN, Role.GM})
    @PostMapping(value = "/start-bill-create")
    public Response createStartBill(UserVO userVO, @RequestBody StartBillVO startBillVO){
        startBillService.createStartBill(userVO,startBillVO);
        return Response.buildSuccess();
    }

    /**
     * 获取全部收款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN, Role.GM})
    @GetMapping(value = "/receiveSheets")
    public Response getAllReceive(){
        return Response.buildSuccess(receiveService.getSheetByState(null));
    }


    /**
     * 获取全部付款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN, Role.GM})
    @GetMapping(value = "/paymentSheets")
    public Response getPaymentSheets(){
        return Response.buildSuccess(paymentService.getSheetByState(null));
    }

}
