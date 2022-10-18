package com.nju.edu.erp.service.Impl.Promotion.Execute;

import com.nju.edu.erp.enums.promotion.PromotionType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionRecordVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehousePresentSheetVO;
import com.nju.edu.erp.service.Promotion.PromotionExecuteStrategy;
import com.nju.edu.erp.service.Sheet.WarehousePresentService;
import com.nju.edu.erp.utils.JSONReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class PresentStrategy implements PromotionExecuteStrategy {
    private final WarehousePresentService warehousePresentService;

    @Autowired
    public PresentStrategy(WarehousePresentService warehousePresentService) {
        this.warehousePresentService = warehousePresentService;
    }

    @Override
    public PromotionRecordVO execute(SaleSheetVO saleSheetVO, PromotionVO promotionVO, UserVO userVO) {
        if(promotionVO.getPromotionType()!= PromotionType.PRESENT){
            throw new RuntimeException("促销方式错误");
        }
        WarehousePresentSheetVO presentSheetVO=new WarehousePresentSheetVO();
        HashMap<String,String> presentConfig= (HashMap<String, String>) JSONReader.getByProperty(promotionVO.getMetaData(), "exec");
        presentSheetVO.setPid(presentConfig.get("pid"));
        presentSheetVO.setQuantity(Integer.parseInt(presentConfig.get("num")));
        warehousePresentService.makeSheet(userVO,presentSheetVO);

        return PromotionRecordVO.builder().promotionId(promotionVO.getId()).time(new Date()).customerId(saleSheetVO.getSupplier()).build();

    }
}
