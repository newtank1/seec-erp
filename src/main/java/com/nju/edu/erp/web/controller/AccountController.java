package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    /**
     * 查看所有银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping( value = "/show" )
    public Response showAllAccount(){
        return Response.buildSuccess(accountService.findAll());
    }

    /**
     * 根据id删除银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping( value = "/delete" )
    public Response deleteAccount(@RequestParam("id") String id){
        if(accountService.findById(Integer.parseInt(id))==null)
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        accountService.deleteByID(Integer.parseInt(id));
        return Response.buildSuccess();
    }

    /**
     * 根据id查找银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping( value = "/findById" )
    public Response findAccountById(@RequestParam("id") String id){
        return Response.buildSuccess(accountService.findById(Integer.parseInt(id)));
    }

    /**
     * 根据keyword查找银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping( value = "/findByKeyword" )
    public Response findAccountByKeyword(@RequestParam("keyword") String keyword){
        return Response.buildSuccess(accountService.search(keyword));
    }

    /**
     * 创建银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping("/make")
    public Response createAccount(@RequestBody BankAccountVO bankAccountVO){
        return Response.buildSuccess(accountService.create(bankAccountVO));
    }

    /**
     * 更改银行账户
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping("/update")
    public Response editAccount(@RequestBody BankAccountVO bankAccountVO){
        accountService.update(bankAccountVO);
        return Response.buildSuccess();
    }

}
