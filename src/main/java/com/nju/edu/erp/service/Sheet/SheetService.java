package com.nju.edu.erp.service.Sheet;

import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

public interface SheetService<SheetVO,SheetState> {
    /**
     * 制定单据
     * @param sheetVO 单据
     */
    void makeSheet(UserVO userVO, SheetVO sheetVO);

    /**
     * 根据过滤条件获取单据
     * @param filterVO 过滤条件
     * @return 符合条件的单据VO，包含所有内容
     */
    List<SheetVO> findByFilter(SheetFilterVO filterVO);

    /**
     * 根据状态获取单据(state == null 则获取所有单据)
     * @param state 单据状态
     * @return 单据
     */

    List<SheetVO> getSheetByState(SheetState state);

    /**
     * 根据单据id进行审批(state参照state的类)
     * 在controller层进行权限控制
     * @param sheetId 单据id
     * @param state 单据修改后的状态
     */
    void approval(String sheetId,SheetState state);
}
