package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.ReceiveDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.FinancialSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ReceiveSheetPO;
import com.nju.edu.erp.model.po.SheetFilterPO;
import com.nju.edu.erp.model.po.TransferPO;
import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.model.vo.ReceiveSheetVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.TransferVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.Sheet.ReceiveService;
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
public class ReceiveServiceImpl implements ReceiveService{

    private final ReceiveDao receiveDao;

    private final CustomerService customerService;

    private final AccountService accountService;

    @Autowired
    public ReceiveServiceImpl(ReceiveDao receiveDao, CustomerService customerService, AccountService accountService) {
        this.receiveDao = receiveDao;
        this.customerService = customerService;
        this.accountService = accountService;
    }


    /**
     * 向ReceiveDao保存收款单
     *
     * @param userVO         用户
     * @param receiveSheetVO 收款单内容
     */
    @Override
    @Transactional
    public void makeSheet(UserVO userVO, ReceiveSheetVO receiveSheetVO) {
        ReceiveSheetPO receiveSheetPO=new ReceiveSheetPO();
        BeanUtils.copyProperties(receiveSheetVO,receiveSheetPO);

        receiveSheetPO.setOperator(userVO.getName());
        receiveSheetPO.setCreateTime(new Date());

        ReceiveSheetPO latest=receiveDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
        receiveSheetPO.setId(id);
        receiveSheetPO.setState(FinancialSheetState.PENDING);
        BigDecimal total=BigDecimal.ZERO;

        List<TransferPO> transferPOS=new ArrayList<>();
        for (TransferVO transferVO : receiveSheetVO.getTransferList()) {
            TransferPO transferPO=new TransferPO();
            BeanUtils.copyProperties(transferVO,transferPO);
            transferPO.setSheetId(id);
            transferPO.setId(null);
            total=total.add(transferPO.getTransferMoney());
            transferPOS.add(transferPO);
        }
        receiveSheetPO.setTotalAmount(total);
        receiveDao.insert(receiveSheetPO);
        receiveDao.saveBatch(transferPOS);
    }

    /**
     * 从ReceiveDao查询符合state的收款单
     *
     * @param state 状态
     * @return 符合状态的收款单列表。state为null时查询所有
     */
    @Override
    public List<ReceiveSheetVO> getSheetByState(FinancialSheetState state) {
        List<ReceiveSheetVO> res=new ArrayList<>();
        List<ReceiveSheetPO> all=new ArrayList<>();
        if (state == null) {
            all=receiveDao.findAll();
        }else {
            all=receiveDao.findAllByState(state);
        }
        for (ReceiveSheetPO po :all){
            res.add(transformPOtoVO(po));
        }

        return res;
    }

    /**
     * 审批收款单
     *
     * @param receiveSheetId 待审批单据编号
     * @param state          目标状态
     */
    @Override
    @Transactional
    public void approval(String receiveSheetId, FinancialSheetState state) {
        if(state==FinancialSheetState.PENDING){
            throw new RuntimeException("需要审批结果");
        }
        ReceiveSheetPO receiveSheetPO = receiveDao.findById(receiveSheetId);
        if(receiveSheetPO.getState()!=FinancialSheetState.PENDING){
            throw new RuntimeException("该单据已审批");
        }
        int effectLine=receiveDao.updateState(receiveSheetId,state);
        if(effectLine==0){
            throw new RuntimeException("审批更新失败");
        }
        if(state==FinancialSheetState.SUCCESS){
            //更新客户应付
            Integer customerId = receiveSheetPO.getCustomerId();
            CustomerPO customer = customerService.findCustomerById(customerId);
            BigDecimal receivable = customer.getReceivable();
            receivable=receivable.add(receiveSheetPO.getTotalAmount());
            customer.setReceivable(receivable);
            customerService.updateCustomer(customer);

            //修改账户余额
            List<TransferPO> transferPOS = receiveDao.findTransferById(receiveSheetId);
            for (TransferPO transferPO : transferPOS) {
                BigDecimal transferMoney = transferPO.getTransferMoney();
                Integer accountId = transferPO.getAccountId();
                BankAccountVO accountVO = accountService.findById(accountId);
                BigDecimal surplus = accountVO.getSurplus();
                surplus=surplus.subtract(transferMoney);
                accountVO.setSurplus(surplus);
                accountService.update(accountVO);
            }
        }
    }

    /**
     * 获取收款单
     *
     * @param filter 筛选条件
     * @return 符合条件的收款单列表
     */
    @Override
    public List<ReceiveSheetVO> findByFilter(SheetFilterVO filter) {
        Assert.isTrue(filter.getType()== SheetType.RECEIVE||filter.getType()==null,"过滤类型需为收款单");
        SheetFilterPO sheetFilterPO = new SheetFilterPO();
        BeanUtils.copyProperties(filter,sheetFilterPO);
        List<ReceiveSheetPO> receiveSheetPOS = receiveDao.findByFilter(sheetFilterPO);
        List<ReceiveSheetVO> receiveSheetVOS=new ArrayList<>();
        for (ReceiveSheetPO receiveSheetPO : receiveSheetPOS) {
            receiveSheetVOS.add(transformPOtoVO(receiveSheetPO));

        }
        return receiveSheetVOS;
    }

    private ReceiveSheetVO transformPOtoVO(ReceiveSheetPO receiveSheetPO){
        ReceiveSheetVO receiveSheetVO=new ReceiveSheetVO();
        BeanUtils.copyProperties(receiveSheetPO,receiveSheetVO);

        List<TransferPO> transferPOS = receiveDao.findTransferById(receiveSheetVO.getId());
        List<TransferVO> transferVOS=new ArrayList<>();
        for (TransferPO transferPO : transferPOS) {
            TransferVO transferVO=new TransferVO();
            BeanUtils.copyProperties(transferPO,transferVO);
            transferVOS.add(transferVO);
        }
        receiveSheetVO.setTransferList(transferVOS);
        return receiveSheetVO;
    }
}
