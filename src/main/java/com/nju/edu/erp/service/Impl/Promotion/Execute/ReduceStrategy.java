package com.nju.edu.erp.service.Impl.Promotion.Execute;

import com.nju.edu.erp.enums.promotion.PromotionType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionRecordVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Promotion.PromotionExecuteStrategy;
import com.nju.edu.erp.utils.JSONReader;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ReduceStrategy implements PromotionExecuteStrategy {
    @Override
    public PromotionRecordVO execute(SaleSheetVO saleSheetVO, PromotionVO promotionVO, UserVO userVO) {
        if(promotionVO.getPromotionType()!= PromotionType.REDUCE){
            throw new RuntimeException("促销方式错误");
        }

        PromotionRecordVO recordVO=PromotionRecordVO.builder().promotionId(promotionVO.getId()).time(new Date()).customerId(saleSheetVO.getSupplier()).build();
        Integer reduce =(Integer) JSONReader.getByProperty(promotionVO.getMetaData(), "exec");
        BigDecimal newVoucher = new BigDecimal(reduce);
        BigDecimal finalAmount = saleSheetVO.getFinalAmount();
        BigDecimal reduceAmount = saleSheetVO.getVoucherAmount();
        if(finalAmount.compareTo(newVoucher)<=0){
            reduceAmount=reduceAmount.add(finalAmount);
            finalAmount=BigDecimal.ZERO;//最低折让到0
        }else {
            reduceAmount = reduceAmount.add(new BigDecimal(reduce));  //降价在折扣比例计算之后
            finalAmount = finalAmount.subtract(newVoucher);
        }
        saleSheetVO.setFinalAmount(finalAmount);
        saleSheetVO.setVoucherAmount(reduceAmount);
        return recordVO;
    }
}
