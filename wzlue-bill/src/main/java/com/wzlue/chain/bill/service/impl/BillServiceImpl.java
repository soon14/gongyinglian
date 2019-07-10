package com.wzlue.chain.bill.service.impl;


import com.wzlue.chain.agent.dao.AgentContractDao;
import com.wzlue.chain.agent.dao.AgentOrderDao;
import com.wzlue.chain.agent.entity.AgentOrderEntity;
import com.wzlue.chain.bill.dao.IncomeExpenditureDetailDao;
import com.wzlue.chain.bill.dao.MyAccountDao;
import com.wzlue.chain.bill.entity.*;
import com.wzlue.chain.common.enums.CreditType;
import com.wzlue.chain.common.utils.SplitUtil;
import com.wzlue.chain.company.entity.MerchantCompanyEntity;
import com.wzlue.chain.company.dao.MerchantCompanyDao;
import com.wzlue.chain.bill.dao.PaymentRecordDao;
import com.wzlue.chain.declare.dao.DeclareOrderDao;
import com.wzlue.chain.declare.entity.DeclareOrderEntity;
import com.wzlue.chain.logistics.dao.LogisticsOrderDao;
import com.wzlue.chain.logistics.entity.LogisticsOrderEntity;
import com.wzlue.chain.order.dao.GoodsOrderDao;
import com.wzlue.chain.order.entity.GoodsOrderEntity;
import com.wzlue.chain.ship.entity.GoodsOrderShipEntity;
import com.wzlue.chain.sys.MSdx.ApiDemo4Java;
import com.wzlue.chain.sys.entity.ImageEntity;
import com.wzlue.chain.sys.entity.SysUserEntity;
import com.wzlue.chain.sys.service.ImageService;
import com.wzlue.chain.sys.service.SysNumberRuleService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.wzlue.chain.bill.dao.BillDao;
import com.wzlue.chain.bill.service.BillService;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;


@Service("billService")
public class BillServiceImpl implements BillService {
    @Autowired
    private BillDao billDao;
    @Autowired
    private PaymentRecordDao paymentRecordDao;
    @Autowired
    private MerchantCompanyDao merchantCompanyDao;
    @Autowired
    private MyAccountDao myAccountDao;
    @Autowired
    private DeclareOrderDao declareOrderDao;
    @Autowired
    private GoodsOrderDao goodsOrderDao;
    @Autowired
    private LogisticsOrderDao logisticsOrderDao;
    @Autowired
    private AgentOrderDao agentOrderDao;
    @Autowired
    private ImageService imageService;
    @Autowired
    private SysNumberRuleService numberRuleService;
    @Autowired
    private IncomeExpenditureDetailDao incomeExpenditureDetailDao;
    @Autowired
    private AgentContractDao agentContractDao;


    @Override
    public BillEntity queryObject(Integer id) {
        return billDao.queryObject(id);
    }

    @Override
    public List<BillEntity> queryList(Map<String, Object> map) {
        return billDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return billDao.queryTotal(map);
    }

    @Override
    public void save(BillEntity bill) {
        //卖家接单 生成账单以及款项记录信息
        Integer orderType = bill.getOrderType();    //订单类型 0：报关 1：货物 2：物流 3：仓储 4：代理
        String orderNumber = bill.getOrderNumber(); //订单号
        String contractNumber = null;   //合同编号
        BigDecimal total = null;    //订单金额
        String payee = null;           //收款商家名称
        String payer = null;          //付款商家名称
        Integer payeeId = null;
        Integer payerId = null;
        String date = null;         //接单日期
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer transactionMode = null;  //货物订单 付款方式
        BigDecimal downPayment = null;  //货物订单 首付金额
        Long unit = null;       //  价格单位(币种)

        //（货物订单）收付款银行卡号
        String cardPayee = null;
        String cardPayer = null;

        switch (orderType) {    //报关订单
            case 0:
                DeclareOrderEntity declareOrder = declareOrderDao.queryByOrderNumber(orderNumber);
                if (declareOrder != null) {
                    contractNumber = declareOrder.getContractNumber();
                    total = declareOrder.getTotal();
                    payee = declareOrder.getMerchantCompanyName();
                    payer = declareOrder.getBuyCompanyName();
                    payeeId = declareOrder.getMerchantCompanyId();
                    payerId = declareOrder.getBuyCompanyId();
                    if (declareOrder.getOrderTime() == null) {
                        date = sdf.format(currentTime);
                    } else {
                        date = sdf.format(declareOrder.getOrderTime());
                    }
                    unit = declareOrder.getUnit();
                }
                // 卖家通知买家 已接单
                MerchantCompanyEntity maiJia = merchantCompanyDao.getCompanyByCompnayid(payerId + "");
                if (maiJia != null) {
                    ApiDemo4Java.sendbJD(orderNumber, payee, maiJia.getMobile());
                }

                break;
            case 1:     //货物订单      货物订单可向平台申请账期
                GoodsOrderEntity goodsOrder = goodsOrderDao.getListByOrderNumber(orderNumber);
                if (goodsOrder != null) {
                    contractNumber = goodsOrder.getAgreementId();
//                    BigDecimal totalPrice = goodsOrder.getTotalPrice();//商品总价
//                    BigDecimal transportationExpenses = goodsOrder.getTransportationExpenses();//运费
//                    total = totalPrice.add(transportationExpenses);
                    total = goodsOrder.getActualPayment();
                    payee = goodsOrder.getSellerName();
                    payer = goodsOrder.getPurchaserCompanyName();
                    payeeId = Integer.valueOf(goodsOrder.getSellerId());
                    payerId = Integer.valueOf(goodsOrder.getPurchaserCompanyId());
                    date = sdf.format(goodsOrder.getCreateTime());
                    transactionMode = Integer.valueOf(goodsOrder.getTransactionMode());//付款方式'一次性支付', '分期支付', '其他（账期）
                    downPayment = goodsOrder.getDownPayment();//首付金额
                    //币种
                    if (goodsOrder.getCurrency() == 1) {
                        unit = Long.valueOf(0);
                    } else if (goodsOrder.getCurrency() == 2) {
                        unit = Long.valueOf(1);
                    }
                    // 卖家通知买家 已接单
                    MerchantCompanyEntity maiJia2 = merchantCompanyDao.getCompanyByCompnayid(payerId + "");
                    if (maiJia2 != null) {
                        ApiDemo4Java.sendbJD(orderNumber, payee, maiJia2.getMobile());
                    }
                    //收款帐号 付款账号
                    if (isNotBlank(goodsOrder.getSellerBankCard())) {
                        cardPayee = goodsOrder.getSellerBankCard();
                    }
                    if (isNotBlank(goodsOrder.getBuyerBankCard())) {
                        cardPayer = goodsOrder.getBuyerBankCard();
                    }
                }
                break;
            case 2:     //物流订单
                LogisticsOrderEntity logisticsOrder = logisticsOrderDao.queryByOrderNumber(orderNumber);
                if (logisticsOrder != null) {
                    contractNumber = logisticsOrder.getContractNumber();
                    total = logisticsOrder.getTotal();
                    unit = logisticsOrder.getUnit();
                    payee = logisticsOrder.getMerchantCompanyName();
                    payer = logisticsOrder.getContactCompanyName();
                    payeeId = logisticsOrder.getMerchantCompanyId().intValue();
                    payerId = logisticsOrder.getCompanyId().intValue();//买家Id
                    date = sdf.format(logisticsOrder.getReceiptTime());
                    // 卖家通知买家 已接单
                    MerchantCompanyEntity maiJia3 = merchantCompanyDao.getCompanyByCompnayid(payerId + "");
                    if (maiJia3 != null) {
                        ApiDemo4Java.sendbJD(orderNumber, payee, maiJia3.getMobile());
                    }
                }
                break;
            case 3:     //仓储订单
                break;
            case 4:     //代理订单
                AgentOrderEntity agentOrder = agentOrderDao.queryByOrderNum(orderNumber);
                if (agentOrder != null) {
                    contractNumber = agentOrder.getContractNumber();
                    total = agentOrder.getTotalPrice();
                    payee = agentOrder.getMerchantCompanyName();
                    payer = agentOrder.getContactCompanyName();
                    payeeId = agentOrder.getMerchantCompanyId().intValue();
                    payerId = agentOrder.getContactCompanyId().intValue();
                    date = sdf.format(agentOrder.getReceiptTime());
                    //币种
                    if (agentOrder.getCurrency().equals("CNY")) {
                        unit = Long.valueOf(0);
                    } else if (agentOrder.getCurrency().equals("USD")) {
                        unit = Long.valueOf(1);
                    }
                    // 卖家通知买家 已接单
                    MerchantCompanyEntity maiJia4 = merchantCompanyDao.getCompanyByCompnayid(payerId + "");
                    if (maiJia4 != null) {
                        ApiDemo4Java.sendbJD(orderNumber, payee, maiJia4.getMobile());
                    }
                }
                break;
        }
        // 0：报关  2：物流  4：代理  1:一次性支付的货物订单
        if (orderType == 0 || orderType == 2 || orderType == 4 || (orderType == 1 && transactionMode == 0)) {
            bill.setBillType(0);    //普通账单
            bill.setContractNumber(contractNumber);
            bill.setPayee(payee);
            bill.setPayer(payer);
            bill.setPayeeId(payeeId);
            bill.setPayerId(payerId);
            bill.setAmountPayable(total);
            bill.setAmountReceivable(total);
            bill.setAmountPaid(BigDecimal.ZERO);
            bill.setAmountReceived(BigDecimal.ZERO);
            bill.setRemainingAmountPayable(total);
            bill.setRemainingAmountReceivable(total);
            bill.setPayableStatus(0);
            bill.setReceivableStatus(0);
            bill.setCreateBy(user.getUserId().intValue());
            bill.setCreatedTime(currentTime);
            bill.setUnit(unit);
            billDao.save(bill);

            Integer billId = bill.getId();
            PaymentRecordEntity record = new PaymentRecordEntity();
            record.setBillId(billId);
            record.setAmount(total);
            record.setDate(date);
            record.setPayStatus(0);
            record.setCreateBy(user.getUserId().intValue());
            record.setCreatedTime(currentTime);
            record.setUnit(unit);
            if (cardPayee != null) {
                record.setPayeeNumber(cardPayee);
            }
            if (cardPayer != null) {
                record.setPayerNumber(cardPayer);
            }
            paymentRecordDao.save(record);
        }
        if (orderType == 1 && transactionMode == 1) {     // 分期支付的货物订单
            bill.setBillType(0);    //普通账单
            bill.setContractNumber(contractNumber);
            bill.setPayee(payee);
            bill.setPayer(payer);
            bill.setPayeeId(payeeId);
            bill.setPayerId(payerId);
            bill.setAmountPayable(total);
            bill.setAmountReceivable(total);
            bill.setAmountPaid(BigDecimal.ZERO);
            bill.setAmountReceived(BigDecimal.ZERO);
            bill.setRemainingAmountPayable(total);
            bill.setRemainingAmountReceivable(total);
            bill.setPayableStatus(0);
            bill.setReceivableStatus(0);
            bill.setCreateBy(user.getUserId().intValue());
            bill.setCreatedTime(currentTime);
            bill.setUnit(unit);
            billDao.save(bill);

            Integer billId = bill.getId();
            PaymentRecordEntity record1 = new PaymentRecordEntity();
            record1.setBillId(billId);
            record1.setAmount(downPayment);//首付金额
            record1.setDate(date);
            record1.setPayStatus(0);
            record1.setCreateBy(user.getUserId().intValue());
            record1.setCreatedTime(currentTime);
            record1.setUnit(unit);
            if (cardPayee != null) {
                record1.setPayeeNumber(cardPayee);
            }
            if (cardPayer != null) {
                record1.setPayerNumber(cardPayer);
            }
            paymentRecordDao.save(record1);

            if (total.compareTo(downPayment) == 1) { //total>downPayment
                PaymentRecordEntity record2 = new PaymentRecordEntity();
                record2.setBillId(billId);
                record2.setAmount(total.subtract(downPayment));//尾款=总价款-首付金额
                record2.setDate(date);
                record2.setPayStatus(0);
                record2.setCreateBy(user.getUserId().intValue());
                record2.setCreatedTime(currentTime);
                record2.setUnit(unit);
                if (cardPayee != null) {
                    record2.setPayeeNumber(cardPayee);
                }
                if (cardPayer != null) {
                    record2.setPayerNumber(cardPayer);
                }
                paymentRecordDao.save(record2);
            }
        }
        if (orderType == 1 && transactionMode == 2) {        //付款方式为账期的货物订单
            bill.setBillType(1);    //账期账单
            bill.setContractNumber(contractNumber);
            bill.setPayee(payee);
            bill.setPayer(payer);
            bill.setPayeeId(payeeId);
            bill.setPayerId(payerId);
            bill.setFinalPayee(payee);  //最终收款方 即订单的卖家
            bill.setFinalPayeeId(payeeId);
            bill.setAmountPayable(total);
            bill.setAmountReceivable(total);
            bill.setAmountPaid(BigDecimal.ZERO);
            bill.setAmountReceived(BigDecimal.ZERO);
            bill.setRemainingAmountPayable(total);
            bill.setRemainingAmountReceivable(total);
            bill.setPayableStatus(0);
            bill.setReceivableStatus(0);
            bill.setCreateBy(user.getUserId().intValue());
            bill.setCreatedTime(currentTime);

            bill.setCreditNumber(numberRuleService.getCodeNumberByPrefix("GDOD")); //编码规则产生的账期编号
            bill.setApplicationTime(currentTime);   //申请时间
            bill.setAuditStatus(0);     //审核状态  0：待审核 1：通过 2：不通过
            bill.setCreditAmount(total);    //账期金额
            bill.setUnit(unit);
            billDao.save(bill);

            Integer billId = bill.getId();
            PaymentRecordEntity record = new PaymentRecordEntity();
            record.setBillId(billId);
            record.setAmount(total);
            record.setDate(date);
            record.setPayStatus(0);
            record.setCreateBy(user.getUserId().intValue());
            record.setCreatedTime(currentTime);
            record.setUnit(unit);
            paymentRecordDao.save(record);

        }
    }

    @Override
    public void update(BillEntity bill) {
        billDao.update(bill);
    }

    @Override
    public void delete(Integer id) {
        billDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        billDao.deleteBatch(ids);
    }

    //应收账单
    @Override
    public List<BillEntity> queryListReceive(Map<String, Object> map) {
        return billDao.queryListReceive(map);
    }

    @Override
    public int queryTotalReceive(Map<String, Object> map) {
        return billDao.queryTotalReceive(map);
    }

    //应付账单
    @Override
    public List<BillEntity> queryListPay(Map<String, Object> map) {
        return billDao.queryListPay(map);
    }

    @Override
    public int queryTotalPay(Map<String, Object> map) {
        return billDao.queryTotalPay(map);
    }

    @Override
    public MyAccountEntity getMyAccount() {
        //当前用户 ---> 公司 ---> 公司账户
        MyAccountEntity myAccount = null;
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        MerchantCompanyEntity company = merchantCompanyDao.queryByUserId(user.getUserId());
        if (company != null) {
            myAccount = myAccountDao.queryObjectByCompanyId(company.getId().intValue());
        }
        return myAccount;
    }

    //余额支付 -- 买家打款给平台
    @Override
    public void balancePayment(List<PaymentRecordEntity> paymentRecords, PaymentEntity payment) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
//        BigDecimal accountBalance = payment.getAccountBalance();账户余额
        BigDecimal amountToPay = payment.getAmountToPay();
        String remarks = payment.getRemarks();
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String payeeLegalPersonName = null;
        String payerLegalPersonName = null;
        String payeeAccountNumber = null;
        String payerAccountNumber = null;
        //billId --- bill ---payeeId、payerId
        Integer billId = paymentRecords.get(0).getBillId();
        BillEntity bill = billDao.queryObject(billId);
        Integer payeeId = bill.getPayeeId();
        Integer payerId = bill.getPayerId();

        ////更改买家应付账单
        BigDecimal paid = bill.getAmountPaid().add(amountToPay);   //已付金额
        BigDecimal remaining = bill.getAmountPayable().subtract(paid);   //剩余应付金额
        int i = remaining.compareTo(BigDecimal.ZERO);   //剩余应付金额与0比较
        if (i == 1) {   //剩余应付金额>0
            bill.setRemainingAmountPayable(remaining);
            bill.setPayableStatus(0);    //未结清
        } else {     //剩余应付金额<=0
            bill.setRemainingAmountPayable(BigDecimal.ZERO);
            bill.setPayableStatus(1);    //已结清
        }
        bill.setAmountPaid(paid);
        bill.setPaymentTime(currentTime);
        bill.setPaymentRemarks(remarks);
        bill.setUpdateBy(user.getUserId().intValue());
        bill.setUpdateTime(currentTime);
        billDao.update(bill);

        //买家 打款给 平台 （之后由平台操作 【余额支付】打款给 卖家）
        //*****假设*****  平台的 公司id为1 账户id为4
        //公司 --- 户名
        MerchantCompanyEntity payeeCompany = merchantCompanyDao.queryObject(payeeId);
        MerchantCompanyEntity payerCompany = merchantCompanyDao.queryObject(payerId);
        MerchantCompanyEntity masterCompany = merchantCompanyDao.queryObject(1);
        if (payeeCompany != null) {
            payeeLegalPersonName = payeeCompany.getLegalPersonName();
        }
        if (payerCompany != null) {
            payerLegalPersonName = payerCompany.getLegalPersonName();
        }
        //账户 --- 账号
        MyAccountEntity payeeAccount = myAccountDao.queryObjectByCompanyId(payeeId);
        MyAccountEntity payerAccount = myAccountDao.queryObjectByCompanyId(payerId);
        MyAccountEntity masterAccount = myAccountDao.queryObjectByCompanyId(1);    //平台账户
        if (payeeAccount != null) {
            payeeAccountNumber = payeeAccount.getAccount();
        }
        //给买家用户打一笔款
        //扣减买家账户余额
        if (payerAccount != null) {
            payerAccountNumber = payerAccount.getAccount();
            BigDecimal balance = payerAccount.getBalance(); //买家账户余额
            BigDecimal subtract = balance.subtract(amountToPay);
            payerAccount.setBalance(subtract);
            myAccountDao.update(payerAccount);
        }
        //增加平台账户余额
        if (masterAccount != null) {
            BigDecimal balance = masterAccount.getBalance();
            BigDecimal add = balance.add(amountToPay);
            masterAccount.setBalance(add);
            myAccountDao.update(masterAccount);
        }

        ////更改买家付款记录
        for (PaymentRecordEntity record : paymentRecords) {
            record.setPayStatus(1);
            record.setPayMethod(1);
            record.setPayeeNumber(payeeAccountNumber);
            record.setPayerNumber(payerAccountNumber);
            record.setPayeeName(payeeLegalPersonName);
            record.setPayerName(payerLegalPersonName);
            record.setPaymentOperator(user.getUsername());
            record.setPaymentTime(currentTime);
            record.setPaymentRemarks(remarks);
            record.setUpdateBy(user.getUserId().intValue());
            record.setUpdateTime(currentTime);
            paymentRecordDao.update(record);
        }

        //给平台账户打一笔款
        ////给平台新增 一条账单以及款项记录 （平台只有应付账单）
        BillEntity masterBill = new BillEntity();
        masterBill.setBillType(0);      //普通账单
        masterBill.setRepeatOrder(0);   //*****订单号重复的账单
        masterBill.setOrderType(bill.getOrderType());
        masterBill.setOrderNumber(bill.getOrderNumber());
        masterBill.setContractNumber(bill.getContractNumber());
        masterBill.setPayee(bill.getPayee());
        masterBill.setPayeeId(bill.getPayeeId());
        masterBill.setPayer(masterCompany.getCompanyName());
        masterBill.setPayerId(1);//付款公司id 为 平台的公司id
        masterBill.setAmountPayable(amountToPay);
        masterBill.setAmountPaid(BigDecimal.ZERO);
        masterBill.setRemainingAmountPayable(amountToPay);
        masterBill.setPayableStatus(0);
        masterBill.setAmountReceivable(amountToPay);
        masterBill.setAmountReceived(BigDecimal.ZERO);
        masterBill.setRemainingAmountReceivable(amountToPay);
        masterBill.setReceivableStatus(0);

        masterBill.setCreateBy(user.getUserId().intValue());
        masterBill.setCreatedTime(currentTime);
        billDao.save(masterBill);

        PaymentRecordEntity masterRecord = new PaymentRecordEntity();
        masterRecord.setBillId(masterBill.getId());
        masterRecord.setAmount(amountToPay);
        masterRecord.setDate(sdf.format(currentTime));
        masterRecord.setPayStatus(0);

        masterRecord.setCreateBy(user.getUserId().intValue());
        masterRecord.setCreatedTime(currentTime);
        paymentRecordDao.save(masterRecord);

        ////更改各类订单的状态
        updateStatus(bill);
    }

    ////付款 更改各类订单的状态
    private void updateStatus(BillEntity bill) {
        Integer orderType = bill.getOrderType();    //订单类型 0：报关 1：货物 2：物流 3：仓储 4：代理
        String orderNumber = bill.getOrderNumber();
        Date currentTime = new Date();
        switch (orderType) {
            case 0:     //报关
                DeclareOrderEntity declareOrder = declareOrderDao.queryByOrderNumber(orderNumber);
                if (declareOrder != null) {
                    if (bill.getReceivableStatus() == 1) { ////应收款项状态  0：未结清 1：已结清
                        declareOrder.setOrderState(3);
                    } else {
                        declareOrder.setPayState(1);
                    }
                    declareOrderDao.update(declareOrder);
                }
                break;
            case 1:     //货物
                GoodsOrderEntity goodsOrder = goodsOrderDao.getListByOrderNumber(orderNumber);
                if (goodsOrder != null) {
                    //付款
                    String orderStatus = goodsOrder.getOrderStatus();
// pre: '待审核', ep: '审核通过', af: '审核失败', tbc: '待确认', ctom: '待卖家取消', cto: '订单取消',  tbp: '待支付',
// ap: '已付款',  tbd: '待安排', tbpu: '待提货',  s: '已发货', pr: '待收货',  tc: '交易完成', agr: '代理',
                    if (orderStatus.equals("tbp")) {
                        goodsOrder.setOrderStatus("ap");
                    }
                    //例如：首付后没收款 再付款
                    boolean secondPay = false;
                    int count = 0;
                    List<PaymentRecordEntity> paymentRecords = bill.getPaymentRecords();
                    for (PaymentRecordEntity pay : paymentRecords) {
                        if (pay.getPayStatus() == 1 && pay.getConfirmStatus() == 0) { //已付款 未收款
                           count++;
                        }
                    }
                    // =1付款再付款 不改变订单状态 为啥不是2
                    if (count != 1) { // !=1 改变订单状态
                        if (orderStatus.equals("ap")) {
                            goodsOrder.setOrderStatus("tbd");
                        }
                    }
                    goodsOrderDao.update(goodsOrder);
                }
                break;
            case 2:     //物流
                LogisticsOrderEntity logisticsOrder = logisticsOrderDao.queryByOrderNumber(orderNumber);
                if (logisticsOrder != null) {
                    if (bill.getReceivableStatus() == 1) {
                        logisticsOrder.setOrderStatus(3);
                    } else {
                        logisticsOrder.setPassStatus(1);
                        logisticsOrder.setOrderStatus(2);
                    }
                    logisticsOrderDao.update(logisticsOrder);
                }
                break;
            case 3:     //仓储
                //payStatus状态: 0:仓储费未结清、1:仓储费已结清
                //passStatus订单状态 7：交易完成
                //在controller里面写
                break;
            case 4:     //代理
                AgentOrderEntity agentOrder = agentOrderDao.queryByOrderNum(orderNumber);
                if (agentOrder != null) {
                    if (bill.getReceivableStatus() == 1) {
                        agentOrder.setOrderStatus(3); //交易完成
                        //增加买卖双方信用分数
                        MerchantCompanyEntity company1 = merchantCompanyDao.queryObject(bill.getPayerId());
                        MerchantCompanyEntity company2 = merchantCompanyDao.queryObject(bill.getPayeeId());
                        merchantCompanyDao.updateCreditType(CreditType.COMPLETEORDER, company1);
                        merchantCompanyDao.updateCreditType(CreditType.COMPLETEORDER, company2);
                        //设置合同订单到期日期
                        agentContractDao.setExpireDateByOrderNumber(orderNumber);
                    } else {
                        agentOrder.setPayStatus(2);
                        agentOrder.setPayTime(currentTime);
                    }
                    agentOrderDao.update(agentOrder);
                }
                break;
        }
    }

    //余额支付 -- 平台打款给卖家
    @Override
    public void masterBalancePayment(List<PaymentRecordEntity> masterRecords, PaymentEntity payment) {
        BigDecimal amountToPay = payment.getAmountToPay();
        String remarks = payment.getRemarks();
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        Date currentTime = new Date();
        String payeeLegalPersonName = null;
        String payerLegalPersonName = null;
        String payeeAccountNumber = null;
        String payerAccountNumber = null;

        PaymentRecordEntity masterRecord = masterRecords.get(0);
        Integer masterBillId = masterRecord.getBillId(); //平台账单id
        BillEntity masterBill = billDao.queryObject(masterBillId);
        Integer payeeId = masterBill.getPayeeId();  //收款方公司id
        Integer payerId = masterBill.getPayerId(); //平台的公司id

        MerchantCompanyEntity payerCompany = merchantCompanyDao.queryObject(payerId);
        MerchantCompanyEntity payeeCompany = merchantCompanyDao.queryObject(payeeId);
        if (payerCompany != null) {
            payerLegalPersonName = payerCompany.getLegalPersonName();
        }
        if (payeeCompany != null) {
            payeeLegalPersonName = payeeCompany.getLegalPersonName();
        }
        ////更改账户余额 平台的 、卖家的
        MyAccountEntity payerAccount = myAccountDao.queryObjectByCompanyId(payerId);
        MyAccountEntity payeeAccount = myAccountDao.queryObjectByCompanyId(payeeId);
        //扣减平台账户余额
        if (payerAccount != null) {
            payerAccountNumber = payerAccount.getAccount();
            BigDecimal balance = payerAccount.getBalance();
            BigDecimal subtract = balance.subtract(amountToPay);
            payerAccount.setBalance(subtract);
            myAccountDao.update(payerAccount);
        }
        //增加卖家账户余额
        if (payeeAccount != null) {
            payeeAccountNumber = payeeAccount.getAccount();
            BigDecimal balance = payeeAccount.getBalance();
            BigDecimal add = balance.add(amountToPay);
            payeeAccount.setBalance(add);
            myAccountDao.update(payeeAccount);
        }

        ////更改账单、款项记录 （平台的 、卖家的）
        //更改平台账单
        masterBill.setAmountPaid(amountToPay);
        masterBill.setRemainingAmountPayable(BigDecimal.ZERO);
        masterBill.setPayableStatus(1);
        masterBill.setAmountReceived(amountToPay);
        masterBill.setRemainingAmountReceivable(BigDecimal.ZERO);
        masterBill.setReceivableStatus(1);
        masterBill.setPaymentRemarks(remarks);
        masterBill.setPaymentTime(currentTime);
        masterBill.setUpdateBy(user.getUserId().intValue());
        masterBill.setUpdateTime(currentTime);
        billDao.update(masterBill);
        //更改平台款项记录 (平台勾选的一条款项记录）
        masterRecord.setPayStatus(1);
        masterRecord.setPayMethod(1);
        masterRecord.setPayerName(payerLegalPersonName);
        masterRecord.setPayerNumber(payerAccountNumber);
        masterRecord.setPayeeName(payeeLegalPersonName);
        masterRecord.setPayeeNumber(payeeAccountNumber);
        masterRecord.setPaymentRemarks(remarks);
        masterRecord.setPaymentTime(currentTime);
        masterRecord.setPaymentOperator(user.getUsername());
        masterRecord.setUpdateBy(user.getUserId().intValue());
        masterRecord.setUpdateTime(currentTime);
        paymentRecordDao.update(masterRecord);
        //更改卖家账单
        String orderNumber = masterBill.getOrderNumber();//订单编号
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderNumber", orderNumber);
        map.put("payerId", payerId);   //平台的公司id
        BillEntity bill = billDao.queryByOrderNumberAndPayerId(map);//排除 付款公司为平台 的账单 即为卖家账单(普通账单)
        if (bill != null) {
            BigDecimal received = bill.getAmountReceived().add(amountToPay);//已收金额
            BigDecimal remaining = bill.getAmountReceivable().subtract(received); //剩余应收金额
            int i = remaining.compareTo(BigDecimal.ZERO); //剩余应收金额 与 0 比较
            if (i == 1) {   //剩余应收金额 > 0
                bill.setRemainingAmountReceivable(remaining);
                bill.setReceivableStatus(0);
            } else {     //剩余应收金额 <= 0
                bill.setRemainingAmountReceivable(BigDecimal.ZERO);
                bill.setReceivableStatus(1);
            }
            bill.setAmountReceived(received);
            bill.setUpdateBy(user.getUserId().intValue());
            bill.setUpdateTime(currentTime);
            billDao.update(bill);
            //卖家款项记录 没什么需要更改的
        }
    }

    //线下转账
    @Override
    public void offlinePay(List<PaymentRecordEntity> paymentRecords, PaymentEntity payment) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        String remarks = payment.getRemarks();
        String payerNumber = payment.getPayerNumber();//付款账号
        String payeeNumber = payment.getPayeeNumber();//收款帐号
        String operator = payment.getOperator();
        BigDecimal amountPaid = payment.getAmountPaid();
        Long unit = payment.getUnit();
        Date paymentTime = payment.getPaymentTime();
        Date currentTime = new Date();
        if (paymentTime == null) {      //app付款时间没传
            paymentTime = currentTime;
        }

        //billId --- bill ---payeeId、payerId
        Integer billId = paymentRecords.get(0).getBillId();
        BillEntity bill = billDao.queryObject(billId);
        Integer payerId = bill.getPayerId();
        Integer payeeId = bill.getPayeeId();
        MerchantCompanyEntity payerCompany = merchantCompanyDao.queryObject(payerId);
        MerchantCompanyEntity payeeCompany = merchantCompanyDao.queryObject(payeeId);

        ////更改应付账单
        BigDecimal paid = bill.getAmountPaid().add(amountPaid);   //已付金额
        BigDecimal remaining = bill.getAmountPayable().subtract(paid);   //剩余应付金额
        int i = remaining.compareTo(BigDecimal.ZERO);   //剩余应付金额与0比较
        if (i == 1) {   //剩余应付金额>0
            bill.setRemainingAmountPayable(remaining);
            bill.setPayableStatus(0);    //未结清
        } else {     //剩余应付金额<=0
            bill.setRemainingAmountPayable(BigDecimal.ZERO);
            bill.setPayableStatus(1);    //已结清
        }
        bill.setAmountPaid(paid);
        bill.setPaymentTime(paymentTime);
        bill.setPaymentRemarks(remarks);
        bill.setUpdateBy(user.getUserId().intValue());
        bill.setUpdateTime(currentTime);
        billDao.update(bill);

        ////生成流水
        String serialNumber = numberRuleService.getCodeNumber("ACCD");//付款流水号
        IncomeExpenditureDetailEntity incomeExpenditureDetail = new IncomeExpenditureDetailEntity();
        incomeExpenditureDetail.setLinkOrder(1);//是否关联订单 0：不关联 1：关联
        incomeExpenditureDetail.setOrderNumber(bill.getOrderNumber());
        incomeExpenditureDetail.setPaymentType(0);
        incomeExpenditureDetail.setSerialNumber(serialNumber);
        incomeExpenditureDetail.setExpenditur(amountPaid);
        incomeExpenditureDetail.setPayerNumber(payerNumber);//
        incomeExpenditureDetail.setPayeeNumber(payeeNumber);//
        incomeExpenditureDetail.setPayeeId(payeeId.longValue());
        incomeExpenditureDetail.setOperator(operator);
        incomeExpenditureDetail.setPayTime(currentTime);
        incomeExpenditureDetail.setRemarks(remarks);
        incomeExpenditureDetail.setCompanyId(user.getCompanyId());
        incomeExpenditureDetail.setCreateBy(user.getUserId());
        incomeExpenditureDetail.setCreatedTime(currentTime);
        incomeExpenditureDetail.setUnit(unit);
        incomeExpenditureDetailDao.save(incomeExpenditureDetail);

        ////更改款项记录
        for (PaymentRecordEntity record : paymentRecords) {
            record.setPayStatus(1);     //已付款
            record.setPayMethod(0);     //线下转账
            record.setConfirmStatus(0);     //待确认
            record.setPayerNumber(payerNumber);
            record.setPayeeNumber(payeeNumber);
//            record.setPayeeName(payeeLegalPersonName);
//            record.setPayerName(payerLegalPersonName);
//            record.setPayeeBank(payeeOpeningBank);
//            record.setPayerBank(payerOpeningBank);
            record.setPaymentTime(paymentTime);
            record.setPaymentRemarks(remarks);
            record.setPaymentOperator(operator);
            record.setPayerId(user.getCompanyId());//付款公司id
            record.setUpdateBy(user.getUserId().intValue());
            record.setUpdateTime(currentTime);
            record.setSerialNumber(serialNumber);//付款流水号
            paymentRecordDao.update(record);
        }
        ////更改各类订单的状态
        updateStatus(bill);
//        if (payerId != 1) {
//            ////更改各类订单的状态
//            updateStatus(bill);
//        }//payerId=1 付款方为平台 则无需更改订单状态

        ////保存打款凭证
        List<ImageEntity> images = payment.getImages();
        for (ImageEntity image : images) {
            if (StringUtils.isBlank(image.getPicName())) {
                image.setPicName(SplitUtil.splitPicName(image.getPicUrl()));
            }
            image.setType("offline_pay");   //"bill"
            image.setCode(incomeExpenditureDetail.getId());  //billId
            image.setCreateBy(user.getUserId().toString());
            image.setCreateDate(currentTime);
        }
        imageService.saveList(images);
        // 付款通知save
        if (null != payerCompany && null != payeeCompany)
            ApiDemo4Java.sendPay(bill.getOrderNumber(), payerCompany.getCompanyName(), payeeCompany.getMobile());
    }

    //确认收款
    @Override
    public void gatheringMethod(List<PaymentRecordEntity> paymentRecords, Gathering gathering) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        BigDecimal totalAmountCollected = gathering.getAmountCollected();
        String remarks = gathering.getRemarks();
        Date currentTime = new Date();
        //更改收款记录
        for (PaymentRecordEntity record : paymentRecords) {
            record.setConfirmStatus(1);     //已确认
            record.setReceivingTime(currentTime);
//            record.setAmountCollected(totalAmountCollected);
            record.setReceivingRemarks(remarks);
            record.setReceivingOperator(user.getUsername());
            record.setUpdateBy(user.getUserId().intValue());
            record.setUpdateTime(currentTime);
            paymentRecordDao.update(record);
        }
        //更改应收账单
        Integer billId = paymentRecords.get(0).getBillId();
        BillEntity bill = billDao.queryObject(billId);
        if (bill != null) {
            BigDecimal received = bill.getAmountReceived().add(totalAmountCollected);   //已收金额
            BigDecimal remaining = bill.getAmountReceivable().subtract(received);   //剩余应收金额
            int i = remaining.compareTo(BigDecimal.ZERO);   //剩余应收金额与0比较
            if (i == 1) {   //剩余应收金额>0
                bill.setRemainingAmountReceivable(remaining);
                bill.setReceivableStatus(0);    //未结清
            } else {     //剩余应收金额<=0
                bill.setRemainingAmountReceivable(BigDecimal.ZERO);
                bill.setReceivableStatus(1);    //已结清
//                updateStatus(bill);    //款项已清更改订单状态
            }
            bill.setAmountReceived(received);
            bill.setReceivingTime(currentTime);
            bill.setReceivingRemarks(remarks);
            bill.setUpdateBy(user.getUserId().intValue());
            bill.setUpdateTime(currentTime);
            billDao.update(bill);
        }
        ////生成流水
        IncomeExpenditureDetailEntity incomeExpenditureDetail = new IncomeExpenditureDetailEntity();
        incomeExpenditureDetail.setLinkOrder(1);//是否关联订单 0：不关联 1：关联
        incomeExpenditureDetail.setOrderNumber(bill.getOrderNumber());
        incomeExpenditureDetail.setPaymentType(0);
        incomeExpenditureDetail.setIncome(totalAmountCollected);
        incomeExpenditureDetail.setPayeeId(bill.getPayeeId().longValue());
        incomeExpenditureDetail.setRemarks(remarks);
        incomeExpenditureDetail.setCompanyId(user.getCompanyId());
        incomeExpenditureDetail.setCreateBy(user.getUserId());
        incomeExpenditureDetail.setCreatedTime(currentTime);
        incomeExpenditureDetail.setUnit(bill.getUnit());

        String serialNumber = paymentRecords.get(0).getSerialNumber();//款项记录带来的流水号
        incomeExpenditureDetail.setSerialNumber(serialNumber);
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", serialNumber);
        map.put("pay", 1);
        IncomeExpenditureDetailEntity payDetail = incomeExpenditureDetailDao.queryBySerialNumber(map);//付款流水
        if (payDetail.getPayerNumber() != null) {
            incomeExpenditureDetail.setPayerNumber(payDetail.getPayerNumber());
        }
        if (payDetail.getPayeeNumber() != null) {
            incomeExpenditureDetail.setPayeeNumber(payDetail.getPayeeNumber());
        }
        if (payDetail.getOperator() != null) {
            incomeExpenditureDetail.setOperator(payDetail.getOperator());
        }
        if (payDetail.getPayTime() != null) {
            incomeExpenditureDetail.setPayTime(payDetail.getPayTime());
        }
        incomeExpenditureDetailDao.save(incomeExpenditureDetail);
        //图片不塞了 去查付款流水的图片展示

        ////更改各类订单的状态
        updateStatus(bill);

    }

    ////账期账单
    @Override
    public List<BillEntity> queryCreditList(Map<String, Object> map) {
        return billDao.queryCreditList(map);
    }

    @Override
    public int queryCreditTotal(Map<String, Object> map) {
        return billDao.queryCreditTotal(map);
    }

    //账期审核
    @Override
    public void audit(BillEntity bill) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (bill.getAuditStatus() == 1) {   //审核通过
            MerchantCompanyEntity masterCompany = merchantCompanyDao.queryObject(1);
            //新增一条 **普通账单** 收款方--卖家 付款方--平台
            BillEntity commonBill = new BillEntity();
            commonBill.setBillType(0);   //普通账单
            commonBill.setOrderType(bill.getOrderType());
            commonBill.setOrderNumber(bill.getOrderNumber());
            commonBill.setContractNumber(bill.getContractNumber());
            commonBill.setPayee(bill.getPayee());
            commonBill.setPayeeId(bill.getPayeeId());
            commonBill.setPayerId(1);    //付款公司id 为 平台的公司id
            commonBill.setPayer(masterCompany.getCompanyName());
            commonBill.setAmountPayable(bill.getAmountPayable());
            commonBill.setAmountReceivable(bill.getAmountReceivable());
            commonBill.setAmountPaid(BigDecimal.ZERO);
            commonBill.setAmountReceived(BigDecimal.ZERO);
            commonBill.setRemainingAmountPayable(bill.getRemainingAmountPayable());
            commonBill.setRemainingAmountReceivable(bill.getRemainingAmountReceivable());
            commonBill.setPayableStatus(0);
            commonBill.setReceivableStatus(0);
            commonBill.setCreateBy(user.getUserId().intValue());
            commonBill.setCreatedTime(currentTime);
            billDao.save(commonBill);

            PaymentRecordEntity record = new PaymentRecordEntity();
            record.setBillId(commonBill.getId());
            record.setAmount(commonBill.getAmountPayable());
            record.setDate(sdf.format(currentTime));
            record.setPayStatus(0);
            record.setCreateBy(user.getUserId().intValue());
            record.setCreatedTime(currentTime);
            paymentRecordDao.save(record);
            //更改 账期账单 收款方--平台
            bill.setPayeeId(1);
            bill.setPayee(masterCompany.getCompanyName());
        }
        bill.setUpdateBy(user.getUserId().intValue());
        bill.setUpdateTime(currentTime);
        billDao.update(bill);   //更改账单的审核状态与意见
    }

    //账期账单 余额支付 -- 买家打款给平台
    @Override
    public void balancePayment2(List<PaymentRecordEntity> paymentRecords, PaymentEntity payment) {
        BigDecimal amountToPay = payment.getAmountToPay();
        String remarks = payment.getRemarks();
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        Date currentTime = new Date();
        String payeeLegalPersonName = null;
        String payerLegalPersonName = null;
        String payeeAccountNumber = null;
        String payerAccountNumber = null;
        //billId --- bill ---payeeId、payerId
        Integer billId = paymentRecords.get(0).getBillId();
        BillEntity bill = billDao.queryObject(billId);
        Integer payeeId = bill.getPayeeId();
        Integer payerId = bill.getPayerId();

        ////更改买家 账期 应付账单
        BigDecimal paid = bill.getAmountPaid().add(amountToPay);   //已付金额
        BigDecimal remaining = bill.getAmountPayable().subtract(paid);   //剩余应付金额
        int i = remaining.compareTo(BigDecimal.ZERO);   //剩余应付金额与0比较
        if (i == 1) {   //剩余应付金额>0
            bill.setRemainingAmountPayable(remaining);
            bill.setPayableStatus(0);    //未结清
        } else {     //剩余应付金额<=0
            bill.setRemainingAmountPayable(BigDecimal.ZERO);
            bill.setPayableStatus(1);    //已结清
        }
        BigDecimal received = bill.getAmountReceived().add(amountToPay);    //已收金额
        BigDecimal remaining2 = bill.getAmountReceivable().subtract(received);   //剩余应收金额
        int j = remaining2.compareTo(BigDecimal.ZERO);   //剩余应收金额与0比较
        if (j == 1) {   //剩余应收金额>0
            bill.setRemainingAmountReceivable(remaining2);
            bill.setReceivableStatus(0);    //未结清
        } else {     //剩余应收金额<=0
            bill.setRemainingAmountReceivable(BigDecimal.ZERO);
            bill.setReceivableStatus(1);    //已结清
        }
        bill.setAmountPaid(paid);
        bill.setAmountReceived(received);
        bill.setPaymentTime(currentTime);
        bill.setPaymentRemarks(remarks);
        bill.setUpdateBy(user.getUserId().intValue());
        bill.setUpdateTime(currentTime);
        billDao.update(bill);

        //买家 打款给 平台 （平台的公司id为1）
        //公司 --- 户名
        MerchantCompanyEntity payeeCompany = merchantCompanyDao.queryObject(payeeId);
        MerchantCompanyEntity payerCompany = merchantCompanyDao.queryObject(payerId);
        if (payeeCompany != null) {
            payeeLegalPersonName = payeeCompany.getLegalPersonName();
        }
        if (payerCompany != null) {
            payerLegalPersonName = payerCompany.getLegalPersonName();
        }
        //账户 --- 账号
        MyAccountEntity payeeAccount = myAccountDao.queryObjectByCompanyId(payeeId);
        MyAccountEntity payerAccount = myAccountDao.queryObjectByCompanyId(payerId);
        //给买家用户扣一笔款
        //扣减买家账户余额
        if (payerAccount != null) {
            payerAccountNumber = payerAccount.getAccount();
            BigDecimal balance = payerAccount.getBalance();
            BigDecimal subtract = balance.subtract(amountToPay);
            payerAccount.setBalance(subtract);
            myAccountDao.update(payerAccount);
        }
        //给平台账户打一笔款
        //增加平台账户余额
        if (payeeAccount != null) {
            payeeAccountNumber = payeeAccount.getAccount();
            BigDecimal balance = payeeAccount.getBalance();
            BigDecimal add = balance.add(amountToPay);
            payeeAccount.setBalance(add);
            myAccountDao.update(payeeAccount);
        }

        ////更改买家付款记录
        for (PaymentRecordEntity record : paymentRecords) {
            record.setPayStatus(1);
            record.setPayMethod(1);
            record.setPayeeNumber(payeeAccountNumber);
            record.setPayerNumber(payerAccountNumber);
            record.setPayeeName(payeeLegalPersonName);
            record.setPayerName(payerLegalPersonName);
            record.setPaymentOperator(user.getUsername());
            record.setPaymentTime(currentTime);
            record.setPaymentRemarks(remarks);
            record.setUpdateBy(user.getUserId().intValue());
            record.setUpdateTime(currentTime);
            paymentRecordDao.update(record);
        }

        ////更改各类订单的状态
        updateStatus(bill);
    }

    @Override
    public BillEntity queryByOrderNumber(String orderNumber) {
        return billDao.queryByOrder(orderNumber);
    }

    @Override
    public void updatePayerId(BillEntity bill) {
        billDao.updatePayerId(bill);
    }

}


