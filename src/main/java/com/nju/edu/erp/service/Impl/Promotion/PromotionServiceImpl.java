package com.nju.edu.erp.service.Impl.Promotion;

import com.nju.edu.erp.dao.PromotionDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.PromotionPO;
import com.nju.edu.erp.model.po.PromotionRecordPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.promotion.PromotionRecordVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Promotion.PromotionCheckStrategy;
import com.nju.edu.erp.service.Promotion.PromotionExecuteStrategy;
import com.nju.edu.erp.service.Promotion.PromotionService;
import com.nju.edu.erp.service.Promotion.PromotionStrategyDispatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionDao promotionDao;

    private final PromotionStrategyDispatcher promotionStrategyDispatcher;
    @Autowired
    public PromotionServiceImpl(PromotionDao promotionDao, PromotionStrategyDispatcher promotionStrategyDispatcher) {
        this.promotionDao = promotionDao;
        this.promotionStrategyDispatcher = promotionStrategyDispatcher;
    }


    @Override
    public void createPromotion(PromotionVO promotionVO) {
        PromotionPO promotionPO=new PromotionPO();
        BeanUtils.copyProperties(promotionVO,promotionPO);
        int effect = promotionDao.insert(promotionPO);
        if(effect==0){
            throw new MyServiceException("P0001","促销策略生成失败！");
        }
    }

    @Override
    public List<PromotionVO> getPromotion() {
        List<PromotionPO> promotionPOS = promotionDao.findAll();
        List<PromotionVO> promotionVOS=new ArrayList<>();
        for (PromotionPO promotionPO : promotionPOS) {
            PromotionVO promotionVO=new PromotionVO();
            BeanUtils.copyProperties(promotionPO,promotionVO);
            promotionVOS.add(promotionVO);
        }
        return promotionVOS;
    }

    @Override
    public void deleteById(Integer id) {
        int effect=promotionDao.deleteById(id);
        if(effect==0){
            throw new MyServiceException("P0002","促销策略删除失败！");
        }
    }

    @Override
    public void promote(UserVO userVO, SaleSheetVO saleSheetVO) {
        List<PromotionVO> promotionVOS = getPromotion();
        for (PromotionVO promotion : promotionVOS) {
            PromotionCheckStrategy promotionCheckStrategy = promotionStrategyDispatcher.dispatchCheckStrategy(promotion.getTriggerType());
            if (promotionCheckStrategy.checkPromotion(saleSheetVO,promotion)) {
                PromotionExecuteStrategy promotionExecuteStrategy = promotionStrategyDispatcher.dispatchExecStrategy(promotion.getPromotionType());
                PromotionRecordVO recordVO = promotionExecuteStrategy.execute(saleSheetVO, promotion, userVO);
                PromotionRecordPO recordPO=new PromotionRecordPO();
                BeanUtils.copyProperties(recordVO,recordPO);
                promotionDao.insertRecord(recordPO);
            }
        }
    }
}
