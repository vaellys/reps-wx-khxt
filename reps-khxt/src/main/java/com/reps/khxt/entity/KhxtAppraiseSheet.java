package com.reps.khxt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 考核表
 * 
 * @author ：Alex
 * @date 2018年3月22日
 */
@Entity
@Table(name = "reps_khxt_appraise_sheet")
public class KhxtAppraiseSheet extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1860565348436086612L;

	/** 名称 */
	@Column(name = "name", length = 50)
	private String name;

	/** 权重ID */
	@Column(name = "weight_id", length = 32, insertable = false, updatable = false)
	private String weight;

	/** 权重ID */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "weight_id")
	private KhxtLevelWeight levelWeight;

	/** 考核时段 */
	@Column(name = "phase", length = 10)
	private String phase;

	/** 考核年（季/月） */
	@Column(name = "season", length = 6)
	private String season;

	/** 考核人级别ID */
	@Column(name = "khr_level_ids", length = 500)
	private String khrId;


	/** 被考核人级别ID */
	@Column(name = "bkhr_level_id", length = 32, insertable = false, updatable = false)
	private String bkhrId;

	/** 被考核人级别 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "bkhr_level_id")
	private KhxtLevel bkhr;

	/** 被考核人总数 */
	@Column(name = "bkhr_count")
	private Integer bkhrCount;

	/** 填报开始时间 */
	@Column(name = "begin_date", length = 16)
	private String beginDate;

	/** 截止填报时间 */
	@Column(name = "end_date", length = 16)
	private String endEate;

	/** 添加时间 */
	@Column(name = "add_time")
	private Date addTime;

	/** 添加人用户ID */
	@Column(name = "add_user_id", length = 32)
	private String userId;

	/** 是否是考核人 */
	@JsonIgnore
	@Transient
	private boolean checkKhr;

	/**
	 * 是否已完成打分
	 */
	@JsonIgnore
	@Transient
	private boolean checkCompletedMarking;

	@Transient
	@JsonIgnore
	private String weightDisplay;

	/** 考核进度 0：未完成 1：已完成 2：已结束 */
	@Column(name = "progress", length = 32)
	private Short progress;

	/**
	 * 进度情况
	 */
	@Column(name = "result", length = 500)
	private String result;

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "reps_khxt_performance_item", joinColumns = {
			@JoinColumn(name = "performance_id") }, inverseJoinColumns = { @JoinColumn(name = "item_id") })
	private Set<KhxtItem> item;

	@JsonIgnore
	@OneToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sheet_id")
	private List<KhxtKhrProcess> khrProcessList;

	/** 查询时用 */
	@Transient
	@JsonIgnore
	private String khrPersonId;

	/** 查询打分状态 */
	@Transient
	@JsonIgnore
	private Integer status;

	/** 考核人级别显示 */
	@Transient
	@JsonIgnore
	private String levelDisplay;
	/** 考核人级别名称 */
	@Transient
	@JsonIgnore
	private String khrName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public KhxtLevelWeight getLevelWeight() {
		return levelWeight;
	}

	public void setLevelWeight(KhxtLevelWeight levelWeight) {
		this.levelWeight = levelWeight;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getKhrId() {
		return khrId;
	}

	public void setKhrId(String khrId) {
		this.khrId = khrId;
	}

	public String getBkhrId() {
		return bkhrId;
	}

	public void setBkhrId(String bkhrId) {
		this.bkhrId = bkhrId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public KhxtLevel getBkhr() {
		return bkhr;
	}

	public void setBkhr(KhxtLevel bkhr) {
		this.bkhr = bkhr;
	}

	public Set<KhxtItem> getItem() {
		return item;
	}

	public void setItem(Set<KhxtItem> item) {
		this.item = item;
	}

	public Integer getBkhrCount() {
		return bkhrCount;
	}

	public void setBkhrCount(Integer bkhrCount) {
		this.bkhrCount = bkhrCount;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndEate() {
		return endEate;
	}

	public void setEndEate(String endEate) {
		this.endEate = endEate;
	}

	public Short getProgress() {
		return progress;
	}

	public void setProgress(Short progress) {
		this.progress = progress;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isCheckKhr() {
		return checkKhr;
	}

	public void setCheckKhr(boolean checkKhr) {
		this.checkKhr = checkKhr;
	}

	public boolean isCheckCompletedMarking() {
		return checkCompletedMarking;
	}

	public void setCheckCompletedMarking(boolean checkCompletedMarking) {
		this.checkCompletedMarking = checkCompletedMarking;
	}

	public String getWeightDisplay() {
		return weightDisplay;
	}

	public void setWeightDisplay(String weightDisplay) {
		this.weightDisplay = weightDisplay;
	}

	public List<KhxtKhrProcess> getKhrProcessList() {
		return khrProcessList;
	}

	public void setKhrProcessList(List<KhxtKhrProcess> khrProcessList) {
		this.khrProcessList = khrProcessList;
	}

	public String getKhrPersonId() {
		return khrPersonId;
	}

	public void setKhrPersonId(String khrPersonId) {
		this.khrPersonId = khrPersonId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLevelDisplay() {
		return levelDisplay;
	}

	public void setLevelDisplay(String levelDisplay) {
		this.levelDisplay = levelDisplay;
	}

	public String getKhrName() {
		return khrName;
	}

	public void setKhrName(String khrName) {
		this.khrName = khrName;
	}
	
}
