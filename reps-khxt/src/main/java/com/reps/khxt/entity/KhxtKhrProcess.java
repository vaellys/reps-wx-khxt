package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;
import com.reps.system.entity.Person;

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

	/** 考核人personId */
	@Column(name = "khr_person_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String khrPersonId;
	
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "khr_person_id")
	private Person khrPerson;

	/** 考核表ID */
	@Column(name = "sheet_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String sheetId;

	/**
	 * 考核表ID
	 */
	@JsonIgnore
	@JoinColumn(name = "sheet_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private KhxtAppraiseSheet appraiseSheet;

	/**
	 * 打分情况 0：未完成打分 1：已完成打分
	 */
	@Column(name = "status")
	private Integer status;
	
	@JsonIgnore
	@Transient
	private String organizeName;
	
	@JsonIgnore
	@Transient
	private String beginDate;
	
	@JsonIgnore
	@Transient
	private String endDate;
	
	/**
	 * 是否已完成打分
	 */
	@JsonIgnore
	@Transient
	private boolean checkCompletedMarking;
	
	public KhxtAppraiseSheet getAppraiseSheet() {
		return appraiseSheet;
	}

	public void setAppraiseSheet(KhxtAppraiseSheet appraiseSheet) {
		this.appraiseSheet = appraiseSheet;
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

	public Person getKhrPerson() {
		return khrPerson;
	}

	public void setKhrPerson(Person khrPerson) {
		this.khrPerson = khrPerson;
	}

	public String getOrganizeName() {
		return organizeName;
	}

	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isCheckCompletedMarking() {
		return checkCompletedMarking;
	}

	public void setCheckCompletedMarking(boolean checkCompletedMarking) {
		this.checkCompletedMarking = checkCompletedMarking;
	}
	
}
