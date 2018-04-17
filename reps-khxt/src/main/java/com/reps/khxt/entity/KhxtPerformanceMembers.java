package com.reps.khxt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;
import com.reps.system.entity.Person;

/**
 * 
 * @ClassName: KhxtPerformanceMembers
 * @Description: 考核人员名单
 * @author qianguobing
 * @date 2018年3月24日 下午2:02:29
 */
@Entity
@Table(name = "reps_khxt_performance_members")
public class KhxtPerformanceMembers extends IdEntity implements Serializable {

	private static final long serialVersionUID = -4949689189823390197L;

	/** 考核表ID */
	@Column(name = "sheet_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String sheetId;

	/** 考核表 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "sheet_id")
	private KhxtAppraiseSheet appraiseSheet;

	/** 考核人ID */
	@Column(name = "khr_person_id", length = 32, nullable = false, insertable = false, updatable = false)
	private String khrPersonId;

	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "khr_person_id")
	private Person khrPerson;

	/** 被考核人ID */
	@Column(name = "bkhr_person_id", length = 32, nullable = false, insertable = false, updatable = false)
	private String bkhrPersonId;

	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "bkhr_person_id")
	private Person bkhrPerson;

	/** 评定总分 */
	@Column(name = "total_points")
	private Double totalPoints;

	/** 评定时间 */
	@Column(name = "appraise_time")
	private Date appraiseTime;

	/** 评定状态 0:未上报-1：已上报-2：已打分 */
	@Column(name = "status")
	private Short status;

	@JsonIgnore
	@OneToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private List<KhxtPerformancePoint> performancePoints;

	@JsonIgnore
	@Transient
	private String personOrganize;

	public String getPersonOrganize() {
		return personOrganize;
	}

	public void setPersonOrganize(String personOrganize) {
		this.personOrganize = personOrganize;
	}

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

	public String getKhrPersonId() {
		return khrPersonId;
	}

	public void setKhrPersonId(String khrPersonId) {
		this.khrPersonId = khrPersonId;
	}

	public String getBkhrPersonId() {
		return bkhrPersonId;
	}

	public void setBkhrPersonId(String bkhrPersonId) {
		this.bkhrPersonId = bkhrPersonId;
	}

	public Double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Date getAppraiseTime() {
		return appraiseTime;
	}

	public void setAppraiseTime(Date appraiseTime) {
		this.appraiseTime = appraiseTime;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Person getBkhrPerson() {
		return bkhrPerson;
	}

	public void setBkhrPerson(Person bkhrPerson) {
		this.bkhrPerson = bkhrPerson;
	}

	public Person getKhrPerson() {
		return khrPerson;
	}

	public void setKhrPerson(Person khrPerson) {
		this.khrPerson = khrPerson;
	}

	public List<KhxtPerformancePoint> getPerformancePoints() {
		return performancePoints;
	}

	public void setPerformancePoints(List<KhxtPerformancePoint> performancePoints) {
		this.performancePoints = performancePoints;
	}

}
