package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SaleRecordVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.Sheet.SaleReturnsService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleReturnsServiceImpl implements SaleReturnsService {

    SaleReturnsSheetDao saleReturnsSheetDao;

    ProductDao productDao;

    CustomerDao customerDao;

    ProductService productService;

    CustomerService customerService;

    WarehouseService warehouseService;

    SaleSheetDao saleSheetDao;

    WarehouseDao warehouseDao;


    @Autowired
    public SaleReturnsServiceImpl(WarehouseDao warehouseDao, SaleSheetDao saleSheetDao,SaleReturnsSheetDao saleReturnsSheetDao, ProductDao productDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService) {
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.saleSheetDao = saleSheetDao;
        this.warehouseDao = warehouseDao;
    }

    /**
     * 制定销售退货单
     * @param userVO
     * @param saleReturnsSheetVO
     */
    @Override
    @Transactional
    public void makeSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO){
        SaleReturnsSheetPO saleReturnsSheetPO = new SaleReturnsSheetPO();
        BeanUtils.copyProperties(saleReturnsSheetVO,saleReturnsSheetPO);

        saleReturnsSheetPO.setOperator(userVO.getName());
        saleReturnsSheetPO.setCreateTime(new Date());

        SaleReturnsSheetPO latest=saleReturnsSheetDao.getLatestSheet();
        String id= IdGenerator.generateSheetId(latest == null ? null : latest.getId(),"XSTHD");
        saleReturnsSheetPO.setId(id);
        saleReturnsSheetPO.setState(SaleReturnsSheetState.PENDING_LEVEL_1);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContent =
                saleSheetDao.findContentBySaleSheetId(saleReturnsSheetPO.getSaleSheetId());
        Map<String, SaleSheetContentPO> map = new HashMap<>();
        for(SaleSheetContentPO item : saleSheetContent) {
            map.put(item.getPid(), item);
        }

        List<SaleReturnsSheetContentPO> pContentPOList = new ArrayList<>();
        for(SaleReturnsSheetContentVO content : saleReturnsSheetVO.getSaleReturnsSheetContent()) {
            SaleReturnsSheetContentPO pContentPO = new SaleReturnsSheetContentPO();
            BeanUtils.copyProperties(content,pContentPO);

            pContentPO.setSaleReturnsSheetId(id);
            SaleSheetContentPO item = map.get(pContentPO.getPid());
            pContentPO.setUnitPrice(item.getUnitPrice());

            BigDecimal unitPrice = pContentPO.getUnitPrice();
            pContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(pContentPO.getQuantity())));
            pContentPOList.add(pContentPO);
            totalAmount = totalAmount.add(pContentPO.getTotalPrice());
        }
        saleReturnsSheetDao.saveBatchSheetContent(pContentPOList);
        saleReturnsSheetPO.setFinalAmount(totalAmount);
        saleReturnsSheetDao.saveSheet(saleReturnsSheetPO);
    }

    /**
     * 根据单据状态获取销售退货单
     * @param state
     * @return
     */
    @Override
    public List<SaleReturnsSheetVO> getSheetByState(SaleReturnsSheetState state){
        List<SaleReturnsSheetVO> res = new ArrayList<>();
        List<SaleReturnsSheetPO> all;
        if(state == null) {
            all = saleReturnsSheetDao.findAllSheet();
        } else {
            all = saleReturnsSheetDao.findAllByState(state);
        }
        for(SaleReturnsSheetPO po: all) {
            SaleReturnsSheetVO vo = new SaleReturnsSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleReturnsSheetContentPO> alll = saleReturnsSheetDao.findContentBySheetId(po.getId());
            List<SaleReturnsSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnsSheetContentPO p : alll) {
                SaleReturnsSheetContentVO v = new SaleReturnsSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 审批单据
     * @param saleReturnsSheetId
     * @param state
     */
    @Override
    @Transactional
    public void approval(String saleReturnsSheetId, SaleReturnsSheetState state){
        SaleReturnsSheetPO saleReturnsSheet = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if(state.equals(PurchaseReturnsSheetState.FAILURE)) {
            if(saleReturnsSheet.getState() == SaleReturnsSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleReturnsSheetDao.updateSheetState(saleReturnsSheetId, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleReturnsSheetState prevState;
            if(state.equals(SaleReturnsSheetState.SUCCESS)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_2;
            } else if(state.equals(SaleReturnsSheetState.PENDING_LEVEL_2)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleReturnsSheetDao.updateSheetStateOnPrev(saleReturnsSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(SaleReturnsSheetState.SUCCESS)) {
                // TODO 审批完成, 修改一系列状态
                // 销售退货单id， 关联的销售单id 【   销售退货单id->销售单id->出库单id->批次id】
                Integer batchId = saleReturnsSheetDao.findBatchId(saleReturnsSheetId);

                //后面全要反过来，回头再改吧。(_　_)。゜zｚＺ
                //- 销售退货单id-pid， quantity 【批次id+pid -> 定位到库存的一个条目->库存加上quantity】
                //- 【 pid -> 定位到单位售价->Σ单位售价*quantity=要退回的钱->客户payable加上要退回的钱】
                List<SaleReturnsSheetContentPO> contents = saleReturnsSheetDao.findContentBySheetId(saleReturnsSheetId);
                BigDecimal payableToAdd = BigDecimal.ZERO;
                for (SaleReturnsSheetContentPO content:
                        contents) {
                    String pid = content.getPid();
                    Integer quantity = content.getQuantity();
                    WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid, batchId);
                    if(warehousePO == null) throw new RuntimeException("单据发生错误！请联系管理员！");
                    if(warehousePO.getQuantity() >= quantity) {
                        warehousePO.setQuantity(quantity);
                        warehouseDao.deductQuantity(warehousePO);
                        ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                        productInfoVO.setQuantity(productInfoVO.getQuantity()+quantity);
                        productService.updateProduct(productInfoVO);
                        payableToAdd = payableToAdd.add(content.getUnitPrice().multiply(BigDecimal.valueOf(quantity))) ;
                    } else {
                        saleReturnsSheetDao.updateSheetState(saleReturnsSheetId, SaleReturnsSheetState.FAILURE);
                        throw new RuntimeException("商品数量不足！审批失败！");
                    }
                }

                SaleSheetPO saleSheetPO = saleSheetDao.findOneById(saleReturnsSheet.getSaleSheetId());
                Integer supplier = saleSheetPO.getSupplier();
                CustomerPO customer = customerService.findCustomerById(supplier);

                customer.setPayable(customer.getPayable().add(payableToAdd));
                customerService.updateCustomer(customer);
            }
        }
    }

    /**
     * 根据销售退货单Id搜索销售退货单信息
     * @param saleReturnsSheetId 销售退货单Id
     * @return 销售退货单全部信息
     */
    public SaleReturnsSheetVO getSaleReturnsSheetById(String saleReturnsSheetId){
        SaleReturnsSheetPO saleReturnsSheetPO = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if(saleReturnsSheetPO == null) return null;
        List<SaleReturnsSheetContentPO> contentPO = saleReturnsSheetDao.findContentBySheetId(saleReturnsSheetId);
        SaleReturnsSheetVO sVO = new SaleReturnsSheetVO();
        BeanUtils.copyProperties(saleReturnsSheetPO, sVO);
        List<SaleReturnsSheetContentVO> saleReturnsSheetContentVOList = new ArrayList<>();
        for (SaleReturnsSheetContentPO content:
                contentPO) {
            SaleReturnsSheetContentVO sContentVO = new SaleReturnsSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleReturnsSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleReturnsSheetContent(saleReturnsSheetContentVOList);
        return sVO;
    }

    @Override
    public List<SaleRecordVO> findRecords(SaleRecordFilterVO filterVO) {
        SheetFilterVO sheetFilterVO=new SheetFilterVO();//构造sheetFilter
        sheetFilterVO.setBeginDate(filterVO.getBeginDate());
        sheetFilterVO.setEndDate(filterVO.getEndDate());
        sheetFilterVO.setCustomerId(filterVO.getCustomerId());
        sheetFilterVO.setSalesman(filterVO.getSalesman());
        sheetFilterVO.setWarehouseId(filterVO.getWarehouseId());

        List<SaleReturnsSheetVO> returnsSheetVOS = findByFilter(sheetFilterVO);
        List<SaleRecordVO> recordVOS=new ArrayList<>();
        for (SaleReturnsSheetVO returnsSheetVO : returnsSheetVOS) {
            for (SaleReturnsSheetContentVO saleReturnsSheetContentVO : returnsSheetVO.getSaleReturnsSheetContent()) {
                String pid = saleReturnsSheetContentVO.getPid();
                ProductPO productPO = productDao.findById(pid);
                if(productPO.getName().equals(filterVO.getProductName())){
                    SaleRecordVO recordVO=new SaleRecordVO();
                    recordVO.setDate(returnsSheetVO.getCreateTime());
                    recordVO.setProductName(productPO.getName());
                    recordVO.setType(productPO.getType());
                    recordVO.setQuantity(saleReturnsSheetContentVO.getQuantity());
                    recordVO.setUnitPrice(saleReturnsSheetContentVO.getUnitPrice());
                    recordVO.setTotalPrice(saleReturnsSheetContentVO.getTotalPrice());
                    recordVOS.add(recordVO);
                }
            }
        }
        return recordVOS;
    }

    @Override
    public List<SaleReturnsSheetVO> findByFilter(SheetFilterVO filterVO) {
        Assert.isTrue(filterVO.getType()== SheetType.SALE_RETURN||filterVO.getType()==null,"过滤类型需为销售退货单");
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterVO,filterPO);
        if(filterPO.getSalesman()!=null&&filterPO.getSalesman().isEmpty()){
            filterPO.setSalesman(null);
        }
        List<SaleReturnsSheetPO> sheetPOS = saleReturnsSheetDao.findByFilter(filterPO);
        return sheetPOS.stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    private SaleReturnsSheetVO transformPOtoVO(SaleReturnsSheetPO po){
        SaleReturnsSheetVO vo=new SaleReturnsSheetVO();
        BeanUtils.copyProperties(po,vo);
        List<SaleReturnsSheetContentVO> contentVOS=new ArrayList<>();
        List<SaleReturnsSheetContentPO> contentPOS = saleReturnsSheetDao.findContentBySheetId(po.getId());
        for (SaleReturnsSheetContentPO contentPO : contentPOS) {
            SaleReturnsSheetContentVO contentVO=new SaleReturnsSheetContentVO();
            BeanUtils.copyProperties(contentPO,contentVO);
            contentVOS.add(contentVO);
        }
        vo.setSaleReturnsSheetContent(contentVOS);
        return vo;
    }
}
