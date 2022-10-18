package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.PaymentDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.TransferPO;
import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.model.vo.PaymentSheetVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.TransferVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.Sheet.PaymentService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;

    private final CustomerService customerService;

    private final AccountService accountService;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao, CustomerService customerService, AccountService accountService) {
        this.paymentDao = paymentDao;
        this.customerService = customerService;
        this.accountService = accountService;
    }


    /**
     * 向PaymentDao保存付款单
     *
     * @param userVO         用户
     * @param paymentSheetVO 付款单内容
     */
    @Override
    @Transactional
    public void makeSheet(UserVO userVO, PaymentSheetVO paymentSheetVO) {
        PaymentSheetPO paymentSheetPO=new PaymentSheetPO();
        BeanUtils.copyProperties(paymentSheetVO,paymentSheetPO);

        paymentSheetPO.setOperator(userVO.getName());
        paymentSheetPO.setCreateTime(new Date());

        PaymentSheetPO latest=paymentDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "FKD");
        paymentSheetPO.setId(id);
        paymentSheetPO.setState(FinancialSheetState.PENDING);
        BigDecimal total=BigDecimal.ZERO;

        List<TransferPO> transferPOS=new ArrayList<>();
        for (TransferVO transferVO : paymentSheetVO.getTransferList()) {
            TransferPO transferPO=new TransferPO();
            BeanUtils.copyProperties(transferVO,transferPO);
            transferPO.setSheetId(id);
            transferPO.setId(null);
            total=total.add(transferPO.getTransferMoney());
            transferPOS.add(transferPO);
        }
        paymentSheetPO.setTotalAmount(total);
        paymentDao.insert(paymentSheetPO);
        paymentDao.saveBatch(transferPOS);
    }

    /**
     * 从PaymentDao查询符合state的付款单
     *
     * @param state 状态
     * @return 符合状态的付款单列表。state为null时查询所有
     */
    @Override
    public List<PaymentSheetVO> getSheetByState(FinancialSheetState state) {
        List<PaymentSheetVO> res=new ArrayList<>();
        List<PaymentSheetPO> all=new ArrayList<>();
        if (state == null) {
            all=paymentDao.findAll();
        }else {
            all=paymentDao.findAllByState(state);
        }
        for (PaymentSheetPO po :all){
            res.add(transformPOtoVO(po));
        }

        return res;
    }

    /**
     * 审批付款单
     *
     * @param paymentSheetId 待审批单据编号
     * @param state          目标状态
     */
    @Override
    @Transactional
    public void approval(String paymentSheetId, FinancialSheetState state) {
        if(state==FinancialSheetState.PENDING){
            throw new RuntimeException("需要审批结果");
        }
        PaymentSheetPO paymentSheetPO = paymentDao.findById(paymentSheetId);
        if(paymentSheetPO.getState()!=FinancialSheetState.PENDING){
            throw new RuntimeException("该单据已审批");
        }
        int effectLine=paymentDao.updateState(paymentSheetId,state);
        if(effectLine==0){
            throw new RuntimeException("审批更新失败");
        }
        if(state==FinancialSheetState.SUCCESS){
            //修改客户应付数据
            Integer customerId = paymentSheetPO.getCustomerId();
            CustomerPO customer = customerService.findCustomerById(customerId);
            BigDecimal payable = customer.getPayable();
            payable=payable.add(paymentSheetPO.getTotalAmount());
            customer.setPayable(payable);
            customerService.updateCustomer(customer);

            //修改账户余额
            List<TransferPO> transferPOS = paymentDao.findTransferById(paymentSheetId);
            for (TransferPO transferPO : transferPOS) {
                BigDecimal transferMoney = transferPO.getTransferMoney();
                Integer accountId = transferPO.getAccountId();
                BankAccountVO accountVO = accountService.findById(accountId);
                BigDecimal surplus = accountVO.getSurplus();
                surplus=surplus.add(transferMoney);
                accountVO.setSurplus(surplus);
                accountService.update(accountVO);
            }
        }
    }

    /**
     * 获取付款单
     *
     * @param filter 筛选条件
     * @return 符合条件的付款单列表
     */
    @Override
    public List<PaymentSheetVO> findByFilter(SheetFilterVO filter) {
        Assert.isTrue(filter.getType()== SheetType.PAYMENT||filter.getType()==null,"过滤类型需为付款单");
        SheetFilterPO sheetFilterPO = new SheetFilterPO();
        BeanUtils.copyProperties(filter,sheetFilterPO);
        List<PaymentSheetPO> paymentSheetPOS = paymentDao.findByFilter(sheetFilterPO);
        List<PaymentSheetVO> paymentSheetVOS=new ArrayList<>();
        for (PaymentSheetPO paymentSheetPO : paymentSheetPOS) {
            paymentSheetVOS.add(transformPOtoVO(paymentSheetPO));
        }
        return paymentSheetVOS;
    }

    private PaymentSheetVO transformPOtoVO(PaymentSheetPO paymentSheetPO){
        PaymentSheetVO paymentSheetVO=new PaymentSheetVO();
        BeanUtils.copyProperties(paymentSheetPO,paymentSheetVO);

        List<TransferPO> transferPOS = paymentDao.findTransferById(paymentSheetVO.getId());
        List<TransferVO> transferVOS=new ArrayList<>();
        for (TransferPO transferPO : transferPOS) {
            TransferVO transferVO=new TransferVO();
            BeanUtils.copyProperties(transferPO,transferVO);
            transferVOS.add(transferVO);
        }
        paymentSheetVO.setTransferList(transferVOS);
        return paymentSheetVO;
    }

}
