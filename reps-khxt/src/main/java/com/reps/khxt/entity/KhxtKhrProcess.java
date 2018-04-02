package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.reps.core.orm.IdEntity;

/**
 * 考核人打分表
 * 
 * @author ：Alex
 * @date 2018年3月29日
 */
@Entity
@Table(name = "reps_khxt_khr_process")
public class KhxtKhrProcess extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7242874814747880345L;

	/** 被考核人级别ID */
	@Column(name = "khr_person_id", length = 32)
	private String khrPersonId;

	/** 考核表ID */
	@Column(name = "sheet_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String sheetId;

	/**
	 * 考核表ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sheet_id")
	private KhxtAppraiseSheet AppraiseSheet;

	/**
	 * 打分情况 0：未完成打分 1：已完成打分
	 */
	@Column(name = "status")
	private Integer status;


	public KhxtAppraiseSheet getAppraiseSheet() {
		return AppraiseSheet;
	}

	public void setAppraiseSheet(KhxtAppraiseSheet appraiseSheet) {
		AppraiseSheet = appraiseSheet;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKhrPersonId() {
		return khrPersonId;
	}

	public void setKhrPersonId(String khrPersonId) {
		this.khrPersonId = khrPersonId;
	}

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

}
