package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 被考核人合计总分表
 * 
 * @author ：Alex
 * @date 2018年4月19日
 */
@Entity
@Table(name = "reps_khxt_bkhr_points")
public class KhxtBkhrPoints extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7024485451444322636L;

	/** 考核表ID */
	@Column(name = "sheet_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String sheetId;

	/** 考核表 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "sheet_id")
	private KhxtAppraiseSheet appraiseSheet;

	/** 被考核人ID */
	@Column(name = "bkhr_person_id", length = 32, nullable = false)
	private String bkhrPersonId;

	/** 被考核人person */
	/*@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "bkhr_person_id")
	private Person bkhrPerson;*/

	/** 评定总分 */
	@JoinColumn(name = "total_points")
	private float totalPoints;

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	public KhxtAppraiseSheet getAppraiseSheet() {
		return appraiseSheet;
	}

	public void setAppraiseSheet(KhxtAppraiseSheet appraiseSheet) {
		this.appraiseSheet = appraiseSheet;
	}

	public String getBkhrPersonId() {
		return bkhrPersonId;
	}

	public void setBkhrPersonId(String bkhrPersonId) {
		this.bkhrPersonId = bkhrPersonId;
	}

	/*public Person getBkhrPerson() {
		return bkhrPerson;
	}

	public void setBkhrPerson(Person bkhrPerson) {
		this.bkhrPerson = bkhrPerson;
	}*/

	public float getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(float totalPoints) {
		this.totalPoints = totalPoints;
	}

}
