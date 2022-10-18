package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.SalaryDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.Impl.Salary.TaxTable;
import com.nju.edu.erp.service.Salary.SalaryStrategy;
import com.nju.edu.erp.service.Salary.SalaryStrategyFactory;
import com.nju.edu.erp.service.Sheet.SalaryService;
import com.nju.edu.erp.service.UserService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaryServiceImpl implements SalaryService {
    private final SalaryDao salaryDao;
    private final SalaryStrategyFactory salaryStrategyFactory;
    private final TaxTable taxTable;
    private final UserService userService;

    @Autowired
    public SalaryServiceImpl(SalaryDao salaryDao, SalaryStrategyFactory salaryStrategyFactory, TaxTable taxTable, UserService userService) {
        this.salaryDao = salaryDao;
        this.salaryStrategyFactory = salaryStrategyFactory;
        this.taxTable = taxTable;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void makeSheet(UserVO userVO, SalarySheetVO salarySheetVO) {
        SalarySheetPO salarySheetPO=new SalarySheetPO();
        BeanUtils.copyProperties(salarySheetVO,salarySheetPO);

        salarySheetPO.setCreateTime(new Date());

        SalarySheetPO latest = salaryDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZD");
        salarySheetPO.setId(id);
        salarySheetPO.setState(FinancialSheetState.PENDING);

        UserVO user=userService.getUserByName(salarySheetVO.getStaffName());
        //原始工资
        SalaryStrategy salaryStrategy = salaryStrategyFactory.getSalaryStrategy(user.getRole());
        BigDecimal rawSalary = salaryStrategy.calculateRawSalary(user);
        salarySheetPO.setRawSalary(rawSalary);

        calculateTotalTaxAndSalary(salarySheetPO);

        UserInfoVO userInfo = userService.getUserInfo(salarySheetVO.getStaffName());
        salarySheetPO.setBankAccount(userInfo.getAccount());

        int effect = salaryDao.insert(salarySheetPO);
        if(effect==0){
            throw new MyServiceException("S0001","工资单生成失败！");
        }
    }

    /**
     * 计算并填入各项税款和最终工资
     * @param salarySheetPO
     */
    private void calculateTotalTaxAndSalary(SalarySheetPO salarySheetPO) {
        BigDecimal rawSalary=salarySheetPO.getRawSalary();
        //个人所得税
        BigDecimal tax = taxTable.calculateTax(rawSalary);
        salarySheetPO.setIncomeTax(tax);

        //住房公积金，需要算去年平均工资
        Date startDate=new Date();
        startDate.setYear(startDate.getYear()-1);
        startDate.setMonth(Calendar.JANUARY);
        startDate.setDate(1);
        Date endDate=new Date();
        endDate.setYear(startDate.getYear());
        endDate.setMonth(Calendar.DECEMBER);
        endDate.setDate(31);
        List<SalarySheetPO> lastYearSalaryRecord = salaryDao.findByNameAndTime(startDate, endDate, salarySheetPO.getStaffName());
        List<BigDecimal> lastYearSalary = lastYearSalaryRecord.stream().map(SalarySheetPO::getFinalSalary).collect(Collectors.toList());

        BigDecimal lastYearTotal=BigDecimal.ZERO;
        if(!lastYearSalary.isEmpty()){
            lastYearTotal = lastYearSalary.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(lastYearSalaryRecord.size()),RoundingMode.HALF_EVEN);
        }
        BigDecimal houseFund = lastYearTotal.divide(new BigDecimal(100),RoundingMode.HALF_EVEN).multiply(new BigDecimal(5));
        salarySheetPO.setHousingFund(houseFund);//住房公积金5%

        //失业保险
        BigDecimal insurance = rawSalary.multiply(new BigDecimal(2)).divide(new BigDecimal(100),RoundingMode.HALF_EVEN);
        salarySheetPO.setInsurance(insurance);


        salarySheetPO.setFinalSalary(rawSalary.subtract(tax).subtract(houseFund).subtract(insurance));
    }

    @Override
    public List<SalarySheetVO> findByFilter(SheetFilterVO filterVO) {
        Assert.isTrue(filterVO.getType()==SheetType.SALARY||filterVO.getType()==null,"过滤类型需为工资单");
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterPO,filterVO);
        return salaryDao.findByFilter(filterPO).stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    @Override
    public List<SalarySheetVO> getSheetByState(FinancialSheetState state) {
        List<SalarySheetPO> pos;
        if(state==null){
            pos=salaryDao.findAll();
        }else {
            pos=salaryDao.findAllByState(state);
        }
        return pos.stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approval(String sheetId, FinancialSheetState state) {
        SalarySheetPO sheetPO = salaryDao.findById(sheetId);
        if(sheetPO==null){
            throw new MyServiceException("S0002","工资单不存在");
        }
        FinancialSheetState prev = sheetPO.getState();
        if(prev!=FinancialSheetState.PENDING){
            throw new MyServiceException("S0003","该工资单已审批过");
        }
        if(state==FinancialSheetState.PENDING){
            throw new MyServiceException("S0004","审批错误");
        }
        sheetPO.setState(state);
        salaryDao.updateState(sheetPO);
    }

    private SalarySheetVO transformPOtoVO(SalarySheetPO po){
        SalarySheetVO vo=new SalarySheetVO();
        BeanUtils.copyProperties(po,vo);
        return vo;
    }

    @Override
    public List<SalarySheetVO> getAll() {
        List<SalarySheetPO> all = salaryDao.findAll();
        return all.stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    @Override
    public void makeBonus(String sheetId, BigDecimal newSalary) {
        SalarySheetPO before = salaryDao.findById(sheetId);
        before.setRawSalary(newSalary);
        calculateTotalTaxAndSalary(before);
        int effect = salaryDao.update(before);
        if(effect==0){
            throw new MyServiceException("S0005","年终奖生成失败！");
        }
    }
}
