package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.StartBillDao;
import com.nju.edu.erp.model.po.StartBankAccountPO;
import com.nju.edu.erp.model.po.StartBillPO;
import com.nju.edu.erp.model.po.StartCustomerPO;
import com.nju.edu.erp.model.po.StartProductPO;
import com.nju.edu.erp.model.vo.startBill.StartBankAccountVO;
import com.nju.edu.erp.model.vo.startBill.StartBillVO;
import com.nju.edu.erp.model.vo.startBill.StartCustomerVO;
import com.nju.edu.erp.model.vo.startBill.StartProductVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.StartBillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StartBillServiceImpl implements StartBillService {

    private final StartBillDao startBillDao;

    @Autowired
    public StartBillServiceImpl(StartBillDao startBillDao) {
        this.startBillDao = startBillDao;
    }


    /**
     * 期初建账
     *
     * @param userVO      用户
     * @param startBillVO 期初账目（包含商品、客户、账户信息）
     */
    @Override
    @Transactional
    public void createStartBill(UserVO userVO, StartBillVO startBillVO) {
        //TODO: change old records
        StartBillPO startBillPO=new StartBillPO();
        BeanUtils.copyProperties(startBillVO,startBillPO);
        startBillPO.setCreateTime(new Date());//设置PO

        int effectLines;
        
        startBillDao.insert(startBillPO);

        //插入期初商品
        List<StartProductPO> productPOS=new ArrayList<>();
        for (StartProductVO productVO : startBillVO.getProducts()) {
            StartProductPO productPO=new StartProductPO();
            BeanUtils.copyProperties(productVO,productPO);
            productPOS.add(productPO);
        }
        effectLines=startBillDao.insertProducts(productPOS);
        if(effectLines!=startBillVO.getProducts().size()){
            throw new RuntimeException("Failed");
        }

        //插入期初客户
        List<StartCustomerPO> customerPOS=new ArrayList<>();
        for (StartCustomerVO customerVO : startBillVO.getCustomers()) {
            StartCustomerPO customerPO=new StartCustomerPO();
            BeanUtils.copyProperties(customerVO,customerPO);
            customerPOS.add(customerPO);
        }
        effectLines=startBillDao.insertCustomers(customerPOS);
        if(effectLines!=startBillVO.getCustomers().size()){
            throw new RuntimeException("Failed");
        }

        //插入期初账户
        List<StartBankAccountPO> accountPOS=new ArrayList<>();
        for (StartBankAccountVO accountVO : startBillVO.getBankAccounts()) {
            StartBankAccountPO accountPO=new StartBankAccountPO();
            BeanUtils.copyProperties(accountVO,accountPO);
            accountPOS.add(accountPO);
        }
        effectLines=startBillDao.insertAccounts(accountPOS);
        if(effectLines!=startBillVO.getBankAccounts().size()){
            throw new RuntimeException("Failed");
        }
    }

    /**
     * 查询期初账目
     *
     * @return 所有期初账目
     */
    @Override
    public List<StartBillVO> getBills() {
        List<StartBillPO> all = startBillDao.findAll();
        List<StartBillVO> vos=new ArrayList<>();
        for (StartBillPO startBillPO : all) {
            vos.add(transformPOtoVO(startBillPO));
        }
        return vos;
    }

    private StartBillVO transformPOtoVO(StartBillPO po){
        StartBillVO vo=new StartBillVO();
        BeanUtils.copyProperties(po,vo);

        //转换期初商品
        List<StartProductPO> productPOS=startBillDao.findProductsById(po.getId());
        List<StartProductVO> productVOS=new ArrayList<>();
        for (StartProductPO productPO : productPOS) {
            StartProductVO productVO=new StartProductVO();
            BeanUtils.copyProperties(productPO,productVO);
            productVOS.add(productVO);
        }
        vo.setProducts(productVOS);

        //转换期初客户
        List<StartCustomerPO> customerPOS=startBillDao.findCustomersById(po.getId());
        List<StartCustomerVO> customerVOS=new ArrayList<>();
        for (StartCustomerPO customerPO : customerPOS) {
            StartCustomerVO customerVO=new StartCustomerVO();
            BeanUtils.copyProperties(customerPO,customerVO);
            customerVOS.add(customerVO);
        }
        vo.setCustomers(customerVOS);

        //转换期初账户
        List<StartBankAccountPO> bankAccountPOS=startBillDao.findAccountsById(po.getId());
        List<StartBankAccountVO> bankAccountVOS=new ArrayList<>();
        for (StartBankAccountPO bankAccountPO : bankAccountPOS) {
            StartBankAccountVO bankAccountVO=new StartBankAccountVO();
            BeanUtils.copyProperties(bankAccountPO,bankAccountVO);
            bankAccountVOS.add(bankAccountVO);
        }
        vo.setBankAccounts(bankAccountVOS);
        return vo;
    }

}
