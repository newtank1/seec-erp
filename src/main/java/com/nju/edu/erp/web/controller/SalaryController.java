package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Sheet.SalaryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {

    private final SalaryService salaryService;

    @Autowired
    public SalaryController(SalaryService salaryService){
        this.salaryService = salaryService;
    }

    /**
     * 制定工资单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/salary-sheet-make")
    public Response createSalarySheet(UserVO userVO, @RequestBody SalarySheetVO salarySheetVO){
        salaryService.makeSheet(userVO, salarySheetVO);
        return Response.buildSuccess();
    }

    /**
     * 查看所有员工工资单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.HR, Role.ADMIN})
    @GetMapping(value = "/getAllSalary")
    public Response getAllSalary(){
        return Response.buildSuccess(salaryService.getAll());
    }

    /**
     * 根据员工姓名查看员工工资单
     */
    @Authorized(roles = {Role.GM, Role.HR, Role.ADMIN})
    @GetMapping(value = "/get-salary")
    public Response getSalarySheetsByName(@RequestBody SheetFilterVO vo){
        return Response.buildSuccess(salaryService.findByFilter(vo));
    }

    /**
     * 查看制定状态工资单
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/get-salary-sheet-by-sate")
    public Response getSalarySheetsByState(@RequestParam("state") String state){
        if( state.equals("审批成功"))
            return Response.buildSuccess(salaryService.getSheetByState(FinancialSheetState.SUCCESS));
        if( state.equals("待审批"))
            return Response.buildSuccess(salaryService.getSheetByState(FinancialSheetState.PENDING));
        if ( state.equals("审批失败"))
            return Response.buildSuccess(salaryService.getSheetByState(FinancialSheetState.FAILURE));
        return Response.buildFailed("000000","不存在该状态");
    }



    /**
     * 制定年终奖
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @PostMapping(value = "/bonus-make")
    public Response makeBonusSheet(UserVO userVO, @RequestBody SalarySheetVO salarySheetVO){
        System.out.println(salarySheetVO.getId()+salarySheetVO.getRawSalary());
        salaryService.makeBonus(salarySheetVO.getId(), salarySheetVO.getRawSalary());
        return Response.buildSuccess();
    }

    /**
     * 制定薪酬规则
     */
    @Authorized(roles = {Role.HR, Role.ADMIN})
    @PostMapping(value = "/rule-make")
    public Response makeSalaryRule(){
        return Response.buildSuccess();
    }

}
