package com.wzlue.chain.marketing.entity;

import com.wzlue.chain.company.entity.MerchantCompanyEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 招标表
 * 
 * @author 
 * @email 
 * @date 2018-10-24 15:19:33
 */
public class MarketTenderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//招标id
	private Long id;
	//招标编号
	private String tenderNumber;
	//招标项目名称
	private String tenderName;
	//类型:0,现货；1,期货
	private Integer type;
	//发布时间
	private Date startTime;
	//截止日期
	private Date endTime;
	//提货地点省
	private String province;
	private String provinceName;
	//船期
	private Date schedule;
	//提货地点市
	private String city;
	private String cityName;
	//联系人
	private String contact;
	//联系电话
	private String telephone;
	//邮箱
	private String email;
	//状态 0:上架,1:下架
	private Integer status;
	//竞标人数
	private Integer count;
	//报名要求
	private String demand;
	//招标描述
	private String describe;
	//公司id
	private Integer companyId;
	//拍卖公司名称
	private String companyName;
	//部门id
	private Integer deptId;
	//创建人(用户id)
	private Long createBy;
	//授权用户id
	private Integer authorizesId;
	//创建日期
	private Date createDate;
	//修改人
	private Long updateBy;
	//修改日期
	private Date updateDate;
	//删除标识 0：未删除 1：删除
	private Integer delFlag;
	//可否正常招标状态 0：可招标 1：不可招标
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	private MerchantCompanyEntity companyEntity;

	public MerchantCompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(MerchantCompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	private List<MarketTenderRecordEntity> marketTenderRecordEntityList;

	public List<MarketTenderRecordEntity> getMarketTenderRecordEntityList() {
		return marketTenderRecordEntityList;
	}

	public void setMarketTenderRecordEntityList(List<MarketTenderRecordEntity> marketTenderRecordEntityList) {
		this.marketTenderRecordEntityList = marketTenderRecordEntityList;
	}

	public Date getSchedule() {
		return schedule;
	}

	public void setSchedule(Date schedule) {
		this.schedule = schedule;
	}

	/**
	 * 设置：招标id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：招标id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：招标编号
	 */
	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}
	/**
	 * 获取：招标编号
	 */
	public String getTenderNumber() {
		return tenderNumber;
	}
	/**
	 * 设置：招标项目名称
	 */
	public void setTenderName(String tenderName) {
		this.tenderName = tenderName;
	}
	/**
	 * 获取：招标项目名称
	 */
	public String getTenderName() {
		return tenderName;
	}
	/**
	 * 设置：类型:0,现货；1,期货
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型:0,现货；1,期货
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：发布时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：发布时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：截止日期
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：截止日期
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：提货地点省
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：提货地点省
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：提货地点市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：提货地点市
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：联系人
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * 获取：联系人
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * 设置：联系电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：状态 0:上架,1:下架
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0:上架,1:下架
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：竞标人数
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * 获取：竞标人数
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * 设置：报名要求
	 */
	public void setDemand(String demand) {
		this.demand = demand;
	}
	/**
	 * 获取：报名要求
	 */
	public String getDemand() {
		return demand;
	}
	/**
	 * 设置：招标描述
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	/**
	 * 获取：招标描述
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * 设置：公司id
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	/**
	 * 获取：公司id
	 */
	public Integer getCompanyId() {
		return companyId;
	}
	/**
	 * 设置：拍卖公司名称
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * 获取：拍卖公司名称
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * 设置：部门id
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门id
	 */
	public Integer getDeptId() {
		return deptId;
	}
	/**
	 * 设置：创建人(用户id)
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人(用户id)
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：授权用户id
	 */
	public void setAuthorizesId(Integer authorizesId) {
		this.authorizesId = authorizesId;
	}
	/**
	 * 获取：授权用户id
	 */
	public Integer getAuthorizesId() {
		return authorizesId;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：修改人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：修改日期
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：修改日期
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * 设置：删除标识 0：未删除 1：删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标识 0：未删除 1：删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
