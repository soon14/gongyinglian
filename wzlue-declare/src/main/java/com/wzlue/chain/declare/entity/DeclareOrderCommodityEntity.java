package com.wzlue.chain.declare.entity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 报关订单商品表
 * 
 * @author 
 * @email 
 * @date 2018-08-20 17:24:10
 */
public class DeclareOrderCommodityEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//货物商品id
	private Long id;
	//报关订单id
	private Long declareOrderId;
	//商品id
	private String commodityId;
	//商品名称
	private String commodityName;
	//商品编号
	private String commodityNumber;
	//商品单位
	private String commodityUnit;
	//商品价格
	private BigDecimal commodityPrice;
	//商品数量
	private String commodityCount;
	//商品历史交易数量
	private String commodityTransactionCount;
	//厂号
	private String commodityFactoryNumber;
	//产地
	private String commodityCountry;
	private String commodityCountryName;
	//包装
	private String commodityPacking;
	//商品分类
	private String commodityClassification;
	//重量
	private BigDecimal weight;
	//重量单位
	private String unit;

	public String getCommodityCountryName() {
		return commodityCountryName;
	}

	public void setCommodityCountryName(String commodityCountryName) {
		this.commodityCountryName = commodityCountryName;
	}

	/**
	 * 设置：货物商品id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：货物商品id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：报关订单id
     * @param declareOrderId
     */
	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}
	/**
	 * 获取：报关订单id
	 */
	public Long getDeclareOrderId() {
		return declareOrderId;
	}
	/**
	 * 设置：商品id
	 */
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}
	/**
	 * 获取：商品id
	 */
	public String getCommodityId() {
		return commodityId;
	}
	/**
	 * 设置：商品名称
	 */
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	/**
	 * 获取：商品名称
	 */
	public String getCommodityName() {
		return commodityName;
	}
	/**
	 * 设置：商品编号
	 */
	public void setCommodityNumber(String commodityNumber) {
		this.commodityNumber = commodityNumber;
	}
	/**
	 * 获取：商品编号
	 */
	public String getCommodityNumber() {
		return commodityNumber;
	}
	/**
	 * 设置：商品单位
	 */
	public void setCommodityUnit(String commodityUnit) {
		this.commodityUnit = commodityUnit;
	}
	/**
	 * 获取：商品单位
	 */
	public String getCommodityUnit() {
		return commodityUnit;
	}
	/**
	 * 设置：商品价格
	 */
	public void setCommodityPrice(BigDecimal commodityPrice) {
		this.commodityPrice = commodityPrice;
	}
	/**
	 * 获取：商品价格
	 */
	public BigDecimal getCommodityPrice() {
		return commodityPrice;
	}
	/**
	 * 设置：商品数量
	 */
	public void setCommodityCount(String commodityCount) {
		this.commodityCount = commodityCount;
	}
	/**
	 * 获取：商品数量
	 */
	public String getCommodityCount() {
		return commodityCount;
	}
	/**
	 * 设置：商品历史交易数量
	 */
	public void setCommodityTransactionCount(String commodityTransactionCount) {
		this.commodityTransactionCount = commodityTransactionCount;
	}
	/**
	 * 获取：商品历史交易数量
	 */
	public String getCommodityTransactionCount() {
		return commodityTransactionCount;
	}
	/**
	 * 设置：厂号
	 */
	public void setCommodityFactoryNumber(String commodityFactoryNumber) {
		this.commodityFactoryNumber = commodityFactoryNumber;
	}
	/**
	 * 获取：厂号
	 */
	public String getCommodityFactoryNumber() {
		return commodityFactoryNumber;
	}
	/**
	 * 设置：产地
	 */
	public void setCommodityCountry(String commodityCountry) {
		this.commodityCountry = commodityCountry;
	}
	/**
	 * 获取：产地
	 */
	public String getCommodityCountry() {
		return commodityCountry;
	}
	/**
	 * 设置：包装
	 */
	public void setCommodityPacking(String commodityPacking) {
		this.commodityPacking = commodityPacking;
	}
	/**
	 * 获取：包装
	 */
	public String getCommodityPacking() {
		return commodityPacking;
	}
	/**
	 * 设置：商品分类
	 */
	public void setCommodityClassification(String commodityClassification) {
		this.commodityClassification = commodityClassification;
	}
	/**
	 * 获取：商品分类
	 */
	public String getCommodityClassification() {
		return commodityClassification;
	}
	/**
	 * 设置：重量
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	/**
	 * 获取：重量
	 */
	public BigDecimal getWeight() {
		return weight;
	}
	/**
	 * 设置：重量单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * 获取：重量单位
	 */
	public String getUnit() {
		return unit;
	}
}
