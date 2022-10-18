package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.dao.WarehousePresentDao;
import com.nju.edu.erp.enums.sheetState.WarehousePresentSheetState;
import com.nju.edu.erp.model.po.WarehousePO;
import com.nju.edu.erp.model.po.WarehousePresentSheetPO;
import com.nju.edu.erp.service.Sheet.WarehousePresentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WarehousePresentServiceImplTest {
    @Autowired
    WarehousePresentDao warehousePresentDao;
    @Autowired
    WarehousePresentService warehousePresentService;
    @Autowired
    WarehouseDao warehouseDao;
    @Test
    @Transactional
    @Rollback(value = true)
    void approval() {
        String id = "ZSD-20220706-000000";
        String pid = "0000000000400000";
        WarehousePresentSheetPO po=WarehousePresentSheetPO.builder()
                .pid(pid)
                .quantity(400)
                .state(WarehousePresentSheetState.PENDING)
                .createTime(new Date())
                .expenses(BigDecimal.ZERO)
                .id(id).build();
        warehousePresentDao.insert(po);

        warehousePresentService.approval(id,WarehousePresentSheetState.SUCCESS);
        WarehousePO batch1 = warehouseDao.findOneByPidAndBatchId(pid, 1);
        assertEquals(0,batch1.getQuantity());
        WarehousePO batch2 = warehouseDao.findOneByPidAndBatchId(pid, 3);
        assertEquals(100,batch2.getQuantity());

        WarehousePresentSheetPO latest = warehousePresentDao.getLatest();
        assertEquals(new BigDecimal("620000.00"),latest.getExpenses());
    }
}