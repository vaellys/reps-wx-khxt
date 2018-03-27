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
 * 
 * @ClassName: KhxPerformancePoint
 * @Description: 个人考核评分
 * @author qianguobing
 * @date 2018年3月24日 下午5:17:36
 */
@Entity
@Table(name = "reps_khxt_performance_point")
public class KhxtPerformancePoint extends IdEntity implements Serializable {

	private static final long serialVersionUID = -4949689189823390197L;
	
	/** 人员名单ID */
	@Column(name = "member_id", nullable = false, length = 32, insertable=false, updatable=false)
	private String memberId;

	/** 人员名单*/
	@JsonIgnore
    @ManyToOne(cascade = {})
    @JoinColumn(name = "member_id")
    private KhxtPerformanceMembers khxtPerformanceMembers;
	
	/** 人员名单ID */
	@Column(name = "item_id", nullable = false, length = 32, insertable=false, updatable=false)
	private String itemId;
	
	/** 指标ID*/
	@JsonIgnore
    @ManyToOne(cascade = {})
    @JoinColumn(name = "item_id")
    private KhxtItem khxtItem;

	/** 分数 */
	@Column(name = "point")
	private Double point;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public KhxtPerformanceMembers getKhxtPerformanceMembers() {
		return khxtPerformanceMembers;
	}

	public void setKhxtPerformanceMembers(KhxtPerformanceMembers khxtPerformanceMembers) {
		this.khxtPerformanceMembers = khxtPerformanceMembers;
	}

	public KhxtItem getKhxtItem() {
		return khxtItem;
	}

	public void setKhxtItem(KhxtItem khxtItem) {
		this.khxtItem = khxtItem;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
	
