package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.service.Sheet.PaymentService;
import com.nju.edu.erp.service.Sheet.ReceiveService;
import com.nju.edu.erp.service.Sheet.SalaryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/approval")
public class ApprovalController {

    private final SalaryService salaryService;
    private final PaymentService paymentService;
    private final ReceiveService receiveService;

    @Autowired
    public ApprovalController(SalaryService salaryService,
                              PaymentService paymentService, ReceiveService receiveService){
        this.salaryService = salaryService;
        this.paymentService = paymentService;
        this.receiveService = receiveService;
    }

    /**
     * 审批工资单
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/salary-approval")
    public Response salaryApproval(@RequestParam("salarySheetId") String salarySheetId,
                                   @RequestParam("state") FinancialSheetState state){
        salaryService.approval(salarySheetId, state);
        return Response.buildSuccess();
    }

    /**
     * 付款单审批
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/pay-approval")
    public Response paymentApproval(@RequestParam("sheetId") String sheetId,
                                    @RequestParam("state") FinancialSheetState state){
        paymentService.approval(sheetId,state);
        return Response.buildSuccess();
    }

    /**
     * 收款单审批
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/receive-approval")
    public Response receiveApproval(@RequestParam("sheetId") String sheetId,
                                    @RequestParam("state") FinancialSheetState state){
        receiveService.approval(sheetId,state);
        return Response.buildSuccess();
    }

}
