package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@Column(name = "khr_level_id", length = 32)
	private String khrId;

	/** 考核人级别 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", insertable = false, updatable = false)
	private KhxtLevel KhxtLevel;

	/** 考核人json */
	@Column(name = "khr", length = 500)
	private String khr;

	/** 被考核人级别ID */
	@Column(name = "bkhr_level_id", length = 32)
	private String bkhrId;

	/** 被考核人级别 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", insertable = false, updatable = false)
	private KhxtLevel bKhxtLevel;

	/** 被考核人json */
	@Column(name = "bkhr")
	private String bkhr;

	/** 备注 */
	@Column(name = "remark")
	private String remark;

	/** 是否参与考核 1：参与    2：不参与 */
	@Column(name = "is_enable", length = 200)
	private Short isEnable;

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
		return KhxtLevel;
	}

	public void setKhxtLevel(KhxtLevel khxtLevel) {
		KhxtLevel = khxtLevel;
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

	public KhxtLevel getbKhxtLevel() {
		return bKhxtLevel;
	}

	public void setbKhxtLevel(KhxtLevel bKhxtLevel) {
		this.bKhxtLevel = bKhxtLevel;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
