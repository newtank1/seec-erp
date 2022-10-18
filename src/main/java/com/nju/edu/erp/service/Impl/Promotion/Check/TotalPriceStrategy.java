package com.nju.edu.erp.service.Impl.Promotion.Check;

import com.nju.edu.erp.enums.promotion.PromotionTriggerType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.Promotion.PromotionCheckStrategy;
import com.nju.edu.erp.utils.JSONReader;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class TotalPriceStrategy implements PromotionCheckStrategy {

    @Override
    public boolean checkPromotion(SaleSheetVO saleSheetVO, PromotionVO promotionVO) {
        if(!isInTime(promotionVO,new Date())) return false;
        if(promotionVO.getTriggerType()!= PromotionTriggerType.PRICE){
            throw new RuntimeException("判断类型错误");
        }
        Integer check = (Integer) JSONReader.getByProperty(promotionVO.getMetaData(), "check");
        if (check == null) {
            throw new RuntimeException("格式错误");
        }
        BigDecimal checkPrice=new BigDecimal(check);

        return saleSheetVO.getRawTotalAmount().compareTo(checkPrice)>=0;
    }
}
