package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;


/**
 * @ClassName: KhxtLevel
 * @Description: 级别设置
 * @author qianguobing
 * @date 2018年3月13日 下午4:33:46
 */
@Entity
@Table(name = "reps_khxt_level")
public class KhxtLevel extends IdEntity implements Serializable {

	private static final long serialVersionUID = 2543085571438556346L;

	/** 级别名称 */
	@Column(name = "name", length = 15)
	private String name;

	/** 级别 */
	@Column(name = "level")
	private Short level;
	
	/** 级别权限 */
	@Column(name = "power")
	private Short power;
	
	@Transient
	@JsonIgnore
	private String personNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getLevel() {
		return level;
	}

	public void setLevel(Short level) {
		this.level = level;
	}

	public Short getPower() {
		return power;
	}

	public void setPower(Short power) {
		this.power = power;
	}

	public String getPersonNames() {
		return personNames;
	}

	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}
	
}
	
