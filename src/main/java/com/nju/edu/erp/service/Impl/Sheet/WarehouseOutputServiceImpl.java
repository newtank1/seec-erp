package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.WarehouseOutputSheetDao;
import com.nju.edu.erp.enums.sheetState.WarehouseOutputSheetState;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseOutputSheetPO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Sheet.WarehouseOutputService;
import com.nju.edu.erp.service.WarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseOutputServiceImpl implements WarehouseOutputService {
    private final WarehouseOutputSheetDao warehouseOutputSheetDao;

    @Autowired
    public WarehouseOutputServiceImpl(WarehouseService warehouseService, WarehouseOutputSheetDao warehouseOutputSheetDao) {
        this.warehouseOutputSheetDao = warehouseOutputSheetDao;
    }


    @Override
    public void makeSheet(UserVO userVO, WarehouseOutputSheetPO warehouseOutputSheetPO) {
        
    }

    @Override
    public List<WarehouseOutputSheetPO> findByFilter(SheetFilterVO filterVO) {
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterVO,filterPO);
        if(filterPO.getSalesman()!=null&&filterPO.getSalesman().isEmpty()){
            filterPO.setSalesman(null);
        }
        List<WarehouseOutputSheetPO> inputSheetPOS = warehouseOutputSheetDao.findByFilter(filterPO);
        for (WarehouseOutputSheetPO inputSheetPO : inputSheetPOS) {
            List<WarehouseOutputSheetContentPO> allContentById = warehouseOutputSheetDao.getAllContentById(inputSheetPO.getId());
            inputSheetPO.setContents(allContentById);
        }
        return inputSheetPOS;
    }

    @Override
    public List<WarehouseOutputSheetPO> getSheetByState(WarehouseOutputSheetState warehouseOutputSheetState) {
        return null;
    }


    @Override
    public void approval(String sheetId, WarehouseOutputSheetState warehouseOutputSheetState) {

    }
}
