package com.nju.edu.erp.service.Impl.Sheet;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleRecordFilterVO;
import com.nju.edu.erp.model.vo.SaleRecordVO;
import com.nju.edu.erp.model.vo.SheetFilterVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.Promotion.PromotionService;
import com.nju.edu.erp.service.Sheet.SaleService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final PromotionService promotionService;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, PromotionService promotionService) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.promotionService = promotionService;
    }

    @Override
    @Transactional
    public void makeSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // TODO
        // 需要持久化销售单（SaleSheet）和销售单content（SaleSheetContent），其中总价或者折后价格的计算需要在后端进行
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        SaleSheetPO saleSheetPO=new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);

        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());

        SaleSheetPO latest=saleSheetDao.getLatestSheet();
        String id=IdGenerator.generateSheetId(latest == null ? null : latest.getId(),"XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);

        BigDecimal totalAmount=BigDecimal.ZERO;
        List<SaleSheetContentPO> sheetContentPOList=new ArrayList<>();

        for (SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()) {
            SaleSheetContentPO saleSheetContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,saleSheetContentPO);
            saleSheetContentPO.setSaleSheetId(id);

            saleSheetContentPO.setTotalPrice(saleSheetContentPO.getUnitPrice().multiply(BigDecimal.valueOf(saleSheetContentPO.getQuantity())));
            sheetContentPOList.add(saleSheetContentPO);
            totalAmount=totalAmount.add(saleSheetContentPO.getTotalPrice());
        }

        saleSheetDao.saveBatchSheetContent(sheetContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);

        BigDecimal discounting = totalAmount.multiply(saleSheetPO.getDiscount());
        saleSheetPO.setFinalAmount(discounting.add(saleSheetPO.getVoucherAmount().negate()));
        promotionService.promote(userVO,saleSheetVO);//进行促销任务
        saleSheetDao.saveSheet(saleSheetPO);
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSheetByState(SaleSheetState state) {
        // TODO
        // 根据单据状态获取销售单（注意：VO包含SaleSheetContent）
        // 依赖的dao层部分方法未提供，需要自己实现
        List<SaleSheetVO> res=new ArrayList<>();
        List<SaleSheetPO> all;
        if(state==null){
            all=saleSheetDao.findAllSheet();
        }else {
            all=saleSheetDao.findAllByState(state);
        }

        for (SaleSheetPO po : all) {
            SaleSheetVO vo=new SaleSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleSheetContentPO> contentPOS=saleSheetDao.findContentBySaleSheetId(po.getId());
            List<SaleSheetContentVO> contentVOS=new ArrayList<>();
            for (SaleSheetContentPO contentPO : contentPOS) {
                SaleSheetContentVO contentVO=new SaleSheetContentVO();
                BeanUtils.copyProperties(contentPO,contentVO);
                contentVOS.add(contentVO);
            }
            vo.setSaleSheetContent(contentVOS);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据销售单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleSheetId 销售单id
     * @param state       销售单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        /* 一些注意点：
            1. 二级审批成功之后需要进行
                 1. 修改单据状态
                 2. 更新商品表
                 3. 更新客户表
                 4. 新建出库草稿
            2. 一级审批状态不能直接到审批完成状态； 二级审批状态不能回到一级审批状态
         */
        SaleSheetPO saleSheetPO=saleSheetDao.findSheetById(saleSheetId);
        if(state.equals(SaleSheetState.FAILURE)){
            if(saleSheetPO.getState().equals(SaleSheetState.SUCCESS)) throw new RuntimeException("已经审批成功！");
            int effectLines=saleSheetDao.updateSheetState(saleSheetId,state);
            if(effectLines==0) throw new RuntimeException("状态更新失败！");
        }else {
            SaleSheetState prevState;
            if(state.equals(SaleSheetState.SUCCESS)){
                prevState=SaleSheetState.PENDING_LEVEL_2;
            }else if(state.equals(SaleSheetState.PENDING_LEVEL_2)){
                prevState= SaleSheetState.PENDING_LEVEL_1;
            }else {
                throw new RuntimeException("状态更新失败！");
            }
            int effectLines=saleSheetDao.updateSheetStateOnPrev(saleSheetId,prevState,state);
            if(effectLines==0) throw new RuntimeException("状态更新失败！");

            if(state.equals(SaleSheetState.SUCCESS)){
                List<SaleSheetContentPO> contents = saleSheetDao.findContentBySaleSheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS=new ArrayList<>();

                for (SaleSheetContentPO content : contents) {
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());

                    productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO warehouseOutputFormContentVO = new WarehouseOutputFormContentVO();
                    warehouseOutputFormContentVO.setSalePrice(content.getUnitPrice());
                    warehouseOutputFormContentVO.setQuantity(content.getQuantity());
                    warehouseOutputFormContentVO.setRemark(content.getRemark());
                    warehouseOutputFormContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(warehouseOutputFormContentVO);
                }

                CustomerPO customerPO = customerService.findCustomerById(saleSheetPO.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheetPO.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                WarehouseOutputFormVO warehouseOutputFormVO=new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null);
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                CustomerPurchaseAmountPO customer = saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman, beginTime, endTime);
                customer.setCustomerPO(customerService.findCustomerById(customer.getCustomerID()));
                return customer;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content:
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
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

        List<SaleSheetVO> saleSheetVOS = findByFilter(sheetFilterVO);
        List<SaleRecordVO> recordVOS=new ArrayList<>();
        for (SaleSheetVO saleSheetVO : saleSheetVOS) {
            for (SaleSheetContentVO saleSheetContentVO : saleSheetVO.getSaleSheetContent()) {
                String pid = saleSheetContentVO.getPid();
                ProductPO productPO = productDao.findById(pid);
                if(filterVO.getProductName()==null||productPO.getName().equals(filterVO.getProductName())){
                    SaleRecordVO recordVO=new SaleRecordVO();
                    recordVO.setDate(saleSheetVO.getCreateTime());
                    recordVO.setProductName(productPO.getName());
                    recordVO.setType(productPO.getType());
                    recordVO.setQuantity(saleSheetContentVO.getQuantity());
                    recordVO.setUnitPrice(saleSheetContentVO.getUnitPrice());
                    recordVO.setTotalPrice(saleSheetContentVO.getTotalPrice());
                    recordVOS.add(recordVO);
                }
            }
        }
        return recordVOS;
    }

    @Override
    public List<SaleSheetVO> findByFilter(SheetFilterVO filterVO) {
        Assert.isTrue(filterVO.getType()== SheetType.SALE||filterVO.getType()==null,"过滤类型需为销售单");
        SheetFilterPO filterPO=new SheetFilterPO();
        BeanUtils.copyProperties(filterVO,filterPO);
        if(filterPO.getSalesman()!=null&&filterPO.getSalesman().isEmpty()){
            filterPO.setSalesman(null);
        }
        List<SaleSheetPO> sheetPOS = saleSheetDao.findByFilter(filterPO);
        return sheetPOS.stream().map(this::transformPOtoVO).collect(Collectors.toList());
    }

    private SaleSheetVO transformPOtoVO(SaleSheetPO po){
        SaleSheetVO vo=new SaleSheetVO();
        BeanUtils.copyProperties(po,vo);
        List<SaleSheetContentVO> contentVOS=new ArrayList<>();
        List<SaleSheetContentPO> contentPOS = saleSheetDao.findContentBySaleSheetId(po.getId());
        for (SaleSheetContentPO contentPO : contentPOS) {
            SaleSheetContentVO contentVO=new SaleSheetContentVO();
            BeanUtils.copyProperties(contentPO,contentVO);
            contentVOS.add(contentVO);
        }
        vo.setSaleSheetContent(contentVOS);
        return vo;
    }
}
