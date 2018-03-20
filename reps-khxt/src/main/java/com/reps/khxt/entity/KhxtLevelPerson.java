package com.reps.khxt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;
import com.reps.system.entity.Organize;



/**
 * @ClassName: KhxtLevelPerson
 * @Description: 级别人员
 * @author qianguobing
 * @date 2018年3月18日 上午11:16:49
 */
@Entity
@Table(name = "reps_khxt_level_person")
public class KhxtLevelPerson extends IdEntity implements Serializable {

	private static final long serialVersionUID = 2543085571438556346L;
	
	/** 级别ID */
	@Column(name = "level_id", nullable = false, length = 32, insertable = false, updatable = false)
	private String levelId;
	
	/** 级别ID */
	@JsonIgnore
    @ManyToOne(cascade = {})
    @JoinColumn(name = "level_id")
    private KhxtLevel khxtLevel;

	/** 人员ID */
	@Column(name = "person_id", nullable = false, length = 32)
	private String personId;

	/** 姓名 */
	@Column(name = "person_name", length = 36)
	private String personName;
	
	/** 性别 */
	@Column(name = "person_sex", length = 1)
	private String personSex;
	
	/** 机构 */
	@JsonIgnore
    @ManyToOne(cascade = {})
	@JoinColumn(name = "organize_id")
	private Organize organize;
	
	@Transient
	@JsonIgnore
	private String personIds;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public KhxtLevel getKhxtLevel() {
		return khxtLevel;
	}

	public void setKhxtLevel(KhxtLevel khxtLevel) {
		this.khxtLevel = khxtLevel;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonSex() {
		return personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	public Organize getOrganize() {
		return organize;
	}

	public void setOrganize(Organize organize) {
		this.organize = organize;
	}

	public String getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

}
	
