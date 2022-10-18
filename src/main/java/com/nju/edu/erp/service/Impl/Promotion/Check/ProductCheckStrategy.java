package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.Promotion.PromotionCheckStrategy;
import com.nju.edu.erp.utils.JSONReader;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductCheckStrategy implements PromotionCheckStrategy {
    @Override
    public boolean checkPromotion(SaleSheetVO saleSheetVO, PromotionVO promotionVO) {
        if(!isInTime(promotionVO,new Date())) return false;
        if(promotionVO.getTriggerType()!= PromotionTriggerType.PRODUCT){
            throw new RuntimeException("判断类型错误");
        }
        List<String> checkList= (List<String>) JSONReader.getByProperty(promotionVO.getMetaData(), "check");
        List<String> productIds = saleSheetVO.getSaleSheetContent().stream().map(SaleSheetContentVO::getPid).collect(Collectors.toList());
        return new HashSet<>(productIds).containsAll(checkList);
    }
}
