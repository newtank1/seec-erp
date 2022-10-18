package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.config.JwtConfig;
import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.UserService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserDao userDao;

    private JwtConfig jwtConfig;

    private UserService userService;

    @Autowired
    public UserController(UserDao userDao, JwtConfig jwtConfig, UserService userService) {
        this.userDao = userDao;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Response userLogin(@RequestBody UserVO userVO) {
        return Response.buildSuccess(userService.login(userVO));
    }

    @PostMapping("/register")
    public Response userRegister(@RequestBody UserVO userVO) {
        userService.register(userVO);
        return Response.buildSuccess();
    }

    @GetMapping("/auth")
    public Response userAuth(@RequestParam(name = "token") String token) {
        return Response.buildSuccess(userService.auth(token));
    }

    /**
     * 查看所有员工
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.HR, Role.ADMIN})
    @GetMapping(value = "/getAll")
    public Response getAllEmployee(){
        return Response.buildSuccess(userService.getAll());
    }

    /**
     * 打卡
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.HR, Role.SALE_STAFF,
            Role.INVENTORY_MANAGER, Role.SALE_MANAGER, Role.ADMIN})
    @PostMapping(value = "/punchCard")
    public Response punchCard(UserVO userVO){
        userService.clockIn(userVO);
        return Response.buildSuccess();
    }

    /**
     * 员工入职登记信息
     */
    @Authorized(roles = {Role.HR, Role.ADMIN})
    @PostMapping(value = "/info-register")
    public Response registerInfo(@RequestBody UserInfoVO userInfovo){
        userService.createInfo(userInfovo);
        return Response.buildSuccess();
    }

    /**
     * 获取全部员工信息
     */
    @Authorized(roles = {Role.GM, Role.HR, Role.ADMIN})
    @GetMapping(value = "/get-all-info")
    public Response getAllInfo(){
        return Response.buildSuccess(userService.getAllInfo());
    }

    /**
     * 根据姓名获取员工信息
     */
    @Authorized(roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/get-info")
    public Response getInfo(@RequestParam("username") String username){
        return Response.buildSuccess(userService.getUserInfo(username));
    }

}
