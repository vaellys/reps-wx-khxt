package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 考核分组实体类
 * 
 * @author ：Alex
 * @date 2018年3月19日
 */
@Entity
@Table(name = "reps_khxt_group")
public class KhxtGroup extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2505956480058026535L;
	/** 分组名称 */
	@Column(name = "name", length = 50)
	private String name;

	/** 考核人级别ID */
	@Column(name = "khr_level_id", length = 32, nullable = false, insertable = false, updatable = false)
	private String khrId;

	/** 考核人级别 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "khr_level_id")
	private KhxtLevel khxtLevel;

	/** 考核人json */
	@Column(name = "khr", length = 500)
	private String khr;

	/** 被考核人级别ID */
	@Column(name = "bkhr_level_id", length = 32, nullable = false, insertable = false, updatable = false)
	private String bkhrId;

	/** 被考核人级别 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "bkhr_level_id")
	private KhxtLevel bkhxtLevel;

	/** 被考核人json */
	@Column(name = "bkhr")
	private String bkhr;

	/** 备注 */
	@Column(name = "remark")
	private String remark;

	/** 是否参与考核 1：参与 2：不参与 */
	@Column(name = "is_enable", length = 200)
	private Short isEnable;

	@Transient
	@JsonIgnore
	private String khrIds;

	@Transient
	@JsonIgnore
	private String bkhrIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKhrId() {
		return khrId;
	}

	public void setKhrId(String khrId) {
		this.khrId = khrId;
	}

	public KhxtLevel getKhxtLevel() {
		return khxtLevel;
	}

	public void setKhxtLevel(KhxtLevel khxtLevel) {
		this.khxtLevel = khxtLevel;
	}

	public KhxtLevel getBkhxtLevel() {
		return bkhxtLevel;
	}

	public void setBkhxtLevel(KhxtLevel bkhxtLevel) {
		this.bkhxtLevel = bkhxtLevel;
	}

	public String getKhr() {
		return khr;
	}

	public void setKhr(String khr) {
		this.khr = khr;
	}

	public String getBkhrId() {
		return bkhrId;
	}

	public void setBkhrId(String bkhrId) {
		this.bkhrId = bkhrId;
	}

	public String getBkhr() {
		return bkhr;
	}

	public void setBkhr(String bkhr) {
		this.bkhr = bkhr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Short getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Short isEnable) {
		this.isEnable = isEnable;
	}

	public String getKhrIds() {
		return khrIds;
	}

	public void setKhrIds(String khrIds) {
		this.khrIds = khrIds;
	}

	public String getBkhrIds() {
		return bkhrIds;
	}

	public void setBkhrIds(String bkhrIds) {
		this.bkhrIds = bkhrIds;
	}

}
