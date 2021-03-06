package com.wzlue.chain.contract.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wzlue.chain.order.entity.GoodsOrderEntity;
import com.wzlue.chain.sys.entity.ImageEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 货物合同
 *
 * @author hjw
 * @email love@und.win
 * @date 2018-08-28 13:28:06
 */
public class GoodsContractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    private Long id;
    //合同编号
    private String contractNumber;
    /**
     * 所属方关联字段
     */
    private String owner;
    //订单编号
    private String orderNumber;
    //合同名称
    private String contractName;
    //甲方名称
    private String partyAName;
    //乙方名称
    private String partyBName;
    //甲方名称
    private String partyAId;
    //乙方名称
    private String partyBId;
    //付款方式
    private Integer paymentMethod;
    //合同金额
    private Double contractAmount;
    //存放位置
    private String storageLocation;
    //签订日期
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfSigning;
    //到期日期
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expireDate;
    //备注
    private String remarks;
    //权限部门id
    private Integer deptId;
    //创建人
    private Integer createBy;
    //创建时间
    private Date createTime;
    //修改人
    private Integer modityBy;
    //修改时间
    private Date modityTime;

    private List<ImageEntity> file;

    public Integer getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Integer automatic) {
        this.automatic = automatic;
    }

    //是否自动生成
    private Integer automatic;

    //关联订单表
    private GoodsOrderEntity goodsOrder;

    public GoodsOrderEntity getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(GoodsOrderEntity goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    /**
     * 设置：id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：合同编号
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * 获取：合同编号
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * 设置：订单编号
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 获取：订单编号
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置：合同名称
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**
     * 获取：合同名称
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * 设置：甲方名称
     */
    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    /**
     * 获取：甲方名称
     */
    public String getPartyAName() {
        return partyAName;
    }

    /**
     * 设置：乙方名称
     */
    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

    /**
     * 获取：乙方名称
     */
    public String getPartyBName() {
        return partyBName;
    }

    /**
     * 设置：付款方式
     */
    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 获取：付款方式
     */
    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 设置：合同金额
     */
    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**
     * 获取：合同金额
     */
    public Double getContractAmount() {
        return contractAmount;
    }

    /**
     * 设置：存放位置
     */
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    /**
     * 获取：存放位置
     */
    public String getStorageLocation() {
        return storageLocation;
    }

    /**
     * 设置：签订日期
     */
    public void setDateOfSigning(Date dateOfSigning) {
        this.dateOfSigning = dateOfSigning;
    }

    /**
     * 获取：签订日期
     */
    public Date getDateOfSigning() {
        return dateOfSigning;
    }

    /**
     * 设置：到期日期
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * 获取：到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * 设置：备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取：备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置：权限部门id
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取：权限部门id
     */
    public Integer getDeptId() {
        return deptId;
    }

    /**
     * 设置：创建人
     */
    public void setCreateBy(Integer createdBy) {
        this.createBy = createBy;
    }

    /**
     * 获取：创建人
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createdTime) {
        this.createTime = createdTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置：修改人
     */
    public void setModityBy(Integer modityBy) {
        this.modityBy = modityBy;
    }

    /**
     * 获取：修改人
     */
    public Integer getModityBy() {
        return modityBy;
    }

    /**
     * 设置：修改时间
     */
    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getModityTime() {
        return modityTime;
    }

    public List<ImageEntity> getFile() {
        return file;
    }

    public void setFile(List<ImageEntity> file) {
        this.file = file;
    }

    public String getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(String partyAId) {
        this.partyAId = partyAId;
    }

    public String getPartyBId() {
        return partyBId;
    }

    public void setPartyBId(String partyBId) {
        this.partyBId = partyBId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
