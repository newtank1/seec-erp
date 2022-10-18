package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @Authorized(roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @PostMapping("/addCustomer")
    public Response addCustomer(UserVO userVO,@RequestBody CustomerVO customerVO){
        //todo
        return Response.buildSuccess(customerService.createCustomer(customerVO));
    }

    @Authorized (roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @PostMapping("/editCustomer")
    public Response editCustomer(UserVO userVO,@RequestBody CustomerVO customerVO){
        //todo
        return Response.buildSuccess(customerService.updateCustomer(customerVO));
    }

    @Authorized (roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @GetMapping("/deleteCustomer")
    public Response deleteCustomer(UserVO userVO,@RequestParam("id") String id){
        //todo
        return Response.buildSuccess(customerService.deleteById(Integer.parseInt(id)));
    }

}
