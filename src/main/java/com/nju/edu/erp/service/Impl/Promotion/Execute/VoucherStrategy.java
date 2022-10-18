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
public class VoucherStrategy implements PromotionExecuteStrategy {
    @Override
    public PromotionRecordVO execute(SaleSheetVO saleSheetVO, PromotionVO promotionVO, UserVO userVO) {
        if(promotionVO.getPromotionType()!= PromotionType.VOUCHER){
            throw new RuntimeException("促销方式错误");
        }

        PromotionRecordVO recordVO=PromotionRecordVO.builder().promotionId(promotionVO.getId()).time(new Date()).customerId(saleSheetVO.getSupplier()).build();
        Integer voucher =(Integer) JSONReader.getByProperty(promotionVO.getMetaData(), "exec");
        BigDecimal newVoucher = new BigDecimal(voucher);
        BigDecimal finalAmount = saleSheetVO.getFinalAmount();
        BigDecimal voucherAmount = saleSheetVO.getVoucherAmount();
        if(finalAmount.compareTo(newVoucher)<=0){
            voucherAmount=voucherAmount.add(finalAmount);
            finalAmount=BigDecimal.ZERO;//最低折让到0
        }else {
            voucherAmount = voucherAmount.add(new BigDecimal(voucher));  //代金券折让在折扣比例计算之后
            finalAmount = finalAmount.subtract(newVoucher);
        }
        saleSheetVO.setFinalAmount(finalAmount);
        saleSheetVO.setVoucherAmount(voucherAmount);
        return recordVO;
    }
}
