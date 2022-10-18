package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    BankAccountVO getMockAccount(){
        return BankAccountVO.builder().name("123").surplus(new BigDecimal("0.00")).build();
    }

    List<BankAccountVO> generateAccounts(){
        List<BankAccountVO> accountVOS=new ArrayList<>();
        accountVOS.add(BankAccountVO.builder().name("1").surplus(new BigDecimal(0)).build());
        accountVOS.add(BankAccountVO.builder().name("12").surplus(new BigDecimal(0)).build());
        accountVOS.add(BankAccountVO.builder().name("13").surplus(new BigDecimal(0)).build());
        accountVOS.add(BankAccountVO.builder().name("123").surplus(new BigDecimal(0)).build());
        return accountVOS;
    }
    @Test
    @Transactional
    @Rollback(value = true)
    void create() {
        BankAccountVO bankAccountVO= getMockAccount();
        BankAccountVO retVO=accountService.create(bankAccountVO);
        assertEquals(retVO.getName(),bankAccountVO.getName());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void deleteByID() {
        BankAccountVO bankAccountVO= getMockAccount();
        BankAccountVO retVO=accountService.create(bankAccountVO);
        assertEquals(2,accountService.search("123").size());
        accountService.deleteByID(retVO.getAccountId());
        assertEquals(1,accountService.search("123").size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void update() {
        BankAccountVO bankAccountVO= getMockAccount();
        BankAccountVO retVO=accountService.create(bankAccountVO);
        assertEquals(2,accountService.search("123").size());
        retVO.setName("124");
        accountService.update(retVO);
        assertEquals(1,accountService.search("123").size());
        assertEquals(1,accountService.search("124").size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void search() {
        List<BankAccountVO> accountVOS = generateAccounts();
        for (BankAccountVO accountVO : accountVOS) {
            accountService.create(accountVO);
        }
        assertEquals(2,accountService.search("123").size());
        assertEquals(3,accountService.search("12").size());
        assertEquals(3,accountService.search("2").size());
        assertEquals(5,accountService.search("1").size());
        assertEquals(0,accountService.search("1234").size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findById(){
        BankAccountVO mockAccount = getMockAccount();
        BankAccountVO inserted = accountService.create(mockAccount);
        assertEquals(inserted,accountService.findById(inserted.getAccountId()));
    }


}