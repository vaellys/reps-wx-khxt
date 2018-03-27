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
import javax.persistence.Table;

import org.hibernate.sql.Insert;

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

	/** 可和年（季/月） */
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

	/** 添加时间 */
	@Column(name = "add_time")
	private Date addTime;

	/** 添加人用户ID */
	@Column(name = "add_user_id", length = 32)
	private String userId;

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "reps_khxt_performance_item", joinColumns = {
			@JoinColumn(name = "performance_id") }, inverseJoinColumns = { @JoinColumn(name = "item_id") })
	private Set<KhxtItem> item;

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

}
