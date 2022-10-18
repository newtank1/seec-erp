package com.nju.edu.erp.service.Promotion;

import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionRecordVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;

public interface PromotionExecuteStrategy {

    /**
     * 执行促销内容，修改销售单中的支付信息
     *
     * @param saleSheetVO 销售内容
     * @param promotionVO 促销内容
     * @param userVO
     * @return 产生的促销记录
     */
    PromotionRecordVO execute(SaleSheetVO saleSheetVO, PromotionVO promotionVO, UserVO userVO);
}
