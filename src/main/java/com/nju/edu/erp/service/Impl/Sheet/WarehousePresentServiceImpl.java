package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.dao.WarehousePresentDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.WarehousePresentSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.WarehousePO;
import com.nju.edu.erp.model.po.WarehousePresentSheetPO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehousePresentSheetVO;
import com.nju.edu.erp.service.Sheet.WarehousePresentService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehousePresentServiceImpl implements WarehousePresentService {
    private final WarehousePresentDao warehousePresentDao;
    private final WarehouseDao warehouseDao;

    @Autowired
    public WarehousePresentServiceImpl(WarehousePresentDao warehousePresentDao, WarehouseDao warehouseDao) {
        this.warehousePresentDao = warehousePresentDao;
        this.warehouseDao = warehouseDao;
    }

    @Override
    @Transactional
    public void makeSheet(UserVO userVO, WarehousePresentSheetVO warehousePresentSheetVO) {
        WarehousePresentSheetPO po=new WarehousePresentSheetPO();
        BeanUtils.copyProperties(warehousePresentSheetVO,po);

        WarehousePresentSheetPO latest = warehousePresentDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "ZSD");
        po.setId(id);
        po.setCreateTime(new Date());
        po.setState(WarehousePresentSheetState.PENDING);
        po.setExpenses(BigDecimal.ZERO);

        int effect = warehousePresentDao.insert(po);
        if(effect==0){
            throw new MyServiceException("Z0000","赠送单生成失败");
        }
    }

    @Override
    public List<WarehousePresentSheetVO> findByFilter(SheetFilterVO filterVO) {
        Assert.isTrue(filterVO.getType()== SheetType.WAREHOUSE_PRESENT||filterVO.getType()==null,"过滤类型需为赠送单");
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterVO,filterPO);
        return warehousePresentDao.findByFilter(filterPO).stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    @Override
    public List<WarehousePresentSheetVO> getSheetByState(WarehousePresentSheetState warehousePresentSheetState) {
        List<WarehousePresentSheetPO> pos;
        if(warehousePresentSheetState==null){
            pos=warehousePresentDao.findAll();
        }else {
            pos=warehousePresentDao.findAllByState(warehousePresentSheetState);
        }
        return pos.stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approval(String sheetId, WarehousePresentSheetState warehousePresentSheetState) {
        if(warehousePresentSheetState== WarehousePresentSheetState.PENDING){
            throw new RuntimeException("需要审批结果");
        }
        WarehousePresentSheetPO warehousePresentSheetPO = warehousePresentDao.findById(sheetId);
        if(warehousePresentSheetPO.getState()!=WarehousePresentSheetState.PENDING){
            throw new RuntimeException("该单据已审批");
        }
        int effectLine=warehousePresentDao.updateState(sheetId,warehousePresentSheetState);
        if(effectLine==0){
            throw new RuntimeException("审批更新失败");
        }
        if(warehousePresentSheetState==WarehousePresentSheetState.SUCCESS){
            List<WarehousePO> warehousePOS = warehouseDao.findByPidOrderByPurchasePricePos(warehousePresentSheetPO.getPid());
            Collections.reverse(warehousePOS);//按进价从低到高赠送

            Integer totalQuantity = warehousePresentSheetPO.getQuantity();
            BigDecimal expenses=BigDecimal.ZERO;
            for (WarehousePO warehousePO : warehousePOS) {
                Integer quantity = warehousePO.getQuantity();
                if(quantity.compareTo(totalQuantity)>=0){
                    expenses=expenses.add(new BigDecimal(totalQuantity).multiply(warehousePO.getPurchasePrice()));
                    warehousePO.setQuantity(totalQuantity);//在某批次完成赠送
                    warehouseDao.deductQuantity(warehousePO);

                    effectLine=warehousePresentDao.setExpense(sheetId,expenses);//设置成本，用于审计
                    if(effectLine==0){
                        throw new RuntimeException("赠送单更新失败");
                    }
                    return;
                }
                expenses=expenses.add(new BigDecimal(quantity).multiply(warehousePO.getPurchasePrice()));
                warehouseDao.deductQuantity(warehousePO);
                totalQuantity=totalQuantity-quantity;
            }

            throw new MyServiceException("Z0001","可赠送商品不足");
        }
    }

    private WarehousePresentSheetVO transformPOtoVO(WarehousePresentSheetPO po){
        WarehousePresentSheetVO vo=new WarehousePresentSheetVO();
        BeanUtils.copyProperties(po,vo);
        return vo;
    }
}
