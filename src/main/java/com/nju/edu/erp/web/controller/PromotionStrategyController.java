package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.Promotion.PromotionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/promotion")
public class PromotionStrategyController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionStrategyController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    /**
     * 促销策略制定
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @PostMapping(value = "/strategy-make")
    public Response makeSaleStrategy(@RequestBody PromotionVO promotionVO){
        promotionService.createPromotion(promotionVO);
        return Response.buildSuccess();
    }

    /**
     * 根据id删除促销策略
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/strategy-delete")
    public Response deleteSaleStrategy(@RequestParam("id") String id){
        promotionService.deleteById(Integer.parseInt(id));
        return Response.buildSuccess();
    }

    /**
     * 获取所以销售策略
     * @param
     * @return
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/strategy-all")
    public Response getAllSaleStrategy(){
        return Response.buildSuccess(promotionService.getPromotion());
    }
}
