package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.TransferPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PaymentDao {
    int insert(PaymentSheetPO paymentSheetPO);

    List<PaymentSheetPO> findAllByState(FinancialSheetState state);

    List<PaymentSheetPO> findAll();

    int saveBatch(List<TransferPO> pos);

    List<TransferPO> findTransferById(String paymentSheetId);

    int updateState(String paymentSheetId, FinancialSheetState state);

    List<PaymentSheetPO> findByFilter(SheetFilterPO filterPO);

    PaymentSheetPO getLatest();

    PaymentSheetPO findById(String id);
}
