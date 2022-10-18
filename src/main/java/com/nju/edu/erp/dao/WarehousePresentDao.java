package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.WarehousePresentSheetState;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.WarehousePresentSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface WarehousePresentDao {

    WarehousePresentSheetPO getLatest();

    int insert(WarehousePresentSheetPO po);

    List<WarehousePresentSheetPO> findByFilter(SheetFilterPO filterPO);

    List<WarehousePresentSheetPO> findAll();

    List<WarehousePresentSheetPO> findAllByState(WarehousePresentSheetState state);

    WarehousePresentSheetPO findById(String id);

    int updateState(String sheetId,WarehousePresentSheetState state);

    int setExpense(String sheetId, BigDecimal expense);
}
