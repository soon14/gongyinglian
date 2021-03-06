package com.wzlue.chain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 物流求购
 * 
 * @author pengyong
 * @email sunlightcs@gmail.com
 * @date 2018-08-21 16:17:43
 */
public class LogisticsDemandEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//求购id
	private Long id;
	//标题名称
	private String title;
	//运输货物
	private String goods;
	//重量
	private BigDecimal height;
	//单位 0:吨,1:千克
	private Long unit;
	private String unitName;
	//运输分类 0:船舶;1:车辆,2:航空,3:火车
	private Integer categoryId;
	private String categoryName;

	//物流类型 0:国内物流,1:国际物流
	private Integer type;
	private String typeName;
	//出发地省
	private String provinceStart;
	private String provinceStartName;
	//出发地市
	private String cityStart;
	private String cityStartName;
	//区
	private String area;
	//目的地省
	private String provinceEnd;
	private String provinceEndName;
	//目的地市
	private String cityEnd;
	private String cityEndName;
	//详细描述
	private String describe;
	//公司id
	private Integer companyId;
	//部门id
	private Integer deptId;
	//创建人
	private Long createBy;
	//授权用户id
	private Integer authorizesId;
	//创建日期
	private Date creatDate;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateDate;
	//删除标识 0：未删除 1：删除
	private Integer delFlag;

	public String getProvinceStartName() {
		return provinceStartName;
	}

	public void setProvinceStartName(String provinceStartName) {
		this.provinceStartName = provinceStartName;
	}

	public String getProvinceEndName() {
		return provinceEndName;
	}

	public void setProvinceEndName(String provinceEndName) {
		this.provinceEndName = provinceEndName;
	}

	public String getCityStartName() {
		return cityStartName;
	}

	public void setCityStartName(String cityStartName) {
		this.cityStartName = cityStartName;
	}

	public String getCityEndName() {
		return cityEndName;
	}

	public void setCityEndName(String cityEndName) {
		this.cityEndName = cityEndName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getAuthorizesId() {
		return authorizesId;
	}

	public void setAuthorizesId(Integer authorizesId) {
		this.authorizesId = authorizesId;
	}

	/**
	 * 设置：求购id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：求购id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：标题名称
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题名称
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：运输货物
	 */
	public void setGoods(String goods) {
		this.goods = goods;
	}
	/**
	 * 获取：运输货物
	 */
	public String getGoods() {
		return goods;
	}
	/**
	 * 设置：重量
	 */
	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	/**
	 * 获取：重量
	 */
	public BigDecimal getHeight() {
		return height;
	}
	/**
	 * 设置：单位 0:吨,1:千克
	 */
	public void setUnit(Long unit) {
		this.unit = unit;
	}
	/**
	 * 获取：单位 0:吨,1:千克
	 */
	public Long getUnit() {
		return unit;
	}
	/**
	 * 设置：运输分类 0:船舶;1:车辆,2:航空,3:火车
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * 获取：运输分类 0:船舶;1:车辆,2:航空,3:火车
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	/**
	 * 设置：物流类型 0:国内物流,1:国际物流
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：物流类型 0:国内物流,1:国际物流
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：出发地省
	 */
	public void setProvinceStart(String provinceStart) {
		this.provinceStart = provinceStart;
	}
	/**
	 * 获取：出发地省
	 */
	public String getProvinceStart() {
		return provinceStart;
	}
	/**
	 * 设置：出发地市
	 */
	public void setCityStart(String cityStart) {
		this.cityStart = cityStart;
	}
	/**
	 * 获取：出发地市
	 */
	public String getCityStart() {
		return cityStart;
	}
	/**
	 * 设置：区
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 获取：区
	 */
	public String getArea() {
		return area;
	}
	/**
	 * 设置：目的地省
	 */
	public void setProvinceEnd(String provinceEnd) {
		this.provinceEnd = provinceEnd;
	}
	/**
	 * 获取：目的地省
	 */
	public String getProvinceEnd() {
		return provinceEnd;
	}
	/**
	 * 设置：目的地市
	 */
	public void setCityEnd(String cityEnd) {
		this.cityEnd = cityEnd;
	}
	/**
	 * 获取：目的地市
	 */
	public String getCityEnd() {
		return cityEnd;
	}
	/**
	 * 设置：详细描述
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	/**
	 * 获取：详细描述
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * 设置：公司id
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
	 * 设置：创建人id
	 */
	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * 设置：创建日期
	 */
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreatDate() {
		return creatDate;
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
	 * 设置：修改时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：修改时间
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
