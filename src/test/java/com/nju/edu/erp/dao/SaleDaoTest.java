package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class SaleDaoTest {
    @Autowired
    SaleSheetDao saleSheetDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void testFilter() {
        SheetFilterVO filterVO = SheetFilterVO.builder()
                .build();
        System.out.println(filterVO);
        SheetFilterPO filterPO = new SheetFilterPO();
        BeanUtils.copyProperties(filterVO, filterPO);
        System.out.println(filterPO);
        List<SaleSheetPO> sheetPOS = saleSheetDao.findByFilter(filterPO);
        System.out.println(sheetPOS);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void testFindSheetById() {
        SaleSheetPO sheetPO = saleSheetDao.findSheetById("XSD-20220524-00002");
        System.out.println(sheetPO);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void testGetLatest() {
        SaleSheetPO saleSheetPO = saleSheetDao.getLatestSheet();
        System.out.println(saleSheetPO);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void testInsert() {
        SaleSheetPO saleSheetPO = SaleSheetPO.builder()
                .id("XSD-20220708-00001")
                .build();
        int i = saleSheetDao.saveSheet(saleSheetPO);
        System.out.println(i);
    }
}
