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
 * @ClassName: KhxtItem
 * @Description: 指标定义
 * @author qianguobing
 * @date 2018年3月13日 下午4:33:46
 */
@Entity
@Table(name = "reps_khxt_item")
public class KhxtItem extends IdEntity implements Serializable {

	private static final long serialVersionUID = -4949689189823390197L;
	
	/** 所属分类ID */
	@Column(name = "category_id", nullable = false, length = 32, insertable=false, updatable=false)
	private String categoryId;

	/** 所属分类 */
	@JsonIgnore
    @ManyToOne(cascade = {})
    @JoinColumn(name = "category_id")
    private KhxtCategory khxtCategory;

	/** 指标名称 */
	@Column(name = "name", length = 60)
	private String name;

	/** 分数 */
	@Column(name = "point")
	private Double point;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public KhxtCategory getKhxtCategory() {
		return khxtCategory;
	}

	public void setKhxtCategory(KhxtCategory khxtCategory) {
		this.khxtCategory = khxtCategory;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
}
	
