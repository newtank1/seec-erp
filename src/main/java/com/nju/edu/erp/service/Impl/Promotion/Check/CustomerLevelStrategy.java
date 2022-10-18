package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.Promotion.PromotionCheckStrategy;
import com.nju.edu.erp.utils.JSONReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomerLevelStrategy implements PromotionCheckStrategy {
    private final CustomerService customerService;

    @Autowired
    public CustomerLevelStrategy(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public boolean checkPromotion(SaleSheetVO saleSheetVO, PromotionVO promotionVO) {
        if(!isInTime(promotionVO,new Date())) return false;
        if(promotionVO.getTriggerType()!= PromotionTriggerType.CUSTOMER_LEVEL){
            throw new RuntimeException("判断类型错误");
        }
        Integer check = (Integer)JSONReader.getByProperty(promotionVO.getMetaData(), "check");
        Integer customerId = saleSheetVO.getSupplier();

        CustomerPO customer = customerService.findCustomerById(customerId);
        return customer.getLevel().equals(check);
    }

}
