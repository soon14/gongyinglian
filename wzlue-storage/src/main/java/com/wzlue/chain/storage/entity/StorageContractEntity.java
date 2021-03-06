package com.wzlue.chain.storage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wzlue.chain.agent.entity.ContractAnnexEntity;
import com.wzlue.chain.bill.service.JsonDateDeserializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * 仓库合同信息表
 * 
 * @author 
 * @email 
 * @date 2018-09-28 15:02:40
 */
public class StorageContractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键,id
	private Long id;
	//合同编号
	private String contractNumber;
	//订单编号
	private String orderNumber;
	//合同创建来源 1.订单创建 2.自主录入
	private Integer dataSource;
	//甲方企业名称
	private String customsCopName;
	//乙方企业名称
	private String merchantCopName;
	//付款方式
	private Integer payMethod;
	//合同金额
	private BigDecimal contractAmount;
	//合同存放位置
	private String storageLocation;
	//签订日期
	private Date signingTime;
	//到期日期
	private Date expireDate;
	//备注
	private String remarks;
	//创建人
	private Long createBy;
	//创建时间
	private Date createTime;
	//修改用户id
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//客户公司id
	private Long cusCompanyId;
	//商家公司id
	private Long merCompanyId;
	//合同名称
	private String contractName;
	//合同归属公司id
	private Long companyId;
	//合同文件
	private List<ContractAnnexEntity> file;

	//关联订单
	private OrderEntity storageOrder;

	public OrderEntity getStorageOrder() {
		return storageOrder;
	}

	public void setStorageOrder(OrderEntity storageOrder) {
		this.storageOrder = storageOrder;
	}

	/**
	 * 设置：自增主键,id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键,id
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
	 * 设置：合同创建来源 1.订单创建 2.自主录入
	 */
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 获取：合同创建来源 1.订单创建 2.自主录入
	 */
	public Integer getDataSource() {
		return dataSource;
	}
	/**
	 * 设置：甲方企业名称
	 */
	public void setCustomsCopName(String customsCopName) {
		this.customsCopName = customsCopName;
	}
	/**
	 * 获取：甲方企业名称
	 */
	public String getCustomsCopName() {
		return customsCopName;
	}
	/**
	 * 设置：乙方企业名称
	 */
	public void setMerchantCopName(String merchantCopName) {
		this.merchantCopName = merchantCopName;
	}
	/**
	 * 获取：乙方企业名称
	 */
	public String getMerchantCopName() {
		return merchantCopName;
	}
	/**
	 * 设置：付款方式
	 */
	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * 获取：付款方式
	 */
	public Integer getPayMethod() {
		return payMethod;
	}
	/**
	 * 设置：合同金额
	 */
	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}
	/**
	 * 获取：合同金额
	 */
	public BigDecimal getContractAmount() {
		return contractAmount;
	}
	/**
	 * 设置：合同存放位置
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}
	/**
	 * 获取：合同存放位置
	 */
	public String getStorageLocation() {
		return storageLocation;
	}
	/**
	 * 设置：签订日期
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setSigningTime(Date signingTime) {
		this.signingTime = signingTime;
	}
	/**
	 * 获取：签订日期
	 */
    @JsonFormat(pattern = "yyyy-MM-dd")
	public Date getSigningTime() {
		return signingTime;
	}
	/**
	 * 设置：到期日期
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
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
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改用户id
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：修改用户id
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：客户公司id
	 */
	public void setCusCompanyId(Long cusCompanyId) {
		this.cusCompanyId = cusCompanyId;
	}
	/**
	 * 获取：客户公司id
	 */
	public Long getCusCompanyId() {
		return cusCompanyId;
	}
	/**
	 * 设置：商家公司id
	 */
	public void setMerCompanyId(Long merCompanyId) {
		this.merCompanyId = merCompanyId;
	}
	/**
	 * 获取：商家公司id
	 */
	public Long getMerCompanyId() {
		return merCompanyId;
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
	 * 设置：合同归属公司id
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	/**
	 * 获取：合同归属公司id
	 */
	public Long getCompanyId() {
		return companyId;
	}

	public List<ContractAnnexEntity> getFile() {
		return file;
	}

	public void setFile(List<ContractAnnexEntity> file) {
		this.file = file;
	}
}
