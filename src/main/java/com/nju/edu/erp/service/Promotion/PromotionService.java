package com.nju.edu.erp.service.Promotion;

import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

public interface PromotionService {

    void createPromotion(PromotionVO promotionVO);

    List<PromotionVO> getPromotion();

    void deleteById(Integer id);

    void promote(UserVO userVO, SaleSheetVO saleSheetVO);
}
