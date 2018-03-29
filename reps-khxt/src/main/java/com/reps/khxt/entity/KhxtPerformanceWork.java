package com.reps.khxt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 被考核人工作绩效
 * 
 * @author ：Alex
 * @date 2018年3月27日
 */
@Entity
@Table(name = "reps_khxt_performance_work")
public class KhxtPerformanceWork extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4248398723512435250L;

	@Column(name = "sheet_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String sheetId;

	/** 考核表 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "sheet_id")
	private KhxtAppraiseSheet sheet;

	/** 上报人ID */
	@Column(name = "person_id", length = 32)
	private String personId;

	/** 计划 */
	@Column(name = "planning", length = 500)
	private String planning;

	/** 执行 */
	@Column(name = "execution", length = 500)
	private String execution;

	/** 添加时间 */
	@Column(name = "add_time")
	private Date addTime;

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	public KhxtAppraiseSheet getSheet() {
		return sheet;
	}

	public void setSheet(KhxtAppraiseSheet sheet) {
		this.sheet = sheet;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPlanning() {
		return planning;
	}

	public void setPlanning(String planning) {
		this.planning = planning;
	}

	public String getExecution() {
		return execution;
	}

	public void setExecution(String execution) {
		this.execution = execution;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
