package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface SalaryDao {
    /**
     *添加一个工资单 
     */
    int insert(SalarySheetPO salarySheetPO);

    /**
     *获取所有工资单 
     */
    List<SalarySheetPO> findAll();

    /**
     *获取符合State的所有工资单 
     */
    List<SalarySheetPO> findAllByState(FinancialSheetState state);

    /**
     *更新工资单状态 
     */
    int updateState(SalarySheetPO salarySheetPO);

    /**
     *获取符合条件的工资单 
     */
    List<SalarySheetPO> findByFilter(SheetFilterPO filterPO);

    SalarySheetPO getLatest();

    SalarySheetPO findById(String id);

    List<SalarySheetPO> findByNameAndTime(Date beginDate,Date endDate,String staffName);

    int update(SalarySheetPO po);
}
