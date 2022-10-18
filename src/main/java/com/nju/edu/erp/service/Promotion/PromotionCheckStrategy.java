package com.nju.edu.erp.service.Promotion;

import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;

import java.util.Date;

public interface PromotionCheckStrategy {
    /**
     * 判断销售是否符合促销条件
     * @param saleSheetVO 销售内容
     * @param promotionVO 促销策略
     * @return 是否符合促销条件
     */
    boolean checkPromotion(SaleSheetVO saleSheetVO, PromotionVO promotionVO);

    /**
     * 判读是否在当前促销时间内
     * @param promotionVO 促销策略
     * @param now 当前时间
     * @return true if beginTime<=now<beginTime+interval
     */
    default boolean isInTime(PromotionVO promotionVO,Date now){
        if (promotionVO.getBeginTime().compareTo(now)>0) {
            return false;
        }
        long end = promotionVO.getBeginTime().getTime() + promotionVO.getInterval();
        Date endTime=new Date(end);
        return endTime.compareTo(now)>0;
    }
}
