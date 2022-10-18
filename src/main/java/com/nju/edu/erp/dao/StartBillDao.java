package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.StartBankAccountPO;
import com.nju.edu.erp.model.po.StartBillPO;
import com.nju.edu.erp.model.po.StartCustomerPO;
import com.nju.edu.erp.model.po.StartProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StartBillDao {
    /**
     *插入一条期初建账
     */
    int insert(StartBillPO startBillPO);

    /**
     *插入期初建账中的商品信息
     */
    int insertProducts(List<StartProductPO> pos);

    /**
     *插入期初建账中的客户信息
     */
    int insertCustomers(List<StartCustomerPO> pos);

    /**
     *插入期初建账中的账户信息
     */
    int insertAccounts(List<StartBankAccountPO> pos);

    /**
     *查询所有期初建账
     */
    List<StartBillPO> findAll();

    /**
     *根据建账id查询所有期初商品
     */
    List<StartProductPO> findProductsById(Integer id);

    /**
     *根据建账id查询所有期初客户
     */
    List<StartCustomerPO> findCustomersById(Integer id);

    /**
     *根据建账id查询所有期初账户
     */
    List<StartBankAccountPO> findAccountsById(Integer id);


}
