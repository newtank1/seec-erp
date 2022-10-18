package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.po.ReceiveSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.TransferPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReceiveDao {
    int insert(ReceiveSheetPO receiveSheetPO);

    List<ReceiveSheetPO> findAllByState(FinancialSheetState state);

    List<ReceiveSheetPO> findAll();

    int saveBatch(List<TransferPO> pos);

    List<TransferPO> findTransferById(String receiveSheetId);

    int updateState(String receiveSheetId, FinancialSheetState state);

    List<ReceiveSheetPO> findByFilter(SheetFilterPO filterPO);

    ReceiveSheetPO getLatest();

    ReceiveSheetPO findById(String id);
}
