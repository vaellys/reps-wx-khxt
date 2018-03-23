package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;



/**
 * @ClassName: KhxtLevelWeight
 * @Description: 级别权重
 * @author qianguobing
 * @date 2018年3月18日 上午11:16:49
 */
@Entity
@Table(name = "reps_khxt_level_weight")
public class KhxtLevelWeight extends IdEntity implements Serializable {

	private static final long serialVersionUID = 2543085571438556346L;
	
	/** 权重姓名 */
	@Column(name = "name", length = 60)
	private String name;
	
	/** 年度 */
	@Column(name = "year")
	private Integer year;
	
	/** 级别权重json */
	@Column(name = "weight")
	private String weight;
	
	/** 是否公开打分明细 */
	@Column(name = "visible")
	private Short visible;
	
	@Transient
	@JsonIgnore
	private String weightDisplay;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Short getVisible() {
		return visible;
	}

	public void setVisible(Short visible) {
		this.visible = visible;
	}

	public String getWeightDisplay() {
		return weightDisplay;
	}

	public void setWeightDisplay(String weightDisplay) {
		this.weightDisplay = weightDisplay;
	}
	
}
	
