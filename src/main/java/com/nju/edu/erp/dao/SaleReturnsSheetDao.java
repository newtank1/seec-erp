package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SaleReturnsSheetDao {
    /**
     * 根据filter获取单据
     * @param sheetFilterPO 过滤条件
     * @return 满足此条件的单据列表
     */
    List<SaleReturnsSheetPO> findByFilter(SheetFilterPO sheetFilterPO);


    /**
     * 获取最近一条销售退货单
     * @return
     */
    SaleReturnsSheetPO getLatestSheet();

    /**
     * 存入一条销售退货单记录
     * @param toSave 一条销售退货单记录
     * @return 影响的行数
     */
    int saveSheet(SaleReturnsSheetPO toSave);

    /**
     * 把销售退货单上的具体内容存入数据库
     * @param saleReturnsSheetContent 入销售退货单上的具体内容
     */
    int saveBatchSheetContent(List<SaleReturnsSheetContentPO> saleReturnsSheetContent);

    /**
     * 查找所有销售退货单
     */
    List<SaleReturnsSheetPO> findAllSheet();

    /**
     * 根据state返回销售退货单
     * @param state 销售退货单状态
     * @return 销售退货单列表
     */
    List<SaleReturnsSheetPO> findAllByState(SaleReturnsSheetState state);

    /**
     * 查找指定销售退货单下具体的商品内容
     * @param sheetId
     */
    List<SaleReturnsSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 更新指定销售退货单的状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, SaleReturnsSheetState state);

    /**
     * 根据当前状态更新销售退货单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, SaleReturnsSheetState prev, SaleReturnsSheetState state);

    /**
     * 通过saleReturnsSheetId找到对应条目
     * @param saleReturnsSheetId 销售退货单id
     * @return
     */
    SaleReturnsSheetPO findOneById(String saleReturnsSheetId);

    /**
     * 通过saleReturnsSheetId找到退的货的对应批次
     * @param saleReturnsSheetId
     * @return 批次号
     */
    Integer findBatchId(String saleReturnsSheetId);

}
