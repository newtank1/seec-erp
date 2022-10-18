package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.BankAccountPO;
import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    public BankAccountVO create(BankAccountVO accountVO) {
        BankAccountPO accountPO=new BankAccountPO();
        BeanUtils.copyProperties(accountVO,accountPO);
        int res=accountDao.insert(accountPO);
        if(res==0){
            throw new MyServiceException("E0001","not inserted");
        }
        BankAccountVO ret=new BankAccountVO();
        BeanUtils.copyProperties(accountPO,ret);
        return ret;
    }

    @Override
    public void deleteByID(Integer id) {
        int res=accountDao.deleteById(id);
        if(res==0){
            throw new MyServiceException("E0002","not deleted");
        }
    }

    @Override
    public void update(BankAccountVO accountVO) {
        BankAccountPO accountPO=new BankAccountPO();
        BeanUtils.copyProperties(accountVO,accountPO);
        int res=accountDao.update(accountPO);
        if(res==0){
            throw new MyServiceException("E0003","not updated");
        }
    }

    @Override
    public List<BankAccountVO> search(String keyword) {
        List<BankAccountPO> accountPOS = accountDao.findByKeyword(keyword);
        List<BankAccountVO> accountVOS=new ArrayList<>();
        for (BankAccountPO accountPO : accountPOS) {
            BankAccountVO accountVO=new BankAccountVO();
            BeanUtils.copyProperties(accountPO,accountVO);
            accountVOS.add(accountVO);
        }
        return accountVOS;
    }

    @Override
    public BankAccountVO findById(Integer id) {
        BankAccountPO accountPO = accountDao.findById(id);
        BankAccountVO accountVO=new BankAccountVO();
        BeanUtils.copyProperties(accountPO,accountVO);
        return accountVO;
    }

    @Override
    public List<BankAccountVO> findAll() {
        List<BankAccountPO> pos = accountDao.findAll();
        List<BankAccountVO> vos=new ArrayList<>();
        for (BankAccountPO po : pos) {
            BankAccountVO vo=new BankAccountVO();
            BeanUtils.copyProperties(po,vo);
            vos.add(vo);
        }
        return vos;
    }

}
