package com.nju.edu.erp.service.Impl.Salary;

import com.nju.edu.erp.dao.SaleReturnsSheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Salary.SalaryStrategy;
import com.nju.edu.erp.service.Sheet.SaleService;
import com.nju.edu.erp.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BasicSalaryStrategy implements SalaryStrategy {
    private final SaleService saleService;
    private final UserService userService;
    private final SaleReturnsSheetDao saleReturnsSheetDao;
    private final SaleSheetDao saleSheetDao;

    public BasicSalaryStrategy(SaleService saleService, UserService userService, SaleReturnsSheetDao saleReturnsSheetDao, SaleSheetDao saleSheetDao) {
        this.saleService = saleService;
        this.userService = userService;
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.saleSheetDao = saleSheetDao;
    }

    @Override
    public BigDecimal calculateRawSalary(UserVO userVO) {
        UserInfoVO userInfo = userService.getUserInfo(userVO.getName());
        BigDecimal basicSalary = userInfo.getBasicSalary();

        //扣缺勤
        int absentDay=userService.getAbsentDay(userVO);
        basicSalary=basicSalary.multiply(new BigDecimal(Math.max(30-absentDay,0))).divide(new BigDecimal(30), RoundingMode.HALF_EVEN);

        //加提成
        //获取销售记录
        Date beginDate=new Date();
        beginDate.setDate(1);
        Date endDate=new Date();
        SheetFilterPO filter=SheetFilterPO.builder().beginDate(beginDate).endDate(endDate).salesman(userVO.getName()).build();
        List<SaleSheetPO> saleSheetPOS = saleSheetDao.findByFilter(filter);
        List<SaleReturnsSheetPO> allReturn = saleReturnsSheetDao.findAllSheet();
        List<SaleReturnsSheetPO> usedReturnSheets=new ArrayList<>();

        List<String> saleSheetIds = saleSheetPOS.stream().map(SaleSheetPO::getId).collect(Collectors.toList());
        for (SaleReturnsSheetPO saleReturnsSheetPO : allReturn) {
            if(saleSheetIds.contains(saleReturnsSheetPO.getSaleSheetId())){
                usedReturnSheets.add(saleReturnsSheetPO);
            }
        }

        //计算提成
        BigDecimal commission=BigDecimal.ZERO;
        for (SaleSheetPO saleSheetPO : saleSheetPOS) {
            if(saleSheetPO.getState()== SaleSheetState.SUCCESS)
                commission=commission.add(saleSheetPO.getFinalAmount());
        }
        for (SaleReturnsSheetPO saleReturnsSheetPO : usedReturnSheets) {
            if(saleReturnsSheetPO.getState()== SaleReturnsSheetState.SUCCESS)
                commission=commission.subtract(saleReturnsSheetPO.getFinalAmount());
        }
        if(commission.compareTo(BigDecimal.ZERO)<0) commission=BigDecimal.ZERO;

        commission=commission.multiply(new BigDecimal(2)).divide(new BigDecimal(100),RoundingMode.HALF_EVEN);//2%提成
        return basicSalary.add(commission);
    }
}
