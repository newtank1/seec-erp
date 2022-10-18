package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.WarehouseInputSheetDao;
import com.nju.edu.erp.enums.sheetState.WarehouseInputSheetState;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.WarehouseInputSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseInputSheetPO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Sheet.WarehouseInputService;
import com.nju.edu.erp.service.WarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseInputServiceImpl implements WarehouseInputService {
    private final WarehouseInputSheetDao warehouseInputSheetDao;

    @Autowired
    public WarehouseInputServiceImpl(WarehouseService warehouseService, WarehouseInputSheetDao warehouseInputSheetDao) {
        this.warehouseInputSheetDao = warehouseInputSheetDao;
    }


    @Override
    public void makeSheet(UserVO userVO, WarehouseInputSheetPO warehouseInputSheetPO) {
        
    }

    @Override
    public List<WarehouseInputSheetPO> findByFilter(SheetFilterVO filterVO) {
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterVO,filterPO);
        if(filterPO.getSalesman()!=null&&filterPO.getSalesman().isEmpty()){
            filterPO.setSalesman(null);
        }
        List<WarehouseInputSheetPO> inputSheetPOS = warehouseInputSheetDao.findByFilter(filterPO);
        for (WarehouseInputSheetPO inputSheetPO : inputSheetPOS) {
            List<WarehouseInputSheetContentPO> allContentById = warehouseInputSheetDao.getAllContentById(inputSheetPO.getId());
            inputSheetPO.setContents(allContentById);
        }
        return inputSheetPOS;
    }

    @Override
    public List<WarehouseInputSheetPO> getSheetByState(WarehouseInputSheetState warehouseInputSheetState) {
        return null;
    }


    @Override
    public void approval(String sheetId, WarehouseInputSheetState warehouseInputSheetState) {

    }
}
