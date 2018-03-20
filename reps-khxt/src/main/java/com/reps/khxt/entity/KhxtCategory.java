package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.reps.core.orm.IdEntity;


/**
 * @ClassName: KhxtCategory
 * @Description: 指标类别
 * @author qianguobing
 * @date 2018年3月13日 下午4:33:46
 */
@Entity
@Table(name = "reps_khxt_category")
public class KhxtCategory extends IdEntity implements Serializable {

	private static final long serialVersionUID = 7951341876197753153L;
	
	/** 分类名称 */
	@Column(name = "name", length = 20)
	private String name;

	/** 备注 */
	@Column(name = "remark", length = 200)
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
