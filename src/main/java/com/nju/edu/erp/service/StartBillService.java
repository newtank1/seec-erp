package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.startBill.StartBillVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StartBillService {

    /**
     * 期初建账
     * @param userVO 用户
     * @param startBillVO 期初账目（包含商品、客户、账户信息）
     */
    void createStartBill(UserVO userVO, StartBillVO startBillVO);

    /**
     * 查询期初账目
     * @return 所有期初账目
     */
    List<StartBillVO> getBills();
}
