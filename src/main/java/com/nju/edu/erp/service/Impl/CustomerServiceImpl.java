package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 增加客户
     * @param inputVO
     * @return CustomerVO
     */
    @Override
    public CustomerVO createCustomer(CustomerVO inputVO){
        CustomerPO savePO = new CustomerPO();
        BeanUtils.copyProperties(inputVO, savePO);
        customerDao.createCustomer(savePO);
        CustomerVO ans = new CustomerVO();
        BeanUtils.copyProperties(savePO, ans);
        return ans;
    }

    /**
     * 根据id更新客户信息
     *
     * @param customerPO 客户信息
     */
    @Override
    public int updateCustomer(CustomerPO customerPO) {
        return customerDao.updateOne(customerPO);
    }

    /**
     * 根据id更新客户信息
     *
     * @param customerVO 客户信息
     */
    @Override
    public int updateCustomer(CustomerVO customerVO) {
        CustomerPO customerPO = customerDao.findOneById(customerVO.getId());
        if(customerPO==null){
            throw new MyServiceException("D0001","用户不存在");
        }
        BeanUtils.copyProperties(customerVO,customerPO);
        int ans= customerDao.updateOne(customerPO);
        if(ans==0){
            throw new MyServiceException("D0001","更新失败");
        }
        return ans;
    }

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    @Override
    public List<CustomerPO> getCustomersByType(CustomerType type) {

        return customerDao.findAllByType(type);
    }

    @Override
    public CustomerPO findCustomerById(Integer supplier) {
        return customerDao.findOneById(supplier);
    }

    /**
     * 删除客户
     * @param id
     */
    @Override
    public int deleteById(Integer id){
        CustomerPO customerPO = customerDao.findOneById(id);
        if (customerPO == null) {
            throw new MyServiceException("D0004", "删除失败！");
        }
        int ans = customerDao.deleteById(id);
        if (ans == 0) {
            throw new MyServiceException("D0004", "删除失败！");
        }
        return ans;
    }
}
