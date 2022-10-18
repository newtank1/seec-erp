package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionPO;
import com.nju.edu.erp.model.po.PromotionRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PromotionDao {
    int insert(PromotionPO promotionPO);

    List<PromotionPO> findAll();

    int deleteById(Integer id);

    int insertRecord(PromotionRecordPO promotionRecordPO);
}
